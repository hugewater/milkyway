import { getApiUrl, BACKEND_API_URLS } from './apiUrls.js'

// API Service Class
class ApiService {
  constructor() {
    this.baseURL = BACKEND_API_URLS.BASE
  }

  // Helper method to get auth headers
  getAuthHeaders() {
    const token = localStorage.getItem('token')
    return {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    }
  }

  // Generic request method
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`
    const config = {
      headers: this.getAuthHeaders(),
      ...options
    }

    try {
      const response = await fetch(url, config)
      let data
      try {
        data = await response.json()
      } catch (parseErr) {
        // 非JSON响应
        data = { success: false, error: `Non-JSON response, status ${response.status}` }
      }

      if (!response.ok) {
        const message = (data && (data.error || data.message)) || `HTTP error! status: ${response.status}`
        throw new Error(message)
      }

      return data
    } catch (error) {
      console.error('API request failed:', error)
      throw error
    }
  }

  // Authentication APIs
  async login(email, password) {
    return this.request(BACKEND_API_URLS.LOGIN, {
      method: 'POST',
      body: JSON.stringify({ email, password })
    })
  }

  async register(userData) {
    return this.request(BACKEND_API_URLS.REGISTER, {
      method: 'POST',
      body: JSON.stringify(userData)
    })
  }

  async validateToken(token) {
    return this.request(BACKEND_API_URLS.VALIDATE, {
      method: 'POST',
      body: JSON.stringify({ token })
    })
  }

  // User APIs
  async getUsers() {
    return this.request('/users')
  }

  async getUsersPaged({ offset = 0, limit = 50, sort = 'created_at', order = 'desc', role = '', status = '', level = '', q = '' } = {}) {
    const params = new URLSearchParams()
    params.set('offset', offset)
    params.set('limit', limit)
    if (sort) params.set('sort', sort)
    if (order) params.set('order', order)
    if (role) params.set('role', role)
    if (status) params.set('status', status)
    if (level) params.set('level', level)
    if (q) params.set('q', q)
    return this.request(`/users?${params.toString()}`)
  }

  async getUserById(id) {
    return this.request(`/users/${id}`)
  }

  async getUserStats() {
    return this.request('/users/stats')
  }

  async createUser(userData) {
    return this.request('/users', {
      method: 'POST',
      body: JSON.stringify(userData)
    })
  }

  async updateUser(id, userData) {
    return this.request(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(userData)
    })
  }

  async deleteUser(id) {
    return this.request(`/users/${id}`, {
      method: 'DELETE'
    })
  }

  // Certificate APIs
  async getCertificates() {
    return this.request('/certificates')
  }

  async getCertificateById(id) {
    return this.request(`/certificates/${id}`)
  }

  async createCertificate(certificateData) {
    return this.request('/certificates', {
      method: 'POST',
      body: JSON.stringify(certificateData)
    })
  }

  async updateCertificate(id, certificateData) {
    return this.request(`/certificates/${id}`, {
      method: 'PUT',
      body: JSON.stringify(certificateData)
    })
  }

  async deleteCertificate(id) {
    return this.request(`/certificates/${id}`, {
      method: 'DELETE'
    })
  }

  async toggleCertificateStatus(id) {
    return this.request(`/certificates/${id}/toggle-status`, {
      method: 'POST'
    })
  }

  async updateCertificatePrice(id, newPrice) {
    return this.request(`/certificates/${id}/price`, {
      method: 'PUT',
      body: JSON.stringify({ priceUsdt: newPrice })
    })
  }

  async updateCertificateSupply(id, maxSupply) {
    return this.request(`/certificates/${id}/supply`, {
      method: 'PUT',
      body: JSON.stringify({ maxSupply })
    })
  }

  async getCertificateStats() {
    return this.request('/certificates/stats')
  }

  // Journal APIs
  async getJournals() {
    return this.request('/journals')
  }

  async getJournalById(id) {
    return this.request(`/journals/${id}`)
  }

  async createJournal(journalData) {
    return this.request('/journals', {
      method: 'POST',
      body: JSON.stringify(journalData)
    })
  }

  async updateJournal(id, journalData) {
    return this.request(`/journals/${id}`, {
      method: 'PUT',
      body: JSON.stringify(journalData)
    })
  }

  async deleteJournal(id) {
    return this.request(`/journals/${id}`, {
      method: 'DELETE'
    })
  }

  async getJournalStats() {
    return this.request('/journals/stats')
  }

  // Wallet APIs
  async getWallets() {
    return this.request('/wallets')
  }

  async getWalletsPaged({ offset = 0, limit = 50, sort = 'created_at', order = 'desc', type = '', active = undefined, q = '' } = {}) {
    const params = new URLSearchParams()
    params.set('offset', offset)
    params.set('limit', limit)
    if (sort) params.set('sort', sort)
    if (order) params.set('order', order)
    if (type && type !== '') params.set('type', type)
    if (typeof active === 'boolean') params.set('active', active ? 'true' : 'false')
    if (q && q !== '') params.set('q', q)
    
    console.log('=== getWalletsPaged Debug ===')
    console.log('Input parameters:', { offset, limit, sort, order, type, active, q })
    console.log('URL params:', params.toString())
    
    const url = `/wallets?${params.toString()}`
    console.log('Final URL:', url)
    
    return this.request(url)
  }

  async getWalletsByUserId(userId) {
    return this.request(`/wallets/user/${userId}`)
  }

  async createWallet(walletData) {
    return this.request('/wallets', {
      method: 'POST',
      body: JSON.stringify(walletData)
    })
  }

  async getWalletStats() {
    return this.request('/wallets/stats')
  }

  async transferBetweenWallets(payload) {
    return this.request('/wallets/transfer', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  async addBalanceToWallet(payload) {
    return this.request('/wallets/add-balance', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  async updateWallet(walletId, walletData) {
    return this.request(`/wallets/${walletId}`, {
      method: 'PUT',
      body: JSON.stringify(walletData)
    })
  }

  async deleteWallet(walletId) {
    return this.request(`/wallets/${walletId}`, {
      method: 'DELETE'
    })
  }

  async toggleWalletStatus(walletId) {
    return this.request(`/wallets/${walletId}/toggle-status`, {
      method: 'POST'
    })
  }

  async payToWallet(walletId, payload) {
    return this.request(`/wallets/${walletId}/pay`, {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  async withdrawFromWallet(walletId, payload) {
    return this.request(`/wallets/${walletId}/withdraw`, {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  // NEW WALLET API METHODS
  
  async getUserWallet(userId) {
    return this.request(`/user/${userId}/wallet`)
  }

  async createUserWallet(userId, walletData) {
    return this.request(`/user/${userId}/wallet`, {
      method: 'POST',
      body: JSON.stringify(walletData)
    })
  }

  async updateWalletAddresses(userId, addresses) {
    return this.request(`/user/${userId}/wallet/addresses`, {
      method: 'PUT',
      body: JSON.stringify(addresses)
    })
  }

  async updateWalletAddress(userId, network, address) {
    return this.request(`/user/${userId}/wallet/address/${network}`, {
      method: 'PUT',
      body: JSON.stringify({ address })
    })
  }

  async getAllNewWallets() {
    return this.request('/wallets/new')
  }

  async recordWalletPayment(payload) {
    return this.request('/transactions/self-report', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  }

  // Transaction APIs
  async getTransactionsByUserId(userId) {
    return this.request(`/transactions/user/${userId}`)
  }

  async getTransactionsByWalletId(walletId) {
    return this.request(`/transactions/wallet/${walletId}`)
  }

  // AI Agents
  async listAgents(params = {}) {
    const q = new URLSearchParams(params).toString()
    return this.request(`/ai/agents${q ? `?${q}` : ''}`)
  }
  async createAgent(payload) {
    return this.request(`/ai/agents`, { method: 'POST', body: JSON.stringify(payload) })
  }
  async updateAgent(id, payload) {
    return this.request(`/ai/agents/${id}`, { method: 'PUT', body: JSON.stringify(payload) })
  }
  async deleteAgent(id) {
    return this.request(`/ai/agents/${id}`, { method: 'DELETE' })
  }

  async getUserNetwork(userId) {
    return this.request(`/users/${userId}/network`)
  }

  async addDownline(userId, downlineData) {
    return this.request(`/users/${userId}/add-downline`, {
      method: 'POST',
      body: JSON.stringify(downlineData)
    })
  }

  async getDownlineCount(userId) {
    return this.request(`/users/${userId}/downlines/count`)
  }

  async getDownlineCountBatch(userIds = []) {
    return this.request(`/users/downlines/count`, {
      method: 'POST',
      body: JSON.stringify({ userIds })
    })
  }

  // AI Chats
  async listChats(params = {}) {
    const q = new URLSearchParams(params).toString()
    return this.request(`/ai/chats${q ? `?${q}` : ''}`)
  }
  async createChat(payload) {
    return this.request(`/ai/chats`, { method: 'POST', body: JSON.stringify(payload) })
  }
  async updateChat(id, payload) {
    return this.request(`/ai/chats/${id}`, { method: 'PUT', body: JSON.stringify(payload) })
  }
  async deleteChat(id) {
    return this.request(`/ai/chats/${id}`, { method: 'DELETE' })
  }

  // Health check
  async healthCheck() {
    return this.request('/health')
  }

  // Version info
  async getVersion() {
    return this.request('/version')
  }

  // COMPANY WALLET API METHODS
  
  async getCompanyWallets() {
    return this.request('/company-wallets')
  }

  async getActiveCompanyWallets() {
    return this.request('/company-wallets/active')
  }

  async getCompanyWallet(id) {
    return this.request(`/company-wallets/${id}`)
  }

  async createCompanyWallet(walletData) {
    return this.request('/company-wallets', {
      method: 'POST',
      body: JSON.stringify(walletData)
    })
  }

  async updateCompanyWallet(id, walletData) {
    return this.request(`/company-wallets/${id}`, {
      method: 'PUT',
      body: JSON.stringify(walletData)
    })
  }

  async deleteCompanyWallet(id) {
    return this.request(`/company-wallets/${id}`, {
      method: 'DELETE'
    })
  }

  async toggleCompanyWalletStatus(id) {
    return this.request(`/company-wallets/${id}/toggle-status`, {
      method: 'POST'
    })
  }

  async getRandomCompanyWallet() {
    return this.request('/company-wallets/random')
  }

  async getCompanyWalletQRCodes(id) {
    return this.request(`/company-wallets/${id}/qr-codes`)
  }

  // New wallet architecture API methods
  async getCompanyWalletsGrouped() {
    return this.request('/company-wallets/grouped')
  }

  async getCompanyWalletsByType(walletType) {
    return this.request(`/company-wallets/by-type/${walletType}`)
  }

  async getCompanyWalletStatistics() {
    return this.request('/company-wallets/statistics')
  }

  async getWalletTypes() {
    return this.request('/wallet-types')
  }
}

// Create and export API service instance
const apiService = new ApiService()
export default apiService

// Export individual methods with proper binding
export const login = apiService.login.bind(apiService)
export const register = apiService.register.bind(apiService)
export const validateToken = apiService.validateToken.bind(apiService)
export const getUsers = apiService.getUsers.bind(apiService)
export const getUsersPaged = apiService.getUsersPaged.bind(apiService)
export const getUserById = apiService.getUserById.bind(apiService)
export const getUserStats = apiService.getUserStats.bind(apiService)
export const createUser = apiService.createUser.bind(apiService)
export const updateUser = apiService.updateUser.bind(apiService)
export const deleteUser = apiService.deleteUser.bind(apiService)
export const getCertificates = apiService.getCertificates.bind(apiService)
export const getCertificateById = apiService.getCertificateById.bind(apiService)
export const createCertificate = apiService.createCertificate.bind(apiService)
export const updateCertificate = apiService.updateCertificate.bind(apiService)
export const deleteCertificate = apiService.deleteCertificate.bind(apiService)
export const toggleCertificateStatus = apiService.toggleCertificateStatus.bind(apiService)
export const updateCertificatePrice = apiService.updateCertificatePrice.bind(apiService)
export const updateCertificateSupply = apiService.updateCertificateSupply.bind(apiService)
export const getCertificateStats = apiService.getCertificateStats.bind(apiService)
export const getJournals = apiService.getJournals.bind(apiService)
export const getJournalById = apiService.getJournalById.bind(apiService)
export const createJournal = apiService.createJournal.bind(apiService)
export const updateJournal = apiService.updateJournal.bind(apiService)
export const deleteJournal = apiService.deleteJournal.bind(apiService)
export const getJournalStats = apiService.getJournalStats.bind(apiService)
export const getWallets = apiService.getWallets.bind(apiService)
export const getWalletsPaged = apiService.getWalletsPaged.bind(apiService)
export const getWalletsByUserId = apiService.getWalletsByUserId.bind(apiService)
export const createWallet = apiService.createWallet.bind(apiService)
export const getWalletStats = apiService.getWalletStats.bind(apiService)
export const transferBetweenWallets = apiService.transferBetweenWallets.bind(apiService)
export const addBalanceToWallet = apiService.addBalanceToWallet.bind(apiService)
export const updateWallet = apiService.updateWallet.bind(apiService)
export const deleteWallet = apiService.deleteWallet.bind(apiService)
export const toggleWalletStatus = apiService.toggleWalletStatus.bind(apiService)
export const payToWallet = apiService.payToWallet.bind(apiService)
export const withdrawFromWallet = apiService.withdrawFromWallet.bind(apiService)
export const getUserWallet = apiService.getUserWallet.bind(apiService)
export const createUserWallet = apiService.createUserWallet.bind(apiService)
export const updateWalletAddresses = apiService.updateWalletAddresses.bind(apiService)
export const updateWalletAddress = apiService.updateWalletAddress.bind(apiService)
export const getAllNewWallets = apiService.getAllNewWallets.bind(apiService)
export const getTransactionsByUserId = apiService.getTransactionsByUserId.bind(apiService)
export const getTransactionsByWalletId = apiService.getTransactionsByWalletId.bind(apiService)
export const getUserNetwork = apiService.getUserNetwork.bind(apiService)
export const addDownline = apiService.addDownline.bind(apiService)
export const getDownlineCount = apiService.getDownlineCount.bind(apiService)
export const getDownlineCountBatch = apiService.getDownlineCountBatch.bind(apiService)
export const healthCheck = apiService.healthCheck.bind(apiService)
export const getVersion = apiService.getVersion.bind(apiService)

// Agents
export const listAgents = apiService.listAgents.bind(apiService)
export const createAgent = apiService.createAgent.bind(apiService)
export const updateAgent = apiService.updateAgent.bind(apiService)
export const deleteAgent = apiService.deleteAgent.bind(apiService)

// Company Wallets
export const getCompanyWallets = apiService.getCompanyWallets.bind(apiService)
export const getActiveCompanyWallets = apiService.getActiveCompanyWallets.bind(apiService)
export const getCompanyWallet = apiService.getCompanyWallet.bind(apiService)
export const createCompanyWallet = apiService.createCompanyWallet.bind(apiService)
export const updateCompanyWallet = apiService.updateCompanyWallet.bind(apiService)
export const deleteCompanyWallet = apiService.deleteCompanyWallet.bind(apiService)
export const toggleCompanyWalletStatus = apiService.toggleCompanyWalletStatus.bind(apiService)
export const getRandomCompanyWallet = apiService.getRandomCompanyWallet.bind(apiService)
export const getCompanyWalletQRCodes = apiService.getCompanyWalletQRCodes.bind(apiService)
export const getCompanyWalletsGrouped = apiService.getCompanyWalletsGrouped.bind(apiService)
export const getCompanyWalletsByType = apiService.getCompanyWalletsByType.bind(apiService)
export const getCompanyWalletStatistics = apiService.getCompanyWalletStatistics.bind(apiService)
export const getWalletTypes = apiService.getWalletTypes.bind(apiService)

