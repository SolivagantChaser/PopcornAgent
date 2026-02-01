package com.popcorn.agent.core.agent;


/**
 * Agent 顶级核心接口：定义 Agent 的核心行为（感知-规划-执行-反思-记忆）
 * 单一职责原则，所有 Agent 实例均实现此接口，便于统一管理和扩展
 */
public interface Agent {
    /**
     * Agent 核心执行方法：处理用户请求，返回执行结果
     *
     * @param request 用户请求（包含问题、上下文、参数等）
     * @return Agent 执行结果（包含答案、执行步骤、工具调用记录等）
     */
    AgentResponse execute(AgentRequest request);

    /**
     * 获取 Agent 唯一标识
     *
     * @return Agent ID
     */
    String getAgentId();

    /**
     * 获取 Agent 名称
     *
     * @return Agent 名称
     */
    String getName();

    /**
     * 初始化 Agent（加载配置、注册工具、连接依赖等）
     */
    void init();

    /**
     * 销毁 Agent（释放资源、关闭连接等）
     */
    void destroy();
}
