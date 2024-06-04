package com.zeal.securitytest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/19 12:54
 * @Version 1.0
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello, zeal!!");
        // 1.获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        System.out.println("身份信息: " + user.getUsername());
        System.out.println("密码信息: " + user.getPassword());
        System.out.println("权限信息: " + authentication.getAuthorities());
        return "hello, zeal security!";
    }
}
