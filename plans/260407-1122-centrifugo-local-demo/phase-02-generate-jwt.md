# Phase 02 — Generate JWT Token

## Overview
Centrifugo yêu cầu JWT (HS256) ký bằng `token_hmac_secret_key` để client authenticate.

## JWT Payload
```json
{
  "sub": "user-123",
  "exp": 9999999999
}
```
- `sub`: user id (unique)
- `exp`: expiry timestamp (Unix seconds)

## Cách tạo

### Option A — jwt.io (nhanh nhất)
1. Mở https://jwt.io
2. Algorithm: **HS256**
3. Payload: paste JSON trên
4. Secret: `demo-secret-key-change-me` (khớp với config)
5. Copy token ra

### Option B — Node.js script
**`demo/generate-token.js`**
```js
const crypto = require('crypto');

const secret = 'demo-secret-key-change-me';
const payload = { sub: 'user-123', exp: Math.floor(Date.now() / 1000) + 3600 };

const header = { alg: 'HS256', typ: 'JWT' };
const b64 = (o) => Buffer.from(JSON.stringify(o)).toString('base64url');
const data = `${b64(header)}.${b64(payload)}`;
const sig = crypto.createHmac('sha256', secret).update(data).digest('base64url');

console.log(`${data}.${sig}`);
```
Chạy: `node generate-token.js`

### Option C — Centrifugo CLI
```bash
docker exec centrifugo-demo centrifugo gentoken -u user-123
```

## Todo
- [ ] Chọn 1 trong 3 cách
- [ ] Tạo token cho `user-123`
- [ ] Lưu token để dùng ở Phase 03

## Success Criteria
- Có JWT token hợp lệ dán được vào FE
