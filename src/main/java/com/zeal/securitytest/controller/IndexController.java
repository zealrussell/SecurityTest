package com.zeal.securitytest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/26 12:32
 * @Version 1.0
 */
@RestController
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        System.out.println("hello index");
        return "hello, index";
    }
}
