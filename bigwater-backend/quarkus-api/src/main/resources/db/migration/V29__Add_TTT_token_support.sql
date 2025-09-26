-- V29: Add TTT token support to usdt_wallets table
BEGIN;

-- Drop the existing check constraint
ALTER TABLE usdt_wallets DROP CONSTRAINT IF EXISTS ck_wallet_type_blockchain;

-- Add a new check constraint including 'TTT'
ALTER TABLE usdt_wallets ADD CONSTRAINT ck_wallet_type_blockchain
CHECK (wallet_type IN ('POL', 'TRX', 'SOL', 'BSC', 'BTC', 'ETH', 'ADA', 'AVAX', 'DOT', 'LINK', 'ACT', 'TTT'));

COMMIT;
