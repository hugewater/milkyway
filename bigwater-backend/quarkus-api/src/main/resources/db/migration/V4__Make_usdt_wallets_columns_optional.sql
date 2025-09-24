-- PostgreSQL-compatible: adjust column nullability/defaults
ALTER TABLE usdt_wallets ALTER COLUMN wallet_name DROP NOT NULL;
ALTER TABLE usdt_wallets ALTER COLUMN wallet_type DROP NOT NULL;
ALTER TABLE usdt_wallets ALTER COLUMN wallet_type SET DEFAULT 'MAIN';
ALTER TABLE usdt_wallets ALTER COLUMN balance SET DEFAULT 0.00000000;
ALTER TABLE usdt_wallets ALTER COLUMN is_active SET DEFAULT TRUE;
ALTER TABLE usdt_wallets ALTER COLUMN is_verified SET DEFAULT FALSE;
