package com.app6768688.service;

import com.app6768688.model.Transaction;
import com.app6768688.model.Wallet;
import com.app6768688.model.UsdtWallet; // Keep for backward compatibility
import com.app6768688.repository.WalletRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WalletService {

    @Inject
    WalletRepository walletRepository;
    
    @Inject
    TransactionService transactionService;

    // NEW WALLET MODEL METHODS
    
    @Transactional
    public Wallet createUserWallet(Long userId, String walletName) {
        // Check if user already has a wallet
        Optional<Wallet> existingWallet = findWalletByUserId(userId);
        if (existingWallet.isPresent()) {
            throw new RuntimeException("User already has a wallet");
        }

        String finalWalletName = walletName != null ? walletName : "Primary Wallet";
        Wallet wallet = new Wallet(userId, finalWalletName);
        
        return walletRepository.createWallet(wallet);
    }
    
    @Transactional
    public Wallet createUserWallet(Long userId, String walletName, String tronAddress, String polygonAddress) {
        // Check if user already has a wallet
        Optional<Wallet> existingWallet = findWalletByUserId(userId);
        if (existingWallet.isPresent()) {
            throw new RuntimeException("User already has a wallet");
        }

        String finalWalletName = walletName != null ? walletName : "Primary Wallet";
        Wallet wallet = new Wallet(userId, finalWalletName, tronAddress, polygonAddress);
        
        return walletRepository.createWallet(wallet);
    }

    public Optional<Wallet> findWalletByUserId(Long userId) {
        return walletRepository.findWalletByUserId(userId);
    }

    public Optional<Wallet> findWalletById(Long walletId) {
        return walletRepository.findWalletById(walletId);
    }

    @Transactional
    public Wallet updateWalletAddresses(Long userId, String tronAddress, String polygonAddress) {
        Optional<Wallet> walletOpt = findWalletByUserId(userId);
        if (walletOpt.isEmpty()) {
            throw new RuntimeException("User wallet not found");
        }
        
        Wallet wallet = walletOpt.get();
        wallet.setTronAddress(tronAddress);
        wallet.setPolygonAddress(polygonAddress);
        
        return walletRepository.updateWallet(wallet);
    }

    @Transactional
    public Wallet updateWalletAddress(Long userId, String network, String address) {
        Optional<Wallet> walletOpt = findWalletByUserId(userId);
        if (walletOpt.isEmpty()) {
            throw new RuntimeException("User wallet not found");
        }
        
        Wallet wallet = walletOpt.get();
        wallet.setAddressByNetwork(network, address);
        
        return walletRepository.updateWallet(wallet);
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAllWallets();
    }

    // LEGACY WALLET MODEL METHODS (for backward compatibility)
    
    @Transactional
    public UsdtWallet createWallet(Long userId, String walletAddress, String walletName, 
                                  UsdtWallet.WalletType walletType) {
        
        // Check if wallet address already exists
        if (walletRepository.existsByAddress(walletAddress)) {
            throw new RuntimeException("Wallet with address " + walletAddress + " already exists");
        }

        // Set default values for nullable fields
        String finalWalletName = walletName != null ? walletName : "Auto-created Wallet";
        UsdtWallet.WalletType finalWalletType = walletType != null ? walletType : UsdtWallet.WalletType.MAIN;

        UsdtWallet wallet = new UsdtWallet(userId, walletAddress, finalWalletName, finalWalletType);
        
        // Ensure default values are set
        if (wallet.getBalance() == null) {
            wallet.setBalance(BigDecimal.ZERO);
        }
        if (wallet.getIsActive() == null) {
            wallet.setIsActive(true);
        }
        if (wallet.getIsVerified() == null) {
            wallet.setIsVerified(false);
        }
        
        return walletRepository.create(wallet);
    }

    public Optional<UsdtWallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    public List<UsdtWallet> findByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    public Optional<UsdtWallet> findByAddress(String walletAddress) {
        return walletRepository.findByAddress(walletAddress);
    }

    public List<UsdtWallet> findByType(UsdtWallet.WalletType walletType) {
        return walletRepository.findByType(walletType);
    }

    public List<UsdtWallet> findActive() {
        return walletRepository.findActive();
    }

    public List<UsdtWallet> findAll() {
        return walletRepository.findAll();
    }

    public List<UsdtWallet> findByQuery(WalletRepository.WalletQuery q) {
        return walletRepository.findByQuery(q);
    }

    public long countByQuery(WalletRepository.WalletQuery q) {
        return walletRepository.countByQuery(q);
    }

    public List<UsdtWallet> findVerified() {
        return walletRepository.findVerified();
    }

    @Transactional
    public UsdtWallet updateWallet(UsdtWallet wallet) {
        return walletRepository.update(wallet);
    }

    @Transactional
    public void deleteWallet(Long id) {
        walletRepository.delete(id);
    }

    @Transactional
    public void updateBalance(Long walletId, BigDecimal newBalance) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            UsdtWallet wallet = walletOpt.get();
            BigDecimal oldBalance = wallet.getBalance();
            BigDecimal difference = newBalance.subtract(oldBalance);
            
            walletRepository.updateBalance(walletId, newBalance);
            
            // Record the transaction if there's a balance change
            if (difference.compareTo(BigDecimal.ZERO) != 0) {
                String description = String.format("Balance updated from %s to %s USDT", oldBalance, newBalance);
                Transaction.TransactionType transactionType = difference.compareTo(BigDecimal.ZERO) > 0 ? 
                    Transaction.TransactionType.DEPOSIT : Transaction.TransactionType.WITHDRAWAL;
                
                Transaction transaction = transactionService.createTransaction(
                    wallet.getUserId(), walletId,
                    transactionType, difference.abs(), description
                );
                transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
                transactionService.updateTransaction(transaction);
            }
        } else {
            throw new RuntimeException("Wallet not found with ID: " + walletId);
        }
    }

    @Transactional
    public void addBalance(Long walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            UsdtWallet wallet = walletOpt.get();
            walletRepository.addBalance(walletId, amount);
            
            // Record the transaction
            String description = String.format("Balance added to wallet %d", walletId);
            Transaction transaction = transactionService.createTransaction(
                wallet.getUserId(), walletId,
                Transaction.TransactionType.DEPOSIT, amount, description
            );
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionService.updateTransaction(transaction);
        } else {
            throw new RuntimeException("Wallet not found with ID: " + walletId);
        }
    }

    @Transactional
    public void subtractBalance(Long walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            UsdtWallet wallet = walletOpt.get();
            if (!wallet.hasSufficientBalance(amount)) {
                throw new RuntimeException("Insufficient balance");
            }
            walletRepository.subtractBalance(walletId, amount);
            
            // Record the transaction
            String description = String.format("Balance withdrawn from wallet %d", walletId);
            Transaction transaction = transactionService.createTransaction(
                wallet.getUserId(), walletId,
                Transaction.TransactionType.WITHDRAWAL, amount, description
            );
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionService.updateTransaction(transaction);
        } else {
            throw new RuntimeException("Wallet not found with ID: " + walletId);
        }
    }

    @Transactional
    public void verifyWallet(Long walletId) {
        walletRepository.verifyWallet(walletId);
    }

    @Transactional
    public void deactivateWallet(Long walletId) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            UsdtWallet wallet = walletOpt.get();
            wallet.setIsActive(false);
            walletRepository.update(wallet);
        } else {
            throw new RuntimeException("Wallet not found with ID: " + walletId);
        }
    }

    @Transactional
    public void activateWallet(Long walletId) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            UsdtWallet wallet = walletOpt.get();
            wallet.setIsActive(true);
            walletRepository.update(wallet);
        } else {
            throw new RuntimeException("Wallet not found with ID: " + walletId);
        }
    }

    public boolean hasSufficientBalance(Long walletId, BigDecimal amount) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        return walletOpt.map(wallet -> wallet.hasSufficientBalance(amount)).orElse(false);
    }

    public BigDecimal getWalletBalance(Long walletId) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        return walletOpt.map(UsdtWallet::getBalance).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTotalBalanceByUserId(Long userId) {
        return walletRepository.getTotalBalanceByUserId(userId);
    }

    public boolean isWalletActive(Long walletId) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        return walletOpt.map(UsdtWallet::getIsActive).orElse(false);
    }

    public boolean isWalletVerified(Long walletId) {
        Optional<UsdtWallet> walletOpt = walletRepository.findById(walletId);
        return walletOpt.map(UsdtWallet::getIsVerified).orElse(false);
    }

    public boolean canCreateWallet(Long userId) {
        long walletCount = walletRepository.countByUserId(userId);
        return walletCount < 5;
    }

    public long getWalletCountByUserId(Long userId) {
        return walletRepository.countByUserId(userId);
    }

    public List<UsdtWallet> getMainWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId).stream()
                .filter(wallet -> wallet.getWalletType() == UsdtWallet.WalletType.MAIN)
                .toList();
    }

    public List<UsdtWallet> getTradingWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId).stream()
                .filter(wallet -> wallet.getWalletType() == UsdtWallet.WalletType.TRADING)
                .toList();
    }

    public List<UsdtWallet> getStakingWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId).stream()
                .filter(wallet -> wallet.getWalletType() == UsdtWallet.WalletType.STAKING)
                .toList();
    }

    public List<UsdtWallet> getRewardWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId).stream()
                .filter(wallet -> wallet.getWalletType() == UsdtWallet.WalletType.REWARDS)
                .toList();
    }

    public UsdtWallet getOrCreateMainWallet(Long userId) {
        List<UsdtWallet> mainWallets = getMainWalletsByUserId(userId);
        if (!mainWallets.isEmpty()) {
            return mainWallets.get(0);
        }
        
        // Create a default main wallet
        String defaultAddress = "MAIN_" + userId + "_" + System.currentTimeMillis();
        return createWallet(userId, defaultAddress, "Main Wallet", UsdtWallet.WalletType.MAIN);
    }

    @Transactional
    public boolean transferBetweenWallets(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        Optional<UsdtWallet> fromWalletOpt = walletRepository.findById(fromWalletId);
        Optional<UsdtWallet> toWalletOpt = walletRepository.findById(toWalletId);

        if (fromWalletOpt.isEmpty() || toWalletOpt.isEmpty()) {
            throw new RuntimeException("One or both wallets not found");
        }

        UsdtWallet fromWallet = fromWalletOpt.get();
        UsdtWallet toWallet = toWalletOpt.get();

        if (!fromWallet.hasSufficientBalance(amount)) {
            throw new RuntimeException("Insufficient balance in source wallet");
        }

        if (!fromWallet.getIsActive() || !toWallet.getIsActive()) {
            throw new RuntimeException("One or both wallets are inactive");
        }

        // Perform the transfer
        walletRepository.subtractBalance(fromWalletId, amount);
        walletRepository.addBalance(toWalletId, amount);

        // Record the transaction
        String description = String.format("Transfer from wallet %d to wallet %d", fromWalletId, toWalletId);
        transactionService.createTransferTransaction(
            fromWallet.getUserId(), fromWalletId,
            toWallet.getUserId(), toWalletId,
            amount, description
        );

        return true;
    }

    public long getTotalWalletCount() {
        return walletRepository.findActive().size();
    }

    public long getVerifiedWalletCount() {
        return walletRepository.findVerified().size();
    }

    public BigDecimal getTotalSystemBalance() {
        return walletRepository.findActive().stream()
                .map(UsdtWallet::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getAverageWalletBalance() {
        List<UsdtWallet> activeWallets = walletRepository.findActive();
        if (activeWallets.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalBalance = activeWallets.stream()
                .map(UsdtWallet::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalBalance.divide(BigDecimal.valueOf(activeWallets.size()), 8, BigDecimal.ROUND_HALF_UP);
    }
}
