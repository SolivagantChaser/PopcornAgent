package com.popcorn.agent.core.tool;

import com.popcorn.agent.foundation.exception.ToolExecuteException;

import java.util.Map;

/**
 * 工具模块采用 **“注册 - 发现 - 调用”设计，
 * 所有工具实现统一接口，通过工厂模式 ** 注册，
 * Agent 可动态发现并调用工具，实现 “可插拔” 扩展
 */
public interface Tool {
    /**
     * 工具执行方法
     *
     * @param parameters 工具入参（键值对，通用格式）
     * @return 工具执行结果
     * @throws ToolExecuteException 工具执行异常
     */
    ToolResult execute(Map<String, Object> parameters) throws ToolExecuteException;

    /**
     * 获取工具唯一标识
     *
     * @return 工具ID
     */
    String getToolId();

    /**
     * 获取工具描述（用于大模型选择工具）
     *
     * @return 工具描述（包含功能、入参、出参）
     */
    String getDescription();
}