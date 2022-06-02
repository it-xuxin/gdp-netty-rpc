package com.gdp.netty.rpc.common.codec;

import com.gdp.netty.rpc.common.serializer.Serializer;
import com.google.common.base.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Rpc Decoder
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    private Serializer serializer;

    public RpcDecoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4){
            log.warn("Readable bytes to short");
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = null;
        try {
            obj = serializer.deserialize(data, genericClass);
            out.add(obj);
        }catch (Exception e){
            log.error("Decode error: " + Throwables.getStackTraceAsString(e));
        }
    }
}
