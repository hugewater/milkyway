package com.bigwater.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "affiliate_transactions")
public class AffiliateTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;
    
    @ManyToOne
    @JoinColumn(name = "affiliate_id", nullable = false)
    private Affiliate affiliate;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 10)
    private String currency = "USDT";
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
    
    @Lob
    private String description;
    
    @Lob
    @Column(columnDefinition = "JSON")
    private String metadata;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;
    
    @Column(name = "commission_calculated_at")
    private LocalDateTime commissionCalculatedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;
    
    // Enums
    public enum TransactionType {
        PURCHASE, SUBSCRIPTION, CONSUMPTION, OTHER
    }
    
    public enum TransactionStatus {
        PENDING, CONFIRMED, COMMISSION_CALCULATED, COMMISSION_PAID
    }
    
    // Constructors
    public AffiliateTransaction() {
        this.createdAt = LocalDateTime.now();
    }
    
    public AffiliateTransaction(String transactionId, Affiliate affiliate, BigDecimal amount, 
                              TransactionType transactionType, String description) {
        this();
        this.transactionId = transactionId;
        this.affiliate = affiliate;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public Affiliate getAffiliate() { return affiliate; }
    public void setAffiliate(Affiliate affiliate) { this.affiliate = affiliate; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    
    public LocalDateTime getCommissionCalculatedAt() { return commissionCalculatedAt; }
    public void setCommissionCalculatedAt(LocalDateTime commissionCalculatedAt) { this.commissionCalculatedAt = commissionCalculatedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    
    // Business Methods
    public boolean isConfirmed() {
        return status == TransactionStatus.CONFIRMED || 
               status == TransactionStatus.COMMISSION_CALCULATED || 
               status == TransactionStatus.COMMISSION_PAID;
    }
    
    public boolean isCommissionCalculated() {
        return status == TransactionStatus.COMMISSION_CALCULATED || 
               status == TransactionStatus.COMMISSION_PAID;
    }
    
    public boolean isCommissionPaid() {
        return status == TransactionStatus.COMMISSION_PAID;
    }
    
    public void markAsConfirmed() {
        this.status = TransactionStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }
    
    public void markCommissionCalculated() {
        this.status = TransactionStatus.COMMISSION_CALCULATED;
        this.commissionCalculatedAt = LocalDateTime.now();
    }
    
    public void markCommissionPaid() {
        this.status = TransactionStatus.COMMISSION_PAID;
    }
    
    public boolean qualifiesForCommission() {
        return isConfirmed() && amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}