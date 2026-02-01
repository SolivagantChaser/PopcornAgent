package com.popcorn.agent.core.excution;

import com.popcorn.agent.adapter.python.grpc.PythonGrpcClient;
import com.popcorn.agent.foundation.AgentConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 核心层 - 执行引擎
 * 阶段一极简实现：执行规划步骤，调用Python能力完成最终任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExecutionEngine {
    private final PythonGrpcClient pythonGrpcClient;

    /**
     * 执行规划：根据规划步骤执行并返回最终结果
     *
     * @param planResult 规划模块生成的步骤
     * @return 最终执行结果
     */
    public String execute(String planResult) {
        log.info("【{}】开始执行规划步骤", AgentConstant.STAGE_EXECUTION);
        // 构建执行提示词
        String prompt = "请根据以下执行步骤，直接给出用户指令的最终答案，简洁明了：" + planResult;
        // 调用Python LLM执行并返回结果
        String executeResult = pythonGrpcClient.callLlmInfer(prompt);
        log.info("【{}】执行完成，最终结果：{}", AgentConstant.STAGE_EXECUTION, executeResult);
        return executeResult;
    }
}