package com.popcorn.agent.core.memory.impl;

import com.popcorn.agent.core.memory.MemoryService;
import com.popcorn.agent.core.memory.dto.MemoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 核心层-记忆模块：内存级实现（阶段一基础版）
 * 基于ConcurrentHashMap实现，线程安全，适用于单实例Agent，无需外部存储
 * 纯内存实现，项目重启后记忆丢失（符合阶段一基础要求）
 */
@Slf4j
@Service
public class InMemoryServiceImpl implements MemoryService {
    // 内存存储容器：key=memoryId，value=MemoryDTO（ConcurrentHashMap保证线程安全）
    private final Map<String, MemoryDTO> memoryStorage = new ConcurrentHashMap<>();

    @Override
    public String addMemory(MemoryDTO memoryDTO) {
        // 参数校验（基础非空）
        if (memoryDTO == null || memoryDTO.getSessionId() == null || memoryDTO.getContent() == null) {
            throw new IllegalArgumentException("会话ID[sessionId]和记忆内容[content]不能为空");
        }
        // 生成唯一记忆ID（UUID），初始化时间
        String memoryId = UUID.randomUUID().toString().replace("-", "");
        memoryDTO.setMemoryId(memoryId);
        memoryDTO.initTime();
        // 存入内存
        memoryStorage.put(memoryId, memoryDTO);
        log.info("新增内存记忆成功，记忆ID={}，会话ID={}，记忆类型={}",
                memoryId, memoryDTO.getSessionId(), memoryDTO.getMemoryType());
        return memoryId;
    }

    @Override
    public Optional<MemoryDTO> getMemoryById(String memoryId) {
        if (memoryId == null || memoryId.trim().isEmpty()) {
            return Optional.empty();
        }
        // 查询并更新最后访问时间
        MemoryDTO memory = memoryStorage.get(memoryId);
        if (memory != null) {
            memory.updateAccessTime();
            memoryStorage.put(memoryId, memory);
            log.info("查询内存记忆成功，记忆ID={}", memoryId);
        } else {
            log.warn("查询内存记忆失败，记忆ID={}不存在", memoryId);
        }
        return Optional.ofNullable(memory);
    }

    @Override
    public List<MemoryDTO> listMemoryBySessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        // 按会话ID过滤，按创建时间升序排列
        List<MemoryDTO> memoryList = memoryStorage.values().stream()
                .filter(memory -> sessionId.equals(memory.getSessionId()))
                .sorted(Comparator.comparing(MemoryDTO::getCreateTime))
                .collect(Collectors.toList());
        // 批量更新最后访问时间
        memoryList.forEach(memory -> {
            memory.updateAccessTime();
            memoryStorage.put(memory.getMemoryId(), memory);
        });
        log.info("查询会话关联记忆成功，会话ID={}，记忆数量={}", sessionId, memoryList.size());
        return memoryList;
    }

    @Override
    public boolean deleteMemoryById(String memoryId) {
        if (memoryId == null || memoryId.trim().isEmpty()) {
            return false;
        }
        MemoryDTO removed = memoryStorage.remove(memoryId);
        if (removed != null) {
            log.info("删除内存记忆成功，记忆ID={}", memoryId);
            return true;
        } else {
            log.warn("删除内存记忆失败，记忆ID={}不存在", memoryId);
            return false;
        }
    }

    @Override
    public int deleteMemoryBySessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return 0;
        }
        // 筛选出该会话下的所有记忆ID
        List<String> needDeleteIds = memoryStorage.values().stream()
                .filter(memory -> sessionId.equals(memory.getSessionId()))
                .map(MemoryDTO::getMemoryId)
                .collect(Collectors.toList());
        // 批量删除
        needDeleteIds.forEach(memoryStorage::remove);
        log.info("批量删除会话关联记忆成功，会话ID={}，删除数量={}", sessionId, needDeleteIds.size());
        return needDeleteIds.size();
    }
}