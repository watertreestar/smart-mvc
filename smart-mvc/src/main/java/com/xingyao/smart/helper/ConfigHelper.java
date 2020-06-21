package com.xingyao.smart.helper;

import com.xingyao.smart.config.ConfigConstant;
import com.xingyao.smart.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author ranger
 * @Date 2020/6/19 9:40
 * smart.properties配置文件加载类
 **/
public class ConfigHelper {

    private static Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

    private static Properties properties;

    static {
        try {
            properties = PropsUtil.load(ConfigConstant.CONFIG_FILE);
        } catch (IOException e) {
            logger.error("load config file exception");
        }
    }

    public static String getConfig(String key,String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    public static String getConfig(String key){
        return properties.getProperty(key);
    }


}
