package com.popcorn.agent.core.perception.impl;

import com.popcorn.agent.core.perception.PerceptionResult;
import com.popcorn.agent.core.perception.Perceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单感知实现：基础的请求解析，提取问题和意图
 */
@Slf4j
@Component
public class SimplePerceptor implements Perceptor {
    @Override
    public PerceptionResult perceive(String rawRequest) {
        if (rawRequest == null || rawRequest.trim().isEmpty()) {
            throw new RuntimeException("原始请求不能为空");
        }
        String trimRequest = rawRequest.trim();
        // 简单意图识别：包含"数据"->"数据处理"，否则->"智能问答"
        String intent = trimRequest.contains("数据") ? "数据处理" : "智能问答";
        // 提取关键信息（基础版，仅存请求内容）
        Map<String, Object> keyInfo = new HashMap<>();
        keyInfo.put("rawContent", trimRequest);

        PerceptionResult result = PerceptionResult.builder()
                .question(trimRequest)
                .intent(intent)
                .keyInfo(keyInfo)
                .build();
        log.info("感知处理完成，结果：{}", result);
        return result;
    }
}
