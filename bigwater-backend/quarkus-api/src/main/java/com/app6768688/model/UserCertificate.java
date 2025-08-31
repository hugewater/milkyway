package com.app6768688.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserCertificate {
    private Long id;
    private Long userId;
    private Long certificateId;
    private LocalDateTime purchaseDate;
    private LocalDateTime expiryDate;
    private CertificateStatus status;
    private BigDecimal purchaseAmountUsdt;
    private String transactionHash;
    private Long walletId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public UserCertificate() {}

    public UserCertificate(Long userId, Long certificateId, BigDecimal purchaseAmountUsdt, 
                          LocalDateTime expiryDate, Long walletId) {
        this.userId = userId;
        this.certificateId = certificateId;
        this.purchaseAmountUsdt = purchaseAmountUsdt;
        this.expiryDate = expiryDate;
        this.walletId = walletId;
        this.status = CertificateStatus.ACTIVE;
        this.purchaseDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCertificateId() { return certificateId; }
    public void setCertificateId(Long certificateId) { this.certificateId = certificateId; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public CertificateStatus getStatus() { return status; }
    public void setStatus(CertificateStatus status) { this.status = status; }

    public BigDecimal getPurchaseAmountUsdt() { return purchaseAmountUsdt; }
    public void setPurchaseAmountUsdt(BigDecimal purchaseAmountUsdt) { this.purchaseAmountUsdt = purchaseAmountUsdt; }

    public String getTransactionHash() { return transactionHash; }
    public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }

    public Long getWalletId() { return walletId; }
    public void setWalletId(Long walletId) { this.walletId = walletId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isActive() {
        return status == CertificateStatus.ACTIVE;
    }

    public boolean isExpired() {
        return status == CertificateStatus.EXPIRED;
    }

    public boolean isCancelled() {
        return status == CertificateStatus.CANCELLED;
    }

    public boolean isExpiredByDate() {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon() {
        if (expiryDate == null) return false;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysFromNow = now.plusDays(30);
        return expiryDate.isBefore(thirtyDaysFromNow) && expiryDate.isAfter(now);
    }

    public long getDaysUntilExpiry() {
        if (expiryDate == null) return -1;
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiryDate)) return 0;
        return java.time.Duration.between(now, expiryDate).toDays();
    }

    public String getFormattedPurchaseAmount() {
        return purchaseAmountUsdt != null ? purchaseAmountUsdt.toString() + " USDT" : "0 USDT";
    }

    public String getFormattedStatus() {
        return status != null ? status.name() : "UNKNOWN";
    }

    public String getFormattedPurchaseDate() {
        return purchaseDate != null ? purchaseDate.toString() : "Not set";
    }

    public String getFormattedExpiryDate() {
        return expiryDate != null ? expiryDate.toString() : "Not set";
    }

    public boolean hasTransactionHash() {
        return transactionHash != null && !transactionHash.trim().isEmpty();
    }

    public boolean isRecentlyPurchased() {
        if (purchaseDate == null) return false;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        return purchaseDate.isAfter(sevenDaysAgo);
    }

    // Enums
    public enum CertificateStatus {
        ACTIVE, EXPIRED, CANCELLED
    }
}
