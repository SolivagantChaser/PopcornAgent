package com.popcorn.agent.core.memory.impl;

import com.alibaba.fastjson2.JSON;
import com.popcorn.agent.core.memory.MemoryService;
import com.popcorn.agent.core.memory.dto.MemoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMemoryServiceImpl implements MemoryService {

    private static final String KEY_PREFIX = "agent:memory:";
    private static final String SESSION_PREFIX = "agent:session:";
    private static final long EXPIRE_DAYS = 7;
    private final StringRedisTemplate redisTemplate;

    @Override
    public String addMemory(MemoryDTO dto) {
        String memoryId = UUID.randomUUID().toString().replace("-", "");
        dto.setMemoryId(memoryId);

        String key = KEY_PREFIX + memoryId;
        String sessionKey = SESSION_PREFIX + dto.getSessionId();

        try {
            String json = JSON.toJSONString(dto);
            redisTemplate.opsForValue().set(key, json, EXPIRE_DAYS, TimeUnit.DAYS);
            redisTemplate.opsForSet().add(sessionKey, memoryId);
            redisTemplate.expire(sessionKey, EXPIRE_DAYS, TimeUnit.DAYS);
            log.info("Redis存储记忆成功, memoryId={}, sessionId={}", memoryId, dto.getSessionId());
            return memoryId;
        } catch (Exception e) {
            log.error("Redis存储记忆异常", e);
            throw new RuntimeException("Redis存储记忆失败");
        }
    }

    @Override
    public Optional<MemoryDTO> getMemoryById(String memoryId) {
        String key = KEY_PREFIX + memoryId;
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(JSON.parseObject(json, MemoryDTO.class));
        } catch (Exception e) {
            log.error("解析记忆JSON异常", e);
            return Optional.empty();
        }
    }

    @Override
    public List<MemoryDTO> listMemoryBySessionId(String sessionId) {
        String sessionKey = SESSION_PREFIX + sessionId;
        Set<String> memoryIds = redisTemplate.opsForSet().members(sessionKey);
        if (memoryIds == null || memoryIds.isEmpty()) {
            return Collections.emptyList();
        }
        return memoryIds.stream()
                .map(this::getMemoryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(MemoryDTO::getCreateTime))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteMemoryById(String memoryId) {
        String key = KEY_PREFIX + memoryId;
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public int deleteMemoryBySessionId(String sessionId) {
        String sessionKey = SESSION_PREFIX + sessionId;
        Set<String> memoryIds = redisTemplate.opsForSet().members(sessionKey);
        if (memoryIds == null || memoryIds.isEmpty()) return 0;

        int count = 0;
        for (String mid : memoryIds) {
            if (deleteMemoryById(mid)) count++;
        }
        redisTemplate.delete(sessionKey);
        return count;
    }
}
