package com.popcorn.agent.app.manager;

import com.popcorn.agent.core.agent.Agent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 应用层 - Agent实例管理器
 * 阶段一极简实现：管理Agent实例，提供统一获取入口
 */
@Component
@RequiredArgsConstructor
public class AgentManager {
    // 注入所有实现Agent接口的实例（Spring自动注入）
    private final Map<String, Agent> agentMap;

    /**
     * 根据Agent ID获取实例
     *
     * @param agentId Agent标识
     * @return Agent实例
     */
    public Optional<Agent> getAgent(String agentId) {
        return Optional.ofNullable(agentMap.get(agentId));
    }

    /**
     * 获取默认Agent实例（阶段一唯一实现）
     *
     * @return 基础Agent实例
     */
    public Agent getDefaultAgent() {
        return agentMap.get("basicAgent"); // Spring默认首字母小写
    }
}