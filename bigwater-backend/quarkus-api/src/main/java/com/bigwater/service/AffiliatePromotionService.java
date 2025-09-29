package com.bigwater.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import com.bigwater.model.Affiliate;
import com.bigwater.model.AffiliateLevel;
import com.bigwater.model.AffiliatePromotion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AffiliatePromotionService {
    
    private static final Logger logger = Logger.getLogger(AffiliatePromotionService.class.getName());
    
    @Inject
    EntityManager em;
    
    /**
     * Checks and promotes affiliates based on their qualification criteria
     * Called periodically (e.g., daily via scheduler)
     */
    @Transactional
    public void processPromotions() {
        logger.info("Starting affiliate promotion process...");
        
        // Get all affiliates who need promotion check
        List<Affiliate> affiliates = getAllAffiliatesForPromotionCheck();
        
        int promotionsCount = 0;
        for (Affiliate affiliate : affiliates) {
            if (checkAndPromoteAffiliate(affiliate)) {
                promotionsCount++;
            }
        }
        
        logger.info(String.format("Promotion process completed. %d affiliates promoted.", promotionsCount));
    }
    
    /**
     * Checks if an affiliate qualifies for promotion and promotes them
     */
    @Transactional
    public boolean checkAndPromoteAffiliate(Affiliate affiliate) {
        AffiliateLevel currentLevel = affiliate.getLevel();
        AffiliateLevel nextLevel = AffiliateLevel.getNextLevel(currentLevel);
        
        // Already at highest level
        if (currentLevel == nextLevel) {
            affiliate.setLastPromotionCheck(LocalDateTime.now());
            em.merge(affiliate);
            return false;
        }
        
        // Check qualification for next level
        if (qualifiesForLevel(affiliate, nextLevel)) {
            logger.info(String.format("Promoting affiliate %s from %s to %s", 
                affiliate.getEmail(), currentLevel, nextLevel));
            
            // Create promotion record
            AffiliatePromotion promotion = new AffiliatePromotion(
                affiliate, currentLevel, nextLevel, AffiliatePromotion.PromotionType.AUTOMATIC);
            
            // Set qualification metrics
            promotion.setDirectReferralsCount(getDirectReferralCount(affiliate.getId()));
            promotion.setTotalDownlinesCount(getTotalDownlineCount(affiliate.getId()));
            promotion.setTotalConsumption(BigDecimal.valueOf(affiliate.getTotalConsumption()));
            
            // Special handling when becoming President
            if (nextLevel == AffiliateLevel.PRESIDENT) {
                promotion.setBecamePresidentIndependent(true);
                handlePresidentPromotion(affiliate);
            }
            
            // Promote the affiliate
            affiliate.setLevel(nextLevel);
            affiliate.setLastPromotionCheck(LocalDateTime.now());
            
            // Persist both records
            em.persist(promotion);
            em.merge(affiliate);
            
            return true;
        } else {
            affiliate.setLastPromotionCheck(LocalDateTime.now());
            em.merge(affiliate);
            return false;
        }
    }
    
    /**
     * Special handling when an affiliate becomes President
     * Mark them as independent so uplines stop collecting commissions
     */
    private void handlePresidentPromotion(Affiliate newPresident) {
        logger.info(String.format("Handling President promotion for %s", newPresident.getEmail()));
        
        // Mark the new President as independent
        newPresident.setIsPresidentIndependent(true);
        
        // Log this important event for commission calculation system
        logger.info(String.format("President %s is now independent. Uplines will no longer collect commissions from this branch.", 
            newPresident.getEmail()));
        
        // You might want to trigger a commission recalculation job here
        // or send notifications to affected uplines
        notifyPresidentPromotion(newPresident);
    }
    
    /**
     * Checks if an affiliate qualifies for a specific level
     */
    private boolean qualifiesForLevel(Affiliate affiliate, AffiliateLevel targetLevel) {
        int directReferrals = getDirectReferralCount(affiliate.getId());
        int totalDownlines = getTotalDownlineCount(affiliate.getId());
        
        boolean qualifies = true;
        
        // Check direct referrals requirement
        if (directReferrals < targetLevel.getMinDirectReferrals()) {
            qualifies = false;
        }
        
        // Check total downlines requirement
        if (totalDownlines < targetLevel.getMinTotalDownlines()) {
            qualifies = false;
        }
        
        // Check consumption requirement (for Reader and Subscriber levels)
        if (targetLevel.getMinConsumption() > 0) {
            // For Reader: Each direct referral should consume at least 1680
            if (targetLevel == AffiliateLevel.READER) {
                qualifies = qualifies && checkReaderConsumptionRequirement(affiliate);
            }
            // For Subscriber: At least 1 direct referral consuming 1680
            else if (targetLevel == AffiliateLevel.SUBSCRIBER) {
                qualifies = qualifies && checkSubscriberConsumptionRequirement(affiliate);
            }
        }
        
        logger.info(String.format("Qualification check for %s to %s: DirectReferrals=%d/%d, TotalDownlines=%d/%d, Qualifies=%b",
            affiliate.getEmail(), targetLevel, directReferrals, targetLevel.getMinDirectReferrals(),
            totalDownlines, targetLevel.getMinTotalDownlines(), qualifies));
        
        return qualifies;
    }
    
    /**
     * Gets count of direct referrals for an affiliate
     */
    private int getDirectReferralCount(Long affiliateId) {
        Query query = em.createQuery("SELECT COUNT(a) FROM Affiliate a WHERE a.referrer.id = :affiliateId");
        query.setParameter("affiliateId", affiliateId);
        return ((Long) query.getSingleResult()).intValue();
    }
    
    /**
     * Gets total count of all downlines (recursive)
     */
    private int getTotalDownlineCount(Long affiliateId) {
        // This is a recursive query to count all downlines
        Query query = em.createNativeQuery("""
            WITH RECURSIVE downline_tree AS (
                -- Base case: direct referrals
                SELECT id, referrer_id, 1 as level
                FROM affiliates 
                WHERE referrer_id = :affiliateId
                
                UNION ALL
                
                -- Recursive case: referrals of referrals
                SELECT a.id, a.referrer_id, dt.level + 1
                FROM affiliates a
                INNER JOIN downline_tree dt ON a.referrer_id = dt.id
                WHERE dt.level < 10  -- Prevent infinite recursion
            )
            SELECT COUNT(*) FROM downline_tree
        """);
        query.setParameter("affiliateId", affiliateId);
        return ((Number) query.getSingleResult()).intValue();
    }
    
    /**
     * Check if Reader consumption requirement is met
     * Each direct referral should consume at least 1680
     */
    private boolean checkReaderConsumptionRequirement(Affiliate affiliate) {
        Query query = em.createQuery("""
            SELECT COUNT(a) FROM Affiliate a 
            WHERE a.referrer.id = :affiliateId 
            AND a.totalConsumption >= :minConsumption
        """);
        query.setParameter("affiliateId", affiliate.getId());
        query.setParameter("minConsumption", 1680.0);
        
        int qualifiedReferrals = ((Long) query.getSingleResult()).intValue();
        int directReferrals = getDirectReferralCount(affiliate.getId());
        
        // All direct referrals must meet consumption requirement
        return qualifiedReferrals >= directReferrals && directReferrals >= 3;
    }
    
    /**
     * Check if Subscriber consumption requirement is met
     * At least 1 direct referral consuming 1680
     */
    private boolean checkSubscriberConsumptionRequirement(Affiliate affiliate) {
        Query query = em.createQuery("""
            SELECT COUNT(a) FROM Affiliate a 
            WHERE a.referrer.id = :affiliateId 
            AND a.totalConsumption >= :minConsumption
        """);
        query.setParameter("affiliateId", affiliate.getId());
        query.setParameter("minConsumption", 1680.0);
        
        int qualifiedReferrals = ((Long) query.getSingleResult()).intValue();
        return qualifiedReferrals >= 1;
    }
    
    /**
     * Gets all affiliates that need promotion checking
     */
    private List<Affiliate> getAllAffiliatesForPromotionCheck() {
        Query query = em.createQuery("""
            SELECT a FROM Affiliate a 
            WHERE a.level != :presidentLevel 
            AND (a.lastPromotionCheck IS NULL 
                 OR a.lastPromotionCheck < :checkThreshold)
            ORDER BY a.level DESC, a.createdAt ASC
        """, Affiliate.class);
        
        query.setParameter("presidentLevel", AffiliateLevel.PRESIDENT);
        query.setParameter("checkThreshold", LocalDateTime.now().minusHours(24)); // Check once per day
        
        @SuppressWarnings("unchecked")
        List<Affiliate> result = query.getResultList();
        return result;
    }
    
    /**
     * Notify about President promotion
     */
    private void notifyPresidentPromotion(Affiliate newPresident) {
        // Implementation for notifications
        // This could send emails, create notifications, update commission calculations, etc.
        logger.info(String.format("Notification: %s has been promoted to President level", newPresident.getEmail()));
        
        // You might want to:
        // 1. Send congratulatory email to new President
        // 2. Notify uplines about commission structure change
        // 3. Trigger commission recalculation for the entire upline chain
        // 4. Update any cached commission calculations
    }
    
    /**
     * Manual promotion trigger for specific affiliate
     */
    @Transactional
    public boolean promoteAffiliate(Long affiliateId) {
        Affiliate affiliate = em.find(Affiliate.class, affiliateId);
        if (affiliate == null) {
            return false;
        }
        
        return checkAndPromoteAffiliate(affiliate);
    }
    
    /**
     * Check if an affiliate's upline chain contains any Presidents
     * Used for commission calculations
     */
    public boolean hasPresidentInUpline(Long affiliateId) {
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
                WHERE ut.level < 10 AND a.referrer_id IS NOT NULL
            )
            SELECT COUNT(*) FROM affiliates a
            INNER JOIN upline_tree ut ON a.id = ut.referrer_id
            WHERE a.affiliate_level = 'PRESIDENT' AND a.is_president_independent = true
        """);
        query.setParameter("affiliateId", affiliateId);
        
        int presidentCount = ((Number) query.getSingleResult()).intValue();
        return presidentCount > 0;
    }
}