package com.demo.stomp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Configures the STOMP broker, WebSocket endpoint and a simple principal
 * interceptor that reads the username from the CONNECT header.
 *
 * In production, replace the username-header trick with JWT validation
 * and a full Spring Security setup.
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // In-memory broker for two destination prefixes.
        // /topic/* — fan-out to all subscribers
        // /queue/* — used by /user/queue/* user-specific destinations
        config.enableSimpleBroker("/topic", "/queue");

        // Client -> Server messages are sent to /app/* (routed to @MessageMapping handlers).
        config.setApplicationDestinationPrefixes("/app");

        // Enables convertAndSendToUser(username, "/queue/X", ...) routing to /user/queue/X.
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Handshake endpoint. Shares the same Tomcat port as REST (8080).
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(principalInterceptor());
    }

    /** REST CORS allow-all (demo only — tighten for prod). */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("*"));
        cfg.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(src);
    }

    /**
     * Reads the username from CONNECT header `x-username` and sets it as Principal.
     * After this, convertAndSendToUser(username, ...) routes to this client.
     */
    private ChannelInterceptor principalInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor
                        .getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String username = accessor.getFirstNativeHeader("x-username");
                    if (username == null || username.isBlank()) {
                        username = "anonymous-" + accessor.getSessionId();
                    }
                    final String principalName = username;
                    accessor.setUser((Principal) () -> principalName);
                    log.info("[stomp] CONNECT user={} sessionId={}", principalName, accessor.getSessionId());
                }
                return message;
            }
        };
    }
}
