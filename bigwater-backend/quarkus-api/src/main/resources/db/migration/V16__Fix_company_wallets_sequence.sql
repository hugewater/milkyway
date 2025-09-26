-- Fix company_wallets sequence issue
-- Drop and recreate the sequence with correct name and ownership

-- Drop the existing sequence if it exists
DROP SEQUENCE IF EXISTS company_wallets_SEQ;

-- Create the sequence with the correct name that Hibernate expects
CREATE SEQUENCE company_wallets_SEQ START WITH 4 INCREMENT BY 1;

-- Alternative: Use the standard PostgreSQL naming convention
-- DROP SEQUENCE IF EXISTS company_wallets_id_seq;
-- CREATE SEQUENCE company_wallets_id_seq START WITH 4 INCREMENT BY 1;

-- Ensure the sequence is owned by the id column
ALTER SEQUENCE company_wallets_SEQ OWNED BY company_wallets.id;

-- Set the current value to be higher than existing records
SELECT setval('company_wallets_SEQ', COALESCE((SELECT MAX(id) FROM company_wallets), 0) + 1, false);

