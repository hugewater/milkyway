-- BigWater Digital Weekly Journal Database Schema
-- Comprehensive database design for the web application

-- Users table - Core user information
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'subscriber' CHECK (role IN ('subscriber', 'admin', 'super_admin')),
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'suspended')),
    subscription_type VARCHAR(20) DEFAULT 'standard' CHECK (subscription_type IN ('standard', 'double')),
    star_level INTEGER DEFAULT 1 CHECK (star_level >= 1 AND star_level <= 5),
    referral_code VARCHAR(20) UNIQUE NOT NULL,
    referred_by_code VARCHAR(20),
    join_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_login TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Foreign key constraint for referral system
    FOREIGN KEY (referred_by_code) REFERENCES users(referral_code)
);

-- User profiles table - Extended user information
CREATE TABLE user_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    date_of_birth DATE,
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    timezone VARCHAR(50) DEFAULT 'UTC',
    language VARCHAR(10) DEFAULT 'en',
    notification_preferences JSONB DEFAULT '{"email": true, "sms": false}',
    profile_picture_url VARCHAR(500),
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Wallets table - User cryptocurrency wallets
CREATE TABLE wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    wallet_type VARCHAR(20) NOT NULL CHECK (wallet_type IN ('USDT', 'BTC', 'ETH')),
    wallet_address VARCHAR(255) NOT NULL,
    wallet_name VARCHAR(100),
    is_primary BOOLEAN DEFAULT FALSE,
    balance DECIMAL(18, 8) DEFAULT 0,
    last_balance_update TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(user_id, wallet_type, wallet_address)
);

-- Admin permissions table - Granular admin permissions
CREATE TABLE admin_permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    permission_type VARCHAR(50) NOT NULL CHECK (permission_type IN (
        'manage_users', 'manage_payments', 'manage_tokens', 
        'manage_drawings', 'manage_journals', 'manage_reports'
    )),
    granted_by UUID REFERENCES users(id),
    granted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(user_id, permission_type)
);

-- Journals table - Weekly journal content
CREATE TABLE journals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    week_number INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    excerpt TEXT,
    content TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'draft' CHECK (status IN ('draft', 'published', 'scheduled')),
    featured BOOLEAN DEFAULT FALSE,
    tags TEXT,
    views INTEGER DEFAULT 0,
    publish_date DATE,
    created_by UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(week_number)
);

-- Journal views table - Track user journal reading
CREATE TABLE journal_views (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    journal_id UUID NOT NULL REFERENCES journals(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    viewed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    reading_time_seconds INTEGER,
    
    UNIQUE(journal_id, user_id)
);

-- Referrals table - Track referral relationships and levels
CREATE TABLE referrals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    referrer_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    referred_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    referral_level INTEGER NOT NULL CHECK (referral_level >= 1 AND referral_level <= 3),
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive')),
    commission_rate DECIMAL(5, 4) DEFAULT 0.0000,
    total_earnings DECIMAL(18, 8) DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(referrer_id, referred_id)
);

-- Rewards table - User reward transactions
CREATE TABLE rewards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reward_type VARCHAR(50) NOT NULL CHECK (reward_type IN (
        'referral_commission', 'level_bonus', 'drawing_prize', 'subscription_reward'
    )),
    amount DECIMAL(18, 8) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    description TEXT,
    transaction_id VARCHAR(255),
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'failed')),
    earned_from UUID REFERENCES users(id), -- The user who generated this reward
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    processed_at TIMESTAMP WITH TIME ZONE
);

-- Powerball numbers table - User's weekly number selections
CREATE TABLE powerball_numbers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    week_number INTEGER NOT NULL,
    set_number INTEGER NOT NULL CHECK (set_number >= 1),
    number1 INTEGER NOT NULL CHECK (number1 >= 1 AND number1 <= 69),
    number2 INTEGER NOT NULL CHECK (number2 >= 1 AND number2 <= 69),
    number3 INTEGER NOT NULL CHECK (number3 >= 1 AND number3 <= 69),
    number4 INTEGER NOT NULL CHECK (number4 >= 1 AND number4 <= 69),
    number5 INTEGER NOT NULL CHECK (number5 >= 1 AND number5 <= 69),
    powerball INTEGER NOT NULL CHECK (powerball >= 1 AND powerball <= 26),
    is_custom BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(user_id, week_number, set_number)
);

