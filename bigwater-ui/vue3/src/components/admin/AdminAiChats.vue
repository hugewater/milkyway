<template>
  <AppLayout>
    <div class="h-[calc(100vh-80px)] flex">
      <!-- Left sidebar: conversations list -->
      <div class="w-72 border-r bg-white flex flex-col">
        <div class="p-4 border-b flex items-center justify-between">
          <h2 class="text-lg font-semibold text-deep-ocean">Chats</h2>
          <button @click="newChat" class="px-2 py-1 text-xs border rounded hover:bg-gray-50">New</button>
        </div>
        <div class="p-3">
          <input v-model="q" @input="debouncedReload" placeholder="Search chats" class="w-full border rounded px-3 py-2 text-sm" />
        </div>
        <div class="flex-1 overflow-y-auto">
          <div v-for="c in chats" :key="c.id" @click="selectChat(c)"
               class="px-4 py-3 cursor-pointer border-b hover:bg-gray-50"
               :class="{ 'bg-blue-50': currentChat && currentChat.id===c.id }">
            <div class="text-sm font-medium text-gray-900 truncate">{{ c.title }}</div>
            <div class="text-xs text-gray-500 truncate">{{ c.agentName }} · {{ formatDate(c.updatedAt) }}</div>
          </div>
        </div>
        <div class="p-3 border-t">
          <label class="text-xs text-gray-500">Active Agent</label>
          <select v-model="activeAgentId" class="mt-1 w-full border rounded px-2 py-2 text-sm">
            <option v-for="a in agents" :key="a.id" :value="a.id">{{ a.name }} ({{ a.model }})</option>
          </select>
        </div>
      </div>

      <!-- Chat area -->
      <div class="flex-1 flex flex-col bg-gray-50">
        <div class="px-6 py-4 border-b bg-white flex items-center justify-between">
          <div class="flex items-center gap-3">
            <h1 class="text-lg font-semibold text-deep-ocean">{{ currentChat ? currentChat.title : 'New Chat' }}</h1>
            <span v-if="currentChat" class="text-xs text-gray-500">· {{ currentChat.agentName }} ({{ currentChat.agentModel }})</span>
          </div>
          <div class="space-x-2">
            <button v-if="currentChat" @click="renameChat" class="text-sm text-ocean hover:text-deep-ocean">Rename</button>
            <button v-if="currentChat" @click="deleteCurrent" class="text-sm text-red-600 hover:text-red-700">Delete</button>
          </div>
        </div>

        <div class="flex-1 overflow-y-auto p-6">
          <div v-if="messages.length===0" class="text-center text-gray-500 text-sm mt-10">No messages yet. Start typing below.</div>
          <div class="space-y-4 max-w-3xl mx-auto">
            <div v-for="m in messages" :key="m.id" class="flex" :class="m.role==='user' ? 'justify-end' : 'justify-start'">
              <div class="px-4 py-2 rounded-2xl text-sm max-w-[80%]"
                   :class="m.role==='user' ? 'bg-blue-600 text-white' : 'bg-white border'">
                <div class="whitespace-pre-wrap">{{ m.content }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="p-4 border-t bg-white">
          <div class="max-w-3xl mx-auto flex items-end gap-3">
            <textarea v-model="draft" rows="1" @keydown.enter.exact.prevent="send"
                      placeholder="Message..."
                      class="flex-1 border rounded-xl px-4 py-3 text-sm resize-y min-h-[48px] max-h-48"></textarea>
            <button @click="send" class="px-4 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700">Send</button>
          </div>
        </div>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppLayout from '../layouts/AppLayout.vue'
import apiService from '../../utils/api.js'

const chats = ref([])
const agents = ref([])
const currentChat = ref(null)
const messages = ref([])
const draft = ref('')
const q = ref('')
const activeAgentId = ref(null)
let debounceTimer

const formatDate = (d) => { try { return new Date(d).toLocaleString() } catch { return d } }

const loadAgents = async () => {
  const resp = await apiService.listAgents({ enabled: true, status: 'ACTIVE' })
  agents.value = (resp && resp.data) || []
  if (agents.value.length && !activeAgentId.value) activeAgentId.value = agents.value[0].id
}

const loadChats = async () => {
  const params = {}
  if (q.value) params.q = q.value
  if (activeAgentId.value) params.agentId = activeAgentId.value
  const resp = await apiService.listChats(params)
  chats.value = (resp && resp.data) || []
  if (!currentChat.value && chats.value.length) selectChat(chats.value[0])
}

const debouncedReload = () => { clearTimeout(debounceTimer); debounceTimer = setTimeout(loadChats, 300) }

const newChat = async () => {
  const agentId = activeAgentId.value || (agents.value[0] && agents.value[0].id)
  if (!agentId) return
  const resp = await apiService.createChat({ agentId, title: 'New Chat' })
  await loadChats()
  if (resp && resp.data && resp.data.id) {
    const created = chats.value.find(c => c.id === resp.data.id) || chats.value[0]
    selectChat(created)
  }
}

const selectChat = (c) => {
  currentChat.value = c
  // Placeholder: in future, load messages by chat id
  messages.value = []
}

const renameChat = async () => {
  const title = prompt('New title', currentChat.value.title)
  if (!title) return
  await apiService.updateChat(currentChat.value.id, { agentId: currentChat.value.agentId, title })
  await loadChats()
}

const deleteCurrent = async () => {
  if (!currentChat.value) return
  if (!confirm('Delete this chat?')) return
  await apiService.deleteChat(currentChat.value.id)
  currentChat.value = null
  await loadChats()
}

const send = async () => {
  const text = draft.value.trim()
  if (!text) return
  // Local append
  messages.value.push({ id: Date.now(), role: 'user', content: text })
  draft.value = ''
  // TODO: Call backend or streaming endpoint for assistant response
}

onMounted(async () => {
  await loadAgents()
  await loadChats()
})
</script>

<style scoped>
</style>


