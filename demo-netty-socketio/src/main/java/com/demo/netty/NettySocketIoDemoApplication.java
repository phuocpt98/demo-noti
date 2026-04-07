package com.demo.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Netty-SocketIO demo embedded in Spring Boot.
 * Starts Spring Boot on :8080 and Netty-SocketIO on :9092 (see application.yml).
 */
@SpringBootApplication
public class NettySocketIoDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettySocketIoDemoApplication.class, args);
    }
}
