package com.popcorn.agent.core.reflection.impl;

import com.popcorn.agent.core.excution.ExecutionResult;
import com.popcorn.agent.core.planning.Plan;
import com.popcorn.agent.core.reflection.ReflectionResult;
import com.popcorn.agent.core.reflection.Reflector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 简单反思实现：根据执行结果做基础评估，生成简单优化建议
 */
@Slf4j
@Component
public class SimpleReflector implements Reflector {
    @Override
    public ReflectionResult reflect(ExecutionResult executionResult, Plan plan) {
        if (executionResult.getSuccess()) {
            // 执行成功：高分，无错误分析
            return ReflectionResult.builder()
                    .score(90)
                    .errorAnalysis("无")
                    .optimizationSuggestion("执行成功，建议保留当前执行策略，可针对同类请求复用规划模板")
                    .build();
        } else {
            // 执行失败：低分，错误分析为执行异常信息
            return ReflectionResult.builder()
                    .score(30)
                    .errorAnalysis(executionResult.getErrorMsg())
                    .optimizationSuggestion("执行失败，建议检查工具入参是否正确、工具服务是否可用，或更换备选工具")
                    .build();
        }
    }
}
