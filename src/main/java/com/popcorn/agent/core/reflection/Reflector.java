package com.popcorn.agent.core.reflection;

import com.popcorn.agent.core.excution.ExecutionResult;
import com.popcorn.agent.core.planning.Plan;

/**
 * 反思模块核心接口：评估执行结果，生成优化建议
 */
public interface Reflector {
    /**
     * 反思评估：基于执行结果，评估执行效果，分析错误，生成优化建议
     *
     * @param executionResult 执行结果
     * @param plan            原始执行规划
     * @return 反思结果（包含评估得分、错误原因、下一步优化策略）
     */
    ReflectionResult reflect(ExecutionResult executionResult, Plan plan);
}
