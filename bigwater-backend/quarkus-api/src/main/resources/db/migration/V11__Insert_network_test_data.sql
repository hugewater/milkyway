-- Insert test network data with referral relationships (no explicit IDs)
-- This creates a simple network structure for testing upline/downline functionality

-- Ensure admin is referred by company in test data
UPDATE users
SET referred_by_code = 'COMPANY001'
WHERE email IN ('admin@bigwater.com')
  AND (referred_by_code IS NULL OR referred_by_code <> 'COMPANY001');

-- Insert additional test users to create a more complex network
INSERT INTO users (
    email, password_hash, first_name, last_name, phone, role, status, level,
    referral_code, referred_by_code, join_date, created_at, updated_at
) VALUES
('upline1@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Upline', 'One', '', 'SUBSCRIBER', 'ACTIVE', 'silver', 'UPLINE001', 'COMPANY001', '2024-01-01', NOW(), NOW()),
('upline2@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Upline', 'Two', '', 'SUBSCRIBER', 'ACTIVE', 'silver', 'UPLINE002', 'UPLINE001', '2024-01-15', NOW(), NOW()),
('downline1@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Downline', 'One', '', 'SUBSCRIBER', 'ACTIVE', 'bronze', 'DOWN001', 'UPLINE002', '2024-02-01', NOW(), NOW()),
('downline2@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Downline', 'Two', '', 'SUBSCRIBER', 'ACTIVE', 'bronze', 'DOWN002', 'DOWN001', '2024-02-15', NOW(), NOW()),
('downline3@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Downline', 'Three', '', 'SUBSCRIBER', 'ACTIVE', 'bronze', 'DOWN003', 'DOWN002', '2024-03-01', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Network structure (for reference):
-- COMPANY001 -> UPLINE001 -> UPLINE002 -> DOWN001 -> DOWN002 -> DOWN003

