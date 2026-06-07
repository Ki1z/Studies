package com.sky.controller.admin;

import com.sky.service.TestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/test")
public class TestController {

    private final TestService testService;

    @GetMapping
    public void test() {
        testService.test();
    }
}
