package com.popcorn.agent.core.memory.config;

import com.popcorn.agent.core.memory.MemoryService;
import com.popcorn.agent.core.memory.impl.InMemoryServiceImpl;
import com.popcorn.agent.core.memory.impl.RedisMemoryServiceImpl;
import com.popcorn.agent.foundation.config.MemoryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class MemoryConfig {

    private final MemoryProperties memoryProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final InMemoryServiceImpl inMemoryService;

    @Bean
    public MemoryService memoryService() {
        String type = memoryProperties.getStoreType();
        if ("redis".equalsIgnoreCase(type)) {
            return new RedisMemoryServiceImpl(stringRedisTemplate);
        } else {
            return inMemoryService;
        }
    }
}
