package com.popcorn.agent.core.agent;

import com.popcorn.agent.core.planning.PlanStep;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Agent统一响应结果
 */
@Data
@Builder
public class AgentResponse {
    private Boolean success;       // 接口是否成功
    private String errorCode;      // 错误码
    private String errorMessage;   // 错误信息
    private String answer;         // Agent最终答案
    private List<PlanStep> executeSteps; // 执行步骤
    private String reflectionSuggestion; // 反思建议

    // 成功响应快捷方法
    public static AgentResponseBuilder success() {
        return AgentResponse.builder().success(true);
    }

    // 失败响应快捷方法
    public static AgentResponseBuilder failure() {
        return AgentResponse.builder().success(false);
    }
}
