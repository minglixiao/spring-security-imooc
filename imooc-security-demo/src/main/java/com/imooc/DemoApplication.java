package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * spring boot项目启动类
 */
@SpringBootApplication
// restful接口声明注解
@RestController
// spring  boot  定时器
@EnableScheduling
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // 我们先创建一个接口，测试一下。
    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }
}