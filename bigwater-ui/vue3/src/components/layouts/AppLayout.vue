<template>
  <div class="flex h-screen bg-gray-50 relative">
    <!-- Mobile menu button -->
    <div class="lg:hidden fixed top-4 left-4 z-50">
      <button
        @click="toggleSidebar"
        class="p-2 rounded-md bg-ocean text-white hover:bg-deep-ocean shadow-lg"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
        </svg>
      </button>
    </div>

    <!-- Overlay for mobile -->
    <div
      v-if="sidebarOpen"
      @click="closeSidebar"
      class="fixed inset-0 bg-black opacity-50 z-30 lg:hidden"
    ></div>

    <!-- Sidebar -->
    <div 
      class="sidebar w-64 min-h-screen text-white fixed lg:relative z-40 transition-transform duration-300 ease-in-out bg-gradient-to-br from-blue-600 to-blue-800 !important"
      :class="sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'"
      style="background: linear-gradient(135deg, #1e40af 0%, #1e3a8a 100%) !important;"
    >
      <!-- Logo -->
      <div class="p-4 lg:p-6 border-b border-white/20">
        <h1 class="text-xl lg:text-2xl font-bold">{{ isAdmin.value ? 'BigWater Admin' : 'BigWater' }}</h1>
        <p class="text-sm text-white/80">{{ isAdmin.value ? 'Management Portal' : 'Digital Weekly Journal' }}</p>
      </div>

      <!-- Navigation -->
      <nav class="mt-6 overflow-y-auto max-h-[calc(100vh-120px)]">
        <div class="px-4 lg:px-6 mb-6">
          <div class="text-xs uppercase tracking-wider text-white/60 mb-3">
            {{ isAdmin.value ? 'Management' : 'Dashboard' }}
          </div>
          <!-- Flat items -->SWEEPSTAKES
          <router-link
            v-for="item in flatMenuItems"
            :key="item.path"
            :to="item.path"
            class="flex items-center px-3 lg:px-4 py-3 text-white/90 hover:bg-white/10 rounded-lg mb-2 transition-colors"
            :class="{ 'bg-white/10 text-white': isActiveRoute(item.path) }"
            @click="closeSidebar"
          >
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="item.iconPath"></path>
            </svg>
            <span class="text-sm font-medium">{{ item.name }}</span>
          </router-link>

          <!-- AI collapsible group moved to bottom (Admin and User) -->
          <div class="mt-4">
            <button
              @click="toggleAi"
              class="w-full flex items-center justify-between px-3 lg:px-4 py-3 text-white/90 hover:bg-white/10 rounded-lg transition-colors"
              :class="{ 'bg-white/10 text-white': route.path.startsWith('/admin/ai') || route.path.startsWith('/ai') }"
            >
              <div class="flex items-center">
                <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 11c0 1.657-1.343 3-3 3S6 12.657 6 11s1.343-3 3-3 3 3 1.343 3 3zm6 0a3 3 0 11-6 0 3 3 0 016 0zm-9 5a5 5 0 00-5 5h10a5 5 0 00-5-5zm9 0a5 5 0 00-5 5h10a5 5 0 00-5-5z"></path>
                </svg>
                <span class="text-sm font-medium">AI</span>
              </div>
              <svg class="w-4 h-4 transform transition-transform" :class="{ 'rotate-90': aiOpen }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
              </svg>
            </button>
            <div v-show="aiOpen" class="ml-8 mt-1">
              <router-link
                v-if="isAdmin.value || (currentUser?.role && String(currentUser.role).toUpperCase().trim() === 'SUPER_ADMIN')"
                to="/admin/ai/agents"
                class="block px-3 lg:px-4 py-2 text-white/80 hover:bg-white/10 rounded-lg mb-1"
                :class="{ 'bg-white/10 text-white': isActiveRoute('/admin/ai/agents') }"
                @click="closeSidebar"
              >
                Agents
              </router-link>
              <router-link
                :to="isAdmin.value ? '/admin/ai/chats' : '/ai/chats'"
                class="block px-3 lg:px-4 py-2 text-white/80 hover:bg-white/10 rounded-lg"
                :class="{ 'bg-white/10 text-white': isActiveRoute('/admin/ai/chats') || isActiveRoute('/ai/chats') }"
                @click="closeSidebar"
              >
                Chats
              </router-link>
            </div>
          </div>
        </div>

        <!-- Logout (Admin) / Account (User) -->
        <div v-if="isAdmin.value" class="px-4 lg:px-6 py-2 mt-8 border-t border-white/20">
          <div class="text-xs uppercase tracking-wider text-white/60 mb-3">Account</div>
          <button @click="logout" class="flex items-center px-3 lg:px-4 py-3 text-white/90 hover:bg-white/10 rounded-lg transition-colors w-full text-left">
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
            </svg>
            <span class="text-sm font-medium">Logout</span>
          </button>
        </div>
      </nav>
    </div>

    <!-- Main content area -->
    <div class="flex-1 flex flex-col overflow-hidden w-full lg:ml-0">
      <!-- Top header -->
      <header class="bg-white shadow-sm border-b border-gray-200">
        <div class="flex items-center justify-between px-4 lg:px-6 py-4">
          <div class="flex items-center space-x-4">
            <!-- Mobile menu space -->
            <div class="w-10 lg:hidden"></div>
            <div>
              <h1 class="text-xl lg:text-2xl font-bold text-deep-ocean">{{ pageTitle }}</h1>
              <p v-if="pageSubtitle" class="text-sm text-gray-600 hidden sm:block">{{ pageSubtitle }}</p>
            </div>
          </div>
          
          <div class="flex items-center space-x-2 lg:space-x-4">

            <!-- Language Selector (hidden on mobile) -->
            <select v-if="!isAdmin.value" class="hidden md:block border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="en">English</option>
              <option value="es">Español</option>
              <option value="fr">Français</option>
            </select>

            <!-- User menu -->
            <div class="relative user-menu-container">
              <button
                @click="userMenuOpen = !userMenuOpen"
                class="flex items-center space-x-2 text-gray-700 hover:text-ocean"
              >
                <div class="w-8 h-8 bg-ocean rounded-full flex items-center justify-center text-white text-sm font-medium">
                  {{ userInitials }}
                </div>
                <span class="text-sm font-medium hidden sm:block">{{ userName }}</span>
                <svg class="w-4 h-4 hidden sm:block" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
              </button>

              <!-- Dropdown menu -->
              <div
                v-if="userMenuOpen"
                class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 z-50"
              >
                <router-link 
                  to="/my-account" 
                  @click="userMenuOpen = false"
                  class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50"
                >
                  My Account
                </router-link>
                <router-link 
                  to="/settings" 
                  @click="userMenuOpen = false"
                  class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50"
                >
                  Settings
                </router-link>
                <div class="border-t border-gray-200"></div>
                <button @click="logout" class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-50">
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- Page content -->
      <main class="flex-1 overflow-y-auto">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { currentUser, isAuthenticated, logout as authLogout, isAdmin as authIsAdmin } from '../../utils/auth.js'

