package com.xingyao.smart.enums;

/**
 * @Author ranger
 * @Date 2020/6/19 17:24
 **/
public enum  RequestMethod {

    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE")
    ;


    private RequestMethod(String name){
        this.name = name;
    }

    String name;
}
