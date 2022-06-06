package com.gdp.netty.rpc.server.registry;

import com.gdp.netty.rpc.common.config.Constant;
import com.gdp.netty.rpc.common.protocol.RpcProtocol;
import com.gdp.netty.rpc.common.protocol.RpcServiceInfo;
import com.gdp.netty.rpc.common.util.ServiceUtil;
import com.gdp.netty.rpc.common.zookeeper.CuratorClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class ServiceRegistry {

    private CuratorClient curatorClient;
    private List<String> pathList = new ArrayList<>();

    public ServiceRegistry(String registryAddress) {
        this.curatorClient = new CuratorClient(registryAddress, 5000);
    }

    public void registerService(String host, int port, Map<String, Object> serviceMap) {
        List<RpcServiceInfo> serviceInfoList = new ArrayList<>();

        for (String key : serviceMap.keySet()) {
            String[] serviceInfo = key.split(ServiceUtil.SERVICE_CONCAT_TOKEN);
            if (serviceInfo.length > 0) {
                RpcServiceInfo rpcServiceInfo = new RpcServiceInfo();
                rpcServiceInfo.setServiceName(serviceInfo[0]);
                if (serviceInfo.length == 2) {
                    rpcServiceInfo.setVersion(serviceInfo[1]);
                } else {
                    rpcServiceInfo.setVersion("");
                }
                log.info("Register new service: {} ", key);
                serviceInfoList.add(rpcServiceInfo);
            } else {
                log.warn("Can not get service name and version: {} ", key);
            }
        }
        try {
            RpcProtocol rpcProtocol = new RpcProtocol();
            rpcProtocol.setHost(host);
            rpcProtocol.setPort(port);
            rpcProtocol.setServiceInfoList(serviceInfoList);
            String serviceData = rpcProtocol.toJson();
            byte[] bytes = serviceData.getBytes(StandardCharsets.UTF_8);
            String path = Constant.ZK_DATA_PATH + "-" + rpcProtocol.hashCode();
            path = this.curatorClient.createPathData(path, bytes);
            log.info("Create path data , path = {}, bytes size = {}", path, bytes.length);
            pathList.add(path);
            log.info("Register {} new service, host : {}, port : {}", serviceInfoList.size(), host, port);
        } catch (Exception e) {
            log.error("Register service fail, exception: {}", e.getMessage());
        }

        curatorClient.addConnectionStateListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                if (connectionState == ConnectionState.RECONNECTED) {
                    log.info("Connection state: {}, register service after reconnected", connectionState);
                    registerService(host, port, serviceMap);
                }
            }
        });
    }

    public void unregisterService() {
        log.info("Unregister all service");
        for (String path : pathList) {
            try {
                this.curatorClient.deletePath(path);
            } catch (Exception ex) {
                log.error("Delete service path error: " + ex.getMessage());
            }
        }
        this.curatorClient.close();
    }
}
