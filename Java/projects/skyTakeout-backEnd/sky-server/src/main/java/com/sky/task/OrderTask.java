package com.sky.task;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderService orderService;

    /**
     * 定时任务，处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void overdueOrderHandlerTask() {
        log.info("开始处理超时订单");
        // 获取所有订单时间在当前时间15分钟之前，且订单状态为待付款的订单
        // 构造查询条件
        Orders orders = Orders.builder()
                .status(Orders.PENDING_PAYMENT)
                .orderTime(LocalDateTime.now().plusMinutes(-15))
                .build();
        // 查询
        List<Orders> ordersList = orderService.getOrdersByConditions(orders);
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                order.setCancelReason("订单超时未支付，系统自动取消");
                orderService.cancelByAdmin(order);
            }
        }
        int size = 0;
        if (ordersList != null && !ordersList.isEmpty())
            size = ordersList.size();
        log.info("处理超时订单完成，处理的订单数量: {}", size);
    }

    /**
     * 定时处理每日派送中但未完成订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void completeOrderHandlerTask() {
        log.info("开始处理派送中但未完成订单");
        // 获取所有一小时前派送中但未完成订单
        Orders orders = Orders.builder()
                .status(Orders.DELIVERY_IN_PROGRESS)
                .orderTime(LocalDateTime.now().plusHours(-1))
                .build();
        // 查询
        List<Orders> ordersList = orderService.getOrdersByConditions(orders);
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                orderService.complete(order.getId());
            }
        }
        int size = 0;
        if (ordersList != null && !ordersList.isEmpty())
            size = ordersList.size();
        log.info("处理派送中但未完成订单完成，处理的订单数量: {}", size);
    }
}
