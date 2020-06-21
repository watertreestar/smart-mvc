package com.xingyao.smart.bean;

import java.util.Map;

/**
 * @Author ranger
 * @Date 2020/6/19 16:12
 * 视图封装，包括模板的路径，模板的数据对象
 **/
public class View {

    private String path;

    private Map<String,Object> model;

    public View(String path,Map<String,Object> model){
        this.path = path;
        this.model = model;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
