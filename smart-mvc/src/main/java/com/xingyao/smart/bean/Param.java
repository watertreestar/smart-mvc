package com.xingyao.smart.bean;

import java.util.Map;

/**
 * @Author ranger
 * @Date 2020/6/19 16:08
 * 参数封装
 **/
public class Param {

    private Map<String,Object> paramMap;

    public Param(Map map){
        this.paramMap = map;
    }

    public Map getParamMap(){
        return this.paramMap;
    }


}
