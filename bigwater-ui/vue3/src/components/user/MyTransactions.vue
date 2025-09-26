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
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Transaction Hash</th>
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
                <td class="px-4 py-2 text-sm font-mono text-xs max-w-32">
                  <span v-if="t.transactionHash" class="text-blue-600 truncate block" :title="t.transactionHash">{{ truncateText(t.transactionHash, 20) }}</span>
                  <span v-else class="text-gray-400">-</span>
                </td>
                <td class="px-4 py-2 text-sm max-w-40">
                  <span class="truncate block" :title="t.description">{{ truncateText(t.description, 30) }}</span>
                </td>
                <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(t.createdAt) }}</td>
                <td class="px-4 py-2 text-sm space-x-3">
                  <button @click="view(t)" class="text-ocean hover:text-deep-ocean text-sm">View</button>
                  <button @click="verify(t)" class="text-green-600 hover:text-green-700 text-sm">Verify</button>
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
        <div class="flex justify-between"><span class="text-gray-600">Transaction Hash</span><span class="font-mono text-xs">{{ viewing?.transactionHash || '-' }}</span></div>
        <div><span class="text-gray-600">Description</span><div class="mt-1 p-2 border rounded text-xs whitespace-pre-wrap">{{ viewing?.description }}</div></div>
        <div class="flex justify-between"><span class="text-gray-600">Created</span><span>{{ formatDate(viewing?.createdAt) }}</span></div>
      </div>
      <div class="flex justify-end mt-6">
        <button @click="showView = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
      </div>
    </div>
  </div>

  <!-- Verify Modal -->
  <div v-if="showVerify" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-2xl">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Verify Transaction</h3>
      <div class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">From Address</label>
            <input 
              v-model="verifyForm.fromAddress" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono text-sm"
              placeholder="0x..."
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">To Address</label>
            <input 
              v-model="verifyForm.toAddress" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono text-sm"
              placeholder="0x..."
            />
          </div>
        </div>
        
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Amount</label>
            <input 
              v-model="verifyForm.amount" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
              placeholder="0.00"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Chain</label>
            <select 
              v-model="verifyForm.chain" 
              @change="updateContractAddress"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
            >
              <option value="POL">Polygon (POL)</option>
              <option value="TRX">TRON (TRX)</option>
              <option value="ACT">ACT Token (Polygon Amoy)</option>
              <option value="TTT">TTT Token (TRON Nile)</option>
              <option value="ETH">Ethereum (ETH)</option>
              <option value="BSC">Binance Smart Chain (BSC)</option>
            </select>
          </div>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Transaction Hash</label>
          <input 
            v-model="verifyForm.txHash" 
            type="text" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono text-sm"
            placeholder="0x..."
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Contract Address</label>
          <input 
            v-model="verifyForm.contractAddress" 
            type="text" 
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono text-sm"
            placeholder="0x..."
          />
          <p class="text-xs text-gray-500 mt-1">USDT contract address for the selected chain</p>
        </div>
        
        <!-- Verification Result -->
        <div v-if="verificationResult" class="mt-4 p-4 rounded-lg" 
             :class="verificationResult.verified ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'">
          <div class="flex items-center">
            <span v-if="verificationResult.verified" class="text-green-600 text-sm">✅</span>
            <span v-else class="text-red-600 text-sm">❌</span>
            <span class="ml-2 text-sm font-medium" 
                  :class="verificationResult.verified ? 'text-green-800' : 'text-red-800'">
              {{ verificationResult.message }}
            </span>
          </div>
          <div v-if="verificationResult.details" class="mt-2 text-xs text-gray-600">
            <p v-if="verificationResult.details.contract">Contract: {{ verificationResult.details.contract }}</p>
            <p v-if="verificationResult.details.chain">Chain: {{ verificationResult.details.chain }}</p>
          </div>
        </div>
      </div>
      
      <div class="flex justify-end space-x-3 mt-6">
        <button @click="closeVerify" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
          Cancel
        </button>
        <button @click="performVerification" 
                :disabled="isVerifying || !verifyForm.fromAddress || !verifyForm.toAddress || !verifyForm.amount"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed">
          <span v-if="isVerifying">Verifying...</span>
          <span v-else>Verify Transaction</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { getUserId } from '../../utils/auth.js'
