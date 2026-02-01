package com.popcorn.agent.core.planning;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 执行规划步骤
 */
@Data
@Builder
public class PlanStep {
    private Integer stepNo;        // 步骤编号
    private String stepName;       // 步骤名称
    private String toolId;         // 调用的工具ID（空则不调用工具）
    private Map<String, Object> toolParams; // 工具参数
    private String description;    // 步骤描述
}
