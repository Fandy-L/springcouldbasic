package com.chungke.basic.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyHelloWord {


    @GetMapping("hello")
    public String hello(){
        Logger logger = LoggerFactory.getLogger(MyHelloWord.class);
        logger.info("日志启动啦....");

        return "hello to8to!";
    }
}
