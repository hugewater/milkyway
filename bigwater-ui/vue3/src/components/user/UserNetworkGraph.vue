<template>
  <div class="bg-white rounded-lg shadow-xl max-w-6xl w-full mx-4 max-h-[90vh] overflow-hidden resize overflow-auto" 
       ref="modalContainer"
       :style="{ minWidth: '800px', minHeight: '600px' }">
    <!-- Header -->
    <div class="flex items-center justify-between p-6 border-b border-gray-200">
      <div>
        <h2 class="text-2xl font-bold text-deep-ocean">Team Network Graph</h2>
        <p class="text-gray-600 mt-1">Visual representation of your downline structure</p>
      </div>
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
    <div class="p-6 flex flex-col h-full">
      <!-- Toggle Buttons -->
      <div class="flex justify-center mb-6">
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
      <div v-if="viewMode === 'graph'" class="w-full flex-1 min-h-0">
        <div ref="chartContainer" id="chartContainer" class="w-full h-full" style="min-height: 400px;"></div>
      </div>

      <!-- Table View -->
      <div v-else class="overflow-x-auto flex-1 min-h-0">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Member</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Level</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                <button @click="sortByReferral" class="inline-flex items-center gap-1 hover:text-gray-700">
                  Referral Code
                  <span v-if="sortKey === 'referralCode'">{{ sortAsc ? '▲' : '▼' }}</span>
                </button>
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="downline in sortedDownlines" :key="downline.id" class="hover:bg-gray-50">
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
                    <div class="text-xs text-gray-500 mt-1">Level {{ downline.networkLevel }}</div>
                    <div class="text-xs text-gray-500 mt-1" title="Referral Code: {{ downline.referralCode || 'N/A' }}">
                      Code: {{ downline.referralCode ? downline.referralCode.substring(0, 6) + '...' : 'N/A' }}
                    </div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                      :class="getLevelBadgeColor(downline.networkLevel)">
                  Level {{ downline.networkLevel }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ downline.email }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-900">
                {{ downline.referralCode || 'N/A' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Footer Actions -->
      <div class="mt-4 pt-4 border-t border-gray-200 flex justify-end">
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
import * as echarts from 'echarts'

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

const emit = defineEmits(['close'])

const viewMode = ref('graph')
const chartContainer = ref(null)
const modalContainer = ref(null)
let chart = null
let resizeObserver = null
let keepAliveTimer = null

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

const getNodeLevel = (data) => {
  // Root node uses numeric user id
  if (data.value === props.selectedUser.id) return 'You'
  if (typeof data.value === 'string' && data.value.startsWith('level1_')) return 'Level 1'
  if (typeof data.value === 'string' && data.value.startsWith('level2_')) return 'Level 2'
  if (typeof data.value === 'string' && data.value.startsWith('level3_')) return 'Level 3'
  return 'Member'
}

const buildSeries = (treeData) => ({
  name: 'Team Network',
  type: 'tree',
  data: [treeData],
  top: '10%',
  left: '5%',
  bottom: '5%',
  right: '5%',
  symbol: 'emptyCircle',
  symbolSize: 8,
  orient: 'LR',
  initialTreeDepth: -1,
  roam: true,
  label: {
    show: true,
    position: 'left',
    verticalAlign: 'middle',
    align: 'right',
    fontSize: 12,
    color: '#374151',
    formatter: (params) => {
      const d = params.data || {}
      return d.referralCode || ''
    }
  },
  leaves: {
    label: {
      position: 'right',
      verticalAlign: 'middle',
      align: 'left',
      fontSize: 12,
      color: '#374151',
      formatter: (params) => {
        const d = params.data || {}
        return d.referralCode || ''
      }
    }
  },
  emphasis: {
    focus: 'descendant',
    lineStyle: { width: 3 }
  },
  expandAndCollapse: true,
  animationDuration: 550,
  animationDurationUpdate: 750,
  lineStyle: {
    color: '#d1d5db',
    width: 1.5
  },
  edgeShape: 'curve',
  edgeForkPosition: '50%'
})

const applyTreeOption = (treeData) => {
  if (!chart) return
  const option = {
    backgroundColor: '#f8fafc',
    title: {
      text: 'Team Network Graph',
      left: 'center',
      top: 20,
      textStyle: { color: '#1e40af', fontSize: 18 }
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        const d = params.data || {}
        const email = d.email || 'N/A'
        return `<div style="padding:8px; line-height:1.4; font-weight:600;">${email}</div>`
      }
    },
    series: [buildSeries(treeData)]
  }
  // notMerge=true, replaceMerge series
  chart.setOption(option, true)
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
    itemStyle: { color: tag === 'l1' ? '#60a5fa' : tag === 'l2' ? '#34d399' : '#fbbf24' },
    label: { fontSize: 12, color: '#374151' },
    symbolSize: 8,
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
        n2.children.push(makeNode(m3, 'l3'))
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

      console.log('Creating ECharts instance...')
      const container = chartContainer.value
      console.log('Container element:', container)
      console.log('Container dimensions:', {
        offsetWidth: container.offsetWidth,
        offsetHeight: container.offsetHeight,
        clientWidth: container.clientWidth,
        clientHeight: container.clientHeight,
        scrollWidth: container.scrollWidth,
        scrollHeight: container.scrollHeight
      })
      
      chart = echarts.init(chartContainer.value)
      console.log('ECharts instance created:', chart)
      
      const treeData = prepareTreeData()
      console.log('Tree data prepared:', treeData)
      console.log('Tree data children count:', treeData.children ? treeData.children.length : 0)
      
      console.log('Applying tree option...')
      try {
        applyTreeOption(treeData)
        console.log('Tree chart option applied')
      } catch (error) {
        console.error('Error setting tree chart option:', error)
        
        console.log('Falling back to simple pie chart...')
        const pieOption = {
          backgroundColor: '#f8fafc',
          title: {
            text: 'Test Pie Chart - ECharts Test',
            left: 'center',
            top: 20,
            textStyle: { color: '#1e40af', fontSize: 18 }
          },
          series: [
            {
              name: 'Test Data',
              type: 'pie',
              radius: '50%',
              center: ['50%', '60%'],
              data: [
                { value: 1, name: 'You' },
                { value: 1, name: 'Level 1' },
                { value: 2, name: 'Level 2' },
                { value: 1, name: 'Level 3' }
              ]
            }
          ]
        }
        
        try {
          chart.setOption(pieOption)
          console.log('Pie chart set successfully')
        } catch (pieError) {
          console.error('Even pie chart failed:', pieError)
          chartContainer.value.innerHTML = `
            <div style="display: flex; align-items: center; justify-content: center; height: 100%; font-size: 18px; color: #666;">
              <div>
                <h3>Chart Loading Failed</h3>
                <p>Tree data: ${treeData.children ? treeData.children.length : 0} children</p>
                <p>Please check console for details</p>
              </div>
            </div>
          `
        }
      }
      
      // Keep-alive reinforcement to prevent disappearance
      setTimeout(() => {
        if (chart) {
          console.log('Reinforce update @300ms')
          applyTreeOption(treeData)
          chart.resize()
        }
      }, 300)

      keepAliveTimer && clearInterval(keepAliveTimer)
      keepAliveTimer = setInterval(() => {
        if (!chart || !chartContainer.value) return
        const sizeOk = chartContainer.value.clientWidth > 0 && chartContainer.value.clientHeight > 0
        if (sizeOk) {
          chart.resize()
        }
        const opt = chart.getOption()
        if (!opt || !opt.series || opt.series.length === 0) {
          console.log('Series missing; reapply tree option')
          applyTreeOption(treeData)
        }
      }, 2000)
      
      // Handle container resize
      const handleResize = () => {
        if (!chart) return
        console.log('Resizing chart...')
        chart.resize()
        // After resize, re-apply series to be safe
        const td = prepareTreeData()
        applyTreeOption(td)
      }
      
      if (modalContainer.value && window.ResizeObserver) {
        resizeObserver = new ResizeObserver(handleResize)
        resizeObserver.observe(modalContainer.value)
      }
      
      window.addEventListener('resize', handleResize)
      
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
      const treeData = prepareTreeData()
      const seriesUpdate = {
        name: 'Team Network',
        type: 'tree',
        data: [treeData],
        top: '10%',
        left: '5%',
        bottom: '5%',
        right: '5%',
        symbolSize: 25,
        orient: 'TB',
        initialTreeDepth: -1,
        roam: true,
        label: {
          show: true,
          position: 'left',
          verticalAlign: 'middle',
          align: 'right',
          fontSize: 14,
          color: '#333',
          backgroundColor: 'rgba(255,255,255,0.8)',
          padding: [4, 8],
          borderRadius: 4
        },
        leaves: {
          label: {
            position: 'right',
            verticalAlign: 'middle',
            align: 'left',
            backgroundColor: 'rgba(255,255,255,0.8)',
            padding: [4, 8],
            borderRadius: 4
          }
        },
        emphasis: {
          focus: 'descendant',
          lineStyle: { width: 3 }
        },
        expandAndCollapse: true,
        animationDuration: 550,
        animationDurationUpdate: 750,
        lineStyle: {
          color: '#ccc',
          width: 2,
          curveness: 0.1
        }
      }
      // Use notMerge=true to avoid accidental series clearing by merge
      chart.setOption({ series: [seriesUpdate] }, true)
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
  if (chart) {
    chart.dispose()
  }
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
}
</style>

 