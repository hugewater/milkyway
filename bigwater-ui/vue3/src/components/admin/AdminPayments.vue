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
            <option value="COMPANY">Company</option>
            <option value="MEMBER">Member</option>
            <option value="TESTING">Testing</option>
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

    <!-- Company Wallets List -->
    <div class="card p-6 rounded-2xl mt-6">
      <div class="flex items-center justify-between mb-4">
        <h3 class="text-lg font-bold text-deep-ocean">Company Wallets</h3>
        <button @click="loadCompanyWallets" class="px-3 py-2 border border-gray-300 rounded-lg text-sm hover:bg-gray-50">
          Refresh
        </button>
      </div>
      <div class="overflow-x-auto overflow-y-visible">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nick Name</th>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Address</th>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
              <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-if="loadingWallets">
              <td colspan="5" class="px-4 py-6 text-center text-sm text-gray-500">Loading wallets...</td>
            </tr>
            <tr v-for="w in wallets" :key="w.id" class="hover:bg-gray-50" :class="!w.isActive ? 'bg-red-50' : ''">
              <td class="px-4 py-2 text-sm" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'">{{ w.id }}</td>
              <td class="px-4 py-2 text-sm" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'">{{ w.walletName || '-' }}</td>
              <td class="px-4 py-2 text-sm font-mono" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'" :title="w.walletAddress">{{ (w.walletAddress || '').slice(0, 10) }}...</td>
              <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(w.createdAt || w.created_at) }}</td>
              <td class="px-4 py-2 text-sm">
                <div class="relative inline-block text-left" data-wallet-actions-menu>
                  <button @click="toggleActions(w.id, $event)"
                    class="inline-flex justify-center w-9 h-9 items-center rounded-md border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6h.01M12 12h.01M12 18h.01"></path>
                    </svg>
                  </button>
                  <teleport to="body">
                    <div v-if="openActionId === w.id"
                         class="fixed w-44 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50"
                         :style="{ top: `${menuPos.top}px`, left: `${menuPos.left}px` }">
                      <div class="py-1">
                        <button @click="openViewWallet(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">View</button>
                        <button @click="openEditWallet(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">Edit</button>
                        <button @click="toggleStatus(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">{{ w.isActive ? 'Deactivate' : 'Activate' }}</button>
                        <button @click="deleteWalletRow(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50">Delete</button>
                      </div>
                    </div>
                  </teleport>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- View Wallet Modal -->
    <div v-if="showViewModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Wallet Details</h3>
        <div class="space-y-3 text-sm">
          <div class="flex justify-between"><span class="text-gray-600">ID</span><span class="font-mono">{{ selectedWallet?.id }}</span></div>
          <div class="flex justify-between"><span class="text-gray-600">Nick Name</span><span>{{ selectedWallet?.walletName || '-' }}</span></div>
          <div class="flex justify-between"><span class="text-gray-600">Address</span><span class="font-mono">{{ selectedWallet?.walletAddress }}</span></div>
          <div class="flex justify-between"><span class="text-gray-600">Type</span><span>{{ selectedWallet?.walletType || '-' }}</span></div>
          <div class="flex justify-between"><span class="text-gray-600">Created</span><span>{{ formatDate(selectedWallet?.createdAt || selectedWallet?.created_at) }}</span></div>
        </div>
        <div class="flex justify-end mt-6">
          <button @click="showViewModal = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
        </div>
      </div>
    </div>

    <!-- Edit Wallet Modal -->
    <div v-if="showEditModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Edit Wallet</h3>
        <form @submit.prevent="saveWalletEdit" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Nick Name</label>
            <input v-model="editWalletForm.walletName" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Address</label>
            <input v-model="editWalletForm.walletAddress" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Type</label>
            <select v-model="editWalletForm.walletType" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
              <option value="COMPANY">Company</option>
              <option value="MEMBER">Member</option>
              <option value="TESTING">Testing</option>
            </select>
          </div>
          <div class="flex justify-end space-x-3 pt-2">
            <button type="button" @click="showEditModal = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Save</button>
          </div>
          <p v-if="editErrorMsg" class="text-sm text-red-600">{{ editErrorMsg }}</p>
        </form>
      </div>
    </div>
    </div>
  </AppLayout>
</template>

