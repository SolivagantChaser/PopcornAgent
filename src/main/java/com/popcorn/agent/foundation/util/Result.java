package com.popcorn.agent.foundation.util;

import com.popcorn.agent.foundation.AgentConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础层 - 通用统一返回体
 * 所有HTTP接口统一返回格式，极简设计
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 响应状态：success/fail
    private String code;
    // 响应信息
    private String msg;
    // 响应数据
    private T data;
    // 执行耗时（毫秒）
    private Long costTime;

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(AgentConstant.STATUS_SUCCESS)
                .msg("操作成功")
                .costTime(0L)
                .build();
    }

    // 成功响应（带数据+耗时）
    public static <T> Result<T> success(T data, Long costTime) {
        return Result.<T>builder()
                .code(AgentConstant.STATUS_SUCCESS)
                .msg("操作成功")
                .data(data)
                .costTime(costTime)
                .build();
    }

    // 失败响应
    public static <T> Result<T> fail(String msg) {
        return Result.<T>builder()
                .code(AgentConstant.STATUS_FAIL)
                .msg(msg)
                .costTime(0L)
                .build();
    }
}
