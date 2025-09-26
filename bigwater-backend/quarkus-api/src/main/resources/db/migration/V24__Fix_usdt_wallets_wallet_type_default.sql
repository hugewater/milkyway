-- V24: Fix usdt_wallets.wallet_type default and normalize values

BEGIN;

-- 1) Normalize legacy values to blockchain types
UPDATE usdt_wallets SET wallet_type = 'BTC' WHERE wallet_type = 'MAIN';

-- Optionally map other legacy values if any remain
UPDATE usdt_wallets SET wallet_type = 'ETH' WHERE wallet_type = 'COMPANY';
UPDATE usdt_wallets SET wallet_type = 'TRX' WHERE wallet_type = 'MEMBER';
UPDATE usdt_wallets SET wallet_type = 'BSC' WHERE wallet_type = 'TESTING';
UPDATE usdt_wallets SET wallet_type = 'POL' WHERE wallet_type = 'TRADING';
UPDATE usdt_wallets SET wallet_type = 'ADA' WHERE wallet_type = 'STAKING';
UPDATE usdt_wallets SET wallet_type = 'AVAX' WHERE wallet_type = 'REWARDS';

-- 2) Drop old check constraint if exists, then add the correct one
ALTER TABLE usdt_wallets DROP CONSTRAINT IF EXISTS ck_wallet_type_blockchain;
ALTER TABLE usdt_wallets ADD CONSTRAINT ck_wallet_type_blockchain
CHECK (wallet_type IN ('POL','TRX','SOL','BSC','BTC','ETH','ADA','AVAX','DOT','LINK'));

-- 3) Fix column default: drop legacy default and set to BTC
ALTER TABLE usdt_wallets ALTER COLUMN wallet_type DROP DEFAULT;
ALTER TABLE usdt_wallets ALTER COLUMN wallet_type SET DEFAULT 'BTC';

COMMIT;

