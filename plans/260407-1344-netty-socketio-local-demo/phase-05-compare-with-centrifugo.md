# Phase 05 — So sánh với Centrifugo

## Bảng so sánh sau khi demo cả 2

| Tiêu chí | Centrifugo | Netty-SocketIO |
|----------|-----------|----------------|
| **Setup time** | 1 file config + docker up | Full Spring Boot project |
| **Dependency** | Docker image | Gradle dependency Java |
| **Runtime** | Go (15MB image) | JVM (shared với Spring Boot) |
| **Admin UI** | ✅ Có sẵn | ❌ Phải tự build |
| **Presence API** | ✅ Built-in | ⚠️ Tự track qua room events |
| **History** | ✅ Built-in buffer | ❌ Không — phải lưu DB |
| **ACL native** | ✅ `#user`, `,multi`, namespace | ⚠️ Tự code AuthorizationListener |
| **Scaling multi-instance** | Redis Pub/Sub có sẵn | `RedisStoreFactory` adapter |
| **Protocol fallback** | WS + SSE | WS + HTTP long-polling (Socket.IO) |
| **FE ecosystem** | centrifuge-js (~2K stars) | socket.io-client (~62K stars) |
| **Monitoring** | Prometheus metrics built-in | Tự add Micrometer |
| **Integration Spring** | HTTP call từ Spring → Centrifugo | Inject SocketIOServer bean trực tiếp |
| **Latency** | ~1-5ms (HTTP) | ~0ms (in-process) |
| **Debug** | Logs 2 services | Breakpoint Spring IDE |
| **Deploy** | 2 containers | 1 JAR |

## Kịch bản nào nên chọn cái nào?

### Chọn Centrifugo khi:
- Team không strict về stack Java
- Cần admin UI, presence, history ngay
- Muốn scale WS server độc lập với API server
- Traffic lớn (>10K concurrent connections)
- Có DevOps team quản Docker

### Chọn Netty-SocketIO khi:
- Ưu tiên cùng stack Java (debug, logging chung)
- Monolith, team nhỏ, ít user (<1000 concurrent)
- Cần tích hợp sâu với domain events (in-process)
- Không muốn thêm infra mới
- FE đã dùng socket.io-client (ecosystem lớn)

## Khuyến nghị OneSubs
Dựa theo comparison report:
- **Phase 1 hiện tại:** Spring WS STOMP (chọn từ Library comparison) — đơn giản nhất
- **Phase 2 (chat nội bộ):** Mở rộng Spring WS STOMP
- **Phase 3 (scale):**
  - Nếu cần features (presence, history, admin) → Centrifugo
  - Nếu muốn giữ Java stack → Netty-SocketIO standalone JAR

## Kết luận sau demo
- Netty-SocketIO phù hợp khi muốn **embed** (zero ops overhead)
- Centrifugo phù hợp khi muốn **tách scale** (dedicated WS infra)
- Spring WS STOMP phù hợp khi muốn **monolith đơn giản nhất**

## Todo
- [ ] Demo Centrifugo (đã có)
- [ ] Demo Netty-SocketIO
- [ ] Ghi lại latency đo thực tế
- [ ] Screenshot admin UI Centrifugo + Spring logs Netty-SocketIO
- [ ] Trình bày so sánh + đề xuất cuối cho team
