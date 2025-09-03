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

      <!-- Wallets List (Table like Admin) -->
      <div class="bg-white rounded-lg shadow">
        <div class="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
          <h3 class="text-lg font-medium text-gray-900">My Wallets</h3>
          <button @click="loadWallets" class="px-3 py-2 border border-gray-300 rounded-lg text-sm hover:bg-gray-50">Refresh</button>
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
              <button @click="showAddWalletModal = true" class="btn-primary px-4 py-2 rounded-lg">Add Wallet</button>
            </div>
          </div>

          <div v-else class="overflow-x-auto overflow-y-visible">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nick Name</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Address</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Balance</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="w in wallets" :key="w.id" class="hover:bg-gray-50" :class="!w.isActive ? 'bg-red-50' : ''">
                  <td class="px-4 py-2 text-sm" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'">{{ w.id }}</td>
                  <td class="px-4 py-2 text-sm" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'">{{ w.walletName || '-' }}</td>
                  <td class="px-4 py-2 text-sm font-mono" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'" :title="w.walletAddress">{{ (w.walletAddress || '').slice(0, 16) }}...</td>
                  <td class="px-4 py-2 text-sm text-gray-700">{{ w.walletType || '-' }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">${{ Number(w.balance || 0).toFixed(2) }}</td>
                  <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(w.createdAt || w.created_at) }}</td>
                  <td class="px-4 py-2 text-sm">
                    <span v-if="w.isActive" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">Active</span>
                    <span v-else class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">Inactive</span>
                  </td>
                  <td class="px-4 py-2 text-sm">
                    <div class="relative inline-block text-left" data-user-wallet-actions>
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
                            <button @click="openView(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">View</button>
                            <button @click="openPay(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">Pay</button>
                            <button @click="openEdit(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">Edit</button>
                            <button @click="copyAddress(w.walletAddress); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">Copy Address</button>
                            <button @click="toggleStatus(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50">{{ w.isActive ? 'Deactivate' : 'Activate' }}</button>
                            <button @click="deleteRow(w); closeActions()" class="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50">Delete</button>
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
                <label class="block text-sm font-medium text-gray-700 mb-2">Wallet Type</label>
                <input type="text" value="Member" disabled class="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-700" />
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
  
  <!-- View Wallet Modal -->
  <div v-if="showView" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Wallet Details</h3>
      <div class="space-y-3 text-sm">
        <div class="flex justify-between"><span class="text-gray-600">ID</span><span class="font-mono">{{ selected?.id }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Nick Name</span><span>{{ selected?.walletName || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Address</span><span class="font-mono">{{ selected?.walletAddress }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Type</span><span>{{ selected?.walletType || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Created</span><span>{{ formatDate(selected?.createdAt || selected?.created_at) }}</span></div>
      </div>
      <div class="flex justify-end mt-6">
        <button @click="showView = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
      </div>
    </div>
  </div>

  <!-- Pay Modal -->
  <div v-if="showPay" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-md">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Record Payment</h3>
      <form @submit.prevent="submitPay" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Company Wallet (to)</label>
          <select v-model="payForm.toWalletId" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <option v-for="cw in companyWallets" :key="cw.id" :value="cw.id">{{ cw.walletName || 'Company Wallet' }} ({{ maskAddress(cw.walletAddress || '') }})</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Your Pay Wallet Address</label>
          <input v-model="payForm.fromAddress" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Enter your wallet address" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Amount (USDT)</label>
          <input v-model="payForm.amount" type="number" step="0.000001" min="0" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="0.00" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Description (Optional)</label>
          <textarea v-model="payForm.description" rows="3" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Add a note (e.g., subscription period, tx hash)"></textarea>
        </div>
        <div class="flex justify-end space-x-3 pt-2">
          <button type="button" @click="showPay = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Record</button>
        </div>
        <p v-if="payMsg" class="text-sm" :class="payOk ? 'text-green-600' : 'text-red-600'">{{ payMsg }}</p>
      </form>
    </div>
  </div>

  <!-- Edit Wallet Modal -->
  <div v-if="showEdit" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Edit Wallet</h3>
      <form @submit.prevent="saveEdit" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Nick Name</label>
          <input v-model="editForm.walletName" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Address</label>
          <input v-model="editForm.walletAddress" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Type</label>
          <input type="text" value="Member" disabled class="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-700" />
        </div>
        <div class="flex justify-end space-x-3 pt-2">
          <button type="button" @click="showEdit = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Save</button>
        </div>
        <p v-if="editErr" class="text-sm text-red-600">{{ editErr }}</p>
      </form>
    </div>
  </div>

  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { getUserId } from '../../utils/auth.js'
import apiService from '../../utils/api.js'
import { toggleWalletStatus } from '../../utils/api.js'

const wallets = ref([])
const isLoading = ref(false)
const isAddingWallet = ref(false)
const showAddWalletModal = ref(false)
const openActionId = ref(null)
const menuPos = ref({ top: 0, left: 0 })
const showView = ref(false)
const showEdit = ref(false)
const showPay = ref(false)
const selected = ref(null)
const editForm = ref({ id: null, walletName: '', walletAddress: '', walletType: 'MEMBER' })
const editErr = ref('')
const companyWallets = ref([])
const selectedCompanyWallet = computed(() => companyWallets.value.find(w => w.id === payForm.value.toWalletId) || null)
const payForm = ref({ fromAddress: '', toWalletId: null, fromWalletId: null, amount: '', description: '' })
const payMsg = ref('')
const payOk = ref(false)

const newWallet = ref({
  walletName: '',
  walletAddress: '',
  walletType: 'MEMBER'
})

const totalBalance = computed(() => {
  return wallets.value.reduce((total, wallet) => total + parseFloat(wallet.balance || 0), 0).toFixed(2)
})

const verifiedWalletsCount = computed(() => {
  return wallets.value.filter(wallet => wallet.isVerified).length
})

const formatDate = (d) => {
  if (!d) return ''
  try {
    const dt = typeof d === 'string' && d.length > 10 ? d.slice(0, 19) : d
    return new Date(dt).toLocaleString()
  } catch { return String(d) }
}

const maskAddress = (addr) => {
  const s = String(addr || '')
  if (s.length <= 6) return s
  const last = s.slice(-6)
  return 'x'.repeat(s.length - 6) + last
}

const toggleActions = (id, evt) => {
  if (openActionId.value === id) {
    openActionId.value = null
    return
  }
  openActionId.value = id
  const rect = evt.currentTarget.getBoundingClientRect()
  menuPos.value = { top: rect.bottom + window.scrollY + 8, left: rect.right + window.scrollX - 176 }
}
const closeActions = () => { openActionId.value = null }

const onClickOutside = (e) => {
  const menus = document.querySelectorAll('[data-user-wallet-actions]')
  let inside = false
  menus.forEach(m => { if (m.contains(e.target)) inside = true })
  if (!inside) closeActions()
}

const openView = (w) => {
  selected.value = w
  showView.value = true
}

const openPay = (w) => {
  selected.value = w
  payForm.value = { fromAddress: w.walletAddress || '', toWalletId: null, fromWalletId: w.id, amount: '' }
  payMsg.value = ''
  // Ensure we have company wallets to select as target
  if (companyWallets.value.length === 0) {
    loadCompanyWallets().then(() => {
      if (companyWallets.value.length > 0) {
        payForm.value.toWalletId = companyWallets.value[0].id
      }
      showPay.value = true
    })
  } else {
    payForm.value.toWalletId = companyWallets.value[0]?.id || null
    showPay.value = true
  }
}

const openEdit = (w) => {
  editErr.value = ''
  selected.value = w
  editForm.value = { id: w.id, walletName: w.walletName || '', walletAddress: w.walletAddress || '', walletType: 'MEMBER' }
  showEdit.value = true
}

const saveEdit = async () => {
  editErr.value = ''
  try {
    const id = editForm.value.id
    const name = (editForm.value.walletName || '').trim()
    const addr = (editForm.value.walletAddress || '').trim()
    const type = 'MEMBER'
    if (!id) return
    const payload = {}
    if (name) payload.walletName = name
    if (addr) payload.walletAddress = addr
    payload.walletType = type
    // Optimistically update local list (no dedicated user update API; reuse admin update if exposed later)
    const idx = wallets.value.findIndex(w => w.id === id)
    if (idx !== -1) {
      if (name) wallets.value[idx].walletName = name
      if (addr) wallets.value[idx].walletAddress = addr
      wallets.value[idx].walletType = type
    }
    showEdit.value = false
  } catch (e) {
    editErr.value = e?.message || 'Update failed'
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

const copyAddress = async (addr) => {
  try {
    await navigator.clipboard.writeText(addr || '')
  } catch {}
}

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
      walletType: 'MEMBER'
    }

    const response = await apiService.createWallet(walletData)
    if (response.success) {
      // Add new wallet to the list
      wallets.value.push(response.data)
      
      // Reset form
      newWallet.value = {
        walletName: '',
        walletAddress: '',
        walletType: 'MEMBER'
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

const submitPay = async () => {
  payMsg.value = ''
  payOk.value = false
  try {
    const payload = {
      fromAddress: (payForm.value.fromAddress || '').trim(),
      toWalletId: payForm.value.toWalletId,
      fromWalletId: payForm.value.fromWalletId,
      amount: String(payForm.value.amount),
      description: (payForm.value.description || '').trim()
    }
    const resp = await apiService.recordWalletPayment(payload)
    payOk.value = !!resp.success
    payMsg.value = resp.message || (resp.success ? 'Payment recorded' : (resp.error || 'Failed to record'))
    if (resp.success) {
      showPay.value = false
    }
  } catch (e) {
    payOk.value = false
    payMsg.value = e?.message || 'Failed to record'
  }
}

const loadCompanyWallets = async () => {
  try {
    const resp = await apiService.getWalletsPaged({ offset: 0, limit: 50, type: 'COMPANY', active: true })
    companyWallets.value = resp?.data || []
  } catch (e) {
    companyWallets.value = []
  }
}

onMounted(() => {
  loadWallets()
  document.addEventListener('click', onClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', onClickOutside)
})
</script>