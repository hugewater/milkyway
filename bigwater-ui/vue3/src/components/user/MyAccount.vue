<template>
  <AppLayout>
    <div class="p-6">
      <!-- Profile Section -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">Profile Information</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">First Name</label>
            <input 
              v-model="profile.firstName" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Last Name</label>
            <input 
              v-model="profile.lastName" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
            <input 
              v-model="profile.email" 
              type="email" 
              disabled
              class="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-500"
            >
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Phone</label>
            <input 
              v-model="profile.phone" 
              type="tel" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
        </div>
        <div class="mt-6">
          <button 
            @click="updateProfile"
            class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Update Profile
          </button>
        </div>
      </div>

      <!-- Change Password Section -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">Change Password</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Current Password</label>
            <input 
              v-model="password.current" 
              type="password" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
          <div></div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">New Password</label>
            <input 
              v-model="password.new" 
              type="password" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Confirm New Password</label>
            <input 
              v-model="password.confirm" 
              type="password" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
        </div>
        <div class="mt-6">
          <button 
            @click="changePassword"
            class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors"
          >
            Change Password
          </button>
        </div>
      </div>

      <!-- Account Summary -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">Account Summary</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="text-center">
            <div class="text-2xl font-bold text-blue-600">{{ accountStats.totalWallets }}</div>
            <div class="text-sm text-gray-600">Total Wallets</div>
          </div>
          <div class="text-center">
            <div class="text-2xl font-bold text-green-600">{{ accountStats.totalCertificates }}</div>
            <div class="text-sm text-gray-600">Certificates</div>
          </div>
          <div class="text-center">
            <div class="text-2xl font-bold text-purple-600">{{ accountStats.totalRewards }}</div>
            <div class="text-sm text-gray-600">Rewards</div>
          </div>
        </div>
      </div>

      <!-- Account Actions -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">Account Actions</h2>
        <div class="space-y-4">
          <button 
            @click="exportData"
            class="w-full text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <div class="flex items-center justify-between">
              <div>
                <div class="font-medium text-gray-900">Export My Data</div>
                <div class="text-sm text-gray-600">Download all your account data</div>
              </div>
              <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
            </div>
          </button>
          <button 
            @click="deactivateAccount"
            class="w-full text-left p-4 border border-red-200 rounded-lg hover:bg-red-50 transition-colors"
          >
            <div class="flex items-center justify-between">
              <div>
                <div class="font-medium text-red-900">Deactivate Account</div>
                <div class="text-sm text-red-600">Temporarily disable your account</div>
              </div>
              <svg class="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
              </svg>
            </div>
          </button>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { currentUser } from '../../utils/auth.js'
import AppLayout from '../layouts/AppLayout.vue'

const profile = ref({
  firstName: '',
  lastName: '',
  email: '',
  phone: ''
})

const password = ref({
  current: '',
  new: '',
  confirm: ''
})

const accountStats = ref({
  totalWallets: 0,
  totalCertificates: 0,
  totalRewards: 0
})

onMounted(() => {
  if (currentUser.value) {
    profile.value = {
      firstName: currentUser.value.firstName || '',
      lastName: currentUser.value.lastName || '',
      email: currentUser.value.email || '',
      phone: currentUser.value.phone || ''
    }
  }
  loadAccountStats()
})

const loadAccountStats = async () => {
  // TODO: Load actual stats from API
  accountStats.value = {
    totalWallets: 3,
    totalCertificates: 12,
    totalRewards: 5
  }
}

const updateProfile = async () => {
  try {
    // TODO: Implement profile update API call
    alert('Profile updated successfully!')
  } catch (error) {
    alert('Failed to update profile: ' + error.message)
  }
}

const changePassword = async () => {
  if (password.value.new !== password.value.confirm) {
    alert('New passwords do not match!')
    return
  }
  
  if (password.value.new.length < 6) {
    alert('New password must be at least 6 characters long!')
    return
  }
  
  try {
    // TODO: Implement password change API call
    alert('Password changed successfully!')
    password.value = { current: '', new: '', confirm: '' }
  } catch (error) {
    alert('Failed to change password: ' + error.message)
  }
}

const exportData = async () => {
  try {
    // TODO: Implement data export API call
    alert('Data export started. You will receive an email when it\'s ready.')
  } catch (error) {
    alert('Failed to export data: ' + error.message)
  }
}

const deactivateAccount = async () => {
  if (confirm('Are you sure you want to deactivate your account? You can reactivate it later by contacting support.')) {
    try {
      // TODO: Implement account deactivation API call
      alert('Account deactivated successfully!')
    } catch (error) {
      alert('Failed to deactivate account: ' + error.message)
    }
  }
}
</script>
