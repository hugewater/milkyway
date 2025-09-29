// Minimal ERC-20 interaction helper for USDT transfers (Polygon or other EVM chains)
// Assumes a connected EVM wallet (MetaMask / SafePal / Trust) and window.ethereum present.
// Avoids bringing in full ethers.js for now; uses raw JSON-RPC.

import { walletState } from './walletConnect'

// USDT addresses vary by chain; provide mapping (extend as needed)
export const USDT_ADDRESSES = {
  // Polygon (PoS) mainnet
  '0x89': '0x2791Bca1f2de4661ED88A30C99A7a9449Aa84174',
  // Ethereum mainnet
  '0x1': '0xdAC17F958D2ee523a2206206994597C13D831ec7'
}

// Standard (subset) ERC-20 ABI fragments we need
const ERC20_ABI = {
  name: '0x06fdde03', // name()
  symbol: '0x95d89b41', // symbol()
  decimals: '0x313ce567', // decimals()
  balanceOf: '0x70a08231', // balanceOf(address)
  transfer: '0xa9059cbb' // transfer(address,uint256)
}

function toHex(value) {
  if (typeof value === 'number') return '0x' + value.toString(16)
  if (typeof value === 'bigint') return '0x' + value.toString(16)
  if (typeof value === 'string' && value.startsWith('0x')) return value
  return '0x' + BigInt(value).toString(16)
}

function pad32(hexNo0x) {
  return hexNo0x.padStart(64, '0')
}

function encodeAddress(addr) {
  return pad32(addr.toLowerCase().replace(/^0x/, ''))
}

async function rpc(method, params) {
  return await window.ethereum.request({ method, params })
}

export async function getCurrentChainId() {
  return await rpc('eth_chainId', [])
}

export function getUsdtContractAddress(chainId) {
  return USDT_ADDRESSES[chainId]
}

export async function getErc20Decimals(contract) {
  const data = ERC20_ABI.decimals
  const result = await rpc('eth_call', [{ to: contract, data }, 'latest'])
  return parseInt(result, 16)
}

export async function getErc20Balance(contract, address) {
  const data = ERC20_ABI.balanceOf + encodeAddress(address)
  const raw = await rpc('eth_call', [{ to: contract, data }, 'latest'])
  return BigInt(raw)
}

export async function buildTransferData(to, amountBigInt) {
  // function transfer(address,uint256)
  return ERC20_ABI.transfer + encodeAddress(to) + pad32(amountBigInt.toString(16))
}

export function formatUnits(amountBigInt, decimals) {
  const str = amountBigInt.toString()
  if (decimals === 0) return str
  const pad = decimals - str.length + 1
  if (pad > 0) return '0.' + '0'.repeat(pad) + str
  const idx = str.length - decimals
  return str.slice(0, idx) + '.' + str.slice(idx)
}

export function parseUnits(amountStr, decimals) {
  if (!/^\d+(?:\.\d+)?$/.test(amountStr)) throw new Error('Invalid amount format')
  const [intPart, decPartRaw] = amountStr.split('.')
  const decPart = (decPartRaw || '').slice(0, decimals)
  const padded = decPart + '0'.repeat(decimals - decPart.length)
  return BigInt(intPart + padded)
}

export async function sendUsdt({ to, amount, onPending, onSent, onConfirmed }) {
  if (!window.ethereum) throw new Error('No EVM provider')
  if (!walletState.address) throw new Error('Wallet not connected')
  const chainId = await getCurrentChainId()
  const contract = getUsdtContractAddress(chainId)
  if (!contract) throw new Error(`USDT not configured for chain ${chainId}`)

  const decimals = await getErc20Decimals(contract)
  const amountBig = parseUnits(amount, decimals)

  // Basic balance check (optional, may be slightly stale)
  const bal = await getErc20Balance(contract, walletState.address)
  if (bal < amountBig) throw new Error('Insufficient USDT balance')

  const data = await buildTransferData(to, amountBig)

  const tx = {
    from: walletState.address,
    to: contract,
    data,
    value: '0x0'
  }

  if (onPending) onPending(tx)
  const txHash = await rpc('eth_sendTransaction', [tx])
  if (onSent) onSent(txHash)

  // Poll for receipt
  const receipt = await waitForReceipt(txHash, 30, 4000)
  if (!receipt || receipt.status !== '0x1') throw new Error('Transaction failed')
  if (onConfirmed) onConfirmed(receipt)
  return { txHash, receipt }
}

async function waitForReceipt(txHash, maxTries = 30, intervalMs = 4000) {
  for (let i = 0; i < maxTries; i++) {
    const r = await rpc('eth_getTransactionReceipt', [txHash])
    if (r) return r
    await new Promise(r => setTimeout(r, intervalMs))
  }
  return null
}
