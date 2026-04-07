# Phase 05 — Verify & Admin UI

## Verify Checklist
- [ ] `docker ps` → `centrifugo-demo` running
- [ ] http://localhost:8000 → Admin UI login OK
- [ ] FE `index.html` → status "Connected"
- [ ] Run publish script → FE hiện toast + entry mới trong list
- [ ] Admin UI → tab **Channels** → thấy `notifications:user#123` với 1 subscriber
- [ ] Admin UI → tab **Actions** → **Publish** từ UI, FE vẫn nhận

## Troubleshooting
| Lỗi | Nguyên nhân | Fix |
|-----|-------------|-----|
| `unauthorized` | JWT sai secret / hết hạn | Generate lại token |
| `permission denied` (subscribe) | Namespace không `allow_subscribe_for_client` | Check `config.json` |
| CORS error | `allowed_origins` thiếu | Set `"*"` cho demo |
| Connection refused | Container chưa up | `docker compose up -d` |
| `bad channel` | Namespace không tồn tại | Check prefix `notifications:` khớp config |

## Next Steps
- Spring Boot integration: gọi `/api/publish` từ service layer
- Presence API: xem ai đang online
- History API: lấy noti cũ khi reconnect
