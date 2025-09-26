/**
 * Centralized API URLs configuration
 * This file contains all external API URLs used throughout the frontend
 */

// Backend API URLs
export const BACKEND_API_URLS = {
  // Base API URL
  BASE: process.env.NODE_ENV === 'production' 
    ? 'https://your-production-api.com/bw-api' 
    : 'http://localhost:9999/bw-api',
  
  // Authentication endpoints
  LOGIN: '/auth/login',
  LOGOUT: '/auth/logout',
  REGISTER: '/auth/register',
  VALIDATE: '/auth/validate',
  
  // Wallet endpoints
  WALLETS: '/wallets',
  WALLETS_USER: '/wallets/user',
  WALLETS_COMPANY: '/wallets/company',
  WALLETS_COMPANY_RANDOM: '/wallets/company/random',
  
  // Transaction endpoints
  TRANSACTIONS: '/transactions',
  TRANSACTIONS_USER: '/transactions/user',
  TRANSACTIONS_VERIFY: '/transactions/verify',
  TRANSACTIONS_VERIFY_TEST: '/transactions/verify/test',
  
  // Payment endpoints
  RECORD_PAYMENT: '/transactions/record-payment'
}

// External blockchain API URLs (for reference)
export const BLOCKCHAIN_API_URLS = {
  // Polygon APIs
  POLYGON_SCAN: 'https://api.polygonscan.com/api',
  POLYGON_AMOY_SCAN: 'https://api-amoy.polygonscan.com/api',
  
  // TRON APIs
  TRON_GRID: 'https://api.trongrid.io',
  
  // Alchemy APIs
  ALCHEMY_POLYGON: 'https://polygon-mainnet.g.alchemy.com/v2',
  
  // Moralis APIs
  MORALIS_POLYGON: 'https://deep-index.moralis.io/api/v2'
}

/**
 * Get full API URL by combining base URL with endpoint
 * @param {string} endpoint API endpoint
 * @returns {string} Full API URL
 */
export function getApiUrl(endpoint) {
  return BACKEND_API_URLS.BASE + endpoint
}

/**
 * Get all backend API URLs
 * @returns {Object} Map of endpoint names to full URLs
 */
export function getAllBackendUrls() {
  const urls = {}
  for (const [key, value] of Object.entries(BACKEND_API_URLS)) {
    if (key !== 'BASE') {
      urls[key] = getApiUrl(value)
    }
  }
  return urls
}

/**
 * Get blockchain API URL by service and network
 * @param {string} service Service name (polygon, tron, alchemy, moralis)
 * @param {string} network Network name (mainnet, amoy, etc.)
 * @returns {string|null} API URL or null if not found
 */
export function getBlockchainApiUrl(service, network) {
  if (service === 'polygon') {
    if (network === 'amoy') {
      return BLOCKCHAIN_API_URLS.POLYGON_AMOY_SCAN
    }
    return BLOCKCHAIN_API_URLS.POLYGON_SCAN
  } else if (service === 'tron') {
    return BLOCKCHAIN_API_URLS.TRON_GRID
  } else if (service === 'alchemy') {
    return BLOCKCHAIN_API_URLS.ALCHEMY_POLYGON
  } else if (service === 'moralis') {
    return BLOCKCHAIN_API_URLS.MORALIS_POLYGON
  }
  return null
}
