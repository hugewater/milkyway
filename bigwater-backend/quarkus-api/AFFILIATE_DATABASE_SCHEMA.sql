-- ===================================================================
-- AFFILIATE SYSTEM DATABASE SCHEMA
-- ===================================================================
-- Additional tables needed for the comprehensive affiliate promotion system
-- This extends the existing database schema with affiliate-specific tables

-- Affiliates table - Core affiliate information with levels and promotion tracking
CREATE TABLE affiliates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    
    -- Affiliate level and hierarchy
    affiliate_level ENUM('FAN', 'SUBSCRIBER', 'READER', 'PROMOTER', 'LEADER', 'INFLUENCER', 'PRESIDENT') DEFAULT 'FAN',
    referrer_id BIGINT,
    
    -- Tracking fields
    total_consumption DECIMAL(10,2) DEFAULT 0.00,
    is_president_independent BOOLEAN DEFAULT FALSE,
    last_promotion_check DATETIME,
    
    -- Timestamps
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (referrer_id) REFERENCES affiliates(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_affiliate_level (affiliate_level),
    INDEX idx_referrer_id (referrer_id),
    INDEX idx_email (email),
    INDEX idx_president_independent (is_president_independent),
    INDEX idx_last_promotion_check (last_promotion_check)
);

-- Commissions table - Track all commission payments and calculations
CREATE TABLE commissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Commission recipient and source
    recipient_id BIGINT NOT NULL,
    source_affiliate_id BIGINT NOT NULL,
    
    -- Commission details
    amount DECIMAL(10,2) NOT NULL,
    commission_type ENUM('GENERATION', 'LEADERSHIP', 'DIRECT_REFERRAL') NOT NULL,
    generation_level INT, -- 1, 2, 3, 4 for generation commissions
    percentage_rate DECIMAL(5,4), -- The percentage rate used (e.g., 0.25 for 25%)
    
    -- Transaction reference
    transaction_id VARCHAR(255),
    transaction_amount DECIMAL(10,2), -- Original transaction amount
    
    -- Status tracking
    status ENUM('PENDING', 'CONFIRMED', 'PAID', 'CANCELLED') DEFAULT 'PENDING',
    
    -- Timestamps
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    confirmed_at DATETIME,
    paid_at DATETIME,
    
    -- Foreign key constraints
    FOREIGN KEY (recipient_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    FOREIGN KEY (source_affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    INDEX idx_recipient_id (recipient_id),
    INDEX idx_source_affiliate_id (source_affiliate_id),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_status (status),
    INDEX idx_commission_type (commission_type),
    INDEX idx_created_at (created_at)
);

-- Affiliate Transactions table - Track all qualifying transactions for commission calculations
CREATE TABLE affiliate_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Transaction details
    transaction_id VARCHAR(255) UNIQUE NOT NULL,
    affiliate_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    
    -- Transaction type and metadata
    transaction_type ENUM('PURCHASE', 'SUBSCRIPTION', 'CONSUMPTION', 'OTHER') NOT NULL,
    description TEXT,
    metadata JSON, -- Additional transaction data
    
    -- Status and processing
    status ENUM('PENDING', 'CONFIRMED', 'COMMISSION_CALCULATED', 'COMMISSION_PAID') DEFAULT 'PENDING',
    commission_calculated_at DATETIME,
    
    -- Timestamps
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    confirmed_at DATETIME,
    
    -- Foreign key constraints
    FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    INDEX idx_affiliate_id (affiliate_id),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_status (status),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_created_at (created_at)
);

-- Affiliate Promotions table - Track all level promotions and their history
CREATE TABLE affiliate_promotions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Promotion details
    affiliate_id BIGINT NOT NULL,
    from_level ENUM('FAN', 'SUBSCRIBER', 'READER', 'PROMOTER', 'LEADER', 'INFLUENCER', 'PRESIDENT'),
    to_level ENUM('FAN', 'SUBSCRIBER', 'READER', 'PROMOTER', 'LEADER', 'INFLUENCER', 'PRESIDENT') NOT NULL,
    
    -- Qualification metrics at time of promotion
    direct_referrals_count INT,
    total_downlines_count INT,
    total_consumption DECIMAL(10,2),
    
    -- Promotion trigger
    promotion_type ENUM('AUTOMATIC', 'MANUAL', 'ADMIN_OVERRIDE') DEFAULT 'AUTOMATIC',
    triggered_by_user_id BIGINT, -- Admin user who triggered manual promotion
    notes TEXT,
    
    -- President independence handling
    became_president_independent BOOLEAN DEFAULT FALSE,
    affected_uplines_count INT, -- Number of uplines affected if became President
    
    -- Timestamps
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    INDEX idx_affiliate_id (affiliate_id),
    INDEX idx_to_level (to_level),
    INDEX idx_promotion_type (promotion_type),
    INDEX idx_created_at (created_at),
    INDEX idx_became_president_independent (became_president_independent)
);

