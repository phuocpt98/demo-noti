package com.demo.stomp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Publisher facade around Spring's SimpMessagingTemplate.
 * - sendToUser: personal destination via /user/queue/*
 * - sendToTopic: role/broadcast destination via /topic/*
 * - broadcastAll: fan-out to /topic/broadcast
 * - stats: number of connected users + current topic list via SimpUserRegistry
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final SimpMessagingTemplate messaging;
    private final SimpUserRegistry userRegistry;

    /** Send to a single user's personal queue. Client subs /user/queue/notifications. */
    public void sendToUser(String username, Object payload) {
        messaging.convertAndSendToUser(username, "/queue/notifications", payload);
        log.info("[publish] user={} dest=/user/queue/notifications", username);
    }

    /** Send to a topic — all subscribers of /topic/<name> receive. */
    public void sendToTopic(String topic, Object payload) {
        String dest = "/topic/" + topic;
        messaging.convertAndSend(dest, payload);
        log.info("[publish] topic dest={}", dest);
    }

    /** Global broadcast — everyone subscribed to /topic/broadcast. */
    public void broadcastAll(Object payload) {
        messaging.convertAndSend("/topic/broadcast", payload);
        log.info("[publish] broadcast-all");
    }

    /** Connected user count. */
    public int totalUsers() {
        return userRegistry.getUserCount();
    }

    /** Connected usernames. */
    public Set<String> connectedUsernames() {
        return userRegistry.getUsers().stream()
                .map(u -> u.getName())
                .collect(Collectors.toSet());
    }
}
