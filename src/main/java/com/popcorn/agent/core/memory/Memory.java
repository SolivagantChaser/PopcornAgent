package com.popcorn.agent.core.memory;

import java.util.List;

/**
 * 记忆模块核心接口
 */
public interface Memory {
    /**
     * 保存记忆
     *
     * @param memoryEntry 记忆条目（包含内容、类型、时间戳、关联ID等）
     */
    void save(MemoryEntry memoryEntry);

    /**
     * 查询记忆：基于关键词/上下文/时间范围
     *
     * @param condition 记忆查询条件
     * @return 匹配的记忆条目列表
     */
    List<MemoryEntry> query(MemoryQueryCondition condition);

    /**
     * 更新记忆
     *
     * @param memoryEntry 待更新的记忆条目
     */
    void update(MemoryEntry memoryEntry);

    /**
     * 记忆淘汰：按策略（如LRU、时间）清理过期/无用记忆
     */
    void evict();
}
