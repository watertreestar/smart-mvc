package com.xingyao.smart.helper;

import com.xingyao.smart.config.ConfigConstant;
import com.xingyao.smart.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author ranger
 * @Date 2020/6/19 14:28
 * 获取指定注解的类
 **/
public class ClassHelper {

    private static String BASE_PACKAGE;

    private static Set<Class<?>> classSet;


    static {
        BASE_PACKAGE = ConfigHelper.getConfig(ConfigConstant.BASE_PACKAGE);
        classSet = ClassUtil.getClass(BASE_PACKAGE);
    }

    public static Set<Class<?>> getClassWithAnnotation(Class annotation){
        Set<Class<?>> annotationClass = new HashSet<>();
        for(Class clz : classSet){
            if(clz.getAnnotation(annotation) != null){
                annotationClass.add(clz);
            }
        }
        return annotationClass;
    }



}
