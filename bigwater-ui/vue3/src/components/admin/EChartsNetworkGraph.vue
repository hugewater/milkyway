<template>
  <div class="echarts-network-container" ref="containerRef" :style="containerStyle" :class="{ 'fullscreen': isFullscreen }">
    <div class="graph-header" :class="{ 'fullscreen-header': isFullscreen }">
      <h3 class="text-lg font-bold text-deep-ocean">Network Tree - {{ selectedUser?.fullName || selectedUser?.name }}</h3>
      <div class="flex space-x-2">
        <button @click="toggleViewMode" class="px-3 py-1 text-sm bg-ocean text-white rounded hover:bg-ocean-dark">
          {{ viewMode === 'graph' ? 'Show Table' : 'Show Tree' }}
        </button>
        <button @click="resetGraph" class="px-3 py-1 text-sm bg-gray-500 text-white rounded hover:bg-gray-600">
          Reset
        </button>
        <button @click="toggleFullscreen" class="px-3 py-1 text-sm bg-purple-500 text-white rounded hover:bg-purple-600">
          {{ isFullscreen ? 'Exit Fullscreen' : 'Fullscreen' }}
        </button>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">✕</button>
      </div>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center h-64">
      <div class="text-gray-500">Loading network data...</div>
    </div>
    
    <div v-else-if="viewMode === 'graph'" class="graph-view" :style="graphViewStyle">
      <v-chart 
        class="network-chart" 
        :option="chartOption" 
        :autoresize="true"
        @click="onNodeClick"
        ref="chartRef"
      />
      
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
    
    <!-- Resize handles (only show when not fullscreen) -->
    <template v-if="!isFullscreen">
      <div class="resize-handle resize-handle-n" @mousedown="startResize('n')"></div>
      <div class="resize-handle resize-handle-s" @mousedown="startResize('s')"></div>
      <div class="resize-handle resize-handle-e" @mousedown="startResize('e')"></div>
      <div class="resize-handle resize-handle-w" @mousedown="startResize('w')"></div>
      <div class="resize-handle resize-handle-ne" @mousedown="startResize('ne')"></div>
      <div class="resize-handle resize-handle-nw" @mousedown="startResize('nw')"></div>
      <div class="resize-handle resize-handle-se" @mousedown="startResize('se')"></div>
      <div class="resize-handle resize-handle-sw" @mousedown="startResize('sw')"></div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick, onUnmounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { TreeChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  TreeChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const props = defineProps({
  selectedUser: Object,
  uplines: Array,
  downlines: Array,
  loading: Boolean
})

const emit = defineEmits(['close'])

const viewMode = ref('graph')
const chartInstance = ref(null)
const containerRef = ref(null)
const chartRef = ref(null)

// 窗口大小状态
const isFullscreen = ref(false)
const windowSize = ref({
  width: 800,
  height: 600
})
const isResizing = ref(false)
const resizeDirection = ref('')
const resizeStartPos = ref({ x: 0, y: 0 })
const resizeStartSize = ref({ width: 0, height: 0 })

// 最小和最大尺寸
const MIN_WIDTH = 400
const MIN_HEIGHT = 300
const MAX_WIDTH = 1200
const MAX_HEIGHT = 800

const containerStyle = computed(() => {
  if (isFullscreen.value) {
    return {
      width: '100vw',
      height: '100vh',
      position: 'fixed',
      top: 0,
      left: 0,
      zIndex: 9999,
      borderRadius: 0
    }
  }
  
  return {
    width: `${windowSize.value.width}px`,
    height: `${windowSize.value.height}px`,
    minWidth: `${MIN_WIDTH}px`,
    minHeight: `${MIN_HEIGHT}px`,
    maxWidth: `${MAX_WIDTH}px`,
    maxHeight: `${MAX_HEIGHT}px`
  }
})

const graphViewStyle = computed(() => ({
  height: isFullscreen.value ? 'calc(100vh - 120px)' : 'calc(100% - 120px)'
}))

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

// 生成树形数据
const generateTreeData = () => {
  if (!props.selectedUser) return null
  
  // 创建根节点（选中的用户）
  const rootNode = {
    name: props.selectedUser.fullName || props.selectedUser.name || 'User',
    value: props.selectedUser.id,
    itemStyle: {
      color: '#3B82F6',
      borderColor: '#1E40AF',
      borderWidth: 2
    },
    label: {
      color: '#1F2937',
      fontSize: 14,
      fontWeight: 'bold'
    },
    children: []
  }
  
  // 添加上线（父节点）
  if (props.uplines.length > 0) {
    const upline = props.uplines[0]
    const uplineNode = {
      name: upline.fullName || upline.name || 'Upline',
      value: upline.id,
      itemStyle: {
        color: '#60A5FA',
        borderColor: '#3B82F6',
        borderWidth: 2
      },
      label: {
        color: '#1F2937',
        fontSize: 12
      },
      children: [rootNode] // 上线作为父节点，包含当前用户
    }
    
    // 添加下线（子节点）到当前用户
    props.downlines.forEach((downline, index) => {
      rootNode.children.push({
        name: downline.fullName || downline.name || 'Downline',
        value: downline.id,
        itemStyle: {
          color: '#10B981',
          borderColor: '#059669',
          borderWidth: 2
        },
        label: {
          color: '#1F2937',
          fontSize: 12
        }
      })
    })
    
    return uplineNode // 返回上线作为根节点
  } else {
    // 如果没有上线，直接添加下线到当前用户
    props.downlines.forEach((downline, index) => {
      rootNode.children.push({
        name: downline.fullName || downline.name || 'Downline',
        value: downline.id,
        itemStyle: {
          color: '#10B981',
          borderColor: '#059669',
          borderWidth: 2
        },
        label: {
          color: '#1F2937',
          fontSize: 12
        }
      })
    })
    
    return rootNode
  }
}

// 图表配置
const chartOption = computed(() => {
  const treeData = generateTreeData()
  
  if (!treeData) {
    return {
      title: {
        text: 'No Data Available',
        left: 'center',
        top: 'center',
        textStyle: {
          color: '#6B7280',
          fontSize: 16
        }
      }
    }
  }
  
  return {
    title: {
      text: 'Network Tree',
      subtext: `${props.selectedUser?.fullName || props.selectedUser?.name || 'User'}'s Network`,
      top: 'top',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#1F2937'
      },
      subtextStyle: {
        fontSize: 12,
        color: '#6B7280'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        return `
          <div class="p-2">
            <div class="font-bold">${params.data.name}</div>
            <div class="text-sm text-gray-600">ID: ${params.data.value}</div>
            <div class="text-sm text-gray-600">Type: ${getNodeType(params.data)}</div>
          </div>
        `
      }
    },
    legend: {
      data: ['Selected User', 'Upline', 'Downlines'],
      top: 50,
      left: 'center',
      textStyle: {
        fontSize: 12
      }
    },
    series: [
      {
        name: 'Network Tree',
        type: 'tree',
        data: [treeData],
        top: 80,
        left: '10%',
        bottom: '10%',
        right: '10%',
        symbolSize: 12,
        orient: 'horizontal',
        label: {
          position: 'left',
          verticalAlign: 'middle',
          align: 'right',
          fontSize: 12
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
        animationDurationUpdate: 750,
        initialTreeDepth: 3, // 显示所有层级
        lineStyle: {
          color: '#D1D5DB',
          width: 1,
          curveness: 0.5
        },
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1
        }
      }
    ]
  }
})

