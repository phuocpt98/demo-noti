# Phase 04 — Publish Notification (Test)

## Overview
Gửi notification từ "BE" (curl/Postman) tới Centrifugo HTTP API. FE subscribe sẽ nhận ngay.

## API
- **URL:** `POST http://localhost:8000/api/publish`
- **Header:** `X-API-Key: demo-api-key`
- **Body:**
  ```json
  {
    "channel": "notifications:user#123",
    "data": {
      "title": "Đơn hàng mới #ORD-001",
      "body": "Khách hàng Nguyễn Văn A vừa đặt đơn",
      "level": "info",
      "createdAt": "2026-04-07T11:22:00+07:00"
    }
  }
  ```

## Script

**`demo/publish-notification.sh`** (Linux/Mac/Git Bash)
```bash
#!/usr/bin/env bash
curl -s -X POST http://localhost:8000/api/publish \
  -H "X-API-Key: demo-api-key" \
  -H "Content-Type: application/json" \
  -d '{
    "channel": "notifications:user#123",
    "data": {
      "title": "Đơn hàng mới #ORD-001",
      "body": "Khách Nguyễn Văn A vừa đặt đơn",
      "level": "info"
    }
  }'
echo
```

**`demo/publish-notification.ps1`** (Windows PowerShell)
```powershell
$body = @{
  channel = "notifications:user#123"
  data = @{
    title = "Đơn hàng mới #ORD-001"
    body  = "Khách Nguyễn Văn A vừa đặt đơn"
    level = "info"
  }
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Uri http://localhost:8000/api/publish `
  -Method Post `
  -Headers @{ "X-API-Key" = "demo-api-key" } `
  -ContentType "application/json" `
  -Body $body
```

## Todo
- [ ] Tạo script publish
- [ ] Chạy script, verify response `{"result":{}}`
- [ ] Check FE hiện toast + list
