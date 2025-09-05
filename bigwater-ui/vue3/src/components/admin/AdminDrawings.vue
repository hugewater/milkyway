<template>
  <AppLayout>
    <div class="p-6">
      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div class="flex items-center">
            <div class="p-2 bg-blue-100 rounded-lg">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Total Drawings</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.totalDrawings }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div class="flex items-center">
            <div class="p-2 bg-yellow-100 rounded-lg">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Pending</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.pendingDrawings }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div class="flex items-center">
            <div class="p-2 bg-red-100 rounded-lg">
              <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Cancelled</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.cancelledDrawings }}</p>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div class="flex items-center">
            <div class="p-2 bg-purple-100 rounded-lg">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Completed</p>
              <p class="text-2xl font-semibold text-gray-900">{{ stats.completedDrawings }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Search and Filter -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <div class="flex flex-col md:flex-row gap-4">
          <div class="flex-1">
            <input 
              v-model="searchQuery"
              type="text" 
              placeholder="Search drawings..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
          </div>
          <div class="flex gap-2">
            <select 
              v-model="statusFilter"
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="">All Status</option>
              <option value="PENDING">Pending</option>
              <option value="COMPLETED">Completed</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
            <button 
              @click="createDrawing"
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
            >
              Create Drawing
            </button>
          </div>
        </div>
      </div>

      <!-- Drawings Table -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Drawing</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Prize Pool</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Draw Date</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="drawing in filteredDrawings" :key="drawing.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <div class="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center">
                        <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"></path>
                        </svg>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ drawing.name }}</div>
                      <div class="text-sm text-gray-500">{{ drawing.id }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ drawing.prizePool }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getStatusClass(drawing.status)" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                    {{ drawing.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ formatDate(drawing.drawDate) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button @click="viewDrawing(drawing)" class="text-blue-600 hover:text-blue-900 mr-3">View</button>
                  <button @click="editDrawing(drawing)" class="text-green-600 hover:text-green-900 mr-3">Edit</button>
                  <button @click="deleteDrawing(drawing)" class="text-red-600 hover:text-red-900">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Create Drawing Modal -->
    <div v-if="showCreateModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-gray-900 mb-4">Create New Drawing</h3>
        <form @submit.prevent="submitCreateDrawing">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Drawing Name</label>
              <input
                v-model="newDrawing.name"
                type="text"
                required
                placeholder="Enter drawing name"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Drawing Type</label>
              <select
                v-model="newDrawing.drawingType"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="SWEEPSTAKES">Sweepstakes</option>
                <option value="RAFFLE">Raffle</option>
                <option value="CONTEST">Contest</option>
              </select>
            </div>
            


            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Prize Pool</label>
              <input
                v-model="newDrawing.prizePool"
                type="number"
                required
                min="0"
                step="0.01"
                placeholder="Enter prize pool amount"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Draw Date</label>
              <input
                v-model="newDrawing.drawDate"
                type="datetime-local"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Winning Numbers</label>
              <div class="space-y-2">
                <div class="flex gap-2 items-center justify-between">
                  <label class="text-xs text-gray-600">Main Numbers (1-69):</label>
                  <button
                    type="button"
                    @click="generateRandomNumbers"
                    class="text-xs bg-blue-100 text-blue-600 px-2 py-1 rounded hover:bg-blue-200"
                  >
                    Generate Random
                  </button>
                </div>
                <div class="flex gap-2">
                  <input
                    v-model="newDrawing.number1"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="1"
                    @input="validateNumber(1)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="newDrawing.number2"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="2"
                    @input="validateNumber(2)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="newDrawing.number3"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="3"
                    @input="validateNumber(3)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="newDrawing.number4"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="4"
                    @input="validateNumber(4)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="newDrawing.number5"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="5"
                    @input="validateNumber(5)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                </div>
                <div class="flex gap-2 items-center">
                  <label class="text-xs text-gray-600">Power Ball (1-26):</label>
                  <input
                    v-model="newDrawing.powerBall"
                    type="number"
                    min="1"
                    max="26"
                    required
                    placeholder="1"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                </div>
              </div>
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              @click="showCreateModal = false"
              class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
            >
              Create Drawing
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- View Drawing Modal -->
    <div v-if="showViewModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-gray-900 mb-4">View Drawing Details</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Drawing Name</label>
            <p class="text-sm text-gray-900 bg-gray-50 px-3 py-2 rounded-lg">{{ viewingDrawing.name }}</p>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Prize Pool</label>
            <p class="text-sm text-gray-900 bg-gray-50 px-3 py-2 rounded-lg">{{ viewingDrawing.prizePool }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Draw Date</label>
            <p class="text-sm text-gray-900 bg-gray-50 px-3 py-2 rounded-lg">{{ formatDate(viewingDrawing.drawDate) }}</p>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <span :class="getStatusClass(viewingDrawing.status)" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
              {{ viewingDrawing.status }}
            </span>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Winning Numbers</label>
            <div class="space-y-2">
              <div class="flex gap-2">
                <label class="text-xs text-gray-600">Main Numbers:</label>
              </div>
              <div class="flex gap-2">
                <span v-for="(num, index) in viewingDrawing.winningNumbers?.split(',')?.slice(0, 5)" :key="index" 
                      class="w-12 h-8 bg-blue-100 text-blue-800 rounded-lg flex items-center justify-center text-sm font-semibold">
                  {{ num?.replace('[', '').replace(' ', '') }}
                </span>
              </div>
              <div class="flex gap-2 items-center">
                <label class="text-xs text-gray-600">Power Ball:</label>
                <span class="w-12 h-8 bg-red-100 text-red-800 rounded-lg flex items-center justify-center text-sm font-semibold">
                  {{ viewingDrawing.winningNumbers?.split(',')?.slice(-1)[0]?.replace(']', '').replace(' ', '') }}
                </span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="flex justify-end space-x-3 mt-6">
          <button
            @click="showViewModal = false"
            class="px-4 py-2 text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
          >
            Close
          </button>
        </div>
      </div>
    </div>

    <!-- Edit Drawing Modal -->
    <div v-if="showEditModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md mx-4">
        <h3 class="text-lg font-bold text-gray-900 mb-4">Edit Drawing</h3>
        <form @submit.prevent="submitEditDrawing">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Drawing Name</label>
              <input
                v-model="editingDrawing.name"
                type="text"
                required
                placeholder="Enter drawing name"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Drawing Type</label>
              <select
                v-model="editingDrawing.drawingType"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="SWEEPSTAKES">Sweepstakes</option>
                <option value="RAFFLE">Raffle</option>
                <option value="CONTEST">Contest</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Prize Pool</label>
              <input
                v-model="editingDrawing.prizePool"
                type="number"
                required
                min="0"
                step="0.01"
                placeholder="Enter prize pool amount"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Draw Date</label>
              <input
                v-model="editingDrawing.drawDate"
                type="datetime-local"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
              <select
                v-model="editingDrawing.status"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="PENDING">Pending</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Winning Numbers</label>
              <div class="space-y-2">
                <div class="flex gap-2">
                  <label class="text-xs text-gray-600">Main Numbers (1-69):</label>
                </div>
                <div class="flex gap-2">
                  <input
                    v-model="editingDrawing.number1"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="1"
                    @input="validateEditNumber(1)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="editingDrawing.number2"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="2"
                    @input="validateEditNumber(2)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="editingDrawing.number3"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="3"
                    @input="validateEditNumber(3)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="editingDrawing.number4"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="4"
                    @input="validateEditNumber(4)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                  <input
                    v-model="editingDrawing.number5"
                    type="number"
                    min="1"
                    max="69"
                    required
                    placeholder="5"
                    @input="validateEditNumber(5)"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                </div>
                <div class="flex gap-2 items-center">
                  <label class="text-xs text-gray-600">Power Ball (1-26):</label>
                  <input
                    v-model="editingDrawing.powerBall"
                    type="number"
                    min="1"
                    max="26"
                    required
                    placeholder="1"
                    class="w-12 px-2 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-center"
                  />
                </div>
              </div>
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
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
            >
              Update Drawing
            </button>
          </div>
        </form>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'

const searchQuery = ref('')
const statusFilter = ref('')
const showCreateModal = ref(false)
const showViewModal = ref(false)
const showEditModal = ref(false)

const viewingDrawing = ref({})
const editingDrawing = ref({})

const newDrawing = ref({
  name: '',
  drawingType: 'SWEEPSTAKES',
  prizePool: '',
  drawDate: '',
  number1: '',
  number2: '',
  number3: '',
  number4: '',
  number5: '',
  powerBall: ''
})

const stats = ref({
  totalDrawings: 0,
  activeDrawings: 0,
  scheduledDrawings: 0,
  completedDrawings: 0
})

const drawings = ref([])

onMounted(() => {
  loadDrawings()
})

const loadDrawings = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      console.log('No token found, using mock data')
      drawings.value = [
        {
          id: 'DRAW001',
          name: 'Weekly Lottery',
          prizePool: '$10,000',
          status: 'PENDING',
          drawDate: '2024-01-15'
        },
        {
          id: 'DRAW002',
          name: 'Monthly Jackpot',
          prizePool: '$50,000',
          status: 'COMPLETED',
          drawDate: '2024-02-01'
        }
      ]
      return
    }
    
    console.log('Token found, loading from API')
    
    const response = await fetch('/bw-api/drawings', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    const result = await response.json()
    
    if (result.success) {
      drawings.value = result.data.map(drawing => ({
        id: drawing.id,
        name: drawing.drawingName,
        prizePool: `$${parseFloat(drawing.prizePool).toLocaleString()}`,
        status: drawing.status,
        drawDate: drawing.drawingDate,
        winningNumbers: drawing.winningNumbers,
        powerBall: drawing.powerBall
      }))
            console.log('Drawings loaded successfully:', drawings.value.length)
    } else {
      console.error('Failed to load drawings:', result.error)
      throw new Error(result.error)
    }
  } catch (error) {
    console.error('Error loading drawings:', error)
    // Fallback to mock data
    drawings.value = [
              {
          id: 'DRAW001',
          name: 'Weekly Sweepstakes',
          type: 'SWEEPSTAKES',
          prizePool: '$10,000',
          status: 'PENDING',
          drawDate: '2024-01-15'
        },
        {
          id: 'DRAW002',
          name: 'Monthly Contest',
          type: 'CONTEST',
          prizePool: '$50,000',
          status: 'COMPLETED',
          drawDate: '2024-02-01'
        }
    ]
  }
  
  // Load stats after drawings are loaded
  loadStats()
}

