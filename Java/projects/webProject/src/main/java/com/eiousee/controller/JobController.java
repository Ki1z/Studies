package com.eiousee.controller;

import com.eiousee.pojo.Result;
import com.eiousee.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public Result getJob() {
        log.info("查询职位列表");
        return Result.success(jobService.getJobList());
    }
}
