package com.xingyao.smart.helper;


import com.xingyao.smart.annotation.Inject;
import com.xingyao.smart.util.ArrayUtils;
import com.xingyao.smart.util.CollectionUtils;
import com.xingyao.smart.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ranger
 * @Date 2020/6/19 15:30
 * IOC助手
 **/
public class IOCHelper {

    /**
     * 完成BeanHelper中所有bean的注入功能
     */
    public static void populate() {
        Map<Class, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtils.isNotEmpty(beanMap)) {
            for (Map.Entry<Class, Object> entry : beanMap.entrySet()) {
                Class beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                // 获取所有的属性，判断是否需要注入
                Field[] fields = beanClass.getDeclaredFields();

                if(ArrayUtils.isNotEmpty(fields)){
                    for(Field field : fields){
                        // 标记有Inject注解
                        if(field.isAnnotationPresent(Inject.class)){
                            Class fieldClass = field.getClass();
                            Object fieldBean = BeanHelper.getBean(fieldClass);
                            ReflectionUtil.setField(beanInstance,field,fieldBean);
                        }
                    }
                }
            }
        }


    }

}
