<template>
  <AppLayout>
    <div class="px-4 lg:px-6 py-6">
      <div class="flex justify-end mb-4">
        <button
          @click="showAddWalletModal = true"
          class="btn-primary px-4 py-2 rounded-lg"
        >
          Add Wallet
        </button>
      </div>

      <!-- Main Content -->
      <div class="max-w-7xl mx-auto">
      <!-- Stats -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-green-100 text-green-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Total Balance</p>
              <p class="text-2xl font-semibold text-gray-900">${{ totalBalance }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-blue-100 text-blue-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Total Wallets</p>
              <p class="text-2xl font-semibold text-gray-900">{{ wallets.length }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-yellow-100 text-yellow-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Verified Wallets</p>
              <p class="text-2xl font-semibold text-gray-900">{{ verifiedWalletsCount }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Wallets List -->
      <div class="bg-white rounded-lg shadow">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">My Wallets</h3>
        </div>
        <div class="p-6">
          <div v-if="isLoading" class="text-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-ocean mx-auto"></div>
            <p class="mt-2 text-gray-500">Loading wallets...</p>
          </div>
          
          <div v-else-if="wallets.length === 0" class="text-center py-8">
            <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
            </svg>
            <h3 class="mt-2 text-sm font-medium text-gray-900">No wallets</h3>
            <p class="mt-1 text-sm text-gray-500">Get started by adding your first wallet.</p>
            <div class="mt-6">
              <button
                @click="showAddWalletModal = true"
                class="btn-primary px-4 py-2 rounded-lg"
              >
                Add Wallet
              </button>
            </div>
          </div>
          
          <div v-else class="space-y-4">
            <div v-for="wallet in wallets" :key="wallet.id" class="border border-gray-200 rounded-lg p-4">
              <div class="flex items-center justify-between">
                <div class="flex items-center space-x-4">
                  <div class="flex-shrink-0">
                    <div class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center">
                      <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
                      </svg>
                    </div>
                  </div>
                  <div>
                    <h4 class="text-sm font-medium text-gray-900">{{ wallet.walletName }}</h4>
                    <p class="text-sm text-gray-500">{{ wallet.walletAddress }}</p>
                    <p class="text-xs text-gray-400">{{ wallet.walletType }}</p>
                  </div>
                </div>
                <div class="flex items-center space-x-4">
                  <div class="text-right">
                    <p class="text-lg font-semibold text-gray-900">${{ wallet.balance }}</p>
                    <p class="text-sm text-gray-500">{{ wallet.walletType }}</p>
                  </div>
                  <div class="flex items-center space-x-2">
                    <span v-if="wallet.isVerified" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                      Verified
                    </span>
                    <span v-else class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                      Pending
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div>
    </div>

    <!-- Add Wallet Modal -->
    <div v-if="showAddWalletModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <h3 class="text-lg font-medium text-gray-900 mb-4">Add New Wallet</h3>
          
          <form @submit.prevent="addWallet">
            <div class="space-y-4">
              <div>
                <label for="walletName" class="block text-sm font-medium text-gray-700 mb-2">
                  Wallet Name
                </label>
                <input
                  id="walletName"
                  v-model="newWallet.walletName"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                  placeholder="Enter wallet name"
                />
              </div>

              <div>
                <label for="walletAddress" class="block text-sm font-medium text-gray-700 mb-2">
                  Wallet Address
                </label>
                <input
                  id="walletAddress"
                  v-model="newWallet.walletAddress"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                  placeholder="Enter USDT wallet address"
                />
              </div>

              <div>
                <label for="walletType" class="block text-sm font-medium text-gray-700 mb-2">
                  Wallet Type
                </label>
                <select
                  id="walletType"
                  v-model="newWallet.walletType"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-ocean"
                >
                  <option value="">Select wallet type</option>
                  <option value="PERSONAL">Personal</option>
                  <option value="BUSINESS">Business</option>
                  <option value="EXCHANGE">Exchange</option>
                </select>
              </div>
            </div>

            <div class="flex justify-end space-x-3 mt-6">
              <button
                type="button"
                @click="showAddWalletModal = false"
                class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200"
              >
                Cancel
              </button>
              <button
                type="submit"
                :disabled="isAddingWallet"
                class="btn-primary px-4 py-2 rounded-lg disabled:opacity-50"
              >
                <span v-if="isAddingWallet">Adding...</span>
                <span v-else>Add Wallet</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { getUserId } from '../../utils/auth.js'
import apiService from '../../utils/api.js'

const wallets = ref([])
const isLoading = ref(false)
const isAddingWallet = ref(false)
const showAddWalletModal = ref(false)

const newWallet = ref({
  walletName: '',
  walletAddress: '',
  walletType: ''
})

const totalBalance = computed(() => {
  return wallets.value.reduce((total, wallet) => total + parseFloat(wallet.balance || 0), 0).toFixed(2)
})

const verifiedWalletsCount = computed(() => {
  return wallets.value.filter(wallet => wallet.isVerified).length
})

const loadWallets = async () => {
  isLoading.value = true
  try {
    const userId = getUserId()
    if (userId) {
      const response = await apiService.getWalletsByUserId(userId)
      if (response.success) {
        wallets.value = response.data || []
      }
    }
  } catch (error) {
    console.error('Failed to load wallets:', error)
  } finally {
    isLoading.value = false
  }
}

const addWallet = async () => {
  if (!newWallet.value.walletName || !newWallet.value.walletAddress || !newWallet.value.walletType) {
    return
  }

  isAddingWallet.value = true
  try {
    const userId = getUserId()
    const walletData = {
      userId: userId.toString(),
      walletName: newWallet.value.walletName,
      walletAddress: newWallet.value.walletAddress,
      walletType: newWallet.value.walletType
    }

    const response = await apiService.createWallet(walletData)
    if (response.success) {
      // Add new wallet to the list
      wallets.value.push(response.data)
      
      // Reset form
      newWallet.value = {
        walletName: '',
        walletAddress: '',
        walletType: ''
      }
      
      // Close modal
      showAddWalletModal.value = false
    }
  } catch (error) {
    console.error('Failed to add wallet:', error)
  } finally {
    isAddingWallet.value = false
  }
}

onMounted(() => {
  loadWallets()
})
</script>