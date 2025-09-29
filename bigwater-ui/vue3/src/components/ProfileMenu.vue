<template>
  <div v-if="isAuthenticated" class="relative profile-menu-wrapper" :class="wrapperClass">
    <button @click="toggle" class="flex items-center space-x-2 p-2 rounded-lg hover:bg-gray-100 transition-colors" title="Profile Menu">
      <div class="w-8 h-8 bg-indigo-500 rounded-full flex items-center justify-center text-white text-sm font-medium">
        {{ userInitials }}
      </div>
      <svg v-if="!compact" class="w-4 h-4 text-gray-500 transition-transform" :class="{ 'rotate-180': open }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
      </svg>
    </button>

    <transition name="fade-scale">
      <div v-if="open" class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 z-50">
        <div class="px-4 py-2 border-b border-gray-100">
          <p class="text-sm font-medium text-gray-900 truncate">{{ userName }}</p>
          <p class="text-xs text-gray-500 truncate">{{ currentUser?.email }}</p>
        </div>
        <router-link to="/dashboard" @click.native="close" class="menu-item">
          <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z"/></svg>
          Dashboard
        </router-link>
        <router-link to="/profile" @click.native="close" class="menu-item">
          <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
          Profile
        </router-link>
        <router-link to="/settings" @click.native="close" class="menu-item">
          <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
          Settings
        </router-link>
        <button @click="handleLogout" class="menu-item text-red-600 hover:bg-red-50">
          <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/></svg>
          Logout
        </button>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { isAuthenticated, currentUser, logout } from '../utils/auth.js'

const props = defineProps({
  compact: { type: Boolean, default: false },
  wrapperClass: { type: String, default: '' }
})

const router = useRouter()
const open = ref(false)

const userName = computed(() => {
  if (!currentUser.value) return 'User'
  if (currentUser.value.firstName && currentUser.value.lastName) {
    return `${currentUser.value.firstName} ${currentUser.value.lastName}`
  }
  return currentUser.value.email || 'User'
})

const userInitials = computed(() => {
  if (!currentUser.value) return 'U'
  if (currentUser.value.firstName && currentUser.value.lastName) {
    return `${currentUser.value.firstName[0]}${currentUser.value.lastName[0]}`.toUpperCase()
  }
  return currentUser.value.email ? currentUser.value.email[0].toUpperCase() : 'U'
})

function toggle() { open.value = !open.value }
function close() { open.value = false }

async function handleLogout() {
  try {
    await logout()
    close()
    router.push('/login')
  } catch (e) {
    console.error('Logout failed', e)
  }
}

function onClickOutside(e) {
  const root = e.target.closest('.profile-menu-wrapper')
  if (!root && open.value) {
    open.value = false
  }
}

onMounted(() => document.addEventListener('click', onClickOutside))
onBeforeUnmount(() => document.removeEventListener('click', onClickOutside))
</script>

<style scoped>
.menu-item {
  display: flex;
  align-items: center;
  padding: 0.5rem 1rem; /* py-2 px-4 */
  font-size: 0.875rem; /* text-sm */
  line-height: 1.25rem;
  color: #374151; /* gray-700 */
  width: 100%;
  text-align: left;
  transition: background-color 0.15s ease-in-out, color 0.15s ease-in-out;
  background: transparent;
}
.menu-item:hover { background: #f3f4f6; } /* gray-100 */
.icon { width: 1rem; height: 1rem; margin-right: 0.75rem; }
.fade-scale-enter-active, .fade-scale-leave-active { transition: all .15s ease; }
.fade-scale-enter-from, .fade-scale-leave-to { opacity:0; transform: translateY(-4px) scale(.98); }
</style>

<!-- Removed duplicate <script setup> blocks; icons inlined above -->
