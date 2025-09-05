package com.app6768688.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "wallet_id", nullable = false)
    private Long walletId;
    
    @Column(name = "to_wallet_id", nullable = false)
    private Long toWalletId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
    
    @Column(name = "amount_usdt", nullable = false, precision = 20, scale = 8)
    private BigDecimal amountUsdt;
    
    @Column(name = "transaction_hash")
    private String transactionHash;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "related_entity_type")
    private String relatedEntityType;
    
    @Column(name = "related_entity_id")
    private Long relatedEntityId;
    
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Transaction() {}

    public Transaction(Long userId, TransactionType transactionType, BigDecimal amountUsdt, 
                      String description, Long walletId) {
        this.userId = userId;
        this.transactionType = transactionType;
        this.amountUsdt = amountUsdt;
        this.description = description;
        this.walletId = walletId;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Transaction(Long userId, TransactionType transactionType, BigDecimal amountUsdt, 
                      String description, Long walletId, Long toWalletId) {
        this.userId = userId;
        this.transactionType = transactionType;
        this.amountUsdt = amountUsdt;
        this.description = description;
        this.walletId = walletId;
        this.toWalletId = toWalletId;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getWalletId() { return walletId; }
    public void setWalletId(Long walletId) { this.walletId = walletId; }

    public Long getToWalletId() { return toWalletId; }
    public void setToWalletId(Long toWalletId) { this.toWalletId = toWalletId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmountUsdt() { return amountUsdt; }
    public void setAmountUsdt(BigDecimal amountUsdt) { this.amountUsdt = amountUsdt; }

    public String getTransactionHash() { return transactionHash; }
    public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }

    public Long getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(Long relatedEntityId) { this.relatedEntityId = relatedEntityId; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }

    public boolean isCompleted() {
        return status == TransactionStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == TransactionStatus.FAILED;
    }

    public boolean isCancelled() {
        return status == TransactionStatus.CANCELLED;
    }

    public boolean isDeposit() {
        return transactionType == TransactionType.DEPOSIT;
    }

    public boolean isWithdrawal() {
        return transactionType == TransactionType.WITHDRAWAL;
    }

    public boolean isPurchase() {
        return transactionType == TransactionType.PURCHASE;
    }

    public boolean isReward() {
        return transactionType == TransactionType.REWARD;
    }

    public boolean isCommission() {
        return transactionType == TransactionType.COMMISSION;
    }

    public String getFormattedAmount() {
        return amountUsdt != null ? amountUsdt.toString() + " USDT" : "0 USDT";
    }

    public String getFormattedStatus() {
        return status != null ? status.name() : "UNKNOWN";
    }

    public String getFormattedType() {
        return transactionType != null ? transactionType.name() : "UNKNOWN";
    }

    public boolean hasTransactionHash() {
        return transactionHash != null && !transactionHash.trim().isEmpty();
    }

    public boolean hasRelatedEntity() {
        return relatedEntityType != null && relatedEntityId != null;
    }

    // Enums
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, PURCHASE, REFUND, REWARD, COMMISSION
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED
    }
}