import apiService from '../../utils/api.js'
import { USDT_CONTRACTS } from '../../config/contractAddresses.js'

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
        transactionHash: (r.transactionHash !== undefined && r.transactionHash !== null) ? r.transactionHash : (r.transaction_hash !== undefined ? r.transaction_hash : null),
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
const truncateText = (text, maxLength = 20) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
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

// Contract addresses are now imported from centralized configuration

const view = (t) => { viewing.value = t; showView.value = true }

// Verify modal state
const showVerify = ref(false)
const isVerifying = ref(false)
const verificationResult = ref(null)
const verifyForm = ref({
  fromAddress: '',
  toAddress: '',
  amount: '',
  chain: 'POL',
  txHash: '',
  contractAddress: '',
  transactionId: null
})

const verify = (t) => {
  // Pre-fill form with transaction data
  // Determine chain type from transaction data or default to POL
  const chain = t.walletType || 'POL' // Use transaction's wallet type or default to Polygon
  const contractAddress = USDT_CONTRACTS[chain] || ''
  
  console.log('User verify - Transaction:', t)
  console.log('User verify - Chain:', chain)
  console.log('User verify - Contract Address:', contractAddress)
  console.log('User verify - USDT_CONTRACTS:', USDT_CONTRACTS)
  console.log('User verify - TTT specific:', USDT_CONTRACTS['TTT'])
  console.log('User verify - TTT direct access:', USDT_CONTRACTS.TTT)
  console.log('User verify - All keys:', Object.keys(USDT_CONTRACTS))
  
  verifyForm.value = {
    fromAddress: t.fromWalletAddress || '',
    toAddress: t.toWalletAddress || '',
    amount: t.amountUsdt || '',
    chain: chain,
    txHash: t.transactionHash || '',
    contractAddress: contractAddress, // Auto-fill based on wallet type
    transactionId: t.id // Include transaction ID for status update
  }
  verificationResult.value = null
  showVerify.value = true
}

const updateContractAddress = () => {
  // Update contract address when chain selection changes
  const contractAddress = USDT_CONTRACTS[verifyForm.value.chain] || ''
  verifyForm.value.contractAddress = contractAddress
}

const closeVerify = () => {
  showVerify.value = false
  verificationResult.value = null
  verifyForm.value = {
    fromAddress: '',
    toAddress: '',
    amount: '',
    chain: 'POL',
    txHash: '',
    contractAddress: ''
  }
}

const updateTransactionStatus = async (transactionId, status) => {
  try {
    const response = await apiService.request(`/transactions/${transactionId}`, {
      method: 'PUT',
      body: JSON.stringify({ status: status })
    })
    
    if (!response || !response.success) {
      throw new Error(response?.error || 'Failed to update transaction status')
    }
    
    return response
  } catch (error) {
    console.error('Error updating transaction status:', error)
    throw error
  }
}

const performVerification = async () => {
  if (!verifyForm.value.fromAddress || !verifyForm.value.toAddress || !verifyForm.value.amount || !verifyForm.value.contractAddress) {
    return
  }
  
  isVerifying.value = true
  verificationResult.value = null
  
  try {
    const payload = {
      txHash: verifyForm.value.txHash,
      fromAddress: verifyForm.value.fromAddress,
      toAddress: verifyForm.value.toAddress,
      amount: verifyForm.value.amount,
      chain: verifyForm.value.chain,
      contractAddress: verifyForm.value.contractAddress
    }
    
    const response = await apiService.request('/transactions/verify', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
    
    if (response && response.success !== undefined) {
      verificationResult.value = {
        verified: response.verified,
        message: response.message,
        details: response.details
      }
    } else {
      verificationResult.value = {
        verified: false,
        message: response?.error || 'Verification failed'
      }
    }
  } catch (error) {
    verificationResult.value = {
      verified: false,
      message: error?.message || 'Verification failed'
    }
  } finally {
    isVerifying.value = false
  }
}

onMounted(load)
</script>

