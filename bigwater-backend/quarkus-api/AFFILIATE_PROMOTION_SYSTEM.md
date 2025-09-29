# Affiliate Auto-Promotion System

## Overview

This Java implementation provides an automated affiliate promotion system with sophisticated commission calculation rules, including the special President Independence Rule where uplines stop collecting commissions when someone becomes President.

## Key Features

- **Automatic Level Promotion**: Affiliates are automatically promoted based on referral counts, downline sizes, and consumption requirements
- **President Independence Rule**: When an affiliate becomes President, all uplines stop collecting commissions from that President's entire network
- **Comprehensive Commission Calculation**: Multi-generational commission structure with different percentages per level
- **REST API Management**: Manual promotion triggers and commission calculations via API
- **Real-time Qualification Checking**: Dynamic evaluation of promotion requirements

## Affiliate Levels & Requirements

| Level | Direct Referrals | Total Downlines | Special Requirements |
|-------|-----------------|-----------------|---------------------|
| FAN | 0 | 0 | Starting level |
| SUBSCRIBER | 1 | 10 | 1 referral consuming ≥1680 |
| READER | 3 | 0 | All referrals consuming ≥1680 |
| PROMOTER | 5 | 10 | Standard referral requirements |
| LEADER | 10 | 30 | 5 consuming referrals |
| INFLUENCER | 20 | 50 | 7 consuming referrals |
| PRESIDENT | 50 | 100 | 10 consuming referrals |

## Commission Structure

### Generation Percentages by Level

| Level | Gen 1 | Gen 2 | Gen 3 | Gen 4 | Gen 5 | Gen 6 |
|-------|-------|-------|-------|-------|-------|-------|
| FAN | 0% | - | - | - | - | - |
| SUBSCRIBER | 10% | - | - | - | - | - |
| READER | 12% | 5% | - | - | - | - |
| PROMOTER | 15% | 7% | 3% | - | - | - |
| LEADER | 18% | 10% | 5% | 2% | - | - |
| INFLUENCER | 20% | 12% | 7% | 3% | 2% | - |
| PRESIDENT | 25% | 15% | 10% | 5% | 3% | 2% |

## Core Components

### 1. AffiliateLevel Enum
Defines all affiliate levels with their qualification requirements and commission percentages.

```java
public enum AffiliateLevel {
    FAN(0, 0, 0, 0, 0, 0, 0, 0, 0),
    SUBSCRIBER(1, 10, 0, 0, 0, 0, 1, 0, 1680),
    READER(2, 12, 5, 0, 0, 0, 3, 0, 1680),
    PROMOTER(3, 15, 7, 3, 0, 0, 5, 10, 0),
    LEADER(4, 18, 10, 5, 2, 5, 10, 30, 0),
    INFLUENCER(5, 20, 12, 7, 3, 7, 20, 50, 0),
    PRESIDENT(6, 25, 15, 10, 5, 10, 50, 100, 0);
}
```

### 2. Affiliate Entity
JPA entity representing an affiliate with referral relationships and tracking fields.

Key fields:
- `level`: Current affiliate level
- `referrer`: Parent affiliate (upline)
- `totalConsumption`: Sum of all purchases/consumption
- `isPresidentIndependent`: Flag for President independence rule
- `lastPromotionCheck`: Timestamp of last promotion evaluation

### 3. AffiliatePromotionService
Main service handling automatic promotions with business logic:

- `processPromotions()`: Check all affiliates for promotion eligibility
- `checkAndPromoteAffiliate()`: Evaluate and promote individual affiliate
- `handlePresidentPromotion()`: Special handling for President promotions
- `hasPresidentInUpline()`: Check if upline contains independent Presidents

### 4. CommissionCalculationService
Handles commission calculations with President independence:

- `calculateCommissions()`: Calculate commissions for a transaction
- `shouldPayCommissions()`: Check if commissions should be paid (no President blocking)
- `processCommissionPayments()`: Process actual commission payments

### 5. AffiliateController
REST API for manual management:

- `POST /api/affiliate/promotions/process` - Process all promotions
- `POST /api/affiliate/{id}/promote` - Promote specific affiliate
- `POST /api/affiliate/commissions/calculate` - Calculate commissions
- `GET /api/affiliate/{id}/commission-summary` - Get commission summary
- `GET /api/affiliate/{id}/commission-eligible` - Check commission eligibility

## President Independence Rule Implementation

### The Business Rule
When an affiliate reaches President level:
1. They become "independent" (`isPresidentIndependent = true`)
2. All uplines immediately stop collecting commissions from:
   - The new President's transactions
   - ALL transactions from the President's entire downline network
3. The President keeps their full commission structure for their own network

### Technical Implementation

#### 1. Promotion Detection
```java
if (nextLevel == AffiliateLevel.PRESIDENT) {
    handlePresidentPromotion(affiliate);
}
```

