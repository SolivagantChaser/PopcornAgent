package com.popcorn.agent.core.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 核心层-记忆模块：记忆数据传输对象
 * 封装Agent记忆的核心属性，阶段一基础版：会话关联、内容存储、时间标识
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryDTO {
    /**
     * 记忆唯一标识（主键）
     */
    private String memoryId;
    /**
     * 会话ID（关联同一用户/同一任务的所有记忆）
     */
    private String sessionId;
    /**
     * 记忆类型（预留：USER_INSTRUCTION/PLAN_STEP/EXECUTE_RESULT等）
     */
    private String memoryType;
    /**
     * 记忆核心内容（字符串格式，支持简单文本/JSON）
     */
    private String content;
    /**
     * 记忆创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后访问时间（用于惰性清理，阶段一基础版预留）
     */
    private LocalDateTime lastAccessTime;

    // 初始化时间（创建时调用）
    public MemoryDTO initTime() {
        this.createTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
        return this;
    }

    // 更新最后访问时间（查询时调用）
    public MemoryDTO updateAccessTime() {
        this.lastAccessTime = LocalDateTime.now();
        return this;
    }
}