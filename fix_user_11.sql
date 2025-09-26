-- Fix user 11 total_pay value
-- First, let's check the current value
SELECT id, total_pay, total_reward, total_win 
FROM bigwater.users 
WHERE id = 11;

-- Update user 11's total_pay to 1680 (the amount from the transaction)
UPDATE bigwater.users 
SET total_pay = 1680.00000000, updated_at = CURRENT_TIMESTAMP 
WHERE id = 11;

-- Check the updated value
SELECT id, total_pay, total_reward, total_win 
FROM bigwater.users 
WHERE id = 11;

-- Show all users sorted by total_pay desc
SELECT id, total_pay, total_reward, total_win 
FROM bigwater.users 
ORDER BY total_pay DESC 
LIMIT 10;