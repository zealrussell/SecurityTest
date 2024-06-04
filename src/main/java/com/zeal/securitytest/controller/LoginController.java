package com.zeal.securitytest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/26 16:08
 * @Version 1.0
 */
@Controller
public class LoginController {

    @RequestMapping("/login.html")
    public String login(){
        System.out.println("hello controller");
        return "login";
    }
}
