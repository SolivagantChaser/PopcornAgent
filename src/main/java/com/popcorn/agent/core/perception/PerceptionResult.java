package com.popcorn.agent.core.perception;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 感知结果：解析后的用户请求信息
 */
@Data
@Builder
public class PerceptionResult {
    private String question;       // 提取的用户问题
    private Map<String, Object> keyInfo; // 关键信息（如参数、上下文）
    private String intent;         // 用户意图（如问答、数据处理、工具调用）
}
