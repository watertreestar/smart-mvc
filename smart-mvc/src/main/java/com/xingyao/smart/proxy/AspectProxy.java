package com.xingyao.smart.proxy;

import java.lang.reflect.Method;

/**
 * @Author ranger
 * @Date 2020/6/21 21:00
 * 代理抽象类
 * 实现Proxy接口中的doProxy方法
 **/
public abstract class AspectProxy implements Proxy {
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> clz = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethodd();
        Object[] params = proxyChain.getParams();


        begin();
        try{
            if(intercept(clz,method,params)){
                before(clz,method,params);
                result = proxyChain.doProxyChain();
                after(clz,method,params,result);
            }else {
                result = proxyChain.doProxyChain();
            }
        }catch (Throwable t){
            error(clz,method,params,t);
            throw t;
        }finally {
            end();
        }
        return result;
    }

    /**
     * 是否需要执行当前Proxy
     * @param clz
     * @param method
     * @param params
     * @return
     * @throws Throwable
     */
    protected  boolean intercept(Class<?> clz, Method method, Object[] params) throws Throwable{
        return true;
    }

    /**
     * 前置方法
     * @param clz
     * @param method
     * @param params
     * @throws Throwable
     */
    protected abstract void before(Class<?> clz, Method method, Object[] params) throws Throwable;

    /**
     * 后置方法
     * @param clz
     * @param method
     * @param params
     * @param result  原方法调用后的返回值
     * @throws Throwable
     */
    protected abstract void after(Class<?> clz, Method method, Object[] params,Object result) throws Throwable;

    /**
     * doProxy方法执行开始
     */
    protected  abstract void begin();

    /**
     * doProxy方法执行结束
     */
    protected abstract void end();

    /**
     * 执行异常
     * @param clz
     * @param method
     * @param params
     * @param e
     */
    protected abstract void error(Class<?> clz, Method method, Object[] params,Throwable e);
}
