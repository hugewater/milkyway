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
                <h3 class="text-lg font-bold text-deep-ocean">{{ certificate.name }}</h3>
              </div>
              <p class="text-sm text-gray-600 mb-3">{{ certificate.description }}</p>
              
              <div class="space-y-2 text-sm mb-4">
                <div class="flex justify-between">
                  <span class="text-gray-600">Price per Certificate:</span>
                  <span class="font-bold text-deep-ocean">${{ certificate.pricePerCertificate.toFixed(2) }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-600">Minimum Purchase:</span>
                  <span class="font-medium">${{ certificate.minimumPurchase.toFixed(2) }}</span>
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
                  <span class="text-gray-600">Available:</span>
                  <span class="font-medium">{{ certificate.availableCertificates.toLocaleString() }} certificates</span>
                </div>
              </div>

              <!-- Certificate Details -->
              <div class="bg-gray-50 rounded-lg p-3 mb-4">
                <h4 class="text-sm font-semibold text-gray-700 mb-2">Key Features:</h4>
                <ul class="text-xs text-gray-600 space-y-1">
                  <li v-for="feature in certificate.features" :key="feature" class="flex items-center">
                    <svg class="w-3 h-3 text-green-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                    </svg>
                    {{ feature }}
                  </li>
                </ul>
              </div>

              <!-- Purchase Section -->
              <div class="border-t pt-4">
                <div class="mb-3">
                  <label class="block text-sm font-medium text-gray-700 mb-1">Quantity to Purchase</label>
                  <input 
                    v-model.number="certificate.purchaseQuantity" 
                    type="number" 
                    :min="Math.ceil(certificate.minimumPurchase / certificate.pricePerCertificate)"
                    :max="certificate.availableCertificates"
                    class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"
                    :placeholder="`Min: ${Math.ceil(certificate.minimumPurchase / certificate.pricePerCertificate)} certificates`"
                  >
                </div>
                <div class="flex justify-between text-sm mb-3">
                  <span>Total Cost:</span>
                  <span class="font-bold text-deep-ocean">
                    ${{ ((certificate.purchaseQuantity || 0) * certificate.pricePerCertificate).toFixed(2) }}
                  </span>
                </div>
                <button 
                  @click="purchaseCertificate(certificate)" 
                  :disabled="!certificate.purchaseQuantity || certificate.purchaseQuantity < Math.ceil(certificate.minimumPurchase / certificate.pricePerCertificate) || certificate.purchaseQuantity > certificate.availableCertificates"
                  class="w-full btn-primary py-2 px-4 rounded-lg font-medium disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Purchase Certificates
                </button>
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
          <p class="text-gray-600">No active certificates available for purchase.</p>
          <p class="text-sm text-gray-500">Please check back later or contact admin.</p>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

// User's purchased certificates
const purchasedCertificates = ref([])

// Available certificates - fetched from admin-created certificates (active only)
const availableCertificates = ref([])

// Filters and sorting
const sortBy = ref('name')

// Computed properties
const totalInvested = computed(() => {
  return purchasedCertificates.value.reduce((sum, certificate) => sum + (certificate.purchasePrice || 0), 0)
})

const filteredAvailableCertificates = computed(() => {
  let filtered = availableCertificates.value

  // Apply sorting
  switch (sortBy.value) {
    case 'price':
      return filtered.sort((a, b) => a.pricePerCertificate - b.pricePerCertificate)
    case 'profit':
      return filtered.sort((a, b) => b.profitSharing - a.profitSharing)
    case 'duration':
      return filtered.sort((a, b) => {
        const aDuration = parseInt(a.duration)
        const bDuration = parseInt(b.duration)
        return aDuration - bDuration
      })
    default:
      return filtered.sort((a, b) => a.name.localeCompare(b.name))
  }
})

// Methods
const purchaseCertificate = (certificate) => {
  if (!certificate.purchaseQuantity || certificate.purchaseQuantity < Math.ceil(certificate.minimumPurchase / certificate.pricePerCertificate)) {
    alert(`Minimum purchase is ${Math.ceil(certificate.minimumPurchase / certificate.pricePerCertificate)} certificates`)
    return
  }

  if (certificate.purchaseQuantity > certificate.availableCertificates) {
    alert(`Only ${certificate.availableCertificates} certificates available`)
    return
  }

  const totalCost = certificate.purchaseQuantity * certificate.pricePerCertificate
  const confirmed = confirm(`Purchase ${certificate.purchaseQuantity} certificates of "${certificate.name}" for $${totalCost.toFixed(2)}?`)
  
  if (confirmed) {
    // Calculate expiration date based on duration
    const purchaseDate = new Date()
    const expirationDate = new Date(purchaseDate)
    const durationMonths = parseInt(certificate.duration) || 12
    expirationDate.setMonth(expirationDate.getMonth() + durationMonths)

    // Add to purchased certificates
    const newPurchase = {
      id: Date.now(),
      name: certificate.name,
      quantity: certificate.purchaseQuantity,
      purchasePrice: totalCost,
      profitSharing: certificate.profitSharing,
      purchaseDate: purchaseDate.toISOString().split('T')[0],
      expirationDate: expirationDate.toISOString().split('T')[0],
      status: 'active'
    }
    
    purchasedCertificates.value.push(newPurchase)
    
    // Update available certificates
    certificate.availableCertificates -= certificate.purchaseQuantity
    certificate.purchaseQuantity = null
    
    // Save purchase to localStorage
    const existingPurchases = JSON.parse(localStorage.getItem('userPurchasedCertificates') || '[]')
    existingPurchases.push(newPurchase)
    localStorage.setItem('userPurchasedCertificates', JSON.stringify(existingPurchases))
    
    alert(`Successfully purchased ${newPurchase.quantity} certificates of "${certificate.name}"!`)
  }
}

// Load active certificates created by admin
const loadActiveCertificates = () => {
  try {
    // Get admin-created certificates from localStorage
    const adminCertificates = JSON.parse(localStorage.getItem('certificates') || '[]')
    
    // Filter to only active certificates and add purchase-related fields
    const activeCertificates = adminCertificates
      .filter(cert => cert.status === 'active')
      .map(cert => ({
        ...cert,
        purchaseQuantity: null,
        // Ensure required fields exist with proper mapping
        pricePerCertificate: cert.price || cert.pricePerCertificate || 0,
        availableCertificates: cert.totalSupply || cert.availableCertificates || 1000,
        profitSharing: cert.profitSharing || 0,
        minimumPurchase: cert.minimumInvestment || cert.minimumPurchase || (cert.price || 0) * 4,
        features: cert.features || [
          'Professional fund management',
          'Regular profit distributions', 
          'Transparent reporting',
          'Risk management protocols'
        ]
      }))
    
    // Update the availableCertificates array
    availableCertificates.value = activeCertificates
    console.log('Loaded active certificates:', activeCertificates)
  } catch (error) {
    console.error('Error loading certificates:', error)
    availableCertificates.value = []
  }
}

// Load user's purchased certificates
const loadPurchasedCertificates = () => {
  try {
    const purchases = JSON.parse(localStorage.getItem('userPurchasedCertificates') || '[]')
    purchasedCertificates.value = purchases
  } catch (error) {
    console.error('Error loading purchased certificates:', error)
    purchasedCertificates.value = []
  }
}

// Load certificates when component mounts
onMounted(() => {
  loadActiveCertificates()
  loadPurchasedCertificates()
})
</script>