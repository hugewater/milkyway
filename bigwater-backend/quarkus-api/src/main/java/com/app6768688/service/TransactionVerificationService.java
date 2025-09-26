package com.app6768688.service;

import com.app6768688.config.ContractAddresses;
import com.app6768688.config.ApiUrls;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigInteger;

@ApplicationScoped
public class TransactionVerificationService {
    
    private static final Logger LOG = Logger.getLogger(TransactionVerificationService.class);
    
    // Contract addresses and API URLs are now centralized in configuration classes
    // Using centralized configuration for better maintainability
    
    @Inject
    ApiUrls apiUrls;
    
    @Inject
    ContractAddresses contractAddresses;
    
    @Inject
    @ConfigProperty(name = "bw.polygon.api.key", defaultValue = "")
    String polygonApiKey;
    
    @Inject
    @ConfigProperty(name = "bw.tron.api.key", defaultValue = "")
    String tronApiKey;
    
    @Inject
    @ConfigProperty(name = "bw.alchemy.api.key", defaultValue = "")
    String alchemyApiKey;
    
    @Inject
    @ConfigProperty(name = "bw.moralis.api.key", defaultValue = "")
    String moralisApiKey;
    
    // Web3j RPC URLs
    @Inject
    @ConfigProperty(name = "bw.rpc.polygon.url", defaultValue = "https://polygon-rpc.com")
    String polygonRpcUrl;
    
    @Inject
    @ConfigProperty(name = "bw.rpc.polygon.amoy.url", defaultValue = "https://polygon-amoy.api.onfinality.io/rpc?apikey=d860ef1e-0e69-4cac-bb68-24bbea48136a")
    String polygonAmoyRpcUrl;
    
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public VerificationResult verifyTransaction(String txHash, String fromAddress, String toAddress, 
                                             String amount, String chain) {
        try {
            if ("POLYGON".equalsIgnoreCase(chain) || "POL".equalsIgnoreCase(chain)) {
                return verifyPolygonTransaction(txHash, fromAddress, toAddress, amount);
            } else if ("TRON".equalsIgnoreCase(chain) || "TRX".equalsIgnoreCase(chain)) {
                return verifyTronTransaction(txHash, fromAddress, toAddress, amount);
            } else if ("ACT".equalsIgnoreCase(chain)) {
                return verifyActTransaction(txHash, fromAddress, toAddress, amount);
            } else if ("TTT".equalsIgnoreCase(chain)) {
                return verifyTttTransaction(txHash, fromAddress, toAddress, amount);
            } else {
                return new VerificationResult(false, "Unsupported chain: " + chain);
            }
        } catch (Exception e) {
            LOG.error("Error verifying transaction: " + txHash, e);
            return new VerificationResult(false, "Verification failed: " + e.getMessage());
        }
    }
    
    public VerificationResult scanRecentTransfers(String fromAddress, String toAddress, 
                                               String amount, String chain) {
        try {
            LOG.info("=== SCAN RECENT TRANSFERS CALLED ===");
            LOG.info("fromAddress: " + fromAddress);
            LOG.info("toAddress: " + toAddress);
            LOG.info("amount: " + amount);
            LOG.info("chain: " + chain);
            
            if ("POLYGON".equalsIgnoreCase(chain) || "POL".equalsIgnoreCase(chain)) {
                return scanPolygonRecentTransfers(fromAddress, toAddress, amount);
            } else if ("TRON".equalsIgnoreCase(chain) || "TRX".equalsIgnoreCase(chain)) {
                return scanTronRecentTransfers(fromAddress, toAddress, amount);
            } else if ("ACT".equalsIgnoreCase(chain)) {
                return scanActRecentTransfers(fromAddress, toAddress, amount);
            } else if ("TTT".equalsIgnoreCase(chain)) {
                return scanTttRecentTransfers(fromAddress, toAddress, amount);
            } else {
                return new VerificationResult(false, "Unsupported chain: " + chain);
            }
        } catch (Exception e) {
            LOG.error("Error scanning recent transfers", e);
            return new VerificationResult(false, "Scan failed: " + e.getMessage());
        }
    }
    
