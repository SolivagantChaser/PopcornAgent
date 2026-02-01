package com.popcorn.agent.core.agent;

import lombok.Data;

/**
 * Agent请求参数
 */
@Data
public class AgentRequest {
    private String agentId;        // 调用的AgentID
    private String rawRequest;     // 原始用户请求
}
