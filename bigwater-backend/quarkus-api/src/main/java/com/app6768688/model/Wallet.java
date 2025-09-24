package com.app6768688.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "wallet_name", nullable = false)
    private String walletName = "Primary Wallet";
    
    @Column(name = "tron_address", nullable = true)
    private String tronAddress;
    
    @Column(name = "polygon_address", nullable = true)
    private String polygonAddress;
    
    @Column(name = "balance", nullable = false, precision = 20, scale = 8)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Wallet() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Wallet(Long userId, String walletName) {
        this.userId = userId;
        this.walletName = walletName != null ? walletName : "Primary Wallet";
        this.balance = BigDecimal.ZERO;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Wallet(Long userId, String walletName, String tronAddress, String polygonAddress) {
        this(userId, walletName);
        this.tronAddress = tronAddress;
        this.polygonAddress = polygonAddress;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getWalletName() { return walletName; }
    public void setWalletName(String walletName) { this.walletName = walletName; }

    public String getTronAddress() { return tronAddress; }
    public void setTronAddress(String tronAddress) { this.tronAddress = tronAddress; }

    public String getPolygonAddress() { return polygonAddress; }
    public void setPolygonAddress(String polygonAddress) { this.polygonAddress = polygonAddress; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

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
        this.updatedAt = LocalDateTime.now();
    }

    public void subtractBalance(BigDecimal amount) {
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        return this.balance.compareTo(amount) >= 0;
    }

    public boolean hasAddresses() {
        return (tronAddress != null && !tronAddress.trim().isEmpty()) ||
               (polygonAddress != null && !polygonAddress.trim().isEmpty());
    }

    public boolean hasTronAddress() {
        return tronAddress != null && !tronAddress.trim().isEmpty();
    }

    public boolean hasPolygonAddress() {
        return polygonAddress != null && !polygonAddress.trim().isEmpty();
    }

    // Network-specific address getters for convenience
    public String getAddressByNetwork(String network) {
        if (network == null) return null;
        
        switch (network.toUpperCase()) {
            case "TRON":
            case "TRC20":
                return tronAddress;
            case "POLYGON":
            case "MATIC":
            case "ERC20":
                return polygonAddress;
            default:
                return null;
        }
    }

    public void setAddressByNetwork(String network, String address) {
        if (network == null) return;
        
        switch (network.toUpperCase()) {
            case "TRON":
            case "TRC20":
                this.tronAddress = address;
                break;
            case "POLYGON":
            case "MATIC":
            case "ERC20":
                this.polygonAddress = address;
                break;
        }
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", userId=" + userId +
                ", walletName='" + walletName + '\'' +
                ", tronAddress='" + tronAddress + '\'' +
                ", polygonAddress='" + polygonAddress + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
