package com.xingyao.smart.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ranger
 * @Date 2020/6/21 16:06
 * 代理链条
 * 持有一些列的Proxy对象，可以一个一个去执行
 **/
public class ProxyChain {

    /**
     * 目标类
     */
    private Class<?> targetClass;

    /**
     * 目标对象
     */
    private Object targetObject;

    /**
     * 目标方法
     */
    private Method targetMethod;

    /**
     * 方法代理
     */
    private MethodProxy methodProxy;

    /**
     * 方法参数
     */
    private Object[] params;

    /**
     * 代理列表
     */
    private List<Proxy> proxyList = new ArrayList<>();

    /**
     * 执行代理列表索引
     */
    private int proxyIndex = 0;


    public ProxyChain(Class<?> targetClass,
                      Object targetObject,
                      Method targetMethod,
                      MethodProxy methodProxy,
                      Object[] params,
                      List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;

        this.params = params;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethodd() {
        return targetMethod;
    }

    public Object[] getParams() {
        return params;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;

        if (proxyIndex < proxyList.size()) {
            methodResult = proxyList.get(proxyIndex++).doProxy(this);

        } else {
            methodResult = methodProxy.invokeSuper(targetObject, params);
        }

        return methodResult;

    }
}
