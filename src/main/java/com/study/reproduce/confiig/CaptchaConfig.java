package com.study.reproduce.confiig;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.study.reproduce.confiig.properties.CaptchaProperties;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Data
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {
    private String border;
    private String textProducerFontColor;
    private String textProducerCharSpace;
    private String textProducerCharLength;
    private String textProducerFontSize;
    private String imageWidth;
    private String imageHeight;
    private String sessionKey;

    private CaptchaProperties captchaProperties;

    public CaptchaConfig(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
        this.border = captchaProperties.getBorder();
        this.textProducerFontColor = captchaProperties.getTextProducerFontColor();
        this.textProducerCharSpace = captchaProperties.getTextProducerCharSpace();
        this.textProducerCharLength = captchaProperties.getTextProducerCharLength();
        this.textProducerFontSize = captchaProperties.getTextProducerFontSize();
        this.imageWidth = captchaProperties.getImageWidth();
        this.imageHeight = captchaProperties.getImageHeight();
        this.sessionKey = captchaProperties.getSessionKey();
    }

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
