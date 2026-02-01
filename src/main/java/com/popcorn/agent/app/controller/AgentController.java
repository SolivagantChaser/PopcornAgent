package com.popcorn.agent.app.controller;

import com.popcorn.agent.app.manager.AgentManager;
import com.popcorn.agent.core.memory.MemoryService;
import com.popcorn.agent.core.memory.dto.MemoryDTO;
import com.popcorn.agent.core.reflection.ReflectionService;
import com.popcorn.agent.core.reflection.dto.ReflectionDTO;
import com.popcorn.agent.foundation.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 应用层 - Agent核心HTTP接口控制器
 * 包含：核心执行（简易/全链路）、记忆模块全套接口、反射模块全套接口
 * 外部唯一调用入口，所有接口返回标准化Result对象
 */
@Slf4j
@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {
    // 注入核心依赖：Agent管理器、记忆服务、反射服务
    private final AgentManager agentManager;
    private final MemoryService memoryService;
    private final ReflectionService reflectionService;

    // ==================== 一、Agent核心执行接口 ====================

    /**
     * 简易执行接口（无会话ID，兼容原有调用，自动生成临时会话ID）
     * POST /api/agent/execute
     * 请求参数：{"instruction": "你的用户指令"}
     * 返回值：执行结果（自动触发反射+记忆存储，不感知会话ID）
     */
    @PostMapping("/execute")
    public Result<String> execute(@RequestBody Map<String, String> param) {
        long startTime = System.currentTimeMillis();
        try {
            // 获取并校验用户指令
            String instruction = param.get("instruction");
            if (instruction == null || instruction.trim().isEmpty()) {
                return Result.fail("用户指令[instruction]不能为空");
            }
            log.info("接收Agent简易调用请求，用户指令：{}", instruction);

            // 调用默认Agent执行（自动生成临时会话ID）
            com.popcorn.agent.core.agent.impl.BasicAgent agent =
                    (com.popcorn.agent.core.agent.impl.BasicAgent) agentManager.getDefaultAgent();
            String result = agent.execute(instruction);

            // 计算耗时并返回成功响应
            long costTime = System.currentTimeMillis() - startTime;
            log.info("Agent简易调用完成，总耗时：{}ms", costTime);
            return Result.success(result, costTime);
        } catch (Exception e) {
            // 异常处理，返回失败响应
            long costTime = System.currentTimeMillis() - startTime;
            log.error("Agent简易调用失败，总耗时：{}ms", costTime, e);
            return Result.fail("Agent执行失败：" + e.getMessage());
        }
    }

    /**
     * 全链路执行接口（带会话ID，感知→规划→执行→反射→记忆全联动）
     * POST /api/agent/execute/full
     * 请求参数：{"instruction": "你的用户指令", "sessionId": "自定义会话ID"}
     * 返回值：执行结果（反射结果、执行结果自动关联会话ID存入记忆）
     */
    @PostMapping("/execute/full")
    public Result<String> executeFullLink(@RequestBody Map<String, String> param) {
        long startTime = System.currentTimeMillis();
        try {
            // 获取并校验请求参数（指令+会话ID为必传）
            String instruction = param.get("instruction");
            String sessionId = param.get("sessionId");
            if (instruction == null || instruction.trim().isEmpty()) {
                return Result.fail("用户指令[instruction]不能为空");
            }
            if (sessionId == null || sessionId.trim().isEmpty()) {
                return Result.fail("会话ID[sessionId]不能为空，全链路调用需指定会话ID");
            }
            log.info("接收Agent全链路调用请求，会话ID={}，用户指令：{}", sessionId, instruction);

            // 调用默认Agent的全链路执行方法
            com.popcorn.agent.core.agent.impl.BasicAgent agent =
                    (com.popcorn.agent.core.agent.impl.BasicAgent) agentManager.getDefaultAgent();
            String result = agent.execute(instruction, sessionId);

            // 计算耗时并返回成功响应
            long costTime = System.currentTimeMillis() - startTime;
            log.info("Agent全链路调用完成，会话ID={}，总耗时：{}ms", sessionId, costTime);
            return Result.success(result, costTime);
        } catch (Exception e) {
            // 异常处理，返回失败响应
            long costTime = System.currentTimeMillis() - startTime;
            log.error("Agent全链路调用失败，总耗时：{}ms", costTime, e);
            return Result.fail("Agent全链路执行失败：" + e.getMessage());
        }
    }

    // ==================== 二、记忆模块全套接口（增/查/删） ====================

    /**
     * 新增记忆
     * POST /api/agent/memory/add
     * 请求参数：MemoryDTO全字段（sessionId/memoryType/content为必传）
     * 返回值：新增的记忆唯一标识memoryId
     */
    @PostMapping("/memory/add")
    public Result<String> addMemory(@RequestBody MemoryDTO memoryDTO) {
        try {
            String memoryId = memoryService.addMemory(memoryDTO);
            log.info("新增记忆成功，记忆ID={}，会话ID={}", memoryId, memoryDTO.getSessionId());
            return Result.success(memoryId, 0L);
        } catch (Exception e) {
            log.error("新增记忆失败", e);
            return Result.fail("新增记忆失败：" + e.getMessage());
        }
    }

    /**
     * 根据记忆ID查询单条记忆
     * GET /api/agent/memory/get/{memoryId}
     * 路径参数：memoryId（记忆唯一标识）
     * 返回值：对应的MemoryDTO对象（无则返回提示）
     */
    @GetMapping("/memory/get/{memoryId}")
    public Result<MemoryDTO> getMemoryById(@PathVariable String memoryId) {
        try {
            Optional<MemoryDTO> memory = memoryService.getMemoryById(memoryId);
            return memory.map(dto -> Result.success(dto, 0L))
                    .orElse(Result.fail("记忆记录不存在，记忆ID=" + memoryId));
        } catch (Exception e) {
            log.error("查询记忆失败，记忆ID={}", memoryId, e);
            return Result.fail("查询记忆失败：" + e.getMessage());
        }
    }

    /**
     * 根据会话ID查询所有关联记忆（按创建时间升序）
     * GET /api/agent/memory/list/{sessionId}
     * 路径参数：sessionId（会话唯一标识）
     * 返回值：该会话下的所有记忆列表（无则返回空列表）
     */
    @GetMapping("/memory/list/{sessionId}")
    public Result<List<MemoryDTO>> listMemoryBySessionId(@PathVariable String sessionId) {
        try {
            List<MemoryDTO> memoryList = memoryService.listMemoryBySessionId(sessionId);
            log.info("查询会话记忆成功，会话ID={}，记忆数量={}", sessionId, memoryList.size());
            return Result.success(memoryList, 0L);
        } catch (Exception e) {
            log.error("查询会话记忆失败，会话ID={}", sessionId, e);
            return Result.fail("查询会话记忆失败：" + e.getMessage());
        }
    }

    /**
     * 根据记忆ID删除单条记忆
     * DELETE /api/agent/memory/delete/{memoryId}
     * 路径参数：memoryId（记忆唯一标识）
     * 返回值：删除结果（true-成功，false-不存在）
     */
    @DeleteMapping("/memory/delete/{memoryId}")
    public Result<Boolean> deleteMemoryById(@PathVariable String memoryId) {
        try {
            boolean success = memoryService.deleteMemoryById(memoryId);
            return Result.success(success, 0L);
        } catch (Exception e) {
            log.error("删除记忆失败，记忆ID={}", memoryId, e);
            return Result.fail("删除记忆失败：" + e.getMessage());
        }
    }

    /**
     * 根据会话ID批量删除所有关联记忆
     * DELETE /api/agent/memory/delete/session/{sessionId}
     * 路径参数：sessionId（会话唯一标识）
     * 返回值：删除的记忆数量
     */
    @DeleteMapping("/memory/delete/session/{sessionId}")
    public Result<Integer> deleteMemoryBySessionId(@PathVariable String sessionId) {
        try {
            int deleteCount = memoryService.deleteMemoryBySessionId(sessionId);
            log.info("批量删除会话记忆成功，会话ID={}，删除数量={}", sessionId, deleteCount);
            return Result.success(deleteCount, 0L);
        } catch (Exception e) {
            log.error("批量删除会话记忆失败，会话ID={}", sessionId, e);
            return Result.fail("批量删除会话记忆失败：" + e.getMessage());
        }
    }

    // ==================== 三、反射模块全套接口（触发/查/保存） ====================

    /**
     * 手动触发反射（支持自定义全链路数据，无需走Agent执行流程）
     * POST /api/agent/reflection/trigger
     * 请求参数：ReflectionDTO全字段（sessionId/agentId/originalInstruction等）
     * 返回值：完成反思后的完整ReflectionDTO对象（含结论/建议/反射ID）
     */
    @PostMapping("/reflection/trigger")
    public Result<ReflectionDTO> triggerReflection(@RequestBody ReflectionDTO reflectionDTO) {
        try {
            ReflectionDTO result = reflectionService.reflect(reflectionDTO);
            log.info("手动触发反射成功，会话ID={}，反射ID={}", reflectionDTO.getSessionId(), result.getReflectionId());
            return Result.success(result, 0L);
        } catch (Exception e) {
            log.error("手动触发反射失败，会话ID={}", reflectionDTO.getSessionId(), e);
            return Result.fail("手动触发反射失败：" + e.getMessage());
        }
    }

    /**
     * 根据反射ID查询单条反射记录
     * GET /api/agent/reflection/get/{reflectionId}
     * 路径参数：reflectionId（反射唯一标识）
     * 返回值：对应的ReflectionDTO对象（无则返回提示）
     */
    @GetMapping("/reflection/get/{reflectionId}")
    public Result<ReflectionDTO> getReflectionById(@PathVariable String reflectionId) {
        try {
            Optional<ReflectionDTO> reflection = reflectionService.getReflectionById(reflectionId);
            return reflection.map(dto -> Result.success(dto, 0L))
                    .orElse(Result.fail("反射记录不存在，反射ID=" + reflectionId));
        } catch (Exception e) {
            log.error("查询反射记录失败，反射ID={}", reflectionId, e);
            return Result.fail("查询反射记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据会话ID查询所有关联反射记录（按创建时间升序）
     * GET /api/agent/reflection/list/{sessionId}
     * 路径参数：sessionId（会话唯一标识）
     * 返回值：该会话下的所有反射列表（无则返回空列表）
     */
    @GetMapping("/reflection/list/{sessionId}")
    public Result<List<ReflectionDTO>> listReflectionBySessionId(@PathVariable String sessionId) {
        try {
            List<ReflectionDTO> reflectionList = reflectionService.listReflectionBySessionId(sessionId);
            log.info("查询会话反射记录成功，会话ID={}，反射记录数量={}", sessionId, reflectionList.size());
            return Result.success(reflectionList, 0L);
        } catch (Exception e) {
            log.error("查询会话反射记录失败，会话ID={}", sessionId, e);
            return Result.fail("查询会话反射记录失败：" + e.getMessage());
        }
    }

    /**
     * 手动保存反射记录（无需触发反思，仅单纯存储反射数据）
     * POST /api/agent/reflection/save
     * 请求参数：ReflectionDTO对象（至少包含sessionId，其余字段可选）
     * 返回值：保存的反射唯一标识reflectionId
     */
    @PostMapping("/reflection/save")
    public Result<String> saveReflection(@RequestBody ReflectionDTO reflectionDTO) {
        try {
            String reflectionId = reflectionService.saveReflection(reflectionDTO);
            log.info("手动保存反射记录成功，会话ID={}，反射ID={}", reflectionDTO.getSessionId(), reflectionId);
            return Result.success(reflectionId, 0L);
        } catch (Exception e) {
            log.error("手动保存反射记录失败，会话ID={}", reflectionDTO.getSessionId(), e);
            return Result.fail("手动保存反射记录失败：" + e.getMessage());
        }
    }
}