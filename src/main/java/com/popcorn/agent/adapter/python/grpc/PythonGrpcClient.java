package com.popcorn.agent.adapter.python.grpc;

import com.popcorn.agent.core.adapter.python.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 适配层 - Python gRPC客户端（与proto文件1:1严格匹配）
 * 核心修正：存根类、方法名完全对齐proto定义，无任何命名偏差
 * 阶段一极简实现：仅基础调用，无重试/超时/降级
 */
@Slf4j
@Component
public class PythonGrpcClient {
    // Python gRPC服务地址（与Python服务端一致，阶段一固定）
    private static final String PYTHON_GRPC_SERVER = "127.0.0.1:50051";
    // gRPC通信通道（单例复用，避免频繁创建）
    private ManagedChannel channel;
    // gRPC服务存根（核心调用入口，严格匹配proto服务名）
    private PythonAgentServiceGrpc.PythonAgentServiceBlockingStub blockingStub;

    /**
     * 初始化gRPC通道和存根（项目启动时执行）
     * 关键：通过proto生成的PythonAgentServiceGrpc创建存根，1:1匹配
     */
    @PostConstruct
    public void initGrpcClient() {
        log.info("开始初始化Python gRPC客户端，服务地址：{}", PYTHON_GRPC_SERVER);
        // 构建明文通道（开发环境，与Python服务端一致）
        this.channel = ManagedChannelBuilder.forTarget(PYTHON_GRPC_SERVER)
                .usePlaintext() // 明文连接，禁止修改（Python服务端为无加密）
                .keepAliveTime(30, TimeUnit.SECONDS)
                .build();
        // 创建阻塞式存根（阶段一使用阻塞调用，简单直接）
        this.blockingStub = PythonAgentServiceGrpc.newBlockingStub(channel);
        log.info("Python gRPC客户端初始化成功，存根创建完成");
    }

    /**
     * 调用Python LLM推理能力（严格匹配proto方法：LlmInfer → llmInfer）
     *
     * @param prompt     推理提示词
     * @param model      模型名称
     * @param parameters 模型参数（map格式，与proto一致）
     * @return Python返回的推理结果
     */
    public String callLlmInfer(String prompt, String model, Map<String, String> parameters) {
        try {
            // 构建请求对象（严格匹配proto的LlmInferRequest）
            LlmInferRequest request = LlmInferRequest.newBuilder()
                    .setPrompt(prompt)
                    .setModel(model == null ? "default" : model)
                    .putAllParameters(parameters == null ? Map.of() : parameters)
                    .build();
            log.info("调用Python gRPC LlmInfer方法，提示词长度：{}，模型：{}", prompt.length(), model);
            // 核心：调用生成的llmInfer方法（小驼峰，与proto严格匹配）
            LlmInferResponse response = blockingStub.llmInfer(request);
            // 解析响应（严格匹配proto的LlmInferResponse）
            if (response.getSuccess()) {
                log.info("Python LlmInfer调用成功，结果长度：{}", response.getResult().length());
                return response.getResult();
            } else {
                String errorMsg = response.getErrorMsg().isEmpty() ? "未知错误" : response.getErrorMsg();
                log.error("Python LlmInfer调用失败：{}", errorMsg);
                throw new RuntimeException("Python LLM推理失败：" + errorMsg);
            }
        } catch (Exception e) {
            log.error("Python gRPC LlmInfer方法调用异常", e);
            throw new RuntimeException("Python gRPC通信异常（LlmInfer）：" + e.getMessage());
        }
    }

    /**
     * 调用Python数据处理能力（严格匹配proto方法：DataProcess → dataProcess）
     *
     * @param data        原始处理数据
     * @param processType 处理类型（与proto的process_type一致）
     * @return Python返回的处理结果
     */
    public String callDataProcess(String data, String processType) {
        try {
            // 构建请求对象（严格匹配proto的DataProcessRequest）
            DataProcessRequest request = DataProcessRequest.newBuilder()
                    .setData(data)
                    .setProcessType(processType)
                    .build();
            log.info("调用Python gRPC DataProcess方法，处理类型：{}，数据长度：{}", processType, data.length());
            // 核心：调用生成的dataProcess方法（小驼峰，与proto严格匹配）
            DataProcessResponse response = blockingStub.dataProcess(request);
            // 解析响应（严格匹配proto的DataProcessResponse）
            if (response.getSuccess()) {
                log.info("Python DataProcess调用成功");
                return response.getResult();
            } else {
                String errorMsg = response.getErrorMsg().isEmpty() ? "未知错误" : response.getErrorMsg();
                log.error("Python DataProcess调用失败：{}", errorMsg);
                throw new RuntimeException("Python数据处理失败：" + errorMsg);
            }
        } catch (Exception e) {
            log.error("Python gRPC DataProcess方法调用异常", e);
            throw new RuntimeException("Python gRPC通信异常（DataProcess）：" + e.getMessage());
        }
    }

    /**
     * 重载方法：默认参数调用LLM推理（简化上层调用，阶段一实用版）
     */
    public String callLlmInfer(String prompt) {
        return callLlmInfer(prompt, "default", Map.of());
    }

    /**
     * 重载方法：指定模型，默认参数调用LLM推理（简化上层调用）
     */
    public String callLlmInfer(String prompt, String model) {
        return callLlmInfer(prompt, model, Map.of());
    }

    /**
     * 优雅关闭gRPC通道（项目关闭时执行，避免连接泄漏）
     */
    @PreDestroy
    public void shutdownGrpcClient() {
        if (channel != null && !channel.isShutdown()) {
            log.info("开始关闭Python gRPC客户端通道");
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                log.info("Python gRPC客户端通道关闭成功");
            } catch (InterruptedException e) {
                log.error("关闭Python gRPC通道异常", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}