package com.popcorn.agent.foundation.exception;

/**
 * 工具执行异常
 */
public class ToolExecuteException extends RuntimeException {
    public ToolExecuteException(String message) {
        super(message);
    }

    public ToolExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
