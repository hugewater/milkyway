<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Welcome section -->
        <div class="mb-8 card p-6 rounded-2xl">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h2 class="text-xl font-bold text-deep-ocean mb-2">Welcome back, {{ userName }}!</h2>
              <div class="flex items-center space-x-4">
                <div class="flex items-center space-x-2">
                  <span class="text-2xl">{{ userLevel.icon }}</span>
                  <div>
                    <p class="font-semibold text-deep-ocean">{{ userLevel.name }}</p>
                    <p class="text-sm text-gray-600">{{ userLevel.stars }} Star Level</p>
                  </div>
                </div>
                <div class="text-right">
                  <p class="text-sm text-gray-600">Role</p>
                  <p class="font-semibold text-deep-ocean capitalize">{{ userRole }}</p>
                </div>
              </div>
            </div>
            <div class="text-right">
              <p class="text-sm text-gray-600">Referral Code</p>
              <span class="font-mono bg-gray-100 px-3 py-1 rounded text-deep-ocean">{{ userReferralCode }}</span>
            </div>
          </div>
          
          <!-- Level Progress -->
          <div v-if="userLevel.level < 5" class="mt-4">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Progress to {{ Object.values(SUBSCRIBER_LEVELS)[userLevel.level].name }}</span>
              <span class="text-sm text-gray-600">{{ referralData.directReferrals }}/{{ Object.values(SUBSCRIBER_LEVELS)[userLevel.level].minReferrals }}</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2">
              <div 
                class="ocean-gradient h-2 rounded-full transition-all duration-300" 
                :style="{ width: `${Math.min(100, (referralData.directReferrals / Object.values(SUBSCRIBER_LEVELS)[userLevel.level].minReferrals) * 100)}%` }"
              ></div>
            </div>
          </div>
        </div>

        <!-- Stats grid -->
        <div class="responsive-grid mb-6 lg:mb-8">
          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-blue-100">
                <svg class="w-6 h-6 text-ocean" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Active Subscription</p>
                <p class="text-2xl font-bold text-deep-ocean">{{ subscriptionType }}</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-green-100">
                <svg class="w-6 h-6 text-forest-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Team Members</p>
                <p class="text-2xl font-bold text-deep-ocean">12</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-yellow-100">
                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Total Rewards</p>
                <p class="text-2xl font-bold text-deep-ocean">$245 USDT</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-purple-100">
                <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Total Winnings</p>
                <p class="text-2xl font-bold text-deep-ocean">$0 USDT</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Current Week Numbers -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-4">This Week's Numbers</h3>
          <div class="space-y-4">
            <div v-for="(set, index) in currentWeekNumbers" :key="index" class="flex items-center space-x-2">
              <span class="text-sm text-gray-600 w-16">Set {{ index + 1 }}:</span>
              <div class="flex space-x-1">
                <div v-for="(number, numIndex) in set.slice(0, 5)" :key="numIndex" 
                     class="number-ball">
                  {{ number }}
                </div>
                <div class="number-ball powerball">
                  {{ set[5] }}
                </div>
              </div>
              <button class="text-ocean hover:text-deep-ocean text-sm font-medium ml-4">
                Customize
              </button>
            </div>
          </div>
        </div>

        <!-- Recent Activity -->
        <div class="card p-6 rounded-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-4">Recent Activity</h3>
          <div class="space-y-4">
            <div class="flex items-center justify-between py-3 border-b border-gray-100 last:border-0">
              <div class="flex items-center space-x-3">
                <div class="w-2 h-2 bg-green-500 rounded-full"></div>
                <span class="text-sm text-gray-700">Subscription renewed for Week 45</span>
              </div>
              <span class="text-xs text-gray-500">2 hours ago</span>
            </div>
            <div class="flex items-center justify-between py-3 border-b border-gray-100 last:border-0">
              <div class="flex items-center space-x-3">
                <div class="w-2 h-2 bg-blue-500 rounded-full"></div>
                <span class="text-sm text-gray-700">New team member joined</span>
              </div>
              <span class="text-xs text-gray-500">1 day ago</span>
            </div>
            <div class="flex items-center justify-between py-3 border-b border-gray-100 last:border-0">
              <div class="flex items-center space-x-3">
                <div class="w-2 h-2 bg-yellow-500 rounded-full"></div>
                <span class="text-sm text-gray-700">Reward earned: $15 USDT</span>
              </div>
              <span class="text-xs text-gray-500">3 days ago</span>
            </div>
          </div>
        </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { calculateUserLevel, calculateTotalEarnings, SUBSCRIBER_LEVELS } from '../utils/userLevels.js'
import AppLayout from './layouts/AppLayout.vue'

const router = useRouter()

// User data
const userData = ref({})
const currentWeekNumbers = ref([])

// Mock referral data
const referralData = ref({
  directReferrals: 12,
  tier2Referrals: 23,
  tier3Referrals: 12
})

const userName = computed(() => {
  return userData.value.firstName && userData.value.lastName 
    ? `${userData.value.firstName} ${userData.value.lastName}`
    : userData.value.email || 'User'
})

const userInitials = computed(() => {
  if (userData.value.firstName && userData.value.lastName) {
    return `${userData.value.firstName[0]}${userData.value.lastName[0]}`.toUpperCase()
  }
  return userData.value.email ? userData.value.email[0].toUpperCase() : 'U'
})

const userReferralCode = computed(() => {
  return userData.value.referralCode || 'BW12345678'
})

const subscriptionType = computed(() => {
  return userData.value.subscriptionType === 'double' ? 'Double Win' : 'Standard'
})

// Weeks subscribed: prefer stored user field; fallback to 24 (treated as fully eligible)
const weeksSubscribed = computed(() => {
  return userData.value?.weeksSubscribed ?? userData.value?.subscriptionWeeks ?? 24
})

const userLevel = computed(() => {
  return calculateUserLevel(referralData.value.directReferrals, weeksSubscribed.value)
})

const userRole = computed(() => {
  return userData.value.role || 'subscriber'
})

const totalEarnings = computed(() => {
  return calculateTotalEarnings(
    userLevel.value,
    referralData.value.directReferrals,
    referralData.value.tier2Referrals,
    referralData.value.tier3Referrals
  )
})

// Generate random Powerball numbers
const generatePowerballNumbers = () => {
  const numbers = []
  const setsCount = userData.value.subscriptionType === 'double' ? 20 : 10
  
  for (let i = 0; i < setsCount; i++) {
    const set = []
    // Generate 5 white balls (1-69)
    const whiteBalls = []
    while (whiteBalls.length < 5) {
      const num = Math.floor(Math.random() * 69) + 1
      if (!whiteBalls.includes(num)) {
        whiteBalls.push(num)
      }
    }
    set.push(...whiteBalls.sort((a, b) => a - b))
    
    // Generate 1 Powerball (1-26)
    set.push(Math.floor(Math.random() * 26) + 1)
    
    numbers.push(set)
  }
  return numbers
}

const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userData.value = JSON.parse(user)
    currentWeekNumbers.value = generatePowerballNumbers()
  } else {
    router.push('/login')
  }
})
</script>