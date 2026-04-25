package com.eiousee.controller;

import com.eiousee.pojo.Result;
import com.eiousee.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        log.info("查询员工职位统计数据");
        return Result.success(reportService.getEmpJobData());
    }

    @GetMapping("/empDeptData")
    public Result getEmpDeptData() {
        log.info("查询部门职位统计数据");
        return Result.success(reportService.getEmpDeptData());
    }

    @GetMapping("/empSexData")
    public Result getEmpSexData() {
        log.info("查询员工性别统计数据");
        return Result.success(reportService.getEmpSexData());
    }

    @GetMapping("/classSizeData")
    public Result getClassSizeData() {
        log.info("查询班级人数统计数据");
        return Result.success(reportService.getClassSizeData());
    }

    @GetMapping("/educationData")
    public Result getEducationData() {
        log.info("查询学历人数统计数据");
        return Result.success(reportService.getEducationData());
    }
}
