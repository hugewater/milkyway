-- Update user 11's total_pay to the correct value
UPDATE bigwater.users 
SET total_pay = 1680.00000000, updated_at = CURRENT_TIMESTAMP 
WHERE id = 11;

-- Verify the update
SELECT id, total_pay, total_reward, total_win 
FROM bigwater.users 
WHERE id = 11;

-- Show all users sorted by total_pay desc
SELECT id, total_pay, total_reward, total_win 
FROM bigwater.users 
ORDER BY total_pay DESC 
LIMIT 10;