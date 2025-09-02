-- Create company user with known password
-- Password: company123 (SHA-256 hash)
INSERT INTO users (
    email, 
    password_hash, 
    first_name, 
    last_name, 
    phone, 
    role, 
    status, 
    referral_code, 
    referred_by_code
) VALUES (
    'company@bigwater.com',
    '5EZ3F7u6Jt7TpRa96eWY5SR/ZQg7wFLzV1U=', -- QCEncryptor hash of 'company123'
    'BigWater',
    'Company',
    '+1234567890',
    'SUPER_ADMIN',
    'ACTIVE',
    'COMPANY001',
    'COMPANY001'
) ON DUPLICATE KEY UPDATE
    password_hash = VALUES(password_hash),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    phone = VALUES(phone),
    role = VALUES(role),
    status = VALUES(status);
