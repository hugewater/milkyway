服务端分页 + 轻量筛选/排序 + 必要时虚拟滚动”

CREATE TABLE IF NOT EXISTS ai_agents (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  provider VARCHAR(50) NOT NULL,
  model VARCHAR(100) NOT NULL,
  api_key VARCHAR(255),
  webhook_url VARCHAR(255),
  role VARCHAR(50),
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  description TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_ai_agents_name (name),
  KEY idx_ai_agents_status (status),
  KEY idx_ai_agents_enabled (enabled)
);

CREATE TABLE IF NOT EXISTS ai_chats (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  agent_id BIGINT NOT NULL,
  user_id BIGINT NULL,
  title VARCHAR(200) NOT NULL,
  last_message TEXT,
  status ENUM('ACTIVE','ARCHIVED') NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_ai_chats_agent FOREIGN KEY (agent_id) REFERENCES ai_agents(id) ON DELETE CASCADE,
  INDEX idx_ai_chats_agent (agent_id),
  INDEX idx_ai_chats_status (status),
  INDEX idx_ai_chats_user (user_id)
);


CREATE INDEX idx_users_created_at ON users (created_at);
CREATE INDEX idx_users_join_date ON users (join_date);
CREATE INDEX idx_users_last_login ON users (last_login);
CREATE INDEX idx_users_status ON users (status);
CREATE INDEX idx_users_level ON users (level);
CREATE INDEX idx_users_role ON users (role);
CREATE UNIQUE INDEX uk_users_email ON users (email);
CREATE UNIQUE INDEX uk_users_referral_code ON users (referral_code);


CREATE INDEX idx_wallets_created_at ON usdt_wallets (created_at);
CREATE INDEX idx_wallets_is_active ON usdt_wallets (is_active);
CREATE INDEX idx_wallets_wallet_type ON usdt_wallets (wallet_type);
CREATE INDEX idx_wallets_user_id ON usdt_wallets (user_id);
CREATE UNIQUE INDEX uk_wallets_wallet_address ON usdt_wallets (wallet_address);


SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'usdt_wallets'
  AND COLUMN_NAME = 'wallet_type';

ALTER TABLE transactions
  ADD COLUMN from_wallet_id BIGINT NULL,
  ADD COLUMN to_wallet_id   BIGINT NULL;

-- 2) 回填：把旧的 wallet_id 迁移到 to_wallet_id
UPDATE transactions
SET to_wallet_id = wallet_id
WHERE to_wallet_id IS NULL AND wallet_id IS NOT NULL;

-- 3) 索引（按需命名）
CREATE INDEX idx_transactions_from_wallet_id ON transactions (from_wallet_id);
CREATE INDEX idx_transactions_to_wallet_id   ON transactions (to_wallet_id);

-- 4) 外键（如果已有同名外键，先手动 DROP 再执行）
ALTER TABLE transactions
  ADD CONSTRAINT fk_transactions_from_wallet
    FOREIGN KEY (from_wallet_id) REFERENCES usdt_wallets(id),
  ADD CONSTRAINT fk_transactions_to_wallet
    FOREIGN KEY (to_wallet_id)   REFERENCES usdt_wallets(id);

//the below table never created, no need

CREATE TABLE IF NOT EXISTS wallet_payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  from_address VARCHAR(255) NOT NULL,
  to_wallet_id BIGINT NOT NULL,
  amount DECIMAL(20,8) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_wallet_payments_wallet FOREIGN KEY (to_wallet_id) REFERENCES usdt_wallets(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
