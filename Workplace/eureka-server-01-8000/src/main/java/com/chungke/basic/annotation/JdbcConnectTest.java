//package com.chungke.basic.annotation;
//
//import java.lang.reflect.Method;
//
//public class JdbcConnectTest {
//    public static void main(String[] args) throws ClassNotFoundException {
//        Class<JdbcConnectExp> clazz = (Class<JdbcConnectExp>) Class.forName("com.chungke.basic.annotation.JdbcConnectExp");
//        Method[] declaredMethods = clazz.getDeclaredMethods();
//        for(Method m:declaredMethods){
//            JdbcConnectExp annotation = m.getAnnotation(JdbcConnectExp.class);
//            String host = annotation.host();
//            String port = annotation.port();
//            System.out.println(host+":"+port);
//        }
//    }
//
//}
//
//class  JdbcConnectExp extends  JdbcConnectVo{
//
//}
