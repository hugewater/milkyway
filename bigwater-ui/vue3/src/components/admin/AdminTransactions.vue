<template>
  <AppLayout>
    <div class="p-6">
      <header class="bg-white shadow-sm border-b border-gray-200 mb-6">
        <div class="flex items-center justify-between px-6 py-4">
          <h1 class="text-2xl font-bold text-deep-ocean">Transactions</h1>
          <div class="flex gap-2">
            <input v-model="q" @input="onSearch" placeholder="Search description/address" class="border rounded px-3 py-2 text-sm w-64" />
            <select v-model="status" @change="reload()" class="border rounded px-2 py-2 text-sm">
              <option value="">All Status</option>
              <option value="PENDING">PENDING</option>
              <option value="COMPLETED">COMPLETED</option>
              <option value="FAILED">FAILED</option>
            </select>
          </div>
        </div>
      </header>

      <div class="card rounded-2xl overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th @click="toggleSort('id')" class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer">ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">From Wallet ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">To Wallet ID</th>
                <th @click="toggleSort('amount_usdt')" class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer">Amount</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                <th @click="toggleSort('created_at')" class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer">Created</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="t in items" :key="t.id">
                <td class="px-4 py-2 text-sm text-gray-900">{{ t.id }}</td>
                <td class="px-4 py-2 text-sm font-mono">{{ t.walletId || t.fromWalletId || '-' }}</td>
                <td class="px-4 py-2 text-sm font-mono">{{ t.toWalletId || '-' }}</td>
                <td class="px-4 py-2 text-sm">${{ t.amountUsdt }}</td>
                <td class="px-4 py-2 text-sm">
                  <select v-model="t.status" @change="update(t)" class="border rounded px-2 py-1 text-xs">
                    <option>PENDING</option>
                    <option>COMPLETED</option>
                    <option>FAILED</option>
                  </select>
                </td>
                <td class="px-4 py-2 text-sm">
                  <input v-model="t.description" @blur="update(t)" class="border rounded px-2 py-1 text-xs w-72" />
                </td>
                <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(t.createdAt) }}</td>
                <td class="px-4 py-2 text-sm space-x-3">
                  <button @click="view(t)" class="text-ocean hover:text-deep-ocean text-sm">View</button>
                  <button @click="remove(t)" class="text-red-600 hover:text-red-700 text-sm">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="flex items-center justify-between p-4 border-t">
          <div class="text-sm text-gray-600">Total: {{ total }} · Page {{ page }} / {{ totalPages }}</div>
          <div class="space-x-2">
            <button @click="go(1)" class="px-3 py-1 border rounded text-sm" :disabled="page===1">First</button>
            <button @click="go(page-1)" class="px-3 py-1 border rounded text-sm" :disabled="page===1">Prev</button>
            <button @click="go(page+1)" class="px-3 py-1 border rounded text-sm" :disabled="page===totalPages">Next</button>
            <button @click="go(totalPages)" class="px-3 py-1 border rounded text-sm" :disabled="page===totalPages">Last</button>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
  <!-- View Modal moved inside the main template -->
  <div v-if="showView" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Transaction Details</h3>
      <div class="space-y-2 text-sm">
        <div class="flex justify-between"><span class="text-gray-600">ID</span><span class="font-mono">{{ viewing?.id }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">From Wallet</span><span class="font-mono">{{ viewing?.fromWalletAddress || viewing?.walletId }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">To Wallet</span><span class="font-mono">{{ viewing?.toWalletAddress || viewing?.toWalletId }}</span></div>
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
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import apiService from '../../utils/api.js'

const items = ref([])
const total = ref(0)
const offset = ref(0)
const limit = ref(50)
const sort = ref('created_at')
const order = ref('desc')
const q = ref('')
const status = ref('')

const page = computed(() => Math.floor(offset.value/limit.value)+1)
const totalPages = computed(() => Math.max(1, Math.ceil((total.value||0)/(limit.value||50))))

const load = async () => {
  try {
    const params = new URLSearchParams({ offset: offset.value, limit: limit.value, sort: sort.value, order: order.value })
    if (q.value) params.set('q', q.value)
    if (status.value) params.set('status', status.value)
    const resp = await fetch(`/bw-api/transactions?${params.toString()}`)
    const data = await resp.json()
    items.value = data.data || []
    total.value = data.total || 0
  } catch (e) {
    items.value = []
    total.value = 0
  }
}

const reload = () => { offset.value = 0; load() }
const onSearch = () => { clearTimeout(window.__txn_timer); window.__txn_timer=setTimeout(reload,300) }
const toggleSort = (col) => { if (sort.value===col) order.value=order.value==='asc'?'desc':'asc'; else {sort.value=col; order.value='desc'}; reload() }
const go = (p) => { const tp=totalPages.value; const n=Math.max(1, Math.min(p, tp)); offset.value=(n-1)*limit.value; load() }

const update = async (t) => {
  try {
    await fetch(`/bw-api/transactions/${t.id}`, { method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ status: t.status, description: t.description }) })
  } catch (e) {}
}
const remove = async (t) => {
  if (!confirm('Delete this transaction?')) return
  try {
    await fetch(`/bw-api/transactions/${t.id}`, { method:'DELETE' })
    reload()
  } catch (e) {}
}

const formatDate = (d) => { try { return new Date(d).toLocaleString() } catch { return d } }
const mask = (v) => { const s=String(v||''); if (s.length<=6) return s; return 'x'.repeat(s.length-6)+s.slice(-6) }

onMounted(load)

// View modal state
const showView = ref(false)
const viewing = ref(null)
const view = (t) => { viewing.value = t; showView.value = true }
</script>

