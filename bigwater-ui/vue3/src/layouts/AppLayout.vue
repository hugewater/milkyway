<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header/Navigation -->
    <nav class="bg-white shadow sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <!-- Logo -->
            <div class="flex-shrink-0 flex items-center">
              <img class="h-6 w-auto sm:h-8" src="/vite.svg" alt="Logo">
              <span class="ml-2 text-lg font-bold text-gray-900 hidden xs:block">BigWater</span>
            </div>
            <!-- Desktop Navigation Links -->
            <div class="hidden md:ml-6 md:flex md:space-x-8">
              <router-link 
                to="/dashboard"
                class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors"
              >
                Dashboard
              </router-link>
              <router-link 
                to="/my-wallets"
                class="border-indigo-500 text-gray-900 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium"
              >
                Wallets
              </router-link>
            </div>
          </div>
          
          <!-- Right side items -->
          <div class="flex items-center space-x-2 sm:space-x-4">
            <!-- Wallet Connection (only on My Wallets page) -->
            <div v-if="route.path === '/my-wallets'" class="flex-shrink-0">
              <ConnectWalletButton />
            </div>
            

            
            <ProfileMenu v-if="isAuthenticated" />
            
            <!-- Mobile menu button -->
            <div class="md:hidden">
              <button @click="showMobileMenu = !showMobileMenu" class="p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500">
                <span class="sr-only">Open main menu</span>
                <svg class="h-6 w-6" :class="{'hidden': showMobileMenu, 'block': !showMobileMenu}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
                <svg class="h-6 w-6" :class="{'block': showMobileMenu, 'hidden': !showMobileMenu}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Mobile Navigation Menu -->
      <div class="md:hidden" :class="{'block': showMobileMenu, 'hidden': !showMobileMenu}">
        <div class="px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-white border-t">
          <!-- Navigation Links -->
          <router-link 
            to="/dashboard"
            @click="showMobileMenu = false"
            class="text-gray-500 hover:text-gray-700 block px-3 py-2 rounded-md text-base font-medium"
          >
            Dashboard
          </router-link>
          <router-link 
            to="/my-wallets"
            @click="showMobileMenu = false"
            class="text-gray-900 block px-3 py-2 rounded-md text-base font-medium bg-gray-100"
          >
            My Wallets
          </router-link>
          
          <!-- Profile Section (Mobile) -->
          <div v-if="isAuthenticated" class="border-t border-gray-200 pt-3 mt-3">
            <div class="px-3 py-2">
              <div class="flex items-center space-x-3">
                <div class="w-10 h-10 bg-indigo-500 rounded-full flex items-center justify-center text-white font-medium">
                  {{ userInitials }}
                </div>
                <div class="flex-1 min-w-0">
                  <p class="text-sm font-medium text-gray-900 truncate">{{ userName }}</p>
                  <p class="text-xs text-gray-500 truncate">{{ currentUser?.email }}</p>
                </div>
              </div>
            </div>
            <router-link 
              to="/profile"
              @click="showMobileMenu = false"
              class="flex items-center px-3 py-2 text-base font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50"
            >
              <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              Profile
            </router-link>
            <router-link 
              to="/settings"
              @click="showMobileMenu = false"
              class="flex items-center px-3 py-2 text-base font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50"
            >
              <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              Settings
            </router-link>
            <button 
              @click="handleLogout"
              class="flex items-center w-full px-3 py-2 text-base font-medium text-red-600 hover:text-red-800 hover:bg-red-50"
            >
              <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- Page Content -->
    <main class="flex-1">
      <div class="max-w-7xl mx-auto px-2 sm:px-4 lg:px-8 py-4 lg:py-6">
        <div class="bg-white shadow rounded-lg overflow-hidden">
          <slot></slot>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import ConnectWalletButton from '../components/ConnectWalletButton.vue'
import { isAuthenticated, currentUser, logout } from '../utils/auth.js'
import ProfileMenu from '../components/ProfileMenu.vue'

const router = useRouter()
const showMobileMenu = ref(false)

// Computed properties for user display

// Handle logout
async function handleLogout() { /* handled inside ProfileMenu now */ }



// Close dropdowns when clicking outside
function handleClickOutside(event) { /* profile menu encapsulated */ }

onMounted(() => {
  console.log('AppLayout mounted')
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
/* Profile dropdown animations */
.relative {
  position: relative;
}

/* Smooth transitions for dropdown */
.transition-colors {
  transition: all 0.2s ease-in-out;
}

/* Rotate arrow animation */
.rotate-180 {
  transform: rotate(180deg);
  transition: transform 0.2s ease-in-out;
}

/* Mobile menu slide animation */
@media (max-width: 768px) {
  .md\:hidden > div {
    animation: slideDown 0.2s ease-out;
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Profile dropdown shadow and positioning */
.absolute.right-0 {
  box-shadow: 0 10px 25px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  animation: fadeInScale 0.15s ease-out;
}

@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0.95) translateY(-10px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

/* Hover effects */
.hover\:bg-gray-100:hover {
  background-color: #f3f4f6;
  transition: background-color 0.15s ease-in-out;
}

.hover\:bg-red-50:hover {
  background-color: #fef2f2;
  transition: background-color 0.15s ease-in-out;
}
</style>