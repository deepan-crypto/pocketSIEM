import { Platform } from 'react-native';

// API Configuration
// For mobile/physical devices: Use your computer's IP address
// For web/emulator: Can use localhost

// To find your IP address:
// - Windows: ipconfig (look for IPv4)
// - Mac/Linux: ifconfig or hostname -I

// IMPORTANT: This IP address is auto-detected. Update if needed.
const API_BASE_URL = __DEV__
  ? 'http://172.17.2.170:5000/api'  // Your computer's IP address
  : 'https://your-production-api.com/api';

export const API_CONFIG = {
  BASE_URL: API_BASE_URL,
  API_KEY: 'pocketsiem-demo-key-12345',
  ENDPOINTS: {
    HEALTH: '/v1/health',
    REPUTATION: '/v1/reputation',
    REPORT: '/v1/report',
  },
  TIMEOUT: 10000,
};

export default API_BASE_URL;
