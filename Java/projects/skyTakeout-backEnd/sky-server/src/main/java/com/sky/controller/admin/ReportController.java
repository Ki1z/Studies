package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "报表管理")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 统计销量Top10
     * @return 销量Top10
     */
    @GetMapping("/top10")
    @ApiOperation("销量前十")
    public Result<SalesTop10ReportVO> getSalesTop10(String begin, String end) {
        SalesTop10ReportVO report = reportService.getSalesTop10(LocalDate.parse(begin), LocalDate.parse(end));
        log.info("查询销量前十数据：{}", report);
        return Result.success(report);
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return 用户统计数据
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(String begin, String end) {
        UserReportVO report = reportService.getUserStatistics(LocalDate.parse(begin), LocalDate.parse(end));
        log.info("查询用户统计数据：{}", report);
        return Result.success(report);
    }

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(String begin, String end) {
        TurnoverReportVO report = reportService.getTurnoverStatistics(LocalDate.parse(begin), LocalDate.parse(end));
        log.info("营业额数据：{}", report);
        return Result.success(report);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderStatistics(String begin, String end) {
        OrderReportVO report = reportService.getOrderStatistics(LocalDate.parse(begin), LocalDate.parse(end));
        log.info("订单数据：{}", report);
        return Result.success(report);
    }

    /**
     * 数据导出
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation("导出数据")
    public void export(HttpServletResponse response) {
        reportService.export(response);
    }
}
