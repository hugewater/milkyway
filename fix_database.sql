-- Remove bigwin column if it exists
ALTER TABLE bigwater.users DROP COLUMN IF EXISTS bigwin;

-- Check the current structure
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'users' 
AND table_schema = 'bigwater' 
ORDER BY ordinal_position;
