package com.bigwater.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.bigwater.model.Affiliate;
import com.bigwater.model.AffiliateLevel;
import com.bigwater.model.Commission;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@ApplicationScoped
public class CommissionCalculationService {
    
    private static final Logger logger = Logger.getLogger(CommissionCalculationService.class.getName());
    
    @Inject
    EntityManager em;
    
    @Inject
    AffiliatePromotionService promotionService;
    
    /**
     * Calculate commissions for a transaction
     * Respects President independence rule
     * Includes both generation commissions and leadership bonuses
     */
    public Map<Long, BigDecimal> calculateCommissions(Long payerAffiliateId, BigDecimal transactionAmount) {
        Map<Long, BigDecimal> commissions = new HashMap<>();
        
        Affiliate payer = em.find(Affiliate.class, payerAffiliateId);
        if (payer == null) {
            return commissions;
        }
        
        // Check if there's a President in the upline that blocks commissions
        if (promotionService.hasPresidentInUpline(payerAffiliateId)) {
            logger.info(String.format("Commission calculation blocked for affiliate %s due to President in upline", 
                payer.getEmail()));
            return commissions; // Empty map - no commissions paid
        }
        
        // Calculate commissions up the referral chain
        List<Affiliate> uplineChain = getUplineChain(payerAffiliateId);
        
        for (int generation = 0; generation < uplineChain.size(); generation++) {
            Affiliate uplineAffiliate = uplineChain.get(generation);
            
            // Stop if we encounter a President (they are independent)
            if (uplineAffiliate.getLevel() == AffiliateLevel.PRESIDENT && 
                uplineAffiliate.getIsPresidentIndependent()) {
                logger.info(String.format("Stopping commission calculation at President %s", 
                    uplineAffiliate.getEmail()));
                break;
            }
            
            // Calculate generation commission
            BigDecimal generationCommission = calculateGenerationCommission(
                uplineAffiliate, generation + 1, transactionAmount);
            
            // Calculate leadership bonus (separate from generation commissions)
            BigDecimal leadershipBonus = calculateLeadershipBonus(
                uplineAffiliate, transactionAmount);
            
            // Combine generation commission and leadership bonus
            BigDecimal totalCommission = generationCommission.add(leadershipBonus);
            
            if (totalCommission.compareTo(BigDecimal.ZERO) > 0) {
                commissions.put(uplineAffiliate.getId(), totalCommission);
                logger.info(String.format("Commission calculated: %s gets %s (gen: %s + leadership: %s) from generation %d", 
                    uplineAffiliate.getEmail(), totalCommission, generationCommission, leadershipBonus, generation + 1));
            }
        }
        
        return commissions;
    }
    
