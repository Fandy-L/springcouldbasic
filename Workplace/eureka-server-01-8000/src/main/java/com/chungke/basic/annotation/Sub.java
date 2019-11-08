package com.chungke.basic.annotation;

import java.lang.annotation.*;
import java.util.Arrays;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@interface ATable {

    public String name() default "";
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface BTable {
    public String name() default "";
}


@ATable
class Super {
    private int superx;
    public int supery;
    public Super() {
    }
    private int superX(){
        return 0;
    }
    public int superY(){
        return 0;
    }

}


@BTable
public class Sub extends Super{
    private int subx;
    public int suby;
    private Sub()
    {
    }
    public Sub(int i){
    }
    private int subX(){
        return 0;
    }
    public int subY(){
        return 0;
    }
}



class TestMain {
    public static void main(String[] args) {

        Class<Sub> clazz = Sub.class;
        System.out.println("============================Field===========================");
        System.out.println(Arrays.toString(clazz.getFields()));
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));  //all + 自身
        System.out.println("============================Method===========================");
        System.out.println(Arrays.toString(clazz.getMethods()));   //public + 继承
        //all + 自身
        System.out.println(Arrays.toString(clazz.getDeclaredMethods()));
        System.out.println("============================Constructor===========================");
        System.out.println(Arrays.toString(clazz.getConstructors()));
        System.out.println(Arrays.toString(clazz.getDeclaredConstructors()));
        System.out.println("============================AnnotatedElement===========================");
        //注解DBTable2是否存在于元素上
        System.out.println(clazz.isAnnotationPresent(BTable.class));
        //如果存在该元素的指定类型的注释DBTable2，则返回这些注释，否则返回 null。
        System.out.println(clazz.getAnnotation(BTable.class));
        //继承
        System.out.println(Arrays.toString(clazz.getAnnotations()));
        System.out.println(Arrays.toString(clazz.getDeclaredAnnotations()));  ////自身
    }
}