-- Drawings table - Weekly drawing results
CREATE TABLE drawings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    week_number INTEGER UNIQUE NOT NULL,
    drawing_date DATE NOT NULL,
    winning_number1 INTEGER CHECK (winning_number1 >= 1 AND winning_number1 <= 69),
    winning_number2 INTEGER CHECK (winning_number2 >= 1 AND winning_number2 <= 69),
    winning_number3 INTEGER CHECK (winning_number3 >= 1 AND winning_number3 <= 69),
    winning_number4 INTEGER CHECK (winning_number4 >= 1 AND winning_number4 <= 69),
    winning_number5 INTEGER CHECK (winning_number5 >= 1 AND winning_number5 <= 69),
    winning_powerball INTEGER CHECK (winning_powerball >= 1 AND winning_powerball <= 26),
    total_prize_pool DECIMAL(18, 8) DEFAULT 1000000.00,
    status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'drawn', 'completed')),
    drawn_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    drawn_at TIMESTAMP WITH TIME ZONE
);

-- Winnings table - User winnings from drawings
CREATE TABLE winnings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    drawing_id UUID NOT NULL REFERENCES drawings(id) ON DELETE CASCADE,
    powerball_numbers_id UUID NOT NULL REFERENCES powerball_numbers(id) ON DELETE CASCADE,
    matches INTEGER NOT NULL CHECK (matches >= 0 AND matches <= 6),
    powerball_match BOOLEAN DEFAULT FALSE,
    prize_amount DECIMAL(18, 8) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    claimed BOOLEAN DEFAULT FALSE,
    claimed_at TIMESTAMP WITH TIME ZONE,
    transaction_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Tokens table - Investment tokens for sale
CREATE TABLE tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token_name VARCHAR(100) NOT NULL,
    token_symbol VARCHAR(10) NOT NULL,
    description TEXT,
    price_per_token DECIMAL(18, 8) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    total_supply BIGINT NOT NULL,
    available_supply BIGINT NOT NULL,
    min_purchase_amount INTEGER DEFAULT 1,
    max_purchase_amount INTEGER,
    status VARCHAR(20) DEFAULT 'draft' CHECK (status IN ('draft', 'active', 'sold_out', 'ended')),
    sale_start_date TIMESTAMP WITH TIME ZONE,
    sale_end_date TIMESTAMP WITH TIME ZONE,
    image_url VARCHAR(500),
    whitepaper_url VARCHAR(500),
    created_by UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Token purchases table - User token purchase history
CREATE TABLE token_purchases (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_id UUID NOT NULL REFERENCES tokens(id) ON DELETE CASCADE,
    quantity BIGINT NOT NULL,
    price_per_token DECIMAL(18, 8) NOT NULL,
    total_amount DECIMAL(18, 8) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    payment_wallet_id UUID REFERENCES wallets(id),
    transaction_hash VARCHAR(255),
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'failed', 'refunded')),
    purchased_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    confirmed_at TIMESTAMP WITH TIME ZONE
);

-- Payment wallets table - Admin-managed payment wallets
CREATE TABLE payment_wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_name VARCHAR(100) NOT NULL,
    wallet_address VARCHAR(255) NOT NULL,
    wallet_type VARCHAR(20) NOT NULL CHECK (wallet_type IN ('USDT', 'BTC', 'ETH')),
    assignment_type VARCHAR(50) NOT NULL CHECK (assignment_type IN ('last_name_prefix', 'user_group', 'default')),
    assignment_value VARCHAR(100), -- Could be 'A-M', 'N-Z', or specific user IDs
    is_active BOOLEAN DEFAULT TRUE,
    balance DECIMAL(18, 8) DEFAULT 0,
    last_balance_update TIMESTAMP WITH TIME ZONE,
    managed_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Payment transactions table - All payment transactions
