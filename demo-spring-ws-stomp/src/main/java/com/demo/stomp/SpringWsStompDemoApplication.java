package com.demo.stomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring WebSocket + STOMP demo entry point.
 * Runs on embedded Tomcat (port 8080) — STOMP endpoint shares the same port.
 */
@SpringBootApplication
public class SpringWsStompDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWsStompDemoApplication.class, args);
    }
}
