package com.popcorn.agent.core.perception;

/**
 * 感知模块核心接口：解析原始请求，提取关键信息
 */
public interface Perceptor {
    /**
     * 感知处理：解析用户请求，提取关键信息、上下文、意图
     *
     * @param rawRequest 原始用户请求（未解析）
     * @return 解析后的感知结果（包含关键信息、意图、上下文元数据）
     */
    PerceptionResult perceive(String rawRequest);
}
