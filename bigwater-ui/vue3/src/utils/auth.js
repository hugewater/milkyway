import { ref, reactive } from 'vue'
import apiService from './api.js'

// Reactive state
export const isAuthenticated = ref(false)
export const currentUser = ref(null)
export const token = ref(localStorage.getItem('token') || null)

// Initialize authentication state
export function initializeAuth() {
  const storedToken = localStorage.getItem('token')
  const storedUser = localStorage.getItem('user')
  
  if (storedToken && storedUser) {
    token.value = storedToken
    currentUser.value = JSON.parse(storedUser)
    isAuthenticated.value = true
    
    // Validate token with backend
    validateStoredToken(storedToken)
  }
}

// Validate stored token
async function validateStoredToken(tokenValue) {
  try {
    const response = await apiService.validateToken(tokenValue)
    if (!response.success) {
      logout()
    }
  } catch (error) {
    console.error('Token validation failed:', error)
    logout()
  }
}

// Login function
export async function login(email, password) {
  try {
    const response = await apiService.login(email, password)
    
    if (response.success) {
      // Store token and user data
      token.value = response.token
      currentUser.value = response.user
      isAuthenticated.value = true
      
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      
      return { success: true, user: response.user }
    } else {
      return { success: false, message: response.message }
    }
  } catch (error) {
    console.error('Login failed:', error)
    return { success: false, message: error.message || 'Login failed' }
  }
}

// Register function
export async function register(userData) {
  try {
    const response = await apiService.register(userData)
    
    if (response.success) {
      // Store token and user data
      token.value = response.token
      currentUser.value = response.user
      isAuthenticated.value = true
      
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      
      return { success: true, user: response.user }
    } else {
      return { success: false, message: response.message }
    }
  } catch (error) {
    console.error('Registration failed:', error)
    return { success: false, message: error.message || 'Registration failed' }
  }
}

// Logout function
export function logout() {
  token.value = null
  currentUser.value = null
  isAuthenticated.value = false
  
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}

// Check if user is admin
export function isAdmin() {
  return currentUser.value && currentUser.value.role === 'ADMIN'
}

// Check if user is super admin
export function isSuperAdmin() {
  return currentUser.value && currentUser.value.role === 'SUPER_ADMIN'
}

// Get user role
export function getUserRole() {
  return currentUser.value ? currentUser.value.role : null
}

// Get user ID
export function getUserId() {
  return currentUser.value ? currentUser.value.id : null
}

// Get user email
export function getUserEmail() {
  return currentUser.value ? currentUser.value.email : null
}

// Get user full name
export function getUserFullName() {
  if (!currentUser.value) return null
  return `${currentUser.value.firstName} ${currentUser.value.lastName}`
}

// Check if user has specific permission
export function hasPermission(permission) {
  if (!currentUser.value) return false
  
  // Add permission logic based on user role
  switch (currentUser.value.role) {
    case 'SUPER_ADMIN':
      return true // Super admin has all permissions
    case 'ADMIN':
      return ['manage_users', 'manage_certificates', 'manage_wallets', 'view_reports'].includes(permission)
    case 'SUBSCRIBER':
      return ['view_journals', 'manage_own_wallets', 'view_own_data'].includes(permission)
    default:
      return false
  }
}

// Auto-logout on token expiration
export function setupTokenExpirationCheck() {
  setInterval(() => {
    if (token.value) {
      // Check if token is expired (you can decode JWT to check expiration)
      // For now, we'll rely on API calls to validate token
    }
  }, 60000) // Check every minute
}
