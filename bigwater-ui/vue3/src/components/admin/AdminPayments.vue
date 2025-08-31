<template>
  <AppLayout>
    <div class="p-4 lg:p-6">

    
    <!-- Payment Statistics -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-green-100">
            <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Total Revenue</p>
            <p class="text-2xl font-bold text-deep-ocean">$45,230.50</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-blue-100">
            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Successful Payments</p>
            <p class="text-2xl font-bold text-deep-ocean">1,234</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-yellow-100">
            <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Pending</p>
            <p class="text-2xl font-bold text-deep-ocean">12</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-red-100">
            <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Failed</p>
            <p class="text-2xl font-bold text-deep-ocean">8</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Company Wallet -->
    <div class="card p-6 rounded-2xl mb-6">
      <h3 class="text-lg font-bold text-deep-ocean mb-6">Add Company Wallet</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">User ID (Company)</label>
          <input v-model.number="walletForm.userId" type="number" placeholder="Enter company user ID"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Wallet Address</label>
          <input v-model="walletForm.walletAddress" type="text" placeholder="Enter wallet address"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Wallet Name</label>
          <input v-model="walletForm.walletName" type="text" placeholder="Enter wallet name"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Wallet Type</label>
          <select v-model="walletForm.walletType"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <option value="MAIN">Main</option>
            <option value="TRADING">Trading</option>
            <option value="STAKING">Staking</option>
            <option value="REWARDS">Rewards</option>
          </select>
        </div>
        <div class="flex items-end">
          <button @click="createCompanyWallet"
            class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">Create Wallet
          </button>
        </div>
      </div>
      <p v-if="walletMsg" class="mt-4 text-sm" :class="walletOk ? 'text-green-600' : 'text-red-600'">{{ walletMsg }}</p>
    </div>

    <!-- Add Balance to Wallet -->
    <div class="card p-6 rounded-2xl mb-6">
      <h3 class="text-lg font-bold text-deep-ocean mb-6">Add Balance to Wallet</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Wallet ID</label>
          <input v-model.number="balanceForm.walletId" type="number" placeholder="Enter wallet ID"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Amount (USDT)</label>
          <input v-model="balanceForm.amount" type="number" step="0.000001" min="0" placeholder="Enter amount"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div class="flex items-end">
          <button @click="addBalance"
            class="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors">Add Balance
          </button>
        </div>
      </div>
      <p v-if="balanceMsg" class="mt-4 text-sm" :class="balanceOk ? 'text-green-600' : 'text-red-600'">{{ balanceMsg }}</p>
    </div>

    <div class="card p-6 rounded-2xl">
      <h3 class="text-lg font-bold text-deep-ocean mb-6">Company -> Subscriber Transfer</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Company Wallet (from)</label>
          <input v-model.number="form.fromWalletId" type="number" placeholder="Enter company wallet ID"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Subscriber Wallet (to)</label>
          <input v-model.number="form.toWalletId" type="number" placeholder="Enter subscriber wallet ID"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Amount (USDT)</label>
          <input v-model="form.amount" type="number" step="0.000001" min="0"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div class="flex items-end">
          <button @click="submitTransfer"
            class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">Transfer
          </button>
        </div>
      </div>
      <p v-if="transferMsg" class="mt-4 text-sm" :class="transferOk ? 'text-green-600' : 'text-red-600'">{{ transferMsg }}</p>
    </div>
    </div>
  </AppLayout>
</template>

<script setup>
import AppLayout from '../layouts/AppLayout.vue'
import { ref } from 'vue'
import { transferBetweenWallets, createWallet, addBalanceToWallet } from '../../utils/api.js'

const form = ref({ fromWalletId: null, toWalletId: null, amount: '' })
const transferMsg = ref('')
const transferOk = ref(false)

const walletForm = ref({ userId: 1, walletAddress: '', walletName: '', walletType: 'MAIN' })
const walletMsg = ref('')
const walletOk = ref(false)

const balanceForm = ref({ walletId: null, amount: '' })
const balanceMsg = ref('')
const balanceOk = ref(false)

const createCompanyWallet = async () => {
  walletMsg.value = ''
  walletOk.value = false
  try {
    const payload = {
      userId: String(walletForm.value.userId),
      walletAddress: walletForm.value.walletAddress,
      walletName: walletForm.value.walletName,
      walletType: walletForm.value.walletType
    }
    const resp = await createWallet(payload)
    walletOk.value = !!resp.success
    walletMsg.value = resp.message || (resp.success ? 'Wallet created successfully' : 'Wallet creation failed')
    if (resp.success) {
      // Reset form after successful creation
      walletForm.value = { userId: 1, walletAddress: '', walletName: '', walletType: 'MAIN' }
    }
  } catch (e) {
    walletOk.value = false
    walletMsg.value = e.message || 'Wallet creation failed'
  }
}

const addBalance = async () => {
  balanceMsg.value = ''
  balanceOk.value = false
  try {
    const payload = {
      walletId: balanceForm.value.walletId,
      amount: String(balanceForm.value.amount)
    }
    const resp = await addBalanceToWallet(payload)
    balanceOk.value = !!resp.success
    balanceMsg.value = resp.message || (resp.success ? 'Balance added successfully' : 'Failed to add balance')
    if (resp.success) {
      // Reset form after successful addition
      balanceForm.value = { walletId: null, amount: '' }
    }
  } catch (e) {
    balanceOk.value = false
    balanceMsg.value = e.message || 'Failed to add balance'
  }
}

const submitTransfer = async () => {
  transferMsg.value = ''
  transferOk.value = false
  try {
    const payload = {
      fromWalletId: form.value.fromWalletId,
      toWalletId: form.value.toWalletId,
      amount: String(form.value.amount)
    }
    const resp = await transferBetweenWallets(payload)
    transferOk.value = !!resp.success
    transferMsg.value = resp.message || (resp.success ? 'Transfer completed' : 'Transfer failed')
  } catch (e) {
    transferOk.value = false
    transferMsg.value = e.message || 'Transfer failed'
  }
}
</script>