const getNodeType = (node) => {
  if (node.itemStyle?.color === '#3B82F6') return 'Selected User'
  if (node.itemStyle?.color === '#60A5FA') return 'Upline'
  if (node.itemStyle?.color === '#10B981') return 'Downline'
  return 'Unknown'
}

const onNodeClick = (params) => {
  if (params.dataType === 'node') {
    console.log('Clicked node:', params.data)
    // 这里可以添加点击节点的功能，比如显示详细信息
  }
}

const toggleViewMode = () => {
  viewMode.value = viewMode.value === 'graph' ? 'table' : 'graph'
}

const resetGraph = () => {
  // ECharts会自动重新布局
  if (chartRef.value) {
    chartRef.value.resize()
  }
}

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value
  
  if (isFullscreen.value) {
    // 进入全屏模式
    if (containerRef.value) {
      // 尝试使用浏览器全屏API
      if (containerRef.value.requestFullscreen) {
        containerRef.value.requestFullscreen()
      } else if (containerRef.value.webkitRequestFullscreen) {
        containerRef.value.webkitRequestFullscreen()
      } else if (containerRef.value.msRequestFullscreen) {
        containerRef.value.msRequestFullscreen()
      }
    }
    
    // 隐藏滚动条
    document.body.style.overflow = 'hidden'
  } else {
    // 退出全屏模式
    if (document.fullscreenElement) {
      if (document.exitFullscreen) {
        document.exitFullscreen()
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen()
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen()
      }
    }
    
    // 恢复滚动条
    document.body.style.overflow = ''
  }
  
  nextTick(() => {
    if (chartRef.value) {
      chartRef.value.resize()
    }
  })
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  if (!document.fullscreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement) {
    // 用户通过ESC键或其他方式退出全屏
    isFullscreen.value = false
    document.body.style.overflow = ''
    
    nextTick(() => {
      if (chartRef.value) {
        chartRef.value.resize()
      }
    })
  }
}

// 调整大小功能
const startResize = (direction) => {
  isResizing.value = true
  resizeDirection.value = direction
  resizeStartPos.value = { x: event.clientX, y: event.clientY }
  resizeStartSize.value = { ...windowSize.value }
  
  document.addEventListener('mousemove', onResize)
  document.addEventListener('mouseup', stopResize)
}

