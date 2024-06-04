package com.zeal.securitytest.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/29 16:11
 * @Version 1.0
 */
public class KaptchaNotMatchException extends AuthenticationException {
    public KaptchaNotMatchException(String msg) {
        super(msg);
    }
    public KaptchaNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
