package com.study.reproduce.confiig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom.my-blog.captcha")
public class CaptchaProperties {
    private String border = "no";
    private String textProducerFontColor = "black";
    private String textProducerCharSpace = "5";
    private String textProducerCharLength = "4";
    private String textProducerFontSize = "30";
    private String imageWidth = "150";
    private String imageHeight = "40";
    private String sessionKey = "verifyCode";
}
