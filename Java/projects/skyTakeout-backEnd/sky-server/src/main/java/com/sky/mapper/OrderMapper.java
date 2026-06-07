package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     * @return
     */
    Integer insert(Orders orders);

    /**
     * 插入订单明细数据
     * @param orderDetails
     * @return
     */
    Integer insertDetails(List<OrderDetail> orderDetails);

    /**
     * 根据订单号查询订单数据
     * @param orderNumber
     * @return
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    List<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单详情
     * @param id
     * @return
     */
    List<OrderDetail> getOrderDetailByOrderId(Long id);

    /**
     * 根据id查询订单数据
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 修改订单信息
     * @param orders
     * @return
     */
    Integer update(Orders orders);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 条件搜索订单
     * @param orders
     * @return
     */
    List<Orders> getOrdersByConditions(Orders orders);
}
