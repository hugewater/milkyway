-- Drop and recreate users table with level field
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    role ENUM('SUBSCRIBER', 'ADMIN', 'SUPER_ADMIN') NOT NULL DEFAULT 'SUBSCRIBER',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    level ENUM('bronze', 'silver', 'gold', 'platinum') NOT NULL DEFAULT 'bronze',
    referral_code VARCHAR(20) NOT NULL UNIQUE,
    referred_by_code VARCHAR(20) DEFAULT 'COMPANY001',
    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    email_verified_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_referral_code (referral_code),
    INDEX idx_referred_by_code (referred_by_code),
    INDEX idx_status (status),
    INDEX idx_role (role),
    INDEX idx_level (level),
    INDEX idx_join_date (join_date)
);

-- Insert default admin user
INSERT INTO users (email, password_hash, first_name, last_name, phone, role, status, level, referral_code, referred_by_code, join_date, created_at, updated_at) 
VALUES (
    'admin@bigwater.com',
    '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', -- admin123
    'Admin',
    'User',
    '',
    'ADMIN',
    'ACTIVE',
    'platinum',
    'ADMIN001',
    'COMPANY001',
    NOW(),
    NOW(),
    NOW()
);

-- Insert test user
INSERT INTO users (email, password_hash, first_name, last_name, phone, role, status, level, referral_code, referred_by_code, join_date, created_at, updated_at) 
VALUES (
    'test@test.com',
    '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', -- admin123
    'Test',
    'User',
    '',
    'SUBSCRIBER',
    'ACTIVE',
    'bronze',
    'TEST001',
    'ADMIN001',
    NOW(),
    NOW(),
    NOW()
);
