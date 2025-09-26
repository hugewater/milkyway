-- V25: Set default wallet_type to POL

BEGIN;

ALTER TABLE usdt_wallets ALTER COLUMN wallet_type SET DEFAULT 'POL';

COMMIT;

