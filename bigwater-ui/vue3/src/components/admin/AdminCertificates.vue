<template>
  <AppLayout @click="closeDropdown">
    <div class="p-6">
      <!-- Header -->
      <header class="bg-white shadow-sm border-b border-gray-200 mb-6">
        <div class="flex items-center justify-between px-6 py-4">
          <h1 class="text-2xl font-bold text-deep-ocean">Certificate Management</h1>
          
          <div class="flex items-center space-x-4">
            <!-- Add Certificate Button -->
            <button
              @click="showAddModal = true"
              class="btn-primary px-4 py-2 rounded-lg font-medium"
            >
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
              </svg>
              Add Certificate
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
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm text-gray-600">Total Certificates</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ stats.totalCertificates }}</p>
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
              <p class="text-sm text-gray-600">Active Certificates</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ stats.activeCertificates }}</p>
            </div>
          </div>
        </div>

        <div class="card p-6 rounded-2xl">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-yellow-100">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm text-gray-600">Total Value</p>
              <p class="text-2xl font-bold text-deep-ocean">${{ stats.totalValue.toLocaleString() }}</p>
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
              <p class="text-sm text-gray-600">Total Sales</p>
              <p class="text-2xl font-bold text-deep-ocean">{{ stats.totalSales }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Search and Filters -->
      <div class="card p-6 rounded-2xl mb-6">
        <div class="flex flex-col md:flex-row gap-4">
          <div class="flex-1">
            <div class="relative">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search certificates..."
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
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
            >
              <option value="">All Status</option>
              <option value="active">Active</option>
              <option value="inactive">Inactive</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Certificates Table -->
      <div class="card rounded-2xl overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Certificate</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price (USDT)</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Supply</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="certificate in filteredCertificates" :key="certificate.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <div class="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center">
                        <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
                        </svg>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ certificate.certificateName }}</div>
                      <div class="text-sm text-gray-500">{{ certificate.description }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  ${{ certificate.priceUsdt }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div v-if="certificate.maxSupply">
                    {{ certificate.currentSupply }}/{{ certificate.maxSupply }}
                  </div>
                  <div v-else>
                    {{ certificate.currentSupply }}/∞
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getStatusBadgeClass(certificate.isActive)" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                    {{ certificate.isActive ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ formatDate(certificate.createdAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="relative inline-block text-left">
                    <button
                      @click.stop="toggleDropdown(certificate.id)"
                      class="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ocean"
                    >
                      Actions
                      <svg class="-mr-1 ml-2 h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                      </svg>
                    </button>
                    
                    <div
                      v-if="openDropdown === certificate.id"
                      class="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-10"
                      @click.stop
                    >
                      <div class="py-1" role="menu" aria-orientation="vertical">
                        <button
                          @click="viewCertificate(certificate)"
                          class="block w-full text-left px-4 py-2 text-sm text-blue-600 hover:bg-blue-50 hover:text-blue-900"
                          role="menuitem"
                        >
                          View
                        </button>
                        <button
                          @click="editCertificate(certificate)"
                          class="block w-full text-left px-4 py-2 text-sm text-green-600 hover:bg-green-50 hover:text-green-900"
                          role="menuitem"
                        >
                          Edit
                        </button>
                        <button
                          @click="toggleCertificateStatusLocal(certificate)"
                          :class="certificate.isActive ? 'text-red-600 hover:bg-red-50 hover:text-red-900' : 'text-green-600 hover:bg-green-50 hover:text-green-900'"
                          class="block w-full text-left px-4 py-2 text-sm"
                          role="menuitem"
                        >
                          {{ certificate.isActive ? 'Deactivate' : 'Activate' }}
                        </button>
                        <button
                          @click="deleteCertificateLocal(certificate)"
                          class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 hover:text-red-900"
                          role="menuitem"
                        >
                          Delete
                        </button>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Add/Edit Certificate Modal -->
    <div v-if="showAddModal || showEditModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl mx-4 max-h-[90vh] overflow-y-auto">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">
          {{ showEditModal ? 'Edit Certificate' : 'Add New Certificate' }}
        </h3>
        <form @submit.prevent="showEditModal ? saveEditCertificate() : saveAddCertificate()">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Certificate Name</label>
              <input
                v-model="certificateForm.certificateName"
                type="text"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
              <textarea
                v-model="certificateForm.description"
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              ></textarea>
            </div>
            
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Price (USDT)</label>
                <input
                  v-model="certificateForm.priceUsdt"
                  type="number"
                  step="0.01"
                  min="0"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Duration (Days)</label>
                <input
                  v-model="certificateForm.durationDays"
                  type="number"
                  min="1"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Max Supply</label>
                <input
                  v-model="certificateForm.maxSupply"
                  type="number"
                  min="1"
                  placeholder="Leave empty for unlimited"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                <div class="flex items-center space-x-3">
                  <label class="flex items-center">
                    <input
                      v-model="certificateForm.isActive"
                      type="radio"
                      :value="true"
                      class="w-4 h-4 text-ocean focus:ring-ocean border-gray-300"
                    />
                    <span class="ml-2 text-sm text-gray-700">Active</span>
                  </label>
                  <label class="flex items-center">
                    <input
                      v-model="certificateForm.isActive"
                      type="radio"
                      :value="false"
                      class="w-4 h-4 text-ocean focus:ring-ocean border-gray-300"
                    />
                    <span class="ml-2 text-sm text-gray-700">Inactive</span>
                  </label>
                </div>
              </div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Benefits (one per line)</label>
              <textarea
                v-model="certificateForm.benefits"
                rows="4"
                placeholder="Weekly newsletter&#10;Priority support&#10;Exclusive content&#10;VIP events"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ocean focus:border-transparent"
              ></textarea>
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="isSaving"
              class="btn-primary px-4 py-2 rounded-lg disabled:opacity-50"
            >
              {{ isSaving ? 'Saving...' : (showEditModal ? 'Update Certificate' : 'Create Certificate') }}
            </button>
          </div>
        </form>
        <p v-if="message" class="mt-3 text-sm" :class="messageOk ? 'text-green-600' : 'text-red-600'">{{ message }}</p>
      </div>
    </div>

    <!-- View Certificate Modal -->
    <div v-if="showViewModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-bold text-deep-ocean">Certificate Details</h3>
          <button @click="showViewModal = false" class="text-gray-500 hover:text-gray-700">✕</button>
        </div>
        
        <div v-if="selectedCertificate" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Certificate Name</label>
            <p class="text-sm text-gray-900">{{ selectedCertificate.certificateName }}</p>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">Description</label>
            <p class="text-sm text-gray-900">{{ selectedCertificate.description }}</p>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Price (USDT)</label>
              <p class="text-sm text-gray-900">${{ selectedCertificate.priceUsdt }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Duration (Days)</label>
              <p class="text-sm text-gray-900">{{ selectedCertificate.durationDays }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Status</label>
              <span :class="getStatusBadgeClass(selectedCertificate.isActive)" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                {{ selectedCertificate.isActive ? 'Active' : 'Inactive' }}
              </span>
            </div>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Supply</label>
              <p class="text-sm text-gray-900">
                {{ selectedCertificate.currentSupply }}/{{ selectedCertificate.maxSupply || '∞' }}
              </p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Created</label>
              <p class="text-sm text-gray-900">{{ formatDate(selectedCertificate.createdAt) }}</p>
            </div>
          </div>
          
          <div v-if="selectedCertificate.benefits">
            <label class="block text-sm font-medium text-gray-700">Benefits</label>
            <div class="mt-2">
              <div v-for="benefit in parseBenefits(selectedCertificate.benefits)" :key="benefit" class="flex items-center mb-1">
                <svg class="w-4 h-4 text-green-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span class="text-sm text-gray-900">{{ benefit }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCertificates, createCertificate, updateCertificate, deleteCertificate, toggleCertificateStatus, getCertificateStats } from '../../utils/api.js'
import AppLayout from '../layouts/AppLayout.vue'

const searchQuery = ref('')
const statusFilter = ref('')

const showAddModal = ref(false)
const showEditModal = ref(false)
const showViewModal = ref(false)
const isSaving = ref(false)
const message = ref('')
const messageOk = ref(false)

const selectedCertificate = ref(null)
const certificates = ref([])
const openDropdown = ref(null)
const stats = ref({
  totalCertificates: 0,
  activeCertificates: 0,
  totalValue: 0,
  totalSales: 0
})

const certificateForm = ref({
  certificateName: '',
  description: '',
  priceUsdt: '',
  durationDays: '',
  benefits: '',
  maxSupply: '',
  isActive: true
})

onMounted(() => {
  loadCertificates()
  loadStats()
})

const loadCertificates = async () => {
  try {
    const response = await getCertificates()
    certificates.value = response.data || response
  } catch (error) {
    console.error('Error loading certificates:', error)
    certificates.value = []
  }
}

const loadStats = async () => {
  try {
    const response = await getCertificateStats()
    stats.value = response.data || response
  } catch (error) {
    console.error('Error loading stats:', error)
    stats.value = {
      totalCertificates: 0,
      activeCertificates: 0,
      totalValue: 0,
      totalSales: 0
    }
  }
}

const filteredCertificates = computed(() => {
  let filtered = certificates.value

  if (searchQuery.value) {
    filtered = filtered.filter(cert => 
      cert.certificateName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      cert.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  if (statusFilter.value) {
    filtered = filtered.filter(cert => 
      statusFilter.value === 'active' ? cert.isActive : !cert.isActive
    )
  }

  return filtered
})

const getStatusBadgeClass = (isActive) => {
  return isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString()
}

const parseBenefits = (benefits) => {
  if (!benefits) return []
  
  try {
    const parsed = JSON.parse(benefits)
    return Array.isArray(parsed) ? parsed : [benefits]
  } catch {
    // If JSON parsing fails, treat as plain text
    return [benefits]
  }
}

const resetForm = () => {
  certificateForm.value = {
    certificateName: '',
    description: '',
    priceUsdt: '',
    durationDays: '',
    benefits: '',
    maxSupply: '',
    isActive: true
  }
}

const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  showViewModal.value = false
  selectedCertificate.value = null
  resetForm()
  message.value = ''
}

const addCertificate = () => {
  showAddModal.value = true
  resetForm()
}

const editCertificate = (certificate) => {
  selectedCertificate.value = certificate
  
  // Convert JSON benefits to text format
  let benefitsText = ''
  if (certificate.benefits) {
    try {
      const benefitsArray = JSON.parse(certificate.benefits)
      benefitsText = Array.isArray(benefitsArray) ? benefitsArray.join('\n') : certificate.benefits
    } catch {
      benefitsText = certificate.benefits
    }
  }
  
  certificateForm.value = {
    certificateName: certificate.certificateName,
    description: certificate.description || '',
    priceUsdt: certificate.priceUsdt,
    durationDays: certificate.durationDays,
    benefits: benefitsText,
    maxSupply: certificate.maxSupply || '',
    isActive: certificate.isActive
  }
  showEditModal.value = true
}

const viewCertificate = (certificate) => {
  selectedCertificate.value = certificate
  showViewModal.value = true
}

const saveAddCertificate = async () => {
  isSaving.value = true
  message.value = ''
  
  try {
    // Convert benefits text to JSON array
    const benefitsArray = certificateForm.value.benefits
      .split('\n')
      .map(line => line.trim())
      .filter(line => line.length > 0)
    
    const formData = {
      ...certificateForm.value,
      priceUsdt: certificateForm.value.priceUsdt.toString(),
      durationDays: certificateForm.value.durationDays.toString(),
      maxSupply: certificateForm.value.maxSupply ? certificateForm.value.maxSupply.toString() : null,
      benefits: JSON.stringify(benefitsArray),
      isActive: certificateForm.value.isActive,
      createdBy: '1' // TODO: Get from auth context
    }
    
    await createCertificate(formData)
    message.value = 'Certificate created successfully!'
    messageOk.value = true
    await loadCertificates()
    await loadStats()
    
    setTimeout(() => {
      closeModal()
    }, 1500)
  } catch (error) {
    console.error('Error creating certificate:', error)
    message.value = error.message || 'Failed to create certificate'
    messageOk.value = false
  } finally {
    isSaving.value = false
  }
}

const saveEditCertificate = async () => {
  isSaving.value = true
  message.value = ''
  
  try {
    // Convert benefits text to JSON array
    const benefitsArray = certificateForm.value.benefits
      .split('\n')
      .map(line => line.trim())
      .filter(line => line.length > 0)
    
    const formData = {
      ...certificateForm.value,
      priceUsdt: certificateForm.value.priceUsdt.toString(),
      durationDays: certificateForm.value.durationDays.toString(),
      maxSupply: certificateForm.value.maxSupply ? certificateForm.value.maxSupply.toString() : null,
      benefits: JSON.stringify(benefitsArray),
      isActive: certificateForm.value.isActive
    }
    
    await updateCertificate(selectedCertificate.value.id, formData)
    message.value = 'Certificate updated successfully!'
    messageOk.value = true
    await loadCertificates()
    await loadStats()
    
    setTimeout(() => {
      closeModal()
    }, 1500)
  } catch (error) {
    console.error('Error updating certificate:', error)
    message.value = error.message || 'Failed to update certificate'
    messageOk.value = false
  } finally {
    isSaving.value = false
  }
}

const toggleCertificateStatusLocal = async (certificate) => {
  try {
    await toggleCertificateStatus(certificate.id)
    await loadCertificates()
    await loadStats()
  } catch (error) {
    console.error('Error toggling certificate status:', error)
    alert('Failed to update certificate status')
  }
}

const deleteCertificateLocal = async (certificate) => {
  if (confirm(`Are you sure you want to delete certificate: ${certificate.certificateName}?`)) {
    try {
      await deleteCertificate(certificate.id)
      await loadCertificates()
      await loadStats()
    } catch (error) {
      console.error('Error deleting certificate:', error)
      alert('Failed to delete certificate')
    }
  }
}

const toggleDropdown = (certificateId) => {
  if (openDropdown.value === certificateId) {
    openDropdown.value = null
  } else {
    openDropdown.value = certificateId
  }
}

// Close dropdown when clicking outside
const closeDropdown = () => {
  openDropdown.value = null
}
</script>
