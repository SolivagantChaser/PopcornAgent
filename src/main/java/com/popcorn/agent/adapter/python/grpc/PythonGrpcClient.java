package com.popcorn.agent.adapter.python.grpc;

import com.popcorn.agent.core.adapter.python.grpc.*;
import com.popcorn.agent.foundation.config.GrpcClientProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonGrpcClient {

    private final GrpcClientProperties grpcProperties;

    private ManagedChannel channel;
    private PythonAgentServiceGrpc.PythonAgentServiceBlockingStub blockingStub;

    @PostConstruct
    public void init() {
        String target = grpcProperties.getPythonServer().getTarget();
        log.info("初始化Python gRPC客户端, target={}", target);

        this.channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .keepAliveTime(grpcProperties.getKeepAliveSecond(), TimeUnit.SECONDS)
                .idleTimeout(grpcProperties.getTimeoutSecond(), TimeUnit.SECONDS)
                .build();

        this.blockingStub = PythonAgentServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(grpcProperties.getTimeoutSecond(), TimeUnit.SECONDS);

        log.info("Python gRPC客户端初始化完成");
    }

    // 原有对外方法完全保留，参数、返回值不变
    public String callLlmInfer(String prompt) {
        return callLlmInfer(prompt, "default", Map.of());
    }

    public String callLlmInfer(String prompt, String model) {
        return callLlmInfer(prompt, model, Map.of());
    }

    public String callLlmInfer(String prompt, String model, Map<String, String> parameters) {
        try {
            LlmInferRequest request = LlmInferRequest.newBuilder()
                    .setPrompt(prompt == null ? "" : prompt)
                    .setModel(model == null ? "default" : model)
                    .putAllParameters(parameters == null ? Map.of() : parameters)
                    .build();

            log.debug("调用LlmInfer, model={}, promptLen={}", model, prompt.length());
            LlmInferResponse resp = blockingStub.llmInfer(request);

            if (!resp.getSuccess()) {
                throw new IllegalStateException("Python端返回失败: " + resp.getErrorMsg());
            }
            return resp.getResult();

        } catch (StatusRuntimeException e) {
            log.error("gRPC通信异常: code={}, desc={}", e.getStatus().getCode(), e.getStatus().getDescription());
            throw new RuntimeException("gRPC调用超时或连接异常", e);
        } catch (Exception e) {
            log.error("callLlmInfer异常", e);
            throw new RuntimeException("LLM推理调用失败", e);
        }
    }

    public String callDataProcess(String data, String processType) {
        try {
            DataProcessRequest request = DataProcessRequest.newBuilder()
                    .setData(data == null ? "" : data)
                    .setProcessType(processType == null ? "default" : processType)
                    .build();

            log.debug("调用DataProcess, type={}, dataLen={}", processType, data.length());
            DataProcessResponse resp = blockingStub.dataProcess(request);

            if (!resp.getSuccess()) {
                throw new IllegalStateException("Python数据处理失败: " + resp.getErrorMsg());
            }
            return resp.getResult();

        } catch (StatusRuntimeException e) {
            log.error("gRPC通信异常", e);
            throw new RuntimeException("gRPC调用异常", e);
        } catch (Exception e) {
            log.error("callDataProcess异常", e);
            throw new RuntimeException("数据处理调用失败", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (channel != null && !channel.isShutdown()) {
            try {
                log.info("开始关闭gRPC通道");
                channel.shutdown()
                        .awaitTermination(grpcProperties.getShutdownWaitSecond(), TimeUnit.SECONDS);
                log.info("gRPC通道已优雅关闭");
            } catch (InterruptedException e) {
                log.warn("gRPC通道关闭被中断", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}