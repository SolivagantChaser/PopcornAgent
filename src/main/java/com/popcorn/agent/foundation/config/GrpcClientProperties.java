package com.popcorn.agent.foundation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "agent.grpc")
public class GrpcClientProperties {
    private PythonServerProperties pythonServer = new PythonServerProperties();
    private Integer timeoutSecond = 5;
    private Integer keepAliveSecond = 30;
    private Integer shutdownWaitSecond = 5;

    @Data
    public static class PythonServerProperties {
        private String host = "127.0.0.1";
        private Integer port = 50051;

        public String getTarget() {
            return String.format("%s:%s", host, port);
        }
    }
}