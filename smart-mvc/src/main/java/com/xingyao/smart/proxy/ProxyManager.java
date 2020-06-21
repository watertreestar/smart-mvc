package com.xingyao.smart.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author ranger
 * @Date 2020/6/21 19:40
 * 创建代理对象
 **/
public class ProxyManager {

    public static <T> T craeteProxy(Class<?> targetClass, List<Proxy> proxyList){
        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                ProxyChain proxyChain = new ProxyChain(targetClass,o,method,methodProxy,objects,proxyList);
                return proxyChain.doProxyChain();
            }
        });
    }
}