const router = useRouter()
const route = useRoute()

const sidebarOpen = ref(false)
const userMenuOpen = ref(false)
const aiOpen = ref(false)

// Admin menu items - Flat items only (exclude AI children)
const adminMenuItems = computed(() => [
  { name: 'Dashboard', path: '/admin', iconPath: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6' },
  { name: 'Journals', path: '/admin/journals', iconPath: 'M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253' },
  { name: 'Members', path: '/admin/members', iconPath: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z' },
  { name: 'Payments', path: '/admin/payments', iconPath: 'M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z' },
  { name: 'Transactions', path: '/admin/transactions', iconPath: 'M12 8v8m4-4H8' },
  { name: 'Rewards', path: '/admin/rewards', iconPath: 'M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1' },
  { name: 'Drawings', path: '/admin/drawings', iconPath: 'M7 20l4-16m2 16l4-16M6 9h14M4 15h14' },
  { name: 'Certificates', path: '/admin/certificates', iconPath: 'M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1' },
  { name: 'Admin Manager', path: '/admin/admin-manager', iconPath: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z' }
])

// User menu items
const userMenuItems = computed(() => [
  { name: 'Dashboard', path: '/dashboard', iconPath: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6' },
  { name: 'My Journals', path: '/journals', iconPath: 'M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253' },
  { name: 'My Team', path: '/team', iconPath: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z' },
  { name: 'My Rewards', path: '/my-rewards', iconPath: 'M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1' },
  { name: 'My Wallets', path: '/my-wallets', iconPath: 'M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z' },
  { name: 'My Transactions', path: '/my-transactions', iconPath: 'M12 8v8m4-4H8' },
  { name: 'My Winnings', path: '/my-winnings', iconPath: 'M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z' },
  { name: 'Winning Numbers', path: '/winning-numbers', iconPath: 'M7 20l4-16m2 16l4-16M6 9h14M4 15h14' },
  { name: 'Certificates', path: '/certificates', iconPath: 'M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1' }
])

const isAdmin = computed(() => {
  // Force reactivity by accessing currentUser.value
  const user = currentUser.value
  const authResult = authIsAdmin()
  
  // Force SUPER_ADMIN to be admin
  const role = user?.role ? String(user.role).toUpperCase().trim() : null
  const forceResult = role === 'SUPER_ADMIN' || authResult
  
  return forceResult
})

// Fixed menu items that never change
const flatMenuItems = computed(() => {
  const isAdminContext = isAdmin.value || route.path.startsWith('/admin')
  return isAdminContext ? adminMenuItems.value : userMenuItems.value
})

const userName = computed(() => {
  if (!currentUser.value) return 'User'
  return currentUser.value.firstName && currentUser.value.lastName 
    ? `${currentUser.value.firstName} ${currentUser.value.lastName}`
    : currentUser.value.email || 'User'
})

const userInitials = computed(() => {
  if (!currentUser.value) return 'U'
  if (currentUser.value.firstName && currentUser.value.lastName) {
    return `${currentUser.value.firstName[0]}${currentUser.value.lastName[0]}`.toUpperCase()
  }
  return currentUser.value.email ? currentUser.value.email[0].toUpperCase() : 'U'
})

const pageTitle = computed(() => {
  const path = route.path
  if (path === '/admin' || path === '/dashboard') return isAdmin.value ? 'Admin Dashboard' : 'Dashboard'
  if (path.includes('/journals')) return 'My Journals'
  if (path.includes('/members')) return 'Members'
  if (path.includes('/team')) return 'My Team'
  if (path.includes('/rewards')) return 'My Rewards'
  if (path.includes('/wallets')) return 'My Wallets'
  if (path.includes('/winnings')) return 'My Winnings'
  if (path.includes('/numbers')) return 'Winning Numbers'
  if (path.includes('/certificates')) return 'Certificates'
  if (path.includes('/payments')) return 'Payments'
  if (path.includes('/drawings')) return 'Drawings'
  if (path.includes('/admin-manager')) return 'Admin Manager'
  if (path.startsWith('/admin/ai')) return 'AI'
  return 'BigWater'
})

const pageSubtitle = computed(() => {
  if (isAdmin.value) return `Welcome back, ${userName.value}`
  return 'Digital Weekly Journal'
})

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value
}

const closeSidebar = () => {
  sidebarOpen.value = false
  userMenuOpen.value = false
}

const logout = () => {
  authLogout()
  userMenuOpen.value = false
  router.push('/login')
}

// Close dropdown when clicking outside
const closeUserMenu = () => {
  userMenuOpen.value = false
}

// Check if a route is active
const isActiveRoute = (path) => {
  if (path === '/admin' && route.path === '/admin') {
    return true
  }
  if (path === '/dashboard' && route.path === '/dashboard') {
    return true
  }
  return route.path.startsWith(path) && path !== '/admin' && path !== '/dashboard'
}

const toggleAi = () => { aiOpen.value = !aiOpen.value }

// Expand AI group when current route is under it
onMounted(() => {
  document.addEventListener('click', (event) => {
    const userMenu = document.querySelector('.user-menu-container')
    if (userMenu && !userMenu.contains(event.target)) {
      userMenuOpen.value = false
    }
  })
  if (route.path.startsWith('/admin/ai')) aiOpen.value = true
})
</script>

<style scoped>
.sidebar {
  background: linear-gradient(135deg, #1e40af 0%, #1e3a8a 100%);
}

/* Responsive navigation */
@media (max-width: 1023px) {
  .sidebar {
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  }
}

/* Icon placeholder styles for missing icons */
svg {
  flex-shrink: 0;
}
</style>