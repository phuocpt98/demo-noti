# Phase 02 — Integrate Netty-SocketIO

## Overview
Config `SocketIOServer` bean + handshake auth + publisher service + REST controller.

## Files to Create
- `config/SocketIoConfig.java` — SocketIOServer bean + listeners
- `service/NotificationPublisher.java` — wrapper publish API
- `controller/PublishController.java` — REST endpoints test

## Key concepts

### 1. SocketIOServer bean
- Lifecycle: start server khi Spring context ready, stop khi shutdown
- Dùng `@PostConstruct` / `@PreDestroy` hoặc Spring `SmartLifecycle`

### 2. Handshake Auth
- Client connect với query param `?token=<JWT>` hoặc `?userId=<id>`
- Server dùng `AuthorizationListener` để validate
- Nếu fail → reject connection
- Nếu OK → lưu userId vào `client.set("userId", id)`

### 3. Join Room
- Khi client connect thành công → join room theo user id
- Room name convention: `user#<userId>`, `role#<role>`, `order#<id>`

### 4. Publish methods
```java
// 1-1: phát vào room của user
server.getRoomOperations("user#123")
      .sendEvent("notification", payload);

// Broadcast: phát vào nhiều room
server.getRoomOperations("role#admin").sendEvent("notification", payload);
server.getRoomOperations("role#sale").sendEvent("notification", payload);

// Global: toàn bộ namespace
server.getBroadcastOperations().sendEvent("notification", payload);
```

## REST Endpoints
| Endpoint | Method | Body |
|----------|--------|------|
| `/api/publish` | POST | `{ room, event, data }` |
| `/api/broadcast` | POST | `{ event, data }` |
| `/api/stats` | GET | — (returns num_clients, rooms count) |

## Todo
- [ ] SocketIoConfig với SocketIOServer bean + start/stop lifecycle
- [ ] Handshake AuthorizationListener — validate userId query param
- [ ] ConnectListener — auto-join room `user#<userId>`
- [ ] DisconnectListener — log
- [ ] NotificationPublisher service — 3 methods publish/broadcast/stats
- [ ] PublishController REST

## Success Criteria
- Spring Boot start → log "Netty-SocketIO started on port 9092"
- Client Socket.IO connect với `?userId=alice` → server log "alice connected, joined room user#alice"
- POST `/api/publish` với `{"room":"user#alice","event":"notification","data":{...}}` → client nhận
