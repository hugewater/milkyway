-- Fix company_wallets sequence issue - try different approach
-- The issue is that Hibernate expects the sequence but it wasn't created properly

-- Check if there are existing records and get the max ID
DO $$
DECLARE
    max_id INTEGER;
BEGIN
    -- Get the maximum existing ID
    SELECT COALESCE(MAX(id), 0) + 1 INTO max_id FROM company_wallets;
    
    -- Drop any existing sequences
    DROP SEQUENCE IF EXISTS company_wallets_SEQ CASCADE;
    DROP SEQUENCE IF EXISTS company_wallets_id_seq CASCADE;
    
    -- Create the sequence that Hibernate expects
    EXECUTE format('CREATE SEQUENCE company_wallets_SEQ START WITH %s INCREMENT BY 1', max_id);
    
    -- Grant permissions
    GRANT USAGE, SELECT ON SEQUENCE company_wallets_SEQ TO PUBLIC;
    
    -- Set ownership
    ALTER SEQUENCE company_wallets_SEQ OWNED BY company_wallets.id;
    
END $$;

