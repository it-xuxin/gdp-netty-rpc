package com.gdp.netty.rpc.client.proxy;

@FunctionalInterface
public interface RpcFunction<T, P> extends SerializableFunction<T> {
    Object apply(T t, P p);
}

