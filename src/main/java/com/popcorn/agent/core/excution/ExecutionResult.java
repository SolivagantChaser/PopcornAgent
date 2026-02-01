package com.popcorn.agent.core.excution;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 执行结果
 */
@Data
@Builder
public class ExecutionResult {
    private Boolean success;       // 是否成功
    private String finalResult;    // 最终结果
    private Map<String, Object> stepResults; // 各步骤执行结果
    private String errorMsg;       // 错误信息
}
