package com.study.reproduce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ReproduceApplication {
    /*
     * 解决druid 日志报错：discard long time none received connection:xxx
     */
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
    public static void main(String[] args) {
        SpringApplication.run(ReproduceApplication.class, args);
    }

}
