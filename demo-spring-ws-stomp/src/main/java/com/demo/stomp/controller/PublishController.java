package com.demo.stomp.controller;

import com.demo.stomp.service.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoints used by the publisher UI and external test tools.
 * Mirrors the Netty-SocketIO demo API for apples-to-apples comparison.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublishController {

    private final NotificationPublisher publisher;

    /** Personal queue — body: { username, data } */
    @PostMapping("/publish-user")
    public Map<String, Object> publishUser(@RequestBody UserRequest req) {
        publisher.sendToUser(req.username(), req.data());
        return Map.of("ok", true, "username", req.username());
    }

    /** Topic broadcast — body: { topic, data } (e.g. topic="role/admin") */
    @PostMapping("/publish-topic")
    public Map<String, Object> publishTopic(@RequestBody TopicRequest req) {
        publisher.sendToTopic(req.topic(), req.data());
        return Map.of("ok", true, "topic", req.topic());
    }

    /** Fan out to /topic/broadcast — body: { data } */
    @PostMapping("/broadcast-all")
    public Map<String, Object> broadcastAll(@RequestBody BroadcastRequest req) {
        publisher.broadcastAll(req.data());
        return Map.of("ok", true, "users", publisher.totalUsers());
    }

    /** Basic server stats. */
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
            "totalUsers", publisher.totalUsers(),
            "usernames", publisher.connectedUsernames()
        );
    }

    // --- Request DTOs ---
    public record UserRequest(String username, Object data) {}
    public record TopicRequest(String topic, Object data) {}
    public record BroadcastRequest(Object data) {}
}
