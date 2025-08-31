<template>
  <AppLayout>
    <div class="p-4 lg:p-6">
    <div class="flex items-center justify-between mb-6">
      <button @click="showAddModal = true" class="btn-primary">
        Create New Journal
      </button>
    </div>

    <!-- Journal Statistics -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-blue-100">
            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Total Journals</p>
            <p class="text-2xl font-bold text-deep-ocean">{{ journals.length }}</p>

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
            <p class="text-sm text-gray-600">Published</p>
            <p class="text-2xl font-bold text-deep-ocean">{{ publishedJournals.length }}</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-yellow-100">
            <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Drafts</p>
            <p class="text-2xl font-bold text-deep-ocean">{{ draftJournals.length }}</p>
          </div>
        </div>
      </div>

      <div class="card p-6 rounded-2xl">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-purple-100">
            <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm text-gray-600">Total Views</p>
            <p class="text-2xl font-bold text-deep-ocean">12,847</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Filters and Search -->
    <div class="card p-6 rounded-2xl mb-6">
      <div class="flex flex-col md:flex-row md:items-center md:justify-between space-y-4 md:space-y-0 md:space-x-4">
        <div class="flex space-x-4">
          <select v-model="statusFilter" class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
            <option value="">All Status</option>
            <option value="PUBLISHED">Published</option>
            <option value="DRAFT">Draft</option>
            <option value="SCHEDULED">Scheduled</option>
          </select>
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Search journals..." 
            class="border border-gray-300 rounded-lg px-3 py-2 text-sm w-64"
          >
        </div>
        <div class="flex items-center space-x-2">
          <span class="text-sm text-gray-600">Week Range:</span>
          <input v-model="weekStart" type="date" class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
          <span class="text-sm text-gray-600">to</span>
          <input v-model="weekEnd" type="date" class="border border-gray-300 rounded-lg px-3 py-2 text-sm">
        </div>
      </div>
    </div>

    <!-- Journals List -->
    <div class="card p-6 rounded-2xl">
      <div class="flex items-center justify-between mb-6">
        <h3 class="text-lg font-bold text-deep-ocean">Weekly Journals</h3>
        <div class="flex space-x-2">
          <button 
            :class="`px-3 py-1 rounded-lg text-sm ${viewMode === 'grid' ? 'bg-ocean text-white' : 'bg-gray-200 text-gray-700'}`"
            @click="viewMode = 'grid'"
          >
            Grid
          </button>
          <button 
            :class="`px-3 py-1 rounded-lg text-sm ${viewMode === 'list' ? 'bg-ocean text-white' : 'bg-gray-200 text-gray-700'}`"
            @click="viewMode = 'list'"
          >
            List
          </button>
        </div>
      </div>

      <!-- Grid View -->
      <div v-if="viewMode === 'grid'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="journal in filteredJournals" :key="journal.id" class="border border-gray-200 rounded-lg p-4 hover:shadow-lg transition-shadow">
          <div class="flex items-center justify-between mb-3">
            <span :class="`px-2 py-1 rounded-full text-xs font-medium ${
              journal.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' : 
              journal.status === 'DRAFT' ? 'bg-yellow-100 text-yellow-800' : 'bg-blue-100 text-blue-800'
            }`">
              {{ journal.status }}
            </span>
            <span class="text-xs text-gray-500">Week {{ journal.weekNumber }}</span>
          </div>
          
          <h4 class="font-semibold text-deep-ocean mb-2 line-clamp-2">{{ journal.title }}</h4>
          <p class="text-sm text-gray-600 mb-3 line-clamp-3">{{ journal.excerpt }}</p>
          
          <div class="flex items-center justify-between text-xs text-gray-500 mb-3">
            <span>{{ formatDate(journal.publishDate) }}</span>
            <span>{{ journal.views }} views</span>
          </div>
          
          <div class="flex space-x-2">
            <button @click="editJournal(journal)" class="flex-1 text-blue-600 hover:text-blue-800 text-sm py-1">
              Edit
            </button>
            <button 
              @click="togglePublishStatus(journal)" 
              :class="`flex-1 text-sm py-1 ${journal.status === 'PUBLISHED' ? 'text-orange-600 hover:text-orange-800' : 'text-green-600 hover:text-green-800'}`"
            >
              {{ journal.status === 'PUBLISHED' ? 'Unpublish' : 'Publish' }}
            </button>
                            <button @click="deleteJournalItem(journal)" class="flex-1 text-red-600 hover:text-red-800 text-sm py-1">
              Delete
            </button>
          </div>
        </div>
      </div>

      <!-- List View -->
      <div v-if="viewMode === 'list'" class="overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="border-b border-gray-200">
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Week</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Title</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Status</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Publish Date</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Views</th>
              <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="journal in filteredJournals" :key="journal.id" class="border-b border-gray-100 hover:bg-gray-50">
              <td class="py-3 px-4 text-sm font-medium text-deep-ocean">Week {{ journal.weekNumber }}</td>
              <td class="py-3 px-4">
                <div>
                  <p class="text-sm font-medium text-gray-900">{{ journal.title }}</p>
                  <p class="text-xs text-gray-500">{{ journal.excerpt.substring(0, 100) }}...</p>
                </div>
              </td>
              <td class="py-3 px-4">
                <span :class="`px-2 py-1 rounded-full text-xs font-medium ${
                  journal.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' : 
                  journal.status === 'DRAFT' ? 'bg-yellow-100 text-yellow-800' : 'bg-blue-100 text-blue-800'
                }`">
                  {{ journal.status }}
                </span>
              </td>
              <td class="py-3 px-4 text-sm text-gray-900">{{ formatDate(journal.publishDate) }}</td>
              <td class="py-3 px-4 text-sm text-gray-900">{{ journal.views }}</td>
              <td class="py-3 px-4">
                <div class="flex space-x-2">
                  <button @click="editJournal(journal)" class="text-blue-600 hover:text-blue-800 text-sm">
                    Edit
                  </button>
                  <button 
                    @click="togglePublishStatus(journal)" 
                    :class="`text-sm ${journal.status === 'PUBLISHED' ? 'text-orange-600 hover:text-orange-800' : 'text-green-600 hover:text-green-800'}`"
                  >
                    {{ journal.status === 'PUBLISHED' ? 'Unpublish' : 'Publish' }}
                  </button>
                  <button @click="deleteJournalItem(journal)" class="text-red-600 hover:text-red-800 text-sm">
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Add/Edit Journal Modal -->
    <div v-if="showAddModal || editingJournal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl p-6 w-full max-w-4xl max-h-[90vh] overflow-y-auto">
        <h3 class="text-lg font-bold text-deep-ocean mb-4">
          {{ editingJournal ? 'Edit Journal' : 'Create New Journal' }}
        </h3>
        
        <form @submit.prevent="saveJournal" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Week Number *</label>
              <input v-model="journalForm.weekNumber" type="number" required class="w-full border border-gray-300 rounded-lg px-3 py-2">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Publish Date *</label>
              <input v-model="journalForm.publishDate" type="date" required class="w-full border border-gray-300 rounded-lg px-3 py-2">
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Title *</label>
            <input v-model="journalForm.title" type="text" required class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="Weekly Journal Title">
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Excerpt</label>
            <textarea v-model="journalForm.excerpt" rows="2" class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="Brief summary for preview..."></textarea>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Content *</label>
            <textarea v-model="journalForm.content" rows="12" required class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="Write your journal content here..."></textarea>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
              <select v-model="journalForm.status" class="w-full border border-gray-300 rounded-lg px-3 py-2">
                <option value="DRAFT">Draft</option>
                <option value="PUBLISHED">Published</option>
                <option value="SCHEDULED">Scheduled</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Featured</label>
              <select v-model="journalForm.featured" class="w-full border border-gray-300 rounded-lg px-3 py-2">
                <option :value="false">No</option>
                <option :value="true">Yes</option>
              </select>
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tags</label>
            <input v-model="journalForm.tags" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2" placeholder="Comma separated tags">
          </div>
          
          <div class="flex space-x-3 pt-4">
            <button type="submit" class="flex-1 btn-primary">
              {{ editingJournal ? 'Update Journal' : 'Create Journal' }}
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
import { ref, computed, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import { getJournals, createJournal, updateJournal, deleteJournal } from '../../utils/api.js'

const showAddModal = ref(false)
const editingJournal = ref(null)
const statusFilter = ref('')
const searchQuery = ref('')
const weekStart = ref('')
const weekEnd = ref('')
const viewMode = ref('grid')

const journalForm = ref({
  weekNumber: '',
  title: '',
  excerpt: '',
  content: '',
  publishDate: '',
  status: 'DRAFT',
  featured: false,
  tags: ''
})

// Real journal data from API
const journals = ref([])

// Load journals from API
const loadJournals = async () => {
  try {
    const response = await getJournals()
    if (response.success) {
      journals.value = response.data || []
    } else {
      console.error('Failed to load journals:', response.message)
    }
  } catch (error) {
    console.error('Error loading journals:', error)
  }
}

// Load journals on component mount
onMounted(() => {
  loadJournals()
})

const publishedJournals = computed(() => journals.value.filter(j => j.status === 'PUBLISHED'))
const draftJournals = computed(() => journals.value.filter(j => j.status === 'DRAFT'))

const filteredJournals = computed(() => {
  let filtered = journals.value

  if (statusFilter.value) {
    const filterStatus = statusFilter.value.toUpperCase()
    filtered = filtered.filter(j => j.status === filterStatus)
  }

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(j => 
      j.title.toLowerCase().includes(query) || 
      j.excerpt.toLowerCase().includes(query) ||
      j.tags.toLowerCase().includes(query)
    )
  }

  if (weekStart.value && weekEnd.value) {
    filtered = filtered.filter(j => {
      if (!j.publishDate) return false
      const base = typeof j.publishDate === 'string' && j.publishDate.length > 10
        ? j.publishDate.slice(0, 10)
        : j.publishDate
      const publishDate = new Date(base)
      const start = new Date(weekStart.value)
      const end = new Date(weekEnd.value)
      return !isNaN(publishDate) && publishDate >= start && publishDate <= end
    })
  }

  return filtered.sort((a, b) => b.weekNumber - a.weekNumber)
})

const formatDate = (dateString) => {
  if (!dateString) return ''
  const normalized = typeof dateString === 'string' && dateString.length > 10
    ? dateString.slice(0, 10)
    : dateString
  return normalized
}

const editJournal = (journal) => {
  editingJournal.value = journal
  journalForm.value = {
    weekNumber: journal.weekNumber,
    title: journal.title,
    excerpt: journal.excerpt,
    content: journal.content,
    publishDate: journal.publishDate ? String(journal.publishDate).slice(0, 10) : '',
    status: journal.status,
    featured: journal.featured,
    tags: journal.tags
  }
}

const closeModal = () => {
  showAddModal.value = false
  editingJournal.value = null
  journalForm.value = {
    weekNumber: '',
    title: '',
    excerpt: '',
    content: '',
    publishDate: '',
    status: 'DRAFT',
    featured: false,
    tags: ''
  }
}

const saveJournal = async () => {
  try {
    const payload = {
      ...journalForm.value,
      publishDate: journalForm.value.publishDate ? `${journalForm.value.publishDate}T00:00:00` : null
    }
    if (editingJournal.value) {
      // Update existing journal
      const response = await updateJournal(editingJournal.value.id, payload)
      if (response.success) {
        await loadJournals()
        alert('Journal updated successfully!')
      } else {
        alert('Failed to update journal: ' + response.message)
      }
    } else {
      // Create new journal
      const response = await createJournal(payload)
      if (response.success) {
        await loadJournals()
        alert('Journal created successfully!')
      } else {
        alert('Failed to create journal: ' + response.message)
      }
    }
    
    closeModal()
  } catch (error) {
    console.error('Error saving journal:', error)
    alert('Error saving journal. Please try again.')
  }
}

const togglePublishStatus = (journal) => {
  if (journal.status === 'PUBLISHED') {
    journal.status = 'DRAFT'
  } else {
    journal.status = 'PUBLISHED'
    const today = new Date()
    const yyyy = today.getFullYear()
    const mm = String(today.getMonth() + 1).padStart(2, '0')
    const dd = String(today.getDate()).padStart(2, '0')
    journal.publishDate = `${yyyy}-${mm}-${dd}T00:00:00`
  }
  const todayStr = new Date().toISOString().slice(0, 10)
  journal.updatedAt = todayStr
}

const deleteJournalItem = async (journal) => {
  if (confirm(`Are you sure you want to delete "${journal.title}"? This action cannot be undone.`)) {
    try {
      const response = await deleteJournal(journal.id)
      if (response.success) {
        // Remove from local data
        const index = journals.value.findIndex(j => j.id === journal.id)
        if (index !== -1) {
          journals.value.splice(index, 1)
        }
        alert('Journal deleted successfully!')
      } else {
        alert('Failed to delete journal: ' + response.message)
      }
    } catch (error) {
      console.error('Error deleting journal:', error)
      alert('Error deleting journal. Please try again.')
    }
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>