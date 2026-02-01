package com.popcorn.agent.app.manager;


import com.popcorn.agent.core.agent.Agent;
import com.popcorn.agent.foundation.exception.AgentNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agent实例管理器：统一管理所有Agent的注册、获取、生命周期
 */
@Slf4j
@Component
public class AgentManager {
    private final ApplicationContext applicationContext;
    // 线程安全的Agent实例映射，key=agentId
    private final Map<String, Agent> agentMap = new ConcurrentHashMap<>();

    public AgentManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 项目启动时自动注册所有Agent实现类
     */
    @PostConstruct
    public void init() {
        Map<String, Agent> allAgents = applicationContext.getBeansOfType(Agent.class);
        allAgents.forEach((beanName, agent) -> {
            agentMap.put(agent.getAgentId(), agent);
            log.info("自动注册Agent：{}({}) - {}", beanName, agent.getAgentId(), agent.getName());
        });
        log.info("Agent注册完成，共注册{}个Agent实例", agentMap.size());
    }

    /**
     * 根据AgentId获取Agent实例
     */
    public Agent getAgent(String agentId) {
        Agent agent = agentMap.get(agentId);
        if (agent == null) {
            throw new AgentNotFoundException("Agent不存在，AgentId：" + agentId);
        }
        return agent;
    }

    /**
     * 注册Agent实例（动态注册）
     */
    public void registerAgent(Agent agent) {
        if (agentMap.containsKey(agent.getAgentId())) {
            log.warn("Agent[{}]已存在，将覆盖原有实例", agent.getAgentId());
        }
        agent.init();
        agentMap.put(agent.getAgentId(), agent);
        log.info("动态注册Agent成功：{}", agent.getAgentId());
    }

    /**
     * 注销Agent实例
     */
    public void unregisterAgent(String agentId) {
        Agent agent = agentMap.remove(agentId);
        if (agent != null) {
            agent.destroy();
            log.info("注销Agent成功：{}", agentId);
        }
    }
}