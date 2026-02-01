package com.popcorn.agent.core.tool.impl;

import com.popcorn.agent.adapter.python.grpc.PythonAbilityClient;
import com.popcorn.agent.core.tool.Tool;
import com.popcorn.agent.core.tool.ToolResult;
import com.popcorn.agent.foundation.exception.ToolExecuteException;
import com.popcorn.agent.foundation.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 数据处理工具：调用Python的结构化数据处理能力
 */
@Slf4j
@Component
public class DataProcessTool implements Tool {
    // 工具固定信息
    private static final String TOOL_ID = "data-tool-001";
    private static final String DESCRIPTION = "数据处理工具，支持JSON格式结构化数据的清洗、转换，输入为JSON字符串，输出为处理后的JSON字符串";
    private final PythonAbilityClient pythonAbilityClient;
    private final JsonUtil jsonUtil;

    public DataProcessTool(PythonAbilityClient pythonAbilityClient, JsonUtil jsonUtil) {
        this.pythonAbilityClient = pythonAbilityClient;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public ToolResult execute(Map<String, Object> parameters) throws ToolExecuteException {
        try {
            String data = (String) parameters.get("data");
            String processType = (String) parameters.get("processType");
            if (data == null || processType == null) {
                throw new ToolExecuteException("数据处理工具入参错误：data和processType不能为空");
            }
            // 调用Python gRPC客户端的数据处理能力
            String processedData = pythonAbilityClient.dataProcess(data, processType);

            return ToolResult.builder()
                    .success(true)
                    .data(jsonUtil.toMap(processedData)) // 转Map方便后续处理
                    .build();
        } catch (Exception e) {
            log.error("数据处理工具执行失败", e);
            throw new ToolExecuteException("数据处理工具执行失败：" + e.getMessage(), e);
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
