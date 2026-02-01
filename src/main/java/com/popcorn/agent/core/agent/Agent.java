package com.popcorn.agent.core.agent;

/**
 * 核心层 - Agent核心接口
 * 定义Agent最基础能力，所有实现类必须遵循
 */
public interface Agent {
    /**
     * Agent核心执行方法
     *
     * @param instruction 用户指令/任务
     * @return 执行结果
     * @throws Exception 执行异常
     */
    String execute(String instruction) throws Exception;

    /**
     * 获取Agent唯一标识
     *
     * @return Agent ID
     */
    String getAgentId();
}