CREATE TABLE payment_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    payment_wallet_id UUID REFERENCES payment_wallets(id),
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN (
        'subscription_payment', 'token_purchase', 'reward_payout', 'drawing_prize'
    )),
    amount DECIMAL(18, 8) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    transaction_hash VARCHAR(255),
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'failed')),
    description TEXT,
    related_id UUID, -- Could reference tokens, drawings, etc.
    processed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Subscriptions table - User subscription history
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    subscription_type VARCHAR(20) NOT NULL CHECK (subscription_type IN ('standard', 'double')),
    week_number INTEGER NOT NULL,
    amount_paid DECIMAL(18, 8) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USDT',
    payment_transaction_id UUID REFERENCES payment_transactions(id),
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'expired', 'cancelled')),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    auto_renew BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Notifications table - System notifications
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN (
        'system', 'reward', 'drawing', 'subscription', 'referral', 'token'
    )),
    read BOOLEAN DEFAULT FALSE,
    action_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    read_at TIMESTAMP WITH TIME ZONE
);

-- System settings table - Application configuration
CREATE TABLE system_settings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT NOT NULL,
    setting_type VARCHAR(20) DEFAULT 'string' CHECK (setting_type IN ('string', 'number', 'boolean', 'json')),
    description TEXT,
    updated_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Audit logs table - Track important system changes
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(100),
    record_id UUID,
    old_values JSONB,
    new_values JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_referral_code ON users(referral_code);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_join_date ON users(join_date);

CREATE INDEX idx_wallets_user_id ON wallets(user_id);
CREATE INDEX idx_wallets_type ON wallets(wallet_type);

CREATE INDEX idx_journals_week_number ON journals(week_number);
CREATE INDEX idx_journals_status ON journals(status);
CREATE INDEX idx_journals_publish_date ON journals(publish_date);

CREATE INDEX idx_referrals_referrer_id ON referrals(referrer_id);
CREATE INDEX idx_referrals_referred_id ON referrals(referred_id);

CREATE INDEX idx_rewards_user_id ON rewards(user_id);
CREATE INDEX idx_rewards_type ON rewards(reward_type);
CREATE INDEX idx_rewards_created_at ON rewards(created_at);

CREATE INDEX idx_powerball_user_week ON powerball_numbers(user_id, week_number);
CREATE INDEX idx_drawings_week_number ON drawings(week_number);

CREATE INDEX idx_tokens_status ON tokens(status);
CREATE INDEX idx_token_purchases_user_id ON token_purchases(user_id);
CREATE INDEX idx_token_purchases_token_id ON token_purchases(token_id);

CREATE INDEX idx_payment_transactions_user_id ON payment_transactions(user_id);
CREATE INDEX idx_payment_transactions_type ON payment_transactions(transaction_type);
CREATE INDEX idx_payment_transactions_created_at ON payment_transactions(created_at);

CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_week_number ON subscriptions(week_number);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(read);

-- Row Level Security policies (example for journals)
ALTER TABLE journals ENABLE ROW LEVEL SECURITY;

-- Policy: Users can only see published journals after their join date
CREATE POLICY "users_can_view_published_journals" ON journals
    FOR SELECT TO authenticated
    USING (
        status = 'published' 
        AND publish_date >= (
            SELECT join_date::date 
            FROM users 
            WHERE id = auth.uid()
        )
    );

-- Policy: Only admins can modify journals
CREATE POLICY "admins_can_modify_journals" ON journals
    FOR ALL TO authenticated
    USING (
        EXISTS (
            SELECT 1 FROM users 
            WHERE id = auth.uid() 
            AND role IN ('admin', 'super_admin')
        )
    );

-- Initial system settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description) VALUES
('drawing_prize_pool', '1000000.00', 'number', 'Default weekly drawing prize pool in USDT'),
('standard_subscription_price', '50.00', 'number', 'Standard subscription price per week in USDT'),
('double_subscription_price', '100.00', 'number', 'Double subscription price per week in USDT'),
('referral_commission_rate', '0.10', 'number', 'Referral commission rate (10%)'),
('max_referral_levels', '3', 'number', 'Maximum referral levels for commission'),
('site_maintenance_mode', 'false', 'boolean', 'Enable/disable site maintenance mode'),
('min_withdrawal_amount', '10.00', 'number', 'Minimum withdrawal amount in USDT');