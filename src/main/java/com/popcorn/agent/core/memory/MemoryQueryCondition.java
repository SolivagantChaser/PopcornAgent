package com.popcorn.agent.core.memory;

import lombok.Data;

/**
 * 记忆查询条件
 */
@Data
public class MemoryQueryCondition {
    private String agentId;        // AgentID
    private MemoryType type;       // 记忆类型
    private Long startTime;        // 开始时间
    private Long endTime;          // 结束时间
}
