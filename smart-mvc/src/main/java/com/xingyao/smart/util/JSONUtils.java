package com.xingyao.smart.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author ranger
 * @Date 2020/6/20 1:53
 **/
public class JSONUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJSON(Object obj){
        String jsonString ;
        try{
            jsonString = objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException("convert Object to json string failed",e);
        }
        return  jsonString;
    }
}