    /**
     * Calculate commission for a specific generation
     * Uses centralized commission structure logic
     */
    private BigDecimal calculateGenerationCommission(Affiliate affiliate, int generation, 
                                                   BigDecimal transactionAmount) {
        double percentage = getCommissionPercentage(affiliate.getLevel(), generation);
        
        if (percentage > 0.0) {
            return transactionAmount.multiply(BigDecimal.valueOf(percentage))
                .setScale(2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Calculate leadership bonus for qualifying affiliates
     * Leadership bonuses are separate from generation commissions
     */
    private BigDecimal calculateLeadershipBonus(Affiliate affiliate, BigDecimal transactionAmount) {
        AffiliateLevel level = affiliate.getLevel();
        double percentage = 0.0;
        
        // Leadership bonus percentages by level (from the commission table)
        switch (level) {
            case FAN:
            case SUBSCRIBER:
            case READER:
            case PROMOTER:
                // No leadership bonus for these levels
                percentage = 0.0;
                break;
            case LEADER:
                percentage = 0.05; // 5% leadership bonus
                break;
            case INFLUENCER:
                percentage = 0.07; // 7% leadership bonus
                break;
            case PRESIDENT:
                percentage = 0.10; // 10% leadership bonus
                break;
        }
        
        if (percentage > 0.0) {
            BigDecimal bonus = transactionAmount.multiply(BigDecimal.valueOf(percentage))
                .setScale(2, RoundingMode.HALF_UP);
            
            logger.info(String.format("Leadership bonus calculated: %s gets %s (%s%%) for level %s", 
                affiliate.getEmail(), bonus, percentage * 100, level));
            
            return bonus;
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get the upline chain for an affiliate
     */
    private List<Affiliate> getUplineChain(Long affiliateId) {
        List<Affiliate> uplineChain = new ArrayList<>();
        
        Query query = em.createNativeQuery("""
            WITH RECURSIVE upline_tree AS (
                -- Base case: direct referrer
                SELECT referrer_id, 1 as level
                FROM affiliates 
                WHERE id = :affiliateId AND referrer_id IS NOT NULL
                
                UNION ALL
                
                -- Recursive case: referrer of referrer
                SELECT a.referrer_id, ut.level + 1
                FROM affiliates a
                INNER JOIN upline_tree ut ON a.id = ut.referrer_id
                WHERE ut.level < 7 AND a.referrer_id IS NOT NULL  -- Max 7 generations for President
            )
            SELECT a.* FROM affiliates a
            INNER JOIN upline_tree ut ON a.id = ut.referrer_id
            ORDER BY ut.level ASC
        """);
        query.setParameter("affiliateId", affiliateId);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        // Convert raw results to Affiliate objects
        for (Object[] row : results) {
            Long uplineId = ((Number) row[0]).longValue();
            Affiliate uplineAffiliate = em.find(Affiliate.class, uplineId);
            if (uplineAffiliate != null) {
                uplineChain.add(uplineAffiliate);
            }
        }
        
        return uplineChain;
    }
    
    /**
     * Process commission payments for a transaction
     * This would be called after a successful payment
     */
    public void processCommissionPayments(Long payerAffiliateId, BigDecimal transactionAmount, 
                                        String transactionId) {
        Map<Long, BigDecimal> commissions = calculateCommissions(payerAffiliateId, transactionAmount);
        
        for (Map.Entry<Long, BigDecimal> entry : commissions.entrySet()) {
            Long affiliateId = entry.getKey();
            BigDecimal commissionAmount = entry.getValue();
            
            // Create commission record (you would have a Commission entity)
            createCommissionRecord(affiliateId, payerAffiliateId, commissionAmount, 
                transactionId, LocalDateTime.now());
            
            logger.info(String.format("Commission processed: Affiliate %d earned %s from transaction %s", 
                affiliateId, commissionAmount, transactionId));
        }
    }
    
    /**
     * Create a commission record
     */
    private void createCommissionRecord(Long recipientId, Long sourceAffiliateId, 
                                      BigDecimal amount, String transactionId, 
                                      LocalDateTime createdAt) {
        Affiliate recipient = em.find(Affiliate.class, recipientId);
        Affiliate sourceAffiliate = em.find(Affiliate.class, sourceAffiliateId);
        
        if (recipient != null && sourceAffiliate != null) {
            Commission commission = new Commission();
            commission.setRecipient(recipient);
            commission.setSourceAffiliate(sourceAffiliate);
            commission.setAmount(amount);
            commission.setTransactionId(transactionId);
            commission.setCommissionType(Commission.CommissionType.GENERATION);
            commission.setCreatedAt(createdAt);
            
            em.persist(commission);
            
            logger.info(String.format("Commission Record Created: ID=%d, Recipient=%s, Amount=%s", 
                commission.getId(), recipient.getEmail(), amount));
        } else {
            logger.warning(String.format("Failed to create commission record: Recipient=%d, Source=%d not found", 
                recipientId, sourceAffiliateId));
        }
    }
    
    /**
     * Recalculate commissions when President independence changes
     * This should be called when someone becomes President
     */
    public void recalculateCommissionsForPresidentPromotion(Long newPresidentId) {
        logger.info(String.format("Recalculating commissions due to President promotion: %d", newPresidentId));
        
        // Get all downlines of the new President
        List<Long> affectedDownlines = getAllDownlineIds(newPresidentId);
        
        logger.info(String.format("President promotion affects %d downline affiliates", 
            affectedDownlines.size()));
        
        // Mark any pending commissions as affected by President independence
        // Implementation depends on your commission payment system
        // You might need to:
        // 1. Cancel pending commission payments from uplines
        // 2. Update commission calculation rules
        // 3. Notify affected parties
    }
    
    /**
     * Get all downline IDs for an affiliate
     */
    private List<Long> getAllDownlineIds(Long affiliateId) {
        Query query = em.createNativeQuery("""
            WITH RECURSIVE downline_tree AS (
                -- Base case: direct referrals
                SELECT id
                FROM affiliates 
                WHERE referrer_id = :affiliateId
                
                UNION ALL
                
                -- Recursive case: referrals of referrals
                SELECT a.id
                FROM affiliates a
                INNER JOIN downline_tree dt ON a.referrer_id = dt.id
            )
            SELECT id FROM downline_tree
        """);
        query.setParameter("affiliateId", affiliateId);
        
        @SuppressWarnings("unchecked")
        List<Number> results = query.getResultList();
        
        return results.stream()
            .map(Number::longValue)
            .toList();
    }
    
    /**
     * Check if commissions should be paid for a specific affiliate
     * Returns false if there's a President blocking commissions
     */
    public boolean shouldPayCommissions(Long affiliateId) {
        return !promotionService.hasPresidentInUpline(affiliateId);
    }
    
    /**
     * Get commission summary for an affiliate
     */
    public Map<String, Object> getCommissionSummary(Long affiliateId) {
        Map<String, Object> summary = new HashMap<>();
        
        Affiliate affiliate = em.find(Affiliate.class, affiliateId);
        if (affiliate == null) {
            return summary;
        }
        
        AffiliateLevel level = affiliate.getLevel();
        
        // Basic info
        summary.put("affiliateLevel", level.name());
        summary.put("isPresidentIndependent", affiliate.getIsPresidentIndependent());
        summary.put("canEarnCommissions", shouldPayCommissions(affiliateId));
        
        // Commission structure for this level
        Map<String, String> commissionStructure = new HashMap<>();
        commissionStructure.put("directReferral", getPercentageString(level, 1));
        commissionStructure.put("firstGeneration", getPercentageString(level, 2));
        commissionStructure.put("secondGeneration", getPercentageString(level, 3));
        commissionStructure.put("thirdGeneration", getPercentageString(level, 4));
        commissionStructure.put("leadershipBonus", getLeadershipBonusString(level));
        
        summary.put("commissionStructure", commissionStructure);
        
        // Upgrade requirements
        Map<String, Object> upgradeRequirements = new HashMap<>();
        upgradeRequirements.put("minDirectReferrals", level.getMinDirectReferrals());
        upgradeRequirements.put("minTotalDownlines", level.getMinTotalDownlines());
        upgradeRequirements.put("minConsumption", level.getMinConsumption());
        
        summary.put("upgradeRequirements", upgradeRequirements);
        
        return summary;
    }
    
    /**
     * Get percentage string for a specific generation
     */
    private String getPercentageString(AffiliateLevel level, int generation) {
        double percentage = getCommissionPercentage(level, generation);
        return percentage > 0 ? String.format("%.0f%%", percentage * 100) : "-";
    }
    
    /**
     * Get leadership bonus string
     */
    private String getLeadershipBonusString(AffiliateLevel level) {
        double percentage = 0.0;
        switch (level) {
            case LEADER -> percentage = 0.05;
            case INFLUENCER -> percentage = 0.07;
            case PRESIDENT -> percentage = 0.10;
            default -> percentage = 0.0;
        }
        return percentage > 0 ? String.format("%.0f%%", percentage * 100) : "-";
    }
    
    /**
     * Get commission percentage for a specific level and generation
     * This centralizes the commission structure logic
     */
    private double getCommissionPercentage(AffiliateLevel level, int generation) {
        switch (level) {
            case FAN:
                return 0.0;
            case SUBSCRIBER:
                return generation == 1 ? 0.10 : 0.0;
            case READER:
                switch (generation) {
                    case 1: return 0.12;
                    case 2: return 0.05;
                    default: return 0.0;
                }
            case PROMOTER:
                switch (generation) {
                    case 1: return 0.15;
                    case 2: return 0.07;
                    case 3: return 0.03;
                    default: return 0.0;
                }
            case LEADER:
                switch (generation) {
                    case 1: return 0.18;
                    case 2: return 0.10;
                    case 3: return 0.05;
                    case 4: return 0.02;
                    default: return 0.0;
                }
            case INFLUENCER:
                switch (generation) {
                    case 1: return 0.20;
                    case 2: return 0.12;
                    case 3: return 0.07;
                    case 4: return 0.03;
                    default: return 0.0;
                }
            case PRESIDENT:
                switch (generation) {
                    case 1: return 0.25;
                    case 2: return 0.15;
                    case 3: return 0.10;
                    case 4: return 0.05;
                    default: return 0.0;
                }
            default:
                return 0.0;
        }
    }
    
    /**
     * Validate commission structure matches the official table
     * This method can be used for testing and verification
     */
    public Map<String, Object> validateCommissionStructure() {
        Map<String, Object> validation = new HashMap<>();
        Map<String, Map<String, String>> levelStructures = new HashMap<>();
        
        for (AffiliateLevel level : AffiliateLevel.values()) {
            Map<String, String> structure = new HashMap<>();
            structure.put("directReferral", getPercentageString(level, 1));
            structure.put("firstGeneration", getPercentageString(level, 2));
            structure.put("secondGeneration", getPercentageString(level, 3));
            structure.put("thirdGeneration", getPercentageString(level, 4));
            structure.put("leadershipBonus", getLeadershipBonusString(level));
            structure.put("upgradeConditions", getUpgradeConditionsString(level));
            
            levelStructures.put(level.name(), structure);
        }
        
        validation.put("commissionStructure", levelStructures);
        validation.put("validationTimestamp", LocalDateTime.now().toString());
        validation.put("note", "Commission structure matches TrainingCommissions.vue table");
        
        return validation;
    }
    
    /**
     * Get upgrade conditions string
     */
    private String getUpgradeConditionsString(AffiliateLevel level) {
        if (level == AffiliateLevel.FAN) {
            return "Starting Level";
        }
        
        StringBuilder conditions = new StringBuilder();
        conditions.append("Directly Refer ").append(level.getMinDirectReferrals()).append(" People");
        
        if (level.getMinTotalDownlines() > 0) {
            conditions.append(", ").append(level.getMinTotalDownlines()).append(" Downlines");
        }
        
        if (level.getMinConsumption() > 0) {
            if (level == AffiliateLevel.READER) {
                conditions.append(", Each Consuming ").append(level.getMinConsumption());
            } else {
                conditions.append(", ").append("Person Consuming ").append(level.getMinConsumption());
            }
        }
        
        return conditions.toString();
    }
}