package com.popcorn.agent.foundation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "agent.memory")
public class MemoryProperties {
    /**
     * 存储类型: memory / redis
     */
    private String storeType = "memory";
}
