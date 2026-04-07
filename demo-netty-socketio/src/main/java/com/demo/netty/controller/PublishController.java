package com.demo.netty.controller;

import com.demo.netty.service.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST endpoints to test the Netty-SocketIO publisher.
 * Mirrors Centrifugo's /api/publish surface so scenarios can be compared directly.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublishController {

    private final NotificationPublisher publisher;

    /** Publish to a single room. Body: { room, event, data } */
    @PostMapping("/publish")
    public Map<String, Object> publish(@RequestBody PublishRequest req) {
        String event = req.event() != null ? req.event() : "notification";
        publisher.publishToRoom(req.room(), event, req.data());
        return Map.of("ok", true, "room", req.room());
    }

    /** Broadcast to many rooms. Body: { rooms: [...], event, data } */
    @PostMapping("/broadcast")
    public Map<String, Object> broadcast(@RequestBody BroadcastRequest req) {
        String event = req.event() != null ? req.event() : "notification";
        publisher.broadcast(req.rooms(), event, req.data());
        return Map.of("ok", true, "count", req.rooms().size());
    }

    /** Broadcast to all clients. Body: { event, data } */
    @PostMapping("/broadcast-all")
    public Map<String, Object> broadcastAll(@RequestBody BroadcastAllRequest req) {
        String event = req.event() != null ? req.event() : "notification";
        publisher.broadcastAll(event, req.data());
        return Map.of("ok", true, "clients", publisher.totalClients());
    }

    /** Server stats — total clients + active rooms. */
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
            "totalClients", publisher.totalClients(),
            "activeRooms", publisher.activeRooms()
        );
    }

    // --- Request DTOs (Java 17 records) ---
    public record PublishRequest(String room, String event, Object data) {}
    public record BroadcastRequest(List<String> rooms, String event, Object data) {}
    public record BroadcastAllRequest(String event, Object data) {}
}
