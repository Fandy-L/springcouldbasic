package com.chungke.basic.util;

/**
 * 动态代理实现
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ListDemo {

    public static void main(String[] args) {
        final List list = new ArrayList<>();
        Object oo = Proxy.newProxyInstance(
//			原始数据类型
                List.class.getClassLoader(),
//			定义接口
                list.getClass().getInterfaces(),
//			调用处理
                new InvocationHandler() {

                    @Override
                    public Object invoke(
//						代理对象
                            Object proxy,
//						代理方法
                            Method method,
//						代理参数
                            Object[] args) throws Throwable {
                        // TODO Auto-generated method stub
                        System.out.println("加入几个对象：");
                        Object returnValue = method.invoke(list, args);
                        System.out.println("加入完成");
                        if (method.getName().equals("size")) {
                            return 100;
                        }
                        return returnValue;
                    }
                });
        List list2 = (List)oo;
        list2.add("123");
        list2.add("456");
        System.out.println("size:"+list2.size()+","+list.size());
    }
}