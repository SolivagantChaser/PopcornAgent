package com.popcorn.agent.core.reflection.impl;

import com.popcorn.agent.adapter.python.grpc.PythonGrpcClient;
import com.popcorn.agent.core.memory.MemoryService;
import com.popcorn.agent.core.memory.dto.MemoryDTO;
import com.popcorn.agent.core.reflection.ReflectionService;
import com.popcorn.agent.core.reflection.dto.ReflectionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 核心层-反射模块：基础实现（阶段一）
 * 功能：执行后反思、结果合理性判断、步骤有效性分析、简单优化建议、联动记忆存储
 * 依赖：PythonGrpcClient（增强反思能力）、MemoryService（保存反思结果）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BasicReflectionServiceImpl implements ReflectionService {
    // 内存存储反射记录（线程安全，阶段一基础版，与Memory模块存储逻辑一致）
    private final Map<String, ReflectionDTO> reflectionStorage = new ConcurrentHashMap<>();
    // 注入Python gRPC客户端（增强反思能力，调用Python生成专业反思结论/建议）
    private final PythonGrpcClient pythonGrpcClient;
    // 注入记忆服务（可选，将反思结果存入记忆模块，实现反射-记忆联动）
    private final MemoryService memoryService;

    @Override
    public ReflectionDTO reflect(ReflectionDTO reflectionDTO) {
        if (reflectionDTO == null || reflectionDTO.getSessionId() == null || reflectionDTO.getExecuteResult() == null) {
            throw new IllegalArgumentException("会话ID[sessionId]和执行结果[executeResult]不能为空，无法触发反射");
        }
        log.info("【反射阶段】开始触发Agent反思，会话ID={}，AgentID={}", reflectionDTO.getSessionId(), reflectionDTO.getAgentId());

        // 步骤1：基础校验（结果非空、链路数据完整）
        String checkResult = checkLinkData(reflectionDTO);
        // 步骤2：调用Python gRPC增强反思，生成核心结论+优化建议（阶段一核心能力）
        String[] reflectionResult = generateReflectionByPython(reflectionDTO);
        // 步骤3：封装完整反射结果
        reflectionDTO.setReflectionConclusion(checkResult + "；" + reflectionResult[0]);
        reflectionDTO.setOptimizeSuggestion(reflectionResult[1]);
        // 步骤4：初始化时间+生成唯一ID，存入内存
        String reflectionId = UUID.randomUUID().toString().replace("-", "");
        reflectionDTO.setReflectionId(reflectionId);
        reflectionDTO.initTime();
        reflectionStorage.put(reflectionId, reflectionDTO);
        // 步骤5：联动记忆模块，将反思结果存入记忆（记忆类型：REFLECTION）
        saveReflectionToMemory(reflectionDTO);

        log.info("【反射阶段】反思完成，会话ID={}，反射ID={}，结论：{}",
                reflectionDTO.getSessionId(), reflectionId, reflectionDTO.getReflectionConclusion());
        return reflectionDTO;
    }

    @Override
    public Optional<ReflectionDTO> getReflectionById(String reflectionId) {
        if (reflectionId == null || reflectionId.trim().isEmpty()) {
            return Optional.empty();
        }
        ReflectionDTO reflection = reflectionStorage.get(reflectionId);
        if (reflection != null) {
            log.info("【反射模块】查询反射记录成功，反射ID={}", reflectionId);
        } else {
            log.warn("【反射模块】查询反射记录失败，反射ID={}不存在", reflectionId);
        }
        return Optional.ofNullable(reflection);
    }

    @Override
    public List<ReflectionDTO> listReflectionBySessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<ReflectionDTO> reflectionList = reflectionStorage.values().stream()
                .filter(reflection -> sessionId.equals(reflection.getSessionId()))
                .sorted(Comparator.comparing(ReflectionDTO::getCreateTime))
                .collect(Collectors.toList());
        log.info("【反射模块】查询会话关联反射记录成功，会话ID={}，反射记录数量={}", sessionId, reflectionList.size());
        return reflectionList;
    }

    @Override
    public String saveReflection(ReflectionDTO reflectionDTO) {
        if (reflectionDTO == null || reflectionDTO.getSessionId() == null) {
            throw new IllegalArgumentException("会话ID[sessionId]不能为空，无法保存反射记录");
        }
        String reflectionId = UUID.randomUUID().toString().replace("-", "");
        reflectionDTO.setReflectionId(reflectionId);
        reflectionDTO.initTime();
        reflectionStorage.put(reflectionId, reflectionDTO);
        // 联动记忆存储
        saveReflectionToMemory(reflectionDTO);
        log.info("【反射模块】手动保存反射记录成功，反射ID={}，会话ID={}", reflectionId, reflectionDTO.getSessionId());
        return reflectionId;
    }

    /**
     * 私有方法：校验Agent执行全链路数据是否完整
     */
    private String checkLinkData(ReflectionDTO reflectionDTO) {
        List<String> emptyFields = new ArrayList<>();
        if (reflectionDTO.getOriginalInstruction() == null) emptyFields.add("原始用户指令");
        if (reflectionDTO.getPerceiveResult() == null) emptyFields.add("感知阶段结果");
        if (reflectionDTO.getPlanResult() == null) emptyFields.add("规划阶段步骤");
        return emptyFields.isEmpty()
                ? "全链路数据完整，结果有效性校验通过"
                : "全链路数据不完整，缺失：" + String.join("、", emptyFields) + "，结果有效性校验存疑";
    }

    /**
     * 私有方法：调用Python gRPC生成专业反思结论和优化建议
     *
     * @return 数组：[0]反思结论，[1]优化建议
     */
    private String[] generateReflectionByPython(ReflectionDTO reflectionDTO) {
        try {
            // 构建Python反思提示词，包含全链路数据
            String prompt = String.format(
                    "请作为Agent反思助手，分析以下执行全链路数据：1.原始指令：%s；2.感知结果：%s；3.规划步骤：%s；4.执行结果：%s。要求：1.给出1句话反思结论（判断结果是否匹配指令、步骤是否有效）；2.给出1-2条简单优化建议（如何提升结果匹配度/步骤合理性），结论和建议用###分隔",
                    reflectionDTO.getOriginalInstruction(),
                    reflectionDTO.getPerceiveResult(),
                    reflectionDTO.getPlanResult(),
                    reflectionDTO.getExecuteResult()
            );
            // 调用Python LlmInfer方法（已修正的客户端，1:1匹配proto）
            String pythonResult = pythonGrpcClient.callLlmInfer(prompt, "reflection", Map.of());
            // 解析Python返回结果
            String[] resultArray = pythonResult.split("###");
            return new String[]{
                    resultArray.length >= 1 ? resultArray[0].trim() : "未生成有效反思结论",
                    resultArray.length >= 2 ? resultArray[1].trim() : "暂未生成优化建议"
            };
        } catch (Exception e) {
            log.error("【反射阶段】调用Python增强反思失败，使用本地默认反思结果", e);
            return new String[]{
                    "调用外部能力失败，本地校验：执行结果与原始指令初步匹配，步骤有效性待验证",
                    "优化建议：1.提升规划步骤的针对性；2.增强执行结果与原始指令的匹配度"
            };
        }
    }

    /**
     * 私有方法：将反射结果存入记忆模块，记忆类型标识为REFLECTION
     */
    private void saveReflectionToMemory(ReflectionDTO reflectionDTO) {
        try {
            String memoryContent = String.format(
                    "反射结论：%s；优化建议：%s",
                    reflectionDTO.getReflectionConclusion(),
                    reflectionDTO.getOptimizeSuggestion()
            );
            memoryService.addMemory(MemoryDTO.builder()
                    .sessionId(reflectionDTO.getSessionId())
                    .memoryType("REFLECTION")
                    .content(memoryContent)
                    .build());
            log.info("【反射-记忆联动】反射结果成功存入记忆模块，会话ID={}", reflectionDTO.getSessionId());
        } catch (Exception e) {
            log.warn("【反射-记忆联动】反射结果存入记忆模块失败", e);
        }
    }
}