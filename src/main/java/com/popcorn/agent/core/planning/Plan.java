package com.popcorn.agent.core.planning;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 执行规划
 */
@Data
@Builder
public class Plan {
    private String planId;         // 规划ID
    private List<PlanStep> steps;  // 执行步骤列表
    private String description;    // 规划描述
}
