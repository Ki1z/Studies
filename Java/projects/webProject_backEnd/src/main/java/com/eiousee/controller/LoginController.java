package com.eiousee.controller;

import com.eiousee.pojo.LoginInfo;
import com.eiousee.pojo.LoginPojo;
import com.eiousee.pojo.LoginResult;
import com.eiousee.pojo.Result;
import com.eiousee.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public Result login(@RequestBody LoginInfo loginInfo) {
        log.info("登录，用户信息：{}", loginInfo);
        LoginResult user = loginService.login(loginInfo);
        return user == null ? Result.error("用户名或密码错误") : Result.success(user);
    }
}