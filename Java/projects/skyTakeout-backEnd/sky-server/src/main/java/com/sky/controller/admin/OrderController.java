package com.sky.controller.admin;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult<OrderVO>> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索：{}", ordersPageQueryDTO);
        PageResult<OrderVO> pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("订单详情")
    public Result<Orders> details(@PathVariable Long id) {
        log.info("订单详情：{}", id);
        return Result.success(orderService.getOrderDetail(id));
    }

    /**
     * 统计订单数据
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("统计订单数据")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        log.info("统计订单数据：{}", orderStatisticsVO);
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单取消
     *
     * @param orders
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<String> cancel(@RequestBody Orders orders) {
        log.info("取消订单：{}", orders);
        orderService.cancelByAdmin(orders);
        return Result.success();
    }

    /**
     * 接单
     *
     * @param ordersDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<String> confirm(@RequestBody OrdersDTO ordersDTO) {
        log.info("接单：{}", ordersDTO);
        orderService.confirm(ordersDTO.getId());
        return Result.success();
    }

    /**
     * 拒单
     *
     * @param orders
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<String> rejection(@RequestBody Orders orders) {
        log.info("拒单：{}", orders);
        orderService.rejection(orders);
        return Result.success();
    }

    /**
     * 派单
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派单")
    public Result<String> delivery(@PathVariable Long id) {
        log.info("派单：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 订单完成
     *
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("订单完成")
    public Result<String> complete(@PathVariable Long id) {
        log.info("订单完成：{}", id);
        orderService.complete(id);
        return Result.success();
    }
}
