package com.popcorn.agent.core.memory.impl;

import com.popcorn.agent.core.memory.Memory;
import com.popcorn.agent.core.memory.MemoryEntry;
import com.popcorn.agent.core.memory.MemoryQueryCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 内存版记忆实现（无需中间件，快速运行）
 */
@Slf4j
@Component
public class InMemoryMemory implements Memory {
    // 线程安全的列表存储记忆，CopyOnWriteArrayList适合读多写少场景
    private final List<MemoryEntry> memoryStore = new CopyOnWriteArrayList<>();

    @Override
    public void save(MemoryEntry memoryEntry) {
        memoryStore.add(memoryEntry);
        log.info("保存记忆：{}", memoryEntry);
    }

    @Override
    public List<MemoryEntry> query(MemoryQueryCondition condition) {
        // 简单过滤查询，可根据实际需求扩展
        return memoryStore.stream()
                .filter(entry -> condition.getAgentId() == null || entry.getAgentId().equals(condition.getAgentId()))
                .filter(entry -> condition.getType() == null || entry.getType() == condition.getType())
                .filter(entry -> condition.getStartTime() == null || entry.getTimestamp() >= condition.getStartTime())
                .filter(entry -> condition.getEndTime() == null || entry.getTimestamp() <= condition.getEndTime())
                .collect(Collectors.toList());
    }

    @Override
    public void update(MemoryEntry memoryEntry) {
        for (int i = 0; i < memoryStore.size(); i++) {
            MemoryEntry entry = memoryStore.get(i);
            if (entry.getAgentId().equals(memoryEntry.getAgentId()) && entry.getTimestamp().equals(memoryEntry.getTimestamp())) {
                memoryStore.set(i, memoryEntry);
                log.info("更新记忆：{}", memoryEntry);
                return;
            }
        }
        log.warn("未找到待更新的记忆：{}", memoryEntry);
    }

    @Override
    public void evict() {
        // 简单淘汰：保留最近100条记忆，避免内存溢出
        if (memoryStore.size() > 100) {
            List<MemoryEntry> toRemove = new ArrayList<>(memoryStore.subList(0, memoryStore.size() - 100));
            memoryStore.removeAll(toRemove);
            log.info("淘汰过期记忆，剩余{}条", memoryStore.size());
        }
    }
}
