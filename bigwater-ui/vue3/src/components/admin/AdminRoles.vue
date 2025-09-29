<template>
  <AppLayout>
    <div class="p-6 space-y-8">
      <div class="flex items-center justify-between flex-wrap gap-4">
        <div>
          <h1 class="text-2xl font-bold text-gray-900">Admin Roles</h1>
          <p class="text-sm text-gray-500 mt-1">Manage user roles, subscriber levels and account status.</p>
        </div>
        <div class="flex items-center gap-3">
          <button @click="showAddModal = true" class="btn-primary px-5 py-2 rounded-lg text-sm font-medium shadow-sm">Add User</button>
        </div>
      </div>

      <!-- Stats -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div class="bg-white rounded-lg border border-gray-200 p-4">
          <p class="text-xs font-medium text-gray-500 uppercase tracking-wide">Total Users</p>
          <p class="mt-2 text-2xl font-semibold text-gray-900">{{ users.length }}</p>
        </div>
        <div class="bg-white rounded-lg border border-gray-200 p-4">
          <p class="text-xs font-medium text-gray-500 uppercase tracking-wide">Subscribers</p>
            <p class="mt-2 text-2xl font-semibold text-gray-900">{{ subscribers.length }}</p>
        </div>
        <div class="bg-white rounded-lg border border-gray-200 p-4">
          <p class="text-xs font-medium text-gray-500 uppercase tracking-wide">Admins</p>
          <p class="mt-2 text-2xl font-semibold text-gray-900">{{ admins.length }}</p>
        </div>
        <div class="bg-white rounded-lg border border-gray-200 p-4">
          <p class="text-xs font-medium text-gray-500 uppercase tracking-wide">Active</p>
          <p class="mt-2 text-2xl font-semibold text-gray-900">{{ activeUsersCount }}</p>
        </div>
      </div>

      <!-- Filters -->
      <div class="bg-white border border-gray-200 rounded-lg p-4 flex flex-wrap gap-4 items-end">
        <div>
          <label class="block text-xs font-medium text-gray-600 uppercase mb-1">Role</label>
          <select v-model="roleFilter" class="input-text !py-2 !pr-8 !w-40">
            <option value="">All</option>
            <option value="subscriber">Subscriber</option>
            <option value="admin">Admin</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-medium text-gray-600 uppercase mb-1">Level</label>
          <select v-model="levelFilter" class="input-text !py-2 !pr-8 !w-40">
            <option value="">All</option>
            <option v-for="lvl in SUBSCRIBER_LEVELS" :key="lvl.level" :value="lvl.level">Level {{ lvl.level }}</option>
          </select>
        </div>
        <div class="ml-auto flex gap-2">
          <button @click="levelFilter = ''; roleFilter = ''" class="px-4 py-2 text-sm bg-gray-200 rounded-lg hover:bg-gray-300">Reset</button>
        </div>
      </div>

      <!-- Users Table -->
      <div class="bg-white border border-gray-200 rounded-lg overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200 text-sm">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-2 text-left font-medium text-gray-600">User</th>
                <th class="px-4 py-2 text-left font-medium text-gray-600">Role</th>
                <th class="px-4 py-2 text-left font-medium text-gray-600">Referrals</th>
                <th class="px-4 py-2 text-left font-medium text-gray-600">Earnings</th>
                <th class="px-4 py-2 text-left font-medium text-gray-600">Status</th>
                <th class="px-4 py-2 text-left font-medium text-gray-600">Level</th>
                <th class="px-4 py-2"></th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-100">
              <tr v-for="u in filteredUsers" :key="u.id" class="hover:bg-gray-50">
                <td class="px-4 py-2">
                  <div class="flex items-center gap-3">
                    <div class="w-9 h-9 rounded-full bg-indigo-500 text-white flex items-center justify-center text-xs font-semibold">{{ u.initials }}</div>
                    <div>
                      <div class="font-medium text-gray-900">{{ u.name }}</div>
                      <div class="text-xs text-gray-500">{{ u.email }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-4 py-2">
                  <span :class="u.role === 'admin' ? 'text-red-600 font-medium' : 'text-gray-700'">{{ u.role }}</span>
                </td>
                <td class="px-4 py-2">{{ u.referrals }}</td>
                <td class="px-4 py-2">${{ u.earnings.toFixed(2) }}</td>
                <td class="px-4 py-2">
                  <button @click="toggleUserStatus(u)" :class="['px-2 py-1 rounded text-xs font-medium', u.status === 'active' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700']">
                    {{ u.status }}
                  </button>
                </td>
                <td class="px-4 py-2">
                  <span v-if="u.role === 'subscriber'" class="text-gray-700">L{{ u.level.level }}</span>
                  <span v-else class="text-gray-400">—</span>
                </td>
                <td class="px-4 py-2 text-right space-x-2">
                  <button @click="editUser(u); showAddModal = true" class="text-indigo-600 hover:underline">Edit</button>
                  <button @click="deleteUser(u)" class="text-red-600 hover:underline">Delete</button>
                </td>
              </tr>
              <tr v-if="!filteredUsers.length">
                <td colspan="7" class="px-4 py-8 text-center text-gray-500">No users match current filters.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Add / Edit Modal -->
      <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 p-4">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-lg p-6 relative">
          <button @click="closeModal" class="absolute top-3 right-3 text-gray-400 hover:text-gray-600" aria-label="Close">✕</button>
          <h2 class="text-lg font-semibold mb-4">{{ editingUser ? 'Edit User' : 'Add User' }}</h2>
          <form @submit.prevent="saveUser" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-xs font-medium text-gray-600 uppercase mb-1">Name</label>
                <input v-model="userForm.name" required type="text" class="input-text" placeholder="Full name" />
              </div>
              <div>
                <label class="block text-xs font-medium text-gray-600 uppercase mb-1">Email</label>
                <input v-model="userForm.email" required type="email" class="input-text" placeholder="Email" />
              </div>
              <div>
                <label class="block text-xs font-medium text-gray-600 uppercase mb-1">Role</label>
                <select v-model="userForm.role" class="input-text">
                  <option value="subscriber">Subscriber</option>
                  <option value="admin">Admin</option>
                </select>
              </div>
            </div>
            <div class="flex gap-3 pt-2">
              <button type="submit" class="btn-primary flex-1 py-2 rounded-lg">
                {{ editingUser ? 'Update User' : 'Add User' }}
              </button>
              <button type="button" @click="closeModal" class="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-400">Cancel</button>
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
import { calculateUserLevel, SUBSCRIBER_LEVELS } from '../../utils/userLevels.js'

const showAddModal = ref(false)
const editingUser = ref(null)
const roleFilter = ref('')
const levelFilter = ref('')

const userForm = ref({ name: '', email: '', role: 'subscriber' })

const users = ref([
  { id: 1, name: 'John Doe', email: 'john@example.com', role: 'subscriber', referrals: 15, earnings: 125.50, status: 'active', initials: 'JD', level: calculateUserLevel(15) },
  { id: 2, name: 'Sarah Johnson', email: 'sarah@example.com', role: 'admin', referrals: 0, earnings: 0, status: 'active', initials: 'SJ', level: null },
  { id: 3, name: 'Mike Wilson', email: 'mike@example.com', role: 'subscriber', referrals: 35, earnings: 450.75, status: 'active', initials: 'MW', level: calculateUserLevel(35) },
  { id: 4, name: 'Emma Davis', email: 'emma@example.com', role: 'subscriber', referrals: 8, earnings: 89.25, status: 'suspended', initials: 'ED', level: calculateUserLevel(8) }
])

const subscribers = computed(() => users.value.filter(u => u.role === 'subscriber'))
const admins = computed(() => users.value.filter(u => u.role === 'admin'))
const activeUsersCount = computed(() => users.value.filter(u => u.status === 'active').length)

const filteredUsers = computed(() => {
  let list = users.value
  if (roleFilter.value) list = list.filter(u => u.role === roleFilter.value)
  if (levelFilter.value) list = list.filter(u => u.role === 'subscriber' && u.level.level === parseInt(levelFilter.value))
  return list
})

const editUser = (user) => {
  editingUser.value = user
  userForm.value = { name: user.name, email: user.email, role: user.role }
}

const closeModal = () => {
  showAddModal.value = false
  editingUser.value = null
  userForm.value = { name: '', email: '', role: 'subscriber' }
}

const saveUser = () => {
  if (editingUser.value) {
    const idx = users.value.findIndex(u => u.id === editingUser.value.id)
    if (idx !== -1) {
      users.value[idx] = { ...users.value[idx], name: userForm.value.name, email: userForm.value.email, role: userForm.value.role, initials: initials(userForm.value.name) }
    }
  } else {
    users.value.push({ id: Date.now(), name: userForm.value.name, email: userForm.value.email, role: userForm.value.role, referrals: 0, earnings: 0, status: 'active', initials: initials(userForm.value.name), level: userForm.value.role === 'subscriber' ? calculateUserLevel(0) : null })
  }
  closeModal()
}

const initials = (name) => name.split(' ').map(n => n[0]).join('').toUpperCase()

const toggleUserStatus = (user) => { user.status = user.status === 'active' ? 'suspended' : 'active' }
const deleteUser = (user) => { if (confirm(`Delete ${user.name}?`)) users.value = users.value.filter(u => u.id !== user.id) }
</script>

<style scoped>
table { border-collapse: separate; border-spacing: 0; }
th { font-size: 0.65rem; letter-spacing: .05em; }
</style>
