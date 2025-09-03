-- Create wallet_payments table to record user self-reported payments
CREATE TABLE IF NOT EXISTS wallet_payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  from_address VARCHAR(255) NOT NULL,
  to_wallet_id BIGINT NOT NULL,
  amount DECIMAL(20,8) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_wallet_payments_wallet FOREIGN KEY (to_wallet_id) REFERENCES usdt_wallets(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


