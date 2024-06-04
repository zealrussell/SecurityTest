package com.zeal.securitytest.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/29 14:52
 * @Version 1.0
 */
@Configuration
public class KaptchaConfig {

    // 配置验证码参数
    @Bean
    public Producer kaptcha() {
        Properties properties = new Properties();
        // 验证码宽度
        properties.setProperty("kaptcha.image.width", "150");
        // 验证码长度
        properties.setProperty("kaptcha.image.height", "50");
        // 验证码字符串
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
