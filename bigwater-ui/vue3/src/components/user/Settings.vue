<template>
  <AppLayout>
    <div class="p-6">
      <!-- Language Settings -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">{{ t('settings.language') }}</h2>
        <div class="max-w-md">
          <label class="block text-sm font-medium text-gray-700 mb-2">{{ t('settings.language.preferred') }}</label>
          <select 
            v-model="settings.language"
            @change="updateLanguage"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
            <option v-for="lang in languages" :key="lang.code" :value="lang.code">
              {{ lang.name }}
            </option>
          </select>
          <p class="text-sm text-gray-600 mt-2">{{ t('settings.language.description') }}</p>
        </div>
      </div>

      <!-- Notification Settings -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">{{ t('settings.notifications') }}</h2>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.notifications.email') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.notifications.email.desc') }}</div>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input 
                v-model="settings.emailNotifications" 
                type="checkbox" 
                class="sr-only peer"
                @change="updateSettings"
              >
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.notifications.sms') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.notifications.sms.desc') }}</div>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input 
                v-model="settings.smsNotifications" 
                type="checkbox" 
                class="sr-only peer"
                @change="updateSettings"
              >
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.notifications.push') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.notifications.push.desc') }}</div>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input 
                v-model="settings.pushNotifications" 
                type="checkbox" 
                class="sr-only peer"
                @change="updateSettings"
              >
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
            </label>
          </div>
        </div>
      </div>

      <!-- Privacy Settings -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">{{ t('settings.privacy') }}</h2>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.privacy.profile') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.privacy.profile.desc') }}</div>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input 
                v-model="settings.profileVisible" 
                type="checkbox" 
                class="sr-only peer"
                @change="updateSettings"
              >
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.privacy.activity') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.privacy.activity.desc') }}</div>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input 
                v-model="settings.showActivityStatus" 
                type="checkbox" 
                class="sr-only peer"
                @change="updateSettings"
              >
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
            </label>
          </div>
        </div>
      </div>

      <!-- Security Settings -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">{{ t('settings.security') }}</h2>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.security.2fa') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.security.2fa.desc') }}</div>
            </div>
            <button 
              @click="setupTwoFactor"
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
            >
              {{ settings.twoFactorEnabled ? 'Manage' : 'Setup' }}
            </button>
          </div>
          
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.security.loginHistory') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.security.loginHistory.desc') }}</div>
            </div>
            <button 
              @click="viewLoginHistory"
              class="text-blue-600 hover:text-blue-700 font-medium"
            >
              View
            </button>
          </div>
        </div>
      </div>

      <!-- Data & Storage -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">{{ t('settings.data') }}</h2>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.data.autoSave') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.data.autoSave.desc') }}</div>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input 
                v-model="settings.autoSaveDrafts" 
                type="checkbox" 
                class="sr-only peer"
                @change="updateSettings"
              >
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between">
            <div>
              <div class="font-medium text-gray-900">{{ t('settings.data.clearCache') }}</div>
              <div class="text-sm text-gray-600">{{ t('settings.data.clearCache.desc') }}</div>
            </div>
            <button 
              @click="clearCache"
              class="text-red-600 hover:text-red-700 font-medium"
            >
              Clear
            </button>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { t, setLanguage, getCurrentLanguage, availableLanguages } from '../../utils/i18n.js'

const settings = ref({
  language: 'en-US',
  emailNotifications: true,
  smsNotifications: false,
  pushNotifications: true,
  profileVisible: true,
  showActivityStatus: true,
  twoFactorEnabled: false,
  autoSaveDrafts: true
})

onMounted(() => {
  loadSettings()
})

const loadSettings = () => {
  // Load settings from localStorage or API
  const savedSettings = localStorage.getItem('userSettings')
  if (savedSettings) {
    settings.value = { ...settings.value, ...JSON.parse(savedSettings) }
  }
  
  // Set language from settings
  // Map legacy simple codes (en, zh) to new extended codes if needed
  const current = getCurrentLanguage()
  const legacyMap = { 'en': 'en-US', 'zh': 'zh-CN' }
  settings.value.language = legacyMap[current] || current
}

const updateSettings = () => {
  // Save settings to localStorage or API
  localStorage.setItem('userSettings', JSON.stringify(settings.value))
  // TODO: Send to API
}

const updateLanguage = () => {
  // Update the language (reduce region to base when only base translations exist)
  const base = settings.value.language.split('-')[0]
  // If we don't have region-specific translations yet, fall back to base (en / zh)
  const supported = ['en', 'zh']
  setLanguage(supported.includes(base) ? base : 'en')
  
  // Save settings
  updateSettings()
  
  // Show success message
  alert(t('message.languageUpdated'))
}

const setupTwoFactor = () => {
  // TODO: Implement 2FA setup
  alert(t('message.featureComingSoon'))
}

const viewLoginHistory = () => {
  // TODO: Implement login history view
  alert(t('message.featureComingSoon'))
}

const clearCache = () => {
  if (confirm(t('message.confirmClearCache'))) {
    localStorage.clear()
    sessionStorage.clear()
    alert(t('message.cacheCleared'))
  }
}

// Computed list of languages
const languages = computed(() => availableLanguages)
</script>
