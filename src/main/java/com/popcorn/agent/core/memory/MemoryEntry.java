package com.popcorn.agent.core.memory;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 记忆条目
 */
@Data
@Builder
public class MemoryEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;        // 记忆内容
    private MemoryType type;       // 记忆类型
    private Long timestamp;        // 时间戳
    private String agentId;        // 所属AgentID
    private String reflectionSuggestion; // 反思建议
}
