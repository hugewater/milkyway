package com.bigwater.service;

import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ScheduledPromotionService {
    
    private static final Logger logger = Logger.getLogger(ScheduledPromotionService.class.getName());
    
    @Inject
    AffiliatePromotionService promotionService;
    
    /**
     * Manual trigger for testing promotions
     * Can be called via REST endpoint
     */
    public void triggerManualPromotions() {
        logger.info("Manual promotion trigger initiated...");
        
        try {
            promotionService.processPromotions();
            logger.info("Manual promotions completed successfully");
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error during manual promotions: {0}", e.getMessage());
            throw new RuntimeException("Manual promotion failed", e);
        }
    }
    
    /**
     * Promote a specific affiliate by ID
     */
    public boolean promoteSpecificAffiliate(Long affiliateId) {
        logger.info(() -> String.format("Manual promotion trigger for affiliate: %d", affiliateId));
        
        try {
            boolean result = promotionService.promoteAffiliate(affiliateId);
            logger.info(() -> String.format("Manual promotion for affiliate %d: %s", 
                affiliateId, result ? "SUCCESS" : "NO_PROMOTION_NEEDED"));
            return result;
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, 
                String.format("Error during manual promotion for affiliate %d: %s", affiliateId, e.getMessage()), e);
            throw new RuntimeException("Manual promotion failed for affiliate " + affiliateId, e);
        }
    }
}