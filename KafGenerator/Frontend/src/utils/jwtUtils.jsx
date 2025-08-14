function base64UrlDecode(str) {
  let base64 = str.replace(/-/g, '+').replace(/_/g, '/');
  while (base64.length % 4) {
    base64 += '=';
  }
  return atob(base64);
}

export function getPayloadFromToken() {
  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) return null;
  
    const parts = token.split('.');
    if (parts.length !== 3) return null;
  
    const payload = JSON.parse(base64UrlDecode(parts[1]));
    return payload;
  } catch (error) {
    console.error('Error decoding token:', error);
    return null;
  }

}

export function getUserFromToken() {
  const payload = getPayloadFromToken();
  return payload?.sub || 'guest';
}

export function getUserIdFromToken() {
  const payload = getPayloadFromToken();
  return payload?.id || null;
}
