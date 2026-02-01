package com.popcorn.agent.core.reflection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 核心层-反射模块：反射数据传输对象
 * 封装Agent执行全链路信息与反思结果，支持会话关联、记忆存储
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReflectionDTO {
    /**
     * 反射记录唯一标识
     */
    private String reflectionId;
    /**
     * 会话ID（关联同一用户/任务的感知、规划、执行、记忆、反射）
     */
    private String sessionId;
    /**
     * Agent唯一标识
     */
    private String agentId;
    /**
     * 原始用户指令
     */
    private String originalInstruction;
    /**
     * 感知阶段结果
     */
    private String perceiveResult;
    /**
     * 规划阶段步骤
     */
    private String planResult;
    /**
     * 执行阶段最终结果
     */
    private String executeResult;
    /**
     * 反射核心结论（结果是否合理/步骤是否有效/错误原因）
     */
    private String reflectionConclusion;
    /**
     * 优化建议（阶段一基础版：简单流程优化建议）
     */
    private String optimizeSuggestion;
    /**
     * 反射创建时间
     */
    private LocalDateTime createTime;

    // 初始化时间（创建时调用）
    public ReflectionDTO initTime() {
        this.createTime = LocalDateTime.now();
        return this;
    }
}