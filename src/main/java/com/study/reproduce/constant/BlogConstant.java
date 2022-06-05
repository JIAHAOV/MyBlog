package com.study.reproduce.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "custom.my-blog.blog")
public class BlogConstant {
    public static int maxContentLength = 150000;
    public static int maxUrlLength = 150;
    public static int maxTitleLength = 150;
    public static int maxTagsLength = 150;
}
