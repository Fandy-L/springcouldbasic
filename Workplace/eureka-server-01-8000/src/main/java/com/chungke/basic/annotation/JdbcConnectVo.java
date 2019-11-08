package com.chungke.basic.annotation;


public class JdbcConnectVo {
    @JdbcBasic(host = "localhost",port = "3661")
    public void getConnetion(){
    }

    @JdbcBasic
    public void getConnetion1(){
    }
}
