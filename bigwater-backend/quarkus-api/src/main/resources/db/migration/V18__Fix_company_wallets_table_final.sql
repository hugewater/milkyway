-- Fix company_wallets table to use proper SERIAL type
-- Drop and recreate the table with the correct column type

-- First, save existing data
CREATE TEMP TABLE company_wallets_backup AS SELECT * FROM company_wallets;

-- Drop the problematic table
DROP TABLE IF EXISTS company_wallets CASCADE;

-- Recreate the table with proper SERIAL type (which automatically creates the sequence)
CREATE TABLE company_wallets (
    id BIGSERIAL PRIMARY KEY,
    wallet_name VARCHAR(255) NOT NULL,
    tron_address VARCHAR(255),
    polygon_address VARCHAR(255),
    tron_balance DECIMAL(20,8) DEFAULT 0,
    polygon_balance DECIMAL(20,8) DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Restore the data (without the id to let SERIAL handle it)
INSERT INTO company_wallets (wallet_name, tron_address, polygon_address, tron_balance, polygon_balance, is_active, created_at, updated_at)
SELECT wallet_name, tron_address, polygon_address, tron_balance, polygon_balance, is_active, created_at, updated_at
FROM company_wallets_backup;

-- Drop the temp table
DROP TABLE company_wallets_backup;

-- The BIGSERIAL type automatically creates a sequence named "company_wallets_id_seq"
-- But Hibernate might expect "company_wallets_SEQ", so let's create an alias
CREATE SEQUENCE IF NOT EXISTS company_wallets_SEQ;
SELECT setval('company_wallets_SEQ', (SELECT COALESCE(MAX(id), 0) FROM company_wallets) + 1);