#### 2. Independence Marking
```java
private void handlePresidentPromotion(Affiliate newPresident) {
    newPresident.setIsPresidentIndependent(true);
    // Trigger commission recalculations
    notifyPresidentPromotion(newPresident);
}
```

#### 3. Commission Blocking
```java
public Map<Long, BigDecimal> calculateCommissions(Long payerAffiliateId, BigDecimal amount) {
    // Check for President in upline
    if (promotionService.hasPresidentInUpline(payerAffiliateId)) {
        return new HashMap<>(); // No commissions paid
    }
    // ... normal commission calculation
}
```

#### 4. Upline Chain Analysis
```java
public boolean hasPresidentInUpline(Long affiliateId) {
    // Recursive SQL query to check entire upline chain for Presidents
    // Returns true if any President exists in upline hierarchy
}
```

## Database Schema Requirements

### Affiliates Table
```sql
CREATE TABLE affiliates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    affiliate_level ENUM('FAN','SUBSCRIBER','READER','PROMOTER','LEADER','INFLUENCER','PRESIDENT'),
    referrer_id BIGINT,
    total_consumption DECIMAL(10,2) DEFAULT 0.00,
    is_president_independent BOOLEAN DEFAULT FALSE,
    last_promotion_check DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (referrer_id) REFERENCES affiliates(id)
);
```

### Commission Records (Optional)
```sql
CREATE TABLE commissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipient_id BIGINT NOT NULL,
    source_affiliate_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_id VARCHAR(255),
    generation_level INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipient_id) REFERENCES affiliates(id),
    FOREIGN KEY (source_affiliate_id) REFERENCES affiliates(id)
);
```

## Usage Examples

### 1. Manual Promotion Check
```bash
curl -X POST http://localhost:8080/api/affiliate/123/promote
```

### 2. Calculate Commissions
```bash
curl -X POST http://localhost:8080/api/affiliate/commissions/calculate \\
  -H "Content-Type: application/json" \\
  -d '{
    "payerAffiliateId": 123,
    "transactionAmount": 1000.00
  }'
```

### 3. Check Commission Eligibility
```bash
curl http://localhost:8080/api/affiliate/123/commission-eligible
```

## Integration Points

### 1. Transaction Processing
When processing payments, call commission calculation:

```java
@Transactional
public void processPayment(PaymentRequest request) {
    // Process payment
    Payment payment = processPaymentTransaction(request);
    
    // Calculate and pay commissions
    if (payment.isSuccessful()) {
        commissionService.processCommissionPayments(
            request.getAffiliateId(),
            request.getAmount(),
            payment.getTransactionId()
        );
    }
}
```

### 2. Periodic Promotion Checks
Set up scheduled jobs (cron, Quartz, etc.) to run promotions:

```java
// Daily at 2 AM
@Scheduled(cron = "0 0 2 * * ?")
public void dailyPromotionCheck() {
    promotionService.processPromotions();
}
```

### 3. Real-time Promotion Triggers
Call promotion check after significant events:

```java
// After referral registration
public void registerReferral(ReferralRequest request) {
    Affiliate newAffiliate = createAffiliate(request);
    
    // Check if referrer qualifies for promotion
    if (newAffiliate.getReferrer() != null) {
        promotionService.checkAndPromoteAffiliate(newAffiliate.getReferrer());
    }
}
```

## Testing Scenarios

### 1. Normal Promotion Flow
- Create affiliate network with various levels
- Add referrals and consumption
- Verify automatic promotions occur
- Check commission calculations

### 2. President Independence
- Promote affiliate to President
- Verify uplines stop receiving commissions
- Test commission calculations for President's network
- Ensure President's own commissions work normally

### 3. Edge Cases
- Multiple Presidents in same upline chain
- Promotion during active transactions
- Commission recalculation after President promotion
- Database consistency during concurrent operations

## Performance Considerations

### 1. Recursive Queries
The upline/downline chain queries use recursive CTEs. For very deep networks (>10 levels), consider:
- Adding depth limits
- Caching frequently accessed chains
- Indexing referrer_id columns

### 2. Commission Calculations
For high-volume transactions:
- Batch commission calculations
- Async processing for non-critical updates
- Cache President independence status

### 3. Promotion Checks
- Limit promotion check frequency (daily vs real-time)
- Batch qualification queries
- Use database triggers for consumption updates

## Security & Validation

### 1. Data Integrity
- Validate referral relationships (no circular references)
- Ensure consumption values are positive
- Verify affiliate level transitions are valid

### 2. API Security
- Authenticate promotion trigger endpoints
- Validate affiliate IDs exist
- Rate limit manual promotion requests

### 3. Audit Trail
- Log all promotions with timestamps
- Track commission calculation changes
- Monitor President independence events

This system provides a robust, scalable foundation for affiliate management with the complex President independence business rule properly implemented.