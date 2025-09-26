-- V28: Add ACT token support for Polygon Amoy testnet
BEGIN;

-- Update the check constraint to include ACT token type
ALTER TABLE usdt_wallets DROP CONSTRAINT IF EXISTS ck_wallet_type_blockchain;

-- Add new check constraint including ACT token type
ALTER TABLE usdt_wallets ADD CONSTRAINT ck_wallet_type_blockchain 
CHECK (wallet_type IN ('POL', 'TRX', 'SOL', 'BSC', 'BTC', 'ETH', 'ADA', 'AVAX', 'DOT', 'LINK', 'ACT'));

COMMIT;
