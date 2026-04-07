# Spring WebSocket + STOMP Local Demo — Plan

**Date:** 2026-04-07
**Goal:** Demo cơ chế Spring WS + STOMP với embedded Tomcat — phương án finalist cho OneSubs Admin
**Stack:** Java 17 · Spring Boot 3.x · Tomcat embedded · @stomp/stompjs

## Đặc điểm khác 2 demo trước
| | Centrifugo | Netty-SocketIO | **Spring WS + STOMP** |
|---|-----------|----------------|------------------------|
| Runtime | Go Docker | JVM + Netty port riêng | **JVM + Tomcat (cùng port)** |
| Port | 8000 | 8080 + 9092 | **8080 only** |
| Protocol | Centrifuge | Socket.IO | **STOMP over WebSocket** |
| Integration | HTTP call | In-process (2 ports) | **Full in-process (1 port)** |
| Spring Security | Proxy | Tự handle | **Native** |

## Phases

| # | Phase | Status | File |
|---|-------|--------|------|
| 1 | Setup Spring Boot + dependency `spring-boot-starter-websocket` | TODO | [phase-01-setup-project.md](phase-01-setup-project.md) |
| 2 | Config STOMP broker + destinations + user principal | TODO | [phase-02-stomp-config.md](phase-02-stomp-config.md) |
| 3 | Frontend — `@stomp/stompjs` receiver | TODO | [phase-03-frontend-demo.md](phase-03-frontend-demo.md) |
| 4 | Publisher UI + REST endpoint test | TODO | [phase-04-publisher-ui.md](phase-04-publisher-ui.md) |

## Deliverables
```
demo-spring-ws-stomp/
├── build.gradle
├── settings.gradle
├── src/main/java/com/demo/stomp/
│   ├── SpringWsStompDemoApplication.java
│   ├── config/WebSocketConfig.java          # @EnableWebSocketMessageBroker
│   ├── config/StompPrincipalInterceptor.java # Read username from CONNECT header
│   ├── service/NotificationPublisher.java   # SimpMessagingTemplate wrapper
│   └── controller/PublishController.java    # REST /api/publish, etc
├── src/main/resources/application.yml
└── frontend/
    ├── index.html                           # STOMP receiver
    └── publisher.html                       # Sender UI
```

## Flow
```
[publisher.html] ─POST─► Spring Boot :8080
                              │
                              ▼
                  NotificationPublisher (SimpMessagingTemplate)
                              │
               ┌──────────────┼──────────────┐
               ▼              ▼              ▼
     /user/queue/notifs  /topic/role/admin  /topic/broadcast
                              │
                              ▼
          STOMP broker (in-memory) → routed to subscribers
                              │
                              ▼
                  [index.html] (stompjs client)
```

## Destinations convention
| Dest | Loại | Subscribe | Dùng cho |
|------|------|-----------|----------|
| `/user/queue/notifications` | user-specific | `sub('/user/queue/notifications')` | Personal noti |
| `/topic/role/{role}` | role broadcast | `sub('/topic/role/admin')` | Role-based |
| `/topic/order/{orderId}` | per-order | `sub('/topic/order/456')` | Chat/update theo đơn |
| `/topic/broadcast` | toàn bộ | `sub('/topic/broadcast')` | Emergency alert |

## Success Criteria
- `./gradlew bootRun` → Spring Boot :8080 (Tomcat), log `Started SpringWsStompDemoApplication`
- Mở `frontend/index.html` → nhập username `alice` → Connect → status "Connected"
- Publisher gửi `/user/queue/notifications` cho alice → chỉ alice nhận
- Publisher gửi `/topic/role/admin` → mọi tab subscribe role admin nhận
- Multi-tab isolation: alice vs bob trên personal queue hoạt động đúng
