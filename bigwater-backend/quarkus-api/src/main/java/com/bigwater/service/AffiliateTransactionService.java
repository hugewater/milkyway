package com.bigwater.service;

import java.math.BigDecimal;
import java.util.logging.Logger;

import com.bigwater.model.Affiliate;
import com.bigwater.model.AffiliateTransaction;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AffiliateTransactionService {
    
    private static final Logger logger = Logger.getLogger(AffiliateTransactionService.class.getName());
    
    @Inject
    EntityManager em;
    
    @Inject
    CommissionCalculationService commissionService;
    
    @Inject
    AffiliatePromotionService promotionService;
    
    /**
     * Record a new affiliate transaction and trigger commission calculations
     */
    @Transactional
    public AffiliateTransaction recordTransaction(Long affiliateId, BigDecimal amount, 
                                                String transactionId, AffiliateTransaction.TransactionType type, 
                                                String description) {
        
        Affiliate affiliate = em.find(Affiliate.class, affiliateId);
        if (affiliate == null) {
            throw new IllegalArgumentException("Affiliate not found: " + affiliateId);
        }
        
        // Create transaction record
        AffiliateTransaction transaction = new AffiliateTransaction(
            transactionId, affiliate, amount, type, description);
        
        em.persist(transaction);
        
        logger.info(String.format("Transaction recorded: %s for affiliate %s, amount: %s", 
            transactionId, affiliate.getEmail(), amount));
        
        return transaction;
    }
    
    /**
     * Confirm a transaction and trigger commission calculations
     */
    @Transactional
    public void confirmTransaction(String transactionId) {
        AffiliateTransaction transaction = findByTransactionId(transactionId);
        if (transaction == null) {
            logger.warning("Transaction not found for confirmation: " + transactionId);
            return;
        }
        
        if (transaction.isConfirmed()) {
            logger.warning("Transaction already confirmed: " + transactionId);
            return;
        }
        
        // Mark transaction as confirmed
        transaction.markAsConfirmed();
        em.merge(transaction);
        
        // Update affiliate consumption
        updateAffiliateConsumption(transaction);
        
        // Calculate and process commissions
        if (transaction.qualifiesForCommission()) {
            processCommissionsForTransaction(transaction);
        }
        
        // Check for promotions
        checkForPromotion(transaction.getAffiliate());
        
        logger.info(String.format("Transaction confirmed and processed: %s", transactionId));
    }
    
    /**
     * Process commissions for a confirmed transaction
     */
    private void processCommissionsForTransaction(AffiliateTransaction transaction) {
        try {
            commissionService.processCommissionPayments(
                transaction.getAffiliate().getId(),
                transaction.getAmount(),
                transaction.getTransactionId()
            );
            
            transaction.markCommissionCalculated();
            em.merge(transaction);
            
        } catch (Exception e) {
            logger.severe("Failed to process commissions for transaction " + 
                transaction.getTransactionId() + ": " + e.getMessage());
        }
    }
    
    /**
     * Update affiliate's total consumption
     */
    private void updateAffiliateConsumption(AffiliateTransaction transaction) {
        Affiliate affiliate = transaction.getAffiliate();
        Double currentConsumption = affiliate.getTotalConsumption();
        Double newConsumption = currentConsumption + transaction.getAmount().doubleValue();
        
        affiliate.setTotalConsumption(newConsumption);
        em.merge(affiliate);
        
        logger.info(String.format("Updated consumption for %s: %s -> %s", 
            affiliate.getEmail(), currentConsumption, newConsumption));
    }
    
    /**
     * Check if affiliate qualifies for promotion after transaction
     */
    private void checkForPromotion(Affiliate affiliate) {
        try {
            boolean promoted = promotionService.checkAndPromoteAffiliate(affiliate);
            if (promoted) {
                logger.info(String.format("Affiliate %s was promoted after transaction", affiliate.getEmail()));
            }
        } catch (Exception e) {
            logger.severe("Failed to check promotion for affiliate " + 
                affiliate.getEmail() + ": " + e.getMessage());
        }
    }
    
    /**
     * Find transaction by transaction ID
     */
    public AffiliateTransaction findByTransactionId(String transactionId) {
        return em.createQuery(
            "SELECT t FROM AffiliateTransaction t WHERE t.transactionId = :transactionId", 
            AffiliateTransaction.class)
            .setParameter("transactionId", transactionId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Mark commissions as paid for a transaction
     */
    @Transactional
    public void markCommissionsPaid(String transactionId) {
        AffiliateTransaction transaction = findByTransactionId(transactionId);
        if (transaction != null && transaction.isCommissionCalculated()) {
            transaction.markCommissionPaid();
            em.merge(transaction);
            
            logger.info("Commissions marked as paid for transaction: " + transactionId);
        }
    }
    
    /**
     * Integration method for wallet payments
     * Call this when a USDT payment is confirmed on blockchain
     */
    @Transactional
    public void handleWalletPayment(String walletAddress, BigDecimal amount, String txHash) {
        // This would integrate with your existing wallet/payment system
        // For now, create a generic consumption transaction
        
        // TODO: Find affiliate by wallet address
        // For demonstration, using a placeholder
        logger.info(String.format("Wallet payment received: %s USDT from %s, tx: %s", 
            amount, walletAddress, txHash));
        
        // You would implement:
        // 1. Find affiliate by wallet address
        // 2. Record transaction with CONSUMPTION type
        // 3. Confirm transaction to trigger commissions
        
        /*
        Affiliate affiliate = findAffiliateByWalletAddress(walletAddress);
        if (affiliate != null) {
            AffiliateTransaction transaction = recordTransaction(
                affiliate.getId(), 
                amount, 
                txHash, 
                AffiliateTransaction.TransactionType.CONSUMPTION,
                "Wallet payment from " + walletAddress
            );
            confirmTransaction(txHash);
        }
        */
    }
}