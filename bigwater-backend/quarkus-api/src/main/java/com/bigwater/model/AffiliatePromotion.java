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
@Table(name = "affiliate_promotions")
public class AffiliatePromotion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "affiliate_id", nullable = false)
    private Affiliate affiliate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "from_level")
    private AffiliateLevel fromLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "to_level", nullable = false)
    private AffiliateLevel toLevel;
    
    @Column(name = "direct_referrals_count")
    private Integer directReferralsCount;
    
    @Column(name = "total_downlines_count")
    private Integer totalDownlinesCount;
    
    @Column(name = "total_consumption", precision = 10, scale = 2)
    private BigDecimal totalConsumption;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false)
    private PromotionType promotionType = PromotionType.AUTOMATIC;
    
    @Column(name = "triggered_by_user_id")
    private Long triggeredByUserId;
    
    @Lob
    private String notes;
    
    @Column(name = "became_president_independent")
    private Boolean becamePresidentIndependent = false;
    
    @Column(name = "affected_uplines_count")
    private Integer affectedUplinesCount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Enum
    public enum PromotionType {
        AUTOMATIC, MANUAL, ADMIN_OVERRIDE
    }
    
    // Constructors
    public AffiliatePromotion() {
        this.createdAt = LocalDateTime.now();
    }
    
    public AffiliatePromotion(Affiliate affiliate, AffiliateLevel fromLevel, AffiliateLevel toLevel, 
                             PromotionType promotionType) {
        this();
        this.affiliate = affiliate;
        this.fromLevel = fromLevel;
        this.toLevel = toLevel;
        this.promotionType = promotionType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Affiliate getAffiliate() { return affiliate; }
    public void setAffiliate(Affiliate affiliate) { this.affiliate = affiliate; }
    
    public AffiliateLevel getFromLevel() { return fromLevel; }
    public void setFromLevel(AffiliateLevel fromLevel) { this.fromLevel = fromLevel; }
    
    public AffiliateLevel getToLevel() { return toLevel; }
    public void setToLevel(AffiliateLevel toLevel) { this.toLevel = toLevel; }
    
    public Integer getDirectReferralsCount() { return directReferralsCount; }
    public void setDirectReferralsCount(Integer directReferralsCount) { this.directReferralsCount = directReferralsCount; }
    
    public Integer getTotalDownlinesCount() { return totalDownlinesCount; }
    public void setTotalDownlinesCount(Integer totalDownlinesCount) { this.totalDownlinesCount = totalDownlinesCount; }
    
    public BigDecimal getTotalConsumption() { return totalConsumption; }
    public void setTotalConsumption(BigDecimal totalConsumption) { this.totalConsumption = totalConsumption; }
    
    public PromotionType getPromotionType() { return promotionType; }
    public void setPromotionType(PromotionType promotionType) { this.promotionType = promotionType; }
    
    public Long getTriggeredByUserId() { return triggeredByUserId; }
    public void setTriggeredByUserId(Long triggeredByUserId) { this.triggeredByUserId = triggeredByUserId; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Boolean getBecamePresidentIndependent() { return becamePresidentIndependent; }
    public void setBecamePresidentIndependent(Boolean becamePresidentIndependent) { this.becamePresidentIndependent = becamePresidentIndependent; }
    
    public Integer getAffectedUplinesCount() { return affectedUplinesCount; }
    public void setAffectedUplinesCount(Integer affectedUplinesCount) { this.affectedUplinesCount = affectedUplinesCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Business Methods
    public boolean isPresidentPromotion() {
        return toLevel == AffiliateLevel.PRESIDENT;
    }
    
    public boolean isAutomaticPromotion() {
        return promotionType == PromotionType.AUTOMATIC;
    }
    
    public boolean isManualPromotion() {
        return promotionType == PromotionType.MANUAL || promotionType == PromotionType.ADMIN_OVERRIDE;
    }
    
    public String getPromotionDescription() {
        if (fromLevel == null) {
            return String.format("Initial promotion to %s", toLevel.name());
        }
        return String.format("Promoted from %s to %s", fromLevel.name(), toLevel.name());
    }
}