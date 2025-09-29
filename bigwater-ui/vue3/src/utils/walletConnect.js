import { ref, reactive } from 'vue'

// Wallet connection states
export const WALLET_STATES = {
  DISCONNECTED: 'disconnected',
  CONNECTING: 'connecting',
  CONNECTED: 'connected',
  ERROR: 'error'
}

// Supported wallet types
export const WALLET_TYPES = {
  METAMASK: 'metamask',
  TRONLINK: 'tronlink',
  SAFEPAL: 'safepal',
  TRUST: 'trust'
}

// Connection state management
export const walletState = reactive({
  status: WALLET_STATES.DISCONNECTED,
  type: null,
  address: null,
  chainId: null,
  error: null
})

// Wallet error states
export const walletErrors = reactive({
  notInstalled: false,
  wrongNetwork: false,
  connectionError: ''
})

// Currently selected EVM provider (MetaMask / Trust / SafePal)
export const currentEvmProvider = ref(null)

// Helper function to shorten wallet addresses
export function shortenAddress(address) {
  if (!address) return ''
  return `${address.slice(0, 6)}...${address.slice(-4)}`
}

// Generic EVM provider discovery helpers
function hasWindowEthereum() {
  return typeof window !== 'undefined' && typeof window.ethereum !== 'undefined'
}

// Find a provider by feature flag (e.g. 'isMetaMask', 'isTrust', 'isSafePal')
function getEvmProviderByFlag(flag) {
  if (!hasWindowEthereum()) return null
  const eth = window.ethereum
  if (eth[flag]) return eth
  if (Array.isArray(eth.providers)) {
    return eth.providers.find(p => p && p[flag]) || null
  }
  return null
}

// Enumerate all injected providers for diagnostics
export function listInjectedProviders() {
  if (!hasWindowEthereum()) return []
  const eth = window.ethereum
  const providers = []
  if (Array.isArray(eth.providers)) {
    eth.providers.forEach(p => providers.push(describeProvider(p)))
  } else {
    providers.push(describeProvider(eth))
  }
  return providers
}

// Return raw provider objects (not just summaries)
export function getAllEvmProviders() {
  if (!hasWindowEthereum()) return []
  const eth = window.ethereum
  return Array.isArray(eth.providers) ? eth.providers : [eth]
}

function describeProvider(p) {
  if (!p) return { flags: [], raw: null }
  const knownFlags = ['isMetaMask','isTrust','isSafePal','isBitKeep','isOkxWallet','isRabby','isBraveWallet']
  const flags = knownFlags.filter(f => !!p[f])
  // Attempt to read walletMeta safely
  let walletMeta = null
  try {
    if (p.walletMeta) {
      walletMeta = {
        name: p.walletMeta.name,
        url: p.walletMeta.url,
        rdns: p.walletMeta.rdns
      }
    }
  } catch (_) { /* ignore */ }
  return { flags, chainId: p.chainId, selectedAddress: p.selectedAddress, walletMeta }
}

const isMetaMaskAvailable = () => !!getEvmProviderByFlag('isMetaMask')

// Check if TronLink is available
const isTronLinkAvailable = () => {
  return typeof window !== 'undefined' && typeof window.tronWeb !== 'undefined'
}

// MetaMask connection handler
async function connectMetaMask() {
  const provider = getEvmProviderByFlag('isMetaMask')
  if (!provider) {
    walletErrors.notInstalled = true
    throw new Error('MetaMask not installed')
  }
  try {
    resetWalletErrors()
    walletState.status = WALLET_STATES.CONNECTING
    walletState.type = 'metamask'
    currentEvmProvider.value = provider

    const accounts = await provider.request({ method: 'eth_requestAccounts' })
    const chainId = await provider.request({ method: 'eth_chainId' })

    finalizeEvmConnection(provider, accounts[0], chainId)
    return accounts[0]
  } catch (error) {
    handleEvmConnectionError(error)
    throw error
  }
}

