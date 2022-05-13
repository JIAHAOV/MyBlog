package com.study.reproduce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReproduceApplication {
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
    public static void main(String[] args) {
        SpringApplication.run(ReproduceApplication.class, args);
    }

}