<script setup>
import AppLayout from '../layouts/AppLayout.vue'
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { transferBetweenWallets, createWallet, addBalanceToWallet, getWallets, toggleWalletStatus, deleteWallet, updateWallet } from '../../utils/api.js'

const form = ref({ fromWalletId: null, toWalletId: null, amount: '' })
const transferMsg = ref('')
const transferOk = ref(false)

const walletForm = ref({ userId: 1, walletAddress: '', walletName: '', walletType: 'COMPANY' })
const walletMsg = ref('')
const walletOk = ref(false)

const balanceForm = ref({ walletId: null, amount: '' })
const balanceMsg = ref('')
const balanceOk = ref(false)

// Company wallets list & modals
const wallets = ref([])
const loadingWallets = ref(false)
const showViewModal = ref(false)
const showEditModal = ref(false)
const editErrorMsg = ref('')
const selectedWallet = ref(null)
const editWalletForm = ref({ id: null, walletName: '', walletAddress: '', walletType: 'MAIN' })
const openActionId = ref(null)
const menuPos = ref({ top: 0, left: 0 })

const toggleActions = (id, evt) => {
  if (openActionId.value === id) {
    openActionId.value = null
    return
  }
  openActionId.value = id
  const rect = evt.currentTarget.getBoundingClientRect()
  // 放到按钮右下角
  menuPos.value = { top: rect.bottom + window.scrollY + 8, left: rect.right + window.scrollX - 176 }
}
const closeActions = () => { openActionId.value = null }

const onClickOutside = (e) => {
  // 如果点击不在任何菜单内，则关闭
  const menus = document.querySelectorAll('[data-wallet-actions-menu]')
  let inside = false
  menus.forEach(m => { if (m.contains(e.target)) inside = true })
  if (!inside) closeActions()
}

const formatDate = (d) => {
  if (!d) return ''
  try {
    const dt = typeof d === 'string' && d.length > 10 ? d.slice(0, 19) : d
    return new Date(dt).toLocaleString()
  } catch { return String(d) }
}

const loadCompanyWallets = async () => {
  loadingWallets.value = true
  try {
    const resp = await getWallets()
    wallets.value = resp.data || resp || []
  } catch (e) {
    wallets.value = []
  } finally {
    loadingWallets.value = false
  }
}

const openViewWallet = (w) => {
  selectedWallet.value = w
  showViewModal.value = true
}

const openEditWallet = (w) => {
  selectedWallet.value = w
  editWalletForm.value = {
    id: w.id,
    walletName: w.walletName || '',
    walletAddress: w.walletAddress || '',
    walletType: w.walletType || 'MAIN'
  }
  editErrorMsg.value = ''
  showEditModal.value = true
}

const saveWalletEdit = async () => {
  try {
    const { id, walletName, walletAddress, walletType } = editWalletForm.value
    const payload = {}
    // Only send non-empty fields to avoid backend validation errors
    if (walletName && walletName.trim() !== '') payload.walletName = walletName.trim()
    if (walletAddress && walletAddress.trim() !== '') payload.walletAddress = walletAddress.trim()
    if (walletType && walletType.trim() !== '') payload.walletType = walletType.trim().toUpperCase()
    const resp = await updateWallet(id, payload)
    if (resp && resp.success) {
      // Update local list
      const idx = wallets.value.findIndex(w => w.id === id)
      if (idx !== -1) {
        wallets.value[idx] = { ...wallets.value[idx], ...payload }
      }
      showEditModal.value = false
      editErrorMsg.value = ''
    }
  } catch (e) {
    editErrorMsg.value = e?.message || 'Update failed'
  }
}

const toggleStatus = async (w) => {
  try {
    const resp = await toggleWalletStatus(w.id)
    if (resp && resp.success) {
      w.isActive = !w.isActive
    }
  } catch (e) {}
}

const deleteWalletRow = async (w) => {
  if (!confirm('Delete this wallet? This action cannot be undone.')) return
  try {
    const resp = await deleteWallet(w.id)
    if (resp && resp.success) {
      wallets.value = wallets.value.filter(x => x.id !== w.id)
    }
  } catch (e) {}
}

onMounted(() => {
  loadCompanyWallets()
  document.addEventListener('click', onClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', onClickOutside)
})

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
      walletForm.value = { userId: 1, walletAddress: '', walletName: '', walletType: 'COMPANY' }
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