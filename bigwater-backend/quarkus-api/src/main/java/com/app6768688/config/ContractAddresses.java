package com.app6768688.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Centralized contract addresses configuration
 * This class loads all blockchain contract addresses from application.properties
 * No recompilation needed when contract addresses change
 */
@ApplicationScoped
public class ContractAddresses {
    
    // USDT Contract Addresses - loaded from configuration
    @Inject
    @ConfigProperty(name = "bw.contract.usdt.polygon", defaultValue = "0xc2132D05D31c914a87C6611C10748AEb04B58e8F")
    String polygonUsdtContract;
    
    @Inject
    @ConfigProperty(name = "bw.contract.usdt.tron", defaultValue = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t")
    String tronUsdtContract;
    
    @Inject
    @ConfigProperty(name = "bw.contract.usdt.ethereum", defaultValue = "0xdAC17F958D2ee523a2206206994597C13D831ec7")
    String ethereumUsdtContract;
    
    @Inject
    @ConfigProperty(name = "bw.contract.usdt.bsc", defaultValue = "0x55d398326f99059fF775485246999027B3197955")
    String bscUsdtContract;
    
    // ACT Token Contract Addresses - loaded from configuration
    @Inject
    @ConfigProperty(name = "bw.contract.act.polygon", defaultValue = "0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082")
    String actTokenContract;
    
    // TTT Token Contract Addresses - loaded from configuration
    @Inject
    @ConfigProperty(name = "bw.contract.ttt.tron", defaultValue = "TQwa7kTensPjJVUdfpqiPBGixaNAenCDMS")
    String tttTokenContract;
    
    // Getters for contract addresses
    public String getPolygonUsdtContract() {
        return polygonUsdtContract;
    }
    
    public String getTronUsdtContract() {
        return tronUsdtContract;
    }
    
    public String getEthereumUsdtContract() {
        return ethereumUsdtContract;
    }
    
    public String getBscUsdtContract() {
        return bscUsdtContract;
    }
    
    public String getActTokenContract() {
        return actTokenContract;
    }
    
    public String getTttTokenContract() {
        return tttTokenContract;
    }
    
    /**
     * Get contract address by token type and network
     * @param tokenType Token type (USDT, ACT, etc.)
     * @param network Network (POL, TRX, ETH, BSC, etc.)
     * @return Contract address or null if not found
     */
    public String getContractAddress(String tokenType, String network) {
        if ("USDT".equalsIgnoreCase(tokenType)) {
            switch (network.toUpperCase()) {
                case "POL":
                case "POLYGON":
                    return polygonUsdtContract;
                case "TRX":
                case "TRON":
                    return tronUsdtContract;
                case "ETH":
                case "ETHEREUM":
                    return ethereumUsdtContract;
                case "BSC":
                case "BINANCE":
                    return bscUsdtContract;
                default:
                    return null;
            }
        } else if ("ACT".equalsIgnoreCase(tokenType)) {
            return actTokenContract;
        } else if ("TTT".equalsIgnoreCase(tokenType)) {
            return tttTokenContract;
        }
        return null;
    }
    
    /**
     * Get all supported contract addresses as a map
     * @return Map of token type and network combinations to contract addresses
     */
    public java.util.Map<String, String> getAllContractAddresses() {
        java.util.Map<String, String> contracts = new java.util.HashMap<>();
        
        // USDT contracts
        contracts.put("USDT_POL", polygonUsdtContract);
        contracts.put("USDT_TRX", tronUsdtContract);
        contracts.put("USDT_ETH", ethereumUsdtContract);
        contracts.put("USDT_BSC", bscUsdtContract);
        
        // ACT contracts
        contracts.put("ACT_POL", actTokenContract);
        
        // TTT contracts
        contracts.put("TTT_TON", tttTokenContract);
        
        return contracts;
    }
    
    /**
     * Get contract address by wallet type (for backward compatibility)
     * @param walletType Wallet type (POL, TRX, ETH, BSC, ACT)
     * @return Contract address or null if not found
     */
    public String getContractAddressByWalletType(String walletType) {
        switch (walletType.toUpperCase()) {
            case "POL":
            case "POLYGON":
                return polygonUsdtContract;
            case "TRX":
            case "TRON":
                return tronUsdtContract;
            case "ETH":
            case "ETHEREUM":
                return ethereumUsdtContract;
            case "BSC":
            case "BINANCE":
                return bscUsdtContract;
            case "ACT":
                return actTokenContract;
            case "TTT":
                return tttTokenContract;
            default:
                return null;
        }
    }
}
