package com.bigwater.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

import com.bigwater.service.AffiliatePromotionService;
import com.bigwater.service.AffiliateTransactionService;
import com.bigwater.service.CommissionCalculationService;
import com.bigwater.service.CommissionTestService;
import com.bigwater.service.ScheduledPromotionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/affiliate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AffiliateController {
    
    private static final Logger logger = Logger.getLogger(AffiliateController.class.getName());
    
    @Inject
    AffiliatePromotionService promotionService;
    
    @Inject
    CommissionCalculationService commissionService;
    
    @Inject
    ScheduledPromotionService scheduledService;
    
    @Inject
    CommissionTestService testService;
    
    @Inject
    AffiliateTransactionService transactionService;
    
    /**
     * Trigger manual promotion check for all affiliates
     * POST /api/affiliate/promotions/process
     */
    @POST
    @Path("/promotions/process")
    public Response processAllPromotions() {
        try {
            scheduledService.triggerManualPromotions();
            return Response.ok()
                .entity(Map.of("status", "success", "message", "Promotions processed successfully"))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to process promotions: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Promote specific affiliate by ID
     * POST /api/affiliate/{id}/promote
     */
    @POST
    @Path("/{id}/promote")
    public Response promoteAffiliate(@PathParam("id") Long affiliateId) {
        try {
            boolean promoted = scheduledService.promoteSpecificAffiliate(affiliateId);
            
            if (promoted) {
                return Response.ok()
                    .entity(Map.of(
                        "status", "success",
                        "message", "Affiliate promoted successfully",
                        "affiliateId", affiliateId,
                        "promoted", true
                    ))
                    .build();
            } else {
                return Response.ok()
                    .entity(Map.of(
                        "status", "success",
                        "message", "Affiliate does not qualify for promotion or is already at highest level",
                        "affiliateId", affiliateId,
                        "promoted", false
                    ))
                    .build();
            }
        } catch (Exception e) {
            logger.severe("Failed to promote affiliate " + affiliateId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Calculate commissions for a transaction
     * POST /api/affiliate/commissions/calculate
     */
    @POST
    @Path("/commissions/calculate")
    public Response calculateCommissions(CommissionRequest request) {
        try {
            Map<Long, BigDecimal> commissions = commissionService.calculateCommissions(
                request.payerAffiliateId, request.transactionAmount);
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "commissions", commissions,
                    "totalCommission", commissions.values().stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to calculate commissions: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Get commission summary for an affiliate
     * GET /api/affiliate/{id}/commission-summary
     */
    @GET
    @Path("/{id}/commission-summary")
    public Response getCommissionSummary(@PathParam("id") Long affiliateId) {
        try {
            Map<String, Object> summary = commissionService.getCommissionSummary(affiliateId);
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "affiliateId", affiliateId,
                    "summary", summary
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to get commission summary for affiliate " + affiliateId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Check if commissions should be paid for an affiliate
     * GET /api/affiliate/{id}/commission-eligible
     */
    @GET
    @Path("/{id}/commission-eligible")
    public Response checkCommissionEligibility(@PathParam("id") Long affiliateId) {
        try {
            boolean eligible = commissionService.shouldPayCommissions(affiliateId);
            boolean hasPresidentUpline = promotionService.hasPresidentInUpline(affiliateId);
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "affiliateId", affiliateId,
                    "eligible", eligible,
                    "hasPresidentUpline", hasPresidentUpline,
                    "reason", hasPresidentUpline ? "President in upline blocks commissions" : "Eligible for commissions"
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to check commission eligibility for affiliate " + affiliateId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Validate commission structure matches the official table
     * GET /api/affiliate/commission-structure/validate
     */
    @GET
    @Path("/commission-structure/validate")
    public Response validateCommissionStructure() {
        try {
            Map<String, Object> validation = commissionService.validateCommissionStructure();
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "message", "Commission structure validation completed",
                    "validation", validation
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to validate commission structure: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Test commission calculations and verify structure
     * GET /api/affiliate/test/commission-calculations
     */
    @GET
    @Path("/test/commission-calculations")
    public Response testCommissionCalculations() {
        try {
            Map<String, Object> testResults = testService.testCommissionCalculations();
            boolean structureValid = testService.verifyCommissionStructure();
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "message", "Commission calculation test completed",
                    "structureValid", structureValid,
                    "testResults", testResults
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to test commission calculations: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Record a new affiliate transaction
     * POST /api/affiliate/transactions/record
     */
    @POST
    @Path("/transactions/record")
    public Response recordTransaction(TransactionRequest request) {
        try {
            com.bigwater.model.AffiliateTransaction transaction = transactionService.recordTransaction(
                request.affiliateId,
                request.amount,
                request.transactionId,
                request.transactionType,
                request.description
            );
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "message", "Transaction recorded successfully",
                    "transactionId", transaction.getTransactionId(),
                    "affiliateId", transaction.getAffiliate().getId()
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to record transaction: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Confirm a transaction and trigger commission calculations
     * POST /api/affiliate/transactions/{transactionId}/confirm
     */
    @POST
    @Path("/transactions/{transactionId}/confirm")
    public Response confirmTransaction(@PathParam("transactionId") String transactionId) {
        try {
            transactionService.confirmTransaction(transactionId);
            
            return Response.ok()
                .entity(Map.of(
                    "status", "success",
                    "message", "Transaction confirmed and commissions processed",
                    "transactionId", transactionId
                ))
                .build();
        } catch (Exception e) {
            logger.severe("Failed to confirm transaction " + transactionId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("status", "error", "message", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Request class for commission calculations
     */
    public static class CommissionRequest {
        public Long payerAffiliateId;
        public BigDecimal transactionAmount;
        
        // Default constructor for JSON deserialization
        public CommissionRequest() {}
        
        public CommissionRequest(Long payerAffiliateId, BigDecimal transactionAmount) {
            this.payerAffiliateId = payerAffiliateId;
            this.transactionAmount = transactionAmount;
        }
    }
    
    /**
     * Request class for transaction recording
     */
    public static class TransactionRequest {
        public Long affiliateId;
        public BigDecimal amount;
        public String transactionId;
        public com.bigwater.model.AffiliateTransaction.TransactionType transactionType;
        public String description;
        
        // Default constructor for JSON deserialization
        public TransactionRequest() {}
        
        public TransactionRequest(Long affiliateId, BigDecimal amount, String transactionId, 
                                com.bigwater.model.AffiliateTransaction.TransactionType transactionType, 
                                String description) {
            this.affiliateId = affiliateId;
            this.amount = amount;
            this.transactionId = transactionId;
            this.transactionType = transactionType;
            this.description = description;
        }
    }
}