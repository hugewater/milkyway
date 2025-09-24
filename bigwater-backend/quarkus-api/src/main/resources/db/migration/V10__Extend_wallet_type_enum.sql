-- PostgreSQL: use CHECK on TEXT column
ALTER TABLE usdt_wallets
  ALTER COLUMN wallet_type SET DEFAULT 'MAIN';

-- Optional runtime check via constraint (skip if exists)
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE constraint_name = 'ck_wallet_type_values'
      AND table_name = 'usdt_wallets'
  ) THEN
    ALTER TABLE usdt_wallets
      ADD CONSTRAINT ck_wallet_type_values
      CHECK (wallet_type IN ('MAIN','TRADING','STAKING','REWARDS','COMPANY','MEMBER','TESTING'));
  END IF;
END$$;
