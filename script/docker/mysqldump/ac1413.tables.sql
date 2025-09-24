-- SQLINES DEMO ***  Distrib 9.4.0, for macos15.4 (arm64)
--
-- SQLINES DEMO ***   Database: bwdb
-- SQLINES DEMO *** -------------------------------------
-- SQLINES DEMO *** 4.0

/* SQLINES DEMO *** CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/* SQLINES DEMO *** CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/* SQLINES DEMO *** COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/* SQLINES DEMO ***  utf8mb4 */;
/* SQLINES DEMO *** TIME_ZONE=@@TIME_ZONE */;
/* SQLINES DEMO *** ZONE='+00:00' */;
/* SQLINES DEMO *** UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/* SQLINES DEMO *** FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/* SQLINES DEMO *** SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/* SQLINES DEMO *** SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- SQLINES DEMO *** or table `access_logs`
--

DROP TABLE IF EXISTS access_logs;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
-- SQLINES FOR EVALUATION USE ONLY (14 DAYS)
CREATE TABLE access_logs (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint DEFAULT NULL,
  ip_address varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  user_agent text CHARACTER SET utf8mb4,
  request_method varchar(10) CHARACTER SET utf8mb4 NOT NULL,
  request_url varchar(500) CHARACTER SET utf8mb4 NOT NULL,
  request_params text CHARACTER SET utf8mb4,
  response_status int NOT NULL,
  response_time_ms int DEFAULT NULL,
  session_id varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  accessed_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
,
  CONSTRAINT access_logs_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
) ;

CREATE INDEX idx_user_id ON access_logs (user_id);
CREATE INDEX idx_ip_address ON access_logs (ip_address);
CREATE INDEX idx_request_method ON access_logs (request_method);
CREATE INDEX idx_response_status ON access_logs (response_status);
CREATE INDEX idx_accessed_at ON access_logs (accessed_at);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** or table `ai_agents`
--

DROP TABLE IF EXISTS ai_agents;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE ai_agents (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  name varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  provider varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  model varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  api_key varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  webhook_url varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  role varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  enabled smallint NOT NULL DEFAULT '1',
  status varchar(30) check (status in ('ACTIVE','INACTIVE')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'ACTIVE',
  description text CHARACTER SET utf8mb4,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id),
  CONSTRAINT uk_ai_agents_name UNIQUE (name)
)  ;

ALTER SEQUENCE ai_agents_seq RESTART WITH 3;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_ai_agents_status ON ai_agents (status);
CREATE INDEX idx_ai_agents_enabled ON ai_agents (enabled);

--
-- SQLINES DEMO *** or table `ai_chats`
--

DROP TABLE IF EXISTS ai_chats;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE ai_chats (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  agent_id bigint NOT NULL,
  user_id bigint DEFAULT NULL,
  title varchar(200) CHARACTER SET utf8mb4 NOT NULL,
  last_message text CHARACTER SET utf8mb4,
  status varchar(30) check (status in ('ACTIVE','ARCHIVED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'ACTIVE',
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id)
,
  CONSTRAINT fk_ai_chats_agent FOREIGN KEY (agent_id) REFERENCES ai_agents (id) ON DELETE CASCADE
)  ;

ALTER SEQUENCE ai_chats_seq RESTART WITH 2;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_ai_chats_agent ON ai_chats (agent_id);
CREATE INDEX idx_ai_chats_status ON ai_chats (status);
CREATE INDEX idx_ai_chats_user ON ai_chats (user_id);

--
-- SQLINES DEMO *** or table `certificates`
--

DROP TABLE IF EXISTS certificates;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE certificates (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  certificate_name varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  certificate_type varchar(30) check (certificate_type in ('BRONZE','SILVER','GOLD','PLATINUM','DIAMOND')) CHARACTER SET utf8mb4 NOT NULL,
  description text CHARACTER SET utf8mb4,
  price_usdt decimal(15,2) NOT NULL,
  duration_days int NOT NULL DEFAULT '365',
  benefits json DEFAULT NULL,
  is_active smallint NOT NULL DEFAULT '1',
  max_supply int DEFAULT NULL,
  current_supply int NOT NULL DEFAULT '0',
  created_by bigint NOT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id)
,
  CONSTRAINT certificates_ibfk_1 FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE
)  ;

ALTER SEQUENCE certificates_seq RESTART WITH 20;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_certificate_type ON certificates (certificate_type);
CREATE INDEX idx_price_usdt ON certificates (price_usdt);
CREATE INDEX idx_is_active ON certificates (is_active);
CREATE INDEX idx_created_by ON certificates (created_by);

--
-- SQLINES DEMO *** or table `drawing_participants`
--

DROP TABLE IF EXISTS drawing_participants;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE drawing_participants (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  drawing_id bigint NOT NULL,
  user_id bigint NOT NULL,
  participant_numbers json DEFAULT NULL,
  entry_fee decimal(10,2) NOT NULL DEFAULT '0.00',
  is_winner smallint NOT NULL DEFAULT '0',
  prize_amount decimal(15,2) DEFAULT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uk_drawing_user UNIQUE (drawing_id,user_id)
,
  CONSTRAINT drawing_participants_ibfk_1 FOREIGN KEY (drawing_id) REFERENCES random_drawings (id) ON DELETE CASCADE,
  CONSTRAINT drawing_participants_ibfk_2 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ;

CREATE INDEX idx_drawing_id ON drawing_participants (drawing_id);
CREATE INDEX idx_user_id ON drawing_participants (user_id);
CREATE INDEX idx_is_winner ON drawing_participants (is_winner);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** or table `error_logs`
--

DROP TABLE IF EXISTS error_logs;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE error_logs (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint DEFAULT NULL,
  error_level varchar(30) check (error_level in ('DEBUG','INFO','WARNING','ERROR','CRITICAL')) CHARACTER SET utf8mb4 NOT NULL,
  error_type varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  error_message text CHARACTER SET utf8mb4 NOT NULL,
  stack_trace text CHARACTER SET utf8mb4,
  request_url varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL,
  request_method varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL,
  ip_address varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  user_agent text CHARACTER SET utf8mb4,
  additional_data json DEFAULT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
,
  CONSTRAINT error_logs_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
) ;

CREATE INDEX idx_user_id ON error_logs (user_id);
CREATE INDEX idx_error_level ON error_logs (error_level);
CREATE INDEX idx_error_type ON error_logs (error_type);
CREATE INDEX idx_created_at ON error_logs (created_at);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** or table `journal_views`
--

DROP TABLE IF EXISTS journal_views;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE journal_views (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  journal_id bigint NOT NULL,
  user_id bigint DEFAULT NULL,
  ip_address varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  user_agent text CHARACTER SET utf8mb4,
  viewed_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
,
  CONSTRAINT journal_views_ibfk_1 FOREIGN KEY (journal_id) REFERENCES journals (id) ON DELETE CASCADE,
  CONSTRAINT journal_views_ibfk_2 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
) ;

CREATE INDEX idx_journal_id ON journal_views (journal_id);
CREATE INDEX idx_user_id ON journal_views (user_id);
CREATE INDEX idx_viewed_at ON journal_views (viewed_at);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** or table `journals`
--

DROP TABLE IF EXISTS journals;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE journals (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  week_number int NOT NULL,
  title varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  excerpt text CHARACTER SET utf8mb4,
  content text CHARACTER SET utf8mb4 NOT NULL,
  status varchar(30) check (status in ('DRAFT','PUBLISHED','SCHEDULED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'DRAFT',
  featured smallint NOT NULL DEFAULT '0',
  tags varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL,
  views int NOT NULL DEFAULT '0',
  publish_date timestamp(0) NULL DEFAULT NULL,
  created_by bigint NOT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id),
  CONSTRAINT week_number UNIQUE (week_number)
,
  CONSTRAINT journals_ibfk_1 FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE
)  ;

ALTER SEQUENCE journals_seq RESTART WITH 8;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_week_number ON journals (week_number);
CREATE INDEX idx_status ON journals (status);
CREATE INDEX idx_featured ON journals (featured);
CREATE INDEX idx_publish_date ON journals (publish_date);
CREATE INDEX idx_created_by ON journals (created_by);

--
-- SQLINES DEMO *** or table `random_drawings`
--

DROP TABLE IF EXISTS random_drawings;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE random_drawings (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  drawing_name varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  drawing_type varchar(30) check (drawing_type in ('SWEEPSTAKES','RAFFLE','CONTEST')) CHARACTER SET utf8mb4 NOT NULL,
  week_number int NOT NULL,
  drawing_date timestamp(0) NOT NULL,
  prize_pool decimal(15,2) NOT NULL DEFAULT '0.00',
  winning_numbers json DEFAULT NULL,
  status varchar(30) check (status in ('PENDING','COMPLETED','CANCELLED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'PENDING',
  total_participants int NOT NULL DEFAULT '0',
  created_by bigint NOT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id)
,
  CONSTRAINT random_drawings_ibfk_1 FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE
)  ;

ALTER SEQUENCE random_drawings_seq RESTART WITH 11;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_drawing_type ON random_drawings (drawing_type);
CREATE INDEX idx_week_number ON random_drawings (week_number);
CREATE INDEX idx_drawing_date ON random_drawings (drawing_date);
CREATE INDEX idx_status ON random_drawings (status);
CREATE INDEX idx_created_by ON random_drawings (created_by);

--
-- SQLINES DEMO *** or table `referral_relationships`
--

DROP TABLE IF EXISTS referral_relationships;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE referral_relationships (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  referrer_id bigint NOT NULL,
  referred_id bigint NOT NULL,
  referral_level int NOT NULL DEFAULT '1',
  commission_rate decimal(5,4) NOT NULL DEFAULT '0.1000',
  status varchar(30) check (status in ('ACTIVE','INACTIVE','CANCELLED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'ACTIVE',
  total_earnings decimal(15,2) NOT NULL DEFAULT '0.00',
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id),
  CONSTRAINT uk_referrer_referred UNIQUE (referrer_id,referred_id)
,
  CONSTRAINT referral_relationships_ibfk_1 FOREIGN KEY (referrer_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT referral_relationships_ibfk_2 FOREIGN KEY (referred_id) REFERENCES users (id) ON DELETE CASCADE
) ;

CREATE INDEX idx_referrer_id ON referral_relationships (referrer_id);
CREATE INDEX idx_referred_id ON referral_relationships (referred_id);
CREATE INDEX idx_referral_level ON referral_relationships (referral_level);
CREATE INDEX idx_status ON referral_relationships (status);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** or table `system_settings`
--

DROP TABLE IF EXISTS system_settings;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE system_settings (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  setting_key varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  setting_value text CHARACTER SET utf8mb4 NOT NULL,
  setting_type varchar(30) check (setting_type in ('STRING','NUMBER','BOOLEAN','JSON')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'STRING',
  description text CHARACTER SET utf8mb4,
  is_public smallint NOT NULL DEFAULT '0',
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id),
  CONSTRAINT setting_key UNIQUE (setting_key)
)  ;

ALTER SEQUENCE system_settings_seq RESTART WITH 13;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_setting_key ON system_settings (setting_key);
CREATE INDEX idx_is_public ON system_settings (is_public);

--
-- SQLINES DEMO *** or table `transactions`
--

DROP TABLE IF EXISTS transactions;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE transactions (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint NOT NULL,
  wallet_id bigint DEFAULT NULL,
  transaction_type varchar(30) check (transaction_type in ('WINNING','DEPOSIT','WITHDRAWAL','PURCHASE','REFUND','REWARD','COMMISSION')) CHARACTER SET utf8mb4 NOT NULL,
  amount_usdt decimal(20,8) NOT NULL,
  transaction_hash varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  status varchar(30) check (status in ('PENDING','COMPLETED','FAILED','CANCELLED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'PENDING',
  description text CHARACTER SET utf8mb4,
  related_entity_type varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  related_entity_id bigint DEFAULT NULL,
  metadata json DEFAULT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  to_wallet_id bigint check (to_wallet_id > 0) NOT NULL,
  PRIMARY KEY (id)
,
  CONSTRAINT transactions_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT transactions_ibfk_2 FOREIGN KEY (wallet_id) REFERENCES usdt_wallets (id) ON DELETE SET NULL
)  ;

ALTER SEQUENCE transactions_seq RESTART WITH 17;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_user_id ON transactions (user_id);
CREATE INDEX idx_wallet_id ON transactions (wallet_id);
CREATE INDEX idx_transaction_type ON transactions (transaction_type);
CREATE INDEX idx_status ON transactions (status);
CREATE INDEX idx_created_at ON transactions (created_at);

--
-- SQLINES DEMO *** or table `usdt_wallets`
--

DROP TABLE IF EXISTS usdt_wallets;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE usdt_wallets (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint NOT NULL,
  wallet_address varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  wallet_name varchar(100) CHARACTER SET utf8mb4 DEFAULT 'Main Wallet',
  wallet_type varchar(30) check (wallet_type in ('COMPANY','MEMBER','TESTING','MAIN','TRADING','STAKING','REWARDS')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'COMPANY',
  balance decimal(20,8) NOT NULL DEFAULT '0.00000000',
  is_active smallint NOT NULL DEFAULT '1',
  is_verified smallint NOT NULL DEFAULT '0',
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id),
  CONSTRAINT wallet_address UNIQUE (wallet_address),
  CONSTRAINT uk_wallets_wallet_address UNIQUE (wallet_address)
,
  CONSTRAINT usdt_wallets_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
)  ;

ALTER SEQUENCE usdt_wallets_seq RESTART WITH 21;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_user_id ON usdt_wallets (user_id);
CREATE INDEX idx_wallet_address ON usdt_wallets (wallet_address);
CREATE INDEX idx_wallet_type ON usdt_wallets (wallet_type);
CREATE INDEX idx_is_active ON usdt_wallets (is_active);

--
-- SQLINES DEMO *** or table `user_certificates`
--

DROP TABLE IF EXISTS user_certificates;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE user_certificates (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint NOT NULL,
  certificate_id bigint NOT NULL,
  purchase_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expiry_date timestamp(0) NOT NULL,
  status varchar(30) check (status in ('ACTIVE','EXPIRED','CANCELLED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'ACTIVE',
  purchase_amount_usdt decimal(15,2) NOT NULL,
  transaction_hash varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  wallet_id bigint DEFAULT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  PRIMARY KEY (id)
,
  CONSTRAINT user_certificates_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT user_certificates_ibfk_2 FOREIGN KEY (certificate_id) REFERENCES certificates (id) ON DELETE CASCADE,
  CONSTRAINT user_certificates_ibfk_3 FOREIGN KEY (wallet_id) REFERENCES usdt_wallets (id) ON DELETE SET NULL
) ;

CREATE INDEX wallet_id ON user_certificates (wallet_id);
CREATE INDEX idx_user_id ON user_certificates (user_id);
CREATE INDEX idx_certificate_id ON user_certificates (certificate_id);
CREATE INDEX idx_status ON user_certificates (status);
CREATE INDEX idx_expiry_date ON user_certificates (expiry_date);
CREATE INDEX idx_purchase_date ON user_certificates (purchase_date);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** or table `users`
--

DROP TABLE IF EXISTS users;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE users (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  email varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  password_hash varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  first_name varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  last_name varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  phone varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  role varchar(30) check (role in ('SUBSCRIBER','ADMIN','SUPER_ADMIN')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'SUBSCRIBER',
  status varchar(30) check (status in ('ACTIVE','INACTIVE','SUSPENDED')) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'ACTIVE',
  referral_code varchar(20) CHARACTER SET utf8mb4 NOT NULL,
  referred_by_code varchar(20) CHARACTER SET utf8mb4 DEFAULT 'COMPANY001',
  join_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_login timestamp(0) NULL DEFAULT NULL,
  email_verified_at timestamp(0) NULL DEFAULT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  level varchar(20) CHARACTER SET utf8mb4 DEFAULT 'CHIEF',
  PRIMARY KEY (id),
  CONSTRAINT email UNIQUE (email),
  CONSTRAINT referral_code UNIQUE (referral_code),
  CONSTRAINT uk_users_referral_code UNIQUE (referral_code)
)  ;

ALTER SEQUENCE users_seq RESTART WITH 246;
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

CREATE INDEX idx_email ON users (email);
CREATE INDEX idx_referral_code ON users (referral_code);
CREATE INDEX idx_referred_by_code ON users (referred_by_code);
CREATE INDEX idx_status ON users (status);
CREATE INDEX idx_role ON users (role);
CREATE INDEX idx_join_date ON users (join_date);

--
-- SQLINES DEMO *** or table `wallet_payments`
--

DROP TABLE IF EXISTS wallet_payments;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE wallet_payments (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  from_address varchar(255) NOT NULL,
  to_wallet_id bigint NOT NULL,
  amount decimal(20,8) NOT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
,
  CONSTRAINT fk_wallet_payments_wallet FOREIGN KEY (to_wallet_id) REFERENCES usdt_wallets (id)
) ;

CREATE INDEX fk_wallet_payments_wallet ON wallet_payments (to_wallet_id);
/* SQLINES DEMO *** cter_set_client = @saved_cs_client */;

--
-- SQLINES DEMO *** for database 'bwdb'
--
/* SQLINES DEMO *** ZONE=@OLD_TIME_ZONE */;

/* SQLINES DEMO *** ODE=@OLD_SQL_MODE */;
/* SQLINES DEMO *** GN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/* SQLINES DEMO *** E_CHECKS=@OLD_UNIQUE_CHECKS */;
/* SQLINES DEMO *** CTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/* SQLINES DEMO *** CTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/* SQLINES DEMO *** TION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/* SQLINES DEMO *** OTES=@OLD_SQL_NOTES */;

-- SQLINES DEMO ***  2025-09-18  9:51:31

