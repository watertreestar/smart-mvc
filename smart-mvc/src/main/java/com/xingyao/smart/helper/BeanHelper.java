package com.xingyao.smart.helper;

import com.xingyao.smart.annotation.Controller;
import com.xingyao.smart.annotation.Service;
import com.xingyao.smart.util.ReflectionUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ranger
 * @Date 2020/6/19 14:43
 * 容器助手
 **/
public class BeanHelper {
    private static Map<Class,Object> beanMap;

    static {
        beanMap = new ConcurrentHashMap<>();
        Set<Class<?>> controllerClass = ClassHelper.getClassWithAnnotation(Controller.class);
        Set<Class<?>> serviceClasses = ClassHelper.getClassWithAnnotation(Service.class);

        for(Class clz : controllerClass){
            Object bean = ReflectionUtil.newInstance(clz);
            beanMap.put(clz,bean);
        }

        for(Class clz : serviceClasses){
            Object bean = ReflectionUtil.newInstance(clz);
            beanMap.put(clz,bean);
        }
    }


    public static Map<Class,Object> getBeanMap(){
        return beanMap;
    }

    public static <T> T getBean(Class<T> clz){
        if(!beanMap.containsKey(clz)){
            throw new RuntimeException("bean not found");
        }
        return (T) beanMap.get(clz);
    }
}
