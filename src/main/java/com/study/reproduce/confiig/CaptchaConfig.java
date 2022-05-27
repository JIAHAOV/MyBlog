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
    private String textProducerCharLength = "4";
    private String textProducerFontSize = "30";
    private String imageWidth = "150";
    private String imageHeight = "40";
    private String sessionKey = "verifyCode";

    /**
     * 配置验证码属性
     * @return DefaultKaptcha
     */
    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", border);
        properties.setProperty("kaptcha.textproducer.font.color", textProducerFontColor);
        properties.setProperty("kaptcha.textproducer.char.space", textProducerCharSpace);
        properties.setProperty("kaptcha.textproducer.font.size", textProducerFontSize);
        properties.setProperty("kaptcha.image.width", imageWidth);
        properties.setProperty("kaptcha.image.height", imageHeight);
        properties.setProperty("kaptcha.session.key", sessionKey);
        properties.setProperty("kaptcha.textproducer.char.length", textProducerCharLength);
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
