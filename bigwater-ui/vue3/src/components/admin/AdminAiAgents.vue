<template>
  <AppLayout>
    <div class="p-6">
      <header class="bg-white shadow-sm border-b border-gray-200 mb-6">
        <div class="flex items-center justify-between px-6 py-4">
          <h1 class="text-2xl font-bold text-deep-ocean">AI · Agents</h1>
          <button @click="openAdd" class="px-3 py-2 border border-gray-300 rounded-lg text-sm hover:bg-gray-50">Add Agent</button>
        </div>
      </header>

      <div class="card rounded-2xl overflow-hidden">
        <div class="p-4 flex items-center gap-3 border-b">
          <input v-model="q" @input="debouncedReload" placeholder="Search name/provider/model" class="border rounded px-3 py-2 text-sm w-64" />
          <select v-model="status" @change="reload" class="border rounded px-2 py-2 text-sm">
            <option value="">All Status</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
          <select v-model="enabled" @change="reload" class="border rounded px-2 py-2 text-sm">
            <option :value="''">All</option>
            <option :value="true">Enabled</option>
            <option :value="false">Disabled</option>
          </select>
        </div>
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Provider</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Model</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Enabled</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Updated</th>
                <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="a in items" :key="a.id">
                <td class="px-4 py-2 text-sm text-gray-900">{{ a.id }}</td>
                <td class="px-4 py-2 text-sm">{{ a.name }}</td>
                <td class="px-4 py-2 text-sm">{{ a.provider }}</td>
                <td class="px-4 py-2 text-sm">{{ a.model }}</td>
                <td class="px-4 py-2 text-sm">
                  <label class="inline-flex items-center cursor-pointer">
                    <input type="checkbox" class="sr-only peer" :checked="a.enabled" @change="toggleEnabled(a)" />
                    <div class="w-10 h-6 bg-gray-200 rounded-full peer peer-checked:bg-green-500 relative transition-colors">
                      <div class="absolute top-1 left-1 w-4 h-4 bg-white rounded-full transition-transform peer-checked:translate-x-4"></div>
                    </div>
                  </label>
                </td>
                <td class="px-4 py-2 text-sm">
                  <select v-model="a.status" @change="save(a)" class="border rounded px-2 py-1 text-xs">
                    <option>ACTIVE</option>
                    <option>INACTIVE</option>
                  </select>
                </td>
                <td class="px-4 py-2 text-sm text-gray-700">{{ formatDate(a.updatedAt) }}</td>
                <td class="px-4 py-2 text-sm space-x-3">
                  <button @click="edit(a)" class="text-ocean hover:text-deep-ocean text-sm">Edit</button>
                  <button @click="remove(a)" class="text-red-600 hover:text-red-700 text-sm">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Modal -->
      <div v-if="show" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-2xl p-6 w-full max-w-xl max-h-[80vh] overflow-y-auto shadow-2xl">
          <h3 class="text-lg font-bold text-deep-ocean mb-4">{{ form.id ? 'Edit Agent' : 'Add Agent' }}</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="text-xs text-gray-500">Name</label>
              <input v-model="form.name" class="mt-1 w-full border rounded px-3 py-2 text-sm" />
            </div>
            <div>
              <label class="text-xs text-gray-500">Provider</label>
              <input v-model="form.provider" placeholder="OpenAI / Anthropic / DeepSeek" class="mt-1 w-full border rounded px-3 py-2 text-sm" />
            </div>
            <div>
              <label class="text-xs text-gray-500">Model</label>
              <input v-model="form.model" placeholder="gpt-5 / claude-4 / deepseek-3" class="mt-1 w-full border rounded px-3 py-2 text-sm" />
            </div>
            <div>
              <label class="text-xs text-gray-500">Role</label>
              <input v-model="form.role" placeholder="assistant / planner / router ..." class="mt-1 w-full border rounded px-3 py-2 text-sm" />
            </div>
            <div class="md:col-span-2">
              <label class="text-xs text-gray-500">Webhook URL</label>
              <input v-model="form.webhookUrl" class="mt-1 w-full border rounded px-3 py-2 text-sm" />
            </div>
            <div class="md:col-span-2">
              <label class="text-xs text-gray-500">API Key</label>
              <input v-model="form.apiKey" type="password" autocomplete="new-password" class="mt-1 w-full border rounded px-3 py-2 text-sm" />
            </div>
            <div class="md:col-span-2">
              <label class="text-xs text-gray-500">Description</label>
              <textarea v-model="form.description" rows="3" class="mt-1 w-full border rounded px-3 py-2 text-sm"></textarea>
            </div>
            <div class="md:col-span-2 flex items-center gap-3">
              <label class="inline-flex items-center cursor-pointer">
                <input type="checkbox" class="sr-only peer" v-model="form.enabled" />
                <div class="w-10 h-6 bg-gray-200 rounded-full peer peer-checked:bg-green-500 relative transition-colors">
                  <div class="absolute top-1 left-1 w-4 h-4 bg-white rounded-full transition-transform peer-checked:translate-x-4"></div>
                </div>
              </label>
              <span class="text-sm">Enabled</span>
              <select v-model="form.status" class="border rounded px-2 py-1 text-xs">
                <option>ACTIVE</option>
                <option>INACTIVE</option>
              </select>
            </div>
          </div>
          <div class="flex justify-end mt-6 gap-2 sticky bottom-0 bg-white pt-4 border-t">
            <button @click="show=false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 text-gray-700">Cancel</button>
            <button @click="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400 shadow-md">Save</button>
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

const items = ref([])
const q = ref('')
const status = ref('')
const enabled = ref('')
let debounceTimer

const show = ref(false)
const form = ref({ id: null, name: '', provider: '', model: '', apiKey: '', webhookUrl: '', role: '', enabled: true, status: 'ACTIVE', description: '' })

const formatDate = (d) => { try { return new Date(d).toLocaleString() } catch { return d } }

const load = async () => {
  const params = {}
  if (q.value) params.q = q.value
  if (status.value) params.status = status.value
  if (enabled.value !== '') params.enabled = enabled.value
  const resp = await apiService.listAgents(params)
  items.value = (resp && resp.data) || []
}

const reload = () => { load() }
const debouncedReload = () => { clearTimeout(debounceTimer); debounceTimer = setTimeout(load, 300) }

const openAdd = () => { form.value = { id: null, name: '', provider: '', model: '', apiKey: '', webhookUrl: '', role: '', enabled: true, status: 'ACTIVE', description: '' }; show.value = true }
const edit = (a) => { form.value = { ...a }; show.value = true }
const remove = async (a) => { if (!confirm('Delete this agent?')) return; await apiService.deleteAgent(a.id); load() }
const submit = async () => { if (form.value.id) await apiService.updateAgent(form.value.id, form.value); else await apiService.createAgent(form.value); show.value=false; load() }
const save = async (a) => { await apiService.updateAgent(a.id, a) }
const toggleEnabled = async (a) => { a.enabled = !a.enabled; await apiService.updateAgent(a.id, { ...a, enabled: a.enabled }) }

onMounted(load)
</script>

<style scoped>
</style>


