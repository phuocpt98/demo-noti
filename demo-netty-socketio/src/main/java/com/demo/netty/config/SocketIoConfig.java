package com.demo.netty.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Configures and lifecycle-manages the embedded Netty-SocketIO server.
 * - Listens on {socketio.port} inside the same JVM as Spring Boot.
 * - Uses query param `userId` from the client handshake for identity.
 * - On connect: auto-joins room `user#<userId>`.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIoConfig {

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private int port;

    @Value("${socketio.origin}")
    private String origin;

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration cfg = new Configuration();
        cfg.setHostname(host);
        cfg.setPort(port);
        cfg.setOrigin(origin);

        // Identity check at handshake — reject if userId is missing.
        cfg.setAuthorizationListener(data -> {
            String userId = data.getSingleUrlParam("userId");
            if (userId == null || userId.isBlank()) {
                log.warn("[handshake] rejected — missing userId");
                return com.corundumstudio.socketio.AuthorizationResult.FAILED_AUTHORIZATION;
            }
            log.info("[handshake] accepted userId={}", userId);
            return com.corundumstudio.socketio.AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
        });

        this.server = new SocketIOServer(cfg);
        this.server.addConnectListener(onConnect());
        this.server.addDisconnectListener(onDisconnect());
        // Start the Netty server immediately on bean creation.
        this.server.start();
        log.info("[netty-socketio] started on {}:{}", host, port);
        return this.server;
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.stop();
            log.info("[netty-socketio] stopped");
        }
    }

    private ConnectListener onConnect() {
        return (SocketIOClient client) -> {
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            client.set("userId", userId);
            String room = "user#" + userId;
            client.joinRoom(room);
            log.info("[connect] user={} joined room={} sessionId={}", userId, room, client.getSessionId());
        };
    }

    private DisconnectListener onDisconnect() {
        return (SocketIOClient client) -> {
            Object userId = client.get("userId");
            log.info("[disconnect] user={} sessionId={}", userId, client.getSessionId());
        };
    }
}
