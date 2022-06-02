package com.gdp.netty.rpc.common.serializer;

public abstract class Serializer {
    public abstract <T> byte[] serialize(T obj);

    public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
