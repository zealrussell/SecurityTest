package com.zeal.securitytest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/27 11:51
 * @Version 1.0
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "登录失败" + exception.getMessage());
        result.put("status", 500);
        response.setContentType("application/json;charset=UTF-8");

        String s = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(s);   // 把后端的java对象转换成string返回给前端
    }
}
