package com.xingyao.smart.proxy;

/**
 * @Author ranger
 * @Date 2020/6/21 16:02
 * 创建代理
 **/
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
