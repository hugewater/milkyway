-- BigWater Database Schema
-- Version: 1.0.0
-- Description: Initial database schema for BigWater Digital Weekly Journal

-- =====================================================
-- USERS TABLE
-- =====================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    role ENUM('SUBSCRIBER', 'ADMIN', 'SUPER_ADMIN') NOT NULL DEFAULT 'SUBSCRIBER',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    referral_code VARCHAR(20) NOT NULL UNIQUE,
    referred_by_code VARCHAR(20) DEFAULT 'COMPANY001', -- Default company referral
    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    email_verified_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_referral_code (referral_code),
    INDEX idx_referred_by_code (referred_by_code),
    INDEX idx_status (status),
    INDEX idx_role (role),
    INDEX idx_join_date (join_date)
);

-- =====================================================
-- USDT WALLETS TABLE
-- =====================================================
CREATE TABLE usdt_wallets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    wallet_address VARCHAR(255) NOT NULL UNIQUE,
    wallet_name VARCHAR(100) DEFAULT 'Main Wallet',
    wallet_type ENUM('MAIN', 'TRADING', 'STAKING', 'REWARDS') NOT NULL DEFAULT 'MAIN',
    balance DECIMAL(20,8) NOT NULL DEFAULT 0.00000000,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_wallet_address (wallet_address),
    INDEX idx_wallet_type (wallet_type),
    INDEX idx_is_active (is_active)
);

-- =====================================================
-- JOURNALS TABLE
-- =====================================================
CREATE TABLE journals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    week_number INT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    excerpt TEXT,
    content LONGTEXT NOT NULL,
    status ENUM('DRAFT', 'PUBLISHED', 'SCHEDULED') NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    tags VARCHAR(500),
    views INT NOT NULL DEFAULT 0,
    publish_date TIMESTAMP NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_week_number (week_number),
    INDEX idx_status (status),
    INDEX idx_featured (featured),
    INDEX idx_publish_date (publish_date),
    INDEX idx_created_by (created_by)
);

-- =====================================================
-- JOURNAL VIEWS TABLE
-- =====================================================
CREATE TABLE journal_views (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    journal_id BIGINT NOT NULL,
    user_id BIGINT NULL, -- NULL for anonymous views
    ip_address VARCHAR(45),
    user_agent TEXT,
    viewed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (journal_id) REFERENCES journals(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_journal_id (journal_id),
    INDEX idx_user_id (user_id),
    INDEX idx_viewed_at (viewed_at)
);

-- =====================================================
-- RANDOM DRAWINGS TABLE
-- =====================================================
CREATE TABLE random_drawings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drawing_name VARCHAR(255) NOT NULL,
    drawing_type ENUM('POWERBALL', 'LOTTERY', 'RAFFLE') NOT NULL,
    week_number INT NOT NULL,
    drawing_date TIMESTAMP NOT NULL,
    prize_pool DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    winning_numbers JSON, -- Store winning numbers as JSON
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_participants INT NOT NULL DEFAULT 0,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_drawing_type (drawing_type),
    INDEX idx_week_number (week_number),
    INDEX idx_drawing_date (drawing_date),
    INDEX idx_status (status),
    INDEX idx_created_by (created_by)
);

-- =====================================================
-- DRAWING PARTICIPANTS TABLE
-- =====================================================
CREATE TABLE drawing_participants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    drawing_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    participant_numbers JSON, -- Store participant's numbers as JSON
    entry_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    is_winner BOOLEAN NOT NULL DEFAULT FALSE,
    prize_amount DECIMAL(15,2) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (drawing_id) REFERENCES random_drawings(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_drawing_user (drawing_id, user_id),
    INDEX idx_drawing_id (drawing_id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_winner (is_winner)
);

-- =====================================================
-- CERTIFICATES TABLE
-- =====================================================
CREATE TABLE certificates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    certificate_name VARCHAR(255) NOT NULL,
    certificate_type ENUM('BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND') NOT NULL,
    description TEXT,
    price_usdt DECIMAL(15,2) NOT NULL,
    duration_days INT NOT NULL DEFAULT 365, -- Certificate validity in days
    benefits JSON, -- Store benefits as JSON
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    max_supply INT NULL, -- NULL for unlimited
    current_supply INT NOT NULL DEFAULT 0,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_certificate_type (certificate_type),
    INDEX idx_price_usdt (price_usdt),
    INDEX idx_is_active (is_active),
    INDEX idx_created_by (created_by)
);

