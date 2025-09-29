# Database Migration Guide for Affiliate System

## Required Database Changes

**YES, you need to create new database tables** for the affiliate system to work properly. The existing schema has basic referral functionality, but lacks the comprehensive affiliate level system we've implemented.

## What Tables Need to Be Created

### 1. **Core Tables (Required)**
- `affiliates` - Main affiliate records with levels and hierarchy
- `commissions` - Track all commission payments (generation + leadership)
- `affiliate_transactions` - Record qualifying transactions
- `affiliate_promotions` - Track level promotion history

### 2. **Performance Tables (Recommended)**
- `affiliate_statistics` - Cached statistics for better performance
- `commission_calculation_logs` - Debug and audit commission calculations

## Migration Options

### Option A: Clean Installation (Recommended)
If you're starting fresh or can recreate the database:

```bash
# Run the complete affiliate schema
mysql -u your_username -p your_database < AFFILIATE_DATABASE_SCHEMA.sql
```

### Option B: Add to Existing Database
If you have existing data to preserve:

```sql
-- Step 1: Create affiliate tables
SOURCE AFFILIATE_DATABASE_SCHEMA.sql;

-- Step 2: Migrate existing users to affiliates (optional)
INSERT INTO affiliates (email, first_name, last_name, affiliate_level, created_at)
SELECT email, first_name, last_name, 'FAN', created_at 
FROM users 
WHERE role = 'subscriber';

-- Step 3: Link existing referral relationships
UPDATE affiliates a1 
SET referrer_id = (
    SELECT a2.id 
    FROM affiliates a2 
    JOIN users u2 ON a2.email = u2.email
    JOIN users u1 ON u1.referred_by_code = u2.referral_code
    WHERE u1.email = a1.email
);
```

### Option C: Gradual Migration
Run tables one by one to test:

```sql
-- Core affiliate table first
CREATE TABLE affiliates (...);

-- Test with sample data
INSERT INTO affiliates (email, affiliate_level) VALUES ('test@example.com', 'FAN');

-- Add other tables as needed
CREATE TABLE commissions (...);
CREATE TABLE affiliate_transactions (...);
```

## Database Configuration Changes

### 1. **Application Properties**
Update your Quarkus configuration:

```properties
# Database settings for affiliate system
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.sql-load-script=AFFILIATE_DATABASE_SCHEMA.sql
```

### 2. **JPA Entity Scanning**
Ensure your entities are scanned:

```properties
quarkus.hibernate-orm.packages=com.bigwater.model
```

## Verification Steps

### 1. **Test Database Structure**
```sql
-- Verify tables exist
SHOW TABLES LIKE 'affiliate%';
SHOW TABLES LIKE 'commission%';

-- Check table structure
DESCRIBE affiliates;
DESCRIBE commissions;
```

### 2. **Test API Endpoints**
```bash
# Test commission structure validation
curl http://localhost:8080/api/affiliate/commission-structure/validate

# Test commission calculations
curl http://localhost:8080/api/affiliate/test/commission-calculations
```

### 3. **Sample Data Testing**
The schema includes sample data for testing all affiliate levels and referral chains.

## Integration with Existing Schema

### Current Schema Compatibility
Your existing schema has:
- ✅ `users` table with basic referral codes
- ✅ `referrals` table with commission tracking
- ✅ `rewards` table for payments

### New Schema Adds:
- 🆕 **Affiliate Levels**: 7-tier system (Fan → President)
- 🆕 **Generation Commissions**: Multi-level percentage structure
- 🆕 **Leadership Bonuses**: Additional bonuses for higher levels
- 🆕 **President Independence**: Complex business rule implementation
- 🆕 **Auto-Promotion**: Automatic level advancement based on criteria

## Data Migration Strategy

### If You Have Existing Users:
```sql
-- Migrate users to affiliates
INSERT INTO affiliates (email, first_name, last_name, affiliate_level, total_consumption, created_at)
SELECT 
    email, 
    first_name, 
    last_name, 
    'FAN' as affiliate_level,
    0.00 as total_consumption,
    created_at
FROM users;

-- Set up referral relationships
UPDATE affiliates a
SET referrer_id = (
    SELECT referrer.id 
    FROM affiliates referrer
    JOIN users u_referrer ON referrer.email = u_referrer.email
    JOIN users u_referred ON u_referred.referred_by_code = u_referrer.referral_code
    WHERE u_referred.email = a.email
);
```

## Performance Considerations

### 1. **Indexes**
All critical indexes are included in the schema:
- Affiliate hierarchy navigation
- Commission lookups
- Transaction tracking
- Performance statistics

### 2. **Caching**
The `affiliate_statistics` table caches expensive calculations:
- Referral counts
- Commission totals
- Performance metrics

### 3. **Batch Processing**
Commission calculations can be batched for better performance:
- Daily promotion checks
- Bulk commission calculations
- Statistics updates

## Testing Checklist

Before deploying to production:

- [ ] Database tables created successfully
- [ ] Sample data inserted and relationships work
- [ ] Commission calculations return correct percentages
- [ ] President independence rule blocks upline commissions
- [ ] Auto-promotion system evaluates criteria correctly
- [ ] API endpoints return expected responses
- [ ] Performance acceptable with test data volume

## Rollback Plan

If you need to rollback:

```sql
-- Remove affiliate tables (BE CAREFUL!)
DROP TABLE IF EXISTS commission_calculation_logs;
DROP TABLE IF EXISTS affiliate_statistics;
DROP TABLE IF EXISTS affiliate_promotions;
DROP TABLE IF EXISTS affiliate_transactions;
DROP TABLE IF EXISTS commissions;
DROP TABLE IF EXISTS affiliates;

-- Remove configuration entries
DELETE FROM system_configurations WHERE config_key LIKE 'affiliate_%';
```

## Summary

**You MUST create these database tables** for the affiliate system to function. The Java code expects these specific table structures and relationships. The existing referral system is too basic for the comprehensive affiliate level and commission system we've implemented.

**Recommended approach:**
1. Run `AFFILIATE_DATABASE_SCHEMA.sql` on a test database first
2. Test the API endpoints with sample data
3. Migrate your existing user data if needed
4. Deploy to production with proper backups

The new schema is designed to be performant, scalable, and fully supports all the business rules in your commission table.