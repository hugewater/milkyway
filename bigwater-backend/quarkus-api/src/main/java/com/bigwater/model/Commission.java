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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "commissions")
public class Commission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Affiliate recipient;
    
    @ManyToOne
    @JoinColumn(name = "source_affiliate_id", nullable = false)
    private Affiliate sourceAffiliate;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "commission_type", nullable = false)
    private CommissionType commissionType;
    
    @Column(name = "generation_level")
    private Integer generationLevel;
    
    @Column(name = "percentage_rate", precision = 5, scale = 4)
    private BigDecimal percentageRate;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "transaction_amount", precision = 10, scale = 2)
    private BigDecimal transactionAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionStatus status = CommissionStatus.PENDING;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    // Enums
    public enum CommissionType {
        GENERATION, LEADERSHIP, DIRECT_REFERRAL
    }
    
    public enum CommissionStatus {
        PENDING, CONFIRMED, PAID, CANCELLED
    }
    
    // Constructors
    public Commission() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Commission(Affiliate recipient, Affiliate sourceAffiliate, BigDecimal amount, 
                     CommissionType commissionType, String transactionId, BigDecimal transactionAmount) {
        this();
        this.recipient = recipient;
        this.sourceAffiliate = sourceAffiliate;
        this.amount = amount;
        this.commissionType = commissionType;
        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Affiliate getRecipient() { return recipient; }
    public void setRecipient(Affiliate recipient) { this.recipient = recipient; }
    
    public Affiliate getSourceAffiliate() { return sourceAffiliate; }
    public void setSourceAffiliate(Affiliate sourceAffiliate) { this.sourceAffiliate = sourceAffiliate; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public CommissionType getCommissionType() { return commissionType; }
    public void setCommissionType(CommissionType commissionType) { this.commissionType = commissionType; }
    
    public Integer getGenerationLevel() { return generationLevel; }
    public void setGenerationLevel(Integer generationLevel) { this.generationLevel = generationLevel; }
    
    public BigDecimal getPercentageRate() { return percentageRate; }
    public void setPercentageRate(BigDecimal percentageRate) { this.percentageRate = percentageRate; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public BigDecimal getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(BigDecimal transactionAmount) { this.transactionAmount = transactionAmount; }
    
    public CommissionStatus getStatus() { return status; }
    public void setStatus(CommissionStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    
    // Business Methods
    public boolean isPaid() {
        return status == CommissionStatus.PAID;
    }
    
    public boolean isPending() {
        return status == CommissionStatus.PENDING;
    }
    
    public void markAsConfirmed() {
        this.status = CommissionStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }
    
    public void markAsPaid() {
        this.status = CommissionStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }
    
    public void cancel() {
        this.status = CommissionStatus.CANCELLED;
    }
}