package com.popcorn.agent.foundation;

/**
 * 基础层 - 全局通用常量
 * 仅保留最核心常量，无冗余定义
 */
public class AgentConstant {
    // 执行状态
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    // Agent默认标识
    public static final String DEFAULT_AGENT_ID = "basic_agent_001";
    // 核心执行阶段
    public static final String STAGE_PERCEPTION = "感知阶段";
    public static final String STAGE_PLANNING = "规划阶段";
    public static final String STAGE_EXECUTION = "执行阶段";

    // 私有构造，禁止实例化
    private AgentConstant() {
    }
}
