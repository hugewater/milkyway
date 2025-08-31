-- Add level field to users table
ALTER TABLE users ADD COLUMN level ENUM('bronze', 'silver', 'gold', 'platinum') NOT NULL DEFAULT 'bronze' AFTER status;

-- Add index for level field
CREATE INDEX idx_level ON users(level);
