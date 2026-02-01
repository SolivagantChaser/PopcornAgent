package com.popcorn.agent.core.tool;

import java.util.List;

/**
 * 工具注册器：管理所有工具，实现动态注册和发现
 */
public interface ToolRegistry {
    /**
     * 注册工具
     *
     * @param tool 工具实例
     */
    void register(Tool tool);

    /**
     * 根据工具ID获取工具实例
     *
     * @param toolId 工具ID
     * @return 工具实例（不存在则返回null）
     */
    Tool getTool(String toolId);

    /**
     * 获取所有已注册工具
     *
     * @return 工具实例列表
     */
    List<Tool> listAllTools();

    /**
     * 注销工具
     *
     * @param toolId 工具ID
     */
    void unregister(String toolId);
}

