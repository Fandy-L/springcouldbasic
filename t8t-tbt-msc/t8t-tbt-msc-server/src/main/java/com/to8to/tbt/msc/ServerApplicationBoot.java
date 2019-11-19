package com.to8to.tbt.msc;

import com.to8to.sc.SpringBootStarterAnnotation;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 应用服务启动类（环境变量：env = DEV）
 *
 * @author NIGEL.WANG
 */
@SpringBootStarterAnnotation
@EnableSwagger2
@EnableMongoRepositories(basePackages = "com.to8to.tbt.msc.repository.mongo")
public class ServerApplicationBoot {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerApplicationBoot.class).run(args);
    }
}