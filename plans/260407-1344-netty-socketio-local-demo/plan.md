# Netty-SocketIO Local Demo — Plan

**Date:** 2026-04-07
**Goal:** Demo Netty-SocketIO embedded trong Spring Boot — so sánh trực tiếp với Centrifugo demo
**Stack:** Java 17 · Spring Boot 3.x · Netty-SocketIO 2.0.12 · Socket.IO JS client

## Mục tiêu so sánh
| Tiêu chí | Centrifugo | Netty-SocketIO |
|----------|-----------|----------------|
| Runtime | Go (Docker) | Java (JVM — cùng stack Spring Boot) |
| Deploy | Docker container riêng | Embed trong cùng JAR HOẶC standalone |
| Protocol | WebSocket + SSE (Centrifuge protocol) | Socket.IO (WS + long-polling fallback) |
| Port | 8000 | 9092 |
| Auth | JWT HS256 | Tự implement handshake listener |
| Room/Namespace | Channel + namespace | Namespace + Room |
| FE client | centrifuge-js | socket.io-client |
| Admin UI | Có sẵn | Không |

## Phases

| # | Phase | Status | File |
|---|-------|--------|------|
| 1 | Setup Spring Boot project + Gradle | TODO | [phase-01-setup-spring-boot-project.md](phase-01-setup-spring-boot-project.md) |
| 2 | Integrate Netty-SocketIO (config + publisher service) | TODO | [phase-02-integrate-netty-socketio.md](phase-02-integrate-netty-socketio.md) |
| 3 | Frontend demo (socket.io-client) | TODO | [phase-03-frontend-demo.md](phase-03-frontend-demo.md) |
| 4 | Publish scenarios (REST endpoint test) | TODO | [phase-04-publish-scenarios.md](phase-04-publish-scenarios.md) |
| 5 | So sánh với Centrifugo | TODO | [phase-05-compare-with-centrifugo.md](phase-05-compare-with-centrifugo.md) |

## Deliverables
```
demo-netty-socketio/
├── build.gradle
├── settings.gradle
├── src/main/java/com/demo/netty/
│   ├── NettySocketIoDemoApplication.java
│   ├── config/SocketIoConfig.java          # SocketIOServer bean + handshake auth
│   ├── service/NotificationPublisher.java  # sendToUser, sendToRoom, broadcast
│   └── controller/PublishController.java   # REST endpoints /api/publish, /api/broadcast
├── src/main/resources/
│   └── application.yml                     # port, socketio host/port
└── frontend/
    └── index.html                          # Socket.IO client receiver
```

## Flow
```
[curl/REST] ─► Spring Boot :8080
                      │
                      ▼
          NotificationPublisher (inject SocketIOServer)
                      │
                      ▼
          server.getRoomOperations("user#123")
            .sendEvent("notification", payload)
                      │
                      ▼
          Netty-SocketIO :9092 ─► FE index.html (socket.io-client)
```

## Success Criteria
- `./gradlew bootRun` → Spring Boot start trên 8080, Netty-SocketIO start trên 9092
- Mở `frontend/index.html` → click Connect → status "Connected"
- POST `http://localhost:8080/api/publish` → FE nhận toast ngay
- Test multi-client: 3 tab subscribe room khác nhau → publish isolation hoạt động

## Điểm khác biệt cần nhấn mạnh khi demo
1. **Cùng JVM process** — debug được bằng IDE (breakpoint trong publisher → trigger FE nhận)
2. **In-process call** — không HTTP overhead giữa Spring và WS server (nếu embed)
3. **Room + Namespace** — Socket.IO có 2 layer (namespace `/` mặc định, room tự tạo)
4. **Handshake auth** — JWT/query param validate ở lúc connect, không phải từng message
5. **Thiếu features so với Centrifugo:** không có admin UI, presence, history built-in — phải tự implement
