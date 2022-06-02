package com.gdp.netty.rpc.common.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * Rpc Response
 */
@Data
public class RpcResponse implements Serializable {

    private String requestId;
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }

}
