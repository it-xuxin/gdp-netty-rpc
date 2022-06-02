package com.gdp.netty.rpc.common.codec;

import com.gdp.netty.rpc.common.serializer.Serializer;
import com.google.common.base.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Rpc Encoder
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;
    private Serializer serializer;

    public RpcEncoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext xtc, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            try {
                byte[] data = serializer.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            } catch (Exception e) {
                log.error("Encode error: {}", Throwables.getStackTraceAsString(e));
            }
        }
    }
}
