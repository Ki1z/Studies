package com.eiousee.filter;

import com.eiousee.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取URI
        String uri = request.getRequestURI();
        log.info("请求URI：{}", uri);
        // 判断是否是登录请求
        if (uri.contains("login")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 获取Token
        String token = request.getHeader("Token");
        // 验证Token
        if (token == null || token.isEmpty()) {
            log.info("未找到Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            JwtUtils.parseJwt(token);
        } catch (Exception e) {
            log.info("Token验证失败");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 验证通过，放行
        filterChain.doFilter(request, response);
    }
}
