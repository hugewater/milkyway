-- Add bigwin field to users table
ALTER TABLE users ADD COLUMN bigwin DECIMAL(20,8) DEFAULT 0.00000000;

-- Add comment for clarity
COMMENT ON COLUMN users.bigwin IS 'Big win amount for user (for future drawings)';
