<template>
  <div class="p-6">
    <div class="max-w-6xl mx-auto">
      <!-- Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">钱包管理 (新版本)</h1>
        <p class="text-gray-600">每个用户一个钱包，支持TRON和POLYGON地址</p>
      </div>

      <!-- User Wallet Search -->
      <div class="bg-white rounded-lg shadow mb-6 p-6">
        <h2 class="text-xl font-semibold mb-4">查看用户钱包</h2>
        <div class="flex gap-4 items-end">
          <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-2">用户ID</label>
            <input
              v-model="searchUserId"
              type="number"
              placeholder="输入用户ID"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <button
            @click="loadUserWallet"
            :disabled="!searchUserId || loading"
            class="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
          >
            {{ loading ? '加载中...' : '查看钱包' }}
          </button>
        </div>
      </div>

      <!-- User Wallet Display -->
      <div v-if="userWallet" class="bg-white rounded-lg shadow mb-6 p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">用户钱包信息</h2>
          <span class="text-sm text-gray-500">用户ID: {{ userWallet.userId }}</span>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Wallet Info -->
          <div>
            <h3 class="text-lg font-medium mb-3">基本信息</h3>
            <div class="space-y-2">
              <div>
                <span class="text-gray-600">钱包名称:</span>
                <span class="ml-2 font-medium">{{ userWallet.walletName }}</span>
              </div>
              <div>
                <span class="text-gray-600">余额:</span>
                <span class="ml-2 font-medium">{{ userWallet.balance }} USDT</span>
              </div>
              <div>
                <span class="text-gray-600">状态:</span>
                <span :class="userWallet.isActive ? 'text-green-600' : 'text-red-600'" class="ml-2 font-medium">
                  {{ userWallet.isActive ? '活跃' : '未激活' }}
                </span>
              </div>
              <div>
                <span class="text-gray-600">创建时间:</span>
                <span class="ml-2">{{ formatDate(userWallet.createdAt) }}</span>
              </div>
            </div>
          </div>

          <!-- Addresses -->
          <div>
            <h3 class="text-lg font-medium mb-3">钱包地址</h3>
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">TRON地址</label>
                <div class="flex gap-2">
                  <input
                    v-model="editAddresses.tron"
                    :placeholder="userWallet.tronAddress || '未设置'"
                    class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <button
                    @click="updateAddress('tron')"
                    :disabled="updating"
                    class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50"
                  >
                    更新
                  </button>
                </div>
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">POLYGON地址</label>
                <div class="flex gap-2">
                  <input
                    v-model="editAddresses.polygon"
                    :placeholder="userWallet.polygonAddress || '未设置'"
                    class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <button
                    @click="updateAddress('polygon')"
                    :disabled="updating"
                    class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50"
                  >
                    更新
                  </button>
                </div>
              </div>

              <button
                @click="updateBothAddresses"
                :disabled="updating"
                class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
              >
                {{ updating ? '更新中...' : '同时更新两个地址' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Create Wallet -->
      <div v-if="searchUserId && !userWallet && !loading" class="bg-white rounded-lg shadow mb-6 p-6">
        <h2 class="text-xl font-semibold mb-4">创建用户钱包</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">钱包名称</label>
            <input
              v-model="newWallet.walletName"
              placeholder="Primary Wallet"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">TRON地址</label>
            <input
              v-model="newWallet.tronAddress"
              placeholder="TRC20地址"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-2">POLYGON地址</label>
            <input
              v-model="newWallet.polygonAddress"
              placeholder="POLYGON地址"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div class="md:col-span-2">
            <button
              @click="createWallet"
              :disabled="creating"
              class="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50"
            >
              {{ creating ? '创建中...' : '创建钱包' }}
            </button>
          </div>
        </div>
      </div>

      <!-- All Wallets -->
      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">所有钱包</h2>
          <button
            @click="loadAllWallets"
            :disabled="loadingAll"
            class="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 disabled:opacity-50"
          >
            {{ loadingAll ? '加载中...' : '刷新列表' }}
          </button>
        </div>
        
        <div v-if="allWallets.length === 0" class="text-center text-gray-500 py-8">
          暂无钱包数据
        </div>
        
        <div v-else class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">用户ID</th>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">钱包名称</th>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">TRON地址</th>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">POLYGON地址</th>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">余额</th>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">状态</th>
                <th class="px-4 py-2 text-left text-sm font-medium text-gray-500">创建时间</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr v-for="wallet in allWallets" :key="wallet.id" class="hover:bg-gray-50">
                <td class="px-4 py-2 text-sm">{{ wallet.userId }}</td>
                <td class="px-4 py-2 text-sm">{{ wallet.walletName }}</td>
                <td class="px-4 py-2 text-sm">
                  <span v-if="wallet.tronAddress" class="text-blue-600">{{ truncateAddress(wallet.tronAddress) }}</span>
                  <span v-else class="text-gray-400">未设置</span>
                </td>
                <td class="px-4 py-2 text-sm">
                  <span v-if="wallet.polygonAddress" class="text-purple-600">{{ truncateAddress(wallet.polygonAddress) }}</span>
                  <span v-else class="text-gray-400">未设置</span>
                </td>
                <td class="px-4 py-2 text-sm">{{ wallet.balance }}</td>
                <td class="px-4 py-2 text-sm">
                  <span :class="wallet.isActive ? 'text-green-600' : 'text-red-600'">
                    {{ wallet.isActive ? '活跃' : '未激活' }}
                  </span>
                </td>
                <td class="px-4 py-2 text-sm">{{ formatDate(wallet.createdAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Messages -->
      <div v-if="message" class="fixed bottom-4 right-4 max-w-sm">
        <div :class="messageType === 'error' ? 'bg-red-500' : 'bg-green-500'" class="text-white p-4 rounded-lg shadow-lg">
          {{ message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { getUserWallet, createUserWallet, updateWalletAddresses, updateWalletAddress, getAllNewWallets } from '../../utils/api.js'

// State
const searchUserId = ref('')
const userWallet = ref(null)
const loading = ref(false)
const updating = ref(false)
const creating = ref(false)
const loadingAll = ref(false)
const allWallets = ref([])
const message = ref('')
const messageType = ref('success')

const editAddresses = reactive({
  tron: '',
  polygon: ''
})

const newWallet = reactive({
  walletName: 'Primary Wallet',
  tronAddress: '',
  polygonAddress: ''
})

// Methods
const showMessage = (msg, type = 'success') => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 3000)
}

const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleString('zh-CN')
}

const truncateAddress = (address) => {
  if (!address) return ''
  if (address.length <= 16) return address
  return `${address.substring(0, 8)}...${address.substring(address.length - 8)}`
}

const loadUserWallet = async () => {
  if (!searchUserId.value) return
  
  loading.value = true
  try {
    const response = await getUserWallet(searchUserId.value)
    if (response.success) {
      userWallet.value = response.data
      editAddresses.tron = response.data.tronAddress || ''
      editAddresses.polygon = response.data.polygonAddress || ''
      showMessage('成功加载用户钱包')
    } else {
      userWallet.value = null
      showMessage(response.error || '用户钱包不存在', 'error')
    }
  } catch (error) {
    showMessage('加载用户钱包失败: ' + error.message, 'error')
    userWallet.value = null
  } finally {
    loading.value = false
  }
}

const createWallet = async () => {
  if (!searchUserId.value) return
  
  creating.value = true
  try {
    const walletData = {
      walletName: newWallet.walletName || 'Primary Wallet',
      tronAddress: newWallet.tronAddress || null,
      polygonAddress: newWallet.polygonAddress || null
    }
    
    const response = await createUserWallet(searchUserId.value, walletData)
    if (response.success) {
      userWallet.value = response.data
      editAddresses.tron = response.data.tronAddress || ''
      editAddresses.polygon = response.data.polygonAddress || ''
      showMessage('成功创建用户钱包')
      // Reset form
      newWallet.walletName = 'Primary Wallet'
      newWallet.tronAddress = ''
      newWallet.polygonAddress = ''
    } else {
      showMessage(response.error || '创建钱包失败', 'error')
    }
  } catch (error) {
    showMessage('创建钱包失败: ' + error.message, 'error')
  } finally {
    creating.value = false
  }
}

const updateAddress = async (network) => {
  if (!searchUserId.value) return
  
  updating.value = true
  try {
    const address = network === 'tron' ? editAddresses.tron : editAddresses.polygon
    const response = await updateWalletAddress(searchUserId.value, network, address)
    if (response.success) {
      userWallet.value = response.data
      showMessage(`成功更新${network.toUpperCase()}地址`)
    } else {
      showMessage(response.error || `更新${network.toUpperCase()}地址失败`, 'error')
    }
  } catch (error) {
    showMessage(`更新${network.toUpperCase()}地址失败: ` + error.message, 'error')
  } finally {
    updating.value = false
  }
}

const updateBothAddresses = async () => {
  if (!searchUserId.value) return
  
  updating.value = true
  try {
    const addresses = {
      tronAddress: editAddresses.tron || null,
      polygonAddress: editAddresses.polygon || null
    }
    
    const response = await updateWalletAddresses(searchUserId.value, addresses)
    if (response.success) {
      userWallet.value = response.data
      showMessage('成功更新钱包地址')
    } else {
      showMessage(response.error || '更新钱包地址失败', 'error')
    }
  } catch (error) {
    showMessage('更新钱包地址失败: ' + error.message, 'error')
  } finally {
    updating.value = false
  }
}

const loadAllWallets = async () => {
  loadingAll.value = true
  try {
    const response = await getAllNewWallets()
    if (response.success) {
      allWallets.value = response.data || []
      showMessage(`加载了 ${allWallets.value.length} 个钱包`)
    } else {
      showMessage(response.error || '加载钱包列表失败', 'error')
    }
  } catch (error) {
    showMessage('加载钱包列表失败: ' + error.message, 'error')
  } finally {
    loadingAll.value = false
  }
}

// Load all wallets on mount
loadAllWallets()
</script>
