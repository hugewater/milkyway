<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
    <div class="flex items-center justify-end mb-6">
      <button @click="showAddModal = true" class="btn-primary">
        Add New Admin
      </button>
    </div>

    <!-- Admin Statistics -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-blue-100">
            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Total Admins</p>
            <p class="text-2xl font-bold text-deep-ocean">{{ admins.length }}</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-green-100">
            <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Active Admins</p>
            <p class="text-2xl font-bold text-deep-ocean">{{ activeAdmins.length }}</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-yellow-100">
            <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 0h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Super Admins</p>
            <p class="text-2xl font-bold text-deep-ocean">{{ superAdmins.length }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Admin List -->
    <div class="card p-6 rounded-2xl">
      <div class="flex items-center justify-between mb-6">
        <h3 class="text-lg font-bold text-deep-ocean">Admin Users</h3>
        <div class="flex space-x-4">
          <select v-model="statusFilter" class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
            <option value="">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
            <option value="suspended">Suspended</option>
          </select>
          <select v-model="roleFilter" class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
            <option value="">All Roles</option>
            <option value="admin">Admin</option>
            <option value="super_admin">Super Admin</option>
          </select>
        </div>
      </div>
      
      <div class="overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="border-b border-gray-200">
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Admin</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Role</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Wallets</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Last Login</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Status</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="admin in filteredAdmins" :key="admin.id" class="border-b border-gray-100">
              <td class="py-3 px-4">
                <div class="flex items-center space-x-3">
                  <div class="w-10 h-10 bg-ocean rounded-full flex items-center justify-center text-white text-sm font-medium">
                    {{ admin.initials }}
                  </div>
                  <div>
                    <p class="text-sm font-medium text-gray-900">{{ admin.name }}</p>
                    <p class="text-xs text-gray-500">{{ admin.email }}</p>
                  </div>
                </div>
              </td>
              <td class="py-3 px-4">
                <span :class="`px-2 py-1 rounded-full text-xs font-medium ${
                  admin.adminRole === 'super_admin' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'
                }`">
                  {{ admin.adminRole === 'super_admin' ? 'Super Admin' : 'Admin' }}
                </span>
              </td>
              <td class="py-3 px-4">
                <div class="space-y-1">
                  <div v-if="admin.wallets.usdt" class="text-xs">
                    <span class="text-gray-500">USDT:</span> 
                    <span class="font-mono">{{ admin.wallets.usdt.substring(0, 8) }}...</span>
                  </div>
                  <div v-if="admin.wallets.btc" class="text-xs">
                    <span class="text-gray-500">BTC:</span> 
                    <span class="font-mono">{{ admin.wallets.btc.substring(0, 8) }}...</span>
                  </div>
                  <div v-if="admin.wallets.eth" class="text-xs">
                    <span class="text-gray-500">ETH:</span> 
                    <span class="font-mono">{{ admin.wallets.eth.substring(0, 8) }}...</span>
                  </div>
                </div>
              </td>
              <td class="py-3 px-4 text-sm text-gray-900">{{ admin.lastLogin }}</td>
              <td class="py-3 px-4">
                <span :class="`px-2 py-1 rounded-full text-xs font-medium ${
                  admin.status === 'active' ? 'bg-green-100 text-green-800' : 
                  admin.status === 'inactive' ? 'bg-gray-100 text-gray-800' : 'bg-red-100 text-red-800'
                }`">
                  {{ admin.status }}
                </span>
              </td>
              <td class="py-3 px-4">
                <div class="flex space-x-2">
                  <button @click="editAdmin(admin)" class="text-blue-600 hover:text-blue-800 text-sm">
                    Edit
                  </button>
                  <button @click="toggleAdminStatus(admin)" class="text-orange-600 hover:text-orange-800 text-sm">
                    {{ admin.status === 'active' ? 'Suspend' : 'Activate' }}
                  </button>
                  <button @click="deleteAdmin(admin)" class="text-red-600 hover:text-red-800 text-sm">
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Add/Edit Admin Modal -->
    <div v-if="showAddModal || editingAdmin" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">
          {{ editingAdmin ? 'Edit Admin' : 'Add New Admin' }}
        </h3>
        
        <form @submit.prevent="saveAdmin" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">First Name *</label>
              <input v-model="adminForm.firstName" type="text" required class="w-full border border-gray-300 rounded-lg px-3 py-2">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Last Name *</label>
              <input v-model="adminForm.lastName" type="text" required class="w-full border border-gray-300 rounded-lg px-3 py-2">
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Email Address *</label>
            <input v-model="adminForm.email" type="email" required class="w-full border border-gray-300 rounded-lg px-3 py-2">
          </div>
          
          <div v-if="!editingAdmin">
            <label class="block text-sm font-medium text-gray-700 mb-1">Password *</label>
            <input v-model="adminForm.password" type="password" required class="w-full border border-gray-300 rounded-lg px-3 py-2">
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Admin Role</label>
            <select v-model="adminForm.adminRole" class="w-full border border-gray-300 rounded-lg px-3 py-2">
              <option value="admin">Admin</option>
              <option value="super_admin">Super Admin</option>
            </select>
          </div>

          <!-- Wallet Information -->
          <div class="border-t pt-4">
            <h4 class="text-sm font-semibold text-gray-700 mb-3">Wallet Information</h4>
            
            <div class="space-y-3">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">USDT Wallet Address</label>
                <input v-model="adminForm.wallets.usdt" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="TRC20 USDT wallet address">
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Bitcoin Wallet Address</label>
                <input v-model="adminForm.wallets.btc" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="Bitcoin wallet address">
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Ethereum Wallet Address</label>
                <input v-model="adminForm.wallets.eth" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="Ethereum wallet address">
              </div>
            </div>
          </div>

          <!-- Permissions -->
          <div class="border-t pt-4">
            <h4 class="text-sm font-semibold text-gray-700 mb-3">Permissions</h4>
            <div class="grid grid-cols-2 gap-3">
              <label class="flex items-center">
                <input v-model="adminForm.permissions.manageUsers" type="checkbox" class="mr-2">
                <span class="text-sm">Manage Users</span>
              </label>
              <label class="flex items-center">
                <input v-model="adminForm.permissions.managePayments" type="checkbox" class="mr-2">
                <span class="text-sm">Manage Payments</span>
              </label>
              <label class="flex items-center">
                              <input v-model="adminForm.permissions.manageCertificates" type="checkbox" class="mr-2">
              <span class="text-sm">Manage Certificates</span>
              </label>
              <label class="flex items-center">
                <input v-model="adminForm.permissions.manageDrawings" type="checkbox" class="mr-2">
                <span class="text-sm">Manage Drawings</span>
              </label>
              <label class="flex items-center">
                <input v-model="adminForm.permissions.manageJournals" type="checkbox" class="mr-2">
                <span class="text-sm">Manage Journals</span>
              </label>
              <label class="flex items-center">
                <input v-model="adminForm.permissions.viewReports" type="checkbox" class="mr-2">
                <span class="text-sm">View Reports</span>
              </label>
            </div>
          </div>
          
          <div class="flex space-x-3 pt-4">
            <button type="submit" class="flex-1 btn-primary">
              {{ editingAdmin ? 'Update Admin' : 'Add Admin' }}
            </button>
            <button type="button" @click="closeModal" class="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

const showAddModal = ref(false)
const editingAdmin = ref(null)
const statusFilter = ref('')
const roleFilter = ref('')

const adminForm = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  adminRole: 'admin',
  wallets: {
    usdt: '',
    btc: '',
    eth: ''
  },
  permissions: {
    manageUsers: true,
    managePayments: false,
    manageCertificates: false,
    manageDrawings: false,
    manageJournals: false,
    viewReports: true
  }
})

// Mock admin data
const admins = ref([
  {
    id: 1,
    firstName: 'Main',
    lastName: 'Admin',
    name: 'Main Admin',
    email: 'tao1919@proton.me',
    adminRole: 'super_admin',
    status: 'active',
    lastLogin: '2024-08-22 14:30',
    initials: 'MA',
    wallets: {
      usdt: 'TQn9Y2khEsLMJ4ibMQTBBj...XvzUDWY9bq',
      btc: '1A1zP1eP5QGefi2DMPTfTL...5vHAHvZf',
      eth: '0x742d35Cc6610C8...f78A42C967F3a'
    },
    permissions: {
      manageUsers: true,
      managePayments: true,
      manageCertificates: true,
      manageDrawings: true,
      manageJournals: true,
      viewReports: true
    }
  },
  {
    id: 2,
    firstName: 'John',
    lastName: 'Manager',
    name: 'John Manager',
    email: 'john.manager@bigwater.com',
    adminRole: 'admin',
    status: 'active',
    lastLogin: '2024-08-21 09:15',
    initials: 'JM',
    wallets: {
      usdt: 'TLsV52sRDL5DZ46H...R8E7k5M9u2dA',
      btc: '',
      eth: '0x8ba1f109551bD4...2Fe2C4E2553448'
    },
    permissions: {
      manageUsers: true,
      managePayments: false,
      manageCertificates: false,
      manageDrawings: true,
      manageJournals: true,
      viewReports: true
    }
  },
  {
    id: 3,
    firstName: 'Sarah',
    lastName: 'Support',
    name: 'Sarah Support',
    email: 'sarah.support@bigwater.com',
    adminRole: 'admin',
    status: 'inactive',
    lastLogin: '2024-08-15 16:45',
    initials: 'SS',
    wallets: {
      usdt: 'TPYmHEhy5n8TCe...L3zmHDUmgLqobf',
      btc: '',
      eth: ''
    },
    permissions: {
      manageUsers: false,
      managePayments: false,
      manageCertificates: false,
      manageDrawings: false,
      manageJournals: true,
      viewReports: true
    }
  }
])

const activeAdmins = computed(() => admins.value.filter(admin => admin.status === 'active'))
const superAdmins = computed(() => admins.value.filter(admin => admin.adminRole === 'super_admin'))

const filteredAdmins = computed(() => {
  let filtered = admins.value
  
  if (statusFilter.value) {
    filtered = filtered.filter(admin => admin.status === statusFilter.value)
  }
  
  if (roleFilter.value) {
    filtered = filtered.filter(admin => admin.adminRole === roleFilter.value)
  }
  
  return filtered
})

const editAdmin = (admin) => {
  editingAdmin.value = admin
  adminForm.value = {
    firstName: admin.firstName,
    lastName: admin.lastName,
    email: admin.email,
    password: '',
    adminRole: admin.adminRole,
    wallets: { ...admin.wallets },
    permissions: { ...admin.permissions }
  }
}

const closeModal = () => {
  showAddModal.value = false
  editingAdmin.value = null
  adminForm.value = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    adminRole: 'admin',
    wallets: {
      usdt: '',
      btc: '',
      eth: ''
    },
    permissions: {
      manageUsers: true,
      managePayments: false,
      manageCertificates: false,
      manageDrawings: false,
      manageJournals: false,
      viewReports: true
    }
  }
}

const saveAdmin = () => {
  if (editingAdmin.value) {
    // Update existing admin
    const index = admins.value.findIndex(a => a.id === editingAdmin.value.id)
    if (index !== -1) {
      admins.value[index] = {
        ...admins.value[index],
        firstName: adminForm.value.firstName,
        lastName: adminForm.value.lastName,
        name: `${adminForm.value.firstName} ${adminForm.value.lastName}`,
        email: adminForm.value.email,
        adminRole: adminForm.value.adminRole,
        wallets: { ...adminForm.value.wallets },
        permissions: { ...adminForm.value.permissions },
        initials: `${adminForm.value.firstName[0]}${adminForm.value.lastName[0]}`.toUpperCase()
      }
    }
  } else {
    // Add new admin
    const newAdmin = {
      id: Date.now(),
      firstName: adminForm.value.firstName,
      lastName: adminForm.value.lastName,
      name: `${adminForm.value.firstName} ${adminForm.value.lastName}`,
      email: adminForm.value.email,
      adminRole: adminForm.value.adminRole,
      status: 'active',
      lastLogin: 'Never',
      initials: `${adminForm.value.firstName[0]}${adminForm.value.lastName[0]}`.toUpperCase(),
      wallets: { ...adminForm.value.wallets },
      permissions: { ...adminForm.value.permissions }
    }
    admins.value.push(newAdmin)
  }
  
  closeModal()
}

const toggleAdminStatus = (admin) => {
  if (admin.status === 'active') {
    admin.status = 'suspended'
  } else {
    admin.status = 'active'
  }
}

const deleteAdmin = (admin) => {
  if (admin.email === 'tao1919@proton.me') {
    alert('Cannot delete the main super admin!')
    return
  }
  
  if (confirm(`Are you sure you want to delete ${admin.name}? This action cannot be undone.`)) {
    const index = admins.value.findIndex(a => a.id === admin.id)
    if (index !== -1) {
      admins.value.splice(index, 1)
    }
  }
}
</script>