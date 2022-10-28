package com.study.reproduce;

import com.study.reproduce.confiig.WebSiteStyleConfig;
import com.study.reproduce.constant.WebSiteConfigId;
import com.study.reproduce.mapper.WebSiteConfigMapper;
import com.study.reproduce.model.domain.WebSiteConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties
@SpringBootApplication
public class ReproduceApplication {

    /*
     * 解决druid 日志报错：discard long time none received connection:xxx
     */
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ReproduceApplication.class, args);
        WebSiteConfigMapper webSiteConfigMapper = run.getBean("webSiteConfigMapper", WebSiteConfigMapper.class);
        WebSiteConfig webSiteConfig = webSiteConfigMapper.selectById(WebSiteConfigId.WEBSITE_STYLE);
        WebSiteStyleConfig.changeStyle(webSiteConfig.getConfigValue());
    }

}
