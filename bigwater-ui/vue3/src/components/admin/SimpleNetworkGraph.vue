<template>
  <div class="network-graph-container">
    <div class="graph-header">
      <h3 class="text-lg font-bold text-deep-ocean">Network Graph - {{ selectedUser?.fullName || selectedUser?.name }}</h3>
      <div class="flex space-x-2">
        <button @click="toggleViewMode" class="px-3 py-1 text-sm bg-ocean text-white rounded hover:bg-deep-ocean">
          {{ viewMode === 'graph' ? 'Show Table' : 'Show Graph' }}
        </button>
        <button @click="resetPositions" class="px-3 py-1 text-sm bg-gray-500 text-white rounded hover:bg-gray-600">
          Reset
        </button>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">✕</button>
      </div>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center h-64">
      <div class="text-gray-500">Loading network data...</div>
    </div>
    
    <div v-else-if="viewMode === 'graph'" class="graph-view">
      <div class="network-canvas" ref="canvasRef">
        <svg :width="svgWidth" :height="svgHeight" class="w-full h-full">
          <!-- Background grid -->
          <defs>
            <pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse">
              <path d="M 20 0 L 0 0 0 20" fill="none" stroke="#f3f4f6" stroke-width="1"/>
            </pattern>
          </defs>
          <rect width="100%" height="100%" fill="url(#grid)" />
          
          <!-- Connections -->
          <g v-for="connection in connections" :key="connection.id">
            <line
              :x1="connection.x1"
              :y1="connection.y1"
              :x2="connection.x2"
              :y2="connection.y2"
              :stroke="connection.color"
              stroke-width="2"
              marker-end="url(#arrowhead)"
            />
          </g>
          
          <!-- Nodes -->
          <g v-for="node in nodes" :key="node.id">
            <!-- Node circle - with drag events -->
            <circle
              :cx="node.x"
              :cy="node.y"
              :r="node.radius"
              :fill="node.color"
              :stroke="node.borderColor"
              stroke-width="2"
              class="cursor-move hover:opacity-80 transition-opacity"
              @mousedown="startDrag(node, $event)"
              @mousemove="onDrag($event)"
              @mouseup="stopDrag"
              @mouseleave="stopDrag"
            />
            
            <!-- Node label -->
            <text
              :x="node.x"
              :y="node.y + node.radius + 20"
              text-anchor="middle"
              class="text-xs font-medium fill-gray-700 pointer-events-none"
            >
              {{ node.label }}
            </text>
            
            <!-- Node type indicator -->
            <circle
              :cx="node.x + node.radius - 5"
              :cy="node.y - node.radius + 5"
              r="4"
              :fill="node.typeColor"
              class="pointer-events-none"
            />
          </g>
          
          <!-- Arrow marker -->
          <defs>
            <marker
              id="arrowhead"
              markerWidth="10"
              markerHeight="7"
              refX="9"
              refY="3.5"
              orient="auto"
            >
              <polygon points="0 0, 10 3.5, 0 7" fill="#6b7280" />
            </marker>
          </defs>
        </svg>
      </div>
      
      <!-- Legend -->
      <div class="mt-4 flex justify-center space-x-6 text-sm">
        <div class="flex items-center space-x-2">
          <div class="w-4 h-4 bg-blue-500 rounded-full"></div>
          <span>Selected User</span>
        </div>
        <div class="flex items-center space-x-2">
          <div class="w-4 h-4 bg-blue-300 rounded-full"></div>
          <span>Upline</span>
        </div>
        <div class="flex items-center space-x-2">
          <div class="w-4 h-4 bg-green-500 rounded-full"></div>
          <span>Downlines</span>
        </div>
      </div>
    </div>
    
    <div v-else class="table-view">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Uplines Section -->
        <div class="uplines-section">
          <h4 class="text-md font-semibold text-gray-900 mb-3 flex items-center">
            <span class="w-3 h-3 bg-blue-500 rounded-full mr-2"></span>
            Upline ({{ uplines.length }})
          </h4>
          <div v-if="uplines.length === 0" class="text-sm text-gray-500">No upline found.</div>
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
                  <div class="text-xs text-gray-500 mt-1">Direct Upline</div>
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
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  selectedUser: Object,
  uplines: Array,
  downlines: Array,
  loading: Boolean
})

const emit = defineEmits(['close'])

const viewMode = ref('graph')
const svgWidth = ref(800)
const svgHeight = ref(600)
const canvasRef = ref(null)

// Drag functionality
const isDragging = ref(false)
const draggedNode = ref(null)
const dragOffset = ref({ x: 0, y: 0 })

const getLevelBadgeClass = (level) => {
  const classes = {
    CHIEF: 'bg-orange-100 text-orange-800',
    MAYOR: 'bg-gray-100 text-gray-800',
    GOVERNOR: 'bg-yellow-100 text-yellow-800',
    MINISTER: 'bg-purple-100 text-purple-800',
    PRESIDENT: 'bg-blue-100 text-blue-800'
  }
  return classes[level] || 'bg-gray-100 text-gray-800'
}

