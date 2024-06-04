package com.zeal.securitytest.controller;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/29 15:08
 * @Version 1.0
 */
@Controller
public class VerifycodeController {

    private final Producer producer;

    @Autowired
    public VerifycodeController(Producer producer) {
        this.producer = producer;
    }

//    @GetMapping("/vc.jpg")
//    public String getVerifyCode(HttpSession session) throws IOException {
//        // 1. 生成验证码
//        String code = producer.createText();
//        // 2. 放入session
//        session.setAttribute("kaptcha", code);
//        // 3. 生成图片
//        BufferedImage image = producer.createImage(code);
//        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
//        ImageIO.write(image, "jpg", fos);
//        // 4. 返回base64
//        String s = Base64.encodeBase64String(fos.toByteArray());
//        return s;
//    }
    @RequestMapping("/vc.jpg")
    public void verifyCode(HttpServletResponse response, HttpSession session) throws IOException {
        // 1. 生成验证码
        String code = producer.createText();
        // 2. 放入session
        session.setAttribute("kaptcha", code);
        // 3. 生成图片
        BufferedImage image = producer.createImage(code);
        // 4.
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);

    }
}
