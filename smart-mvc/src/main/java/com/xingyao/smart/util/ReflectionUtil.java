package com.xingyao.smart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author ranger
 * @Date 2020/6/19 14:34
 * 反射相关工具类
 **/
public class ReflectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     *
     * @param clz
     * @return
     */
    public static Object newInstance(Class<?> clz) {
        Object instance;
        try {
            instance = clz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create instance failed", e);
        }
        return instance;
    }

    /**
     * 调用对象上指定的方法
     *
     * @param obj
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            result = method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException("invoke method error", e);
        }
        return result;
    }

    /**
     * 给指定对象上的属性赋值
     * @param object
     * @param field
     * @param value
     */
    public static void setField(Object object, Field field, Object value) {
        try {
            boolean access = field.isAccessible();
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("set field value error",e);
        }
    }

}
