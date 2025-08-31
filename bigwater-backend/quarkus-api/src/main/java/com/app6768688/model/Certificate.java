package com.app6768688.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Certificate {
    private Long id;
    private String certificateName;
    private String description;
    private BigDecimal priceUsdt;
    private Integer durationDays;
    private String benefits;
    private Boolean isActive;
    private Integer maxSupply;
    private Integer currentSupply;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Certificate() {}

    public Certificate(String certificateName, String description, 
                      BigDecimal priceUsdt, Integer durationDays, Long createdBy) {
        this.certificateName = certificateName;
        this.description = description;
        this.priceUsdt = priceUsdt;
        this.durationDays = durationDays;
        this.createdBy = createdBy;
        this.isActive = true;
        this.currentSupply = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCertificateName() { return certificateName; }
    public void setCertificateName(String certificateName) { this.certificateName = certificateName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPriceUsdt() { return priceUsdt; }
    public void setPriceUsdt(BigDecimal priceUsdt) { this.priceUsdt = priceUsdt; }

    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getMaxSupply() { return maxSupply; }
    public void setMaxSupply(Integer maxSupply) { this.maxSupply = maxSupply; }

    public Integer getCurrentSupply() { return currentSupply; }
    public void setCurrentSupply(Integer currentSupply) { this.currentSupply = currentSupply; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isAvailable() {
        return isActive && (maxSupply == null || currentSupply < maxSupply);
    }

    public boolean isUnlimited() {
        return maxSupply == null;
    }

    public int getRemainingSupply() {
        if (maxSupply == null) return Integer.MAX_VALUE;
        return Math.max(0, maxSupply - currentSupply);
    }

    public void incrementSupply() {
        if (maxSupply != null && currentSupply >= maxSupply) {
            throw new RuntimeException("Certificate supply limit reached");
        }
        this.currentSupply++;
    }

    public List<String> getBenefitsList() {
        if (benefits == null || benefits.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(benefits.split(","));
    }

    public String getFormattedPrice() {
        return priceUsdt != null ? priceUsdt.toString() + " USDT" : "0 USDT";
    }

    public String getFormattedDuration() {
        if (durationDays == null) return "Unknown";
        if (durationDays == 365) return "1 Year";
        if (durationDays == 730) return "2 Years";
        if (durationDays == 1095) return "3 Years";
        return durationDays + " Days";
    }
}
