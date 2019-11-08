package com.chungke.basic.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JdbcBasic {
    String host() default "";
    String port() default "";
}
