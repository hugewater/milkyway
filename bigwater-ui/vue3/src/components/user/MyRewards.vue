<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Rewards Summary -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-green-100">
                <svg class="w-6 h-6 text-forest-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Total Earned</p>
                <p class="text-2xl font-bold text-deep-ocean">${{ totalEarnings.totalEarnings.toFixed(2) }}</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-blue-100">
                <svg class="w-6 h-6 text-ocean" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">This Month</p>
                <p class="text-2xl font-bold text-deep-ocean">${{ (totalEarnings.totalEarnings * 0.3).toFixed(2) }}</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-purple-100">
                <div class="text-2xl">{{ userLevel.icon }}</div>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Current Level</p>
                <p class="text-2xl font-bold text-deep-ocean">{{ userLevel.name }}</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-yellow-100">
                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Team Size</p>
                <p class="text-2xl font-bold text-deep-ocean">{{ referralData.directReferrals + referralData.tier2Referrals + referralData.tier3Referrals }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Current Level Status -->
        <div class="card p-6 rounded-2xl mb-8">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-bold text-deep-ocean">Your Current Status</h3>
            <div class="flex items-center space-x-2">
              <span class="text-3xl">{{ userLevel.icon }}</span>
              <div>
                <p class="font-bold text-deep-ocean">{{ userLevel.name }}</p>
                <p class="text-sm text-gray-600">{{ userLevel.stars }} Star Level</p>
              </div>
            </div>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div class="bg-blue-50 p-4 rounded-lg text-center">
              <p class="text-sm text-gray-600">Direct Referrals</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ referralData.directReferrals }}</p>
            </div>
            <div class="bg-green-50 p-4 rounded-lg text-center">
              <p class="text-sm text-gray-600">Tier 2 Network</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ referralData.tier2Referrals }}</p>
            </div>
            <div class="bg-yellow-50 p-4 rounded-lg text-center">
              <p class="text-sm text-gray-600">Tier 3 Network</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ referralData.tier3Referrals }}</p>
            </div>
          </div>

          <!-- Level Benefits -->
          <div class="bg-gradient-to-r from-blue-50 to-green-50 p-4 rounded-lg">
            <h4 class="font-semibold text-deep-ocean mb-3">Your Level Benefits</h4>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
              <div>
                <p class="text-gray-600">Tier 1 Bonus</p>
                <p class="font-bold text-green-600">{{ (tier1Bonus * 100).toFixed(1) }}%</p>
              </div>
              <div>
                <p class="text-gray-600">Tier 2 Bonus</p>
                <p class="font-bold text-green-600">{{ (tier2Bonus * 100).toFixed(1) }}%</p>
              </div>
              <div>
                <p class="text-gray-600">Tier 3 Bonus</p>
                <p class="font-bold text-green-600">{{ (tier3Bonus * 100).toFixed(1) }}%</p>
              </div>
            </div>
          </div>

          <!-- Level Progress -->
          <div v-if="userLevel.level < 5" class="mt-6">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Progress to {{ Object.values(SUBSCRIBER_LEVELS)[userLevel.level].name }}</span>
              <span class="text-sm text-gray-600">{{ referralData.directReferrals }}/{{ Object.values(SUBSCRIBER_LEVELS)[userLevel.level].minReferrals }}</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-3">
              <div 
                class="ocean-gradient h-3 rounded-full transition-all duration-300" 
                :style="{ width: `${Math.min(100, (referralData.directReferrals / Object.values(SUBSCRIBER_LEVELS)[userLevel.level].minReferrals) * 100)}%` }"
              ></div>
            </div>
            <p class="text-xs text-gray-500 mt-2">
              {{ Object.values(SUBSCRIBER_LEVELS)[userLevel.level].minReferrals - referralData.directReferrals }} more referrals needed to reach {{ Object.values(SUBSCRIBER_LEVELS)[userLevel.level].name }}
            </p>
          </div>
        </div>

        <!-- Rewards Structure -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Multi-Level Reward Structure</h3>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="text-center p-4 border-2 border-ocean rounded-lg">
              <div class="w-12 h-12 bg-ocean rounded-full flex items-center justify-center mx-auto mb-3">
                <span class="text-white font-bold">T1</span>
              </div>
              <h4 class="font-semibold text-deep-ocean mb-2">Tier 1 - Direct</h4>
              <p class="text-sm text-gray-600 mb-2">Base: 15% + Level Bonus</p>
              <p class="text-xs text-gray-500">Your rate: {{ (tier1Bonus * 100).toFixed(1) }}%</p>
              <p class="text-xs text-green-600 font-medium">${{ totalEarnings.tier1Earnings.toFixed(2) }} earned</p>
            </div>

            <div class="text-center p-4 border-2 border-forest-green rounded-lg">
              <div class="w-12 h-12 bg-forest-green rounded-full flex items-center justify-center mx-auto mb-3">
                <span class="text-white font-bold">T2</span>
              </div>
              <h4 class="font-semibold text-deep-ocean mb-2">Tier 2 - Indirect</h4>
              <p class="text-sm text-gray-600 mb-2">Base: 5% + Level Bonus</p>
              <p class="text-xs text-gray-500">Your rate: {{ (tier2Bonus * 100).toFixed(1) }}%</p>
              <p class="text-xs text-green-600 font-medium">${{ totalEarnings.tier2Earnings.toFixed(2) }} earned</p>
            </div>

            <div class="text-center p-4 border-2 border-yellow-500 rounded-lg">
              <div class="w-12 h-12 bg-yellow-500 rounded-full flex items-center justify-center mx-auto mb-3">
                <span class="text-white font-bold">T3</span>
              </div>
              <h4 class="font-semibold text-deep-ocean mb-2">Tier 3 - Network</h4>
              <p class="text-sm text-gray-600 mb-2">Base: 1% + Level Bonus</p>
              <p class="text-xs text-gray-500">Your rate: {{ (tier3Bonus * 100).toFixed(1) }}%</p>
              <p class="text-xs text-green-600 font-medium">${{ totalEarnings.tier3Earnings.toFixed(2) }} earned</p>
            </div>
          </div>
        </div>

        <!-- Level System -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">5-Star Level System</h3>
          <div class="space-y-4">
            <div v-for="level in Object.values(SUBSCRIBER_LEVELS)" :key="level.level" 
                 :class="`p-4 rounded-lg border-2 ${userLevel.level === level.level ? 'border-ocean bg-blue-50' : 'border-gray-200'}`">
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-3">
                  <span class="text-2xl">{{ level.icon }}</span>
                  <div>
                    <h4 class="font-semibold text-deep-ocean">{{ level.name }}</h4>
                    <p class="text-sm text-gray-600">{{ level.stars }} Star Level</p>
                  </div>
                </div>
                <div class="text-right">
                  <p class="text-sm text-gray-600">Min Referrals: {{ level.minReferrals }}</p>
                  <p class="text-sm font-medium text-green-600">Bonus: +{{ (level.bonusMultiplier * 100).toFixed(0) }}%</p>
                </div>
              </div>
              <div v-if="userLevel.level === level.level" class="mt-2 text-xs text-blue-600 font-medium">
                ✓ Current Level
              </div>
            </div>
          </div>
        </div>

        <!-- Recent Rewards -->
        <div class="card p-6 rounded-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Recent Rewards</h3>
          <div class="space-y-3">
            <div v-for="reward in recentRewards" :key="reward.id" class="flex items-center justify-between py-3 border-b border-gray-100 last:border-0">
              <div class="flex items-center space-x-3">
                <div :class="`w-2 h-2 rounded-full ${reward.type === 'tier1' ? 'bg-ocean' : reward.type === 'tier2' ? 'bg-forest-green' : 'bg-yellow-500'}`"></div>
                <div>
                  <span class="text-sm text-gray-700">{{ reward.description }}</span>
                  <p class="text-xs text-gray-500">{{ reward.referralName }} - {{ reward.tier }}</p>
                </div>
              </div>
              <div class="text-right">
                <span class="text-sm font-medium text-green-600">${{ reward.amount }}</span>
                <p class="text-xs text-gray-500">{{ reward.date }}</p>
              </div>
            </div>
          </div>
        </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { calculateUserLevel, calculateTotalEarnings, calculateBonusPercentage, SUBSCRIBER_LEVELS, REWARD_STRUCTURE } from '../../utils/userLevels.js'
