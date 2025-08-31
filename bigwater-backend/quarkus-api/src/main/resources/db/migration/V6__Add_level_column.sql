-- Add level column to users table
ALTER TABLE users ADD COLUMN level VARCHAR(20) DEFAULT 'CHIEF';

-- Update existing users to have appropriate levels
UPDATE users SET level = 'PRESIDENT' WHERE role = 'SUPER_ADMIN';
UPDATE users SET level = 'MINISTER' WHERE role = 'ADMIN' AND level = 'CHIEF';

