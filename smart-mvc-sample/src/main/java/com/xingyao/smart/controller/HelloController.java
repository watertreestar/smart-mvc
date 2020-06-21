package com.xingyao.smart.controller;

import com.xingyao.smart.annotation.Action;
import com.xingyao.smart.annotation.Controller;
import com.xingyao.smart.bean.Data;

/**
 * @Author ranger
 * @Date 2020/6/19 17:55
 **/
@Controller
public class HelloController {

    @Action("/hello")
    public Data hello(){
        return new Data("hello");
    }
}
