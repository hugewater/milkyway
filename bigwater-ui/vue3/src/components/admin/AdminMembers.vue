<template>
  <AppLayout>
    <div class="p-6">
      <!-- Header -->
      <header class="bg-white shadow-sm border-b border-gray-200 mb-6">
        <div class="flex items-center justify-between px-6 py-4">
          <h1 class="text-2xl font-bold text-deep-ocean">Members Management</h1>
          
          <div class="flex items-center space-x-4">
            <!-- Add Member Button -->
            <button
              @click="showAddModal = true"
              class="btn-primary px-4 py-2 rounded-lg font-medium"
            >
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
              </svg>
              Add Member
            </button>
          </div>
        </div>
      </header>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="card p-6 rounded-2xl">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-blue-100">
              <svg class="w-6 h-6 text-ocean" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm text-gray-600">Total Members</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ members.length }}</p>
            </div>
          </div>
        </div>

        <div class="card p-6 rounded-2xl">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-green-100">
              <svg class="w-6 h-6 text-forest-green" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm text-gray-600">Active Members</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ activeMembersCount }}</p>
            </div>
          </div>
        </div>

        <div class="card p-6 rounded-2xl">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-yellow-100">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm text-gray-600">Premium Members</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ premiumMembersCount }}</p>
            </div>
          </div>
        </div>

        <div class="card p-6 rounded-2xl">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-purple-100">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm text-gray-600">New This Month</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ newMembersThisMonth }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Search and Filters -->
      <div class="card p-6 rounded-2xl mb-6">
        <div class="flex flex-col md:flex-row gap-4 md:items-center">
          <div class="flex-1">
            <div class="relative">
              <input
                v-model="searchQuery"
                @input="onSearchMembers"
                type="text"
                placeholder="Search by name/email/referral code..."
                class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
              <svg class="absolute left-3 top-2.5 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
            </div>
          </div>
          <div class="flex gap-2">
            <select
              v-model="statusFilter"
              @change="offset=0; loadMembers()"
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
            >
              <option value="">All Status</option>
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
              <option value="suspended">Suspended</option>
            </select>
            <select
              v-model="levelFilter"
              @change="offset=0; loadMembers()"
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
            >
              <option value="">All Levels</option>
              <option value="CUSTOMER">Customer</option>
              <option value="CHIEF">Chief</option>
              <option value="MAYOR">Mayor</option>
              <option value="GOVERNOR">Governor</option>
              <option value="MINISTER">Minister</option>
              <option value="PRESIDENT">President</option>
            </select>
            <select
              v-model.number="limit"
              @change="offset=0; loadMembers()"
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
            >
              <option :value="20">20</option>
              <option :value="50">50</option>
              <option :value="100">100</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Members Table -->
      <div class="card rounded-2xl overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th @click="toggleSortMembers('id')" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Level</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th @click="toggleSortMembers('join_date')" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer">Join Date</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Balance</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="member in filteredMembers" :key="member.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ member.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <div class="h-10 w-10 rounded-full bg-ocean flex items-center justify-center text-white font-medium">
                        {{ member.name.charAt(0) }}
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ member.email }}</div>
                      <div class="text-sm">
                        <button v-if="member.referralCode"
                                @click="openAddDownline(member)"
                                class="text-ocean hover:text-deep-ocean hover:underline"
                                title="Add direct downline under this member">
                          {{ member.referralCode }}
                        </button>
                        <span v-else class="text-gray-500">N/A</span>
                      </div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getLevelBadgeClass(member.level)" class="px-2 py-1 text-xs font-medium rounded-full">
                    {{ member.level }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getStatusBadgeClass(member.status)" class="px-2 py-1 text-xs font-medium rounded-full">
                    {{ member.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(member.joinDate) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  ${{ member.balance.toLocaleString() }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium relative">
                  <div class="inline-flex items-center">
                    <button
                      class="px-2 py-1 rounded hover:bg-gray-100"
                      @click="openActions(member, $event)"
                      title="Actions"
                    >
                      ⋯
                    </button>
                  </div>

                  <teleport to="body">
                    <div
                      v-if="actions.open && actions.member?.id === member.id"
                      class="fixed z-[1000] bg-white border border-gray-200 rounded-md shadow-lg w-44 py-1"
                      :style="{ top: actions.pos.top + 'px', left: actions.pos.left + 'px' }"
                      :ref="setActionsMenuEl"
                    >
                      <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="actionEdit()">Edit</button>
                      <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="actionView()">View</button>
                      <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="actionWallets()">Wallets</button>
                      <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="actionGraph()">Graph</button>
                      <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="actionToggle()">{{ actions.member?.status === 'active' ? 'Suspend' : 'Activate' }}</button>
                    </div>
                  </teleport>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- View Member Modal -->
    <div v-if="showViewModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-bold text-deep-ocean">View Member</h3>
          <button @click="showViewModal = false" class="text-gray-500 hover:text-gray-700">✕</button>
        </div>
        <div v-if="viewingMember" class="space-y-3 text-sm">
          <div class="flex justify-between"><span class="text-gray-500">ID</span><span class="text-gray-900">{{ viewingMember.id }}</span></div>
          <div class="flex justify-between"><span class="text-gray-500">Name</span><span class="text-gray-900">{{ viewingMember.name }}</span></div>
          <div class="flex justify-between"><span class="text-gray-500">Email</span><span class="text-gray-900 break-all">{{ viewingMember.email }}</span></div>
          <div class="flex justify-between"><span class="text-gray-500">Level</span><span><span :class="getLevelBadgeClass(viewingMember.level)" class="px-2 py-1 text-xs font-medium rounded-full">{{ viewingMember.level }}</span></span></div>
          <div class="flex justify-between"><span class="text-gray-500">Status</span><span><span :class="getStatusBadgeClass(viewingMember.status)" class="px-2 py-1 text-xs font-medium rounded-full">{{ viewingMember.status }}</span></span></div>
          <div class="flex justify-between"><span class="text-gray-500">Referral Code</span><span class="text-gray-900 font-mono">{{ viewingMember.referralCode || 'N/A' }}</span></div>
          <div class="flex justify-between"><span class="text-gray-500">Join Date</span><span class="text-gray-900">{{ formatDate(viewingMember.joinDate) }}</span></div>
          <div class="flex justify-between"><span class="text-gray-500">Balance</span><span class="text-gray-900">${{ (viewingMember.balance || 0).toLocaleString() }}</span></div>
        </div>
        <div class="flex justify-end mt-6">
          <button @click="showViewModal = false" class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50">Close</button>
        </div>
      </div>
    </div>

    <!-- Add Member Modal -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Add New Member</h3>
        <form @submit.prevent="addMember">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Name (Optional)</label>
              <input
                v-model="newMember.name"
                type="text"
                placeholder="Enter member name (optional)"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
              <input
                v-model="newMember.email"
                type="email"
                required
                placeholder="Enter email address"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Password *</label>
              <input
                v-model="newMember.password"
                type="password"
                required
                placeholder="Enter password for the new member"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Level</label>
              <select
                v-model="newMember.level"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              >
                <option value="CUSTOMER">Customer</option>
                <option value="CHIEF">Chief</option>
                <option value="MAYOR">Mayor</option>
                <option value="GOVERNOR">Governor</option>
                <option value="MINISTER">Minister</option>
                <option value="PRESIDENT">President</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Referred By Code</label>
              <input
                v-model="newMember.referredByCode"
                type="text"
                placeholder="Enter referral code (optional)"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              @click="showAddModal = false"
              class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="btn-primary px-4 py-2 rounded-lg"
            >
              Add Member
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Edit Member Modal -->
    <div v-if="showEditModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Edit Member</h3>
        <form @submit.prevent="saveEditMember">
          <div class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                <input
                  v-model="editingMember.firstName"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                <input
                  v-model="editingMember.lastName"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                />
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input
                v-model="editingMember.email"
                type="email"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Password (Leave blank to keep current)</label>
              <input
                v-model="editingMember.password"
                type="password"
                placeholder="Enter new password (optional)"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
              <input
                v-model="editingMember.phone"
                type="tel"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Level</label>
              <select
                v-model="editingMember.level"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              >
                <option value="CUSTOMER">Customer</option>
                <option value="CHIEF">Chief</option>
                <option value="MAYOR">Mayor</option>
                <option value="GOVERNOR">Governor</option>
                <option value="MINISTER">Minister</option>
                <option value="PRESIDENT">President</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
              <select
                v-model="editingMember.status"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              >
                <option value="active">Active</option>
                <option value="suspended">Suspended</option>
                <option value="inactive">Inactive</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Referred By Code</label>
              <input
                v-model="editingMember.referredByCode"
                type="text"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                placeholder="Enter referral code"
              />
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              @click="showEditModal = false"
              class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="isEditing"
              class="btn-primary px-4 py-2 rounded-lg disabled:opacity-50"
            >
              {{ isEditing ? 'Saving...' : 'Save Changes' }}
            </button>
          </div>
        </form>
        <p v-if="transactionMsg" class="mt-3 text-sm" :class="transactionOk ? 'text-green-600' : 'text-red-600'">{{ transactionMsg }}</p>
      </div>
    </div>

    <!-- Wallets Modal -->
    <div v-if="showWalletsModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-bold text-deep-ocean">Wallets - {{ selectedUser?.name }}</h3>
          <div class="flex space-x-2">
            <button @click="openAddWalletModal" class="btn-primary px-3 py-1 rounded text-sm">Add Wallet</button>
            <button @click="showWalletsModal = false" class="text-gray-500 hover:text-gray-700">✕</button>
          </div>
        </div>
        
        <div v-if="walletsLoading" class="text-sm text-gray-500">Loading...</div>
        <div v-else>
          <div v-if="transactionMsg && !transactionOk" class="text-sm text-red-600 mb-4">{{ transactionMsg }}</div>
          <div v-if="wallets.length === 0" class="text-sm text-gray-500">No wallets found.</div>
          <div v-else class="overflow-x-auto">
            <table class="w-full">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Address</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Balance</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="w in wallets" :key="w.id">
                  <td class="px-4 py-2 text-sm text-gray-900">{{ w.id }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ w.walletName }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ w.walletAddress }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ w.walletType }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ w.balance }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">
                    <span :class="w.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'" class="px-2 py-1 text-xs rounded-full">
                      {{ w.isActive ? 'Active' : 'Inactive' }}
                    </span>
                  </td>
                  <td class="px-4 py-2 text-sm text-gray-900">
                    <button @click="editWallet(w)" class="text-blue-600 hover:text-blue-900 mr-2">Edit</button>
                    <button @click="openPayModal(w)" class="text-green-600 hover:text-green-900 mr-2">Pay</button>
                    <button @click="openWithdrawModal(w)" class="text-purple-600 hover:text-purple-900 mr-2">Withdraw</button>
                    <button @click="viewTransactions(w)" class="text-indigo-600 hover:text-indigo-900 mr-2">Transactions</button>
                    <button @click="toggleWalletStatusLocal(w)" class="text-orange-600 hover:text-orange-900">
                      {{ w.isActive ? 'Deactivate' : 'Activate' }}
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Pay Modal -->
    <div v-if="showPayModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Pay to Wallet: {{ selectedWallet?.walletName }}</h3>
        <form @submit.prevent="submitPay">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Member</label>
              <div class="mt-1 text-sm text-gray-900">{{ selectedUser?.name }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Payee Wallet Address</label>
              <div class="mt-1 text-sm text-gray-900 break-all">{{ selectedWallet?.walletAddress }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">From Wallet Address</label>
              <div class="flex space-x-2">
                <input v-model="transactionForm.fromWalletAddress" type="text" required class="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" placeholder="Enter source wallet address">
                <button @click="generateRandomAddress" type="button" class="px-3 py-2 text-sm bg-gray-100 text-gray-700 border border-gray-300 rounded-lg hover:bg-gray-200">
                  Generate
                </button>
              </div>
              <p class="text-xs text-gray-500 mt-1">If wallet doesn't exist, a new one will be created automatically with 1000 USDT initial balance.</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Current Balance</label>
              <div class="mt-1 text-lg font-semibold text-gray-900">{{ selectedWallet?.balance }} USDT</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Amount to Pay (USDT)</label>
              <input v-model="transactionForm.amount" type="number" step="0.000001" min="0" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" placeholder="0.00">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Description (Optional)</label>
              <input v-model="transactionForm.description" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" placeholder="Payment description">
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-6">
            <button type="button" @click="closePayModal" class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
            <button type="submit" class="btn-primary px-4 py-2 rounded-lg">Pay</button>
          </div>
          <p v-if="transactionMsg" class="mt-3 text-sm" :class="transactionOk ? 'text-green-600' : 'text-red-600'">{{ transactionMsg }}</p>
        </form>
      </div>
    </div>

    <!-- Withdraw Modal -->
    <div v-if="showWithdrawModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Withdraw from Wallet: {{ selectedWallet?.walletName }}</h3>
        <form @submit.prevent="submitWithdraw">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Member</label>
              <div class="mt-1 text-sm text-gray-900">{{ selectedUser?.name }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Wallet Address</label>
              <div class="mt-1 text-sm text-gray-900 break-all">{{ selectedWallet?.walletAddress }}</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">To Address</label>
              <input v-model="transactionForm.toAddress" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" placeholder="Enter destination wallet address">
              <p class="text-xs text-gray-500 mt-1">Enter the external wallet address where you want to withdraw funds.</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Current Balance</label>
              <div class="mt-1 text-lg font-semibold text-gray-900">{{ selectedWallet?.balance }} USDT</div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Amount to Withdraw (USDT)</label>
              <input v-model="transactionForm.amount" type="number" step="0.000001" min="0" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" placeholder="0.00">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Description (Optional)</label>
              <input v-model="transactionForm.description" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" placeholder="Withdrawal description">
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-6">
            <button type="button" @click="closeWithdrawModal" class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
            <button type="submit" class="btn-primary px-4 py-2 rounded-lg">Withdraw</button>
          </div>
          <p v-if="transactionMsg" class="mt-3 text-sm" :class="transactionOk ? 'text-green-600' : 'text-red-600'">{{ transactionMsg }}</p>
        </form>
      </div>
    </div>

    <!-- Transactions Modal -->
    <div v-if="showTransactionsModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-bold text-deep-ocean">Transactions - {{ selectedWallet?.walletName }}</h3>
          <button @click="showTransactionsModal = false" class="text-gray-500 hover:text-gray-700">✕</button>
        </div>
        
        <div v-if="transactionsLoading" class="text-sm text-gray-500">Loading...</div>
        <div v-else>
          <div v-if="transactions.length === 0" class="text-sm text-gray-500">No transactions found.</div>
          <div v-else class="overflow-x-auto">
            <table class="w-full">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="t in transactions" :key="t.id">
                  <td class="px-4 py-2 text-sm text-gray-900">{{ t.id }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">
                    <span :class="getTransactionTypeClass(t.transactionType)" class="px-2 py-1 text-xs rounded-full">
                      {{ t.transactionType }}
                    </span>
                  </td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ t.amountUsdt }} USDT</td>
                  <td class="px-4 py-2 text-sm text-gray-900">
                    <span :class="getTransactionStatusClass(t.status)" class="px-2 py-1 text-xs rounded-full">
                      {{ t.status }}
                    </span>
                  </td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ t.description }}</td>
                  <td class="px-4 py-2 text-sm text-gray-900">{{ formatDateTime(t.createdAt) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Wallet Modal -->
    <div v-if="showAddWalletModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Add New Wallet for {{ selectedUser?.name }}</h3>
        
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
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
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
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                placeholder="Enter USDT wallet address"
              />
            </div>

            <div>
              <label for="walletType" class="block text-sm font-medium text-gray-700 mb-2">
                Wallet Type
              </label>
              <select
                id="walletType"
                v-model="newWallet.walletType"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              >
                <option value="COMPANY">Company</option>
                <option value="MEMBER">Member</option>
                <option value="TESTING">Testing</option>
              </select>
            </div>
          </div>

          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              @click="showAddWalletModal = false"
              class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
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

    <!-- Upline/Downline Modal -->
    <div v-if="showUplineDownlineModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-4xl mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-bold text-deep-ocean">Network - {{ selectedUser?.name }}</h3>
          <button @click="showUplineDownlineModal = false" class="text-gray-500 hover:text-gray-700">✕</button>
        </div>
        
        <div v-if="uplineDownlineLoading" class="text-sm text-gray-500">Loading...</div>
        <div v-else>
          <div v-if="uplines.length === 0 && downlines.length === 0" class="text-sm text-gray-500">No network data available.</div>
          <div v-else>
            <h4 class="text-md font-semibold text-gray-900 mb-3">Uplines:</h4>
            <div v-if="uplines.length === 0" class="text-sm text-gray-500">No uplines found.</div>
            <div v-else class="overflow-x-auto">
              <table class="w-full">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Level</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Balance</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Join Date</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="u in uplines" :key="u.id">
                    <td class="px-4 py-2 text-sm text-gray-900">{{ u.id }}</td>
                    <td class="px-4 py-2 text-sm font-medium text-gray-900">{{ u.name }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900">{{ u.email }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900">
                      <span :class="getLevelBadgeClass(u.level)" class="px-2 py-1 text-xs rounded-full">
                        {{ u.level }}
                      </span>
                    </td>
                    <td class="px-4 py-2 text-sm text-gray-900">
                      <span :class="getStatusBadgeClass(u.status)" class="px-2 py-1 text-xs rounded-full">
                        {{ u.status }}
                      </span>
                    </td>
                    <td class="px-4 py-2 text-sm text-gray-900">${{ u.balance.toLocaleString() }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900">{{ formatDate(u.joinDate) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <h4 class="text-md font-semibold text-gray-900 mt-6 mb-3">Downlines:</h4>
            <div v-if="downlines.length === 0" class="text-sm text-gray-500">No downlines found.</div>
            <div v-else class="overflow-x-auto">
              <table class="w-full">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Level</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Balance</th>
                    <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Join Date</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="d in downlines" :key="d.id">
                    <td class="px-4 py-2 text-sm text-gray-900">{{ d.id }}</td>
                    <td class="px-4 py-2 text-sm font-medium text-gray-900">{{ d.name }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900">{{ d.email }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900">
                      <span :class="getLevelBadgeClass(d.level)" class="px-2 py-1 text-xs rounded-full">
                        {{ d.level }}
                      </span>
                    </td>
                    <td class="px-4 py-2 text-sm text-gray-900">
                      <span :class="getStatusBadgeClass(d.status)" class="px-2 py-1 text-xs rounded-full">
                        {{ d.status }}
                      </span>
                    </td>
                    <td class="px-4 py-2 text-sm text-gray-900">${{ d.balance.toLocaleString() }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900">{{ formatDate(d.joinDate) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Graph Modal (Unified Graph + Table like My Team) -->
    <div v-if="showNetworkGraphModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <UserNetworkGraph
        :selected-user="selectedUser"
        :downlines="downlines"
        :loading="uplineDownlineLoading"
        @close="showNetworkGraphModal = false"
      />
    </div>

    <!-- Add Direct Downline Modal -->
    <div v-if="showAddDownlineModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">Add Direct Downline</h3>
        <p class="text-sm text-gray-600 mb-4">Upline: {{ downlineSelected?.email }} · Referral Code: {{ downlineSelected?.referralCode }}</p>
        <form @submit.prevent="submitAddDownline">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
              <input v-model="addDownlineForm.email" type="email" required autocomplete="off" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" />
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                <input v-model="addDownlineForm.firstName" type="text" autocomplete="off" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                <input v-model="addDownlineForm.lastName" type="text" autocomplete="off" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" />
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
              <input v-model="addDownlineForm.phone" type="tel" autocomplete="off" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
              <input v-model="addDownlineForm.password" type="password" autocomplete="new-password" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Member Level</label>
              <select v-model="addDownlineForm.level" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent">
                <option value="CUSTOMER">Customer</option>
                <option value="CHIEF">Chief</option>
                <option value="MAYOR">Mayor</option>
                <option value="GOVERNOR">Governor</option>
                <option value="MINISTER">Minister</option>
                <option value="PRESIDENT">President</option>
              </select>
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-6">
            <button type="button" @click="showAddDownlineModal = false" class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
            <button type="submit" class="btn-primary px-4 py-2 rounded-lg">Add</button>
          </div>
          <p v-if="downlineMsg" class="mt-3 text-sm" :class="downlineOk ? 'text-green-600' : 'text-red-600'">{{ downlineMsg }}</p>
        </form>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { getWalletsByUserId, transferBetweenWallets, withdrawFromWallet, getWallets, createWallet, getUserNetwork, updateUser, createUser, getUsersPaged, addDownline } from '../../utils/api.js'
import AppLayout from '../layouts/AppLayout.vue'
import NetworkGraph from './NetworkGraph.vue'
import UserNetworkGraph from '../user/UserNetworkGraph.vue'

const router = useRouter()
const showAddModal = ref(false)
const showEditModal = ref(false)
const editingMember = ref(null)
const isEditing = ref(false)

// Wallet management state
const showWalletsModal = ref(false)
const showPayModal = ref(false)
const showWithdrawModal = ref(false)
const showTransactionsModal = ref(false)
const showAddWalletModal = ref(false)
const showViewModal = ref(false)
const viewingMember = ref(null)
const showUplineDownlineModal = ref(false)
const showNetworkGraphModal = ref(false)
const selectedUser = ref(null)
const selectedWallet = ref(null)
const walletsLoading = ref(false)
const wallets = ref([])
const transactions = ref([])
const transactionsLoading = ref(false)
const transactionForm = ref({ amount: 0, description: '', fromWalletAddress: '', toAddress: '' })
const transactionMsg = ref('')
const transactionOk = ref(false)
const isAddingWallet = ref(false)
const uplineDownlineLoading = ref(false)
const uplines = ref([])
const downlines = ref([])

const newWallet = ref({
  walletName: '',
  walletAddress: '',
  walletType: 'MEMBER'
})

// filters & pagination
const searchQuery = ref('')
const statusFilter = ref('') // active/inactive/suspended
const levelFilter = ref('')  // CHIEF/MAYOR/...
const total = ref(0)
const offset = ref(0)
const limit = ref(50)
const sort = ref('created_at')
const order = ref('desc')
let searchTimer = null

const members = ref([])
const loadingMembers = ref(false)

const newMember = ref({
  name: '',
  email: '',
  level: 'CHIEF',
  referredByCode: '',
  password: ''
})

// Computed properties
const activeMembersCount = computed(() => 
  members.value.filter(m => m.status === 'active').length
)

const premiumMembersCount = computed(() => 
  members.value.filter(m => ['MAYOR', 'GOVERNOR', 'MINISTER', 'PRESIDENT'].includes(m.level)).length
)

const newMembersThisMonth = computed(() => {
  const currentMonth = new Date().getMonth()
  const currentYear = new Date().getFullYear()
  return members.value.filter(m => {
    const joinDate = new Date(m.joinDate)
    return joinDate.getMonth() === currentMonth && joinDate.getFullYear() === currentYear
  }).length
})

const filteredMembers = computed(() => members.value)

// Methods
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString()
}
const loadMembers = async () => {
  loadingMembers.value = true
  try {
    const resp = await getUsersPaged({
      offset: offset.value,
      limit: limit.value,
      sort: sort.value,
      order: order.value,
      status: statusFilter.value ? statusFilter.value.toUpperCase() : '',
      level: levelFilter.value,
      q: searchQuery.value
    })
    const items = resp.data || []
    // normalize to fields used by UI
    members.value = items.map(u => ({
      id: u.id,
      name: `${u.firstName || ''} ${u.lastName || ''}`.trim() || u.email,
      email: u.email,
      referralCode: u.referralCode || '',
      level: u.level || 'CHIEF',
      status: (u.status || 'ACTIVE').toLowerCase(),
      joinDate: u.joinDate || u.createdAt || new Date().toISOString(),
      balance: u.balance || 0
    }))
    total.value = resp.total || 0
  } catch (e) {
    members.value = []
    total.value = 0
  } finally {
    loadingMembers.value = false
  }
}

const toggleSortMembers = (col) => {
  if (sort.value === col) {
    order.value = order.value === 'asc' ? 'desc' : 'asc'
  } else {
    sort.value = col
    order.value = 'desc'
  }
  offset.value = 0
  loadMembers()
}

const onSearchMembers = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    offset.value = 0
    loadMembers()
  }, 300)
}

const totalPages = () => Math.max(1, Math.ceil((total.value || 0) / (limit.value || 50)))
const currentPage = () => Math.floor((offset.value || 0) / (limit.value || 50)) + 1
const goToPage = (page) => {
  const tp = totalPages()
  const p = Math.min(tp, Math.max(1, page))
  offset.value = (p - 1) * limit.value
  loadMembers()
}

onMounted(() => {
  loadMembers()
})

const formatDateTime = (dateString) => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleString()
}

// Add Downline Modal state & handlers
const showAddDownlineModal = ref(false)
const addDownlineForm = ref({ email: '', firstName: '', lastName: '', phone: '', password: '', level: 'CUSTOMER' })
const downlineSelected = ref(null)
const downlineMsg = ref('')
const downlineOk = ref(false)

const openAddDownline = (member) => {
  downlineSelected.value = member
  addDownlineForm.value = { email: '', firstName: '', lastName: '', phone: '', password: '', level: 'CUSTOMER' }
  downlineMsg.value = ''
  downlineOk.value = false
  showAddDownlineModal.value = true
}

const submitAddDownline = async () => {
  downlineMsg.value = ''
  downlineOk.value = false
  try {
    if (!downlineSelected.value) return
    const payload = {
      email: addDownlineForm.value.email,
      firstName: addDownlineForm.value.firstName || '',
      lastName: addDownlineForm.value.lastName || '',
      referralCode: downlineSelected.value.referralCode
    }
    if (addDownlineForm.value.level) {
      payload.level = addDownlineForm.value.level
    }
    if (addDownlineForm.value.phone && addDownlineForm.value.phone.trim() !== '') {
      payload.phone = addDownlineForm.value.phone
    }
    if (addDownlineForm.value.password && addDownlineForm.value.password.trim() !== '') {
      payload.password = addDownlineForm.value.password
    }
    const resp = await addDownline(downlineSelected.value.id, payload)
    downlineOk.value = !!resp.success
    downlineMsg.value = resp.message || (resp.success ? 'Downline added successfully' : (resp.error || 'Failed to add downline'))
    if (resp.success) {
      showAddDownlineModal.value = false
    }
  } catch (e) {
    downlineOk.value = false
    downlineMsg.value = e.message || 'Failed to add downline'
  }
}

const getLevelBadgeClass = (level) => {
  const classes = {
    CHIEF: 'bg-red-100 text-red-800',
    MAYOR: 'bg-orange-100 text-orange-800',
    GOVERNOR: 'bg-yellow-100 text-yellow-800',
    MINISTER: 'bg-green-100 text-green-800',
    PRESIDENT: 'bg-blue-100 text-blue-800'
  }
  return classes[level] || 'bg-gray-100 text-gray-800'
}

const getStatusBadgeClass = (status) => {
  const classes = {
    active: 'bg-green-100 text-green-800',
    inactive: 'bg-red-100 text-red-800',
    suspended: 'bg-yellow-100 text-yellow-800'
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

const getTransactionTypeClass = (type) => {
  const classes = {
    DEPOSIT: 'bg-green-100 text-green-800',
    WITHDRAWAL: 'bg-red-100 text-red-800',
    PURCHASE: 'bg-blue-100 text-blue-800',
    REFUND: 'bg-purple-100 text-purple-800',
    REWARD: 'bg-yellow-100 text-yellow-800',
    COMMISSION: 'bg-indigo-100 text-indigo-800'
  }
  return classes[type] || 'bg-gray-100 text-gray-800'
}

const getTransactionStatusClass = (status) => {
  const classes = {
    PENDING: 'bg-yellow-100 text-yellow-800',
    COMPLETED: 'bg-green-100 text-green-800',
    FAILED: 'bg-red-100 text-red-800',
    CANCELLED: 'bg-gray-100 text-gray-800'
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

const addMember = async () => {
  try {
    // Split name into firstName and lastName
    const nameParts = newMember.value.name.trim().split(' ')
    const firstName = nameParts[0] || ''
    const lastName = nameParts.slice(1).join(' ') || ''
    
    const userData = {
      firstName: firstName,
      lastName: lastName,
      email: newMember.value.email,
      level: newMember.value.level,
      referredByCode: newMember.value.referredByCode || null,
      password: newMember.value.password
    }
    
    console.log('Creating user with data:', userData)
    const response = await createUser(userData)
    
    if (response && response.success) {
      // Add the new member to the local list
      const newMemberData = {
        id: response.data.id,
        name: newMember.value.name,
        email: newMember.value.email,
        level: newMember.value.level,
        referredByCode: newMember.value.referredByCode,
        status: 'active',
        joinDate: new Date().toISOString().split('T')[0],
        balance: 0
      }
      members.value.push(newMemberData)
      
      // Reset form and close modal
      newMember.value = { name: '', email: '', level: 'CHIEF', referredByCode: '', password: '' }
      showAddModal.value = false
      
      // Show success message
      transactionMsg.value = 'Member added successfully!'
      transactionOk.value = true
      
      // Clear message after 3 seconds
      setTimeout(() => {
        transactionMsg.value = ''
        transactionOk.value = false
      }, 3000)
    } else {
      transactionMsg.value = response?.error || 'Failed to add member.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Failed to add member:', error)
    transactionMsg.value = `Failed to add member: ${error.message || 'Unknown error'}`
    transactionOk.value = false
  }
}

const editMember = (member) => {
  console.log('Edit member:', member)
  editingMember.value = {
    id: member.id,
    firstName: member.firstName || member.name?.split(' ')[0] || '',
    lastName: member.lastName || member.name?.split(' ').slice(1).join(' ') || '',
    email: member.email,
    phone: member.phone || '',
    level: member.level || 'CHIEF',
    status: member.status || 'active',
    referredByCode: member.referredByCode || '',
    password: ''
  }
  showEditModal.value = true
}

const saveEditMember = async () => {
  if (!editingMember.value) return
  
  isEditing.value = true
  try {
    const userData = {
      firstName: editingMember.value.firstName,
      lastName: editingMember.value.lastName,
      email: editingMember.value.email,
      phone: editingMember.value.phone,
      level: editingMember.value.level,
      status: editingMember.value.status,
      referredByCode: editingMember.value.referredByCode
    }
    
    // Only include password if it's not empty
    if (editingMember.value.password && editingMember.value.password.trim() !== '') {
      userData.password = editingMember.value.password
    }
    
    console.log('Updating user with data:', userData)
    const response = await updateUser(editingMember.value.id, userData)
    
    if (response && response.success) {
      // Update the member in the local list
      const memberIndex = members.value.findIndex(m => m.id === editingMember.value.id)
      if (memberIndex !== -1) {
        members.value[memberIndex] = {
          ...members.value[memberIndex],
          ...userData,
          name: `${userData.firstName} ${userData.lastName}`.trim()
        }
      }
      
      // Close modal and show success message
      showEditModal.value = false
      editingMember.value = null
      transactionMsg.value = 'Member updated successfully!'
      transactionOk.value = true
      
      // Clear message after 3 seconds
      setTimeout(() => {
        transactionMsg.value = ''
        transactionOk.value = false
      }, 3000)
    } else {
      transactionMsg.value = response?.error || 'Failed to update member.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Failed to update member:', error)
    transactionMsg.value = `Failed to update member: ${error.message || 'Unknown error'}`
    transactionOk.value = false
  } finally {
    isEditing.value = false
  }
}

const viewMember = (member) => {
  viewingMember.value = { ...member }
  showViewModal.value = true
}

const toggleMemberStatus = (member) => {
  member.status = member.status === 'active' ? 'suspended' : 'active'
}

// Actions dropdown state
const actions = ref({ open: false, member: null, pos: { top: 0, left: 0 } })
let actionsMenuEl = null
let actionsTriggerEl = null

const openActions = async (member, evt) => {
  actions.value.member = member
  await nextTick()
  actionsTriggerEl = evt.currentTarget
  const rect = actionsTriggerEl.getBoundingClientRect()
  actions.value.pos = { top: rect.bottom + 4, left: Math.min(rect.left, window.innerWidth - 200) }
  actions.value.open = true
}

const closeActions = () => { actions.value.open = false; actions.value.member = null }

const onGlobalClick = (e) => {
  if (!actions.value.open) return
  const menu = actionsMenuEl
  if (menu && typeof menu.contains === 'function' && menu.contains(e.target)) return
  if (actionsTriggerEl && actionsTriggerEl.contains(e.target)) return
  closeActions()
}
const setActionsMenuEl = (el) => { actionsMenuEl = el }
const onGlobalScroll = () => { if (actions.value.open) closeActions() }
const onGlobalResize = () => { if (actions.value.open) closeActions() }

onMounted(() => {
  window.addEventListener('click', onGlobalClick, false)
  window.addEventListener('scroll', onGlobalScroll, true)
  window.addEventListener('resize', onGlobalResize, true)
})
onBeforeUnmount(() => {
  window.removeEventListener('click', onGlobalClick, false)
  window.removeEventListener('scroll', onGlobalScroll, true)
  window.removeEventListener('resize', onGlobalResize, true)
})

// Action handlers
const actionEdit = () => { if (actions.value.member) editMember(actions.value.member); closeActions() }
const actionView = () => { if (actions.value.member) viewMember(actions.value.member); closeActions() }
const actionWallets = () => { if (actions.value.member) openWallets(actions.value.member); closeActions() }
const actionGraph = () => { if (actions.value.member) showNetworkGraph(actions.value.member); closeActions() }
const actionToggle = () => { if (actions.value.member) toggleMemberStatus(actions.value.member); closeActions() }

// Wallet management functions
const openWallets = async (member) => {
  selectedUser.value = member
  walletsLoading.value = true
  transactionMsg.value = ''
  transactionOk.value = false
  
  try {
    console.log('Fetching wallets for user ID:', member.id)
    const response = await getWalletsByUserId(member.id)
    console.log('API response:', response)
    
    if (response && response.success) {
      wallets.value = response.data || []
      console.log('Wallets loaded:', wallets.value)
      showWalletsModal.value = true
    } else {
      console.error('API returned success: false')
      transactionMsg.value = response?.error || 'Failed to load wallets.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Error fetching wallets:', error)
    transactionMsg.value = `Failed to load wallets: ${error.message || 'Unknown error'}`
    transactionOk.value = false
  } finally {
    walletsLoading.value = false
  }
}

const openAddWalletModal = () => {
  newWallet.value = {
    walletName: '',
    walletAddress: '',
    walletType: 'MEMBER'
  }
  showAddWalletModal.value = true
}

const addWallet = async () => {
  if (!newWallet.value.walletName || !newWallet.value.walletAddress || !newWallet.value.walletType) {
    return
  }

  isAddingWallet.value = true
  try {
    const walletData = {
      userId: selectedUser.value.id.toString(),
      walletName: newWallet.value.walletName,
      walletAddress: newWallet.value.walletAddress,
      walletType: newWallet.value.walletType
    }

    const response = await createWallet(walletData)
    if (response && response.success) {
      // Add new wallet to the list
      wallets.value.push(response.data)
      
      // Reset form
      newWallet.value = {
        walletName: '',
        walletAddress: '',
        walletType: 'MAIN'
      }
      
      // Close modal
      showAddWalletModal.value = false
      
      // Show success message
      transactionMsg.value = 'Wallet created successfully!'
      transactionOk.value = true
    } else {
      transactionMsg.value = response?.error || 'Failed to create wallet.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Failed to add wallet:', error)
    transactionMsg.value = `Failed to create wallet: ${error.message || 'Unknown error'}`
    transactionOk.value = false
  } finally {
    isAddingWallet.value = false
  }
}

const editWallet = (wallet) => {
  // Implementation for editing wallet
  console.log('Edit wallet:', wallet)
}

const toggleWalletStatusLocal = (wallet) => {
  // Implementation for toggling wallet status
  console.log('Toggle wallet status:', wallet)
}

const viewTransactions = async (wallet) => {
  selectedWallet.value = wallet
  transactionsLoading.value = true
  try {
    console.log('Fetching transactions for wallet ID:', wallet.id)
    const response = await getTransactionsByWalletId(wallet.id)
    console.log('Transactions API response:', response)
    
    if (response && response.success) {
      transactions.value = response.data || []
      console.log('Transactions loaded:', transactions.value)
      showTransactionsModal.value = true
    } else {
      console.error('API returned success: false')
      transactionMsg.value = response?.error || 'Failed to load transactions.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Error fetching transactions:', error)
    transactionMsg.value = `Failed to load transactions: ${error.message || 'Unknown error'}`
    transactionOk.value = false
  } finally {
    transactionsLoading.value = false
  }
}

const openPayModal = (wallet) => {
  selectedWallet.value = wallet
  transactionForm.value = { amount: 0, description: '', fromWalletAddress: '', toAddress: '' }
  showPayModal.value = true
}

const closePayModal = () => {
  showPayModal.value = false
  transactionMsg.value = ''
  transactionOk.value = false
}

const submitPay = async () => {
  transactionMsg.value = ''
  transactionOk.value = false
  try {
    if (!selectedWallet.value) return
    if (transactionForm.value.amount <= 0) {
      transactionMsg.value = 'Amount must be positive.'
      transactionOk.value = false
      return
    }
    if (transactionForm.value.fromWalletAddress === '') {
      transactionMsg.value = 'Source wallet address is required.'
      transactionOk.value = false
      return
    }

    let fromWallet = await findWalletByAddress(transactionForm.value.fromWalletAddress)
    
    // If source wallet doesn't exist, create one for the payer
    if (!fromWallet) {
      try {
        console.log('Source wallet not found, creating new wallet for payer')
        const newWalletData = {
          userId: selectedUser.value.id.toString(),
          walletAddress: transactionForm.value.fromWalletAddress,
          walletName: `Auto-created Wallet ${Date.now()}`,
          walletType: 'MAIN',
          balance: '1000' // Give some initial balance as string
        }
        
        const createResponse = await createWallet(newWalletData)
        if (createResponse && createResponse.success) {
          fromWallet = createResponse.data
          console.log('Created new wallet:', fromWallet)
          transactionMsg.value = `Created new wallet with address ${transactionForm.value.fromWalletAddress} and initial balance of 1000 USDT.`
        } else {
          transactionMsg.value = 'Failed to create source wallet.'
          transactionOk.value = false
          return
        }
      } catch (createError) {
        console.error('Error creating wallet:', createError)
        transactionMsg.value = `Failed to create source wallet: ${createError.message || 'Unknown error'}`
        transactionOk.value = false
        return
      }
    }

    const payload = {
      fromWalletId: fromWallet.id,
      toWalletId: selectedWallet.value.id,
      amount: parseFloat(transactionForm.value.amount)
    }

    const response = await transferBetweenWallets(payload)

    if (response && response.success) {
      transactionMsg.value = `Successfully paid ${transactionForm.value.amount} USDT to ${selectedWallet.value.walletName}.`
      transactionOk.value = true
      closePayModal()
      await openWallets(selectedUser.value)
    } else {
      transactionMsg.value = response?.error || 'Payment failed.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Error transferring funds:', error)
    transactionMsg.value = `Failed to pay funds: ${error.message || 'Unknown error'}.`
    transactionOk.value = false
  }
}

const openWithdrawModal = (wallet) => {
  selectedWallet.value = wallet
  transactionForm.value = { amount: 0, description: '', fromWalletAddress: '', toAddress: '' }
  showWithdrawModal.value = true
}

const closeWithdrawModal = () => {
  showWithdrawModal.value = false
  transactionMsg.value = ''
  transactionOk.value = false
}

const submitWithdraw = async () => {
  transactionMsg.value = ''
  transactionOk.value = false
  try {
    if (!selectedWallet.value) return
    if (transactionForm.value.amount <= 0) {
      transactionMsg.value = 'Amount must be positive.'
      transactionOk.value = false
      return
    }
    if (transactionForm.value.toAddress === '') {
      transactionMsg.value = 'Destination wallet address is required.'
      transactionOk.value = false
      return
    }

    const payload = {
      amount: parseFloat(transactionForm.value.amount),
      description: transactionForm.value.description || 'Withdrawal',
      toAddress: transactionForm.value.toAddress
    }

    const response = await withdrawFromWallet(selectedWallet.value.id, payload)

    if (response && response.success) {
      transactionMsg.value = `Successfully withdrew ${transactionForm.value.amount} USDT from ${selectedWallet.value.walletName}.`
      transactionOk.value = true
      closeWithdrawModal()
      await openWallets(selectedUser.value)
    } else {
      transactionMsg.value = response?.error || 'Withdrawal failed.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Error withdrawing funds:', error)
    transactionMsg.value = `Failed to withdraw funds: ${error.message || 'Unknown error'}.`
    transactionOk.value = false
  }
}

const findWalletByAddress = async (address) => {
  try {
    const allWallets = await getWallets()
    return allWallets.find(wallet => wallet.walletAddress === address)
  } catch (error) {
    console.error('Error finding wallet by address:', error)
    return null
  }
}

const generateRandomAddress = () => {
  const randomString = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
  transactionForm.value.fromWalletAddress = `0x${randomString}`;
}

const showUplineDownline = async (member) => {
  selectedUser.value = member
  uplineDownlineLoading.value = true
  // Open modal immediately to give instant feedback
  showUplineDownlineModal.value = true
  
  try {
    console.log('Fetching network data for user ID:', member.id)
    const response = await getUserNetwork(member.id)
    console.log('Network API response:', response)
    
    if (response && response.success) {
      uplines.value = response.data.uplines || []
      downlines.value = response.data.downlines || []
      // already opened
    } else {
      console.error('API returned success: false')
      transactionMsg.value = response?.error || 'Failed to load network data.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Error loading upline/downline data:', error)
    // 如果API失败，使用模拟数据作为后备
    await simulateUplineDownlineData(member.id)
    // already opened
  } finally {
    uplineDownlineLoading.value = false
  }
}

const simulateUplineDownlineData = async (memberId) => {
  // 模拟API延迟
  await new Promise(resolve => setTimeout(resolve, 500))
  
  // 模拟上线数据（2个上线）
  uplines.value = [
    {
      id: memberId + 100,
      name: `Upline 1 - ${memberId}`,
      email: `upline1.${memberId}@example.com`,
      level: 'MAYOR',
      status: 'active',
      joinDate: '2023-01-15',
      balance: 8000
    },
    {
      id: memberId + 101,
      name: `Upline 2 - ${memberId}`,
      email: `upline2.${memberId}@example.com`,
      level: 'GOVERNOR',
      status: 'active',
      joinDate: '2023-03-20',
      balance: 5000
    }
  ]
  
  // 模拟下线数据（3个下线）
  downlines.value = [
    {
      id: memberId + 200,
      name: `Downline 1 - ${memberId}`,
      email: `downline1.${memberId}@example.com`,
      level: 'MINISTER',
      status: 'active',
      joinDate: '2024-01-10',
      balance: 2000
    },
    {
      id: memberId + 201,
      name: `Downline 2 - ${memberId}`,
      email: `downline2.${memberId}@example.com`,
      level: 'PRESIDENT',
      status: 'active',
      joinDate: '2024-02-15',
      balance: 800
    },
    {
      id: memberId + 202,
      name: `Downline 3 - ${memberId}`,
      email: `downline3.${memberId}@example.com`,
      level: 'MAYOR',
      status: 'inactive',
      joinDate: '2024-03-05',
      balance: 3500
    }
  ]
}

const showNetworkGraph = async (member) => {
  console.log('showNetworkGraph called with member:', member)
  selectedUser.value = member
  uplineDownlineLoading.value = true
  
  try {
    console.log('Fetching network data for graph - user ID:', member.id)
    const response = await getUserNetwork(member.id)
    console.log('Network API response for graph:', response)
    
    if (response && response.success) {
      uplines.value = response.data.uplines || []
      downlines.value = response.data.downlines || []
      console.log('Setting uplines:', uplines.value)
      console.log('Setting downlines:', downlines.value)
      showNetworkGraphModal.value = true
      console.log('showNetworkGraphModal set to true')
    } else {
      console.error('API returned success: false')
      transactionMsg.value = response?.error || 'Failed to load network data.'
      transactionOk.value = false
    }
  } catch (error) {
    console.error('Error loading network data for graph:', error)
    // 如果API失败，使用模拟数据作为后备
    await simulateUplineDownlineData(member.id)
    showNetworkGraphModal.value = true
  } finally {
    uplineDownlineLoading.value = false
  }
}
</script>
