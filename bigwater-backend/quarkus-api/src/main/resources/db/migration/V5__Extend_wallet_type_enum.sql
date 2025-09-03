-- Extend wallet_type ENUM to include COMPANY, MEMBER, TESTING while keeping legacy values
ALTER TABLE usdt_wallets 
  MODIFY COLUMN wallet_type 
    ENUM('MAIN','TRADING','STAKING','REWARDS','COMPANY','MEMBER','TESTING') 
    NULL DEFAULT 'MAIN';


