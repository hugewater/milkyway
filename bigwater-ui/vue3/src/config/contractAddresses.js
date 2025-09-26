/**
 * Contract addresses configuration
 * This file loads contract addresses from environment variables or uses defaults
 * No recompilation needed when contract addresses change
 */

// Default contract addresses (fallback values)
const DEFAULT_CONTRACT_ADDRESSES = {
  // USDT Contract Addresses
  USDT_POLYGON: '0xc2132D05D31c914a87C6611C10748AEb04B58e8F',
  USDT_TRON: 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t',
  USDT_ETHEREUM: '0xdAC17F958D2ee523a2206206994597C13D831ec7',
  USDT_BSC: '0x55d398326f99059fF775485246999027B3197955',
  
  // ACT Token Contract Addresses
  ACT_POLYGON: '0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082',
  
  // TTT Token Contract Addresses (TRON Nile testnet)
  TTT_TRON: 'TQwa7kTensPjJVUdfpqiPBGixaNAenCDMS'
}

/**
 * Get contract address from environment variable or use default
 * @param {string} envKey Environment variable key
 * @param {string} defaultValue Default value if env var not found
 * @returns {string} Contract address
 */
export function getContractAddress(envKey, defaultValue) {
  // In browser environment, we can't access process.env directly
  // So we'll use a configuration object that can be set at build time
  return (typeof window !== 'undefined' && window.CONTRACT_ADDRESSES?.[envKey]) || defaultValue
}

// Load contract addresses from configuration
export const CONTRACT_ADDRESSES = {
  // USDT Contract Addresses
  POLYGON_USDT: getContractAddress('POLYGON_USDT', DEFAULT_CONTRACT_ADDRESSES.USDT_POLYGON),
  TRON_USDT: getContractAddress('TRON_USDT', DEFAULT_CONTRACT_ADDRESSES.USDT_TRON),
  ETHEREUM_USDT: getContractAddress('ETHEREUM_USDT', DEFAULT_CONTRACT_ADDRESSES.USDT_ETHEREUM),
  BSC_USDT: getContractAddress('BSC_USDT', DEFAULT_CONTRACT_ADDRESSES.USDT_BSC),
  
  // ACT Token Contract Addresses
  ACT_POLYGON: getContractAddress('ACT_POLYGON', DEFAULT_CONTRACT_ADDRESSES.ACT_POLYGON),
  
  // TTT Token Contract Addresses
  TTT_TRON: getContractAddress('TTT_TRON', DEFAULT_CONTRACT_ADDRESSES.TTT_TRON)
}

// Legacy format for backward compatibility
export const USDT_CONTRACTS = {
  'POL': CONTRACT_ADDRESSES.POLYGON_USDT,
  'TRX': CONTRACT_ADDRESSES.TRON_USDT,
  'ETH': CONTRACT_ADDRESSES.ETHEREUM_USDT,
  'BSC': CONTRACT_ADDRESSES.BSC_USDT,
  'ACT': CONTRACT_ADDRESSES.ACT_POLYGON,
  'TTT': CONTRACT_ADDRESSES.TTT_TRON
}

/**
 * Get contract address by token type and network
 * @param {string} tokenType Token type (USDT, ACT, etc.)
 * @param {string} network Network (POL, TRX, ETH, BSC, etc.)
 * @returns {string|null} Contract address or null if not found
 */
export function getContractAddressByTokenType(tokenType, network) {
  if (tokenType === 'USDT') {
    return USDT_CONTRACTS[network] || null
  }
  // Add other token types here
  return null
}

/**
 * Get all supported contract addresses as an object
 * @returns {Object} Map of token type and network combinations to contract addresses
 */
export function getAllContractAddresses() {
  return {
    // USDT contracts
    'USDT_POL': CONTRACT_ADDRESSES.POLYGON_USDT,
    'USDT_TRX': CONTRACT_ADDRESSES.TRON_USDT,
    'USDT_ETH': CONTRACT_ADDRESSES.ETHEREUM_USDT,
    'USDT_BSC': CONTRACT_ADDRESSES.BSC_USDT,
    
    // ACT contracts
    'ACT_POL': CONTRACT_ADDRESSES.ACT_POLYGON,
    'TTT_TRON': CONTRACT_ADDRESSES.TTT_TRON

  }
}

/**
 * Get contract address for a specific wallet type
 * @param {string} walletType Wallet type (POL, TRX, ETH, BSC, ACT, etc.)
 * @returns {string|null} Contract address or null if not found
 */
export function getContractAddressByWalletType(walletType) {
  return USDT_CONTRACTS[walletType] || null
}

/**
 * Initialize contract addresses from a configuration object
 * This can be called at application startup to set contract addresses
 * @param {Object} config Configuration object with contract addresses
 */
export function initializeContractAddresses(config) {
  if (config) {
    Object.assign(CONTRACT_ADDRESSES, config)
    // Update legacy format
    USDT_CONTRACTS.POL = CONTRACT_ADDRESSES.POLYGON_USDT
    USDT_CONTRACTS.TRX = CONTRACT_ADDRESSES.TRON_USDT
    USDT_CONTRACTS.ETH = CONTRACT_ADDRESSES.ETHEREUM_USDT
    USDT_CONTRACTS.BSC = CONTRACT_ADDRESSES.BSC_USDT
    USDT_CONTRACTS.ACT = CONTRACT_ADDRESSES.ACT_POLYGON
    USDT_CONTRACTS.TTT = CONTRACT_ADDRESSES.TTT_TRON
  }
}