const nodes = computed(() => {
  const nodeList = []
  const centerX = svgWidth.value / 2
  const centerY = svgHeight.value / 2
  
  // Add selected user (center)
  if (props.selectedUser) {
    nodeList.push({
      id: props.selectedUser.id,
      label: props.selectedUser.fullName || props.selectedUser.name || 'User',
      x: centerX,
      y: centerY,
      radius: 30,
      color: '#3B82F6',
      borderColor: '#1E40AF',
      typeColor: '#FFFFFF'
    })
  }
  
  // Add upline (above center) - only one upline
  if (props.uplines.length > 0) {
    const upline = props.uplines[0] // Only take the first upline
    nodeList.push({
      id: upline.id,
      label: upline.fullName || upline.name || 'Upline',
      x: centerX,
      y: centerY - 100, // Directly above center
      radius: 25,
      color: '#60A5FA',
      borderColor: '#3B82F6',
      typeColor: '#FFFFFF'
    })
  }
  
  // Add downlines (below center) - spread them out
  props.downlines.forEach((downline, index) => {
    const totalDownlines = props.downlines.length
    const angle = (Math.PI / (totalDownlines + 1)) * (index + 1)
    const distance = 120
    nodeList.push({
      id: downline.id,
      label: downline.fullName || downline.name || 'Downline',
      x: centerX + Math.cos(angle) * distance,
      y: centerY + Math.sin(angle) * distance + 80,
      radius: 25,
      color: '#10B981',
      borderColor: '#059669',
      typeColor: '#FFFFFF'
    })
  })
  
  return nodeList
})

const connections = computed(() => {
  const connectionList = []
  const centerNode = nodes.value.find(n => n.id === props.selectedUser?.id)
  
  if (!centerNode) return connectionList
  
  // Connect upline to center (only one upline)
  if (props.uplines.length > 0) {
    const uplineNode = nodes.value.find(n => n.id === props.uplines[0].id)
    if (uplineNode) {
      connectionList.push({
        id: `upline-${props.uplines[0].id}`,
        x1: uplineNode.x,
        y1: uplineNode.y,
        x2: centerNode.x,
        y2: centerNode.y,
        color: '#3B82F6'
      })
    }
  }
  
  // Connect center to downlines
  props.downlines.forEach((downline, index) => {
    const downlineNode = nodes.value.find(n => n.id === downline.id)
    if (downlineNode) {
      connectionList.push({
        id: `downline-${downline.id}`,
        x1: centerNode.x,
        y1: centerNode.y,
        x2: downlineNode.x,
        y2: downlineNode.y,
        color: '#10B981'
      })
    }
  })
  
  return connectionList
})

const startDrag = (node, event) => {
  console.log('Start drag for node:', node.id)
  isDragging.value = true
  draggedNode.value = node
  
  const rect = canvasRef.value.getBoundingClientRect()
  dragOffset.value = {
    x: event.clientX - rect.left - node.x,
    y: event.clientY - rect.top - node.y
  }
  
  event.preventDefault()
}

const onDrag = (event) => {
  if (!isDragging.value || !draggedNode.value) return
  
  const rect = canvasRef.value.getBoundingClientRect()
  const newX = event.clientX - rect.left - dragOffset.value.x
  const newY = event.clientY - rect.top - dragOffset.value.y
  
  // Keep node within bounds
  const minX = draggedNode.value.radius
  const maxX = svgWidth.value - draggedNode.value.radius
  const minY = draggedNode.value.radius
  const maxY = svgHeight.value - draggedNode.value.radius
  
  draggedNode.value.x = Math.max(minX, Math.min(maxX, newX))
  draggedNode.value.y = Math.max(minY, Math.min(maxY, newY))
}

const stopDrag = () => {
  if (isDragging.value) {
    console.log('Stop drag for node:', draggedNode.value?.id)
  }
  isDragging.value = false
  draggedNode.value = null
}

const resetPositions = () => {
  // This will trigger a re-computation of node positions
  // by forcing a reactive update
  const temp = svgWidth.value
  svgWidth.value = temp
}

const toggleViewMode = () => {
  viewMode.value = viewMode.value === 'graph' ? 'table' : 'graph'
}

const selectNode = (node) => {
  console.log('Selected node:', node)
  // 这里可以添加点击节点的功能，比如显示详细信息
}

onMounted(() => {
  // 调整SVG尺寸以适应容器
  if (canvasRef.value) {
    const rect = canvasRef.value.getBoundingClientRect()
    svgWidth.value = rect.width || 800
    svgHeight.value = rect.height || 600
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
  @apply w-full h-full border border-gray-200 rounded-lg bg-white;
}

.table-view {
  @apply max-h-96 overflow-y-auto;
}

.uplines-section, .downlines-section {
  @apply bg-gray-50 p-4 rounded-lg;
}

/* Prevent text selection during drag */
.network-canvas {
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}
</style>
