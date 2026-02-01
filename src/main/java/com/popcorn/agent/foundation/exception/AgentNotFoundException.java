package com.popcorn.agent.foundation.exception;

/**
 * Agent不存在异常
 */
public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(String message) {
        super(message);
    }

    public AgentNotFoundException(String message, String agentId) {
        super(message);
    }

    public AgentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
