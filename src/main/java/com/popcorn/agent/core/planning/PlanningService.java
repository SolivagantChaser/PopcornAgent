package com.popcorn.agent.core.planning;

import com.popcorn.agent.adapter.python.grpc.PythonGrpcClient;
import com.popcorn.agent.foundation.AgentConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 核心层 - 规划模块
 * 阶段一极简实现：调用Python LLM生成基础执行步骤
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanningService {
    private final PythonGrpcClient pythonGrpcClient;

    /**
     * 规划生成：基于解析后的指令生成执行步骤
     *
     * @param perceiveResult 感知模块解析结果
     * @return 执行步骤规划
     */
    public String plan(String perceiveResult) {
        log.info("【{}】开始生成执行规划", AgentConstant.STAGE_PLANNING);
        // 构建规划提示词
        String prompt = "请根据以下解析后的用户指令，生成1-2步简单执行步骤，仅返回步骤内容：" + perceiveResult;
        // 调用Python LLM生成规划
        String planResult = pythonGrpcClient.callLlmInfer(prompt);
        log.info("【{}】规划生成完成：{}", AgentConstant.STAGE_PLANNING, planResult);
        return planResult;
    }
}