<template>
  <div class="bg-white rounded-lg shadow-xl w-full mx-4 max-h-[90vh] overflow-auto resize max-w-[95vw] md:max-w-6xl" 
       ref="modalContainer"
       :style="{ minWidth: '800px', minHeight: '600px' }">
    <!-- Header (no title) -->
    <div class="flex items-center justify-end p-2" ref="headerActions">
      <div class="flex items-center space-x-3">
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>
    </div>

    <!-- Content -->
    <div class="p-3 flex flex-col h-full">
      <!-- Toggle Buttons -->
      <div class="flex justify-center mb-2" ref="toggleBar">
        <div class="bg-gray-100 rounded-lg p-1">
          <button
            @click="viewMode = 'graph'"
            :class="[
              'px-4 py-2 rounded-md text-sm font-medium transition-colors',
              viewMode === 'graph'
                ? 'bg-white text-deep-ocean shadow-sm'
                : 'text-gray-600 hover:text-gray-900'
            ]"
          >
            Graph View
          </button>
          <button
            @click="viewMode = 'table'"
            :class="[
              'px-4 py-2 rounded-md text-sm font-medium transition-colors',
              viewMode === 'table'
                ? 'bg-white text-deep-ocean shadow-sm'
                : 'text-gray-600 hover:text-gray-900'
            ]"
          >
            Table View
          </button>
        </div>
        
      </div>

      <!-- Graph View -->
      <div v-if="viewMode === 'graph'" class="w-full flex-1 min-h-0 overflow-auto relative">
        <div class="mb-2 text-sm text-gray-700">
          Total downlines: <span class="font-semibold">{{ rootDownlineCount == null ? '…' : rootDownlineCount }}</span>
        </div>
        <div
          ref="chartContainer"
          id="chartContainer"
          class="w-full"
          :style="{ minHeight: chartHeight + 'px', height: chartHeight + 'px' }"
        ></div>
        <div ref="contextMenu" class="absolute bg-white border border-gray-200 rounded-md shadow-lg text-sm hidden z-50">
          <button @click="onContextAddDownline" class="block w-full text-left px-4 py-2 hover:bg-gray-50">Add Downline</button>
          <button @click="onContextEditMember" class="block w-full text-left px-4 py-2 hover:bg-gray-50 border-t border-gray-100">Edit Member</button>
          <button @click="onContextCopyReferral" class="block w-full text-left px-4 py-2 hover:bg-gray-50 border-t border-gray-100">Copy Referral Code</button>
        </div>
      </div>

      <!-- Table View -->
      <div v-else class="overflow-x-auto flex-1 min-h-0">
        <!-- Level 1 -->
        <div class="mb-6">
          <h4 class="font-semibold text-deep-ocean mb-4 flex items-center cursor-pointer select-none" @click="showL1 = !showL1" :aria-expanded="showL1.toString()">
            <span class="w-6 h-6 bg-ocean text-white rounded-full flex items-center justify-center text-sm mr-2">1</span>
            Level 1 - Direct Referrals ({{ l1Rows.length }} members)
            <svg class="w-4 h-4 ml-2 transition-transform" :class="showL1 ? 'rotate-90' : 'rotate-0'" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            </svg>
          </h4>
          <div class="overflow-x-auto" v-show="showL1">
            <table class="min-w-full divide-y divide-gray-200 bg-white rounded-lg border border-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Join Date</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                <button @click="sortByReferral" class="inline-flex items-center gap-1 hover:text-gray-700">
                  Referral Code
                  <span v-if="sortKey === 'referralCode'">{{ sortAsc ? '▲' : '▼' }}</span>
                </button>
              </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contribution</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="downline in l1Rows" :key="downline.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div class="flex-shrink-0 w-10 h-10">
                    <div class="w-10 h-10 rounded-full flex items-center justify-center text-white font-medium"
                         :class="getLevelColor(downline.networkLevel)">
                      {{ getInitials(downline.name) }}
                    </div>
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">{{ downline.name }}</div>
                        <div class="text-xs text-gray-500 mt-1 break-all">{{ downline.email }}</div>
                        <div class="text-xs text-gray-500 mt-1">{{ formatNetworkLevel(downline.networkLevel) }}</div>
                    </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          :class="getRankingBadgeClass(downline.level)">
                      {{ getDisplayUserLevel(downline) }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          :class="getStatusBadgeClass((downline.status || 'active').toLowerCase())">
                      {{ (downline.status || 'active').toLowerCase() }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(downline.joinDate || downline.createdAt) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-900">{{ downline.referralCode || 'N/A' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-forest-green">${{ (downline.contribution != null ? downline.contribution : 0) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm">
                    <button class="px-2 py-1 rounded hover:bg-gray-100" title="Actions" @click="openTableActions(downline, $event)">⋯</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Level 2 -->
        <div class="mb-6">
          <h4 class="font-semibold text-deep-ocean mb-4 flex items-center cursor-pointer select-none" @click="showL2 = !showL2" :aria-expanded="showL2.toString()">
            <span class="w-6 h-6 bg-forest-green text-white rounded-full flex items-center justify-center text-sm mr-2">2</span>
            Level 2 - Indirect Referrals ({{ l2Rows.length }} members)
            <svg class="w-4 h-4 ml-2 transition-transform" :class="showL2 ? 'rotate-90' : 'rotate-0'" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            </svg>
          </h4>
          <div class="overflow-x-auto" v-show="showL2">
            <table class="min-w-full divide-y divide-gray-200 bg-white rounded-lg border border-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Join Date</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    <button @click="sortByReferral" class="inline-flex items-center gap-1 hover:text-gray-700">
                      Referral Code
                      <span v-if="sortKey === 'referralCode'">{{ sortAsc ? '▲' : '▼' }}</span>
                    </button>
                  </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contribution</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="downline in l2Rows" :key="downline.id" class="hover:bg-gray-50">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 w-10 h-10">
                        <div class="w-10 h-10 rounded-full flex items-center justify-center text-white font-medium"
                             :class="getLevelColor(downline.networkLevel)">
                          {{ getInitials(downline.name) }}
                        </div>
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900">{{ downline.name }}</div>
                        <div class="text-xs text-gray-500 mt-1 break-all">{{ downline.email }}</div>
                        <div class="text-xs text-gray-500 mt-1">{{ formatNetworkLevel(downline.networkLevel) }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          :class="getRankingBadgeClass(downline.level)">
                      {{ getDisplayUserLevel(downline) }}
                </span>
              </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          :class="getStatusBadgeClass((downline.status || 'active').toLowerCase())">
                      {{ (downline.status || 'active').toLowerCase() }}
                    </span>
              </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(downline.joinDate || downline.createdAt) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-900">{{ downline.referralCode || 'N/A' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-forest-green">${{ (downline.contribution != null ? downline.contribution : 0) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm">
                    <button class="px-2 py-1 rounded hover:bg-gray-100" title="Actions" @click="openTableActions(downline, $event)">⋯</button>
                  </td>
            </tr>
          </tbody>
        </table>
          </div>
        </div>

        <!-- Level 3 -->
        <div>
          <h4 class="font-semibold text-deep-ocean mb-4 flex items-center cursor-pointer select-none" @click="showL3 = !showL3" :aria-expanded="showL3.toString()">
            <span class="w-6 h-6 bg-yellow-500 text-white rounded-full flex items-center justify-center text-sm mr-2">3</span>
            Level 3 - Third Level ({{ l3Rows.length }} members)
            <svg class="w-4 h-4 ml-2 transition-transform" :class="showL3 ? 'rotate-90' : 'rotate-0'" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            </svg>
          </h4>
          <div class="overflow-x-auto" v-show="showL3">
            <table class="min-w-full divide-y divide-gray-200 bg-white rounded-lg border border-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Join Date</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    <button @click="sortByReferral" class="inline-flex items-center gap-1 hover:text-gray-700">
                      Referral Code
                      <span v-if="sortKey === 'referralCode'">{{ sortAsc ? '▲' : '▼' }}</span>
                    </button>
                  </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Contribution</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="downline in l3Rows" :key="downline.id" class="hover:bg-gray-50">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 w-10 h-10">
                        <div class="w-10 h-10 rounded-full flex items-center justify-center text-white font-medium"
                             :class="getLevelColor(downline.networkLevel)">
                          {{ getInitials(downline.name) }}
                        </div>
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900">{{ downline.name }}</div>
                        <div class="text-xs text-gray-500 mt-1 break-all">{{ downline.email }}</div>
                        <div class="text-xs text-gray-500 mt-1">{{ formatNetworkLevel(downline.networkLevel) }}</div>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          :class="getRankingBadgeClass(downline.level)">
                      {{ getDisplayUserLevel(downline) }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          :class="getStatusBadgeClass((downline.status || 'active').toLowerCase())">
                      {{ (downline.status || 'active').toLowerCase() }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(downline.joinDate || downline.createdAt) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-900">{{ downline.referralCode || 'N/A' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-forest-green">${{ (downline.contribution != null ? downline.contribution : 0) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm">
                    <button class="px-2 py-1 rounded hover:bg-gray-100" title="Actions" @click="openTableActions(downline, $event)">⋯</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Table row actions menu -->
        <teleport to="body">
          <div
            v-if="tableAct.open"
            ref="tableMenu"
            class="fixed z-[1100] bg-white border border-gray-200 rounded-md shadow-lg w-44 py-1"
            :style="{ top: tableAct.pos.top + 'px', left: tableAct.pos.left + 'px' }"
            @click.stop
          >
            <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="onRowAddDownline()">Add Downline</button>
            <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="onRowEditMember()">Edit Member</button>
            <button class="w-full text-left px-3 py-2 text-sm hover:bg-gray-50" @click="onRowCopyReferral()">Copy Referral Code</button>
          </div>
        </teleport>
      </div>

      <!-- Footer Actions -->
      <div class="mt-4 pt-4 border-t border-gray-200 flex justify-end" ref="footerActions">
        <button
          @click="$emit('close')"
          class="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50"
        >
          Close
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { getUserNetwork, getDownlineCountBatch } from '../../utils/api.js'
import * as G6Mod from '@antv/g6'
const G6Lib = (G6Mod && (G6Mod.default && G6Mod.default.TreeGraph ? G6Mod.default : G6Mod))

const props = defineProps({
  selectedUser: {
    type: Object,
    required: true
  },
  downlines: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close','add-downline','edit-member'])

const viewMode = ref('graph')
const chartContainer = ref(null)
const modalContainer = ref(null)
let chart = null
let resizeObserver = null
let keepAliveTimer = null
const currentTreeData = ref(null)
const downlineCounts = ref({})
const rootDownlineCount = ref(null)
const chartHeight = ref(280) // initial; will auto-fit to remaining space
const headerActions = ref(null)
const toggleBar = ref(null)
const footerActions = ref(null)
let graph = null
const contextMenu = ref(null)
let lastContextNode = null
// Table row actions state
const tableAct = ref({ open: false, pos: { top: 0, left: 0 }, row: null })

const openTableActions = (row, evt) => {
  try {
    tableAct.value.row = row
    const rect = evt.currentTarget.getBoundingClientRect()
    const menuWidth = 176
    const menuHeight = 140
    const viewportWidth = window.innerWidth || document.documentElement.clientWidth
    const viewportHeight = window.innerHeight || document.documentElement.clientHeight
    let left = rect.left
    if (left + menuWidth + 8 > viewportWidth) left = Math.max(8, viewportWidth - menuWidth - 8)
    else left = Math.max(8, left)
    let top = rect.bottom + 4
    if (top + menuHeight + 8 > viewportHeight) top = Math.max(8, rect.top - menuHeight - 4)
    tableAct.value.pos = { top, left }
    tableAct.value.open = true
  } catch {}
}

const tableMenu = ref(null)
const closeTableActions = () => { tableAct.value.open = false; tableAct.value.row = null }
const onGlobalClick = (e) => {
  if (!tableAct.value.open) return
  const menuEl = tableMenu.value
  if (menuEl && typeof menuEl.contains === 'function' && menuEl.contains(e.target)) return
  closeTableActions()
}
onMounted(() => { window.addEventListener('click', onGlobalClick, true) })
onUnmounted(() => { window.removeEventListener('click', onGlobalClick, true) })

const onRowAddDownline = () => {
  const r = tableAct.value.row
  closeTableActions()
  if (!r) return
  emit('add-downline', { userId: r.id, referralCode: r.referralCode, email: r.email })
}
const onRowEditMember = () => {
  const r = tableAct.value.row
  closeTableActions()
  if (!r) return
  emit('edit-member', { id: r.id, email: r.email, referralCode: r.referralCode, name: r.name || '' })
}
const onRowCopyReferral = async () => {
  const r = tableAct.value.row
  closeTableActions()
  if (!r || !r.referralCode) return
  try {
    if (navigator.clipboard && window.isSecureContext) await navigator.clipboard.writeText(r.referralCode)
    else {
      const ta = document.createElement('textarea'); ta.value = r.referralCode; ta.style.position = 'fixed'; ta.style.left = '-9999px'; document.body.appendChild(ta); ta.focus(); ta.select(); document.execCommand('copy'); document.body.removeChild(ta)
    }
  } catch (e) { console.error('Copy referral (row) failed', e) }
}

// Collapsible sections for Table View
const showL1 = ref(true)
const showL2 = ref(true)
const showL3 = ref(true)

// Computed rows by L1/L2/L3
const l1Rows = computed(() => (downlinesWithLevels.value || []).filter(d => d.networkLevel === 1))
const l2Rows = computed(() => (downlinesWithLevels.value || []).filter(d => d.networkLevel === 2))
const l3Rows = computed(() => (downlinesWithLevels.value || []).filter(d => d.networkLevel === 3))

// Helper: 将根节点移动到窗口顶部居中位置
const translateRootToTopCenter = () => {
  try {
    if (!graph || !chartContainer.value || !currentTreeData.value) return
    const rootId = String(currentTreeData.value.value)
    const rootItem = graph.findById(rootId)
    if (!rootItem) return
    const m = rootItem.getModel()
    const containerWidth = chartContainer.value.clientWidth || 800
    const topPadding = 80
    const targetX = Math.floor(containerWidth / 2)
    const targetY = topPadding
    const dx = targetX - (m.x || 0)
    const dy = targetY - (m.y || 0)
    if (dx !== 0 || dy !== 0) graph.translate(dx, dy)
  } catch {}
}

// Helper: 同步 G6 折叠状态到源数据 currentTreeData
const updateCollapsedInSource = (root, value, collapsed) => {
  if (!root) return false
  if (String(root.value) === String(value)) {
    root.collapsed = collapsed
    return true
  }
  if (root.children && root.children.length) {
    for (let i = 0; i < root.children.length; i++) {
      if (updateCollapsedInSource(root.children[i], value, collapsed)) return true
    }
  }
  return false
}

// Helper functions
const getInitials = (name) => {
  if (!name) return 'U'
  const parts = name.split(' ')
  if (parts.length >= 2) {
    return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase()
  }
  return name[0]?.toUpperCase() || 'U'
}

const getLevelColor = (level) => {
  switch (level) {
    case 1: return 'bg-ocean'
    case 2: return 'bg-forest-green'
    case 3: return 'bg-yellow-500'
    default: return 'bg-gray-500'
  }
}

const getLevelBadgeColor = (level) => {
  switch (level) {
    case 1: return 'bg-blue-100 text-blue-800'
    case 2: return 'bg-green-100 text-green-800'
    case 3: return 'bg-yellow-100 text-yellow-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

// Display title from backend level
const getDisplayUserLevel = (member) => {
  const lvl = (member?.level || '').toString().toUpperCase()
  switch (lvl) {
    case 'CUSTOMER': return 'Customer'
    case 'CHIEF': return 'Chief'
    case 'MAYOR': return 'Mayor'
    case 'GOVERNOR': return 'Governor'
    case 'MINISTER': return 'Minister'
    case 'PRESIDENT': return 'President'
    default: return 'Customer'
  }
}

// Status badge class mapping
const getStatusBadgeClass = (status) => {
  const s = (status || '').toString().toLowerCase()
  switch (s) {
    case 'active': return 'bg-green-100 text-green-800'
    case 'inactive': return 'bg-red-100 text-red-800'
    case 'suspended': return 'bg-yellow-100 text-yellow-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

// Date formatter
const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  try { return new Date(dateString).toLocaleDateString() } catch { return 'N/A' }
}

// 排名（职级）：Customer, Chief, Mayor, Governor, Minister, President
const formatRanking = (rank) => {
  const r = (rank || '').toString().toUpperCase()
  return r || '—'
}
const getRankingBadgeClass = (rank) => {
  const r = (rank || '').toString().toUpperCase()
  switch (r) {
    case 'CUSTOMER': return 'bg-gray-100 text-gray-800'
    case 'CHIEF': return 'bg-blue-100 text-blue-800'
    case 'MAYOR': return 'bg-indigo-100 text-indigo-800'
    case 'GOVERNOR': return 'bg-purple-100 text-purple-800'
    case 'MINISTER': return 'bg-pink-100 text-pink-800'
    case 'PRESIDENT': return 'bg-yellow-100 text-yellow-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

// 格式化网络层级：显示为 L1/L2/L3
const formatNetworkLevel = (n) => {
  if (n === null || n === undefined) return '—'
  return `L${n}`
}

// 计算下线的网络层级（相对于当前 selectedUser）
const downlinesWithLevels = computed(() => {
  const list = Array.isArray(props.downlines) ? props.downlines.map(m => ({ ...m })) : []
  const rootCode = (props.selectedUser && props.selectedUser.referralCode) ? props.selectedUser.referralCode : ''
  if (!list.length || !rootCode) return list
  const codeToLevel = new Map()
  const referralToMembers = new Map()
  for (let i = 0; i < list.length; i++) {
    const m = list[i]
    const code = (m.referralCode || '')
    if (!referralToMembers.has(code)) referralToMembers.set(code, m)
  }
  // Level 1
  const l1Codes = new Set()
  for (let i = 0; i < list.length; i++) {
    const m = list[i]
    if ((m.referredByCode || '') === rootCode) {
      codeToLevel.set(m.referralCode || '', 1)
      l1Codes.add(m.referralCode || '')
    }
  }
  // Level 2
  const l2Codes = new Set()
  for (let i = 0; i < list.length; i++) {
    const m = list[i]
    if (l1Codes.has(m.referredByCode || '')) {
      codeToLevel.set(m.referralCode || '', 2)
      l2Codes.add(m.referralCode || '')
    }
  }
  // Level 3
  for (let i = 0; i < list.length; i++) {
    const m = list[i]
    if (l2Codes.has(m.referredByCode || '')) {
      codeToLevel.set(m.referralCode || '', 3)
    }
  }
  for (let i = 0; i < list.length; i++) {
    const m = list[i]
    const lvl = codeToLevel.get(m.referralCode || '')
    m.networkLevel = (lvl === undefined ? null : lvl)
  }
  return list
})

const getNodeLevel = (data) => {
  // Root node uses numeric user id
  if (data.value === props.selectedUser.id) return 'You'
  if (typeof data.value === 'string' && data.value.startsWith('level1_')) return 'Level 1'
  if (typeof data.value === 'string' && data.value.startsWith('level2_')) return 'Level 2'
  if (typeof data.value === 'string' && data.value.startsWith('level3_')) return 'Level 3'
  return 'Member'
}

// Build G6 tree data
const getEmailLocal = (email) => {
  if (!email || typeof email !== 'string') return ''
  const at = email.indexOf('@')
  return at > 0 ? email.slice(0, at) : email
}
const toG6Tree = (root) => {
  const mapNode = (n) => ({
    id: String(n.value),
    label: getEmailLocal(n.email) || (n.referralCode || ''),
    data: n,
    children: (n.children || []).map(mapNode),
    collapsed: n.collapsed === true
  })
  return mapNode(root)
}

// 收集当前树中所有 userId
const collectUserIds = (node, outSet) => {
  if (!node) return
  if (node.userId !== undefined && node.userId !== null) {
    outSet.add(Number(node.userId))
  }
  if (Array.isArray(node.children)) {
    for (let i = 0; i < node.children.length; i++) {
      collectUserIds(node.children[i], outSet)
    }
  }
}

// 将 downlineCounts 应用到图节点标签
const applyCountsToGraph = () => {
  if (!graph) return
  try {
    const nodes = graph.getNodes()
    for (let i = 0; i < nodes.length; i++) {
      const item = nodes[i]
      const model = item.getModel()
      const data = model && model.data ? model.data : null
      const base = data ? (getEmailLocal(data.email) || (data.referralCode || '')) : (model.label || '')
      const uid = data && data.userId !== undefined ? Number(data.userId) : null
      const cnt = uid != null ? downlineCounts.value[uid] : undefined
      const newLabel = (typeof cnt === 'number') ? `${base} (${cnt})` : base
      if (model.label !== newLabel) {
        graph.updateItem(item, { label: newLabel })
      }
    }
  } catch (e) {
    console.error('applyCountsToGraph failed', e)
  }
}

// 向后端请求当前树的下线数，并更新标签
const refreshDownlineCountsForTree = async () => {
  try {
    if (!currentTreeData.value) return
    const idsSet = new Set()
    collectUserIds(currentTreeData.value, idsSet)
    const ids = Array.from(idsSet)
    if (!ids.length) return
    const resp = await getDownlineCountBatch(ids)
    if (resp && resp.success) {
      downlineCounts.value = resp.data || {}
      const rootId = Number(currentTreeData.value && currentTreeData.value.userId != null ? currentTreeData.value.userId : (props.selectedUser && props.selectedUser.id))
      rootDownlineCount.value = (rootId != null && downlineCounts.value[rootId] != null) ? downlineCounts.value[rootId] : 0
      applyCountsToGraph()
    }
  } catch (e) {
    console.error('refreshDownlineCountsForTree failed', e)
  }
}

const applyTreeOption = (treeData) => {
  if (!chartContainer.value) return
  if (graph) {
    try { graph.destroy() } catch {}
    graph = null
  }
  const width = chartContainer.value.clientWidth || 800
  const height = chartHeight.value || 300

  // Tooltip to show member email on hover
  const tooltip = new G6Lib.Tooltip({
    offsetX: 10,
    offsetY: 10,
    itemTypes: ['node'],
    getContent: (e) => {
      const model = e && e.item ? e.item.getModel() : null
      const email = (model && model.data && model.data.email) ? model.data.email : 'N/A'
      const referral = (model && model.data && model.data.referralCode) ? model.data.referralCode : 'N/A'
      const div = document.createElement('div')
      div.style.padding = '6px 8px'
      div.style.background = '#ffffff'
      div.style.border = '1px solid #e5e7eb'
      div.style.borderRadius = '6px'
      div.style.boxShadow = '0 4px 12px rgba(0,0,0,0.08)'
      div.innerHTML = `
        <div style="font-size:12px;color:#111827;">Email: <span style="font-family:ui-monospace, SFMono-Regular, Menlo, monospace">${email}</span></div>
        <div style="font-size:11px;color:#6b7280;">Referral Code: ${referral}</div>
      `
      return div
    }
  })

  graph = new G6Lib.TreeGraph({
    container: chartContainer.value,
    width,
    height,
    fitView: false,
    plugins: [tooltip],
    modes: {
      default: [
        'drag-canvas',
        'zoom-canvas'
      ]
    },
    layout: {
      type: 'compactBox',
      direction: 'TB',
      getId: (d) => d.id,
      getHeight: () => 16,
      getWidth: () => 60,
      getVGap: () => 24,
      getHGap: () => 12
    },
    defaultNode: {
      type: 'circle',
      size: 8,
      style: { stroke: '#94a3b8', fill: '#e2e8f0' },
      labelCfg: { position: 'top', offset: 4, style: { fontSize: 11, fill: '#374151' } }
    },
    defaultEdge: {
      type: 'polyline',
      style: { stroke: '#d1d5db', lineWidth: 1 }
    }
  })

  graph.on('node:contextmenu', (evt) => {
    evt.originalEvent.preventDefault()
    lastContextNode = evt.item.getModel()
    const menu = contextMenu.value
    if (!menu) return
    const rect = chartContainer.value.getBoundingClientRect()
    menu.style.left = `${evt.clientX - rect.left}px`
    menu.style.top = `${evt.clientY - rect.top}px`
    menu.classList.remove('hidden')
  })
  graph.on('canvas:click', () => {
    const menu = contextMenu.value
    if (menu) menu.classList.add('hidden')
  })

  const data = toG6Tree(treeData)
  graph.data(data)
  graph.render()

  // Position root to top-center of graph window
  translateRootToTopCenter()
  // 初次渲染后刷新计数
  setTimeout(() => { refreshDownlineCountsForTree() }, 0)

  // On click: toggle collapse/expand OR dynamically load children for leaf
  graph.off('node:click')
  graph.on('node:click', async (evt) => {
    try {
      const model = evt?.item?.getModel()
      if (!model || !model.data) return
      const isLeaf = !model.children || model.children.length === 0
      if (!isLeaf) {
        // 已有children，则切换折叠状态；不重设数据，避免平移被重置
        const currentCollapsed = !!model.collapsed
        const nodeVal = (model && model.data && model.data.value) ? model.data.value : model.id
        updateCollapsedInSource(currentTreeData.value, nodeVal, !currentCollapsed)
        graph.updateItem(evt.item, { collapsed: !currentCollapsed })
        // 仅请求重新布局，保持 translate/zoom
        try { graph.layout() } catch {}
        setTimeout(() => { applyCountsToGraph() }, 0)
        return
      }

      const userId = model.data.userId
      if (!userId) return

      // Load network for this node
      const resp = await getUserNetwork(userId)
      if (!resp || !resp.success) return
      const dl = Array.isArray(resp.data?.downlines) ? resp.data.downlines : []
      // Update our source tree (currentTreeData) then re-render with applyTreeOption
      const findById = (root, id) => {
        if (!root) return null
        if (String(root.value) === String(id)) return root
        if (root.children) {
          for (let i = 0; i < root.children.length; i++) {
            const r = findById(root.children[i], id)
            if (r) return r
          }
        }
        return null
      }
      const target = findById(currentTreeData.value, model.data.value)
      if (!target) return
      const mk = (m, tag) => ({
        name: m.name,
        value: `${tag}_${m.id}`,
        email: m.email || '',
        referralCode: m.referralCode || '',
        userId: m.id,
        collapsed: true,
        children: []
      })
      const ch = []
      const l1 = dl.filter(m => m.referredByCode === (target.referralCode || ''))
      l1.forEach(m1 => {
        const n1 = mk(m1, 'l1')
        const l2 = dl.filter(m => m.referredByCode === m1.referralCode)
        l2.forEach(m2 => {
          const n2 = mk(m2, 'l2')
          const l3 = dl.filter(m => m.referredByCode === m2.referralCode)
          l3.forEach(m3 => n2.children.push(mk(m3, 'l3')))
          n1.children.push(n2)
        })
        ch.push(n1)
      })
      if (ch.length === 0) return
      target.children = ch
      // 保持现有展开状态：把沿途祖先的 collapsed 设为 false，避免回到只显示一层
      const ensureAncestorsExpanded = (root, targetVal) => {
        if (!root) return false
        if (String(root.value) === String(targetVal)) return true
        if (root.children) {
          for (let i = 0; i < root.children.length; i++) {
            if (ensureAncestorsExpanded(root.children[i], targetVal)) {
              root.collapsed = false
              return true
            }
          }
        }
        return false
      }
      ensureAncestorsExpanded(currentTreeData.value, target.value)
      target.collapsed = false
      // 更新图数据并保持位置
      const newData = toG6Tree(currentTreeData.value)
      const handler = () => {
        translateRootToTopCenter()
        graph.off('afterlayout', handler)
      }
      graph.on('afterlayout', handler)
      graph.changeData(newData)
      // 数据改变后刷新计数
      setTimeout(() => { refreshDownlineCountsForTree() }, 0)
      // 也立即应用已有的计数数据，避免闪烁
      setTimeout(() => { applyCountsToGraph() }, 0)
    } catch (e) {
      console.error('Dynamic load downlines failed', e)
    }
  })
}

// Toggle collapsed state of a node by value in currentTreeData
// Only affect the clicked node; other nodes keep their current state
const toggleNodeCollapsedByValue = (root, targetValue) => {
  if (!root) return false;
  if (root.value === targetValue) {
    root.collapsed = !root.collapsed;
    return true;
  }
  if (Array.isArray(root.children)) {
    for (let i = 0; i < root.children.length; i++) {
      if (toggleNodeCollapsedByValue(root.children[i], targetValue)) return true;
    }
  }
  return false;
}

// Debug functions
const forceTestChart = () => {
  console.log('=== FORCE TEST TREE CHART ===')
  if (!chart) {
    console.log('No chart instance, creating one...')
    initChart()
    return
  }
  
  const testTreeData = {
    name: 'You (Root)',
    value: 'root',
    itemStyle: { color: '#1e40af' },
    symbolSize: 30,
    children: [
      {
        name: 'Test Level 1 Member',
        value: 'level1_test',
        itemStyle: { color: '#3b82f6' },
        symbolSize: 25,
        children: [
          {
            name: 'Test Level 2 Member',
            value: 'level2_test',
            itemStyle: { color: '#10b981' },
            symbolSize: 20,
            children: [
              {
                name: 'Test Level 3 Member',
                value: 'level3_test',
                itemStyle: { color: '#f59e0b' },
                symbolSize: 18,
                children: []
              }
            ]
          }
        ]
      },
      {
        name: 'Another Level 1 Member',
        value: 'level1_another',
        itemStyle: { color: '#3b82f6' },
        symbolSize: 25,
        children: []
      }
    ]
  }
  
  const testOption = {
    backgroundColor: '#f8fafc',
    title: {
      text: 'FORCED TEST TREE CHART',
      left: 'center',
      top: 20,
      textStyle: { color: '#dc2626', fontSize: 20 }
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        return `<div style="padding: 8px;"><strong>${params.data.name}</strong></div>`
      }
    },
    series: [
      {
        name: 'Test Tree',
        type: 'tree',
        // IMPORTANT: Tree data must be an array
        data: [testTreeData],
        top: '15%',
        left: '10%',
        bottom: '10%',
        right: '10%',
        symbolSize: 20,
        orient: 'TB',
        initialTreeDepth: -1,
        roam: true,
        label: {
          show: true,
          position: 'left',
          verticalAlign: 'middle',
          align: 'right',
          fontSize: 12,
          color: '#333'
        },
        leaves: {
          label: {
            position: 'right',
            verticalAlign: 'middle',
            align: 'left'
          }
        },
        emphasis: {
          focus: 'descendant'
        },
        expandAndCollapse: true,
        animationDuration: 550,
        lineStyle: {
          color: '#ccc',
          width: 2
        }
      }
    ]
  }
  
  try {
    chart.setOption(testOption, true)
    console.log('Forced test tree chart set successfully')
    console.log('Test tree data:', testTreeData)
  } catch (error) {
    console.error('Forced test tree chart failed:', error)
    console.error('Error details:', error.message, error.stack)
  }
}

const showDebugInfo = () => {
  console.log('=== DEBUG INFO ===')
  console.log('Chart container:', chartContainer.value)
  console.log('Chart instance:', chart)
  console.log('Selected user:', props.selectedUser)
  console.log('Downlines:', props.downlines)
  console.log('View mode:', viewMode.value)
  
  if (chartContainer.value) {
    const container = chartContainer.value
    console.log('Container dimensions:', {
      offsetWidth: container.offsetWidth,
      offsetHeight: container.offsetHeight,
      clientWidth: container.clientWidth,
      clientHeight: container.clientHeight,
      scrollWidth: container.scrollWidth,
      scrollHeight: container.scrollHeight,
      innerHTML: container.innerHTML.substring(0, 200) + '...'
    })
  }
  
  // Show debug info in the container
  if (chartContainer.value) {
    chartContainer.value.innerHTML = `
      <div style="padding: 20px; background: #f3f4f6; border: 2px solid #ef4444; border-radius: 8px;">
        <h3 style="color: #dc2626; margin-bottom: 10px;">DEBUG INFO</h3>
        <p><strong>Container exists:</strong> ${chartContainer.value ? 'YES' : 'NO'}</p>
        <p><strong>Chart instance:</strong> ${chart ? 'YES' : 'NO'}</p>
        <p><strong>Selected user:</strong> ${props.selectedUser ? props.selectedUser.name : 'NO'}</p>
        <p><strong>Downlines count:</strong> ${props.downlines ? props.downlines.length : 'NO'}</p>
        <p><strong>View mode:</strong> ${viewMode.value}</p>
        <p><strong>Container width:</strong> ${chartContainer.value ? chartContainer.value.offsetWidth : 'N/A'}</p>
        <p><strong>Container height:</strong> ${chartContainer.value ? chartContainer.value.offsetHeight : 'N/A'}</p>
      </div>
    `
  }
}

// Prepare tree data structure for ECharts tree
const prepareTreeData = () => {
  console.log('=== PREPARE TREE DATA START ===')
  console.log('Selected user:', props.selectedUser)
  console.log('Downlines:', props.downlines)
  console.log('Downlines type:', typeof props.downlines)
  console.log('Downlines is array:', Array.isArray(props.downlines))
  
  if (props.downlines && props.downlines.length > 0) {
    console.log('First downline member:', props.downlines[0])
    console.log('First downline keys:', Object.keys(props.downlines[0]))
    console.log('First downline networkLevel:', props.downlines[0].networkLevel)
  }
  
  // Create root node (carry email and referralCode for tooltip)
  const root = {
    name: props.selectedUser.name || 'You',
    value: props.selectedUser.id,
    email: props.selectedUser.email || '',
    referralCode: props.selectedUser.referralCode || props.selectedUser.code || '',
    userId: props.selectedUser.id,
    itemStyle: { color: '#1e40af' },
    label: { fontSize: 12, color: '#374151' },
    symbolSize: 10,
    children: []
  }

  // Build real hierarchy: attach by referredByCode === parent referralCode
  const byReferral = new Map()
  ;(props.downlines || []).forEach(m => {
    if (m.referralCode) byReferral.set(m.referralCode, m)
  })

  // Helper to create node
  const makeNode = (m, tag) => ({
    name: m.name,
    value: `${tag}_${m.id}`,
    email: m.email || '',
    referralCode: m.referralCode || '',
    userId: m.id,
    itemStyle: { color: tag === 'l1' ? '#60a5fa' : tag === 'l2' ? '#34d399' : '#fbbf24' },
    label: { fontSize: 12, color: '#374151' },
    symbolSize: 8,
    // default collapsed so clicking toggles to expand just one level
    collapsed: true,
    children: []
  })

  // Level 1: referredByCode === current user's referralCode
  const l1 = (props.downlines || []).filter(m => m.referredByCode === (props.selectedUser.referralCode || ''))
  l1.forEach(m1 => {
    const n1 = makeNode(m1, 'l1')
    // Level 2: referredByCode === m1.referralCode
    const l2 = (props.downlines || []).filter(m => m.referredByCode === m1.referralCode)
    l2.forEach(m2 => {
      const n2 = makeNode(m2, 'l2')
      // Level 3: referredByCode === m2.referralCode
      const l3 = (props.downlines || []).filter(m => m.referredByCode === m2.referralCode)
      l3.forEach(m3 => {
        const n3 = makeNode(m3, 'l3')
        // leaves can keep default collapsed true; no children by default
        n2.children.push(n3)
      })
      n1.children.push(n2)
    })
    root.children.push(n1)
  })
  
  console.log('Final tree root:', root)
  console.log('Root children count:', root.children.length)
  console.log('=== PREPARE TREE DATA END ===')
  
  return root
}

// Initialize chart
const initChart = () => {
  console.log('=== CHART INITIALIZATION START ===')
  console.log('Chart container:', chartContainer.value)
  console.log('Selected user:', props.selectedUser)
  console.log('Downlines:', props.downlines)
  console.log('Downlines length:', props.downlines.length)
  
  if (!chartContainer.value) {
    console.error('Chart container not found')
    return
  }

  nextTick(() => {
    try {
      if (chart) {
        chart.dispose()
        chart = null
      }

      const container = chartContainer.value
      
      const treeData = prepareTreeData()
      console.log('Tree data prepared:', treeData)
      console.log('Tree data children count:', treeData.children ? treeData.children.length : 0)
      currentTreeData.value = treeData
      
      console.log('Applying G6 tree...')
      try {
        applyTreeOption(currentTreeData.value)
        console.log('Tree graph rendered')
      } catch (error) {
        console.error('Error setting tree chart option:', error)
        chartContainer.value.innerHTML = `<div style="padding: 12px; color:#dc2626;">Graph render failed</div>`
      }
      
      // Auto-fit chart height to fill remaining modal space
      const fitHeight = () => {
        if (!modalContainer.value || !chartContainer.value) return
        const modalRect = modalContainer.value.getBoundingClientRect()
        const headerH = headerActions.value ? headerActions.value.getBoundingClientRect().height : 0
        const toggleH = toggleBar.value ? toggleBar.value.getBoundingClientRect().height : 0
        const footerH = footerActions.value ? footerActions.value.getBoundingClientRect().height : 0
        const paddings = 24 // approx vertical paddings/margins
        const h = Math.max(240, Math.floor(modalRect.height - headerH - toggleH - footerH - paddings))
        if (h !== chartHeight.value) {
          chartHeight.value = h
          if (graph) graph.changeSize(chartContainer.value.clientWidth, chartHeight.value)
        }
      }
      setTimeout(fitHeight, 50)
      keepAliveTimer && clearInterval(keepAliveTimer)
      
      // Handle container resize
      const handleResize = () => {
        fitHeight()
      }
      
      if (modalContainer.value && window.ResizeObserver) {
        resizeObserver = new ResizeObserver(handleResize)
        resizeObserver.observe(modalContainer.value)
      }
      
      window.addEventListener('resize', handleResize)

      // Height auto-grow on expand
      if (graph) {
        graph.on('afteritemstatechange', (e) => {
          fitHeight()
        })
      }
      
      console.log('=== CHART INITIALIZATION COMPLETE ===')
      
    } catch (error) {
      console.error('Error initializing chart:', error)
      console.error('Error stack:', error.stack)
    }
  })
}

// Watch for changes
watch(() => props.downlines, () => {
  if (chart && viewMode.value === 'graph') {
    nextTick(() => {
      currentTreeData.value = prepareTreeData()
      applyTreeOption(currentTreeData.value)
    })
  }
}, { deep: true })

// Initialize on mount
onMounted(() => {
  if (viewMode.value === 'graph') {
    nextTick(() => {
      initChart()
    })
  }
})

// Watch view mode changes
watch(viewMode, (newMode) => {
  if (newMode === 'graph') {
    nextTick(() => {
      initChart()
    })
  }
})

// Cleanup on unmount
onUnmounted(() => {
  if (resizeObserver) {
    resizeObserver.disconnect()
  }
  // destroy g6 graph if exists
  if (graph) try { graph.destroy() } catch {}
})

// Table sorting (table view)
const sortKey = ref('')
const sortAsc = ref(true)
const sortByReferral = () => {
  if (sortKey.value === 'referralCode') {
    sortAsc.value = !sortAsc.value
  } else {
    sortKey.value = 'referralCode'
    sortAsc.value = true
  }
}
const sortedDownlines = computed(() => {
  const list = Array.isArray(props.downlines) ? [...props.downlines] : []
  if (sortKey.value === 'referralCode') {
    list.sort((a, b) => {
      const av = (a.referralCode || '').toString()
      const bv = (b.referralCode || '').toString()
      return sortAsc.value ? av.localeCompare(bv) : bv.localeCompare(av)
    })
  }
  return list
})

// Context menu: add downline action
const onContextAddDownline = () => {
  const menu = contextMenu.value
  if (menu) menu.classList.add('hidden')
  if (!lastContextNode || !lastContextNode.data) return
  // Emit event with the user represented by this node so parent can open modal
  emit('add-downline', {
    userId: lastContextNode.data.userId,
    referralCode: lastContextNode.data.referralCode,
    email: lastContextNode.data.email
  })
}

const onContextEditMember = () => {
  const menu = contextMenu.value
  if (menu) menu.classList.add('hidden')
  if (!lastContextNode || !lastContextNode.data) return
  emit('edit-member', {
    id: lastContextNode.data.userId,
    email: lastContextNode.data.email,
    referralCode: lastContextNode.data.referralCode,
    name: lastContextNode.data.name || ''
  })
}

// Context menu: copy referral code action
const onContextCopyReferral = async () => {
  const menu = contextMenu.value
  if (menu) menu.classList.add('hidden')
  const code = lastContextNode && lastContextNode.data ? (lastContextNode.data.referralCode || '') : ''
  if (!code) return
  try {
    if (navigator.clipboard && window.isSecureContext) {
      await navigator.clipboard.writeText(code)
    } else {
      const ta = document.createElement('textarea')
      ta.value = code
      ta.style.position = 'fixed'
      ta.style.left = '-9999px'
      document.body.appendChild(ta)
      ta.focus()
      ta.select()
      document.execCommand('copy')
      document.body.removeChild(ta)
    }
  } catch (e) {
    console.error('Copy referral code failed', e)
  }
}

// Expose method to append a newly created downline under a parent by referral code
const appendDownline = ({ parentReferralCode, newDownline }) => {
  try {
    if (!currentTreeData.value || !parentReferralCode || !newDownline) return
    const findByReferral = (node, code) => {
      if (!node) return null
      if ((node.referralCode || '') === code) return node
      if (node.children && node.children.length) {
        for (let i = 0; i < node.children.length; i++) {
          const found = findByReferral(node.children[i], code)
          if (found) return found
        }
      }
      return null
    }
    const parent = findByReferral(currentTreeData.value, parentReferralCode)
    if (!parent) return
    if (!parent.children) parent.children = []
    const child = {
      name: `${newDownline.firstName || ''} ${newDownline.lastName || ''}`.trim() || newDownline.email || '',
      value: `dyn_${newDownline.id}`,
      email: newDownline.email || '',
      referralCode: newDownline.referralCode || '',
      userId: newDownline.id,
      collapsed: true,
      children: []
    }
    parent.children.push(child)
    parent.collapsed = false
    // Keep ancestors expanded
    const ensureAncestorsExpanded = (root, targetVal) => {
      if (!root) return false
      if (String(root.value) === String(targetVal)) return true
      if (root.children) {
        for (let i = 0; i < root.children.length; i++) {
          if (ensureAncestorsExpanded(root.children[i], targetVal)) {
            root.collapsed = false
            return true
          }
        }
      }
      return false
    }
    ensureAncestorsExpanded(currentTreeData.value, parent.value)
    // Update graph data
    const newData = toG6Tree(currentTreeData.value)
    const handler = () => {
      translateRootToTopCenter()
      graph.off('afterlayout', handler)
    }
    graph.on('afterlayout', handler)
    graph.changeData(newData)
    // 数据改变后刷新计数
    setTimeout(() => { refreshDownlineCountsForTree() }, 0)
  } catch (e) {
    console.error('appendDownline failed', e)
  }
}


// Update member fields (email/name/referralCode) in tree and refresh graph
const updateMember = ({ id, email, firstName, lastName, referralCode }) => {
  try {
    if (!currentTreeData.value || !id) return
    const patch = {
      email: email !== undefined ? email : undefined,
      name: (firstName || lastName) ? `${firstName || ''} ${lastName || ''}`.trim() : undefined,
      referralCode: referralCode !== undefined ? referralCode : undefined
    }
    const applyPatch = (node) => {
      if (!node) return false
      let hit = false
      if (String(node.userId) === String(id)) {
        if (patch.email !== undefined) node.email = patch.email
        if (patch.name !== undefined && patch.name.length > 0) node.name = patch.name
        if (patch.referralCode !== undefined) node.referralCode = patch.referralCode || node.referralCode
        hit = true
      }
      if (node.children && node.children.length) {
        for (let i = 0; i < node.children.length; i++) {
          if (applyPatch(node.children[i])) hit = true
        }
      }
      return hit
    }
    const changed = applyPatch(currentTreeData.value)
    if (!changed) return
    const newData = toG6Tree(currentTreeData.value)
    const handler = () => {
      translateRootToTopCenter()
      graph.off('afterlayout', handler)
    }
    graph.on('afterlayout', handler)
    graph.changeData(newData)
  } catch (e) {
    console.error('updateMember failed', e)
  }
}

// Reparent a member to a new upline by referredByCode
const reparentMember = async ({ id, newReferredByCode }) => {
  try {
    if (!currentTreeData.value || !id || newReferredByCode === undefined) return
    const findWithParent = (node, parent) => {
      if (!node) return null
      if (String(node.userId) === String(id)) return { node, parent }
      if (node.children && node.children.length) {
        for (let i = 0; i < node.children.length; i++) {
          const res = findWithParent(node.children[i], node)
          if (res) return res
        }
      }
      return null
    }
    const findByReferral = (node, code) => {
      if (!node) return null
      if ((node.referralCode || '') === code) return node
      if (node.children && node.children.length) {
        for (let i = 0; i < node.children.length; i++) {
          const found = findByReferral(node.children[i], code)
          if (found) return found
        }
      }
      return null
    }
    const hit = findWithParent(currentTreeData.value, null)
    if (!hit) {
      // Fallback: if the edited node isn't in current tree (not loaded/visible), reload network and rebuild
      try {
        const resp = await getUserNetwork(currentTreeData.value.userId)
        if (resp && resp.success) {
          const root = {
            name: currentTreeData.value.name,
            value: currentTreeData.value.value,
            email: currentTreeData.value.email,
            referralCode: currentTreeData.value.referralCode,
            userId: currentTreeData.value.userId,
            collapsed: false,
            children: []
          }
          const dl = Array.isArray(resp.data?.downlines) ? resp.data.downlines : []
          const makeNode = (m, tag) => ({
            name: m.name,
            value: `${tag}_${m.id}`,
            email: m.email || '',
            referralCode: m.referralCode || '',
            userId: m.id,
            collapsed: true,
            children: []
          })
          const l1 = dl.filter(m => m.referredByCode === (root.referralCode || ''))
          l1.forEach(m1 => {
            const n1 = makeNode(m1, 'l1')
            const l2 = dl.filter(m => m.referredByCode === m1.referralCode)
            l2.forEach(m2 => {
              const n2 = makeNode(m2, 'l2')
              const l3 = dl.filter(m => m.referredByCode === m2.referralCode)
              l3.forEach(m3 => n2.children.push(makeNode(m3, 'l3')))
              n1.children.push(n2)
            })
            root.children.push(n1)
          })
          currentTreeData.value = root
          const newData = toG6Tree(currentTreeData.value)
          const handler = () => {
            translateRootToTopCenter()
            graph.off('afterlayout', handler)
          }
          graph.on('afterlayout', handler)
          graph.changeData(newData)
          setTimeout(() => { refreshDownlineCountsForTree() }, 0)
        }
      } catch (e) {
        console.error('reparentMember reload fallback failed', e)
      }
      return
    }
    const { node: targetNode, parent: oldParent } = hit
    // Remove from old parent
    if (oldParent && oldParent.children) {
      oldParent.children = oldParent.children.filter(c => String(c.userId) !== String(targetNode.userId))
    } else if (!oldParent) {
      // If it was root's child, remove from root
      currentTreeData.value.children = (currentTreeData.value.children || []).filter(c => String(c.userId) !== String(targetNode.userId))
    }
    // Update node's referredBy relation locally
    // Find new parent
    let newParent = null
    if (newReferredByCode && newReferredByCode.length > 0) {
      newParent = findByReferral(currentTreeData.value, newReferredByCode)
    } else {
      // fallback to root if no code
      newParent = currentTreeData.value
    }
    if (!newParent) {
      // Fallback: reload whole network
      const resp = await getUserNetwork(currentTreeData.value.userId)
      if (resp && resp.success) {
        // rebuild source tree from backend downlines with our builder
        const root = {
          name: currentTreeData.value.name,
          value: currentTreeData.value.value,
          email: currentTreeData.value.email,
          referralCode: currentTreeData.value.referralCode,
          userId: currentTreeData.value.userId,
          collapsed: false,
          children: []
        }
        const dl = Array.isArray(resp.data?.downlines) ? resp.data.downlines : []
        const makeNode = (m, tag) => ({
          name: m.name,
          value: `${tag}_${m.id}`,
          email: m.email || '',
          referralCode: m.referralCode || '',
          userId: m.id,
          collapsed: true,
          children: []
        })
        const l1 = dl.filter(m => m.referredByCode === (root.referralCode || ''))
        l1.forEach(m1 => {
          const n1 = makeNode(m1, 'l1')
          const l2 = dl.filter(m => m.referredByCode === m1.referralCode)
          l2.forEach(m2 => {
            const n2 = makeNode(m2, 'l2')
            const l3 = dl.filter(m => m.referredByCode === m2.referralCode)
            l3.forEach(m3 => n2.children.push(makeNode(m3, 'l3')))
            n1.children.push(n2)
          })
          root.children.push(n1)
        })
        currentTreeData.value = root
      }
    } else {
      // attach under new parent
      if (!newParent.children) newParent.children = []
      // Avoid duplicate under new parent
      newParent.children = newParent.children.filter(c => String(c.userId) !== String(targetNode.userId))
      newParent.children.push(targetNode)
      newParent.collapsed = false
    }
    // Ensure new path expanded
    const ensureAncestorsExpanded = (root, targetVal) => {
      if (!root) return false
      if (String(root.value) === String(targetVal)) return true
      if (root.children) {
        for (let i = 0; i < root.children.length; i++) {
          if (ensureAncestorsExpanded(root.children[i], targetVal)) {
            root.collapsed = false
            return true
          }
        }
      }
      return false
    }
    ensureAncestorsExpanded(currentTreeData.value, targetNode.value)
    // Redraw
    const newData = toG6Tree(currentTreeData.value)
    const handler = () => {
      translateRootToTopCenter()
      graph.off('afterlayout', handler)
    }
    graph.on('afterlayout', handler)
    graph.changeData(newData)
    // 数据改变后刷新计数
    setTimeout(() => { refreshDownlineCountsForTree() }, 0)
  } catch (e) {
    console.error('reparentMember failed', e)
  }
}

defineExpose({ appendDownline, updateMember, reparentMember })
</script>

<style scoped>
/* Custom scrollbar for table */
.overflow-x-auto::-webkit-scrollbar {
  height: 8px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Resizable modal styles */
.resize {
  resize: both;
  overflow: auto;
}

.resize::-webkit-resizer {
  background: linear-gradient(-45deg, transparent 33%, #cbd5e1 33%, #cbd5e1 66%, transparent 66%);
  background-size: 8px 8px;
  border: 1px solid #94a3b8;
  border-radius: 2px;
}

/* Smooth transitions for chart container */
.w-full.flex-1.min-h-0 {
  transition: all 0.3s ease;
}

/* Enhanced chart container */
#chartContainer {
  border-radius: 8px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border: 1px solid #e2e8f0;
  /* Ensure scroll on small screens */
  overflow: auto;
}
</style>

 