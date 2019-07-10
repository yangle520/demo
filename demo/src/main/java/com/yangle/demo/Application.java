package com.yangle.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ServletComponentScan(basePackages = "com.yangle.demo")
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
