package com.study.reproduce.confiig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticsearchProperties {
    private int port;
    private String host;
    private String username;
    private String password;
}
