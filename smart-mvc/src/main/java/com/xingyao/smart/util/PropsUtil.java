package com.xingyao.smart.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author ranger
 * @Date 2020/6/19 9:41
 * 加载配置文件，返回Properties
 **/
public class PropsUtil {

    public static Properties load(String path) throws IOException {
        InputStream inputStream = PropsUtil.class.getClassLoader().getResourceAsStream(path);
        Properties properties = new Properties();
        if(inputStream != null){
            properties.load(inputStream);
        }

        return properties;
    }

}
