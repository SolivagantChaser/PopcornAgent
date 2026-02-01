package com.popcorn.agent.core.agent.impl;

import com.popcorn.agent.core.agent.Agent;
import com.popcorn.agent.core.agent.AgentRequest;
import com.popcorn.agent.core.agent.AgentResponse;
import com.popcorn.agent.core.excution.ExecutionResult;
import com.popcorn.agent.core.excution.Executor;
import com.popcorn.agent.core.memory.Memory;
import com.popcorn.agent.core.memory.MemoryEntry;
import com.popcorn.agent.core.memory.MemoryType;
import com.popcorn.agent.core.perception.PerceptionResult;
import com.popcorn.agent.core.perception.Perceptor;
import com.popcorn.agent.core.planning.Plan;
import com.popcorn.agent.core.planning.Planner;
import com.popcorn.agent.core.reflection.ReflectionResult;
import com.popcorn.agent.core.reflection.Reflector;
import com.popcorn.agent.core.tool.impl.DefaultToolRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通用Agent实现类：组合所有核心模块，实现标准执行流程
 */
@Slf4j
@Component
public class GeneralAgent implements Agent {
    // 注入所有核心模块
    private final Perceptor perceptor;
    private final Planner planner;
    private final Executor executor;
    private final Memory memory;
    private final Reflector reflector;
    private final DefaultToolRegistry toolRegistry;

    // Agent配置信息（可通过application.yml注入）
    @Value("${agent.id:general-agent-001}")
    private String agentId;
    @Value("${agent.name:通用智能Agent}")
    private String name;

    // 构造器注入（Spring自动装配）
    public GeneralAgent(Perceptor perceptor, Planner planner, Executor executor,
                        Memory memory, Reflector reflector, DefaultToolRegistry toolRegistry) {
        this.perceptor = perceptor;
        this.planner = planner;
        this.executor = executor;
        this.memory = memory;
        this.reflector = reflector;
        this.toolRegistry = toolRegistry;
    }

    @Override
    public AgentResponse execute(AgentRequest request) {
        try {
            log.info("Agent[{}]开始处理请求：{}", agentId, request.getRawRequest());
            // 1. 感知：解析原始请求
            PerceptionResult perceptionResult = perceptor.perceive(request.getRawRequest());
            // 2. 规划：生成执行步骤
            Plan plan = planner.plan(perceptionResult, memory);
            // 3. 执行：按规划调用工具
            ExecutionResult executionResult = executor.execute(plan, memory);
            if (!executionResult.getSuccess()) {
                throw new RuntimeException("规划执行失败：" + executionResult.getErrorMsg());
            }
            // 4. 反思：评估执行结果
            ReflectionResult reflectionResult = reflector.reflect(executionResult, plan);
            // 5. 记忆：保存本次交互信息
            memory.save(buildMemoryEntry(perceptionResult, executionResult, reflectionResult));
            // 6. 构建并返回响应
            return AgentResponse.success()
                    .answer(executionResult.getFinalResult())
                    .executeSteps(plan.getSteps())
                    .reflectionSuggestion(reflectionResult.getOptimizationSuggestion())
                    .build();
        } catch (Exception e) {
            log.error("Agent[{}]处理请求失败", agentId, e);
            return AgentResponse.failure()
                    .errorCode("AGENT_EXECUTE_ERROR")
                    .errorMessage("Agent处理请求失败：" + e.getMessage())
                    .build();
        }
    }

    /**
     * 构建记忆条目
     */
    private MemoryEntry buildMemoryEntry(PerceptionResult perceptionResult,
                                         ExecutionResult executionResult,
                                         ReflectionResult reflectionResult) {
        return MemoryEntry.builder()
                .content("问题：" + perceptionResult.getQuestion() + " | 答案：" + executionResult.getFinalResult())
                .type(MemoryType.SHORT_TERM)
                .timestamp(System.currentTimeMillis())
                .agentId(agentId)
                .reflectionSuggestion(reflectionResult.getOptimizationSuggestion())
                .build();
    }

    @Override
    public String getAgentId() {
        return agentId;
    }

    // Setter方法（便于配置注入）
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostConstruct
    @Override
    public void init() {
        log.info("Agent[{}]开始初始化，名称：{}", agentId, name);
        // 初始化检查：是否注册了必要工具
        if (toolRegistry.getTool("ai-tool-001") == null || toolRegistry.getTool("data-tool-001") == null) {
            log.warn("Agent[{}]初始化警告：核心工具未注册，部分能力将受限", agentId);
        }
        log.info("Agent[{}]初始化完成", agentId);
    }

    @PreDestroy
    @Override
    public void destroy() {
        log.info("Agent[{}]开始销毁", agentId);
        // 销毁逻辑：淘汰过期记忆
        memory.evict();
        log.info("Agent[{}]销毁完成", agentId);
    }
}