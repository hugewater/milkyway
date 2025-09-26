<template>
  <AppLayout>
    <div class="px-4 lg:px-6 py-6">
      <!-- Header -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-900">Company Wallet Manager</h1>
        <button
          @click="showAddModal = true"
          class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          Add Company Wallet
        </button>
      </div>

      <!-- Statistics Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-blue-100 text-blue-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Total Wallets</p>
              <p class="text-2xl font-bold text-gray-900">{{ statistics.totalCount || 0 }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-green-100 text-green-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Active Wallets</p>
              <p class="text-2xl font-bold text-gray-900">{{ statistics.activeCount || 0 }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-purple-100 text-purple-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.99 1.99 0 013 12V7a4 4 0 014-4z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Wallet Types</p>
              <p class="text-2xl font-bold text-gray-900">{{ Object.keys(statistics.byType || {}).length }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-yellow-100 text-yellow-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-500">Grouped Wallets</p>
              <p class="text-2xl font-bold text-gray-900">{{ Object.keys(groupedWallets).length }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Filters -->
      <div class="bg-white rounded-lg shadow p-4 mb-6">
        <div class="flex flex-wrap gap-4 items-center">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Filter by Type</label>
            <select 
              v-model="selectedWalletType" 
              @change="filterWallets"
              class="px-3 py-2 border border-gray-300 rounded-lg text-sm"
            >
              <option value="">All Types</option>
              <option v-for="type in walletTypes" :key="type.value" :value="type.value">
                {{ type.label }}
              </option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Filter by Status</label>
            <select 
              v-model="selectedStatus" 
              @change="filterWallets"
              class="px-3 py-2 border border-gray-300 rounded-lg text-sm"
            >
              <option value="">All Status</option>
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">View Mode</label>
            <select 
              v-model="viewMode" 
              class="px-3 py-2 border border-gray-300 rounded-lg text-sm"
            >
              <option value="list">List View</option>
              <option value="grouped">Grouped View</option>
            </select>
          </div>

          <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-1">Search</label>
            <input 
              v-model="searchQuery"
              @input="filterWallets"
              type="text" 
              placeholder="Search by name or address..."
              class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm"
            />
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading" class="text-center py-8">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
        <p class="mt-2 text-gray-500">Loading wallets...</p>
      </div>

      <!-- List View -->
      <div v-else-if="viewMode === 'list'" class="bg-white rounded-lg shadow">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">
            Company Wallets ({{ filteredWallets.length }})
          </h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Wallet Name
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Type
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Address
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Balance
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="wallet in filteredWallets" :key="wallet.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ wallet.walletName }}</div>
                  <div class="text-sm text-gray-500">ID: {{ wallet.id }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getWalletTypeClass(wallet.walletType)">
                    {{ getWalletTypeDisplay(wallet.walletType) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900 font-mono">{{ wallet.walletAddress }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ formatBalance(wallet.balance) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="wallet.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                    {{ wallet.isActive ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button @click="editWallet(wallet)" class="text-indigo-600 hover:text-indigo-900">
                      Edit
                    </button>
                    <button @click="toggleStatus(wallet)" 
                            :class="wallet.isActive ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'">
                      {{ wallet.isActive ? 'Deactivate' : 'Activate' }}
                    </button>
                    <button @click="deleteWallet(wallet)" class="text-red-600 hover:text-red-900">
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="filteredWallets.length === 0" class="text-center py-8">
          <p class="text-gray-500">No wallets found matching your criteria</p>
        </div>
      </div>

      <!-- Grouped View -->
      <div v-else class="space-y-6">
        <div v-for="(wallets, groupName) in filteredGroupedWallets" :key="groupName" 
             class="bg-white rounded-lg shadow">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-medium text-gray-900">
              {{ groupName }} ({{ wallets.length }} wallets)
            </h3>
          </div>
          
          <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="wallet in wallets" :key="wallet.id" 
                   class="border border-gray-200 rounded-lg p-4 hover:border-gray-300">
                <div class="flex justify-between items-start mb-2">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="getWalletTypeClass(wallet.walletType)">
                    {{ getWalletTypeDisplay(wallet.walletType) }}
                  </span>
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                        :class="wallet.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                    {{ wallet.isActive ? 'Active' : 'Inactive' }}
                  </span>
                </div>
                
                <div class="mb-2">
                  <p class="text-sm text-gray-500">Address:</p>
                  <p class="text-sm text-gray-900 font-mono break-all">{{ wallet.walletAddress }}</p>
                </div>
                
                <div class="mb-3">
                  <p class="text-sm text-gray-500">Balance:</p>
                  <p class="text-sm text-gray-900 font-semibold">{{ formatBalance(wallet.balance) }}</p>
                </div>
                
                <div class="flex space-x-2">
                  <button @click="editWallet(wallet)" class="text-xs bg-gray-100 text-gray-700 px-2 py-1 rounded hover:bg-gray-200">
                    Edit
                  </button>
                  <button @click="toggleStatus(wallet)" 
                          :class="wallet.isActive ? 'text-xs bg-red-100 text-red-700 px-2 py-1 rounded hover:bg-red-200' : 'text-xs bg-green-100 text-green-700 px-2 py-1 rounded hover:bg-green-200'">
                    {{ wallet.isActive ? 'Deactivate' : 'Activate' }}
                  </button>
                  <button @click="deleteWallet(wallet)" class="text-xs bg-red-100 text-red-700 px-2 py-1 rounded hover:bg-red-200">
                    Delete
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="Object.keys(filteredGroupedWallets).length === 0" class="text-center py-8">
          <p class="text-gray-500">No wallet groups found matching your criteria</p>
        </div>
      </div>

      <!-- Add/Edit Modal -->
      <div v-if="showAddModal || showEditModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
          <h3 class="text-lg font-bold text-gray-900 mb-4">
            {{ showEditModal ? 'Edit Company Wallet' : 'Add Company Wallet' }}
          </h3>
          
          <form @submit.prevent="saveWallet" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Wallet Name *</label>
              <input
                v-model="walletForm.walletName"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg"
                placeholder="Enter wallet name"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Wallet Type *</label>
              <select
                v-model="walletForm.walletType"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg"
              >
                <option value="">Select wallet type</option>
                <option v-for="type in walletTypes" :key="type.value" :value="type.value">
                  {{ type.label }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Address *</label>
              <input
                v-model="walletForm.address"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg font-mono text-sm"
                placeholder="Enter wallet address"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Balance</label>
              <input
                v-model="walletForm.balance"
                type="number"
                step="0.00000001"
                min="0"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg"
                placeholder="0.00000000"
              />
            </div>

            <div class="flex items-center">
              <input
                v-model="walletForm.isActive"
                type="checkbox"
                id="isActive"
                class="h-4 w-4 text-blue-600 border-gray-300 rounded"
              />
              <label for="isActive" class="ml-2 block text-sm text-gray-900">
                Active
              </label>
            </div>

            <div class="flex justify-end space-x-3 pt-2">
              <button type="button" @click="closeModal" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
                Cancel
              </button>
              <button type="submit" :disabled="isSubmitting" class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50">
                {{ isSubmitting ? (showEditModal ? 'Updating...' : 'Creating...') : (showEditModal ? 'Update' : 'Create') }}
              </button>
            </div>
          </form>
          
          <p v-if="errorMessage" class="mt-4 text-sm text-red-600">{{ errorMessage }}</p>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { 
  getCompanyWallets, 
  getCompanyWalletStatistics,
  getCompanyWalletsGrouped,
  getWalletTypes,
  createCompanyWallet, 
  updateCompanyWallet, 
  deleteCompanyWallet, 
  toggleCompanyWalletStatus
} from '../../utils/api.js'

const wallets = ref([])
const groupedWallets = ref({})
const statistics = ref({})
const walletTypes = ref([])
const isLoading = ref(false)
const isSubmitting = ref(false)
const showAddModal = ref(false)
const showEditModal = ref(false)
const editingWallet = ref(null)
const errorMessage = ref('')

// Filters
const selectedWalletType = ref('')
const selectedStatus = ref('')
const searchQuery = ref('')
const viewMode = ref('list')

const walletForm = ref({
  walletName: '',
  walletType: '',
  address: '',
  balance: 0,
  isActive: true
})

// Computed properties
const filteredWallets = computed(() => {
  let filtered = [...wallets.value]
  
  if (selectedWalletType.value) {
    filtered = filtered.filter(w => w.walletType === selectedWalletType.value)
  }
  
  if (selectedStatus.value) {
    const isActive = selectedStatus.value === 'active'
    filtered = filtered.filter(w => w.isActive === isActive)
  }
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(w => 
      w.walletName.toLowerCase().includes(query) ||
      w.address.toLowerCase().includes(query)
    )
  }
  
  return filtered
})

const filteredGroupedWallets = computed(() => {
  const filtered = {}
  for (const [groupName, groupWallets] of Object.entries(groupedWallets.value)) {
    const filteredGroupWallets = groupWallets.filter(w => {
      let match = true
      
      if (selectedWalletType.value) {
        match = match && w.walletType === selectedWalletType.value
      }
      
      if (selectedStatus.value) {
        const isActive = selectedStatus.value === 'active'
        match = match && w.isActive === isActive
      }
      
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        match = match && (
          w.walletName.toLowerCase().includes(query) ||
          w.address.toLowerCase().includes(query)
        )
      }
      
      return match
    })
    
    if (filteredGroupWallets.length > 0) {
      filtered[groupName] = filteredGroupWallets
    }
  }
  return filtered
})

// Methods
const loadData = async () => {
  isLoading.value = true
  try {
    const [walletsResponse, groupedResponse, statsResponse, typesResponse] = await Promise.all([
      getCompanyWallets(),
      getCompanyWalletsGrouped(),
      getCompanyWalletStatistics(),
      getWalletTypes()
    ])
    
    if (walletsResponse.success) {
      wallets.value = walletsResponse.data || []
    }
    
    if (groupedResponse.success) {
      groupedWallets.value = groupedResponse.data || {}
    }
    
    if (statsResponse.success) {
      statistics.value = statsResponse.data || {}
    }
    
    if (typesResponse.success) {
      walletTypes.value = typesResponse.data || []
    }
  } catch (error) {
    console.error('Failed to load data:', error)
    errorMessage.value = 'Failed to load wallet data: ' + error.message
  } finally {
    isLoading.value = false
  }
}

const filterWallets = () => {
  // Filters are reactive, computed properties will update automatically
}

const saveWallet = async () => {
  if (!walletForm.value.walletName || !walletForm.value.walletType || !walletForm.value.address) {
    errorMessage.value = 'Wallet name, type, and address are required.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''
  
  try {
    const walletData = {
      walletName: walletForm.value.walletName,
      walletType: walletForm.value.walletType,
      address: walletForm.value.address,
      balance: parseFloat(walletForm.value.balance) || 0,
      isActive: walletForm.value.isActive
    }

    let response
    if (showEditModal.value) {
      response = await updateCompanyWallet(editingWallet.value.id, walletData)
    } else {
      response = await createCompanyWallet(walletData)
    }
    
    if (response.success) {
      await loadData()
      closeModal()
      alert(`Company wallet ${showEditModal.value ? 'updated' : 'created'} successfully!`)
    } else {
      errorMessage.value = response.error || `Failed to ${showEditModal.value ? 'update' : 'create'} wallet.`
    }
  } catch (error) {
    console.error('Failed to save wallet:', error)
    errorMessage.value = `Failed to ${showEditModal.value ? 'update' : 'create'} wallet: ` + error.message
  } finally {
    isSubmitting.value = false
  }
}

const editWallet = (wallet) => {
  editingWallet.value = wallet
  walletForm.value = {
    walletName: wallet.walletName,
    walletType: wallet.walletType,
    address: wallet.walletAddress, // Fixed: use walletAddress instead of address
    balance: wallet.balance,
    isActive: wallet.isActive
  }
  showEditModal.value = true
}

const toggleStatus = async (wallet) => {
  try {
    const response = await toggleCompanyWalletStatus(wallet.id)
    if (response.success) {
      await loadData()
      alert(`Wallet ${response.data.isActive ? 'activated' : 'deactivated'} successfully!`)
    } else {
      alert('Failed to update wallet status: ' + (response.error || 'Unknown error'))
    }
  } catch (error) {
    console.error('Failed to toggle wallet status:', error)
    alert('Failed to update wallet status: ' + error.message)
  }
}

const deleteWallet = async (wallet) => {
  if (!confirm(`Are you sure you want to delete wallet "${wallet.walletName}" (${wallet.walletType})?`)) {
    return
  }

  try {
    const response = await deleteCompanyWallet(wallet.id)
    if (response.success) {
      await loadData()
      alert('Company wallet deleted successfully!')
    } else {
      alert('Failed to delete wallet: ' + (response.error || 'Unknown error'))
    }
  } catch (error) {
    console.error('Failed to delete wallet:', error)
    alert('Failed to delete wallet: ' + error.message)
  }
}

const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  editingWallet.value = null
  errorMessage.value = ''
  walletForm.value = {
    walletName: '',
    walletType: '',
    address: '',
    balance: 0,
    isActive: true
  }
}

const getWalletTypeClass = (walletType) => {
  const classes = {
    'POL': 'bg-purple-100 text-purple-800',
    'TRX': 'bg-red-100 text-red-800',
    'SOL': 'bg-green-100 text-green-800',
    'BSC': 'bg-yellow-100 text-yellow-800',
    'BTC': 'bg-orange-100 text-orange-800',
    'ETH': 'bg-blue-100 text-blue-800',
    'ADA': 'bg-indigo-100 text-indigo-800',
    'AVAX': 'bg-pink-100 text-pink-800',
    'DOT': 'bg-gray-100 text-gray-800',
    'LINK': 'bg-teal-100 text-teal-800'
  }
  return classes[walletType] || 'bg-gray-100 text-gray-800'
}

const getWalletTypeDisplay = (walletType) => {
  const type = walletTypes.value.find(t => t.code === walletType)
  return type ? type.displayName : walletType
}

const formatBalance = (balance) => {
  return new Intl.NumberFormat('en-US', { 
    minimumFractionDigits: 8,
    maximumFractionDigits: 8
  }).format(balance || 0)
}

onMounted(() => {
  loadData()
})
</script>