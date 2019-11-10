package com.chungke.provider018081;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Server018000Application {

    public static void main(String[] args) {
        SpringApplication.run(Server018000Application.class, args);
    }

}
