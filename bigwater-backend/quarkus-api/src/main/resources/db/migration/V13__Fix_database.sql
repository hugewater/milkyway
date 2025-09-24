-- Ensure users table exists with correct structure (moved from V7 to resolve version conflict)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    role TEXT NOT NULL DEFAULT 'SUBSCRIBER' CHECK (role IN ('SUBSCRIBER','ADMIN','SUPER_ADMIN')),
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED')),
    level TEXT NOT NULL DEFAULT 'bronze' CHECK (level IN ('bronze','silver','gold','platinum')),
    referral_code VARCHAR(20) NOT NULL UNIQUE,
    referred_by_code VARCHAR(20) DEFAULT 'COMPANY001',
    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    email_verified_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_referral_code ON users(referral_code);
CREATE INDEX IF NOT EXISTS idx_referred_by_code ON users(referred_by_code);
CREATE INDEX IF NOT EXISTS idx_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_level ON users(level);
CREATE INDEX IF NOT EXISTS idx_join_date ON users(join_date);

-- Seed minimal users if missing
INSERT INTO users (email, password_hash, first_name, last_name, phone, role, status, level, referral_code, referred_by_code, join_date, created_at, updated_at)
VALUES ('admin@bigwater.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','Admin','User','', 'ADMIN','ACTIVE','platinum','ADMIN001','COMPANY001', NOW(), NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password_hash, first_name, last_name, phone, role, status, level, referral_code, referred_by_code, join_date, created_at, updated_at)
VALUES ('test@test.com','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','Test','User','', 'SUBSCRIBER','ACTIVE','bronze','TEST001','ADMIN001', NOW(), NOW(), NOW())
ON CONFLICT (email) DO NOTHING;


