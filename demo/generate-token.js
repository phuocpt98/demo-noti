// Generate HS256 JWT token for Centrifugo client auth.
// Usage: node generate-token.js [userId]
// Secret must match `token_hmac_secret_key` in config.json.

const crypto = require('crypto');

const SECRET = 'demo-secret-key-change-me';
const userId = process.argv[2] || 'user-123';
const ttlSeconds = 24 * 3600; // 24h

const header = { alg: 'HS256', typ: 'JWT' };
const payload = {
  sub: userId,
  exp: Math.floor(Date.now() / 1000) + ttlSeconds,
};

const b64url = (obj) =>
  Buffer.from(JSON.stringify(obj))
    .toString('base64')
    .replace(/=/g, '')
    .replace(/\+/g, '-')
    .replace(/\//g, '_');

const signingInput = `${b64url(header)}.${b64url(payload)}`;
const signature = crypto
  .createHmac('sha256', SECRET)
  .update(signingInput)
  .digest('base64')
  .replace(/=/g, '')
  .replace(/\+/g, '-')
  .replace(/\//g, '_');

const token = `${signingInput}.${signature}`;
console.log(`\nUser ID : ${userId}`);
console.log(`Expires : ${new Date(payload.exp * 1000).toISOString()}`);
console.log(`\nToken:\n${token}\n`);
