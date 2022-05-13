package com.study.reproduce.confiig;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "custom.my-blog.captcha")
public class CaptchaConfig {
    private String border = "no";
    private String textProducerFontColor = "black";
    private String textProducerCharSpace = "5";
    private String textProducerFontSize = "30";
    private String imageWidth = "150";
    private String imageHeight = "40";
    private String sessionKey = "verifyCode";

    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.put("kaptcha.border", border);
        properties.put("kaptcha.textproducer.font.color", textProducerFontColor);
        properties.put("kaptcha.textproducer.char.space", textProducerCharSpace);
        properties.put("kaptcha.textproducer.font.size", textProducerFontSize);
        properties.put("kaptcha.image.width", imageWidth);
        properties.put("kaptcha.image.height", imageHeight);
        properties.put("kaptcha.session.key", sessionKey);
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
