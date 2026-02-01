package com.popcorn.agent.core.tool.impl;

import com.popcorn.agent.adapter.python.grpc.PythonAbilityClient;
import com.popcorn.agent.core.tool.Tool;
import com.popcorn.agent.core.tool.ToolResult;
import com.popcorn.agent.foundation.exception.ToolExecuteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AI工具：调用Python的大模型推理能力
 */
@Slf4j
@Component
public class AITool implements Tool {
    // 工具固定信息
    private static final String TOOL_ID = "ai-tool-001";
    private static final String DESCRIPTION = "智能问答工具，调用大模型回答用户问题，支持通用问答、逻辑推理、文本生成";
    private final PythonAbilityClient pythonAbilityClient;

    public AITool(PythonAbilityClient pythonAbilityClient) {
        this.pythonAbilityClient = pythonAbilityClient;
    }

    @Override
    public ToolResult execute(Map<String, Object> parameters) throws ToolExecuteException {
        try {
            String prompt = (String) parameters.get("prompt");
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new ToolExecuteException("AI工具入参错误：prompt不能为空");
            }
            // 调用Python gRPC客户端的LLM推理能力
            Map<String, String> llmParams = new HashMap<>();
            llmParams.put("temperature", "0.7");
            llmParams.put("max_tokens", "1024");
            String result = pythonAbilityClient.llmInfer(prompt, "gpt-3.5-turbo", llmParams);

            return ToolResult.builder()
                    .success(true)
                    .data(result)
                    .build();
        } catch (Exception e) {
            log.error("AI工具执行失败", e);
            throw new ToolExecuteException("AI工具执行失败：" + e.getMessage(), e);
        }
    }

    @Override
    public String getToolId() {
        return TOOL_ID;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
