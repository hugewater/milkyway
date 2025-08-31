-- Insert test network data with referral relationships
-- This creates a simple network structure for testing upline/downline functionality

-- Update existing users to create a network structure
UPDATE users SET referredByCode = 'COMPANY001' WHERE id = 1; -- Company user
UPDATE users SET referredByCode = 'COMPANY001' WHERE id = 4; -- test@test.com
UPDATE users SET referredByCode = 'G8DROCH8' WHERE id = 6; -- t1@t1.com (referred by test@test.com)

-- Insert additional test users to create a more complex network
INSERT INTO users (id, email, password_hash, first_name, last_name, phone, role, status, referral_code, referred_by_code, join_date, created_at, updated_at) VALUES
(7, 'upline1@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Upline', 'One', '', 'SUBSCRIBER', 'ACTIVE', 'UPLINE001', 'COMPANY001', '2024-01-01', NOW(), NOW()),
(8, 'upline2@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Upline', 'Two', '', 'SUBSCRIBER', 'ACTIVE', 'UPLINE002', 'UPLINE001', '2024-01-15', NOW(), NOW()),
(9, 'downline1@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Downline', 'One', '', 'SUBSCRIBER', 'ACTIVE', 'DOWN001', 'G8DROCH8', '2024-02-01', NOW(), NOW()),
(10, 'downline2@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Downline', 'Two', '', 'SUBSCRIBER', 'ACTIVE', 'DOWN002', 'DOWN001', '2024-02-15', NOW(), NOW()),
(11, 'downline3@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Downline', 'Three', '', 'SUBSCRIBER', 'ACTIVE', 'DOWN003', 'DOWN002', '2024-03-01', NOW(), NOW());

-- Now the network structure is:
-- COMPANY001 (id=1) -> UPLINE001 (id=7) -> UPLINE002 (id=8)
-- COMPANY001 (id=1) -> G8DROCH8 (id=4) -> G05X3743 (id=6)
-- G8DROCH8 (id=4) -> DOWN001 (id=9) -> DOWN002 (id=10) -> DOWN003 (id=11)
