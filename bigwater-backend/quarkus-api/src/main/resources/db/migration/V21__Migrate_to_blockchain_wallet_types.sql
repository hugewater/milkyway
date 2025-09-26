-- V21: Migrate to blockchain wallet types
-- This migration updates existing wallet types to the new blockchain network types.

BEGIN;

-- Drop any existing wallet_type constraints on usdt_wallets
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.table_constraints 
               WHERE table_name = 'usdt_wallets' 
               AND constraint_name LIKE 'ck_wallet_type%') THEN
        
        -- Find and drop the exact constraint name
        DECLARE
            constraint_name text;
        BEGIN
            SELECT tc.constraint_name INTO constraint_name
            FROM information_schema.table_constraints tc
            WHERE tc.table_name = 'usdt_wallets' 
            AND tc.constraint_name LIKE 'ck_wallet_type%'
            LIMIT 1;
            
            IF constraint_name IS NOT NULL THEN
                EXECUTE 'ALTER TABLE usdt_wallets DROP CONSTRAINT ' || constraint_name;
            END IF;
        END;
    END IF;
END
$$;

-- Update existing wallet types to new blockchain types  
UPDATE usdt_wallets 
SET wallet_type = CASE 
    WHEN wallet_type = 'COMPANY' THEN 'ETH'
    WHEN wallet_type = 'MEMBER' THEN 'TRX' 
    WHEN wallet_type = 'TESTING' THEN 'BSC'
    WHEN wallet_type = 'MAIN' THEN 'BTC'
    WHEN wallet_type = 'TRADING' THEN 'POL'
    WHEN wallet_type = 'STAKING' THEN 'ADA'
    WHEN wallet_type = 'REWARDS' THEN 'AVAX'
    -- If already a new type, keep as is
    WHEN wallet_type IN ('POL', 'TRX', 'SOL', 'BSC', 'BTC', 'ETH', 'ADA', 'AVAX', 'DOT', 'LINK') THEN wallet_type
    -- Default fallback
    ELSE 'BTC'
END
WHERE wallet_type IS NOT NULL;

-- Add new check constraint for blockchain wallet types only
ALTER TABLE usdt_wallets ADD CONSTRAINT ck_wallet_type_blockchain 
CHECK (wallet_type IN ('POL', 'TRX', 'SOL', 'BSC', 'BTC', 'ETH', 'ADA', 'AVAX', 'DOT', 'LINK'));

COMMIT;
