package com.popcorn.agent.app.controller;

import com.popcorn.agent.app.manager.AgentManager;
import com.popcorn.agent.core.agent.AgentRequest;
import com.popcorn.agent.core.agent.AgentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Agent HTTP接口控制器：对外提供RESTful调用入口
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class AgentController {
    private final AgentManager agentManager;

    /**
     * 核心调用接口：执行Agent请求
     *
     * @param request Agent请求参数
     * @return Agent统一响应
     */
    @PostMapping("/execute")
    public AgentResponse execute(@RequestBody AgentRequest request) {
        log.info("接收Agent HTTP请求：agentId={}, rawRequest={}", request.getAgentId(), request.getRawRequest());
        // 基础参数校验
        if (request.getAgentId() == null || request.getAgentId().trim().isEmpty()) {
            return AgentResponse.failure()
                    .errorCode("PARAM_ERROR")
                    .errorMessage("参数错误：agentId不能为空")
                    .build();
        }
        if (request.getRawRequest() == null || request.getRawRequest().trim().isEmpty()) {
            return AgentResponse.failure()
                    .errorCode("PARAM_ERROR")
                    .errorMessage("参数错误：rawRequest不能为空")
                    .build();
        }
        // 调用Agent执行
        try {
            return agentManager.getAgent(request.getAgentId()).execute(request);
        } catch (Exception e) {
            log.error("Agent HTTP接口调用失败", e);
            return AgentResponse.failure()
                    .errorCode("AGENT_CALL_ERROR")
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}