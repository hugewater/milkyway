-- Add total_win field to users table
ALTER TABLE users ADD COLUMN total_win DECIMAL(20,8) DEFAULT 0.00000000;

-- Add comment for clarity
COMMENT ON COLUMN users.total_win IS 'Total win amount for user (accumulated winnings)';