import AppLayout from '../layouts/AppLayout.vue'

// Mock referral data
const referralData = ref({
  directReferrals: 12,
  tier2Referrals: 23,
  tier3Referrals: 12
})

const userLevel = computed(() => {
  return calculateUserLevel(referralData.value.directReferrals)
})

const totalEarnings = computed(() => {
  return calculateTotalEarnings(
    userLevel.value,
    referralData.value.directReferrals,
    referralData.value.tier2Referrals,
    referralData.value.tier3Referrals
  )
})

const tier1Bonus = computed(() => {
  return calculateBonusPercentage(userLevel.value, 'TIER_1')
})

const tier2Bonus = computed(() => {
  return calculateBonusPercentage(userLevel.value, 'TIER_2')
})

const tier3Bonus = computed(() => {
  return calculateBonusPercentage(userLevel.value, 'TIER_3')
})

const recentRewards = ref([
  { id: 1, description: 'Direct referral bonus', referralName: 'John Doe', tier: 'Tier 1', amount: '4.50', date: '2 hours ago', type: 'tier1' },
  { id: 2, description: 'Network bonus', referralName: 'Sarah Johnson', tier: 'Tier 2', amount: '1.50', date: '1 day ago', type: 'tier2' },
  { id: 3, description: 'Extended network bonus', referralName: 'Mike Wilson', tier: 'Tier 3', amount: '0.30', date: '2 days ago', type: 'tier3' },
  { id: 4, description: 'Direct referral bonus', referralName: 'Emma Davis', tier: 'Tier 1', amount: '4.50', date: '3 days ago', type: 'tier1' },
  { id: 5, description: 'Network bonus', referralName: 'James Brown', tier: 'Tier 2', amount: '1.50', date: '4 days ago', type: 'tier2' }
])
</script>