import { isAuthenticated, isAdmin, isSuperAdmin } from './auth.js'

// Route guard for authentication
export function requireAuth(to, from, next) {
  if (!isAuthenticated.value) {
    next('/login')
  } else {
    next()
  }
}

// Route guard for admin access
export function requireAdmin(to, from, next) {
  if (!isAuthenticated.value) {
    next('/login')
  } else if (!isAdmin() && !isSuperAdmin()) {
    next('/dashboard')
  } else {
    next()
  }
}

// Route guard for super admin access
export function requireSuperAdmin(to, from, next) {
  if (!isAuthenticated.value) {
    next('/login')
  } else if (!isSuperAdmin()) {
    next('/admin')
  } else {
    next()
  }
}

// Route guard for guest access (non-authenticated users)
export function requireGuest(to, from, next) {
  if (isAuthenticated.value) {
    // Redirect to appropriate dashboard based on role
    if (isAdmin() || isSuperAdmin()) {
      next('/admin')
    } else {
      next('/dashboard')
    }
  } else {
    next()
  }
}

// Initialize router guards
export function setupRouterGuards(router) {
  router.beforeEach((to, from, next) => {
    // Public routes that don't require authentication
    const publicRoutes = ['/', '/login', '/signup']
    
    if (publicRoutes.includes(to.path)) {
      requireGuest(to, from, next)
    } else if (to.path.startsWith('/admin')) {
      requireAdmin(to, from, next)
    } else {
      requireAuth(to, from, next)
    }
  })
}
