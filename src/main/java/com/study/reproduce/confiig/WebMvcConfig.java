package com.study.reproduce.confiig;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.study.reproduce.interceptor.AdminInterceptor;
import com.study.reproduce.interceptor.VisitorInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new VisitorInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**");
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
    }
}
