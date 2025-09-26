-- Add total_pay and total_reward fields to users table
ALTER TABLE users ADD COLUMN total_pay DECIMAL(20,8) DEFAULT 0.00000000;
ALTER TABLE users ADD COLUMN total_reward DECIMAL(20,8) DEFAULT 0.00000000;

-- Add comments for clarity
COMMENT ON COLUMN users.total_pay IS 'Total amount paid by user (accumulated)';
COMMENT ON COLUMN users.total_reward IS 'Total reward amount for user (for future use)';
