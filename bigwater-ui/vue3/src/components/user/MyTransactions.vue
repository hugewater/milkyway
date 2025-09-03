<template>
  <AppLayout>
    <div class="p-6">
      <header class="bg-white shadow-sm border-b border-gray-200 mb-6">
        <div class="flex items-center justify-between px-6 py-4">
          <h1 class="text-2xl font-bold text-deep-ocean">My Transactions</h1>
          <button @click="load" class="px-3 py-2 border border-gray-300 rounded-lg text-sm hover:bg-gray-50">Refresh</button>
        </div>
      </header>

      <div class="card rounded-2xl overflow-hidden">
        <div v-if="loading" class="p-6 text-sm text-gray-500">Loading...</div>
        <div v-else class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">From Wallet ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">To Wallet ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Created</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="t in items" :key="t.id">
                <td class="px-4 py-2 text-sm text-gray-900">{{ t.id }}</td>
                <td class="px-4 py-2 text-sm font-mono">{{ t.walletId || '-' }}</td>
                <td class="px-4 py-2 text-sm font-mono">{{ t.toWalletId || '-' }}</td>
                <td class="px-4 py-2 text-sm">${{ formatAmount(t.amountUsdt) }}</td>
                <td class="px-4 py-2 text-sm">
                  <span :class="badge(t.status)" class="px-2 py-1 text-xs font-medium rounded-full">{{ t.status }}</span>
                </td>
                <td class="px-4 py-2 text-sm">{{ t.description }}</td>
                <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(t.createdAt) }}</td>
                <td class="px-4 py-2 text-sm">
                  <button @click="view(t)" class="text-ocean hover:text-deep-ocean text-sm">View</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </AppLayout>
  <div v-if="showView" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Transaction Details</h3>
      <div class="space-y-2 text-sm">
        <div class="flex justify-between"><span class="text-gray-600">ID</span><span class="font-mono">{{ viewing?.id }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Your Wallet</span><span class="font-mono">{{ viewing?.fromWalletAddress || viewing?.walletId || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Company Wallet</span><span class="font-mono">{{ mask(viewing?.toWalletAddress) || viewing?.toWalletId || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Amount</span><span>${{ viewing?.amountUsdt }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Status</span><span>{{ viewing?.status }}</span></div>
        <div><span class="text-gray-600">Description</span><div class="mt-1 p-2 border rounded text-xs whitespace-pre-wrap">{{ viewing?.description }}</div></div>
        <div class="flex justify-between"><span class="text-gray-600">Created</span><span>{{ formatDate(viewing?.createdAt) }}</span></div>
      </div>
      <div class="flex justify-end mt-6">
        <button @click="showView = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { getUserId } from '../../utils/auth.js'
import apiService from '../../utils/api.js'

const items = ref([])
const loading = ref(false)
const showView = ref(false)
const viewing = ref(null)

const load = async () => {
  loading.value = true
  try {
    const userId = getUserId()
    if (!userId) { items.value = []; return }
    const resp = await apiService.getTransactionsByUserId(userId)
    if (resp && resp.success) {
      const raw = resp.data || []
      // Normalize fields to match UI expectations
      items.value = raw.map(r => ({
        id: r.id,
        walletId: (r.walletId !== undefined && r.walletId !== null) ? r.walletId : (r.wallet_id !== undefined ? r.wallet_id : null),
        toWalletId: (r.toWalletId !== undefined && r.toWalletId !== null) ? r.toWalletId : (r.to_wallet_id !== undefined ? r.to_wallet_id : null),
        amountUsdt: (r.amountUsdt !== undefined && r.amountUsdt !== null) ? r.amountUsdt : ((r.amount_usdt !== undefined && r.amount_usdt !== null) ? r.amount_usdt : (r.amount !== undefined ? r.amount : 0)),
        status: (r.status !== undefined && r.status !== null) ? r.status : 'PENDING',
        description: (r.description !== undefined && r.description !== null) ? r.description : '',
        createdAt: (r.createdAt !== undefined && r.createdAt !== null) ? r.createdAt : ((r.created_at !== undefined && r.created_at !== null) ? r.created_at : (r.created !== undefined ? r.created : null)),
        fromWalletAddress: (r.fromWalletAddress !== undefined && r.fromWalletAddress !== null) ? r.fromWalletAddress : (r.from_address !== undefined ? r.from_address : null),
        toWalletAddress: (r.toWalletAddress !== undefined && r.toWalletAddress !== null) ? r.toWalletAddress : (r.to_address !== undefined ? r.to_address : null)
      }))
    } else {
      items.value = []
    }
  } catch (e) {
    items.value = []
  } finally {
    loading.value = false
  }
}

const formatDate = (d) => { try { return new Date(d).toLocaleString() } catch { return d } }
const formatAmount = (a) => { try { return Number(a || 0).toFixed(2) } catch { return a }
}
const badge = (s) => {
  const m = { PENDING: 'bg-yellow-100 text-yellow-800', COMPLETED: 'bg-green-100 text-green-800', FAILED: 'bg-red-100 text-red-800' }
  return m[s] || 'bg-gray-100 text-gray-800'
}

const mask = (addr) => {
  const s = String(addr || '')
  if (s.length <= 6) return s
  return 'x'.repeat(s.length - 6) + s.slice(-6)
}

const view = (t) => { viewing.value = t; showView.value = true }

onMounted(load)
</script>