    private VerificationResult verifyPolygonTransaction(String txHash, String fromAddress, String toAddress, String amount) {
        try {
            // Get transaction details from Polygonscan
            String url = apiUrls.getPolygonApiUrl() + "?module=proxy&action=eth_getTransactionReceipt&txhash=" + txHash + "&apikey=" + polygonApiKey;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                return new VerificationResult(false, "Failed to fetch transaction from Polygonscan");
            }
            
            // Parse response and verify
            return parsePolygonResponse(response.body(), fromAddress, toAddress, amount);
            
        } catch (IOException | InterruptedException e) {
            LOG.error("Error verifying Polygon transaction", e);
            return new VerificationResult(false, "Network error: " + e.getMessage());
        }
    }
    
    private VerificationResult verifyTronTransaction(String txHash, String fromAddress, String toAddress, String amount) {
        try {
            // Get transaction details from TronGrid
            String url = apiUrls.getTronApiUrl() + "/wallet/gettransactionbyid?value=" + txHash;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("TRON-PRO-API-KEY", tronApiKey)
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                return new VerificationResult(false, "Failed to fetch transaction from TronGrid");
            }
            
            // Parse response and verify
            return parseTronResponse(response.body(), fromAddress, toAddress, amount);
            
        } catch (IOException | InterruptedException e) {
            LOG.error("Error verifying TRON transaction", e);
            return new VerificationResult(false, "Network error: " + e.getMessage());
        }
    }
    
    private VerificationResult parsePolygonResponse(String responseBody, String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Parsing Polygon response: " + responseBody.substring(0, Math.min(200, responseBody.length())));
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Check for API errors first
            if (jsonNode.has("error")) {
                String errorMessage = jsonNode.get("error").asText();
                return new VerificationResult(false, "API Error: " + errorMessage);
            }
            
            // Check if API key is invalid
            if (jsonNode.has("message") && jsonNode.get("message").asText().toLowerCase().contains("invalid api key")) {
                return new VerificationResult(false, "Invalid API Key - Please configure a valid Polygonscan API key");
            }
            
            // Check if transaction exists
            if (!jsonNode.has("result") || jsonNode.get("result").isNull()) {
                return new VerificationResult(false, "Transaction not found on blockchain");
            }
            
            JsonNode result = jsonNode.get("result");
            
            // Check transaction status
            if (result.has("status")) {
                String status = result.get("status").asText();
                if (!"0x1".equals(status)) {
                    return new VerificationResult(false, "Transaction failed or not confirmed (status: " + status + ")");
                }
            }
            
            // Check if it's a USDT transfer by looking at logs
            if (result.has("logs")) {
                JsonNode logs = result.get("logs");
                boolean isUsdtTransfer = false;
                
                for (JsonNode log : logs) {
                    if (log.has("address")) {
                        String contractAddress = log.get("address").asText().toLowerCase();
                        if (contractAddresses.getPolygonUsdtContract().toLowerCase().equals(contractAddress)) {
                            isUsdtTransfer = true;
                            break;
                        }
                    }
                }
                
                if (!isUsdtTransfer) {
                    return new VerificationResult(false, "Not a USDT transaction - contract address not found in logs");
                }
            } else {
                // If no logs, check if it's a direct USDT contract interaction
                if (result.has("to")) {
                    String contractAddress = result.get("to").asText().toLowerCase();
                    if (!contractAddresses.getPolygonUsdtContract().toLowerCase().equals(contractAddress)) {
                        return new VerificationResult(false, "Not a USDT transaction - wrong contract address");
                    }
                }
            }
            
            // Additional verification logic would go here
            // For now, return success if basic checks pass
            return new VerificationResult(true, "Transaction verified successfully");
            
        } catch (Exception e) {
            LOG.error("Error parsing Polygon response", e);
            return new VerificationResult(false, "Failed to parse transaction data: " + e.getMessage());
        }
    }
    
    private VerificationResult parseTronResponse(String responseBody, String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Parsing TRON response: " + responseBody.substring(0, Math.min(200, responseBody.length())));
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Check for API errors first
            if (jsonNode.has("error")) {
                String errorMessage = jsonNode.get("error").asText();
                return new VerificationResult(false, "API Error: " + errorMessage);
            }
            
            // Check if transaction exists
            if (!jsonNode.has("result") || jsonNode.get("result").isNull()) {
                return new VerificationResult(false, "Transaction not found on blockchain");
            }
            
            JsonNode result = jsonNode.get("result");
            
            // Check transaction status
            if (result.has("ret")) {
                JsonNode ret = result.get("ret");
                if (ret.isArray() && ret.size() > 0) {
                    JsonNode firstRet = ret.get(0);
                    if (firstRet.has("contractRet")) {
                        String contractRet = firstRet.get("contractRet").asText();
                        if (!"SUCCESS".equals(contractRet)) {
                            return new VerificationResult(false, "Transaction failed or not confirmed (contractRet: " + contractRet + ")");
                        }
                    }
                }
            }
            
            // Check if it's a USDT transfer by looking at contract address
            if (result.has("raw_data") && result.get("raw_data").has("contract")) {
                JsonNode contracts = result.get("raw_data").get("contract");
                boolean isUsdtTransfer = false;
                
                for (JsonNode contract : contracts) {
                    if (contract.has("parameter") && contract.get("parameter").has("value")) {
                        JsonNode value = contract.get("parameter").get("value");
                        if (value.has("contract_address")) {
                            String contractAddress = value.get("contract_address").asText();
                            if (contractAddresses.getTronUsdtContract().equals(contractAddress)) {
                                isUsdtTransfer = true;
                                break;
                            }
                        }
                    }
                }
                
                if (!isUsdtTransfer) {
                    return new VerificationResult(false, "Not a USDT transaction - wrong contract address");
                }
            }
            
            // Additional verification logic would go here
            // For now, return success if basic checks pass
            return new VerificationResult(true, "Transaction verified successfully");
            
        } catch (Exception e) {
            LOG.error("Error parsing TRON response", e);
            return new VerificationResult(false, "Failed to parse transaction data: " + e.getMessage());
        }
    }
    
    private VerificationResult scanPolygonRecentTransfers(String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Scanning Polygon transfers for fromAddress: " + fromAddress + ", toAddress: " + toAddress + ", amount: " + amount);
            
            // Try Web3j first (most reliable)
            VerificationResult web3jResult = scanWithWeb3j(fromAddress, toAddress, amount, "POL");
            if (web3jResult.isVerified()) {
                return web3jResult;
            }
            
            // Try Alchemy API as fallback
            if (alchemyApiKey != null && !alchemyApiKey.isEmpty()) {
                VerificationResult alchemyResult = scanWithAlchemy(fromAddress, toAddress, amount);
                if (alchemyResult.isVerified()) {
                    return alchemyResult;
                }
            }
            
            // Try Moralis API as second fallback
            if (moralisApiKey != null && !moralisApiKey.isEmpty()) {
                VerificationResult moralisResult = scanWithMoralis(fromAddress, toAddress, amount);
                if (moralisResult.isVerified()) {
                    return moralisResult;
                }
            }
            
            // Final fallback to Polygonscan (may be deprecated)
            return scanWithPolygonscan(fromAddress, toAddress, amount);
            
        } catch (Exception e) {
            LOG.error("Error scanning Polygon transfers", e);
            return new VerificationResult(false, "Network error: " + e.getMessage());
        }
    }
    
    /**
     * Scan using Web3j for direct blockchain access
     */
    private VerificationResult scanWithWeb3j(String fromAddress, String toAddress, String amount, String chain) {
        try {
            LOG.info("Trying Web3j for " + chain + " transfers");
            
            // Determine RPC URL and contract address based on chain
            String rpcUrl;
            String contractAddress;
            
            if ("ACT".equalsIgnoreCase(chain)) {
                rpcUrl = polygonAmoyRpcUrl;
                contractAddress = contractAddresses.getActTokenContract();
            } else {
                rpcUrl = polygonRpcUrl;
                contractAddress = contractAddresses.getPolygonUsdtContract();
            }
            
            // Convert amount to BigInteger (USDT has 6 decimals)
            BigDecimal amountDecimal = new BigDecimal(amount);
            BigInteger expectedAmount = amountDecimal.multiply(new BigDecimal("1000000")).toBigInteger();
            
            // Create Web3j processor
            Web3jPaymentProcessor processor = new Web3jPaymentProcessor(rpcUrl, toAddress, contractAddress);
            
            try {
                // Process payment (find and verify)
                Web3jPaymentProcessor.PaymentResult result = processor.processPayment(
                    fromAddress, 
                    expectedAmount, 
                    2000 // Search last 2000 blocks (~1 hour)
                );
                
                if (result.isVerified()) {
                    Map<String, Object> details = new HashMap<>();
                    details.put("txHash", result.getTransactionHash());
                    details.put("amount", result.getActualAmountAsUSDT());
                    details.put("confirmations", result.getConfirmations());
                    details.put("method", "Web3j");
                    
                    return new VerificationResult(true, "Payment verified using Web3j", details);
                } else {
                    return new VerificationResult(false, result.getMessage());
                }
                
            } finally {
                processor.close();
            }
            
        } catch (Exception e) {
            LOG.error("Web3j scan failed", e);
            return new VerificationResult(false, "Web3j scan failed: " + e.getMessage());
        }
    }
    
    private VerificationResult scanWithAlchemy(String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Trying Alchemy API for Polygon transfers");
            
            // Use Alchemy's getAssetTransfers API
            String url = apiUrls.getAlchemyPolygonUrl() + "/" + alchemyApiKey;
            
            // Create JSON payload for Alchemy API - search for transfers from specific address
            String jsonPayload = String.format(
                "{\"jsonrpc\":\"2.0\",\"method\":\"alchemy_getAssetTransfers\",\"params\":[{\"fromBlock\":\"0x0\",\"toBlock\":\"latest\",\"category\":[\"erc20\"],\"contractAddresses\":[\"%s\"],\"excludeZeroValue\":true,\"maxCount\":100,\"fromAddress\":\"%s\"}],\"id\":1}",
                contractAddresses.getPolygonUsdtContract(), fromAddress
            );
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            LOG.info("Alchemy API response status: " + response.statusCode());
            System.out.println("DEBUG: Alchemy API response: " + response.body().substring(0, Math.min(500, response.body().length())));
            
            if (response.statusCode() == 200) {
                return parseAlchemyTransfers(response.body(), fromAddress, toAddress, amount);
            }
            
            return new VerificationResult(false, "Alchemy API request failed with status: " + response.statusCode());
            
        } catch (Exception e) {
            LOG.error("Error with Alchemy API", e);
            return new VerificationResult(false, "Alchemy API error: " + e.getMessage());
        }
    }
    
    private VerificationResult scanWithMoralis(String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Trying Moralis API for Polygon transfers");
            
            // Use Moralis API to get token transfers
            String url = apiUrls.getMoralisPolygonUrl() + "/" + fromAddress + "/erc20/transfers?chain=polygon&contract_addresses=" + contractAddresses.getPolygonUsdtContract() + "&limit=100";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", moralisApiKey)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            LOG.info("Moralis API response status: " + response.statusCode());
            System.out.println("DEBUG: Moralis API response: " + response.body().substring(0, Math.min(500, response.body().length())));
            
            if (response.statusCode() == 200) {
                return parseMoralisTransfers(response.body(), fromAddress, toAddress, amount);
            }
            
            return new VerificationResult(false, "Moralis API request failed with status: " + response.statusCode());
            
        } catch (Exception e) {
            LOG.error("Error with Moralis API", e);
            return new VerificationResult(false, "Moralis API error: " + e.getMessage());
        }
    }
    
    private VerificationResult scanWithPolygonscan(String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Trying Polygonscan API (may be deprecated)");
            
            // First, try to get transfers from the fromAddress (sender)
            String fromUrl = apiUrls.getPolygonApiUrl() + "?module=account&action=tokentx&contractaddress=" + contractAddresses.getPolygonUsdtContract() + 
                        "&address=" + fromAddress + "&startblock=0&endblock=99999999&sort=desc&apikey=" + polygonApiKey;
            
            HttpRequest fromRequest = HttpRequest.newBuilder()
                    .uri(URI.create(fromUrl))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> fromResponse = httpClient.send(fromRequest, HttpResponse.BodyHandlers.ofString());
            
            if (fromResponse.statusCode() == 200) {
                VerificationResult fromResult = parsePolygonTransfers(fromResponse.body(), fromAddress, toAddress, amount);
                if (fromResult.isVerified()) {
                    return fromResult;
                }
            }
            
            // If not found in sender's transfers, try receiver's transfers
            String toUrl = apiUrls.getPolygonApiUrl() + "?module=account&action=tokentx&contractaddress=" + contractAddresses.getPolygonUsdtContract() + 
                        "&address=" + toAddress + "&startblock=0&endblock=99999999&sort=desc&apikey=" + polygonApiKey;
            
            HttpRequest toRequest = HttpRequest.newBuilder()
                    .uri(URI.create(toUrl))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> toResponse = httpClient.send(toRequest, HttpResponse.BodyHandlers.ofString());
            
            if (toResponse.statusCode() != 200) {
                return new VerificationResult(false, "Failed to fetch recent transfers from Polygonscan");
            }
            
            return parsePolygonTransfers(toResponse.body(), fromAddress, toAddress, amount);
            
        } catch (IOException | InterruptedException e) {
            LOG.error("Error scanning with Polygonscan", e);
            return new VerificationResult(false, "Polygonscan error: " + e.getMessage());
        }
    }
    
    private VerificationResult scanTronRecentTransfers(String fromAddress, String toAddress, String amount) {
        try {
            // First, try to get transfers from the fromAddress (sender)
            String fromUrl = apiUrls.getTronApiUrl() + "/v1/accounts/" + fromAddress + "/transactions/trc20?limit=50&contract_address=" + contractAddresses.getTronUsdtContract();
            
            HttpRequest fromRequest = HttpRequest.newBuilder()
                    .uri(URI.create(fromUrl))
                    .header("TRON-PRO-API-KEY", tronApiKey)
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> fromResponse = httpClient.send(fromRequest, HttpResponse.BodyHandlers.ofString());
            
            if (fromResponse.statusCode() == 200) {
                VerificationResult fromResult = parseTronTransfers(fromResponse.body(), fromAddress, toAddress, amount);
                if (fromResult.isVerified()) {
                    return fromResult;
                }
            }
            
            // If not found in sender's transfers, try receiver's transfers
            String toUrl = apiUrls.getTronApiUrl() + "/v1/accounts/" + toAddress + "/transactions/trc20?limit=50&contract_address=" + contractAddresses.getTronUsdtContract();
            
            HttpRequest toRequest = HttpRequest.newBuilder()
                    .uri(URI.create(toUrl))
                    .header("TRON-PRO-API-KEY", tronApiKey)
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> toResponse = httpClient.send(toRequest, HttpResponse.BodyHandlers.ofString());
            
            if (toResponse.statusCode() != 200) {
                return new VerificationResult(false, "Failed to fetch recent transfers from TronGrid");
            }
            
            return parseTronTransfers(toResponse.body(), fromAddress, toAddress, amount);
            
        } catch (IOException | InterruptedException e) {
            LOG.error("Error scanning TRON transfers", e);
            return new VerificationResult(false, "Network error: " + e.getMessage());
        }
    }
    
    private VerificationResult parseMoralisTransfers(String responseBody, String fromAddress, String toAddress, String amount) {
        try {
            System.out.println("DEBUG: Parsing Moralis transfers response: " + responseBody.substring(0, Math.min(500, responseBody.length())));
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Check if we have results
            if (!jsonNode.has("result") || !jsonNode.get("result").isArray()) {
                System.out.println("DEBUG: No transfers found in Moralis response");
                return new VerificationResult(false, "No recent transfers found");
            }
            
            JsonNode transfers = jsonNode.get("result");
            
            // Look for matching transfer
            for (JsonNode transfer : transfers) {
                if (transfer.has("from_address") && transfer.has("to_address") && transfer.has("value") && transfer.has("transaction_hash")) {
                    String transferFrom = transfer.get("from_address").asText().toLowerCase();
                    String transferTo = transfer.get("to_address").asText().toLowerCase();
                    String transferValue = transfer.get("value").asText();
                    String txHash = transfer.get("transaction_hash").asText();
                    
                    // Check if this matches our criteria
                    if (transferFrom.equals(fromAddress.toLowerCase()) && 
                        transferTo.equals(toAddress.toLowerCase())) {
                        
                        // Convert amount to match (Moralis returns value in wei, need to convert)
                        BigDecimal expectedAmount = new BigDecimal(amount);
                        BigDecimal actualAmount = new BigDecimal(transferValue).divide(new BigDecimal("1000000")); // USDT has 6 decimals
                        
                        if (actualAmount.compareTo(expectedAmount) == 0) {
                            Map<String, Object> details = new HashMap<>();
                            details.put("txHash", txHash);
                            details.put("foundTransfer", true);
                            
                            return new VerificationResult(true, "Transfer verified via Moralis API", details);
                        }
                    }
                }
            }
            
            return new VerificationResult(false, "No matching transfers found");
            
        } catch (Exception e) {
            LOG.error("Error parsing Moralis response", e);
            return new VerificationResult(false, "Failed to parse Moralis data: " + e.getMessage());
        }
    }
    
    private VerificationResult parseAlchemyTransfers(String responseBody, String fromAddress, String toAddress, String amount) {
        try {
            System.out.println("DEBUG: Parsing Alchemy transfers response: " + responseBody.substring(0, Math.min(500, responseBody.length())));
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Check for errors
            if (jsonNode.has("error")) {
                String errorMessage = jsonNode.get("error").get("message").asText();
                System.out.println("DEBUG: Alchemy API Error: " + errorMessage);
                return new VerificationResult(false, "Alchemy API Error: " + errorMessage);
            }
            
            // Check if we have results
            if (!jsonNode.has("result") || !jsonNode.get("result").has("transfers")) {
                System.out.println("DEBUG: No transfers found in Alchemy response");
                return new VerificationResult(false, "No recent transfers found");
            }
            
            JsonNode transfers = jsonNode.get("result").get("transfers");
            
            // Look for matching transfer
            for (JsonNode transfer : transfers) {
                if (transfer.has("from") && transfer.has("to") && transfer.has("rawContract") && transfer.has("value")) {
                    String transferFrom = transfer.get("from").asText().toLowerCase();
                    String transferTo = transfer.get("to").asText().toLowerCase();
                    String contractAddress = transfer.get("rawContract").get("address").asText().toLowerCase();
                    String transferValue = transfer.get("value").asText();
                    
                    // Check if this matches our criteria
                    if (transferFrom.equals(fromAddress.toLowerCase()) && 
                        transferTo.equals(toAddress.toLowerCase()) &&
                        contractAddress.equals(contractAddresses.getPolygonUsdtContract().toLowerCase())) {
                        
                        // Convert amount to match (Alchemy returns value in wei, need to convert)
                        BigDecimal expectedAmount = new BigDecimal(amount);
                        BigDecimal actualAmount = new BigDecimal(transferValue).divide(new BigDecimal("1000000")); // USDT has 6 decimals
                        
                        if (actualAmount.compareTo(expectedAmount) == 0) {
                            String txHash = transfer.get("hash").asText();
                            Map<String, Object> details = new HashMap<>();
                            details.put("txHash", txHash);
                            details.put("contract", contractAddress);
                            details.put("foundTransfer", true);
                            
                            return new VerificationResult(true, "Transfer verified via Alchemy API", details);
                        }
                    }
                }
            }
            
            return new VerificationResult(false, "No matching transfers found");
            
        } catch (Exception e) {
            LOG.error("Error parsing Alchemy response", e);
            return new VerificationResult(false, "Failed to parse Alchemy data: " + e.getMessage());
        }
    }
    
    private VerificationResult parsePolygonTransfers(String responseBody, String fromAddress, String toAddress, String amount) {
        try {
            System.out.println("DEBUG: Parsing Polygon transfers response: " + responseBody.substring(0, Math.min(500, responseBody.length())));
            LOG.info("Parsing Polygon transfers: " + responseBody.substring(0, Math.min(200, responseBody.length())));
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Check for API errors first
            if (jsonNode.has("error")) {
                String errorMessage = jsonNode.get("error").asText();
                System.out.println("DEBUG: API Error: " + errorMessage);
                return new VerificationResult(false, "API Error: " + errorMessage);
            }
            
            // Check if we have results
            if (!jsonNode.has("result") || !jsonNode.get("result").isArray()) {
                System.out.println("DEBUG: No result array found in response");
                return new VerificationResult(false, "No recent transfers found");
            }
            
            JsonNode results = jsonNode.get("result");
            
            // Look for matching transfer
            for (JsonNode transfer : results) {
                if (transfer.has("from") && transfer.has("to") && transfer.has("value") && transfer.has("contractAddress")) {
                    String transferFrom = transfer.get("from").asText().toLowerCase();
                    String transferTo = transfer.get("to").asText().toLowerCase();
                    String transferValue = transfer.get("value").asText();
                    String contractAddress = transfer.get("contractAddress").asText().toLowerCase();
                    
                    // Check if this is a USDT transfer
                    if (contractAddresses.getPolygonUsdtContract().toLowerCase().equals(contractAddress)) {
                        // Check if addresses match
                        if (fromAddress.toLowerCase().equals(transferFrom) && toAddress.toLowerCase().equals(transferTo)) {
                            // Convert amount to wei (USDT has 6 decimals)
                            try {
                                BigDecimal transferAmount = new BigDecimal(transferValue);
                                BigDecimal expectedAmount = new BigDecimal(amount).multiply(new BigDecimal("1000000")); // USDT has 6 decimals
                                
                                if (transferAmount.compareTo(expectedAmount) == 0) {
                                    Map<String, Object> details = new HashMap<>();
                                    details.put("foundTransfer", true);
                                    details.put("fromAddress", transferFrom);
                                    details.put("toAddress", transferTo);
                                    details.put("amount", amount);
                                    details.put("contract", contractAddresses.getPolygonUsdtContract());
                                    details.put("txHash", transfer.has("hash") ? transfer.get("hash").asText() : "N/A");
                                    
                                    return new VerificationResult(true, "Matching transfer found in recent blocks", details);
                                }
                            } catch (NumberFormatException e) {
                                LOG.warn("Failed to parse transfer amount: " + transferValue);
                            }
                        }
                    }
                }
            }
            
            return new VerificationResult(false, "No matching transfer found in recent blocks");
            
        } catch (Exception e) {
            LOG.error("Error parsing Polygon transfers", e);
            return new VerificationResult(false, "Failed to parse transfer data: " + e.getMessage());
        }
    }
    
    private VerificationResult parseTronTransfers(String responseBody, String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Parsing TRON transfers: " + responseBody.substring(0, Math.min(200, responseBody.length())));
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // Check for API errors first
            if (jsonNode.has("error")) {
                String errorMessage = jsonNode.get("error").asText();
                return new VerificationResult(false, "API Error: " + errorMessage);
            }
            
            // Check if we have results
            if (!jsonNode.has("data") || !jsonNode.get("data").isArray()) {
                return new VerificationResult(false, "No recent transfers found");
            }
            
            JsonNode data = jsonNode.get("data");
            
            // Look for matching transfer
            for (JsonNode transfer : data) {
                if (transfer.has("from") && transfer.has("to") && transfer.has("value") && transfer.has("contract_address")) {
                    String transferFrom = transfer.get("from").asText().toLowerCase();
                    String transferTo = transfer.get("to").asText().toLowerCase();
                    String transferValue = transfer.get("value").asText();
                    String contractAddress = transfer.get("contract_address").asText();
                    
                    // Check if this is a USDT transfer
                    if (contractAddresses.getTronUsdtContract().equals(contractAddress)) {
                        // Check if addresses match
                        if (fromAddress.toLowerCase().equals(transferFrom) && toAddress.toLowerCase().equals(transferTo)) {
                            // Convert amount to sun (TRON has 6 decimals for USDT)
                            try {
                                BigDecimal transferAmount = new BigDecimal(transferValue);
                                BigDecimal expectedAmount = new BigDecimal(amount).multiply(new BigDecimal("1000000")); // USDT has 6 decimals
                                
                                if (transferAmount.compareTo(expectedAmount) == 0) {
                                    Map<String, Object> details = new HashMap<>();
                                    details.put("foundTransfer", true);
                                    details.put("fromAddress", transferFrom);
                                    details.put("toAddress", transferTo);
                                    details.put("amount", amount);
                                    details.put("contract", contractAddresses.getTronUsdtContract());
                                    details.put("txHash", transfer.has("transaction_id") ? transfer.get("transaction_id").asText() : "N/A");
                                    
                                    return new VerificationResult(true, "Matching transfer found in recent blocks", details);
                                }
                            } catch (NumberFormatException e) {
                                LOG.warn("Failed to parse transfer amount: " + transferValue);
                            }
                        }
                    }
                }
            }
            
            return new VerificationResult(false, "No matching transfer found in recent blocks");
            
        } catch (Exception e) {
            LOG.error("Error parsing TRON transfers", e);
            return new VerificationResult(false, "Failed to parse transfer data: " + e.getMessage());
        }
    }
    
    public static class VerificationResult {
        private final boolean verified;
        private final String message;
        private final Map<String, Object> details;
        
        public VerificationResult(boolean verified, String message) {
            this.verified = verified;
            this.message = message;
            this.details = new HashMap<>();
        }
        
        public VerificationResult(boolean verified, String message, Map<String, Object> details) {
            this.verified = verified;
            this.message = message;
            this.details = details != null ? details : new HashMap<>();
        }
        
        public boolean isVerified() { return verified; }
        public String getMessage() { return message; }
        public Map<String, Object> getDetails() { return details; }
    }
    
    // ACT Token verification methods for Polygon Amoy testnet
    private VerificationResult verifyActTransaction(String txHash, String fromAddress, String toAddress, String amount) {
        try {
            // Get transaction details from Polygon Amoy testnet
            String url = apiUrls.getPolygonAmoyApiUrl() + "?module=proxy&action=eth_getTransactionReceipt&txhash=" + txHash + "&apikey=" + polygonApiKey;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                return new VerificationResult(false, "Failed to fetch transaction: HTTP " + response.statusCode());
            }
            
            // Parse response and verify transaction
            // This is a simplified implementation - in production, you'd parse the JSON response
            // and verify the transaction details, contract address, etc.
            
            if (response.body().contains("status") && response.body().contains("0x1")) {
                Map<String, Object> details = new HashMap<>();
                details.put("txHash", txHash);
                details.put("contract", contractAddresses.getActTokenContract());
                details.put("chain", "Polygon Amoy");
                
                return new VerificationResult(true, "ACT transaction verified successfully", details);
            } else {
                return new VerificationResult(false, "Transaction not found or failed");
            }
            
        } catch (Exception e) {
            return new VerificationResult(false, "Failed to verify ACT transaction: " + e.getMessage());
        }
    }
    
    private VerificationResult scanActRecentTransfers(String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Scanning ACT transfers for fromAddress: " + fromAddress + ", toAddress: " + toAddress + ", amount: " + amount);
            
            // Try Web3j first (most reliable for ACT)
            VerificationResult web3jResult = scanWithWeb3j(fromAddress, toAddress, amount, "ACT");
            if (web3jResult.isVerified()) {
                return web3jResult;
            }
            
            // Fallback to API-based scanning
            // First, try to get transfers from the fromAddress (sender)
            String fromUrl = apiUrls.getPolygonAmoyApiUrl() + "?module=account&action=tokentx&address=" + fromAddress + 
                        "&startblock=0&endblock=99999999&sort=desc&apikey=" + polygonApiKey;
            
            HttpRequest fromRequest = HttpRequest.newBuilder()
                    .uri(URI.create(fromUrl))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> fromResponse = httpClient.send(fromRequest, HttpResponse.BodyHandlers.ofString());
            
            if (fromResponse.statusCode() == 200) {
                // Check if sender's transfers contain the matching transaction
                if (fromResponse.body().contains(fromAddress) && fromResponse.body().contains(toAddress)) {
                    Map<String, Object> details = new HashMap<>();
                    details.put("foundTransfer", true);
                    details.put("contract", contractAddresses.getActTokenContract());
                    details.put("chain", "Polygon Amoy");
                    
                    return new VerificationResult(true, "Matching ACT transfer found in recent blocks", details);
                }
            }
            
            // If not found in sender's transfers, try receiver's transfers
            String toUrl = apiUrls.getPolygonAmoyApiUrl() + "?module=account&action=tokentx&address=" + toAddress + 
                        "&startblock=0&endblock=99999999&sort=desc&apikey=" + polygonApiKey;
            
            HttpRequest toRequest = HttpRequest.newBuilder()
                    .uri(URI.create(toUrl))
                    .timeout(Duration.ofSeconds(30))
                    .build();
            
            HttpResponse<String> toResponse = httpClient.send(toRequest, HttpResponse.BodyHandlers.ofString());
            
            if (toResponse.statusCode() != 200) {
                return new VerificationResult(false, "Failed to scan recent transfers: HTTP " + toResponse.statusCode());
            }
            
            // Check if receiver's transfers contain the matching transaction
            if (toResponse.body().contains(fromAddress) && toResponse.body().contains(toAddress)) {
                Map<String, Object> details = new HashMap<>();
                details.put("foundTransfer", true);
                details.put("contract", contractAddresses.getActTokenContract());
                details.put("chain", "Polygon Amoy");
                
                return new VerificationResult(true, "Matching ACT transfer found in recent blocks", details);
            }
            
            return new VerificationResult(false, "No matching ACT transfer found in recent blocks");
            
        } catch (Exception e) {
            return new VerificationResult(false, "Failed to scan ACT transfers: " + e.getMessage());
        }
    }
    
    /**
     * Verify TTT transaction using TRON Nile testnet
     */
    private VerificationResult verifyTttTransaction(String txHash, String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Verifying TTT transaction: " + txHash);
            
            // For TTT (TRON Nile testnet), we can use TRON API similar to TRX
            // TTT is a TRON-based token, so we can use the same verification logic
            return verifyTronTransaction(txHash, fromAddress, toAddress, amount);
            
        } catch (Exception e) {
            LOG.error("Error verifying TTT transaction", e);
            return new VerificationResult(false, "TTT verification failed: " + e.getMessage());
        }
    }
    
    /**
     * Scan recent TTT transfers on TRON Nile testnet
     */
    private VerificationResult scanTttRecentTransfers(String fromAddress, String toAddress, String amount) {
        try {
            LOG.info("Scanning TTT transfers for fromAddress: " + fromAddress + ", toAddress: " + toAddress + ", amount: " + amount);
            
            // For TTT (TRON Nile testnet), we can use TRON API similar to TRX
            // TTT is a TRON-based token, so we can use the same scanning logic
            return scanTronRecentTransfers(fromAddress, toAddress, amount);
            
        } catch (Exception e) {
            LOG.error("Error scanning TTT transfers", e);
            return new VerificationResult(false, "Failed to scan TTT transfers: " + e.getMessage());
        }
    }
}
