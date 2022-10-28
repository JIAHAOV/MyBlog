package com.study.reproduce.confiig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom.cos")
public class TencentCosProperties {
    private String secretId;
    private String secretKey;
    private String bucketName;
    private String region;
}
