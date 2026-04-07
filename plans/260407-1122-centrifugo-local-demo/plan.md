# Centrifugo Local Demo — Plan

**Date:** 2026-04-07
**Goal:** Chạy Centrifugo trên local + FE nhỏ hứng và hiển thị notification realtime
**Stack:** Docker · Centrifugo · HTML/JS (centrifuge-js via CDN)

## Phases

| # | Phase | Status | File |
|---|-------|--------|------|
| 1 | Setup Centrifugo server (Docker + config) | TODO | [phase-01-setup-centrifugo.md](phase-01-setup-centrifugo.md) |
| 2 | Generate JWT token cho client | TODO | [phase-02-generate-jwt.md](phase-02-generate-jwt.md) |
| 3 | FE demo (HTML + centrifuge-js) | TODO | [phase-03-frontend-demo.md](phase-03-frontend-demo.md) |
| 4 | Publish notification từ BE (curl/Postman) | TODO | [phase-04-publish-test.md](phase-04-publish-test.md) |
| 5 | Verify + Admin UI | TODO | [phase-05-verify.md](phase-05-verify.md) |

## Deliverables
- `demo/config.json` — Centrifugo config
- `demo/docker-compose.yml` — run Centrifugo
- `demo/index.html` — FE demo page
- `demo/publish.sh` — curl script gửi noti test

## Flow
```
[curl/BE] --POST /api/publish--> [Centrifugo :8000] --WS--> [index.html]
                                       ↑
                                  JWT auth (HS256)
```

## Success Criteria
- Mở `index.html` → status "Connected"
- Chạy `publish.sh` → FE hiện toast notification ngay
- Admin UI (`:8000`) thấy 1 client đang subscribe
