// Production API Configuration for Vue.js Frontend
// Copy this to src/config/api.js in your Vue project

const API_CONFIG = {
  // Base URL for API calls - replace with your actual domain
  BASE_URL: 'https://yourdomain.com/api',
  
  // Alternative for development/testing
  // BASE_URL: 'http://your-server-ip:8080/api',
  
  // Request timeout in milliseconds
  TIMEOUT: 30000,
  
  // Retry configuration
  RETRY_ATTEMPTS: 3,
  RETRY_DELAY: 1000,
  
  // Authentication
  AUTH_HEADER: 'Authorization',
  
  // API endpoints
  ENDPOINTS: {
    // Health check
    HEALTH: '/q/health',
    
    // Affiliate endpoints
    AFFILIATES: '/affiliates',
    AFFILIATE_BY_ID: '/affiliates/{id}',
    AFFILIATE_REGISTER: '/affiliates/register',
    AFFILIATE_PROMOTE: '/affiliates/{id}/promote',
    
    // Transaction endpoints
    TRANSACTIONS: '/affiliates/{id}/transactions',
    RECORD_TRANSACTION: '/affiliates/transactions/record',
    CONFIRM_TRANSACTION: '/affiliates/transactions/{id}/confirm',
    
    // Commission endpoints
    COMMISSIONS: '/affiliates/{id}/commissions',
    CALCULATE_COMMISSION: '/affiliates/{id}/calculate-commission',
    
    // Promotion endpoints
    PROMOTIONS: '/affiliates/{id}/promotions',
    
    // Statistics endpoints
    AFFILIATE_STATS: '/affiliates/{id}/statistics'
  }
}

// HTTP client configuration
const HTTP_CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  timeout: API_CONFIG.TIMEOUT
}

// Error messages
const ERROR_MESSAGES = {
  NETWORK_ERROR: 'Network error. Please check your internet connection.',
  TIMEOUT_ERROR: 'Request timeout. Please try again.',
  SERVER_ERROR: 'Server error. Please try again later.',
  UNAUTHORIZED: 'Unauthorized. Please log in again.',
  FORBIDDEN: 'Access denied.',
  NOT_FOUND: 'Resource not found.',
  VALIDATION_ERROR: 'Please check your input and try again.'
}

// Helper function to build URLs with parameters
const buildUrl = (endpoint, params = {}) => {
  let url = API_CONFIG.BASE_URL + endpoint
  
  // Replace path parameters
  Object.keys(params).forEach(key => {
    url = url.replace(`{${key}}`, params[key])
  })
  
  return url
}

// Helper function for error handling
const handleApiError = (error) => {
  if (error.response) {
    // Server responded with error status
    const status = error.response.status
    switch (status) {
      case 400:
        return ERROR_MESSAGES.VALIDATION_ERROR
      case 401:
        return ERROR_MESSAGES.UNAUTHORIZED
      case 403:
        return ERROR_MESSAGES.FORBIDDEN
      case 404:
        return ERROR_MESSAGES.NOT_FOUND
      case 500:
        return ERROR_MESSAGES.SERVER_ERROR
      default:
        return `Server error: ${status}`
    }
  } else if (error.request) {
    // Network error
    return ERROR_MESSAGES.NETWORK_ERROR
  } else {
    // Other error
    return error.message || 'An unexpected error occurred'
  }
}

export {
  API_CONFIG,
  HTTP_CONFIG,
  ERROR_MESSAGES,
  buildUrl,
  handleApiError
}