package com.xingyao.smart.annotation;

import com.fasterxml.jackson.databind.AnnotationIntrospector;

import java.lang.annotation.*;

/**
 * @Author ranger
 * @Date 2020/6/21 15:55
 * 基于注解的代理
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 需要代理的注解类
     * @return
     */
    Class<? extends Annotation> value();
}
