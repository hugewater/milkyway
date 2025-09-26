-- V22: Add is_company flag to usdt_wallets table to distinguish company wallets from user wallets

BEGIN;

-- Add is_company column to usdt_wallets table
ALTER TABLE usdt_wallets 
ADD COLUMN is_company BOOLEAN NOT NULL DEFAULT FALSE;

-- Create index for better query performance
CREATE INDEX idx_usdt_wallets_is_company ON usdt_wallets(is_company);

-- Update existing wallets based on wallet_type to set is_company flag
-- This is a reasonable assumption: ETH wallets are typically company wallets
UPDATE usdt_wallets 
SET is_company = TRUE 
WHERE wallet_type = 'ETH';

-- Add comment to the column for documentation
COMMENT ON COLUMN usdt_wallets.is_company IS 'Flag to indicate if this wallet belongs to the company (TRUE) or a user (FALSE)';

COMMIT;

