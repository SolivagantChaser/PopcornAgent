package com.popcorn.agent.core.planning;

import com.popcorn.agent.core.memory.Memory;
import com.popcorn.agent.core.perception.PerceptionResult;

/**
 * 规划模块核心接口：生成执行步骤
 */
public interface Planner {
    /**
     * 生成执行规划：基于感知结果和记忆，拆分子任务，生成执行步骤
     *
     * @param perceptionResult 感知结果
     * @param memory           Agent 记忆（短期+长期）
     * @return 执行规划（包含子任务列表、步骤顺序、工具调用建议）
     */
    Plan plan(PerceptionResult perceptionResult, Memory memory);
}
