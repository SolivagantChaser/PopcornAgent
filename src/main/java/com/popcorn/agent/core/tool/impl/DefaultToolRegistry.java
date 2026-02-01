package com.popcorn.agent.core.tool.impl;

import com.popcorn.agent.core.tool.Tool;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具注册器：自动注册Spring容器中的所有Tool实现类
 */
@Slf4j
@Component
public class DefaultToolRegistry {
    private final ApplicationContext applicationContext;
    // 存储已注册工具，key=toolId
    private final Map<String, Tool> toolMap = new HashMap<>();

    // 延迟注入避免循环依赖
    public DefaultToolRegistry(@Lazy ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 项目启动时自动注册所有Tool实现类
     */
    @PostConstruct
    public void init() {
        Map<String, Tool> allTools = applicationContext.getBeansOfType(Tool.class);
        allTools.forEach((beanName, tool) -> {
            toolMap.put(tool.getToolId(), tool);
            log.info("自动注册工具：{}({}) - {}", beanName, tool.getToolId(), tool.getDescription());
        });
        log.info("工具注册完成，共注册{}个工具", toolMap.size());
    }

    /**
     * 根据ToolId获取工具
     */
    public Tool getTool(String toolId) {
        return toolMap.get(toolId);
    }

    /**
     * 获取所有已注册工具
     */
    public List<Tool> listAllTools() {
        return new ArrayList<>(toolMap.values());
    }
}
