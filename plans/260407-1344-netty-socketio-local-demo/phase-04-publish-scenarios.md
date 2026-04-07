# Phase 04 — Publish Scenarios

## Overview
Test các scenarios publish giống Centrifugo demo, nhưng gọi REST endpoint Spring Boot thay vì Centrifugo HTTP API.

## REST Endpoints

### 1. Publish 1 room
```
POST http://localhost:8080/api/publish
Content-Type: application/json

{
  "room": "user#alice",
  "event": "notification",
  "data": {
    "title": "Đơn hàng mới #ORD-001",
    "body": "Khách Nguyễn Văn A vừa đặt",
    "level": "success"
  }
}
```

### 2. Broadcast nhiều room
```
POST http://localhost:8080/api/broadcast
{
  "rooms": ["user#alice", "user#bob", "role#admin"],
  "event": "notification",
  "data": {...}
}
```

### 3. Global broadcast
```
POST http://localhost:8080/api/broadcast-all
{
  "event": "notification",
  "data": {...}
}
```

### 4. Stats
```
GET http://localhost:8080/api/stats
→ { "totalClients": 3, "rooms": ["user#alice","user#bob"] }
```

## Scenarios test (tương đương Centrifugo demo-scenario.html)

| # | Scenario | Method | Expected |
|---|----------|--------|----------|
| 01 | Publish 1-1 vào room user#alice | POST /api/publish | Chỉ tab alice nhận |
| 02 | Broadcast nhiều room | POST /api/broadcast | Nhiều tab nhận cùng lúc |
| 03 | Stats realtime | GET /api/stats | Đếm số client đang connect |
| 04 | Disconnect user | server.getAllClients().forEach(...) | FE bị kick |
| 05 | Multi-tab isolation | Publish vào user#alice → chỉ alice nhận | ✓ |

## Test bằng curl
```bash
# Publish
curl -X POST http://localhost:8080/api/publish \
  -H "Content-Type: application/json" \
  -d '{"room":"user#alice","event":"notification","data":{"title":"Hi","body":"Hello"}}'

# Stats
curl http://localhost:8080/api/stats
```

## Test bằng PowerShell
```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/publish `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"room":"user#alice","event":"notification","data":{"title":"Hi","body":"Hello"}}'
```

## Todo
- [ ] Chạy Spring Boot + mở FE index.html
- [ ] Test scenario 01-05
- [ ] Verify multi-tab isolation
- [ ] So sánh latency với Centrifugo demo
