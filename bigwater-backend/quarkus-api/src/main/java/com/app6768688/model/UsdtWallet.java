package com.app6768688.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usdt_wallets")
public class UsdtWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "wallet_address", nullable = false, unique = true)
    private String walletAddress;
    
    @Column(name = "wallet_name", nullable = true)
    private String walletName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type", nullable = true)
    private WalletType walletType;
    
    @Column(name = "balance", nullable = true, precision = 20, scale = 8)
    private BigDecimal balance;
    
    @Column(name = "is_active", nullable = true)
    private Boolean isActive;
    
    @Column(name = "is_verified", nullable = true)
    private Boolean isVerified;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public UsdtWallet() {}

    public UsdtWallet(Long userId, String walletAddress, String walletName, WalletType walletType) {
        this.userId = userId;
        this.walletAddress = walletAddress;
        this.walletName = walletName;
        this.walletType = walletType;
        this.balance = BigDecimal.ZERO;
        this.isActive = true;
        this.isVerified = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getWalletAddress() { return walletAddress; }
    public void setWalletAddress(String walletAddress) { this.walletAddress = walletAddress; }

    public String getWalletName() { return walletName; }
    public void setWalletName(String walletName) { this.walletName = walletName; }

    public WalletType getWalletType() { return walletType; }
    public void setWalletType(WalletType walletType) { this.walletType = walletType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public void addBalance(BigDecimal amount) {
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        this.balance = this.balance.add(amount);
    }

    public void subtractBalance(BigDecimal amount) {
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        this.balance = this.balance.subtract(amount);
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        return this.balance.compareTo(amount) >= 0;
    }

    // Enums
    public enum WalletType {
        // New types
        COMPANY, MEMBER, TESTING,
        // Backward-compatible legacy values (kept to avoid breaking existing rows)
        MAIN, TRADING, STAKING, REWARDS
    }
}