// TronLink connection handler
async function connectTronLink() {
  if (!isTronLinkAvailable()) {
    throw new Error('TronLink not installed')
  }

  try {
    resetWalletErrors()
    walletState.status = WALLET_STATES.CONNECTING
    walletState.type = 'tronlink'

    await waitForTronLinkReady()

    // Determine request function
    let requestFn = null
    if (window.tronWeb?.request) {
      requestFn = (m) => window.tronWeb.request({ method: m })
    } else if (window.tronLink?.request) {
      requestFn = (m) => window.tronLink.request({ method: m })
    }

    if (requestFn) {
      try {
        await requestFn('tron_requestAccounts')
      } catch (e) {
        console.warn('[TronLink] tron_requestAccounts may not be required:', e?.message)
      }
    }

    const addr = safeGetTronAddress()
    if (!addr) throw new Error('No Tron address available')
    walletState.address = addr
    walletState.status = WALLET_STATES.CONNECTED
    walletState.error = null

    try { window.tronWeb?.on?.('addressChanged', handleTronAddressChanged) } catch (_) { /* ignore */ }
    return addr
  } catch (error) {
    walletState.status = WALLET_STATES.ERROR
    walletState.error = error.message
    throw error
  }
}

async function waitForTronLinkReady(maxRetries = 25, intervalMs = 150) {
  let i = 0
  while (i < maxRetries) {
    if (window.tronWeb?.ready && window.tronWeb?.defaultAddress?.base58) return true
    await new Promise(r => setTimeout(r, intervalMs))
    i++
  }
  return false
}

function safeGetTronAddress() {
  try {
    if (window.tronWeb?.defaultAddress?.base58) return window.tronWeb.defaultAddress.base58
    if (window.tronWeb?.defaultAddress?.hex) return window.tronWeb.defaultAddress.hex
  } catch (_) {}
  return null
}

// Attempt to deeply inspect an object for a substring match (guard against circular refs)
function deepContains(obj, needle, depth = 0, visited = new Set()) {
  if (!obj || depth > 3 || visited.has(obj)) return false
  visited.add(obj)
  try {
    if (typeof obj === 'string') return obj.toLowerCase().includes(needle)
    if (typeof obj === 'object') {
      for (const key of Object.keys(obj)) {
        if (key.toLowerCase().includes(needle)) return true
        const val = obj[key]
        if (typeof val === 'string' && val.toLowerCase().includes(needle)) return true
        if (typeof val === 'object') {
          if (deepContains(val, needle, depth + 1, visited)) return true
        }
      }
    }
  } catch (_) { /* ignore */ }
  return false
}

function findSafePalProvider({ assumeIfSingle } = {}) {
  if (!hasWindowEthereum()) return null
  const eth = window.ethereum
  const candidates = Array.isArray(eth.providers) ? eth.providers : [eth]
  // Manual override (for debugging) if user sets window.__forceSafePal
  try {
    if (typeof window !== 'undefined' && window.__forceSafePal) {
      console.warn('[SafePal][Manual Override] Using window.__forceSafePal provider override')
      return window.__forceSafePal
    }
  } catch (_) { /* ignore */ }
  // Priority 1: explicit flag
  let provider = candidates.find(p => p && p.isSafePal)
  if (provider) return provider
  // Priority 2: walletMeta name
  provider = candidates.find(p => p?.walletMeta?.name && p.walletMeta.name.toLowerCase().includes('safepal'))
  if (provider) return provider
  // Priority 3: walletMeta url or rdns
  provider = candidates.find(p => {
    try {
      return (
        (p?.walletMeta?.url && p.walletMeta.url.toLowerCase().includes('safepal')) ||
        (p?.walletMeta?.rdns && p.walletMeta.rdns.toLowerCase().includes('safepal'))
      )
    } catch (_) { return false }
  })
  if (provider) return provider
  // Priority 4: constructor name heuristic
  provider = candidates.find(p => {
    try { return (p?.constructor?.name || '').toLowerCase().includes('safepal') } catch (_) { return false }
  })
  if (provider) return provider
  // Priority 5: deep scan for multiple token variations
  const tokens = ['safepal','safe-pal','safe_pal','sfp']
  provider = candidates.find(p => tokens.some(t => deepContains(p, t)))
  if (provider) return provider
  // Priority 6: exclusion heuristic (last resort)
  const knownFlagProps = ['isMetaMask','isTrust','isBraveWallet','isRabby','isOkxWallet','isBitKeep']
  const unidentified = candidates.filter(p => p && !knownFlagProps.some(f => p[f]))
  if (unidentified.length === 1) {
    console.warn('[SafePal][Heuristic] Using exclusion heuristic to pick provider as SafePal.')
    return unidentified[0]
  }
  // Priority 7: optional assumption if user explicitly chose SafePal and only one provider exists
  if (assumeIfSingle && candidates.length === 1 && candidates[0]) {
    console.warn('[SafePal][Assume] Assuming sole provider is SafePal due to user selection and lack of distinguishing flags.')
    return candidates[0]
  }
  return provider || null
}

