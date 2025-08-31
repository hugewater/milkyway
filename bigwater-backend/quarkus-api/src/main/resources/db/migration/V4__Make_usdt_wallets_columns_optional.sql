-- Make usdt_wallets table columns mostly optional
-- This migration modifies the usdt_wallets table to make most columns nullable

-- Modify wallet_name to be nullable
ALTER TABLE usdt_wallets MODIFY COLUMN wallet_name VARCHAR(100) NULL;

-- Modify wallet_type to be nullable with default
ALTER TABLE usdt_wallets MODIFY COLUMN wallet_type ENUM('MAIN', 'TRADING', 'STAKING', 'REWARDS') NULL DEFAULT 'MAIN';

-- Modify balance to be nullable with default
ALTER TABLE usdt_wallets MODIFY COLUMN balance DECIMAL(20,8) NULL DEFAULT 0.00000000;

-- Modify is_active to be nullable with default
ALTER TABLE usdt_wallets MODIFY COLUMN is_active BOOLEAN NULL DEFAULT TRUE;

-- Modify is_verified to be nullable with default
ALTER TABLE usdt_wallets MODIFY COLUMN is_verified BOOLEAN NULL DEFAULT FALSE;

-- Note: user_id and wallet_address remain NOT NULL as they are essential for wallet identification
-- Note: created_at and updated_at remain NOT NULL as they are managed by the application
