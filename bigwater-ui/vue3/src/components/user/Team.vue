<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
        <!-- Team Overview -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-blue-100">
                <svg class="w-6 h-6 text-ocean" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Direct Referrals</p>
                <p class="text-2xl font-bold text-deep-ocean">12</p>
              </div>
            </div>
          </div>

          <div class="card p-6 rounded-2xl">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-green-100">
                <svg class="w-6 h-6 text-forest-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Total Network</p>
                <p class="text-2xl font-bold text-deep-ocean">47</p>
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
                <p class="text-sm text-gray-600">Network Value</p>
                <p class="text-2xl font-bold text-deep-ocean">$1,410</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Referral Link -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-4">Your Referral Link</h3>
          <div class="flex items-center space-x-3">
            <input
              type="text"
              :value="referralLink"
              readonly
              class="flex-1 px-4 py-3 border border-gray-300 rounded-lg bg-gray-50"
            />
            <button
              @click="copyReferralLink"
              class="btn-primary px-6 py-3 rounded-lg font-medium"
            >
              Copy Link
            </button>
          </div>
          <p class="text-sm text-gray-600 mt-2">
            Share this link to earn commissions from new subscribers
          </p>
        </div>

        <!-- Team Structure -->
        <div class="card p-6 rounded-2xl mb-8">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Team Structure</h3>
          
          <!-- Level 1 - Direct Referrals -->
          <div class="mb-6">
            <h4 class="font-semibold text-deep-ocean mb-4 flex items-center">
              <span class="w-6 h-6 bg-ocean text-white rounded-full flex items-center justify-center text-sm mr-2">1</span>
              Level 1 - Direct Referrals (12 members)
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="member in level1Members" :key="member.id" class="border border-gray-200 rounded-lg p-4">
                <div class="flex items-center space-x-3 mb-2">
                  <div class="w-10 h-10 bg-ocean rounded-full flex items-center justify-center text-white font-medium">
                    {{ member.initials }}
                  </div>
                  <div>
                    <p class="font-medium text-gray-900">{{ member.name }}</p>
                    <p class="text-sm text-gray-500">{{ member.email }}</p>
                  </div>
                </div>
                <div class="flex justify-between text-sm">
                  <span class="text-gray-600">Joined: {{ member.joinDate }}</span>
                  <span class="text-forest-green font-medium">${{ member.contribution }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Level 2 - Indirect Referrals -->
          <div class="mb-6">
            <h4 class="font-semibold text-deep-ocean mb-4 flex items-center">
              <span class="w-6 h-6 bg-forest-green text-white rounded-full flex items-center justify-center text-sm mr-2">2</span>
              Level 2 - Indirect Referrals (23 members)
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="member in level2Members" :key="member.id" class="border border-gray-200 rounded-lg p-4">
                <div class="flex items-center space-x-3 mb-2">
                  <div class="w-10 h-10 bg-forest-green rounded-full flex items-center justify-center text-white font-medium">
                    {{ member.initials }}
                  </div>
                  <div>
                    <p class="font-medium text-gray-900">{{ member.name }}</p>
                    <p class="text-sm text-gray-500">{{ member.email }}</p>
                  </div>
                </div>
                <div class="flex justify-between text-sm">
                  <span class="text-gray-600">Joined: {{ member.joinDate }}</span>
                  <span class="text-forest-green font-medium">${{ member.contribution }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Level 3 - Third Level -->
          <div>
            <h4 class="font-semibold text-deep-ocean mb-4 flex items-center">
              <span class="w-6 h-6 bg-yellow-500 text-white rounded-full flex items-center justify-center text-sm mr-2">3</span>
              Level 3 - Third Level (12 members)
            </h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="member in level3Members" :key="member.id" class="border border-gray-200 rounded-lg p-4">
                <div class="flex items-center space-x-3 mb-2">
                  <div class="w-10 h-10 bg-yellow-500 rounded-full flex items-center justify-center text-white font-medium">
                    {{ member.initials }}
                  </div>
                  <div>
                    <p class="font-medium text-gray-900">{{ member.name }}</p>
                    <p class="text-sm text-gray-500">{{ member.email }}</p>
                  </div>
                </div>
                <div class="flex justify-between text-sm">
                  <span class="text-gray-600">Joined: {{ member.joinDate }}</span>
                  <span class="text-forest-green font-medium">${{ member.contribution }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Team Performance -->
        <div class="card p-6 rounded-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-6">Team Performance</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <h4 class="font-semibold text-gray-900 mb-4">Monthly Growth</h4>
              <div class="space-y-3">
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">November 2024</span>
                  <span class="text-sm font-medium text-green-600">+5 members</span>
                </div>
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">October 2024</span>
                  <span class="text-sm font-medium text-green-600">+8 members</span>
                </div>
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">September 2024</span>
                  <span class="text-sm font-medium text-green-600">+12 members</span>
                </div>
              </div>
            </div>
            <div>
              <h4 class="font-semibold text-gray-900 mb-4">Top Performers</h4>
              <div class="space-y-3">
                <div class="flex justify-between items-center">
                  <div class="flex items-center space-x-2">
                    <div class="w-6 h-6 bg-gold rounded-full flex items-center justify-center text-white text-xs">1</div>
                    <span class="text-sm text-gray-900">Sarah Johnson</span>
                  </div>
                  <span class="text-sm font-medium text-green-600">$450</span>
                </div>
                <div class="flex justify-between items-center">
                  <div class="flex items-center space-x-2">
                    <div class="w-6 h-6 bg-gray-400 rounded-full flex items-center justify-center text-white text-xs">2</div>
                    <span class="text-sm text-gray-900">Mike Chen</span>
                  </div>
                  <span class="text-sm font-medium text-green-600">$320</span>
                </div>
                <div class="flex justify-between items-center">
                  <div class="flex items-center space-x-2">
                    <div class="w-6 h-6 bg-yellow-600 rounded-full flex items-center justify-center text-white text-xs">3</div>
                    <span class="text-sm text-gray-900">Lisa Brown</span>
                  </div>
                  <span class="text-sm font-medium text-green-600">$280</span>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

const referralLink = ref('https://6768688.com/signup?ref=BW12345678')

// Mock team data
const level1Members = ref([
  { id: 1, name: 'Sarah Johnson', email: 'sarah@email.com', initials: 'SJ', joinDate: '2024-10-15', contribution: '120' },
  { id: 2, name: 'Mike Chen', email: 'mike@email.com', initials: 'MC', joinDate: '2024-10-12', contribution: '90' },
  { id: 3, name: 'Lisa Brown', email: 'lisa@email.com', initials: 'LB', joinDate: '2024-10-08', contribution: '150' },
  { id: 4, name: 'David Wilson', email: 'david@email.com', initials: 'DW', joinDate: '2024-10-05', contribution: '75' }
])

const level2Members = ref([
  { id: 5, name: 'Emma Davis', email: 'emma@email.com', initials: 'ED', joinDate: '2024-10-20', contribution: '60' },
  { id: 6, name: 'James Miller', email: 'james@email.com', initials: 'JM', joinDate: '2024-10-18', contribution: '45' },
  { id: 7, name: 'Olivia Garcia', email: 'olivia@email.com', initials: 'OG', joinDate: '2024-10-16', contribution: '80' }
])

const level3Members = ref([
  { id: 8, name: 'Noah Martinez', email: 'noah@email.com', initials: 'NM', joinDate: '2024-10-25', contribution: '30' },
  { id: 9, name: 'Ava Rodriguez', email: 'ava@email.com', initials: 'AR', joinDate: '2024-10-22', contribution: '40' }
])

const copyReferralLink = () => {
  navigator.clipboard.writeText(referralLink.value)
  alert('Referral link copied to clipboard!')
}
</script>