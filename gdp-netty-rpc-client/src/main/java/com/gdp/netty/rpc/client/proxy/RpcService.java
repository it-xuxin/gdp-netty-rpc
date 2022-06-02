package com.gdp.netty.rpc.client.proxy;

import com.gdp.netty.rpc.client.handler.RpcFuture;

public interface RpcService<T, P, FN extends SerializableFunction<T>> {

    RpcFuture call(String funcName, Object... args) throws Exception;

    RpcFuture call(FN fn, Object... args) throws Exception;
}
