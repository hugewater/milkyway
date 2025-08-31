-- Insert test users if they don't exist
INSERT IGNORE INTO users (id, name, email, password_hash, level, status, join_date, created_at, updated_at) VALUES
(1, 'Admin User', 'admin@bigwater.com', 'admin_hash', 'platinum', 'active', '2024-01-01', NOW(), NOW()),
(2, 'John Doe', 'john@example.com', 'john_hash', 'gold', 'active', '2024-01-15', NOW(), NOW()),
(3, 'Jane Smith', 'jane@example.com', 'jane_hash', 'silver', 'active', '2024-02-01', NOW(), NOW());

-- Insert test wallets if they don't exist
INSERT IGNORE INTO usdt_wallets (id, user_id, wallet_address, wallet_name, wallet_type, balance, is_active, is_verified, created_at, updated_at) VALUES
(1, 1, '0xAdminWallet123', 'Admin Main Wallet', 'MAIN', 10000.00, 1, 1, NOW(), NOW()),
(2, 2, '0xJohnWallet456', 'John Main Wallet', 'MAIN', 5000.00, 1, 1, NOW(), NOW()),
(3, 3, '0xJaneWallet789', 'Jane Main Wallet', 'MAIN', 3000.00, 1, 1, NOW(), NOW()),
(4, 2, '0xJohnTrading123', 'John Trading Wallet', 'TRADING', 2000.00, 1, 1, NOW(), NOW()),
(5, 3, '0xJaneStaking456', 'Jane Staking Wallet', 'STAKING', 1500.00, 1, 1, NOW(), NOW());
