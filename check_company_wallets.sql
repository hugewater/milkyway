-- Check company wallets in database
SELECT 
    id, 
    wallet_name, 
    wallet_address, 
    wallet_type, 
    is_company, 
    is_active,
    user_id
FROM bigwater.usdt_wallets 
WHERE is_company = true
ORDER BY id;

-- Check all wallets to see what we have
SELECT 
    id, 
    wallet_name, 
    wallet_address, 
    wallet_type, 
    is_company, 
    is_active,
    user_id
FROM bigwater.usdt_wallets 
ORDER BY id;

-- Check if there are any wallets at all
SELECT COUNT(*) as total_wallets FROM bigwater.usdt_wallets;
