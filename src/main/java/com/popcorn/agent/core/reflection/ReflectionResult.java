package com.popcorn.agent.core.reflection;

import lombok.Builder;
import lombok.Data;

/**
 * 反思结果
 */
@Data
@Builder
public class ReflectionResult {
    private Integer score;         // 执行评分（0-100）
    private String errorAnalysis;  // 错误分析（无则为空）
    private String optimizationSuggestion; // 优化建议
}
