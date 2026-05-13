package com.eiousee.controller;

import com.eiousee.pojo.Result;
import com.eiousee.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public Result getLogs(Integer page, Integer pageSize) {
        log.info("获取日志列表");
        return Result.success(logService.getLogs(page, pageSize));
    }
}
