服务端分页 + 轻量筛选/排序 + 必要时虚拟滚动”

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
