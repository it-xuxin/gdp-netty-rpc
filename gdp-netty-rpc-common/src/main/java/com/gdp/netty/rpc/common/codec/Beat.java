package com.gdp.netty.rpc.common.codec;

/**
 * 心跳检测
 */
public final class Beat {

    /**
     * 心跳检测30s
     */
    public static final int BEAT_INTERVAL = 30;

    /**
     * 心跳检测超时30s
     */
    public static final int BEAT_TIMEOUT = 3 * BEAT_INTERVAL;

    public static final String BEAT_ID = "BEAT_PING_PONG";


    public static RpcRequest BEAT_PING;

    static {
        BEAT_PING = new RpcRequest(){};
        BEAT_PING.setRequestId(BEAT_ID);
    }
}


