-- Add from_wallet_id and to_wallet_id to transactions (compatible migration)
ALTER TABLE transactions
  ADD COLUMN IF NOT EXISTS from_wallet_id BIGINT NULL,
  ADD COLUMN IF NOT EXISTS to_wallet_id   BIGINT NULL;

-- Backfill to_wallet_id from legacy wallet_id when possible
UPDATE transactions SET to_wallet_id = wallet_id WHERE to_wallet_id IS NULL AND wallet_id IS NOT NULL;

-- Indexes for faster lookups
CREATE INDEX IF NOT EXISTS idx_transactions_from_wallet_id ON transactions (from_wallet_id);
CREATE INDEX IF NOT EXISTS idx_transactions_to_wallet_id   ON transactions (to_wallet_id);

-- Add foreign keys (ignore if already present)
ALTER TABLE transactions
  ADD CONSTRAINT fk_transactions_from_wallet
    FOREIGN KEY (from_wallet_id) REFERENCES usdt_wallets(id);

ALTER TABLE transactions
  ADD CONSTRAINT fk_transactions_to_wallet
    FOREIGN KEY (to_wallet_id) REFERENCES usdt_wallets(id);


