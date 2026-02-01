package com.popcorn.agent.core.planning.impl;

import com.popcorn.agent.core.memory.Memory;
import com.popcorn.agent.core.perception.PerceptionResult;
import com.popcorn.agent.core.planning.Plan;
import com.popcorn.agent.core.planning.PlanStep;
import com.popcorn.agent.core.planning.Planner;
import com.popcorn.agent.core.tool.Tool;
import com.popcorn.agent.core.tool.impl.DefaultToolRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 简单规划实现：根据用户意图匹配对应工具，生成单步骤执行规划
 */
@Slf4j
@Component
public class SimplePlanner implements Planner {
    private final DefaultToolRegistry toolRegistry;

    public SimplePlanner(DefaultToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    @Override
    public Plan plan(PerceptionResult perceptionResult, Memory memory) {
        String intent = perceptionResult.getIntent();
        String question = perceptionResult.getQuestion();
        // 根据意图匹配工具
        Tool targetTool;
        HashMap<String, Object> toolParams = new HashMap<>();
        if ("智能问答".equals(intent)) {
            targetTool = toolRegistry.getTool("ai-tool-001");
            toolParams.put("prompt", question);
        } else if ("数据处理".equals(intent)) {
            targetTool = toolRegistry.getTool("data-tool-001");
            toolParams.put("data", question); // 基础版：直接将请求作为数据（实际需传JSON）
            toolParams.put("processType", "clean"); // 默认清洗
        } else {
            throw new RuntimeException("不支持的用户意图：" + intent);
        }

        if (targetTool == null) {
            throw new RuntimeException("未找到匹配的工具，意图：" + intent);
        }

        // 生成单步骤执行规划
        PlanStep step = PlanStep.builder()
                .stepNo(1)
                .stepName(targetTool.getDescription())
                .toolId(targetTool.getToolId())
                .toolParams(toolParams)
                .description("调用" + targetTool.getToolId() + "处理请求：" + question)
                .build();
        List<PlanStep> steps = new ArrayList<>();
        steps.add(step);

        Plan plan = Plan.builder()
                .planId(UUID.randomUUID().toString())
                .steps(steps)
                .description("根据用户意图[" + intent + "]生成的单步骤执行规划")
                .build();
        log.info("规划生成完成，规划：{}", plan);
        return plan;
    }
}