export function debugSafePalDetection() {
  const providers = listInjectedProviders()
  const rawProviders = getAllEvmProviders()
  const foundProvider = findSafePalProvider()
  // Collect shallow key set for each provider for forensic analysis
  const forensic = rawProviders.map((p, idx) => {
    let keys = []
    try { keys = Object.getOwnPropertyNames(p).slice(0, 80) } catch (_) {}
    return { index: idx, flags: providers[idx]?.flags || [], keySample: keys.filter(k => /safe|pal/i.test(k)), totalKeys: keys.length, constructor: p?.constructor?.name }
  })
  console.log('[SafePal][Debug] providers summary:', providers)
  console.log('[SafePal][Debug] forensic keys:', forensic)
  console.log('[SafePal][Debug] detected provider:', !!foundProvider, foundProvider)
  return { providers, forensic, detected: !!foundProvider }
}

export function dumpProviderDetails(index = 0, depth = 1) {
  if (!hasWindowEthereum()) return null
  const all = getAllEvmProviders()
  const target = all[index]
  if (!target) return null
  const summary = {}
  const maxProps = 200
  try {
    Object.getOwnPropertyNames(target).slice(0, maxProps).forEach(k => {
      try {
        const val = target[k]
        if (val && typeof val === 'object') {
          if (depth > 0) {
            summary[k] = {
              type: Array.isArray(val) ? 'array' : 'object',
              keys: Object.keys(val).slice(0, 25)
            }
          } else {
            summary[k] = { type: 'object' }
          }
        } else if (typeof val === 'function') {
          summary[k] = 'fn()'
        } else {
          summary[k] = val
        }
      } catch (_) { summary[k] = '[unreadable]' }
    })
  } catch (e) {
    return { error: e.message }
  }
  console.log('[Provider Dump]', { index, summary })
  return { index, summary }
}

// SafePal connection handler (injected EVM provider flavor)
async function connectSafePal() {
  let provider = findSafePalProvider({ assumeIfSingle: true })
  if (!provider) {
    console.warn('[SafePal] Provider not detected. Injected providers:', listInjectedProviders())
    walletErrors.notInstalled = true
    throw new Error('SafePal not installed (not detected via flags or heuristics)')
  }
  try {
    resetWalletErrors()
    walletState.status = WALLET_STATES.CONNECTING
    walletState.type = 'safepal'
    currentEvmProvider.value = provider

    const accounts = await provider.request({ method: 'eth_requestAccounts' })
    if (!accounts || !accounts.length) {
      // Some providers use requestAccounts (legacy)
      try {
        const legacyAccounts = await provider.request({ method: 'requestAccounts' })
        if (legacyAccounts && legacyAccounts.length) {
          finalizeEvmConnection(provider, legacyAccounts[0], await provider.request({ method: 'eth_chainId' }))
          return legacyAccounts[0]
        }
      } catch (_) {/* ignore */}
      throw new Error('No SafePal account returned')
    }
    const chainId = await provider.request({ method: 'eth_chainId' })

    finalizeEvmConnection(provider, accounts[0], chainId)
    return accounts[0]
  } catch (error) {
    handleEvmConnectionError(error)
    throw error
  }
}

// Trust Wallet connection handler (similar to MetaMask as it's EVM compatible)
async function connectTrustWallet() {
  const provider = getEvmProviderByFlag('isTrust')
  if (!provider) {
    walletErrors.notInstalled = true
    throw new Error('Trust Wallet not installed')
  }
  try {
    resetWalletErrors()
    walletState.status = WALLET_STATES.CONNECTING
    walletState.type = 'trust'
    currentEvmProvider.value = provider

    const accounts = await provider.request({ method: 'eth_requestAccounts' })
    if (!accounts || !accounts.length) throw new Error('No Trust Wallet account returned')
    const chainId = await provider.request({ method: 'eth_chainId' })

    finalizeEvmConnection(provider, accounts[0], chainId)
    return accounts[0]
  } catch (error) {
    handleEvmConnectionError(error)
    throw error
  }
}