const loadStats = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      // Fallback to mock data
      stats.value = {
        totalDrawings: 2,
        pendingDrawings: 1,
        completedDrawings: 1,
        cancelledDrawings: 0
      }
      return
    }
    
    // Calculate stats from drawings data
    const total = drawings.value.length
    const pending = drawings.value.filter(d => d.status === 'PENDING').length
    const completed = drawings.value.filter(d => d.status === 'COMPLETED').length
    const cancelled = drawings.value.filter(d => d.status === 'CANCELLED').length
    
    stats.value = {
      totalDrawings: total,
      pendingDrawings: pending,
      completedDrawings: completed,
      cancelledDrawings: cancelled
    }
  } catch (error) {
    console.error('Error loading stats:', error)
    // Fallback to mock data
    stats.value = {
      totalDrawings: 2,
      pendingDrawings: 1,
      completedDrawings: 1,
      cancelledDrawings: 0
    }
  }
}

const filteredDrawings = computed(() => {
  let filtered = drawings.value

  if (searchQuery.value) {
    filtered = filtered.filter(drawing => 
      drawing.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      drawing.id.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  if (statusFilter.value) {
    filtered = filtered.filter(drawing => drawing.status === statusFilter.value)
  }

  return filtered
})

const getStatusClass = (status) => {
  switch (status) {
    case 'PENDING':
      return 'bg-yellow-100 text-yellow-800'
    case 'COMPLETED':
      return 'bg-green-100 text-green-800'
    case 'CANCELLED':
      return 'bg-red-100 text-red-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString()
}

const createDrawing = () => {
  showCreateModal.value = true
}

const validateNumber = (position) => {
  const value = newDrawing.value[`number${position}`]
  if (value) {
    const num = parseInt(value)
    if (num < 1 || num > 69) {
      newDrawing.value[`number${position}`] = ''
      alert(`Number ${position} must be between 1 and 69.`)
    }
  }
}

const validateEditNumber = (position) => {
  const value = editingDrawing.value[`number${position}`]
  if (value) {
    const num = parseInt(value)
    if (num < 1 || num > 69) {
      editingDrawing.value[`number${position}`] = ''
      alert(`Number ${position} must be between 1 and 69.`)
    }
  }
}

const generateRandomNumbers = () => {
  // Generate 5 unique random numbers between 1-69
  const numbers = []
  while (numbers.length < 5) {
    const num = Math.floor(Math.random() * 69) + 1
    if (!numbers.includes(num)) {
      numbers.push(num)
    }
  }
  
  // Sort numbers in ascending order
  numbers.sort((a, b) => a - b)
  
  // Fill the form fields
  newDrawing.value.number1 = numbers[0]
  newDrawing.value.number2 = numbers[1]
  newDrawing.value.number3 = numbers[2]
  newDrawing.value.number4 = numbers[3]
  newDrawing.value.number5 = numbers[4]
  
  // Generate random power ball (1-26)
  newDrawing.value.powerBall = Math.floor(Math.random() * 26) + 1
}

const submitCreateDrawing = async () => {
  try {
    // Validate numbers
    const numbers = [
      newDrawing.value.number1,
      newDrawing.value.number2,
      newDrawing.value.number3,
      newDrawing.value.number4,
      newDrawing.value.number5
    ]
    
    // Check for duplicates
    const uniqueNumbers = new Set(numbers)
    if (uniqueNumbers.size !== 5) {
      alert('Please enter 5 different numbers for the main numbers.')
      return
    }
    
    // Validate number ranges
    for (let i = 0; i < numbers.length; i++) {
      const num = parseInt(numbers[i])
      if (num < 1 || num > 69) {
        alert(`Number ${i + 1} must be between 1 and 69.`)
        return
      }
    }
    
    const powerBall = parseInt(newDrawing.value.powerBall)
    if (powerBall < 1 || powerBall > 26) {
      alert('Power Ball must be between 1 and 26.')
      return
    }
    
    // Create drawing data
    const drawingData = {
      name: newDrawing.value.name,
      drawingType: newDrawing.value.drawingType,
      prizePool: parseFloat(newDrawing.value.prizePool),
      drawDate: newDrawing.value.drawDate,
      winningNumbers: numbers.join(','),
      powerBall: powerBall
    }
    
    console.log('Creating drawing with data:', drawingData)
    
    // Call API to create drawing
    const response = await fetch('/bw-api/drawings', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(drawingData)
    })
    
    const result = await response.json()
    
    if (result.success) {
      // Add to local array
      const newDrawingData = {
        id: result.data.id,
        name: result.data.drawingName,
        prizePool: `$${parseFloat(result.data.prizePool).toLocaleString()}`,
        status: result.data.status,
        drawDate: result.data.drawingDate,
        winningNumbers: result.data.winningNumbers,
        powerBall: result.data.powerBall
      }
      
      drawings.value.push(newDrawingData)
    } else {
      throw new Error(result.error || 'Failed to create drawing')
    }
    
    // Reset form and close modal
    newDrawing.value = {
      name: '',
      drawingType: 'SWEEPSTAKES',
      prizePool: '',
      drawDate: '',
      number1: '',
      number2: '',
      number3: '',
      number4: '',
      number5: '',
      powerBall: ''
    }
    showCreateModal.value = false
    
    // Update stats after creating drawing
    loadStats()
    
    // Show success message
    alert('Drawing created successfully!')
    
  } catch (error) {
    console.error('Failed to create drawing:', error)
    alert('Failed to create drawing. Please try again.')
  }
}

const submitEditDrawing = async () => {
  try {
    // Validate numbers
    const numbers = [
      editingDrawing.value.number1,
      editingDrawing.value.number2,
      editingDrawing.value.number3,
      editingDrawing.value.number4,
      editingDrawing.value.number5
    ]
    
    // Check for duplicates
    const uniqueNumbers = new Set(numbers)
    if (uniqueNumbers.size !== 5) {
      alert('Please enter 5 different numbers for the main numbers.')
      return
    }
    
    // Validate number ranges
    for (let i = 0; i < numbers.length; i++) {
      const num = parseInt(numbers[i])
      if (num < 1 || num > 69) {
        alert(`Number ${i + 1} must be between 1 and 69.`)
        return
      }
    }
    
    const powerBall = parseInt(editingDrawing.value.powerBall)
    if (powerBall < 1 || powerBall > 26) {
      alert('Power Ball must be between 1 and 26.')
      return
    }
    
    // Create drawing data
    const drawingData = {
      name: editingDrawing.value.name,
      prizePool: parseFloat(editingDrawing.value.prizePool),
      drawDate: editingDrawing.value.drawDate,
      winningNumbers: numbers.join(','),
      powerBall: powerBall,
      status: editingDrawing.value.status.toUpperCase()
    }
    
    console.log('Updating drawing with data:', drawingData)
    
    // Call API to update drawing
    const response = await fetch(`/bw-api/drawings/${editingDrawing.value.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(drawingData)
    })
    
    const result = await response.json()
    
    if (result.success) {
      // Update local array
      const index = drawings.value.findIndex(d => d.id === editingDrawing.value.id)
      if (index !== -1) {
        drawings.value[index] = {
          id: editingDrawing.value.id,
          name: drawingData.name,
          prizePool: `$${drawingData.prizePool.toLocaleString()}`,
          status: drawingData.status,
          drawDate: drawingData.drawDate,
          winningNumbers: `[${drawingData.winningNumbers},${drawingData.powerBall}]`,
          powerBall: drawingData.powerBall
        }
      }
      
      showEditModal.value = false
      
      // Update stats after editing drawing
      loadStats()
      
      alert('Drawing updated successfully!')
    } else {
      throw new Error(result.error || 'Failed to update drawing')
    }
    
  } catch (error) {
    console.error('Failed to update drawing:', error)
    alert('Failed to update drawing. Please try again.')
  }
}

const viewDrawing = (drawing) => {
  viewingDrawing.value = { ...drawing }
  showViewModal.value = true
}

const editDrawing = (drawing) => {
  // Parse winning numbers from the stored format
  const numbers = drawing.winningNumbers?.replace('[', '').replace(']', '').split(',').map(n => n.trim()) || []
  
  editingDrawing.value = {
    id: drawing.id,
    name: drawing.name,
    prizePool: drawing.prizePool.replace('$', '').replace(',', ''),
    drawDate: drawing.drawDate,
    status: drawing.status,
    number1: numbers[0] || '',
    number2: numbers[1] || '',
    number3: numbers[2] || '',
    number4: numbers[3] || '',
    number5: numbers[4] || '',
    powerBall: numbers[5] || ''
  }
  showEditModal.value = true
}

const deleteDrawing = async (drawing) => {
  if (confirm(`Are you sure you want to delete drawing: ${drawing.name}?`)) {
    try {
      const response = await fetch(`/bw-api/drawings/${drawing.id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      
      const result = await response.json()
      
      if (result.success) {
        // Remove from local array
        const index = drawings.value.findIndex(d => d.id === drawing.id)
        if (index !== -1) {
          drawings.value.splice(index, 1)
        }
        
        // Update stats after deleting drawing
        loadStats()
        
        alert(`Drawing ${drawing.name} deleted successfully.`)
      } else {
        throw new Error(result.error || 'Failed to delete drawing')
      }
    } catch (error) {
      console.error('Failed to delete drawing:', error)
      alert('Failed to delete drawing. Please try again.')
    }
  }
}
</script>