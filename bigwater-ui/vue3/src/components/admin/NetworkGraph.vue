<template>
  <div class="network-graph-container">
    <div class="graph-header">
      <h3 class="text-lg font-bold text-deep-ocean">Network Graph - {{ selectedUser?.fullName || selectedUser?.name }}</h3>
      <div class="flex space-x-2">
        <button @click="toggleViewMode" class="px-3 py-1 text-sm bg-ocean text-white rounded hover:bg-deep-ocean">
          {{ viewMode === 'graph' ? 'Show Table' : 'Show Graph' }}
        </button>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">✕</button>
      </div>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center h-64">
      <div class="text-gray-500">Loading network data...</div>
    </div>
    
    <div v-else-if="viewMode === 'graph'" class="graph-view">
      <div ref="networkContainer" class="network-canvas"></div>
    </div>
    
    <div v-else class="table-view">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Uplines Section -->
        <div class="uplines-section">
          <h4 class="text-md font-semibold text-gray-900 mb-3 flex items-center">
            <span class="w-3 h-3 bg-blue-500 rounded-full mr-2"></span>
            Uplines ({{ uplines.length }})
          </h4>
          <div v-if="uplines.length === 0" class="text-sm text-gray-500">No uplines found.</div>
          <div v-else class="space-y-2">
            <div v-for="(upline, index) in uplines" :key="upline.id" 
                 class="p-3 border border-gray-200 rounded-lg hover:bg-gray-50">
              <div class="flex items-center justify-between">
                <div>
                  <div class="font-medium text-gray-900">{{ upline.fullName || upline.name }}</div>
                  <div class="text-sm text-gray-500">{{ upline.email }}</div>
                </div>
                <div class="text-right">
                  <span :class="getLevelBadgeClass(upline.level)" class="px-2 py-1 text-xs rounded-full">
                    {{ upline.level }}
                  </span>
                  <div class="text-xs text-gray-500 mt-1">Level {{ index + 1 }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Downlines Section -->
        <div class="downlines-section">
          <h4 class="text-md font-semibold text-gray-900 mb-3 flex items-center">
            <span class="w-3 h-3 bg-green-500 rounded-full mr-2"></span>
            Downlines ({{ downlines.length }})
          </h4>
          <div v-if="downlines.length === 0" class="text-sm text-gray-500">No downlines found.</div>
          <div v-else class="space-y-2">
            <div v-for="(downline, index) in downlines" :key="downline.id" 
                 class="p-3 border border-gray-200 rounded-lg hover:bg-gray-50">
              <div class="flex items-center justify-between">
                <div>
                  <div class="font-medium text-gray-900">{{ downline.fullName || downline.name }}</div>
                  <div class="text-sm text-gray-500">{{ downline.email }}</div>
                </div>
                <div class="text-right">
                  <span :class="getLevelBadgeClass(downline.level)" class="px-2 py-1 text-xs rounded-full">
                    {{ downline.level }}
                  </span>
                  <div class="text-xs text-gray-500 mt-1">Level {{ index + 1 }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { Network } from 'vis-network'
import { DataSet } from 'vis-data'

const props = defineProps({
  selectedUser: Object,
  uplines: Array,
  downlines: Array,
  loading: Boolean
})

const emit = defineEmits(['close'])

const viewMode = ref('graph') // 'graph' or 'table'
const networkContainer = ref(null)
let network = null

const toggleViewMode = () => {
  viewMode.value = viewMode.value === 'graph' ? 'table' : 'graph'
  if (viewMode.value === 'graph') {
    nextTick(() => {
      createNetwork()
    })
  }
}

const getLevelBadgeClass = (level) => {
  const classes = {
    bronze: 'bg-orange-100 text-orange-800',
    silver: 'bg-gray-100 text-gray-800',
    gold: 'bg-yellow-100 text-yellow-800',
    platinum: 'bg-purple-100 text-purple-800'
  }
  return classes[level] || 'bg-gray-100 text-gray-800'
}

const createNetwork = () => {
  if (!networkContainer.value || !props.selectedUser) return

  // Create nodes
  const nodes = new DataSet()
  const edges = new DataSet()

  // Add selected user (center node)
  nodes.add({
    id: props.selectedUser.id,
    label: props.selectedUser.fullName || props.selectedUser.name,
    title: `${props.selectedUser.email}\nLevel: ${props.selectedUser.level}`,
    color: { background: '#3B82F6', border: '#1E40AF' },
    font: { color: '#FFFFFF', size: 16 },
    shape: 'circle',
    size: 30
  })

  // Add upline nodes (blue)
  props.uplines.forEach((upline, index) => {
    nodes.add({
      id: upline.id,
      label: upline.fullName || upline.name,
      title: `${upline.email}\nLevel: ${upline.level}`,
      color: { background: '#60A5FA', border: '#3B82F6' },
      font: { color: '#FFFFFF', size: 14 },
      shape: 'circle',
      size: 25
    })
    
    // Connect upline to selected user
    edges.add({
      from: upline.id,
      to: props.selectedUser.id,
      color: { color: '#3B82F6', width: 2 },
      arrows: 'to',
      title: `Upline Level ${index + 1}`
    })
  })

  // Add downline nodes (green)
  props.downlines.forEach((downline, index) => {
    nodes.add({
      id: downline.id,
      label: downline.fullName || downline.name,
      title: `${downline.email}\nLevel: ${downline.level}`,
      color: { background: '#10B981', border: '#059669' },
      font: { color: '#FFFFFF', size: 14 },
      shape: 'circle',
      size: 25
    })
    
    // Connect selected user to downline
    edges.add({
      from: props.selectedUser.id,
      to: downline.id,
      color: { color: '#10B981', width: 2 },
      arrows: 'to',
      title: `Downline Level ${index + 1}`
    })
  })

  // Network options
  const options = {
    nodes: {
      borderWidth: 2,
      shadow: true
    },
    edges: {
      shadow: true,
      smooth: {
        type: 'continuous',
        forceDirection: 'none'
      }
    },
    physics: {
      enabled: true,
      barnesHut: {
        gravitationalConstant: -2000,
        springConstant: 0.04,
        springLength: 200
      },
      stabilization: {
        enabled: true,
        iterations: 1000
      }
    },
    interaction: {
      hover: true,
      tooltipDelay: 200
    },
    layout: {
      hierarchical: {
        enabled: true,
        direction: 'UD',
        sortMethod: 'directed',
        nodeSpacing: 150,
        levelSeparation: 200
      }
    }
  }

  // Create network
  network = new Network(networkContainer.value, { nodes, edges }, options)
  
  // Fit network to container
  network.once('stabilized', () => {
    network.fit()
  })
}

// Watch for data changes
watch([() => props.uplines, () => props.downlines], () => {
  if (viewMode.value === 'graph' && network) {
    createNetwork()
  }
}, { deep: true })

onMounted(() => {
  if (viewMode.value === 'graph') {
    createNetwork()
  }
})
</script>

<style scoped>
.network-graph-container {
  @apply bg-white rounded-lg p-6 w-full max-w-6xl mx-4 max-h-[90vh] overflow-y-auto;
}

.graph-header {
  @apply flex justify-between items-center mb-4;
}

.graph-view {
  @apply h-96;
}

.network-canvas {
  @apply w-full h-full border border-gray-200 rounded-lg;
}

.table-view {
  @apply max-h-96 overflow-y-auto;
}

.uplines-section, .downlines-section {
  @apply bg-gray-50 p-4 rounded-lg;
}
</style>