// Shared helpers for EVM wallets
function resetWalletErrors() {
  walletErrors.notInstalled = false
  walletErrors.wrongNetwork = false
  walletErrors.connectionError = ''
}

function finalizeEvmConnection(provider, account, chainId) {
  walletState.address = account
  walletState.chainId = chainId
  walletState.status = WALLET_STATES.CONNECTED
  walletState.error = null
  // Attach listeners
  if (provider && provider.on) {
    provider.on('accountsChanged', handleAccountsChanged)
    provider.on('chainChanged', handleChainChanged)
    provider.on('disconnect', handleDisconnect)
  }
}

function handleEvmConnectionError(error) {
  walletState.status = WALLET_STATES.ERROR
  walletState.error = error.message
  walletErrors.connectionError = error.message
}

// Event Handlers
function handleAccountsChanged(accounts) {
  if (accounts.length === 0) {
    // User disconnected
    handleDisconnect()
  } else {
    // Account changed
    walletState.address = accounts[0]
  }
}

function handleChainChanged(chainId) {
  walletState.chainId = chainId
}

function handleDisconnect() {
  walletState.status = WALLET_STATES.DISCONNECTED
  walletState.address = null
  walletState.chainId = null
  walletState.type = null
  walletState.error = null
}

function handleTronAddressChanged(address) {
  walletState.address = address
}

// Main connection handler
export async function connectWallet(walletType) {
  console.log('Connecting wallet:', walletType)
  try {
    walletType = walletType.toLowerCase()
    switch (walletType) {
      case WALLET_TYPES.METAMASK:
        console.log('Connecting to MetaMask...')
        return await connectMetaMask()
      case WALLET_TYPES.TRONLINK:
        console.log('Connecting to TronLink...')
        return await connectTronLink()
      case WALLET_TYPES.SAFEPAL:
        console.log('Connecting to SafePal...')
        return await connectSafePal()
      case WALLET_TYPES.TRUST:
        console.log('Connecting to Trust Wallet...')
        return await connectTrustWallet()
      default:
        console.error('Unsupported wallet type:', walletType)
        throw new Error(`Unsupported wallet type: ${walletType}`)
    }
  } catch (error) {
    console.error('Wallet connection error:', error)
    walletErrors.connectionError = error.message
    throw error
  }
}

// Disconnect wallet
export function disconnectWallet() {
  try {
    if (walletState.type === WALLET_STATES.DISCONNECTED) return
    // Remove EVM listeners
    if (currentEvmProvider.value) {
      try {
        currentEvmProvider.value.removeListener?.('accountsChanged', handleAccountsChanged)
        currentEvmProvider.value.removeListener?.('chainChanged', handleChainChanged)
        currentEvmProvider.value.removeListener?.('disconnect', handleDisconnect)
      } catch (e) {
        // swallow listener cleanup errors
      }
    } else if (walletState.type === 'tronlink' && typeof window !== 'undefined' && window.tronWeb) {
      window.tronWeb.removeListener?.('addressChanged', handleTronAddressChanged)
    }
  } finally {
    currentEvmProvider.value = null
    handleDisconnect()
  }
}

// Helper to check if a wallet is currently connected
export function isWalletConnected() {
  return walletState.status === WALLET_STATES.CONNECTED && walletState.address
}

// Get current wallet info
export function getWalletInfo() {
  return {
    status: walletState.status,
    type: walletState.type,
    address: walletState.address,
    chainId: walletState.chainId
  }
}

// Expose debug utilities for manual console inspection (non-production convenience)
if (typeof window !== 'undefined') {
  try {
    window.__walletDebug = Object.assign({}, window.__walletDebug || {}, {
      debugSafePalDetection,
      listInjectedProviders,
      getAllEvmProviders,
      dumpProviderDetails,
      debugTron: () => {
        try {
          return {
            hasTronWeb: !!window.tronWeb,
            ready: window.tronWeb?.ready,
            defaultAddress: window.tronWeb?.defaultAddress,
            safeAddress: safeGetTronAddress()
          }
        } catch (e) { return { error: e.message } }
      }
    })
  } catch (_) { /* ignore */ }
}