package com.demo.netty.service;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Thin wrapper around SocketIOServer for publishing events.
 * Inject in-process — no HTTP overhead between Spring and WS server.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final SocketIOServer server;

    /** Publish an event to a single room (e.g. "user#alice"). */
    public void publishToRoom(String room, String event, Object data) {
        server.getRoomOperations(room).sendEvent(event, data);
        log.info("[publish] room={} event={}", room, event);
    }

    /** Publish the same event to many rooms (broadcast). */
    public void broadcast(Collection<String> rooms, String event, Object data) {
        rooms.forEach(room -> server.getRoomOperations(room).sendEvent(event, data));
        log.info("[broadcast] rooms={} event={}", rooms.size(), event);
    }

    /** Publish to every connected client in the default namespace. */
    public void broadcastAll(String event, Object data) {
        server.getBroadcastOperations().sendEvent(event, data);
        log.info("[broadcast-all] event={}", event);
    }

    /** Count currently connected clients. */
    public int totalClients() {
        return server.getAllClients().size();
    }

    /** Distinct room names (excluding per-session private rooms). */
    public Set<String> activeRooms() {
        Set<String> rooms = new HashSet<>();
        server.getAllClients().forEach(client -> rooms.addAll(client.getAllRooms()));
        return rooms;
    }
}
