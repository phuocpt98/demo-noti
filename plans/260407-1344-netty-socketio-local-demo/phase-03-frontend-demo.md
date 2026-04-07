# Phase 03 — Frontend Demo

## Overview
HTML file dùng `socket.io-client` qua CDN — không cần build. Connect tới Netty-SocketIO server port 9092, join room theo user id, hiển thị toast + list.

## File
- `demo-netty-socketio/frontend/index.html`

## Tính năng tương tự Centrifugo demo
- Input **User ID** (tự gen/nhập)
- Input **Server URL** (`http://localhost:9092`)
- Input **Room** (auto-sync theo user id: `user#<id>`)
- Button Connect/Disconnect
- Toast popup 4s khi nhận event `notification`
- List history newest-first
- Status badge

## Khác biệt với Centrifugo FE
| Điểm | Centrifugo | Netty-SocketIO |
|------|-----------|----------------|
| Client lib | `centrifuge-js` | `socket.io-client` |
| Auth | JWT trong options | Query param `?userId=X` hoặc `?token=JWT` |
| Subscribe model | `newSubscription(channel)` | `socket.emit('join', room)` hoặc auto-join trên server |
| Event model | `sub.on('publication', cb)` | `socket.on('notification', cb)` |

## Code snippet
```js
const socket = io('http://localhost:9092', {
  query: { userId: 'alice' },
  transports: ['websocket']
});

socket.on('connect', () => setStatus('Connected'));
socket.on('disconnect', () => setStatus('Disconnected'));
socket.on('notification', (data) => {
  renderNoti(data);
  showToast(data);
});
```

## Steps
1. Tạo `frontend/index.html` — copy layout từ Centrifugo demo, đổi lib
2. Mở trực tiếp trong browser hoặc `python -m http.server 5500`
3. Nhập user ID → Connect → verify status

## Todo
- [ ] Tạo index.html với socket.io-client CDN
- [ ] Test connect với `?userId=alice`
- [ ] Test nhận event `notification`
- [ ] Multi-tab test với user id khác nhau

## Success Criteria
- Status "Connected" sau Connect
- Console: `[socket.io] connect`
- Server log: `alice connected, joined room user#alice`
