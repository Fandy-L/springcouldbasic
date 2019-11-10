package com.chungke.provider018081;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class Provider018081Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider018081Application.class, args);
    }

}