const onResize = (event) => {
  if (!isResizing.value) return
  
  const deltaX = event.clientX - resizeStartPos.value.x
  const deltaY = event.clientY - resizeStartPos.value.y
  
  let newWidth = resizeStartSize.value.width
  let newHeight = resizeStartSize.value.height
  
  // 根据调整方向计算新尺寸
  if (resizeDirection.value.includes('e')) {
    newWidth = Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, resizeStartSize.value.width + deltaX))
  }
  if (resizeDirection.value.includes('w')) {
    newWidth = Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, resizeStartSize.value.width - deltaX))
  }
  if (resizeDirection.value.includes('s')) {
    newHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, resizeStartSize.value.height + deltaY))
  }
  if (resizeDirection.value.includes('n')) {
    newHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, resizeStartSize.value.height - deltaY))
  }
  
  windowSize.value = { width: newWidth, height: newHeight }
}

const stopResize = () => {
  isResizing.value = false
  resizeDirection.value = ''
  
  document.removeEventListener('mousemove', onResize)
  document.removeEventListener('mouseup', stopResize)
  
  // 重新调整图表大小
  nextTick(() => {
    if (chartRef.value) {
      chartRef.value.resize()
    }
  })
}

onMounted(() => {
  // 监听全屏状态变化
  document.addEventListener('fullscreenchange', handleFullscreenChange)
  document.addEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.addEventListener('msfullscreenchange', handleFullscreenChange)
  
  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    if (isFullscreen.value) {
      nextTick(() => {
        if (chartRef.value) {
          chartRef.value.resize()
        }
      })
    }
  })
})

// 组件卸载时清理事件监听器
onUnmounted(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
  document.removeEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.removeEventListener('msfullscreenchange', handleFullscreenChange)
  
  // 确保退出全屏
  if (isFullscreen.value) {
    document.body.style.overflow = ''
  }
})

// 监听数据变化，重新渲染图表
watch([() => props.selectedUser, () => props.uplines, () => props.downlines], () => {
  // 数据变化时会自动重新计算 chartOption
}, { deep: true })
</script>

<style scoped>
.echarts-network-container {
  @apply bg-white rounded-lg p-6 relative;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
}

.echarts-network-container.fullscreen {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  z-index: 9999 !important;
  border-radius: 0 !important;
  box-shadow: none !important;
  border: none !important;
}

.graph-header {
  @apply flex justify-between items-center mb-4;
}

.graph-header.fullscreen-header {
  @apply p-4 bg-white border-b border-gray-200;
  position: sticky;
  top: 0;
  z-index: 10;
}

.graph-view {
  @apply relative;
}

.network-chart {
  @apply w-full h-full border border-gray-200 rounded-lg bg-white;
}

.table-view {
  @apply max-h-96 overflow-y-auto;
}

.uplines-section, .downlines-section {
  @apply bg-gray-50 p-4 rounded-lg;
}

/* ECharts 容器样式 */
:deep(.echarts) {
  width: 100%;
  height: 100%;
}

/* 调整大小手柄 */
.resize-handle {
  position: absolute;
  background: transparent;
  z-index: 10;
}

.resize-handle-n {
  top: 0;
  left: 10px;
  right: 10px;
  height: 6px;
  cursor: n-resize;
}

.resize-handle-s {
  bottom: 0;
  left: 10px;
  right: 10px;
  height: 6px;
  cursor: s-resize;
}

.resize-handle-e {
  top: 10px;
  bottom: 10px;
  right: 0;
  width: 6px;
  cursor: e-resize;
}

.resize-handle-w {
  top: 10px;
  bottom: 10px;
  left: 0;
  width: 6px;
  cursor: w-resize;
}

.resize-handle-ne {
  top: 0;
  right: 0;
  width: 10px;
  height: 10px;
  cursor: ne-resize;
}

.resize-handle-nw {
  top: 0;
  left: 0;
  width: 10px;
  height: 10px;
  cursor: nw-resize;
}

.resize-handle-se {
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  cursor: se-resize;
}

.resize-handle-sw {
  bottom: 0;
  left: 0;
  width: 10px;
  height: 10px;
  cursor: sw-resize;
}

/* 悬停效果 */
.resize-handle:hover {
  background: rgba(59, 130, 246, 0.1);
}

.resize-handle-n:hover,
.resize-handle-s:hover {
  background: linear-gradient(to right, transparent, rgba(59, 130, 246, 0.1), transparent);
}

.resize-handle-e:hover,
.resize-handle-w:hover {
  background: linear-gradient(to bottom, transparent, rgba(59, 130, 246, 0.1), transparent);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .echarts-network-container {
    @apply p-4;
  }
  
  .graph-header {
    @apply flex-col space-y-2;
  }
  
  .graph-header > div {
    @apply w-full justify-center;
  }
  
  .resize-handle {
    display: none;
  }
}

/* 全屏模式下的移动端适配 */
@media (max-width: 768px) {
  .echarts-network-container.fullscreen {
    @apply p-2;
  }
  
  .graph-header.fullscreen-header {
    @apply p-2;
  }
}
</style>
