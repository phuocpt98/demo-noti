# Phase 03 — Frontend Demo

## Overview
FE nhỏ 1 file HTML dùng `centrifuge-js` qua CDN — không cần build tool. Kết nối WS, subscribe channel `notifications:user#123`, hiển thị noti dạng toast + list.

## File
- `demo/index.html` (xem file thực tế được tạo cùng phase này)

## Tính năng
- Input JWT token + user id
- Button **Connect** / **Disconnect**
- Status badge (Connecting / Connected / Disconnected / Error)
- Toast popup khi có noti mới (auto dismiss 4s)
- List history các noti đã nhận (newest trên đầu)
- Clear button

## Channel convention
`notifications:user#<userId>` — Centrifugo namespace `notifications`, user-specific channel dùng `#` để tách user part (Centrifugo channel pattern).

## Cách chạy
1. Mở trực tiếp `demo/index.html` trong browser (double click) hoặc:
   ```bash
   cd demo && python -m http.server 5500
   ```
2. Paste JWT token (từ Phase 02)
3. Click **Connect** → status chuyển "Connected"

## Todo
- [ ] Tạo `index.html`
- [ ] Test connect thành công
- [ ] Test disconnect/reconnect

## Success Criteria
- Status "Connected" sau khi bấm Connect
- Console không có error
- Admin UI Centrifugo thấy 1 client online
