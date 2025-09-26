-- Check if bigwin column exists
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'users' 
AND table_schema = 'bigwater' 
AND column_name = 'bigwin';

-- Check all columns in users table
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'users' 
AND table_schema = 'bigwater' 
ORDER BY ordinal_position;
