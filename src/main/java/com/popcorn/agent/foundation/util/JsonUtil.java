package com.popcorn.agent.foundation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * JSON解析工具类
 */
@Slf4j
@Component
public class JsonUtil {
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 对象转JSON字符串
     */
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败", e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * JSON字符串转Map
     */
    public Map<String, Object> toMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.error("JSON转Map失败", e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
}
