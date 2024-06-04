package com.zeal.securitytest.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeal.securitytest.exception.KaptchaNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import java.io.IOError;
import java.io.IOException;
import java.util.Map;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/29 15:53
 * @Version 1.0
 */
// 自定义验证码filter
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {
    public static final String FORM_KAPTCHA_KEY = "kaptcha";

    private String kaptchaParameter = FORM_KAPTCHA_KEY;
    public String getKaptchaParameter() {
        return kaptchaParameter;
    }
    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST"))
            throw new AuthenticationServiceException("Authentication Method not equal: " + request.getMethod());

        try {
            // 1. 获取数据
            Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String kaptcha = userInfo.get(getKaptchaParameter());
            String username = userInfo.get(getUsernameParameter());
            String password = userInfo.get(getPasswordParameter());
            // 2. 从 session 中获取验证码
            String sessionVerifyCode = (String) request.getSession().getAttribute("kaptcha");
            if (!ObjectUtils.isEmpty(kaptcha) && ! ObjectUtils.isEmpty(sessionVerifyCode)
                    && kaptcha.equalsIgnoreCase(sessionVerifyCode)) {
                // 3. 获取用户名\密码\认证
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, token);
                return this.getAuthenticationManager().authenticate(token);
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        throw new KaptchaNotMatchException("验证码不匹配!");
    }
}
