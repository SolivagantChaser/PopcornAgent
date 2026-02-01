package com.popcorn.agent.core.excution;

import com.popcorn.agent.core.memory.Memory;
import com.popcorn.agent.core.planning.Plan;

/**
 * 执行模块核心接口：执行规划步骤，调用工具
 */
public interface Executor {
    /**
     * 执行规划：按步骤执行，按需调用工具，处理异常
     *
     * @param plan   执行规划
     * @param memory Agent 记忆（执行过程中实时更新）
     * @return 执行结果（包含各步骤执行状态、工具调用结果、中间数据）
     */
    ExecutionResult execute(Plan plan, Memory memory);
}
