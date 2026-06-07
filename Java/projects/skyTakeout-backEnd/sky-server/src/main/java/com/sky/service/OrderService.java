package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.models.auth.In;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    LocalDateTime payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return 订单分页数据
     */
    PageResult<Orders> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单详情
     * @param id
     * @return
     */
    Orders getOrderDetail(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 分页条件搜索订单
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计订单数据
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 管理端取消订单
     * @param orders
     */
    void cancelByAdmin(Orders orders);

    /**
     * 接单
     * @param id
     */
    void confirm(Long id);

    /**
     * 拒单
     * @param orders
     */
    void rejection(Orders orders);

    /**
     * 派送订单
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);

    /**
     * 条件查询订单
     * @param orders
     * @return
     */
    List<Orders> getOrdersByConditions(Orders orders);

    /**
     * 催单
     * @param id
     */
    void reminder(Long id);
}
