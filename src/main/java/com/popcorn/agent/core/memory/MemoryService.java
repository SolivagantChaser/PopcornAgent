package com.popcorn.agent.core.memory;

import com.popcorn.agent.core.memory.dto.MemoryDTO;

import java.util.List;
import java.util.Optional;

/**
 * 核心层-记忆模块：核心接口
 * 定义Agent记忆的基础操作，屏蔽底层存储实现（阶段一为内存，阶段二可扩展Redis/MySQL）
 * 阶段一实现：新增、单条查询、按会话查询、删除
 */
public interface MemoryService {
    /**
     * 新增一条记忆
     *
     * @param memoryDTO 记忆信息
     * @return 新增后的记忆ID
     */
    String addMemory(MemoryDTO memoryDTO);

    /**
     * 根据记忆ID查询单条记忆
     *
     * @param memoryId 记忆唯一标识
     * @return 记忆信息（空则返回Optional.empty()）
     */
    Optional<MemoryDTO> getMemoryById(String memoryId);

    /**
     * 根据会话ID查询所有关联记忆（按创建时间升序）
     *
     * @param sessionId 会话ID
     * @return 该会话下的所有记忆
     */
    List<MemoryDTO> listMemoryBySessionId(String sessionId);

    /**
     * 根据记忆ID删除单条记忆
     *
     * @param memoryId 记忆唯一标识
     * @return true-删除成功，false-记忆不存在
     */
    boolean deleteMemoryById(String memoryId);

    /**
     * 根据会话ID删除所有关联记忆
     *
     * @param sessionId 会话ID
     * @return 删除的记忆数量
     */
    int deleteMemoryBySessionId(String sessionId);
}
