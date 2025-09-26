-- Manual fix for bigwin field issue
-- This script will remove the bigwin field and check the table structure

-- Remove bigwin column if it exists
ALTER TABLE bigwater.users DROP COLUMN IF EXISTS bigwin;

-- Check if the column was removed
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'users' 
AND table_schema = 'bigwater' 
AND column_name = 'bigwin';

-- Show all columns in users table
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'users' 
AND table_schema = 'bigwater' 
ORDER BY ordinal_position;
