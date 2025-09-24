-- BigWater Database Schema (PostgreSQL)
-- Version: 1.0.0
-- Description: Initial database schema for BigWater Digital Weekly Journal

-- =====================================================
-- USERS TABLE
-- =====================================================
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    role TEXT NOT NULL DEFAULT 'SUBSCRIBER' CHECK (role IN ('SUBSCRIBER','ADMIN','SUPER_ADMIN')),
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED')),
    level TEXT NOT NULL DEFAULT 'bronze' CHECK (level IN ('bronze','silver','gold','platinum')),
    referral_code VARCHAR(20) NOT NULL UNIQUE,
    referred_by_code VARCHAR(20) DEFAULT 'COMPANY001', -- Default company referral
    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    email_verified_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email ON users (email);
CREATE INDEX idx_referral_code ON users (referral_code);
CREATE INDEX idx_referred_by_code ON users (referred_by_code);
CREATE INDEX idx_status ON users (status);
CREATE INDEX idx_role ON users (role);
CREATE INDEX idx_level ON users (level);
CREATE INDEX idx_join_date ON users (join_date);

-- =====================================================
-- USDT WALLETS TABLE
-- =====================================================
CREATE TABLE usdt_wallets (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    wallet_address VARCHAR(255) NOT NULL UNIQUE,
    wallet_name VARCHAR(100) DEFAULT 'Main Wallet',
    wallet_type TEXT NOT NULL DEFAULT 'MAIN' CHECK (wallet_type IN ('MAIN','TRADING','STAKING','REWARDS','COMPANY','MEMBER','TESTING')),
    balance NUMERIC(20,8) NOT NULL DEFAULT 0.00000000,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_wallets_user_id ON usdt_wallets (user_id);
CREATE INDEX idx_wallets_wallet_address ON usdt_wallets (wallet_address);
CREATE INDEX idx_wallets_wallet_type ON usdt_wallets (wallet_type);
CREATE INDEX idx_wallets_is_active ON usdt_wallets (is_active);

-- =====================================================
-- JOURNALS TABLE
-- =====================================================
CREATE TABLE journals (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    week_number INT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    excerpt TEXT,
    content TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT','PUBLISHED','SCHEDULED')),
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    tags VARCHAR(500),
    views INT NOT NULL DEFAULT 0,
    publish_date TIMESTAMP NULL,
    created_by BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_week_number ON journals (week_number);
CREATE INDEX idx_journals_status ON journals (status);
CREATE INDEX idx_featured ON journals (featured);
CREATE INDEX idx_publish_date ON journals (publish_date);
CREATE INDEX idx_journals_created_by ON journals (created_by);

-- =====================================================
-- JOURNAL VIEWS TABLE
-- =====================================================
CREATE TABLE journal_views (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    journal_id BIGINT NOT NULL REFERENCES journals(id) ON DELETE CASCADE,
    user_id BIGINT NULL REFERENCES users(id) ON DELETE SET NULL, -- NULL for anonymous views
    ip_address VARCHAR(45),
    user_agent TEXT,
    viewed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_journal_views_journal_id ON journal_views (journal_id);
CREATE INDEX idx_journal_views_user_id ON journal_views (user_id);
CREATE INDEX idx_journal_views_viewed_at ON journal_views (viewed_at);

-- =====================================================
-- RANDOM DRAWINGS TABLE
-- =====================================================
CREATE TABLE random_drawings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    drawing_name VARCHAR(255) NOT NULL,
    drawing_type TEXT NOT NULL CHECK (drawing_type IN ('POWERBALL','LOTTERY','RAFFLE')),
    week_number INT NOT NULL,
    drawing_date TIMESTAMP NOT NULL,
    prize_pool NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    winning_numbers JSONB,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','COMPLETED','CANCELLED')),
    total_participants INT NOT NULL DEFAULT 0,
    created_by BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_random_drawings_type ON random_drawings (drawing_type);
CREATE INDEX idx_random_drawings_week_number ON random_drawings (week_number);
CREATE INDEX idx_random_drawings_drawing_date ON random_drawings (drawing_date);
CREATE INDEX idx_random_drawings_status ON random_drawings (status);
CREATE INDEX idx_random_drawings_created_by ON random_drawings (created_by);

-- =====================================================
-- DRAWING PARTICIPANTS TABLE
-- =====================================================
CREATE TABLE drawing_participants (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    drawing_id BIGINT NOT NULL REFERENCES random_drawings(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    participant_numbers JSONB,
    entry_fee NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    is_winner BOOLEAN NOT NULL DEFAULT FALSE,
    prize_amount NUMERIC(15,2) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_drawing_user UNIQUE (drawing_id, user_id)
);

CREATE INDEX idx_drawing_participants_drawing_id ON drawing_participants (drawing_id);
CREATE INDEX idx_drawing_participants_user_id ON drawing_participants (user_id);
CREATE INDEX idx_drawing_participants_is_winner ON drawing_participants (is_winner);

-- =====================================================
-- CERTIFICATES TABLE
-- =====================================================
CREATE TABLE certificates (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    certificate_name VARCHAR(255) NOT NULL,
    certificate_type TEXT NOT NULL CHECK (certificate_type IN ('BRONZE','SILVER','GOLD','PLATINUM','DIAMOND')),
    description TEXT,
    price_usdt NUMERIC(15,2) NOT NULL,
    duration_days INT NOT NULL DEFAULT 365,
    benefits JSONB,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    max_supply INT NULL,
    current_supply INT NOT NULL DEFAULT 0,
    created_by BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cert_certificate_type ON certificates (certificate_type);
CREATE INDEX idx_cert_price_usdt ON certificates (price_usdt);
CREATE INDEX idx_cert_is_active ON certificates (is_active);
CREATE INDEX idx_cert_created_by ON certificates (created_by);

-- =====================================================
-- USER CERTIFICATES TABLE
-- =====================================================
CREATE TABLE user_certificates (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    certificate_id BIGINT NOT NULL REFERENCES certificates(id) ON DELETE CASCADE,
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','EXPIRED','CANCELLED')),
    purchase_amount_usdt NUMERIC(15,2) NOT NULL,
    transaction_hash VARCHAR(255),
    wallet_id BIGINT NULL REFERENCES usdt_wallets(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_cert_user_id ON user_certificates (user_id);
CREATE INDEX idx_user_cert_certificate_id ON user_certificates (certificate_id);
CREATE INDEX idx_user_cert_status ON user_certificates (status);
CREATE INDEX idx_user_cert_expiry_date ON user_certificates (expiry_date);
CREATE INDEX idx_user_cert_purchase_date ON user_certificates (purchase_date);

-- =====================================================
-- ACCESS LOGS TABLE
-- =====================================================
CREATE TABLE access_logs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NULL REFERENCES users(id) ON DELETE SET NULL,
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT,
    request_method VARCHAR(10) NOT NULL,
    request_url VARCHAR(500) NOT NULL,
    request_params TEXT,
    response_status INT NOT NULL,
    response_time_ms INT,
    session_id VARCHAR(255),
    accessed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_access_logs_user_id ON access_logs (user_id);
CREATE INDEX idx_access_logs_ip ON access_logs (ip_address);
CREATE INDEX idx_access_logs_method ON access_logs (request_method);
CREATE INDEX idx_access_logs_status ON access_logs (response_status);
CREATE INDEX idx_access_logs_accessed_at ON access_logs (accessed_at);

-- =====================================================
-- ERROR LOGS TABLE
-- =====================================================
CREATE TABLE error_logs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NULL REFERENCES users(id) ON DELETE SET NULL,
    error_level TEXT NOT NULL CHECK (error_level IN ('DEBUG','INFO','WARNING','ERROR','CRITICAL')),
    error_type VARCHAR(100) NOT NULL,
    error_message TEXT NOT NULL,
    stack_trace TEXT,
    request_url VARCHAR(500),
    request_method VARCHAR(10),
    ip_address VARCHAR(45),
    user_agent TEXT,
    additional_data JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_error_logs_user_id ON error_logs (user_id);
CREATE INDEX idx_error_logs_error_level ON error_logs (error_level);
CREATE INDEX idx_error_logs_error_type ON error_logs (error_type);
CREATE INDEX idx_error_logs_created_at ON error_logs (created_at);

-- =====================================================
-- REFERRAL RELATIONSHIPS TABLE
-- =====================================================
CREATE TABLE referral_relationships (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    referrer_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    referred_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    referral_level INT NOT NULL DEFAULT 1,
    commission_rate NUMERIC(5,4) NOT NULL DEFAULT 0.1000,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE','CANCELLED')),
    total_earnings NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_referrer_referred UNIQUE (referrer_id, referred_id)
);

CREATE INDEX idx_referral_referrer_id ON referral_relationships (referrer_id);
CREATE INDEX idx_referral_referred_id ON referral_relationships (referred_id);
CREATE INDEX idx_referral_level ON referral_relationships (referral_level);
CREATE INDEX idx_referral_status ON referral_relationships (status);

-- =====================================================
-- TRANSACTIONS TABLE
-- =====================================================
CREATE TABLE transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    wallet_id BIGINT NULL REFERENCES usdt_wallets(id) ON DELETE SET NULL,
    transaction_type TEXT NOT NULL CHECK (transaction_type IN ('DEPOSIT','WITHDRAWAL','PURCHASE','REFUND','REWARD','COMMISSION')),
    amount_usdt NUMERIC(20,8) NOT NULL,
    transaction_hash VARCHAR(255),
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','COMPLETED','FAILED','CANCELLED')),
    description TEXT,
    related_entity_type VARCHAR(50),
    related_entity_id BIGINT,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_user_id ON transactions (user_id);
CREATE INDEX idx_transactions_wallet_id ON transactions (wallet_id);
CREATE INDEX idx_transactions_type ON transactions (transaction_type);
CREATE INDEX idx_transactions_status ON transactions (status);
CREATE INDEX idx_transactions_created_at ON transactions (created_at);

-- =====================================================
-- SYSTEM SETTINGS TABLE
-- =====================================================
CREATE TABLE system_settings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    setting_type TEXT NOT NULL DEFAULT 'STRING' CHECK (setting_type IN ('STRING','NUMBER','BOOLEAN','JSON')),
    description TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_system_settings_is_public ON system_settings (is_public);

-- =====================================================
-- INSERT DEFAULT DATA
-- =====================================================

-- Insert default company referral code
INSERT INTO users (email, password_hash, first_name, last_name, role, referral_code, referred_by_code)
VALUES ('company@bigwater.com', '$2a$10$dummy.hash.for.company', 'BigWater', 'Company', 'SUPER_ADMIN', 'COMPANY001', NULL)
ON CONFLICT (email) DO NOTHING;

-- Insert default system settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description, is_public) VALUES
('company_referral_code', 'COMPANY001', 'STRING', 'Default company referral code', TRUE),
('default_commission_rate', '0.1000', 'NUMBER', 'Default referral commission rate (10%)', TRUE),
('min_withdrawal_amount', '10.00', 'NUMBER', 'Minimum withdrawal amount in USDT', TRUE),
('max_referral_levels', '3', 'NUMBER', 'Maximum referral levels for commission', TRUE),
('drawing_entry_fee', '1.00', 'NUMBER', 'Default entry fee for drawings', TRUE),
('system_maintenance_mode', 'false', 'BOOLEAN', 'System maintenance mode', TRUE)
ON CONFLICT (setting_key) DO NOTHING;
