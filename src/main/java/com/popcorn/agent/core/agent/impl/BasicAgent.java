package com.popcorn.agent.core.agent.impl;

import com.popcorn.agent.core.agent.Agent;
import com.popcorn.agent.core.excution.ExecutionEngine;
import com.popcorn.agent.core.memory.MemoryService;
import com.popcorn.agent.core.memory.dto.MemoryDTO;
import com.popcorn.agent.core.perception.PerceptionService;
import com.popcorn.agent.core.planning.PlanningService;
import com.popcorn.agent.core.reflection.ReflectionService;
import com.popcorn.agent.core.reflection.dto.ReflectionDTO;
import com.popcorn.agent.foundation.AgentConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 核心层 - 基础Agent实现
 * 集成完整核心能力：感知→规划→执行→反射，联动记忆模块
 * 新增：反射服务注入，执行后自动触发反射，形成完整链路
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicAgent implements Agent {
    // 原有核心层依赖（无修改）
    private final PerceptionService perceptionService;
    private final PlanningService planningService;
    private final ExecutionEngine executionEngine;
    private final MemoryService memoryService;
    // 新增：注入反射服务（核心修改，实现反射能力集成）
    private final ReflectionService reflectionService;

    /**
     * 核心执行方法：更新为「感知→规划→执行→反射」完整链路
     *
     * @param instruction 原始用户指令
     * @param sessionId   会话ID（新增，用于关联全链路数据+记忆+反射）
     * @return 执行结果（反射结果自动存入记忆，不影响原有返回值）
     * @throws Exception 执行异常
     */
    public String execute(String instruction, String sessionId) throws Exception {
        // 步骤1：感知阶段
        String perceiveResult = perceptionService.perceive(instruction);
        // 步骤2：规划阶段
        String planResult = planningService.plan(perceiveResult);
        // 步骤3：执行阶段
        String executeResult = executionEngine.execute(planResult);
        // 步骤4：反射阶段（新增，自动触发，形成完整链路）
        triggerReflection(instruction, perceiveResult, planResult, executeResult, sessionId);
        // 步骤5：将核心执行结果存入记忆（原有记忆能力，增强链路）
        saveExecuteResultToMemory(instruction, executeResult, sessionId);
        return executeResult;
    }

    /**
     * 兼容原有无会话ID的执行方法（保证接口兼容性，默认生成临时会话ID）
     */
    @Override
    public String execute(String instruction) throws Exception {
        return execute(instruction, "temp_session_" + System.currentTimeMillis());
    }

    // 原有方法（无修改）
    @Override
    public String getAgentId() {
        return AgentConstant.DEFAULT_AGENT_ID;
    }

    // 原有记忆保存方法（无修改）
    public String saveMemory(String sessionId, String memoryType, String content) {
        MemoryDTO memory = MemoryDTO.builder()
                .sessionId(sessionId)
                .memoryType(memoryType)
                .content(content)
                .build();
        return memoryService.addMemory(memory);
    }

    /**
     * 私有方法：触发反射（封装全链路数据，调用反射服务）
     */
    private void triggerReflection(String originalInstruction, String perceiveResult,
                                   String planResult, String executeResult, String sessionId) {
        try {
            ReflectionDTO reflectionDTO = ReflectionDTO.builder()
                    .sessionId(sessionId)
                    .agentId(getAgentId())
                    .originalInstruction(originalInstruction)
                    .perceiveResult(perceiveResult)
                    .planResult(planResult)
                    .executeResult(executeResult)
                    .build();
            // 调用反射核心方法，自动完成反思+结果存储+记忆联动
            reflectionService.reflect(reflectionDTO);
        } catch (Exception e) {
            log.error("【Agent核心链路】触发反射阶段失败，不影响执行结果，会话ID={}", sessionId, e);
        }
    }

    /**
     * 私有方法：将执行结果存入记忆模块（记忆类型：EXECUTE_RESULT）
     */
    private void saveExecuteResultToMemory(String instruction, String executeResult, String sessionId) {
        try {
            saveMemory(sessionId, "EXECUTE_RESULT",
                    String.format("原始指令：%s；执行结果：%s", instruction, executeResult));
        } catch (Exception e) {
            log.warn("【Agent核心链路】执行结果存入记忆失败，会话ID={}", sessionId, e);
        }
    }
}