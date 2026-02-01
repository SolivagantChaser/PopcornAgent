package com.popcorn.agent.adapter.python.grpc;

import com.popcorn.agent.core.adapter.python.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Python能力gRPC客户端：Java调用Python服务的统一入口
 */
@Slf4j
@Component
public class PythonAbilityClient {
    // Python gRPC服务配置（从application.yml注入）
    @Value("${python.agent.grpc.host:127.0.0.1}")
    private String pythonGrpcHost;
    @Value("${python.agent.grpc.port:50051}")
    private int pythonGrpcPort;

    // gRPC核心资源
    private ManagedChannel channel;
    private PythonAgentServiceGrpc.PythonAgentServiceBlockingStub blockingStub;

    /**
     * 初始化gRPC通道和存根（懒加载，首次调用时初始化）
     */
    private void initChannel() {
        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            log.info("初始化Python gRPC客户端，连接：{}:{}", pythonGrpcHost, pythonGrpcPort);
            channel = ManagedChannelBuilder.forAddress(pythonGrpcHost, pythonGrpcPort)
                    .usePlaintext() // 开发环境明文传输，生产环境启用TLS
                    .keepAliveTime(30, TimeUnit.SECONDS) // 保活时间
                    .keepAliveTimeout(5, TimeUnit.SECONDS)
                    .build();
            blockingStub = PythonAgentServiceGrpc.newBlockingStub(channel);
            log.info("Python gRPC客户端初始化完成");
        }
    }

    /**
     * 调用Python AI推理能力
     *
     * @param prompt     提示词
     * @param model      模型名称
     * @param parameters 推理参数
     * @return 推理结果
     */
    public String llmInfer(String prompt, String model, Map<String, String> parameters) {
        initChannel();
        long start = System.currentTimeMillis();
        try {
            // 构建gRPC请求
            LlmInferRequest request = LlmInferRequest.newBuilder()
                    .setPrompt(prompt)
                    .setModel(model)
                    .putAllParameters(parameters)
                    .build();
            // 同步调用Python gRPC服务
            LlmInferResponse response = blockingStub.llmInfer(request);
            long cost = System.currentTimeMillis() - start;
            if (response.getSuccess()) {
                log.info("Python AI推理成功，耗时：{}ms，结果长度：{}", cost, response.getResult().length());
                return response.getResult();
            } else {
                throw new RuntimeException("Python AI推理失败：" + response.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("调用Python AI推理能力失败", e);
            throw new RuntimeException("调用Python服务失败：" + e.getMessage(), e);
        }
    }

    /**
     * 调用Python数据处理能力
     *
     * @param data        原始数据（JSON字符串）
     * @param processType 处理类型
     * @return 处理后数据（JSON字符串）
     */
    public String dataProcess(String data, String processType) {
        initChannel();
        long start = System.currentTimeMillis();
        try {
            // 构建gRPC请求
            DataProcessRequest request = DataProcessRequest.newBuilder()
                    .setData(data)
                    .setProcessType(processType)
                    .build();
            // 同步调用Python gRPC服务
            DataProcessResponse response = blockingStub.dataProcess(request);
            long cost = System.currentTimeMillis() - start;
            if (response.getSuccess()) {
                log.info("Python数据处理成功，耗时：{}ms", cost);
                return response.getProcessedData();
            } else {
                throw new RuntimeException("Python数据处理失败：" + response.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("调用Python数据处理能力失败", e);
            throw new RuntimeException("调用Python服务失败：" + e.getMessage(), e);
        }
    }

    /**
     * 项目关闭时关闭gRPC通道，释放资源
     */
    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            log.info("开始关闭Python gRPC通道");
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                log.info("Python gRPC通道已成功关闭");
            } catch (InterruptedException e) {
                log.error("关闭Python gRPC通道被中断", e);
                Thread.currentThread().interrupt();
                channel.shutdownNow();
            }
        }
    }
}