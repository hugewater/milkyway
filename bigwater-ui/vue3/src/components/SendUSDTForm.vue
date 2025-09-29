<template>
  <div class="border rounded-lg p-4 space-y-4 bg-white shadow-sm">
    <h3 class="text-lg font-semibold text-gray-800">Send USDT to Company Wallet</h3>

    <div v-if="!isConnected" class="p-3 bg-yellow-50 text-yellow-800 rounded text-sm">
      Connect a supported EVM wallet first.
    </div>

    <form @submit.prevent="submit" class="space-y-3" v-else>
      <div>
        <label class="block text-sm font-medium text-gray-600 mb-1">From</label>
        <div class="px-3 py-2 bg-gray-100 rounded text-sm font-mono break-all">{{ walletState.address }}</div>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-600 mb-1">Company Wallet (Destination)</label>
        <input v-model="destination" type="text" class="input-text w-full" placeholder="0x..." required />
      </div>

      <div class="flex items-end space-x-3">
        <div class="flex-1">
          <label class="block text-sm font-medium text-gray-600 mb-1">Amount (USDT)</label>
          <input v-model="amount" type="text" inputmode="decimal" class="input-text w-full" placeholder="0.0" required />
        </div>
        <button type="button" @click="fillMax" class="text-xs text-indigo-600 hover:underline" :disabled="loading">MAX</button>
      </div>

      <div class="text-xs text-gray-500" v-if="balance !== null">
        Balance: <span class="font-mono">{{ balance }}</span> USDT
      </div>

      <div v-if="error" class="p-2 bg-red-100 text-red-700 text-sm rounded">{{ error }}</div>
      <div v-if="txHash && !confirmed" class="p-2 bg-blue-50 text-blue-700 text-sm rounded">
        Pending: <a :href="explorerTxUrl" target="_blank" class="underline">{{ shortHash }}</a>
      </div>
      <div v-if="confirmed" class="p-2 bg-green-100 text-green-700 text-sm rounded">
        Confirmed: <a :href="explorerTxUrl" target="_blank" class="underline">{{ shortHash }}</a>
      </div>

      <div class="flex items-center space-x-2">
        <button type="submit" class="btn-primary px-4 py-2 rounded" :disabled="loading || !canSend">
          <span v-if="loading" class="loading-spinner mr-2"></span>
          {{ loading ? 'Sending...' : 'Send USDT' }}
        </button>
        <button v-if="txHash && !confirmed" type="button" @click="refreshStatus" class="text-sm text-indigo-600 hover:underline" :disabled="loading">Refresh</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { walletState, WALLET_STATES } from '../utils/walletConnect'
import { getCurrentChainId, getUsdtContractAddress, getErc20Balance, getErc20Decimals, formatUnits, sendUsdt } from '../utils/erc20'

// Props could include default destination, but hard-coded for now maybe pass in later
// You can change this to your actual company wallet address
const COMPANY_WALLET = import.meta.env.VITE_COMPANY_WALLET || ''

const destination = ref(COMPANY_WALLET)
const amount = ref('')
const balance = ref(null) // string formatted
const decimals = ref(6) // USDT standard
const loading = ref(false)
const error = ref('')
const txHash = ref('')
const confirmed = ref(false)
const currentChain = ref(null)

const isConnected = computed(() => walletState.status === WALLET_STATES.CONNECTED && !!walletState.address)
const canSend = computed(() => !!destination.value && !!amount.value && parseFloat(amount.value) > 0)
const shortHash = computed(() => txHash.value ? txHash.value.slice(0,10) + '...' + txHash.value.slice(-6) : '')
const explorerTxUrl = computed(() => {
  if (!txHash.value) return '#'
  // Basic chain routing (Polygon vs Ethereum)
  if (currentChain.value === '0x89') return `https://polygonscan.com/tx/${txHash.value}`
  if (currentChain.value === '0x1') return `https://etherscan.io/tx/${txHash.value}`
  return '#'
})

async function loadBalance() {
  try {
    if (!isConnected.value) return
    currentChain.value = await getCurrentChainId()
    const contract = getUsdtContractAddress(currentChain.value)
    if (!contract) return
    const dec = await getErc20Decimals(contract)
    decimals.value = dec
    const raw = await getErc20Balance(contract, walletState.address)
    balance.value = formatUnits(raw, dec).replace(/\.0+$/,'')
  } catch (e) {
    // silent
  }
}

function fillMax() {
  if (balance.value) {
    amount.value = balance.value
  }
}

async function submit() {
  if (!canSend.value) return
  error.value = ''
  txHash.value = ''
  confirmed.value = false
  loading.value = true
  try {
    const { txHash: hash } = await sendUsdt({
      to: destination.value,
      amount: amount.value,
      onPending: () => {},
      onSent: (h) => { txHash.value = h },
      onConfirmed: () => { confirmed.value = true }
    })
    txHash.value = hash
  } catch (e) {
    error.value = e.message || 'Transaction failed'
  } finally {
    loading.value = false
    loadBalance()
  }
}

async function refreshStatus() {
  if (!txHash.value) return
  // Simplistic re-check: just attempt to reload balance (receipt polling already in sendUsdt)
  loadBalance()
}

onMounted(() => {
  loadBalance()
  // Re-load when account changes (in a real app, use an event bus or watch walletState)
  const iv = setInterval(() => { loadBalance() }, 15000)
  // naive interval; replace with event-driven for production
  window.addEventListener('focus', loadBalance)
  // cleanup logic is optional in SFC setup return
})
</script>

<style scoped>
.input-text {
  border: 1px solid #d1d5db;
  border-radius: 4px;
  padding: 0.5rem 0.75rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  width: 100%;
  transition: border-color 0.15s, box-shadow 0.15s;
}
.input-text:focus {
  outline: none;
  border-color: #6366f1;
  box-shadow: 0 0 0 1px #6366f1, 0 0 0 2px rgba(99,102,241,0.3);
}
.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #6366f1;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }
</style>
