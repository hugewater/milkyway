<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
      <h1 class="text-2xl font-bold text-deep-ocean mb-6">Investment Certificates</h1>
      
      <!-- My Purchased Certificates -->
      <div class="card p-6 rounded-2xl mb-8">
        <h2 class="text-xl font-bold text-deep-ocean mb-6">My Certificate Portfolio</h2>
        
        <!-- Portfolio Summary -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <div class="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-blue-500">
                <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Total Invested</p>
                <p class="text-2xl font-bold text-deep-ocean">${{ totalInvested.toFixed(2) }}</p>
              </div>
            </div>
          </div>

          <div class="bg-gradient-to-r from-yellow-50 to-yellow-100 p-4 rounded-lg">
            <div class="flex items-center">
              <div class="p-3 rounded-full bg-yellow-500">
                <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"></path>
                </svg>
              </div>
              <div class="ml-4">
                <p class="text-sm text-gray-600">Certificates Owned</p>
                <p class="text-2xl font-bold text-deep-ocean">{{ purchasedCertificates.length }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Purchased Certificates List -->
        <div v-if="purchasedCertificates.length > 0">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div v-for="certificate in purchasedCertificates" :key="certificate.id" class="border border-gray-200 rounded-lg p-4">
              <div class="flex items-center justify-between mb-3">
                <h3 class="font-bold text-deep-ocean">{{ certificate.name }}</h3>
                <span :class="`px-2 py-1 text-xs rounded-full ${
                  certificate.status === 'active' ? 'bg-green-100 text-green-800' : 
                  certificate.status === 'matured' ? 'bg-blue-100 text-blue-800' : 'bg-gray-100 text-gray-800'
                }`">
                  {{ certificate.status }}
                </span>
              </div>
              <div class="space-y-2 text-sm">
                <div class="flex justify-between">
                  <span class="text-gray-600">Profit Sharing:</span>
                  <span class="font-medium text-green-600">{{ certificate.profitSharing || 0 }}%</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Purchase Date:</span>
                  <span class="font-medium">{{ certificate.purchaseDate }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Expiration Date:</span>
                  <span class="font-medium">{{ certificate.expirationDate || 'N/A' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-center py-8">
          <div class="text-gray-500 mb-2">
            <svg class="w-16 h-16 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M20 7l-8-4-8 4m16 0v10a2 2 0 01-2 2H6a2 2 0 01-2-2V7m16 0L12 11 4 7"></path>
            </svg>
          </div>
          <p class="text-gray-600">You haven't purchased any certificates yet.</p>
          <p class="text-sm text-gray-500">Browse available certificates below to start investing.</p>
        </div>
      </div>

      <!-- Available Certificates for Sale -->
      <div class="card p-6 rounded-2xl">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-bold text-deep-ocean">Available Investment Certificates</h2>
          <div class="flex space-x-2">
            <select v-model="sortBy" class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
              <option value="name">Sort by Name</option>
              <option value="price">Sort by Price</option>
              <option value="profit">Sort by Profit Sharing</option>
              <option value="duration">Sort by Duration</option>
            </select>
          </div>
        </div>

        <!-- Available Certificates Grid -->
        <div v-if="filteredAvailableCertificates.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="certificate in filteredAvailableCertificates" :key="certificate.id" class="border border-gray-200 rounded-lg p-6 hover:shadow-lg transition-shadow">
            <div class="mb-4">
              <div class="flex items-center justify-between mb-2">
                <h3 class="font-bold text-deep-ocean">{{ certificate.name }}</h3>
                <span class="px-2 py-1 text-xs bg-blue-100 text-blue-800 rounded-full">Available</span>
              </div>
              <p class="text-sm text-gray-600">{{ certificate.description }}</p>
            </div>
            
            <div class="space-y-3 mb-4">
              <div class="flex justify-between">
                <span class="text-gray-600">Price:</span>
                <span class="font-bold text-deep-ocean">${{ certificate.price }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">Profit Sharing:</span>
                <span class="font-medium text-green-600">{{ certificate.profitSharing }}%</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">Duration:</span>
                <span class="font-medium">{{ certificate.duration }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">Available Units:</span>
                <span class="font-medium">{{ certificate.availableUnits }}</span>
              </div>
            </div>
            
            <button 
              @click="purchaseCertificate(certificate)"
              :disabled="certificate.availableUnits === 0"
              class="w-full btn-primary py-2 px-4 rounded-lg font-medium disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ certificate.availableUnits === 0 ? 'Sold Out' : 'Purchase Certificate' }}
            </button>
          </div>
        </div>
        
        <div v-else class="text-center py-8">
          <div class="text-gray-500 mb-2">
            <svg class="w-16 h-16 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M20 7l-8-4-8 4m16 0v10a2 2 0 01-2 2H6a2 2 0 01-2-2V7m16 0L12 11 4 7"></path>
            </svg>
          </div>
          <p class="text-gray-600">No certificates available at the moment.</p>
          <p class="text-sm text-gray-500">Check back later for new investment opportunities.</p>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

// Sample data for purchased certificates
const purchasedCertificates = ref([
  {
    id: 1,
    name: 'Premium Growth Certificate',
    status: 'active',
    profitSharing: 15,
    purchaseDate: '2024-10-15',
    expirationDate: '2025-10-15'
  },
  {
    id: 2,
    name: 'Stable Income Certificate',
    status: 'active',
    profitSharing: 8,
    purchaseDate: '2024-09-20',
    expirationDate: '2025-03-20'
  }
])

// Sample data for available certificates
const availableCertificates = ref([
  {
    id: 3,
    name: 'High Yield Certificate',
    description: 'High-risk, high-reward investment with maximum profit sharing',
    price: 500,
    profitSharing: 25,
    duration: '12 months',
    availableUnits: 5
  },
  {
    id: 4,
    name: 'Balanced Growth Certificate',
    description: 'Moderate risk investment with steady returns',
    price: 300,
    profitSharing: 12,
    duration: '6 months',
    availableUnits: 10
  },
  {
    id: 5,
    name: 'Conservative Income Certificate',
    description: 'Low-risk investment with guaranteed returns',
    price: 200,
    profitSharing: 6,
    duration: '3 months',
    availableUnits: 0
  }
])

const sortBy = ref('name')

// Computed properties
const totalInvested = computed(() => {
  return purchasedCertificates.value.reduce((total, cert) => {
    // Assuming each certificate costs $300 on average
    return total + 300
  }, 0)
})

const filteredAvailableCertificates = computed(() => {
  let filtered = [...availableCertificates.value]
  
  // Sort by selected criteria
  switch (sortBy.value) {
    case 'price':
      filtered.sort((a, b) => a.price - b.price)
      break
    case 'profit':
      filtered.sort((a, b) => b.profitSharing - a.profitSharing)
      break
    case 'duration':
      filtered.sort((a, b) => {
        const durationA = parseInt(a.duration)
        const durationB = parseInt(b.duration)
        return durationA - durationB
      })
      break
    default: // name
      filtered.sort((a, b) => a.name.localeCompare(b.name))
  }
  
  return filtered
})

// Methods
const purchaseCertificate = (certificate) => {
  if (certificate.availableUnits > 0) {
    // Add to purchased certificates
    const newCertificate = {
      id: Date.now(),
      name: certificate.name,
      status: 'active',
      profitSharing: certificate.profitSharing,
      purchaseDate: new Date().toISOString().split('T')[0],
      expirationDate: null // Calculate based on duration
    }
    
    purchasedCertificates.value.push(newCertificate)
    
    // Decrease available units
    certificate.availableUnits--
    
    alert(`Successfully purchased ${certificate.name}!`)
  }
}
</script>