-- Affiliate Statistics table - Cached statistics for performance
CREATE TABLE affiliate_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Affiliate reference
    affiliate_id BIGINT NOT NULL UNIQUE,
    
    -- Cached counts (updated periodically)
    direct_referrals_count INT DEFAULT 0,
    total_downlines_count INT DEFAULT 0,
    max_downline_depth INT DEFAULT 0,
    
    -- Commission totals
    total_commissions_earned DECIMAL(12,2) DEFAULT 0.00,
    total_commissions_paid DECIMAL(12,2) DEFAULT 0.00,
    pending_commissions DECIMAL(12,2) DEFAULT 0.00,
    
    -- Performance metrics
    total_transactions_generated DECIMAL(12,2) DEFAULT 0.00,
    last_transaction_date DATETIME,
    
    -- President independence impact
    blocked_commission_amount DECIMAL(12,2) DEFAULT 0.00, -- Amount blocked due to President in upline
    
    -- Last update timestamp
    last_calculated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    INDEX idx_affiliate_id (affiliate_id),
    INDEX idx_last_calculated_at (last_calculated_at)
);

-- Commission Calculation Log table - Track commission calculation runs for debugging
CREATE TABLE commission_calculation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- Calculation run details
    calculation_run_id VARCHAR(36) NOT NULL, -- UUID for grouping related calculations
    transaction_id VARCHAR(255),
    
    -- Calculation details
    payer_affiliate_id BIGINT NOT NULL,
    recipient_affiliate_id BIGINT NOT NULL,
    transaction_amount DECIMAL(10,2) NOT NULL,
    commission_amount DECIMAL(10,2) NOT NULL,
    commission_type ENUM('GENERATION', 'LEADERSHIP') NOT NULL,
    generation_level INT,
    percentage_applied DECIMAL(5,4),
    
    -- President independence tracking
    president_independence_applied BOOLEAN DEFAULT FALSE,
    blocked_by_president_id BIGINT,
    
    -- Status
    status ENUM('CALCULATED', 'APPLIED', 'BLOCKED', 'ERROR') NOT NULL,
    error_message TEXT,
    
    -- Timestamps
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (payer_affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE,
    FOREIGN KEY (blocked_by_president_id) REFERENCES affiliates(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_calculation_run_id (calculation_run_id),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_payer_affiliate_id (payer_affiliate_id),
    INDEX idx_recipient_affiliate_id (recipient_affiliate_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- ===================================================================
-- INTEGRATION WITH EXISTING SCHEMA
-- ===================================================================

-- Link affiliates to existing users table (if you want to integrate)
-- ALTER TABLE affiliates ADD COLUMN user_id UUID;
-- ALTER TABLE affiliates ADD FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Update existing users table to reference affiliate system (optional)
-- ALTER TABLE users ADD COLUMN affiliate_id BIGINT;
-- ALTER TABLE users ADD FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE SET NULL;

-- ===================================================================
-- INITIAL DATA AND CONFIGURATION
-- ===================================================================

-- Insert system configuration for affiliate system
INSERT INTO system_configurations (config_key, config_value, data_type, description) VALUES
('affiliate_system_enabled', 'true', 'boolean', 'Enable/disable the affiliate system'),
('affiliate_promotion_check_interval', '24', 'number', 'Hours between promotion eligibility checks'),
('affiliate_commission_calculation_enabled', 'true', 'boolean', 'Enable automatic commission calculations'),
('affiliate_president_independence_enabled', 'true', 'boolean', 'Enable President independence rule'),
('affiliate_max_generation_levels', '4', 'number', 'Maximum generation levels for commission calculations'),
('affiliate_leadership_bonus_enabled', 'true', 'boolean', 'Enable leadership bonuses for qualifying levels'),
('affiliate_min_transaction_amount', '1.00', 'number', 'Minimum transaction amount for commission calculation'),
('affiliate_commission_payout_threshold', '10.00', 'number', 'Minimum commission amount before payout');

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
('fan@example.com', 'Eve', 'Fan', 'FAN', 0.00);

-- Set up referral hierarchy for testing
UPDATE affiliates SET referrer_id = (SELECT id FROM affiliates WHERE email = 'president@example.com') WHERE email = 'influencer@example.com';
UPDATE affiliates SET referrer_id = (SELECT id FROM affiliates WHERE email = 'influencer@example.com') WHERE email = 'leader@example.com';
UPDATE affiliates SET referrer_id = (SELECT id FROM affiliates WHERE email = 'leader@example.com') WHERE email = 'promoter@example.com';
UPDATE affiliates SET referrer_id = (SELECT id FROM affiliates WHERE email = 'promoter@example.com') WHERE email = 'reader@example.com';
UPDATE affiliates SET referrer_id = (SELECT id FROM affiliates WHERE email = 'reader@example.com') WHERE email = 'subscriber@example.com';
UPDATE affiliates SET referrer_id = (SELECT id FROM affiliates WHERE email = 'subscriber@example.com') WHERE email = 'fan@example.com';

-- Mark President as independent
UPDATE affiliates SET is_president_independent = TRUE WHERE email = 'president@example.com';