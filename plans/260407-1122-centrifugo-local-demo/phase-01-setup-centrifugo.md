# Phase 01 — Setup Centrifugo Server

## Overview
Chạy Centrifugo bằng Docker với config tối thiểu cho demo (JWT HMAC, admin UI, presence, history).

## Files to Create
- `demo/config.json`
- `demo/docker-compose.yml`

## Config

**`demo/config.json`**
```json
{
  "token_hmac_secret_key": "demo-secret-key-change-me",
  "api_key": "demo-api-key",
  "admin": true,
  "admin_password": "admin",
  "admin_secret": "admin-secret-change-me",
  "allowed_origins": ["*"],
  "presence": true,
  "history_size": 10,
  "history_ttl": "300s",
  "namespaces": [
    {
      "name": "notifications",
      "presence": true,
      "history_size": 10,
      "history_ttl": "300s",
      "allow_subscribe_for_client": true
    }
  ]
}
```

**`demo/docker-compose.yml`**
```yaml
services:
  centrifugo:
    image: centrifugo/centrifugo:v5
    container_name: centrifugo-demo
    command: centrifugo -c config.json
    volumes:
      - ./config.json:/centrifugo/config.json
    ports:
      - "8000:8000"
    ulimits:
      nofile:
        soft: 65536
        hard: 65536
```

## Steps
1. `cd demo`
2. `docker compose up -d`
3. Mở http://localhost:8000 → login `admin` / `admin`
4. Verify endpoint: `curl http://localhost:8000/health` → `{"status":"ok"}`

## Todo
- [ ] Tạo thư mục `demo/`
- [ ] Tạo `config.json`
- [ ] Tạo `docker-compose.yml`
- [ ] Start container
- [ ] Verify admin UI + health endpoint

## Success Criteria
- Container running (`docker ps`)
- Admin UI accessible
- Health check OK
