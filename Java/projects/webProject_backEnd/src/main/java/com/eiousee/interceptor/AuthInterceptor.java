package com.eiousee.interceptor;

import com.eiousee.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("请求开始：{}", request.getRequestURI());

        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.info("未找到Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        try {
            // 验证Token
            JwtUtils.parseJwt(token);
        } catch (Exception e) {
            log.info("Token验证失败");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
