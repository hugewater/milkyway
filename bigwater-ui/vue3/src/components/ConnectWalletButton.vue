<template>
  <div class="relative">
    <div class="flex items-center space-x-3">
      <div v-if="isConnected" class="flex items-center px-3 py-1.5 bg-gray-100 rounded-lg text-gray-700 text-sm">
        <img v-if="walletState.type" :src="walletIcon" :alt="walletState.type" class="w-5 h-5 mr-2" />
        <span>{{ shortenedAddress }}</span>
        <button @click="disconnectWallet" class="ml-2 text-gray-400 hover:text-gray-600" title="Disconnect">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
        </button>
      </div>
      <button
        @click="showModal = true"
        class="btn-primary px-4 py-2 rounded-lg flex items-center space-x-2"
        :disabled="isConnecting"
        :class="{ 'opacity-75 cursor-not-allowed': isConnecting }"
      >
        <template v-if="isConnecting">
          <span class="loading-spinner mr-2"></span>
          Connecting...
        </template>
        <template v-else>
          Connect Wallet
        </template>
      </button>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-lg font-bold text-deep-ocean">Connect Wallet</h3>
          <button @click="closeModal" class="text-gray-500 hover:text-gray-700" type="button">✕</button>
        </div>

        <!-- Error Block -->
        <div v-if="showError" class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg">
          <p v-if="walletErrors.notInstalled">Please install the selected wallet extension first.</p>
          <p v-else-if="walletErrors.connectionError">{{ walletErrors.connectionError }}</p>
          <p v-else>{{ error }}</p>
        </div>

        <div class="space-y-4">
          <button
            v-for="wallet in wallets"
            :key="wallet.type"
            @click="connectToWallet(wallet.type)"
            :disabled="isConnecting"
            :class="[
              'w-full p-4 border rounded-lg flex items-center justify-between transition-colors',
              isConnecting ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'
            ]"
          >
            <div class="flex items-center space-x-3">
              <img :src="wallet.icon" :alt="wallet.name" class="w-8 h-8" />
              <span class="font-medium">{{ wallet.name }}</span>
            </div>
            <span v-if="walletState.type === wallet.type" class="text-green-500">Connected</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { connectWallet, disconnectWallet, walletState, walletErrors, WALLET_STATES, shortenAddress } from '../utils/walletConnect'

// Modal state
const showModal = ref(false)

// Wallet options
const wallets = [
  {
    name: 'MetaMask',
    type: 'metamask',
    icon: '/icons/metamask.svg'
  },
  {
    name: 'TronLink',
    type: 'tronlink',
    icon: '/icons/tronlink.svg'
  },
  {
    name: 'Trust Wallet',
    type: 'trust',
    icon: '/icons/trust.svg'
  },
  {
    name: 'SafePal',
    type: 'safepal',
    icon: '/icons/safepal.svg'
  }
]

// Computed properties
const isConnected = computed(() => walletState.status === WALLET_STATES.CONNECTED)
const isConnecting = computed(() => walletState.status === WALLET_STATES.CONNECTING)
const error = computed(() => walletState.error)
const shortenedAddress = computed(() => shortenAddress(walletState.address))
const walletIcon = computed(() => {
  const t = walletState.type
  if (!t) return '/icons/metamask.svg'
  const known = ['metamask','tronlink','trust','safepal']
  return known.includes(t) ? `/icons/${t}.svg` : '/icons/metamask.svg'
})

const showError = computed(() => !!error.value || walletErrors.notInstalled || !!walletErrors.connectionError)

async function connectToWallet(walletType) {
  try {
    await connectWallet(walletType)
    showModal.value = false
  } catch (err) {
    console.error('Failed to connect wallet:', err)
  }
}

// Disconnect helper (could be referenced by legacy code)
function handleDisconnect() { disconnectWallet() }

function closeModal() {
  showModal.value = false
  walletErrors.notInstalled = false
  walletErrors.wrongNetwork = false
  walletErrors.connectionError = ''
}
</script>

<style scoped>
.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.wallet-item {
  transition: all 0.2s ease-in-out;
}

.wallet-item:hover:not(:disabled) {
  background-color: #f8fafc;
  transform: translateY(-1px);
}

.error-modal {
  animation: fadeIn 0.2s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>