-- =====================================================
-- USER CERTIFICATES TABLE
-- =====================================================
CREATE TABLE user_certificates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    certificate_id BIGINT NOT NULL,
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    purchase_amount_usdt DECIMAL(15,2) NOT NULL,
    transaction_hash VARCHAR(255), -- Blockchain transaction hash
    wallet_id BIGINT NULL, -- Which wallet was used for purchase
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (certificate_id) REFERENCES certificates(id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_id) REFERENCES usdt_wallets(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_certificate_id (certificate_id),
    INDEX idx_status (status),
    INDEX idx_expiry_date (expiry_date),
    INDEX idx_purchase_date (purchase_date)
);

-- =====================================================
-- ACCESS LOGS TABLE
-- =====================================================
CREATE TABLE access_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL, -- NULL for anonymous access
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT,
    request_method VARCHAR(10) NOT NULL,
    request_url VARCHAR(500) NOT NULL,
    request_params TEXT,
    response_status INT NOT NULL,
    response_time_ms INT, -- Response time in milliseconds
    session_id VARCHAR(255),
    accessed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_ip_address (ip_address),
    INDEX idx_request_method (request_method),
    INDEX idx_response_status (response_status),
    INDEX idx_accessed_at (accessed_at)
);

-- =====================================================
-- ERROR LOGS TABLE
-- =====================================================
CREATE TABLE error_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL, -- NULL for system errors
    error_level ENUM('DEBUG', 'INFO', 'WARNING', 'ERROR', 'CRITICAL') NOT NULL,
    error_type VARCHAR(100) NOT NULL,
    error_message TEXT NOT NULL,
    stack_trace LONGTEXT,
    request_url VARCHAR(500),
    request_method VARCHAR(10),
    ip_address VARCHAR(45),
    user_agent TEXT,
    additional_data JSON, -- Store additional error context as JSON
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_error_level (error_level),
    INDEX idx_error_type (error_type),
    INDEX idx_created_at (created_at)
);

-- =====================================================
-- REFERRAL RELATIONSHIPS TABLE
-- =====================================================
CREATE TABLE referral_relationships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    referrer_id BIGINT NOT NULL,
    referred_id BIGINT NOT NULL,
    referral_level INT NOT NULL DEFAULT 1, -- 1 = direct, 2 = indirect, etc.
    commission_rate DECIMAL(5,4) NOT NULL DEFAULT 0.1000, -- 10% default
    status ENUM('ACTIVE', 'INACTIVE', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    total_earnings DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (referrer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (referred_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_referrer_referred (referrer_id, referred_id),
    INDEX idx_referrer_id (referrer_id),
    INDEX idx_referred_id (referred_id),
    INDEX idx_referral_level (referral_level),
    INDEX idx_status (status)
);

-- =====================================================
-- TRANSACTIONS TABLE
-- =====================================================
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    wallet_id BIGINT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'PURCHASE', 'REFUND', 'REWARD', 'COMMISSION') NOT NULL,
    amount_usdt DECIMAL(20,8) NOT NULL,
    transaction_hash VARCHAR(255),
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    description TEXT,
    related_entity_type VARCHAR(50), -- 'certificate', 'drawing', 'referral', etc.
    related_entity_id BIGINT,
    metadata JSON, -- Store additional transaction data as JSON
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (wallet_id) REFERENCES usdt_wallets(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_wallet_id (wallet_id),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- =====================================================
-- SYSTEM SETTINGS TABLE
-- =====================================================
CREATE TABLE system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    setting_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') NOT NULL DEFAULT 'STRING',
    description TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_setting_key (setting_key),
    INDEX idx_is_public (is_public)
);

-- =====================================================
-- INSERT DEFAULT DATA
-- =====================================================

-- Insert default company referral code
INSERT INTO users (email, password_hash, first_name, last_name, role, referral_code, referred_by_code) 
VALUES ('company@bigwater.com', '$2a$10$dummy.hash.for.company', 'BigWater', 'Company', 'SUPER_ADMIN', 'COMPANY001', NULL);

-- Insert default system settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description, is_public) VALUES
('company_referral_code', 'COMPANY001', 'STRING', 'Default company referral code', TRUE),
('default_commission_rate', '0.1000', 'NUMBER', 'Default referral commission rate (10%)', TRUE),
('min_withdrawal_amount', '10.00', 'NUMBER', 'Minimum withdrawal amount in USDT', TRUE),
('max_referral_levels', '3', 'NUMBER', 'Maximum referral levels for commission', TRUE),
('drawing_entry_fee', '1.00', 'NUMBER', 'Default entry fee for drawings', TRUE),
('system_maintenance_mode', 'false', 'BOOLEAN', 'System maintenance mode', TRUE);
