<template>
  <AppLayout>
    <div class="px-4 lg:px-6 py-6">
      <div class="flex justify-end mb-4">
        <button
          @click="showAddWalletModal = true"
          class="btn-primary px-4 py-2 rounded-lg"
        >
          Create Wallet
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
            <h3 class="mt-2 text-sm font-medium text-gray-900">No wallet found</h3>
            <p class="mt-1 text-sm text-gray-500">Contact administrator to create your wallet.</p>
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
                  <td class="px-4 py-2 text-sm font-mono" :class="!w.isActive ? 'text-red-600' : 'text-gray-900'" :title="w.walletAddress">{{ (w.walletAddress || '').slice(0, 16) }}{{ w.walletAddress ? '...' : '-' }}</td>
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
      <div class="bg-white rounded-lg p-6 w-full max-w-lg mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Create New Wallet</h3>
        
        <form @submit.prevent="addWallet">
          <div class="space-y-4">
            <div>
              <label for="walletName" class="block text-sm font-medium text-gray-700 mb-2">
                Wallet Name *
              </label>
              <input
                id="walletName"
                v-model="newWallet.walletName"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                placeholder="Enter wallet name"
              />
            </div>

            <div>
              <label for="walletType" class="block text-sm font-medium text-gray-700 mb-2">
                Wallet Type *
              </label>
              <select
                id="walletType"
                v-model="newWallet.walletType"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              >
                <option value="">Select wallet type</option>
                <option v-for="type in walletTypes" :key="type.value" :value="type.value">
                  {{ type.label }}
                </option>
              </select>
            </div>

            <div>
              <label for="walletAddress" class="block text-sm font-medium text-gray-700 mb-2">
                Wallet Address *
              </label>
              <input
                id="walletAddress"
                v-model="newWallet.walletAddress"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent font-mono text-sm"
                :placeholder="getAddressPlaceholder(newWallet.walletType)"
              />
            </div>

            <div>
              <label for="walletBalance" class="block text-sm font-medium text-gray-700 mb-2">
                Initial Balance (Optional)
              </label>
              <input
                id="walletBalance"
                v-model="newWallet.balance"
                type="number"
                step="0.00000001"
                min="0"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                placeholder="0.00"
              />
              <p class="text-xs text-gray-500 mt-1">Enter initial balance in USDT (optional)</p>
            </div>

            <div class="text-sm text-gray-500">
              <p>• Choose the blockchain network type for your wallet</p>
              <p>• Enter the wallet address for the selected network</p>
              <p>• Optionally set an initial balance</p>
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
              <span v-if="isAddingWallet">Creating...</span>
              <span v-else>Create Wallet</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  
  <!-- View Wallet Modal -->
  <div v-if="showView" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-lg">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Wallet Details</h3>
      <div class="space-y-3 text-sm">
        <div class="flex justify-between"><span class="text-gray-600">ID</span><span class="font-mono">{{ selected?.id }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Wallet Name</span><span>{{ selected?.walletName || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Wallet Type</span><span>{{ selected?.walletType || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Address</span><span class="font-mono text-xs break-all">{{ selected?.walletAddress || '-' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Balance</span><span>${{ Number(selected?.balance || 0).toFixed(2) }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Status</span><span>{{ selected?.isActive ? 'Active' : 'Inactive' }}</span></div>
        <div class="flex justify-between"><span class="text-gray-600">Created</span><span>{{ formatDate(selected?.createdAt || selected?.created_at) }}</span></div>
      </div>
      <div class="flex justify-end mt-6">
        <button @click="showView = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
      </div>
    </div>
  </div>

  <!-- Pay Modal -->
  <div v-if="showPay" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-bold text-deep-ocean">Make Payment</h3>
        <button @click="showPay = false" class="text-gray-400 hover:text-gray-600">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>

      <div v-if="isLoadingPayment" class="text-center py-8">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-ocean mx-auto"></div>
        <p class="mt-2 text-gray-500">Loading payment information...</p>
      </div>

      <div v-else-if="randomCompanyWallet" class="space-y-6">
        <!-- Company Wallet Information -->
        <div class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg p-4 border border-blue-200">
          <h4 class="text-md font-semibold text-blue-900 mb-3">{{ randomCompanyWallet.walletName }}</h4>
          
          <!-- Payment Option -->
          <div class="bg-white rounded-lg p-4 border">
            <div class="flex items-center mb-3">
              <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                <span class="text-blue-600 font-bold text-sm">{{ randomCompanyWallet.walletType?.charAt(0) || 'W' }}</span>
              </div>
              <div>
                <h5 class="font-semibold text-gray-900">{{ randomCompanyWallet.walletType }} Wallet</h5>
                <p class="text-xs text-gray-500">Send USDT on {{ randomCompanyWallet.walletType }} network</p>
              </div>
            </div>
            
            <!-- QR Code -->
            <div v-if="companyWalletQRs.walletQR" class="text-center mb-3">
              <img :src="companyWalletQRs.walletQR" alt="Wallet QR Code" class="w-32 h-32 mx-auto border rounded" />
            </div>
            
            <!-- Address -->
            <div class="space-y-2">
              <label class="text-xs font-medium text-gray-700">{{ randomCompanyWallet.walletType }} Address:</label>
              <div class="flex items-center space-x-2">
                <input 
                  :value="randomCompanyWallet.walletAddress" 
                  readonly 
                  class="flex-1 px-2 py-1 text-xs font-mono bg-gray-50 border rounded"
                />
                <button 
                  @click="copyAddress(randomCompanyWallet.walletAddress)" 
                  class="px-2 py-1 text-xs bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                  Copy
                </button>
              </div>
            </div>
          </div>
        </div>

             <!-- Payment Recording Form -->
             <div class="border-t pt-6">
               <h4 class="text-md font-semibold text-gray-900 mb-4">Record Your Payment</h4>
               
               <!-- Transaction Verification Section -->
               <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-4">        
                 
                 <div class="space-y-3">
                   <!-- Verification Method Selection -->
                   <div class="flex space-x-4 mb-3">
                     
                     <label class="flex items-center">
                       <input 
                         v-model="verificationMethod" 
                         type="radio" 
                         value="auto" 
                         class="mr-2"
                       />
                       <span class="text-sm">Auto Scan Recent Blocks</span>
                     </label>
                   </div>
                   
                   <!-- Manual Verification -->
                   <div v-if="verificationMethod === 'manual'" class="space-y-3">
                     <div>
                       <label class="block text-xs font-medium text-gray-700 mb-1">
                         Transaction Hash *
                       </label>
                       <input 
                         v-model="payForm.transactionHash"
                         type="text"
                         placeholder="Enter transaction hash (0x... or TRX hash)"
                         class="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono"
                       />
                     </div>
                     
                     <div>
                       <label class="block text-xs font-medium text-gray-700 mb-1">
                         Contract Address *
                       </label>
                       <input 
                         v-model="payForm.contractAddress"
                         type="text"
                         placeholder="USDT contract address"
                         class="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono"
                       />
                       <p class="text-xs text-gray-500 mt-1">USDT contract address for {{ randomCompanyWallet?.walletType || 'selected' }} network</p>
                     </div>
                     
                     <div class="flex space-x-2">
                       <button 
                         @click="verifyTransaction"
                         :disabled="!payForm.transactionHash || isVerifying"
                         class="flex-1 px-3 py-2 text-sm bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                       >
                         <span v-if="isVerifying">Verifying...</span>
                         <span v-else>🔍 Verify Transaction</span>
                       </button>
                       
                       <button 
                         @click="clearVerification"
                         class="px-3 py-2 text-sm bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                       >
                         Clear
                       </button>
                     </div>
                   </div>
                   
                   <!-- Auto Scan -->
                   <div v-if="verificationMethod === 'auto'" class="space-y-3">
                     <div class="bg-blue-50 border border-blue-200 rounded-lg p-3">
                       <p class="text-xs text-blue-700 mb-2">
                         We'll automatically scan recent blocks to find your USDT transfer.
                       </p>
                       <div class="flex space-x-2">
                         <button 
                           @click="scanRecentTransfers"
                           :disabled="isVerifying"
                           class="flex-1 px-3 py-2 text-sm bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                         >
                           <span v-if="isVerifying">Scanning...</span>
                           <span v-else>🔍 Scan Recent Blocks</span>
                         </button>
                         
                         <button 
                           @click="clearVerification"
                           class="px-3 py-2 text-sm bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                         >
                           Clear
                         </button>
                       </div>
                     </div>
                   </div>
                   
                   <!-- Verification Result -->
                   <div v-if="verificationResult" class="mt-3 p-3 rounded-lg" 
                        :class="verificationResult.verified ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'">
                     <div class="flex items-center">
                       <span v-if="verificationResult.verified" class="text-green-600 text-sm">✅</span>
                       <span v-else class="text-red-600 text-sm">❌</span>
                       <span class="ml-2 text-sm font-medium" 
                             :class="verificationResult.verified ? 'text-green-800' : 'text-red-800'">
                         {{ verificationResult.message }}
                       </span>
                     </div>
                     <div v-if="verificationResult.verified && verificationResult.details" class="mt-2 text-xs text-gray-600">
                       <p v-if="verificationResult.details.foundTransfer">Found matching transfer in recent blocks</p>
                       <p v-if="verificationResult.details.contract">Contract: {{ verificationResult.details.contract }}</p>
                     </div>
                   </div>
                 </div>
               </div>
          <form @submit.prevent="submitPay" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Your Wallet Address</label>
                <input 
                  v-model="payForm.fromAddress" 
                  type="text" 
                  required 
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 font-mono text-sm" 
                  placeholder="Enter your sending wallet address" 
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Amount (USDT)</label>
                <input 
                  v-model="payForm.amount" 
                  type="number" 
                  step="0.000001" 
                  min="0" 
                  required 
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
                  placeholder="0.00" 
                />
              </div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Transaction Hash (Optional)</label>
              <input 
                v-model="payForm.transactionHash" 
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 font-mono text-sm" 
                placeholder="Enter transaction hash after sending payment" 
              />
            </div>

            <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
              <div class="flex">
                <svg class="w-5 h-5 text-yellow-400 mr-3 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
                </svg>
                <div class="text-sm">
                  <p class="text-yellow-800 font-medium">Use TRON or POLYGON for now only, Record Payment.</p>
                </div>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-2">
              <button type="button" @click="showPay = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
              <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Record Payment</button>
            </div>
            
            <p v-if="payMsg" class="text-sm" :class="payOk ? 'text-green-600' : 'text-red-600'">{{ payMsg }}</p>
          </form>
        </div>
      </div>

      <div v-else class="text-center py-8">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
        </svg>
        <h3 class="mt-2 text-sm font-medium text-gray-900">No Payment Wallet Available</h3>
        <p class="mt-1 text-sm text-gray-500">Please contact support or try again later.</p>
        <div class="mt-6">
          <button @click="showPay = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
        </div>
      </div>
    </div>
  </div>

  <!-- Edit Wallet Modal -->
  <div v-if="showEdit" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 w-full max-w-lg mx-4">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">Edit Wallet</h3>
      
      <form @submit.prevent="saveEdit">
        <div class="space-y-4">
          <div>
            <label for="editWalletName" class="block text-sm font-medium text-gray-700 mb-2">
              Wallet Name *
            </label>
            <input
              id="editWalletName"
              v-model="editForm.walletName"
              type="text"
              required
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              placeholder="Enter wallet name"
            />
          </div>

          <div>
            <label for="editWalletType" class="block text-sm font-medium text-gray-700 mb-2">
              Wallet Type *
            </label>
            <select
              id="editWalletType"
              v-model="editForm.walletType"
              required
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
            >
              <option value="">Select wallet type</option>
              <option v-for="type in walletTypes" :key="type.value" :value="type.value">
                {{ type.label }}
              </option>
            </select>
          </div>

          <div>
            <label for="editWalletAddress" class="block text-sm font-medium text-gray-700 mb-2">
              Wallet Address *
            </label>
            <input
              id="editWalletAddress"
              v-model="editForm.walletAddress"
              type="text"
              required
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent font-mono text-sm"
              :placeholder="getAddressPlaceholder(editForm.walletType)"
            />
          </div>

          <div>
            <label for="editWalletBalance" class="block text-sm font-medium text-gray-700 mb-2">
              Balance
            </label>
            <input
              id="editWalletBalance"
              v-model="editForm.balance"
              type="number"
              step="0.00000001"
              min="0"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              placeholder="0.00"
            />
            <p class="text-xs text-gray-500 mt-1">Current balance in USDT</p>
          </div>
        </div>
        
        <div class="flex justify-end space-x-3 pt-4">
          <button type="button" @click="showEdit = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Save</button>
        </div>
        <p v-if="editErr" class="text-sm text-red-600 mt-2">{{ editErr }}</p>
      </form>
    </div>
  </div>

  <!-- Verification Retry Modal -->
  <div v-if="showRetryModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 w-full max-w-lg mx-4">
      <h3 class="text-lg font-bold text-deep-ocean mb-4">🔍 Verification Failed</h3>
      
      <div class="space-y-4">
        <div class="bg-red-50 border border-red-200 rounded-lg p-4">
          <div class="flex items-center">
            <svg class="w-5 h-5 text-red-400 mr-3" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"></path>
            </svg>
            <div>
              <h4 class="text-sm font-semibold text-red-800">Transaction not found</h4>
              <p class="text-xs text-red-700 mt-1">We couldn't verify your payment. This might be because:</p>
              <ul class="text-xs text-red-600 mt-1 ml-4 list-disc">
                <li>Transaction is still pending</li>
                <li>Wrong wallet address or amount</li>
                <li>Transaction on different network</li>
              </ul>
            </div>
          </div>
        </div>

        <div class="space-y-3">
          <h5 class="text-sm font-semibold text-gray-900">Try with a different wallet:</h5>
          
          <div v-if="retryOptions.length > 0" class="space-y-2">
            <div v-for="option in retryOptions" :key="option.id" 
                 class="border border-gray-200 rounded-lg p-3 hover:bg-gray-50 cursor-pointer"
                 @click="selectRetryWallet(option)">
              <div class="flex items-center justify-between">
                <div>
                  <div class="text-sm font-medium text-gray-900">{{ option.walletName }}</div>
                  <div class="text-xs text-gray-500">{{ option.walletType }} Network</div>
                </div>
                <div class="text-xs text-gray-400">
                  {{ option.walletAddress.substring(0, 6) }}...{{ option.walletAddress.substring(-6) }}
                </div>
              </div>
            </div>
          </div>
          
          <div v-else class="text-center py-4 text-gray-500">
            <p class="text-sm">No other wallets available</p>
          </div>
        </div>

        <div class="flex justify-end space-x-3 pt-4">
          <button @click="closeRetryModal" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            Cancel
          </button>
          <button @click="retryWithCurrentWallet" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            Try Again
          </button>
        </div>
      </div>
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
import { USDT_CONTRACTS } from '../../utils/contractAddresses.js'

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
const editForm = ref({ id: null, walletName: '', walletType: '', walletAddress: '', balance: '' })
const editErr = ref('')
const companyWallets = ref([])
const selectedCompanyWallet = computed(() => companyWallets.value.find(w => w.id === payForm.value.toWalletId) || null)
const payForm = ref({ fromAddress: '', toWalletId: null, fromWalletId: null, amount: '', description: '', transactionHash: '', contractAddress: '' })
const payMsg = ref('')
const payOk = ref(false)
const randomCompanyWallet = ref(null)
const companyWalletQRs = ref({})
const isLoadingPayment = ref(false)
const isVerifying = ref(false)
const verificationResult = ref(null)
const verificationMethod = ref('manual')
const showRetryModal = ref(false)
const retryOptions = ref([])

// Contract addresses are now imported from centralized configuration

const newWallet = ref({
  walletName: '',
  walletType: '',
  walletAddress: '',
  balance: ''
})

const walletTypes = ref([
  { value: 'POL', label: 'Polygon (POL)' },
  { value: 'TRX', label: 'TRON (TRX)' },
  { value: 'SOL', label: 'Solana (SOL)' },
  { value: 'BSC', label: 'Binance Smart Chain (BSC)' },
  { value: 'BTC', label: 'Bitcoin (BTC)' },
  { value: 'ETH', label: 'Ethereum (ETH)' },
  { value: 'ADA', label: 'Cardano (ADA)' },
  { value: 'AVAX', label: 'Avalanche (AVAX)' },
  { value: 'DOT', label: 'Polkadot (DOT)' },
  { value: 'LINK', label: 'Chainlink (LINK)' },
  { value: 'ACT', label: 'ACT Token (Polygon Amoy)' },
           { value: 'TTT', label: 'TTT Token (TRON Nile)' }
])

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

const getAddressPlaceholder = (walletType) => {
  const placeholders = {
    'POL': '0x742d35Cc6634C0532925a3b844Bc454e4438f44e',
    'TRX': 'TQn9Y2khEsLJW1ChVWFMSMeRDow5KcbLSE',
    'SOL': '7xKNwKv4JSTXGc5TwDPbN1oMNqtNNQqMRiE8ZhB7dHN3',
    'BSC': '0x742d35Cc6634C0532925a3b844Bc454e4438f44e',
    'BTC': '1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa',
    'ETH': '0x742d35Cc6634C0532925a3b844Bc454e4438f44e',
    'ADA': 'addr1qx2fxv2umyhttkxyxp8x0dlpdt3k6cwng5pxj3jhsydzer3n0d3vllmyqwsx5wktcd8cc3sq835lu7drv2xwl2wywfgse35a3x',
    'AVAX': '0x742d35Cc6634C0532925a3b844Bc454e4438f44e',
    'DOT': '1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa',
    'LINK': '0x742d35Cc6634C0532925a3b844Bc454e4438f44e',
    'ACT': '0x742d35Cc6634C0532925a3b844Bc454e4438f44e',
    'TTT': 'TQwa7kTensPjJVUdfpqiPBGixaNAenCDMS'
  }
  return placeholders[walletType] || 'Enter wallet address'
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

const openPay = async (w) => {
  selected.value = w
  // Auto-fill contract address based on wallet type
  const contractAddress = USDT_CONTRACTS[w.walletType] || ''
  payForm.value = { 
    fromAddress: w.walletAddress || '', 
    toWalletId: null, 
    fromWalletId: w.id, 
    amount: '', 
    description: '', 
    transactionHash: '',
    contractAddress: contractAddress
  }
  payMsg.value = ''
  isLoadingPayment.value = true
  
  try {
    // Get random company wallet of the same type as user's wallet
    const walletType = w.walletType || 'BTC' // Default to BTC if no type
    const response = await apiService.request(`/wallets/company/random?type=${walletType}&qr=true`)
    console.log('API Response:', response) // Debug log
    if (response.success) {
      // Transform the response to match the expected format
      randomCompanyWallet.value = {
        id: response.data.id,
        walletName: `Company ${walletType} Wallet`,
        walletAddress: response.data.walletAddress,
        walletType: response.data.walletType,
        tronAddress: response.data.walletAddress, // For compatibility
        polygonAddress: response.data.walletAddress, // For compatibility
        isActive: true
      }
      companyWalletQRs.value = {
        walletQR: response.data.qrBase64
      }
      console.log('QR Code data:', companyWalletQRs.value) // Debug log
      payForm.value.toWalletId = response.data.id
    } else {
      payMsg.value = `No company wallet available for ${walletType} type. Please try again.`
      payOk.value = false
      return
    }
    
    // Load company wallets for retry options
    await loadCompanyWallets()
    
  } catch (error) {
    console.error('Failed to load random company wallet:', error)
    payMsg.value = 'Failed to load company wallet: ' + error.message
    payOk.value = false
    return
  } finally {
    isLoadingPayment.value = false
  }
  
  showPay.value = true
}

const openEdit = (w) => {
  editErr.value = ''
  selected.value = w
  editForm.value = { 
    id: w.id, 
    walletName: w.walletName || '', 
    walletType: w.walletType || '', 
    walletAddress: w.walletAddress || '',
    balance: w.balance || ''
  }
  showEdit.value = true
}

const saveEdit = async () => {
  editErr.value = ''
  try {
    const walletId = editForm.value.id
    const walletName = (editForm.value.walletName || '').trim()
    const walletType = editForm.value.walletType
    const walletAddress = (editForm.value.walletAddress || '').trim()
    const balance = editForm.value.balance || '0'
    
    if (!walletId) return
    
    // Check if required fields are provided
    if (!walletName || !walletType || !walletAddress) {
      editErr.value = 'Wallet name, type, and address are required.'
      return
    }
    
    const payload = {
      walletName: walletName,
      walletType: walletType,
      walletAddress: walletAddress,
      balance: balance
    }
    
    // Use the updateWallet API for usdt_wallets table
    const response = await apiService.updateWallet(walletId, payload)
    if (response && response.success) {
      // Refresh the wallet list
      await loadWallets()
      showEdit.value = false
      alert('Wallet updated successfully!')
    } else {
      editErr.value = response?.error || 'Update failed'
    }
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
      // Use new usdt_wallets API to get user wallets
      const response = await apiService.request(`/wallets/user?userId=${userId}`)
      if (response.success && response.data) {
        // Convert usdt_wallets format to UI format
        wallets.value = response.data.map(wallet => ({
          id: wallet.id,
          walletName: wallet.walletName,
          walletAddress: wallet.walletAddress || 'No address',
          walletType: wallet.walletType,
          balance: wallet.balance,
          isActive: wallet.isActive,
          isVerified: wallet.isVerified || false,
          tronAddress: wallet.walletAddress, // For compatibility
          polygonAddress: wallet.walletAddress, // For compatibility
          createdAt: wallet.createdAt,
          updatedAt: wallet.updatedAt
        }))
      } else {
        wallets.value = []
      }
    }
  } catch (error) {
    console.error('Failed to load wallets:', error)
    wallets.value = []
  } finally {
    isLoading.value = false
  }
}

const addWallet = async () => {
  // Check if required fields are provided
  if (!newWallet.value.walletName || !newWallet.value.walletType || !newWallet.value.walletAddress) {
    alert('Wallet name, type, and address are required.')
    return
  }

  isAddingWallet.value = true
  try {
    const userId = getUserId()
    const walletData = {
      userId: userId.toString(),
      walletName: newWallet.value.walletName,
      walletAddress: newWallet.value.walletAddress,
      walletType: newWallet.value.walletType,
      balance: newWallet.value.balance || '0',
      isCompany: false
    }

    // Use the createWallet API for usdt_wallets table
    const response = await apiService.createWallet(walletData)
    if (response && response.success) {
      // Refresh the wallet list
      await loadWallets()
      
      // Reset form
      newWallet.value = {
        walletName: '',
        walletType: '',
        walletAddress: '',
        balance: ''
      }
      
      // Close modal
      showAddWalletModal.value = false
      
      alert('Wallet created successfully!')
    } else {
      alert(response?.error || 'Failed to create wallet.')
    }
  } catch (error) {
    console.error('Failed to add wallet:', error)
    alert(`Failed to create wallet: ${error.message || 'Unknown error'}`)
  } finally {
    isAddingWallet.value = false
  }
}

const deleteRow = async (wallet) => {
  if (!confirm(`Are you sure you want to delete wallet "${wallet.walletName}"?`)) {
    return
  }
  
  try {
    const response = await apiService.deleteWallet(wallet.id)
    if (response && response.success) {
      await loadWallets()
      alert('Wallet deleted successfully!')
    } else {
      alert('Failed to delete wallet: ' + (response.error || 'Unknown error'))
    }
  } catch (error) {
    console.error('Failed to delete wallet:', error)
    alert(`Failed to delete wallet: ${error.message || 'Unknown error'}`)
  }
}

const verifyTransaction = async () => {
  if (!payForm.value.transactionHash || !payForm.value.fromAddress || !payForm.value.amount || !payForm.value.contractAddress) {
    verificationResult.value = {
      verified: false,
      message: 'Please fill in transaction hash, from address, amount, and contract address first'
    }
    return
  }
  
  isVerifying.value = true
  verificationResult.value = null
  
  try {
    const payload = {
      txHash: payForm.value.transactionHash,
      fromAddress: payForm.value.fromAddress,
      toAddress: randomCompanyWallet.value?.walletAddress,
      amount: payForm.value.amount,
      chain: randomCompanyWallet.value?.walletType || 'POL',
      contractAddress: payForm.value.contractAddress
    }
    
    const response = await apiService.request('/transactions/verify', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
    if (response && response.success !== undefined) {
      verificationResult.value = {
        verified: response.verified,
        message: response.message
      }
    } else {
      verificationResult.value = {
        verified: false,
        message: response?.error || 'Verification failed'
      }
      // Show retry options when verification fails
      showRetryOptions()
    }
  } catch (e) {
    verificationResult.value = {
      verified: false,
      message: e?.message || 'Verification failed'
    }
    // Show retry options when verification fails
    showRetryOptions()
  } finally {
    isVerifying.value = false
  }
}

const scanRecentTransfers = async () => {
  if (!payForm.value.fromAddress || !payForm.value.amount || !payForm.value.contractAddress) {
    verificationResult.value = {
      verified: false,
      message: 'Please fill in from address, amount, and contract address first'
    }
    return
  }
  
  isVerifying.value = true
  verificationResult.value = null
  
  try {
    const payload = {
      fromAddress: payForm.value.fromAddress,
      toAddress: randomCompanyWallet.value?.walletAddress,
      amount: payForm.value.amount,
      chain: randomCompanyWallet.value?.walletType || 'POL',
      contractAddress: payForm.value.contractAddress
      // Note: No txHash provided, so backend will use auto-scan
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
        message: response?.error || 'Scan failed'
      }
      // Show retry options when scan fails
      showRetryOptions()
    }
  } catch (e) {
    verificationResult.value = {
      verified: false,
      message: e?.message || 'Scan failed'
    }
    // Show retry options when scan fails
    showRetryOptions()
  } finally {
    isVerifying.value = false
  }
}

const clearVerification = () => {
  verificationResult.value = null
  payForm.value.transactionHash = ''
  // Keep contract address as it's auto-filled based on wallet type
}

const showRetryOptions = () => {
  // Get other company wallets of the same type
  const currentWalletType = randomCompanyWallet.value?.walletType
  if (currentWalletType) {
    // Filter out the current wallet and get others of the same type
    retryOptions.value = companyWallets.value.filter(wallet => 
      wallet.walletType === currentWalletType && 
      wallet.id !== randomCompanyWallet.value?.id &&
      wallet.isActive
    )
  } else {
    retryOptions.value = companyWallets.value.filter(wallet => 
      wallet.id !== randomCompanyWallet.value?.id &&
      wallet.isActive
    )
  }
  
  showRetryModal.value = true
}

const selectRetryWallet = (wallet) => {
  // Update the current wallet to the selected one
  randomCompanyWallet.value = {
    id: wallet.id,
    walletName: wallet.walletName,
    walletAddress: wallet.walletAddress,
    walletType: wallet.walletType,
    tronAddress: wallet.walletAddress,
    polygonAddress: wallet.walletAddress,
    isActive: true
  }
  
  // Update the payment form
  payForm.value.toWalletId = wallet.id
  
  // Clear previous verification result
  verificationResult.value = null
  payForm.value.transactionHash = ''
  
  // Close retry modal
  showRetryModal.value = false
  
  // Show success message
  payMsg.value = 'Wallet changed successfully. Please try verification again.'
  payOk.value = true
  
  // Auto-hide message after 3 seconds
  setTimeout(() => {
    payMsg.value = ''
    payOk.value = false
  }, 3000)
}

const retryWithCurrentWallet = () => {
  // Clear verification result and close modal
  verificationResult.value = null
  payForm.value.transactionHash = ''
  showRetryModal.value = false
  
  // Show message to try again
  payMsg.value = 'Please try verification again with the current wallet.'
  payOk.value = false
}

const closeRetryModal = () => {
  showRetryModal.value = false
}

const submitPay = async () => {
  payMsg.value = ''
  payOk.value = false
  try {
    // Get transaction hash from verification result if auto-scan was used
    let transactionHash = payForm.value.transactionHash
    if (verificationMethod.value === 'auto' && verificationResult.value?.verified && verificationResult.value.details?.txHash) {
      transactionHash = verificationResult.value.details.txHash
    }
    
    const payload = {
      fromAddress: (payForm.value.fromAddress || '').trim(),
      toWalletId: payForm.value.toWalletId,
      fromWalletId: payForm.value.fromWalletId,
      amount: String(payForm.value.amount),
      description: (transactionHash || '').trim() // Use transaction hash as description
    }
    const resp = await apiService.recordWalletPayment(payload)
    payOk.value = !!resp.success
    payMsg.value = resp.message || (resp.success ? 'Payment recorded' : (resp.error || 'Failed to record'))
    if (resp.success) {
      showPay.value = false
      // Reset verification
      verificationResult.value = null
      payForm.value.transactionHash = ''
      verificationMethod.value = 'manual'
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