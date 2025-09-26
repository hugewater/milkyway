-- V26: Drop unused wallet tables
-- This migration drops the old company_wallets and wallets tables that are no longer used.

BEGIN;

-- Drop foreign key constraints first
DO $$
BEGIN
    -- Drop any foreign key constraints on company_wallets
    IF EXISTS (SELECT 1 FROM information_schema.table_constraints 
               WHERE table_name = 'company_wallets' 
               AND constraint_type = 'FOREIGN KEY') THEN
        DECLARE
            constraint_name text;
        BEGIN
            FOR constraint_name IN 
                SELECT tc.constraint_name 
                FROM information_schema.table_constraints tc
                WHERE tc.table_name = 'company_wallets' 
                AND tc.constraint_type = 'FOREIGN KEY'
            LOOP
                EXECUTE 'ALTER TABLE company_wallets DROP CONSTRAINT ' || constraint_name;
            END LOOP;
        END;
    END IF;
END
$$;

DO $$
BEGIN
    -- Drop any foreign key constraints on wallets
    IF EXISTS (SELECT 1 FROM information_schema.table_constraints 
               WHERE table_name = 'wallets' 
               AND constraint_type = 'FOREIGN KEY') THEN
        DECLARE
            constraint_name text;
        BEGIN
            FOR constraint_name IN 
                SELECT tc.constraint_name 
                FROM information_schema.table_constraints tc
                WHERE tc.table_name = 'wallets' 
                AND tc.constraint_type = 'FOREIGN KEY'
            LOOP
                EXECUTE 'ALTER TABLE wallets DROP CONSTRAINT ' || constraint_name;
            END LOOP;
        END;
    END IF;
END
$$;

-- Drop the unused tables
DROP TABLE IF EXISTS company_wallets CASCADE;
DROP TABLE IF EXISTS wallets CASCADE;

COMMIT;
