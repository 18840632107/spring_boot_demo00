package com.caiyibin.spring_boot_demo00;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//核心注解，开启自动配置，是一个组合注解，包括SpringBootConfiguration、Configuration等等
@SpringBootApplication
public class SpringBootDemo00Application {
    //入口类
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo00Application.class, args);
    }
}
