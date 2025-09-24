-- Create wallet_payments table to record user self-reported payments (PostgreSQL)
CREATE TABLE IF NOT EXISTS wallet_payments (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  from_address VARCHAR(255) NOT NULL,
  to_wallet_id BIGINT NOT NULL REFERENCES usdt_wallets(id),
  amount NUMERIC(20,8) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


