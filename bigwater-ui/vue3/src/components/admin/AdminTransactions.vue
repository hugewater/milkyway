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
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">User ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">User Email</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">From Wallet ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">To Wallet ID</th>
                <th @click="toggleSort('amount_usdt')" class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer">Amount</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Transaction Hash</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                <th @click="toggleSort('created_at')" class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer">Created</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="t in items" :key="t.id">
                <td class="px-4 py-2 text-sm text-gray-900">{{ t.id }}</td>
                <td class="px-4 py-2 text-sm font-mono">{{ t.userId || '-' }}</td>
                <td class="px-4 py-2 text-sm text-gray-700 max-w-32">
                  <span v-if="t.userEmail" class="truncate block" :title="t.userEmail">{{ truncateText(t.userEmail, 25) }}</span>
                  <span v-else class="text-gray-400">-</span>
                </td>
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
                <td class="px-4 py-2 text-sm font-mono text-xs max-w-32">
                  <span v-if="t.transactionHash" class="text-blue-600 truncate block" :title="t.transactionHash">{{ truncateText(t.transactionHash, 20) }}</span>
                  <span v-else class="text-gray-400">-</span>
                </td>
                <td class="px-4 py-2 text-sm max-w-40">
                  <input v-model="t.description" @blur="update(t)" class="border rounded px-2 py-1 text-xs w-full" :title="t.description" />
                </td>
                <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(t.createdAt) }}</td>
                <td class="px-4 py-2 text-sm space-x-3">
                  <button @click="view(t)" class="text-ocean hover:text-deep-ocean text-sm">View</button>
                  <button @click="verify(t)" class="text-green-600 hover:text-green-700 text-sm">Verify</button>
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
        <button @click="forceVerify" 
                :disabled="isVerifying"
                class="px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 disabled:bg-gray-400 disabled:cursor-not-allowed">
          <span v-if="isVerifying">Processing...</span>
          <span v-else>Force to Verify</span>
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
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import apiService from '../../utils/api.js'
import { USDT_CONTRACTS } from '../../config/contractAddresses.js'

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
const truncateText = (text, maxLength = 20) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

// Contract addresses are now imported from centralized configuration

onMounted(load)

// View modal state
const showView = ref(false)
const viewing = ref(null)
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
  // For transactions, fromWalletAddress should be the user's address, toWalletAddress should be the company's address
  // Determine chain type from transaction data or default to POL
  const chain = t.walletType || 'POL' // Use transaction's wallet type or default to Polygon
  const contractAddress = USDT_CONTRACTS[chain] || ''
  
  console.log('Admin verify - Transaction:', t)
  console.log('Admin verify - Chain:', chain)
  console.log('Admin verify - Contract Address:', contractAddress)
  console.log('Admin verify - USDT_CONTRACTS:', USDT_CONTRACTS)
  console.log('Admin verify - TTT specific:', USDT_CONTRACTS['TTT'])
  console.log('Admin verify - TTT direct access:', USDT_CONTRACTS.TTT)
  console.log('Admin verify - All keys:', Object.keys(USDT_CONTRACTS))
  
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
    contractAddress: '',
    transactionId: null
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
      contractAddress: verifyForm.value.contractAddress,
      transactionId: verifyForm.value.transactionId
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
      
      // If verification is successful, auto-fill txHash and update transaction
      if (response.verified && response.details?.txHash) {
        // Auto-fill the transaction hash in the form
        verifyForm.value.txHash = response.details.txHash
        
        // Update the transaction status to COMPLETED
        try {
          await updateTransactionStatus(verifyForm.value.transactionId, 'COMPLETED')
          
          // Refresh the transaction list to show updated status
          await load()
          
          // Show success message
          verificationResult.value.message = 'Transaction verified and status updated to COMPLETED!'
        } catch (updateError) {
          console.error('Failed to update transaction status:', updateError)
          verificationResult.value.message = 'Transaction verified but failed to update status: ' + updateError.message
        }
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

const forceVerify = async () => {
  if (!verifyForm.value.transactionId) {
    alert('No transaction ID found. Please select a transaction first.')
    return
  }
  
  isVerifying.value = true
  verificationResult.value = null
  
  try {
    // Directly update the transaction status to COMPLETED without verification
    await updateTransactionStatus(verifyForm.value.transactionId, 'COMPLETED')
    
    // Refresh the transaction list to show updated status
    await load()
    
    // Show success message
    verificationResult.value = {
      verified: true,
      message: 'Transaction force verified and status updated to COMPLETED! (External wallet verification)',
      details: {
        method: 'Force Verify',
        bypassed: true
      }
    }
    
    // Close the modal after a short delay to show the success message
    setTimeout(() => {
      closeVerify()
    }, 2000)
    
  } catch (error) {
    verificationResult.value = {
      verified: false,
      message: 'Failed to force verify transaction: ' + (error?.message || 'Unknown error')
    }
  } finally {
    isVerifying.value = false
  }
}
</script>

