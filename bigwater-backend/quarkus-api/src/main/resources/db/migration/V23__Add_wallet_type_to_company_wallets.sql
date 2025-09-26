-- V23: Add wallet_type to company_wallets with blockchain network types only

BEGIN;

-- 1) Add column (nullable for now)
ALTER TABLE company_wallets ADD COLUMN IF NOT EXISTS wallet_type VARCHAR(10);

-- 2) Initialize existing rows with a sensible default
UPDATE company_wallets SET wallet_type = 'ETH' WHERE wallet_type IS NULL;

-- 3) Enforce NOT NULL
ALTER TABLE company_wallets ALTER COLUMN wallet_type SET NOT NULL;

-- 4) Add check constraint for allowed blockchain types
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE table_name = 'company_wallets' AND constraint_name = 'ck_company_wallet_type_blockchain'
    ) THEN
        ALTER TABLE company_wallets ADD CONSTRAINT ck_company_wallet_type_blockchain
        CHECK (wallet_type IN ('POL','TRX','SOL','BSC','BTC','ETH','ADA','AVAX','DOT','LINK'));
    END IF;
END
$$;

-- 5) Optional: index for filtering/grouping by wallet_type
CREATE INDEX IF NOT EXISTS idx_company_wallets_wallet_type ON company_wallets(wallet_type);

COMMIT;

