package com.popcorn.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Agent Java核心工程启动类
 */
@SpringBootApplication
public class AgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Agent Java Core 启动成功！");
        System.out.println("  接口地址：POST /api/v1/agent/execute");
        System.out.println("=====================================");
    }
}
