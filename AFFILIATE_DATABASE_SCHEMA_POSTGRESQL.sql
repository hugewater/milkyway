-- ===================================================================
-- AFFILIATE SYSTEM DATABASE SCHEMA - POSTGRESQL VERSION
-- ===================================================================
-- Additional tables needed for the comprehensive affiliate promotion system
-- This extends the existing database schema with affiliate-specific tables

-- Create custom types for enums (PostgreSQL approach)
DO $$ BEGIN
    CREATE TYPE affiliate_level_type AS ENUM ('FAN', 'SUBSCRIBER', 'READER', 'PROMOTER', 'LEADER', 'INFLUENCER', 'PRESIDENT');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE commission_type_enum AS ENUM ('GENERATION', 'LEADERSHIP', 'DIRECT_REFERRAL');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE commission_status_enum AS ENUM ('PENDING', 'CONFIRMED', 'PAID', 'CANCELLED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE transaction_type_enum AS ENUM ('PURCHASE', 'SUBSCRIPTION', 'CONSUMPTION', 'OTHER');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE transaction_status_enum AS ENUM ('PENDING', 'CONFIRMED', 'COMMISSION_CALCULATED', 'COMMISSION_PAID');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE promotion_type_enum AS ENUM ('AUTOMATIC', 'MANUAL', 'ADMIN_OVERRIDE');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE calculation_status_enum AS ENUM ('CALCULATED', 'APPLIED', 'BLOCKED', 'ERROR');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Affiliates table - Core affiliate information with levels and promotion tracking
CREATE TABLE IF NOT EXISTS affiliates (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    
    -- Affiliate level and hierarchy
    affiliate_level affiliate_level_type DEFAULT 'FAN',
    referrer_id BIGINT,
    
    -- Tracking fields
    total_consumption DECIMAL(10,2) DEFAULT 0.00,
    is_president_independent BOOLEAN DEFAULT FALSE,
    last_promotion_check TIMESTAMP WITH TIME ZONE,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_affiliates_referrer FOREIGN KEY (referrer_id) REFERENCES affiliates(id) ON DELETE SET NULL
);

-- Create indexes for affiliates table
CREATE INDEX IF NOT EXISTS idx_affiliates_level ON affiliates(affiliate_level);
CREATE INDEX IF NOT EXISTS idx_affiliates_referrer_id ON affiliates(referrer_id);
CREATE INDEX IF NOT EXISTS idx_affiliates_email ON affiliates(email);
CREATE INDEX IF NOT EXISTS idx_affiliates_president_independent ON affiliates(is_president_independent);
CREATE INDEX IF NOT EXISTS idx_affiliates_last_promotion_check ON affiliates(last_promotion_check);

-- Create trigger for updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE OR REPLACE TRIGGER update_affiliates_updated_at 
    BEFORE UPDATE ON affiliates 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Commissions table - Track all commission payments and calculations
CREATE TABLE IF NOT EXISTS commissions (
    id BIGSERIAL PRIMARY KEY,
    
    -- Commission recipient and source
    recipient_id BIGINT NOT NULL,
    source_affiliate_id BIGINT NOT NULL,
    
    -- Commission details
    amount DECIMAL(10,2) NOT NULL,
    commission_type commission_type_enum NOT NULL,
    generation_level INTEGER, -- 1, 2, 3, 4 for generation commissions
    percentage_rate DECIMAL(5,4), -- The percentage rate used (e.g., 0.25 for 25%)
    
    -- Transaction reference
    transaction_id VARCHAR(255),
    transaction_amount DECIMAL(10,2), -- Original transaction amount
    
    -- Status tracking
    status commission_status_enum DEFAULT 'PENDING',
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP WITH TIME ZONE,
    paid_at TIMESTAMP WITH TIME ZONE,
    
    -- Foreign key constraints
    CONSTRAINT fk_commissions_recipient FOREIGN KEY (recipient_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    CONSTRAINT fk_commissions_source FOREIGN KEY (source_affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE
);

-- Create indexes for commissions table
CREATE INDEX IF NOT EXISTS idx_commissions_recipient_id ON commissions(recipient_id);
CREATE INDEX IF NOT EXISTS idx_commissions_source_affiliate_id ON commissions(source_affiliate_id);
CREATE INDEX IF NOT EXISTS idx_commissions_transaction_id ON commissions(transaction_id);
CREATE INDEX IF NOT EXISTS idx_commissions_status ON commissions(status);
CREATE INDEX IF NOT EXISTS idx_commissions_type ON commissions(commission_type);
CREATE INDEX IF NOT EXISTS idx_commissions_created_at ON commissions(created_at);

-- Affiliate Transactions table - Track all qualifying transactions for commission calculations
CREATE TABLE IF NOT EXISTS affiliate_transactions (
    id BIGSERIAL PRIMARY KEY,
    
    -- Transaction details
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    affiliate_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    
    -- Transaction type and metadata
    transaction_type transaction_type_enum NOT NULL,
    description TEXT,
    metadata JSONB, -- Additional transaction data (JSONB for better performance in PostgreSQL)
    
    -- Status and processing
    status transaction_status_enum DEFAULT 'PENDING',
    commission_calculated_at TIMESTAMP WITH TIME ZONE,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP WITH TIME ZONE,
    
    -- Foreign key constraints
    CONSTRAINT fk_affiliate_transactions_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE
);

-- Create indexes for affiliate_transactions table
CREATE INDEX IF NOT EXISTS idx_affiliate_transactions_affiliate_id ON affiliate_transactions(affiliate_id);
CREATE INDEX IF NOT EXISTS idx_affiliate_transactions_transaction_id ON affiliate_transactions(transaction_id);
CREATE INDEX IF NOT EXISTS idx_affiliate_transactions_status ON affiliate_transactions(status);
CREATE INDEX IF NOT EXISTS idx_affiliate_transactions_type ON affiliate_transactions(transaction_type);
CREATE INDEX IF NOT EXISTS idx_affiliate_transactions_created_at ON affiliate_transactions(created_at);
CREATE INDEX IF NOT EXISTS idx_affiliate_transactions_metadata ON affiliate_transactions USING GIN(metadata);

-- Affiliate Promotions table - Track all level promotions and their history
CREATE TABLE IF NOT EXISTS affiliate_promotions (
    id BIGSERIAL PRIMARY KEY,
    
    -- Promotion details
    affiliate_id BIGINT NOT NULL,
    from_level affiliate_level_type,
    to_level affiliate_level_type NOT NULL,
    
    -- Qualification metrics at time of promotion
    direct_referrals_count INTEGER,
    total_downlines_count INTEGER,
    total_consumption DECIMAL(10,2),
    
    -- Promotion trigger
    promotion_type promotion_type_enum DEFAULT 'AUTOMATIC',
    triggered_by_user_id BIGINT, -- Admin user who triggered manual promotion
    notes TEXT,
    
    -- President independence handling
    became_president_independent BOOLEAN DEFAULT FALSE,
    affected_uplines_count INTEGER, -- Number of uplines affected if became President
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_affiliate_promotions_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE
);

-- Create indexes for affiliate_promotions table
CREATE INDEX IF NOT EXISTS idx_affiliate_promotions_affiliate_id ON affiliate_promotions(affiliate_id);
CREATE INDEX IF NOT EXISTS idx_affiliate_promotions_to_level ON affiliate_promotions(to_level);
CREATE INDEX IF NOT EXISTS idx_affiliate_promotions_type ON affiliate_promotions(promotion_type);
CREATE INDEX IF NOT EXISTS idx_affiliate_promotions_created_at ON affiliate_promotions(created_at);
CREATE INDEX IF NOT EXISTS idx_affiliate_promotions_president_independent ON affiliate_promotions(became_president_independent);

-- Affiliate Statistics table - Cached statistics for performance
CREATE TABLE IF NOT EXISTS affiliate_statistics (
    id BIGSERIAL PRIMARY KEY,
    
    -- Affiliate reference
    affiliate_id BIGINT NOT NULL UNIQUE,
    
    -- Cached counts (updated periodically)
    direct_referrals_count INTEGER DEFAULT 0,
    total_downlines_count INTEGER DEFAULT 0,
    max_downline_depth INTEGER DEFAULT 0,
    
    -- Commission totals
    total_commissions_earned DECIMAL(12,2) DEFAULT 0.00,
    total_commissions_paid DECIMAL(12,2) DEFAULT 0.00,
    pending_commissions DECIMAL(12,2) DEFAULT 0.00,
    
    -- Performance metrics
    total_transactions_generated DECIMAL(12,2) DEFAULT 0.00,
    last_transaction_date TIMESTAMP WITH TIME ZONE,
    
    -- President independence impact
    blocked_commission_amount DECIMAL(12,2) DEFAULT 0.00, -- Amount blocked due to President in upline
    
    -- Last update timestamp
    last_calculated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_affiliate_statistics_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE
);

-- Create indexes for affiliate_statistics table
CREATE INDEX IF NOT EXISTS idx_affiliate_statistics_affiliate_id ON affiliate_statistics(affiliate_id);
CREATE INDEX IF NOT EXISTS idx_affiliate_statistics_last_calculated ON affiliate_statistics(last_calculated_at);

-- Commission Calculation Log table - Track commission calculation runs for debugging
CREATE TABLE IF NOT EXISTS commission_calculation_logs (
    id BIGSERIAL PRIMARY KEY,
    
    -- Calculation run details
    calculation_run_id UUID NOT NULL, -- UUID for grouping related calculations
    transaction_id VARCHAR(255),
    
    -- Calculation details
    payer_affiliate_id BIGINT NOT NULL,
    recipient_affiliate_id BIGINT NOT NULL,
    transaction_amount DECIMAL(10,2) NOT NULL,
    commission_amount DECIMAL(10,2) NOT NULL,
    commission_type commission_type_enum NOT NULL,
    generation_level INTEGER,
    percentage_applied DECIMAL(5,4),
    
    -- President independence tracking
    president_independence_applied BOOLEAN DEFAULT FALSE,
    blocked_by_president_id BIGINT,
    
    -- Status
    status calculation_status_enum NOT NULL,
    error_message TEXT,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_calculation_logs_payer FOREIGN KEY (payer_affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    CONSTRAINT fk_calculation_logs_recipient FOREIGN KEY (recipient_affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    CONSTRAINT fk_calculation_logs_blocked_by FOREIGN KEY (blocked_by_president_id) REFERENCES affiliates(id) ON DELETE SET NULL
);

-- Create indexes for commission_calculation_logs table
CREATE INDEX IF NOT EXISTS idx_calculation_logs_run_id ON commission_calculation_logs(calculation_run_id);
CREATE INDEX IF NOT EXISTS idx_calculation_logs_transaction_id ON commission_calculation_logs(transaction_id);
CREATE INDEX IF NOT EXISTS idx_calculation_logs_payer_id ON commission_calculation_logs(payer_affiliate_id);
CREATE INDEX IF NOT EXISTS idx_calculation_logs_recipient_id ON commission_calculation_logs(recipient_affiliate_id);
CREATE INDEX IF NOT EXISTS idx_calculation_logs_status ON commission_calculation_logs(status);
CREATE INDEX IF NOT EXISTS idx_calculation_logs_created_at ON commission_calculation_logs(created_at);

-- ===================================================================
-- ADDITIONAL POSTGRESQL OPTIMIZATIONS
-- ===================================================================

-- Add partial indexes for better performance on common queries
CREATE INDEX IF NOT EXISTS idx_affiliates_active_referrers ON affiliates(id) 
    WHERE referrer_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_commissions_pending ON commissions(recipient_id, created_at) 
    WHERE status = 'PENDING';

CREATE INDEX IF NOT EXISTS idx_transactions_pending_commission ON affiliate_transactions(affiliate_id, created_at) 
    WHERE status IN ('CONFIRMED', 'PENDING');

-- Add check constraints for data integrity
ALTER TABLE affiliates ADD CONSTRAINT check_total_consumption_positive 
    CHECK (total_consumption >= 0);

ALTER TABLE commissions ADD CONSTRAINT check_commission_amount_positive 
    CHECK (amount > 0);

ALTER TABLE commissions ADD CONSTRAINT check_generation_level_valid 
    CHECK (generation_level IS NULL OR (generation_level >= 1 AND generation_level <= 4));

ALTER TABLE affiliate_transactions ADD CONSTRAINT check_transaction_amount_positive 
    CHECK (amount > 0);

-- ===================================================================
-- POSTGRESQL-SPECIFIC FUNCTIONS AND PROCEDURES
-- ===================================================================

-- Function to get affiliate hierarchy (recursive)
CREATE OR REPLACE FUNCTION get_affiliate_upline(affiliate_id BIGINT, max_levels INTEGER DEFAULT 10)
RETURNS TABLE(level INTEGER, affiliate_id BIGINT, email VARCHAR, affiliate_level affiliate_level_type) AS $$
WITH RECURSIVE upline AS (
    -- Base case: the affiliate itself
    SELECT 0 as level, a.id, a.email, a.affiliate_level, a.referrer_id
    FROM affiliates a 
    WHERE a.id = $1
    
    UNION ALL
    
    -- Recursive case: get referrers up the chain
    SELECT u.level + 1, a.id, a.email, a.affiliate_level, a.referrer_id
    FROM affiliates a
    INNER JOIN upline u ON a.id = u.referrer_id
    WHERE u.level < $2 AND a.id IS NOT NULL
)
SELECT u.level, u.id, u.email, u.affiliate_level 
FROM upline u
ORDER BY u.level;
$$ LANGUAGE SQL STABLE;

-- Function to get affiliate downline count
CREATE OR REPLACE FUNCTION get_downline_count(affiliate_id BIGINT)
RETURNS INTEGER AS $$
WITH RECURSIVE downline AS (
    -- Base case: direct referrals
    SELECT a.id
    FROM affiliates a 
    WHERE a.referrer_id = $1
    
    UNION ALL
    
    -- Recursive case: referrals of referrals
    SELECT a.id
    FROM affiliates a
    INNER JOIN downline d ON a.referrer_id = d.id
)
SELECT COUNT(*)::INTEGER FROM downline;
$$ LANGUAGE SQL STABLE;

-- Function to check if affiliate has president in upline
CREATE OR REPLACE FUNCTION has_president_in_upline(affiliate_id BIGINT)
RETURNS BOOLEAN AS $$
DECLARE
    has_president BOOLEAN := FALSE;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM get_affiliate_upline($1) 
        WHERE affiliate_level = 'PRESIDENT' AND level > 0
    ) INTO has_president;
    
    RETURN has_president;
END;
$$ LANGUAGE plpgsql STABLE;

-- ===================================================================
-- INTEGRATION WITH EXISTING SCHEMA
-- ===================================================================

-- Link affiliates to existing users table (if you want to integrate)
-- ALTER TABLE affiliates ADD COLUMN user_id UUID;
-- ALTER TABLE affiliates ADD CONSTRAINT fk_affiliates_user 
--     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Update existing users table to reference affiliate system (optional)
-- ALTER TABLE users ADD COLUMN affiliate_id BIGINT;
-- ALTER TABLE users ADD CONSTRAINT fk_users_affiliate 
--     FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE SET NULL;

-- ===================================================================
-- INITIAL DATA AND CONFIGURATION
-- ===================================================================

-- Create system_configurations table if it doesn't exist
CREATE TABLE IF NOT EXISTS system_configurations (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(255) UNIQUE NOT NULL,
    config_value TEXT NOT NULL,
    data_type VARCHAR(50) DEFAULT 'string',
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Insert system configuration for affiliate system
INSERT INTO system_configurations (config_key, config_value, data_type, description) VALUES
('affiliate_system_enabled', 'true', 'boolean', 'Enable/disable the affiliate system'),
('affiliate_promotion_check_interval', '24', 'number', 'Hours between promotion eligibility checks'),
('affiliate_commission_calculation_enabled', 'true', 'boolean', 'Enable automatic commission calculations'),
('affiliate_president_independence_enabled', 'true', 'boolean', 'Enable President independence rule'),
('affiliate_max_generation_levels', '4', 'number', 'Maximum generation levels for commission calculations'),
('affiliate_leadership_bonus_enabled', 'true', 'boolean', 'Enable leadership bonuses for qualifying levels'),
('affiliate_min_transaction_amount', '1.00', 'number', 'Minimum transaction amount for commission calculation'),
('affiliate_commission_payout_threshold', '10.00', 'number', 'Minimum commission amount before payout')
ON CONFLICT (config_key) DO NOTHING;

-- ===================================================================
-- SAMPLE DATA FOR TESTING
-- ===================================================================

-- Insert sample affiliate levels for testing (optional)
INSERT INTO affiliates (email, first_name, last_name, affiliate_level, total_consumption) VALUES
('president@example.com', 'John', 'President', 'PRESIDENT', 5000.00),
('influencer@example.com', 'Jane', 'Influencer', 'INFLUENCER', 3000.00),
('leader@example.com', 'Bob', 'Leader', 'LEADER', 2000.00),
('promoter@example.com', 'Alice', 'Promoter', 'PROMOTER', 1000.00),
('reader@example.com', 'Charlie', 'Reader', 'READER', 1680.00),
('subscriber@example.com', 'Diana', 'Subscriber', 'SUBSCRIBER', 1680.00),
('fan@example.com', 'Eve', 'Fan', 'FAN', 0.00)
ON CONFLICT (email) DO NOTHING;

-- Set up referral hierarchy for testing
DO $$
DECLARE
    president_id BIGINT;
    influencer_id BIGINT;
    leader_id BIGINT;
    promoter_id BIGINT;
    reader_id BIGINT;
    subscriber_id BIGINT;
BEGIN
    SELECT id INTO president_id FROM affiliates WHERE email = 'president@example.com';
    SELECT id INTO influencer_id FROM affiliates WHERE email = 'influencer@example.com';
    SELECT id INTO leader_id FROM affiliates WHERE email = 'leader@example.com';
    SELECT id INTO promoter_id FROM affiliates WHERE email = 'promoter@example.com';
    SELECT id INTO reader_id FROM affiliates WHERE email = 'reader@example.com';
    SELECT id INTO subscriber_id FROM affiliates WHERE email = 'subscriber@example.com';

    UPDATE affiliates SET referrer_id = president_id WHERE email = 'influencer@example.com';
    UPDATE affiliates SET referrer_id = influencer_id WHERE email = 'leader@example.com';
    UPDATE affiliates SET referrer_id = leader_id WHERE email = 'promoter@example.com';
    UPDATE affiliates SET referrer_id = promoter_id WHERE email = 'reader@example.com';
    UPDATE affiliates SET referrer_id = reader_id WHERE email = 'subscriber@example.com';
    UPDATE affiliates SET referrer_id = subscriber_id WHERE email = 'fan@example.com';
END $$;

-- Mark President as independent
UPDATE affiliates SET is_president_independent = TRUE WHERE email = 'president@example.com';

-- ===================================================================
-- POSTGRESQL PERFORMANCE OPTIMIZATIONS
-- ===================================================================

-- Enable auto-vacuum for better maintenance
ALTER TABLE affiliates SET (autovacuum_enabled = true);
ALTER TABLE commissions SET (autovacuum_enabled = true);
ALTER TABLE affiliate_transactions SET (autovacuum_enabled = true);
ALTER TABLE affiliate_promotions SET (autovacuum_enabled = true);
ALTER TABLE affiliate_statistics SET (autovacuum_enabled = true);
ALTER TABLE commission_calculation_logs SET (autovacuum_enabled = true);

-- Create statistics for better query planning
ANALYZE affiliates;
ANALYZE commissions;
ANALYZE affiliate_transactions;
ANALYZE affiliate_promotions;
ANALYZE affiliate_statistics;
ANALYZE commission_calculation_logs;

-- ===================================================================
-- GRANT PERMISSIONS (adjust based on your application user)
-- ===================================================================

-- Grant permissions to application user (replace 'bigwater_app' with your actual user)
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO bigwater_app;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO bigwater_app;
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO bigwater_app;