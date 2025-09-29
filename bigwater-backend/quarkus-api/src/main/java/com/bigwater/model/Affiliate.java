package com.bigwater.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "affiliates")
public class Affiliate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "affiliate_level")
    private AffiliateLevel level = AffiliateLevel.FAN;
    
    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private Affiliate referrer;
    
    @OneToMany(mappedBy = "referrer", fetch = FetchType.LAZY)
    private List<Affiliate> directReferrals = new ArrayList<>();
    
    @Column(name = "total_consumption")
    private Double totalConsumption = 0.0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_promotion_check")
    private LocalDateTime lastPromotionCheck;
    
    @Column(name = "is_president_independent")
    private Boolean isPresidentIndependent = false;
    
    // Constructors
    public Affiliate() {}
    
    public Affiliate(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = LocalDateTime.now();
        this.lastPromotionCheck = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public AffiliateLevel getLevel() { return level; }
    public void setLevel(AffiliateLevel level) { this.level = level; }
    
    public Affiliate getReferrer() { return referrer; }
    public void setReferrer(Affiliate referrer) { this.referrer = referrer; }
    
    public List<Affiliate> getDirectReferrals() { return directReferrals; }
    public void setDirectReferrals(List<Affiliate> directReferrals) { this.directReferrals = directReferrals; }
    
    public Double getTotalConsumption() { return totalConsumption; }
    public void setTotalConsumption(Double totalConsumption) { this.totalConsumption = totalConsumption; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastPromotionCheck() { return lastPromotionCheck; }
    public void setLastPromotionCheck(LocalDateTime lastPromotionCheck) { this.lastPromotionCheck = lastPromotionCheck; }
    
    public Boolean getIsPresidentIndependent() { return isPresidentIndependent; }
    public void setIsPresidentIndependent(Boolean isPresidentIndependent) { this.isPresidentIndependent = isPresidentIndependent; }
    
    // Business Methods
    public int getDirectReferralCount() {
        return directReferrals != null ? directReferrals.size() : 0;
    }
    
    public boolean isPresident() {
        return level == AffiliateLevel.PRESIDENT;
    }
}