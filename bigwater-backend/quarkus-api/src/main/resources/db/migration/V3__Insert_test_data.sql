-- Optional: Insert test users (adjust columns per current schema); using ON CONFLICT for PostgreSQL
INSERT INTO users (email, password_hash, first_name, last_name, role, status, level, referral_code, referred_by_code, join_date, created_at)
VALUES 
('admin@bigwater.com','admin_hash','Admin','User','ADMIN','ACTIVE','platinum','ADMIN001','COMPANY001','2024-01-01', NOW()),
('john@example.com','john_hash','John','Doe','SUBSCRIBER','ACTIVE','gold','JOHN001','COMPANY001','2024-01-15', NOW()),
('jane@example.com','jane_hash','Jane','Smith','SUBSCRIBER','ACTIVE','silver','JANE001','COMPANY001','2024-02-01', NOW())
ON CONFLICT (email) DO NOTHING;

INSERT INTO usdt_wallets (user_id, wallet_address, wallet_name, wallet_type, balance, is_active, is_verified, created_at)
SELECT u.id, v.addr, v.name, v.type, v.balance, TRUE, TRUE, NOW()
FROM (
  VALUES
  ('admin@bigwater.com','0xAdminWallet123','Admin Main Wallet','MAIN', 10000.00::numeric),
  ('john@example.com','0xJohnWallet456','John Main Wallet','MAIN', 5000.00::numeric),
  ('jane@example.com','0xJaneWallet789','Jane Main Wallet','MAIN', 3000.00::numeric),
  ('john@example.com','0xJohnTrading123','John Trading Wallet','TRADING', 2000.00::numeric),
  ('jane@example.com','0xJaneStaking456','Jane Staking Wallet','STAKING', 1500.00::numeric)
) AS v(email, addr, name, type, balance)
JOIN users u ON u.email = v.email
ON CONFLICT (wallet_address) DO NOTHING;
