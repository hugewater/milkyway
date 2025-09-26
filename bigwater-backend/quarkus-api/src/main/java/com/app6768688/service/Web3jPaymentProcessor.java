package com.app6768688.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Web3j-based USDT Payment Processor for blockchain verification
 * Uses direct blockchain connection instead of external APIs
 */
public class Web3jPaymentProcessor {
    
    private final Web3j web3j;
    private final String companyWalletAddress;
    private final String usdtContractAddress;
    
    // ERC20 Transfer event signature
    private static final String TRANSFER_EVENT_SIGNATURE = 
        "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    
    /**
     * Constructor
     * @param rpcUrl Blockchain RPC URL
     * @param companyWalletAddress Your company's wallet address
     * @param usdtContractAddress USDT contract address
     */
    public Web3jPaymentProcessor(String rpcUrl, String companyWalletAddress, String usdtContractAddress) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.companyWalletAddress = companyWalletAddress.toLowerCase();
        this.usdtContractAddress = usdtContractAddress.toLowerCase();
    }
    
    /**
     * Find and verify USDT transfer from customer to company
     * @param customerWallet Customer's wallet address
     * @param expectedAmount Expected payment amount (in wei, 6 decimals for USDT)
     * @param blocksToSearch How many recent blocks to search
     * @return Payment verification result
     */
    public PaymentResult processPayment(String customerWallet, BigInteger expectedAmount, int blocksToSearch) {
        
        PaymentResult result = new PaymentResult();
        result.customerWallet = customerWallet.toLowerCase();
        result.expectedAmount = expectedAmount;
        result.companyWallet = companyWalletAddress;
        
        try {
            // STEP 1: Find the transaction
            List<TransactionMatch> matchingTransactions = findTransactions(
                customerWallet, 
                expectedAmount, 
                blocksToSearch
            );
            
            result.foundTransactions = matchingTransactions;
            
            if (matchingTransactions.isEmpty()) {
                result.status = PaymentStatus.NOT_FOUND;
                result.message = "No matching transaction found in the last " + blocksToSearch + " blocks.";
                return result;
            }
            
            // Use most recent transaction
            TransactionMatch transaction = matchingTransactions.get(0);
            result.transactionHash = transaction.txHash;
            result.actualAmount = transaction.amount;
            result.blockNumber = transaction.blockNumber;
            
            // STEP 2: Verify the transaction thoroughly
            boolean verified = verifyTransaction(transaction.txHash, expectedAmount);
            
            if (verified) {
                result.status = PaymentStatus.VERIFIED;
                result.message = "Payment verified successfully!";
                
                // Get confirmations
                BigInteger currentBlock = getCurrentBlockNumber();
                result.confirmations = currentBlock.subtract(transaction.blockNumber).intValue() + 1;
                
            } else {
                result.status = PaymentStatus.VERIFICATION_FAILED;
                result.message = "Transaction found but verification failed";
            }
            
        } catch (Exception e) {
            result.status = PaymentStatus.ERROR;
            result.message = "Error processing payment: " + e.getMessage();
        }
        
        return result;
    }
    
    /**
     * Find transactions from customer to company with matching amount
     */
    private List<TransactionMatch> findTransactions(String customerWallet, BigInteger expectedAmount, int blocksToSearch) {
        
        List<TransactionMatch> matches = new ArrayList<>();
        
        try {
            BigInteger currentBlock = web3j.ethBlockNumber().send().getBlockNumber();
            BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(blocksToSearch));
            
            if (fromBlock.compareTo(BigInteger.ZERO) < 0) {
                fromBlock = BigInteger.ZERO;
            }
            
            // Pad addresses for filtering
            String paddedFromAddress = "0x" + "0".repeat(24) + customerWallet.substring(2).toLowerCase();
            String paddedToAddress = "0x" + "0".repeat(24) + companyWalletAddress.substring(2);
            
            // Create filter
            EthFilter filter = new EthFilter(
                new DefaultBlockParameterNumber(fromBlock),
                new DefaultBlockParameterNumber(currentBlock),
                usdtContractAddress
            );
            
            filter.addSingleTopic(TRANSFER_EVENT_SIGNATURE);
            filter.addSingleTopic(paddedFromAddress);
            filter.addSingleTopic(paddedToAddress);
            
            // Get logs
            EthLog ethLog = web3j.ethGetLogs(filter).send();
            
            for (EthLog.LogResult logResult : ethLog.getLogs()) {
                Log log = (Log) logResult.get();
                
                if (log.getTopics().size() >= 3) {
                    String fromAddress = "0x" + log.getTopics().get(1).substring(26);
                    String toAddress = "0x" + log.getTopics().get(2).substring(26);
                    String data = log.getData();
                    BigInteger amount = new BigInteger(data.substring(2), 16);
                    
                    // Only include if amount matches or exceeds expected
                    if (amount.compareTo(expectedAmount) >= 0) {
                        matches.add(new TransactionMatch(
                            log.getTransactionHash(),
                            fromAddress,
                            toAddress,
                            amount,
                            log.getBlockNumber()
                        ));
                    }
                }
            }
            
            // Sort by block number (most recent first)
            matches.sort((a, b) -> b.blockNumber.compareTo(a.blockNumber));
            
        } catch (Exception e) {
            // Log error but don't throw
        }
        
        return matches;
    }
    
    /**
     * Verify transaction is valid and successful
     */
    private boolean verifyTransaction(String txHash, BigInteger expectedAmount) {
        try {
            EthGetTransactionReceipt receiptResponse = web3j
                .ethGetTransactionReceipt(txHash)
                .send();
            
            Optional<TransactionReceipt> receiptOpt = receiptResponse.getTransactionReceipt();
            
            if (!receiptOpt.isPresent()) {
                return false;
            }
            
            TransactionReceipt receipt = receiptOpt.get();
            
            // Check transaction status
            if (!receipt.isStatusOK()) {
                return false;
            }
            
            // Verify transfer event exists
            for (Log log : receipt.getLogs()) {
                if (log.getTopics().size() >= 3 && 
                    log.getAddress().equalsIgnoreCase(usdtContractAddress) &&
                    log.getTopics().get(0).equals(TRANSFER_EVENT_SIGNATURE)) {
                    
                    String toAddress = "0x" + log.getTopics().get(2).substring(26);
                    String data = log.getData();
                    BigInteger amount = new BigInteger(data.substring(2), 16);
                    
                    // Verify recipient and amount
                    boolean correctRecipient = toAddress.equalsIgnoreCase(companyWalletAddress);
                    boolean sufficientAmount = amount.compareTo(expectedAmount) >= 0;
                    
                    return correctRecipient && sufficientAmount;
                }
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get current block number
     */
    public BigInteger getCurrentBlockNumber() {
        try {
            return web3j.ethBlockNumber().send().getBlockNumber();
        } catch (Exception e) {
            return BigInteger.ZERO;
        }
    }
    
    /**
     * Close the Web3j connection
     */
    public void close() {
        web3j.shutdown();
    }
    
    /**
     * Transaction Match
     */
    public static class TransactionMatch {
        private final String txHash;
        private final String fromAddress;
        private final String toAddress;
        private final BigInteger amount;
        private final BigInteger blockNumber;
        
        public TransactionMatch(String txHash, String fromAddress, String toAddress, 
                               BigInteger amount, BigInteger blockNumber) {
            this.txHash = txHash;
            this.fromAddress = fromAddress;
            this.toAddress = toAddress;
            this.amount = amount;
            this.blockNumber = blockNumber;
        }
        
        public double getAmountAsUSDT() {
            return amount.doubleValue() / 1_000_000.0;
        }
        
        @Override
        public String toString() {
            return String.format("TX: %s | %.2f USDT | Block: %s", 
                txHash, getAmountAsUSDT(), blockNumber);
        }
    }
    
    /**
     * Payment Status Enum
     */
    public enum PaymentStatus {
        VERIFIED,           // Payment found and verified
        NOT_FOUND,          // No transaction found
        VERIFICATION_FAILED, // Transaction found but failed verification
        ERROR               // Error during processing
    }
    
    /**
     * Complete Payment Result
     */
    public static class PaymentResult {
        private PaymentStatus status;
        private String message;
        private String customerWallet;
        private String companyWallet;
        private String transactionHash;
        private BigInteger expectedAmount;
        private BigInteger actualAmount;
        private BigInteger blockNumber;
        private int confirmations;
        private List<TransactionMatch> foundTransactions = new ArrayList<>();
        
        public PaymentStatus getStatus() { return status; }
        public boolean isVerified() { return status == PaymentStatus.VERIFIED; }
        public String getMessage() { return message; }
        public String getTransactionHash() { return transactionHash; }
        public String getCustomerWallet() { return customerWallet; }
        public BigInteger getActualAmount() { return actualAmount; }
        public int getConfirmations() { return confirmations; }
        
        public double getExpectedAmountAsUSDT() {
            return expectedAmount != null ? expectedAmount.doubleValue() / 1_000_000.0 : 0;
        }
        
        public double getActualAmountAsUSDT() {
            return actualAmount != null ? actualAmount.doubleValue() / 1_000_000.0 : 0;
        }
    }
}
