package com.popcorn.agent.core.perception;

import com.popcorn.agent.foundation.AgentConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 核心层 - 感知模块
 * 阶段一极简实现：仅解析用户指令，提取原始内容
 */
@Slf4j
@Service
public class PerceptionService {
    /**
     * 感知解析：处理用户原始指令
     *
     * @param instruction 用户指令
     * @return 解析后的指令内容
     */
    public String perceive(String instruction) {
        log.info("【{}】开始解析用户指令：{}", AgentConstant.STAGE_PERCEPTION, instruction);
        // 阶段一仅做非空校验+简单返回，无复杂NLP解析
        if (instruction == null || instruction.trim().isEmpty()) {
            throw new IllegalArgumentException("用户指令不能为空");
        }
        String result = "解析成功，原始指令：" + instruction.trim();
        log.info("【{}】解析完成：{}", AgentConstant.STAGE_PERCEPTION, result);
        return result;
    }
}