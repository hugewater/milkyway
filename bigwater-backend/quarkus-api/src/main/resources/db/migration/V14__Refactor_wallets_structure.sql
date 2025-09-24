-- V14: Create new wallets table with TRON and POLYGON addresses
-- Keep existing usdt_wallets table for backward compatibility

-- Create new wallets table with updated structure
CREATE TABLE IF NOT EXISTS wallets (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    wallet_name VARCHAR(100) DEFAULT 'Primary Wallet',
    tron_address VARCHAR(255), -- TRON network address
    polygon_address VARCHAR(255), -- POLYGON network address
    balance NUMERIC(20,8) NOT NULL DEFAULT 0.00000000,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Ensure one wallet per user
    UNIQUE(user_id)
);

-- Create indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_wallets_user_id ON wallets (user_id);
CREATE INDEX IF NOT EXISTS idx_wallets_tron_address ON wallets (tron_address);
CREATE INDEX IF NOT EXISTS idx_wallets_polygon_address ON wallets (polygon_address);
CREATE INDEX IF NOT EXISTS idx_wallets_is_active ON wallets (is_active);

-- Migrate existing data - create one wallet per user, keeping the first wallet's balance
-- Only insert if no wallet exists for this user
INSERT INTO wallets (user_id, wallet_name, tron_address, balance, is_active, created_at, updated_at)
SELECT DISTINCT ON (uw.user_id) 
    uw.user_id,
    'Primary Wallet',
    uw.wallet_address, -- Use existing address as TRON address temporarily
    uw.balance,
    uw.is_active,
    uw.created_at,
    uw.updated_at
FROM usdt_wallets uw
WHERE NOT EXISTS (
    SELECT 1 FROM wallets w WHERE w.user_id = uw.user_id
)
ORDER BY uw.user_id, uw.created_at ASC;

-- Add triggers to automatically update timestamps
CREATE OR REPLACE FUNCTION update_wallet_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER wallet_update_timestamp
    BEFORE UPDATE ON wallets
    FOR EACH ROW
    EXECUTE FUNCTION update_wallet_timestamp();

-- Note: Keep usdt_wallets table for backward compatibility
-- The new wallets table will be used for new wallet functionality
-- Legacy code can continue to use usdt_wallets until migration is complete
