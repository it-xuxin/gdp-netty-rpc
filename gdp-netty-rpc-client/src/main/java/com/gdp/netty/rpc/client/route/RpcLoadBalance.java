package com.gdp.netty.rpc.client.route;

import com.gdp.netty.rpc.client.handler.RpcClientHandler;
import com.gdp.netty.rpc.common.protocol.RpcProtocol;
import com.gdp.netty.rpc.common.protocol.RpcServiceInfo;
import com.gdp.netty.rpc.common.util.ServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RpcLoadBalance {

    protected Map<String, List<RpcProtocol>> getServiceMap(Map<RpcProtocol, RpcClientHandler> connectedServerNodes) {
        Map<String, List<RpcProtocol>> serviceMap = new HashMap<>();
        if (connectedServerNodes != null && connectedServerNodes.size() > 0) {
            for (RpcProtocol rpcProtocol : connectedServerNodes.keySet()) {
                for (RpcServiceInfo serviceInfo : rpcProtocol.getServiceInfoList()) {
                    String serverKey = ServiceUtil.makeServiceKey(serviceInfo.getServiceName(), serviceInfo.getVersion());
                    List<RpcProtocol> rpcProtocolList = serviceMap.getOrDefault(serverKey, new ArrayList<>());
                    rpcProtocolList.add(rpcProtocol);
                    serviceMap.putIfAbsent(serverKey, rpcProtocolList);
                }
            }
        }
        return serviceMap;
    }

    // Route the connection for service key
    public abstract RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception;
}
