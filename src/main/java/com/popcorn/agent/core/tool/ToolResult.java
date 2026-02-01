package com.popcorn.agent.core.tool;

import lombok.Builder;
import lombok.Data;

/**
 * 工具执行结果
 */
@Data
@Builder
public class ToolResult {
    private Boolean success;       // 是否成功
    private Object data;           // 工具返回数据
    private String errorMsg;       // 错误信息
}
