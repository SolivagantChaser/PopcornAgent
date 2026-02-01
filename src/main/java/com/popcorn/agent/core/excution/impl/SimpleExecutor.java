package com.popcorn.agent.core.excution.impl;

import com.popcorn.agent.core.excution.ExecutionResult;
import com.popcorn.agent.core.excution.Executor;
import com.popcorn.agent.core.memory.Memory;
import com.popcorn.agent.core.planning.Plan;
import com.popcorn.agent.core.planning.PlanStep;
import com.popcorn.agent.core.tool.Tool;
import com.popcorn.agent.core.tool.ToolResult;
import com.popcorn.agent.core.tool.impl.DefaultToolRegistry;
import com.popcorn.agent.foundation.exception.ToolExecuteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单执行实现：按顺序执行规划步骤，调用对应工具
 */
@Slf4j
@Component
public class SimpleExecutor implements Executor {
    private final DefaultToolRegistry toolRegistry;

    public SimpleExecutor(DefaultToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    @Override
    public ExecutionResult execute(Plan plan, Memory memory) {
        Map<String, Object> stepResults = new HashMap<>();
        String finalResult = null;
        try {
            for (PlanStep step : plan.getSteps()) {
                log.info("开始执行步骤：{} - {}", step.getStepNo(), step.getStepName());
                // 获取工具并执行
                Tool tool = toolRegistry.getTool(step.getToolId());
                if (tool == null) {
                    throw new ToolExecuteException("步骤执行失败：未找到工具" + step.getToolId());
                }
                ToolResult toolResult = tool.execute(step.getToolParams());
                if (!toolResult.getSuccess()) {
                    throw new ToolExecuteException("步骤执行失败：" + toolResult.getErrorMsg());
                }
                // 记录步骤结果
                String stepKey = "step_" + step.getStepNo();
                stepResults.put(stepKey, toolResult.getData());
                finalResult = toolResult.getData().toString(); // 基础版：直接取工具结果作为最终结果
                log.info("步骤执行成功：{}，结果：{}", stepKey, toolResult.getData());
            }
            // 构建成功执行结果
            return ExecutionResult.builder()
                    .success(true)
                    .finalResult(finalResult)
                    .stepResults(stepResults)
                    .build();
        } catch (Exception e) {
            log.error("规划执行失败", e);
            // 构建失败执行结果
            return ExecutionResult.builder()
                    .success(false)
                    .stepResults(stepResults)
                    .errorMsg(e.getMessage())
                    .build();
        }
    }
}
