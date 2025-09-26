/**
 * Centralized contract addresses configuration
 * This file re-exports contract addresses from the centralized configuration
 * No recompilation needed when contract addresses change
 */

// Re-export from centralized configuration
export {
  USDT_CONTRACTS,
  getContractAddress,
  getAllContractAddresses,
  getContractAddressByWalletType,
  getContractAddressByTokenType,
  initializeContractAddresses
} from '../config/contractAddresses.js'
