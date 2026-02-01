package com.popcorn.agent.core.reflection;

import com.popcorn.agent.core.reflection.dto.ReflectionDTO;

import java.util.List;
import java.util.Optional;

/**
 * 核心层-反射模块：核心接口
 * 定义Agent反射基础能力，实现「执行后反思」核心流程
 * 阶段一实现：触发反思、单条查询、按会话查询、保存反思结果
 */
public interface ReflectionService {
    /**
     * 触发Agent反射（核心方法）
     *
     * @param reflectionDTO 反射全链路数据（含感知/规划/执行结果）
     * @return 完成反思后的完整反射记录（含结论+建议）
     */
    ReflectionDTO reflect(ReflectionDTO reflectionDTO);

    /**
     * 根据反射ID查询单条记录
     *
     * @param reflectionId 反射唯一标识
     * @return 反射记录（空则返回Optional.empty()）
     */
    Optional<ReflectionDTO> getReflectionById(String reflectionId);

    /**
     * 根据会话ID查询所有关联反射记录（按创建时间升序）
     *
     * @param sessionId 会话ID
     * @return 该会话下的所有反射记录
     */
    List<ReflectionDTO> listReflectionBySessionId(String sessionId);

    /**
     * 保存反射记录（独立方法，支持手动/自动保存）
     *
     * @param reflectionDTO 反射记录
     * @return 保存后的反射ID
     */
    String saveReflection(ReflectionDTO reflectionDTO);
}
