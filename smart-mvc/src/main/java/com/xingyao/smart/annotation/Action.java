package com.xingyao.smart.annotation;

import com.xingyao.smart.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author ranger
 * @Date 2020/6/19 14:23
 **/

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * 请求路径
     * @return
     */
    String value();

    RequestMethod method() default RequestMethod.GET;
}
