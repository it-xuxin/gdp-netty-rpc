package com.gdp.netty.rpc.common.protocol;

import com.gdp.netty.rpc.common.util.JsonUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class RpcServiceInfo implements Serializable {
    // interface name
    private String serviceName;
    // service version
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcServiceInfo that = (RpcServiceInfo) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }

    public String toJson() {
        return JsonUtil.objectToJson(this);
    }

    @Override
    public String toString() {
        return toJson();
    }
}
