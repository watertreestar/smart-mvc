package com.xingyao.smart.bean;

/**
 * @Author ranger
 * @Date 2020/6/19 16:16
 * 响应数据封装
 **/
public class Data {

    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
