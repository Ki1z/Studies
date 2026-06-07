package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.BaseException;
import com.sky.exception.FormValueIsNullException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.AddressBookService;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.service.UserService;
import com.sky.utils.LocationUtil;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private LocationUtil locationUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        Integer payMethod = ordersSubmitDTO.getPayMethod();
        LocalDateTime estimatedDeliveryTime = ordersSubmitDTO.getEstimatedDeliveryTime();
        Integer deliveryStatus = ordersSubmitDTO.getDeliveryStatus();
        Integer tablewareNumber = ordersSubmitDTO.getTablewareNumber();
        Integer tablewareStatus = ordersSubmitDTO.getTablewareStatus();
        Integer packAmount = ordersSubmitDTO.getPackAmount();
        BigDecimal amount = ordersSubmitDTO.getAmount();

        // 参数校验
        // 地址簿
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBookId == null ||  addressBook == null)
            throw new FormValueIsNullException(MessageConstant.UNKNOWN_ADDRESS);
        // 支付方式
        // 1为微信，2为支付宝
        if (!Objects.equals(payMethod, 1) && !Objects.equals(payMethod, 2))
            throw new FormValueIsNullException(MessageConstant.UNKNOWN_PAY_METHOD);
        // 预计送达时间
        if (estimatedDeliveryTime == null)
            throw new FormValueIsNullException(MessageConstant.INCORRECT_ESTIMATED_DELIVERY_TIME);
        // 配送状态
        if (!Objects.equals(deliveryStatus, 1) && !Objects.equals(deliveryStatus, 0))
            throw new FormValueIsNullException(MessageConstant.UNKNOWN_DELIVERY_STATUS);
        // 餐具数量
        if (tablewareNumber == null || tablewareNumber < 0)
            throw new FormValueIsNullException(MessageConstant.INCORRECT_TABLEWARE_NUMBER);
        // 餐具数量状态
        if (!Objects.equals(tablewareStatus, 1) && !Objects.equals(tablewareStatus, 0))
            throw new FormValueIsNullException(MessageConstant.UNKNOWN_TABLEWARE_STATUS);
        // 打包费
        if (packAmount == null || packAmount < 0)
            throw new FormValueIsNullException(MessageConstant.INCORRECT_PACK_AMOUNT);
        // 总金额
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new FormValueIsNullException(MessageConstant.INCORRECT_AMOUNT);
        // 购物车
        Long userId = BaseContext.getCurrentId();
        if (userId == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_RECOGNIZE_USER);
        List<ShoppingCart> shoppingCartList = shoppingCartService.getByUserId(userId);
        if (shoppingCartList == null || shoppingCartList.isEmpty())
            throw new FormValueIsNullException(MessageConstant.SHOPPING_CART_IS_EMPTY);

        // 距离校验
        String address = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail();
        String coordinate = locationUtil.getCoordinateByAddress(address);
        if (coordinate == null)
            throw new FormValueIsNullException(MessageConstant.UNKNOWN_ADDRESS);
        Long distance = locationUtil.getDistance(coordinate);
        if (distance > 5000)
            throw new OrderBusinessException(MessageConstant.ADDRESS_IS_TOO_FAR);

        // 插入数据到订单表和订单明细表
        // 订单表
        // 生成订单号 订单号生成规则：时间戳+用户id
        String number = System.currentTimeMillis() + "" + userId;
        // 获取用户数据
        User user = userService.getById(userId);
        if (user == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_RECOGNIZE_USER);
        Orders orders = Orders.builder()
                .number(number)
                .status(Orders.PENDING_PAYMENT)
                .userId(userId)
                .addressBookId(addressBookId)
                .orderTime(LocalDateTime.now())
                .payMethod(payMethod)
                .payStatus(Orders.UN_PAID)
                .amount(amount)
                .remark(ordersSubmitDTO.getRemark())
                .userName(user.getName())
                .phone(addressBook.getPhone())
                .address(address)
                .consignee(addressBook.getConsignee())
                .estimatedDeliveryTime(estimatedDeliveryTime)
                .deliveryStatus(deliveryStatus)
                .packAmount(packAmount)
                .tablewareNumber(tablewareNumber)
                .tablewareStatus(tablewareStatus)
                .build();
        // 插入订单表
        Integer count = orderMapper.insert(orders);
        if (count <= 0)
            throw new OrderBusinessException(MessageConstant.ORDER_SUBMIT_FAILED);

        // 订单明细表
        // 获取订单id
        Long orderId = orders.getId();
        if (orderId == null)
            throw new OrderBusinessException(MessageConstant.ORDER_SUBMIT_FAILED);
        // 将购物车数据转为订单明细数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetails.add(orderDetail);
        }
        // 批量插入订单明细表
        count = orderMapper.insertDetails(orderDetails);
        if (!Objects.equals(count, orderDetails.size()))
            throw new OrderBusinessException(MessageConstant.ORDER_SUBMIT_FAILED);

        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public LocalDateTime payment(OrdersPaymentDTO ordersPaymentDTO) {
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        if (orderNumber == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);

        // 检查订单是否为待支付
        Orders orders = orderMapper.getByNumber(orderNumber);
        if (!Objects.equals(orders.getStatus(), Orders.PENDING_PAYMENT))
            throw new OrderBusinessException(MessageConstant.ORDER_IS_NOT_PENDING_PAYMENT);

        // 模拟支付
        orders = Orders.builder()
                .status(Orders.TO_BE_CONFIRMED)
                .checkoutTime(LocalDateTime.now())
                .payStatus(Orders.PAID)
                .number(ordersPaymentDTO.getOrderNumber())
                .build();
        Integer count = orderMapper.update(orders);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_PAYMENT_FAILED);

        orders = orderMapper.getByNumber(orderNumber);
        if (orders == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        // 支付成功后清除购物车
        shoppingCartService.clean();

        // 向管理端发送来单提醒
        // 构造消息
        Map<String, Object> message = new HashMap<>();
        message.put("type", 1);
        message.put("orderId", orders.getId());
        message.put("content", "订单号: " + orders.getNumber());
        // 发送消息
        webSocketServer.sendToAll(JSON.toJSONString(message));

        return orders.getEstimatedDeliveryTime();
    }

    /**
     * 历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult<Orders> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 设置用户id
        Long userId = BaseContext.getCurrentId();
        ordersPageQueryDTO.setUserId(userId);

        // 获取所有订单
        Page<Orders> page = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        orderMapper.pageQuery(ordersPageQueryDTO);
        PageResult<Orders> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        // 获取所有订单对应的订单详情
        for (Orders orders : pageResult.getRecords()) {
            List<OrderDetail> orderDetails = orderMapper.getOrderDetailByOrderId(orders.getId());
            orders.setOrderDetailList(orderDetails);
        }
        return pageResult;
    }

    /**
     * 获取订单详情
     *
     * @param id
     * @return
     */
    @Override
    public Orders getOrderDetail(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);

        Orders orders = orderMapper.getById(id);
        // 获取订单对应的订单详情
        orders.setOrderDetailList(orderMapper.getOrderDetailByOrderId(id));
        return orders;
    }

    /**
     * 取消订单
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void cancel(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);

        // 获取订单信息
        Orders orders = orderMapper.getById(id);
        if (orders == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        Integer status = orders.getStatus();
        if (Objects.equals(status, Orders.PENDING_PAYMENT)) {
            // 付款前，订单状态修改为取消
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("用户取消");
        } else {
            // 付款后，订单状态修改为退款
            orders.setStatus(Orders.REFUNDING);
            orders.setCancelReason("用户退款");
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setCancelTime(LocalDateTime.now());

        Integer count = orderMapper.update(orders);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_CANCEL_FAILED);
    }

    /**
     * 再来一单
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void repetition(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);

        // 从订单详情表查询对应订单的菜品
        List<OrderDetail> orderDetails = orderMapper.getOrderDetailByOrderId(id);
        // 封装为购物车数据
        for (OrderDetail orderDetail : orderDetails) {
            ShoppingCartDTO cart = new ShoppingCartDTO();
            cart.setDishId(orderDetail.getDishId());
            cart.setSetmealId(orderDetail.getSetmealId());
            cart.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCartService.add(cart);
        }
    }

    /**
     * 条件搜索订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 获取所有订单
        Page<OrderVO> page = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        orderMapper.pageQuery(ordersPageQueryDTO);
        PageResult<OrderVO> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        // 获取所有订单对应的订单详情
        for (OrderVO orderVO : pageResult.getRecords()) {
            List<OrderDetail> orderDetails = orderMapper.getOrderDetailByOrderId(orderVO.getId());
            StringBuilder orderDishes = new StringBuilder();
            for (OrderDetail orderDetail : orderDetails) {
                orderDishes.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append(";  ");
            }
            orderVO.setOrderDishes(orderDishes.toString());
        }
        return pageResult;
    }

    /**
     * 统计订单数据
     *
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        return orderMapper.statistics();
    }

    /**
     * 管理员取消订单
     *
     * @param orders
     */
    @Override
    public void cancelByAdmin(Orders orders) {
        Long id = orders.getId();
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
        String cancelReason = orders.getCancelReason();
        if (cancelReason == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_CANELLATION_NEEDS_A_REASON);
        // 获取订单信息
        Orders ordersDB = orderMapper.getById(id);
        if (ordersDB == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 如果已经支付，则修改为退款
        if (Objects.equals(ordersDB.getPayStatus(), Orders.PAID)) {
            ordersDB.setPayStatus(Orders.REFUND);
            ordersDB.setStatus(Orders.REFUNDING);
        }
        else
            ordersDB.setStatus(Orders.CANCELLED);
        ordersDB.setCancelReason(cancelReason);
        ordersDB.setCancelTime(LocalDateTime.now());
        Integer count = orderMapper.update(ordersDB);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_CANCEL_FAILED);
    }

    /**
     * 接单
     *
     * @param id
     */
    @Override
    public void confirm(Long id) {
       if (id == null)
           throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
       Orders orders = orderMapper.getById(id);
       if (orders == null)
           throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
       // 判断订单状态是否为待接单
        if (!Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
       // 订单状态修改为接单
        orders.setStatus(Orders.CONFIRMED);
        Integer count = orderMapper.update(orders);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_CONFIRM_FAILED);
    }

    /**
     * 拒单
     *
     * @param orders
     */
    @Override
    public void rejection(Orders orders) {
        Long id = orders.getId();
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
        String rejectionReason = orders.getRejectionReason();
        if (rejectionReason == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_REJECTION_NEEDS_A_REASON);
        // 获取订单信息
        Orders ordersDB = orderMapper.getById(id);
        if (ordersDB == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 判断订单状态是否为待接单
        if (!Objects.equals(ordersDB.getStatus(), Orders.TO_BE_CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        // 订单状态修改为退款
        ordersDB.setStatus(Orders.REFUNDING);
        ordersDB.setRejectionReason(rejectionReason);
        ordersDB.setCancelTime(LocalDateTime.now());
        ordersDB.setPayStatus(Orders.REFUND);
        // 更新订单
        Integer count = orderMapper.update(ordersDB);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_REJECTION_FAILED);
    }

    /**
     * 派单
     *
     * @param id
     */
    @Override
    public void delivery(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
        Orders orders = orderMapper.getById(id);
        if (orders == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 判断订单状态是否为待派单
        if (!Objects.equals(orders.getStatus(), Orders.CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        // 订单状态修改为派单
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        Integer count = orderMapper.update(orders);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_DELIVERY_FAILED);
    }

    /**
     * 订单完成
     *
     * @param id
     */
    @Override
    public void complete(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
        Orders orders = orderMapper.getById(id);
        if (orders == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 检查订单状态
        if (!Objects.equals(orders.getStatus(), Orders.DELIVERY_IN_PROGRESS))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        // 订单状态修改为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        Integer count = orderMapper.update(orders);
        if (count == 0)
            throw new OrderBusinessException(MessageConstant.ORDER_COMPLETION_FAILED);
    }

    /**
     * 条件查询订单
     *
     * @param orders
     * @return
     */
    @Override
    public List<Orders> getOrdersByConditions(Orders orders) {
        if (orders == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
        return orderMapper.getOrdersByConditions(orders);
    }

    /**
     * 催单
     * @param id
     */
    @Override
    public void reminder(Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.ORDER_NOT_FOUND);
        // 获取订单信息
        Orders orders = orderMapper.getById(id);
        if (orders == null)
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 判断订单状态是否为待接单和待派单
        if (!Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED) && !Objects.equals(orders.getStatus(), Orders.CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        // 构造消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", 2);
        msg.put("orderId", id);
        msg.put("content", "订单号：" + orders.getNumber());
        // 发送消息
        webSocketServer.sendToAll(JSON.toJSONString(msg));
    }
}
