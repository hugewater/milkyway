-- SQLINES DEMO ***  Distrib 8.4.6, for Linux (aarch64)
--
-- SQLINES DEMO ***   Database: bwdb
-- SQLINES DEMO *** -------------------------------------
-- SQLINES DEMO *** 4.6

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
  ip_address varchar(45) NOT NULL,
  user_agent text,
  request_method varchar(10) NOT NULL,
  request_url varchar(500) NOT NULL,
  request_params text,
  response_status int NOT NULL,
  response_time_ms int DEFAULT NULL,
  session_id varchar(255) DEFAULT NULL,
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
-- SQLINES DEMO *** table `access_logs`
--

LOCK TABLES access_logs WRITE;
/* SQLINES DEMO *** LE `access_logs` DISABLE KEYS */;
/* SQLINES DEMO *** LE `access_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `ai_agents`
--

DROP TABLE IF EXISTS ai_agents;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE ai_agents (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  name varchar(100) NOT NULL,
  provider varchar(50) NOT NULL,
  model varchar(100) NOT NULL,
  api_key varchar(255) DEFAULT NULL,
  webhook_url varchar(255) DEFAULT NULL,
  role varchar(50) DEFAULT NULL,
  enabled smallint NOT NULL DEFAULT '1',
  status varchar(30) check (status in ('ACTIVE','INACTIVE')) NOT NULL DEFAULT 'ACTIVE',
  description text,
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
-- SQLINES DEMO *** table `ai_agents`
--

LOCK TABLES ai_agents WRITE;
/* SQLINES DEMO *** LE `ai_agents` DISABLE KEYS */;
INSERT INTO ai_agents VALUES (1,'ChatGPT-5','OpenAI','gpt-5',NULL,NULL,NULL,1,'ACTIVE',NULL,'2025-09-03 18:18:08','2025-09-03 18:23:31'),(2,'DeepSeek 3','Deep Seek','deepseek-3',NULL,NULL,NULL,1,'ACTIVE',NULL,'2025-09-03 18:23:01','2025-09-03 18:23:01');
/* SQLINES DEMO *** LE `ai_agents` ENABLE KEYS */;
UNLOCK TABLES;

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
  title varchar(200) NOT NULL,
  last_message text,
  status varchar(30) check (status in ('ACTIVE','ARCHIVED')) NOT NULL DEFAULT 'ACTIVE',
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
-- SQLINES DEMO *** table `ai_chats`
--

LOCK TABLES ai_chats WRITE;
/* SQLINES DEMO *** LE `ai_chats` DISABLE KEYS */;
INSERT INTO ai_chats VALUES (1,1,NULL,'New Chat',NULL,'ACTIVE','2025-09-03 18:22:09','2025-09-03 18:22:09');
/* SQLINES DEMO *** LE `ai_chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `certificates`
--

DROP TABLE IF EXISTS certificates;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE certificates (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  certificate_name varchar(255) NOT NULL,
  certificate_type varchar(30) check (certificate_type in ('BRONZE','SILVER','GOLD','PLATINUM','DIAMOND')) NOT NULL,
  description text,
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
-- SQLINES DEMO *** table `certificates`
--

LOCK TABLES certificates WRITE;
/* SQLINES DEMO *** LE `certificates` DISABLE KEYS */;
INSERT INTO certificates VALUES (5,'Diamond Member','DIAMOND','Ultimate membership with exclusive benefits',250.00,365,'["Weekly newsletter", "Priority support", "Exclusive content", "VIP events", "Personal advisor", "Custom services"]',0,NULL,0,1,'2025-08-24 21:57:58','2025-08-30 20:59:50'),(13,'100k','DIAMOND','100k',100000.00,365,'["free journals", "5% jackpots"]',1,NULL,0,1,'2025-08-30 20:59:04','2025-08-30 21:39:38'),(19,'1million','BRONZE','1m',1000000.00,365,'["5%"]',1,5,0,1,'2025-08-30 21:40:44','2025-08-30 21:40:44');
/* SQLINES DEMO *** LE `certificates` ENABLE KEYS */;
UNLOCK TABLES;

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
-- SQLINES DEMO *** table `drawing_participants`
--

LOCK TABLES drawing_participants WRITE;
/* SQLINES DEMO *** LE `drawing_participants` DISABLE KEYS */;
/* SQLINES DEMO *** LE `drawing_participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `error_logs`
--

DROP TABLE IF EXISTS error_logs;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE error_logs (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint DEFAULT NULL,
  error_level varchar(30) check (error_level in ('DEBUG','INFO','WARNING','ERROR','CRITICAL')) NOT NULL,
  error_type varchar(100) NOT NULL,
  error_message text NOT NULL,
  stack_trace text,
  request_url varchar(500) DEFAULT NULL,
  request_method varchar(10) DEFAULT NULL,
  ip_address varchar(45) DEFAULT NULL,
  user_agent text,
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
-- SQLINES DEMO *** table `error_logs`
--

LOCK TABLES error_logs WRITE;
/* SQLINES DEMO *** LE `error_logs` DISABLE KEYS */;
/* SQLINES DEMO *** LE `error_logs` ENABLE KEYS */;
UNLOCK TABLES;

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
  ip_address varchar(45) DEFAULT NULL,
  user_agent text,
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
-- SQLINES DEMO *** table `journal_views`
--

LOCK TABLES journal_views WRITE;
/* SQLINES DEMO *** LE `journal_views` DISABLE KEYS */;
/* SQLINES DEMO *** LE `journal_views` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `journals`
--

DROP TABLE IF EXISTS journals;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE journals (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  week_number int NOT NULL,
  title varchar(255) NOT NULL,
  excerpt text,
  content text NOT NULL,
  status varchar(30) check (status in ('DRAFT','PUBLISHED','SCHEDULED')) NOT NULL DEFAULT 'DRAFT',
  featured smallint NOT NULL DEFAULT '0',
  tags varchar(500) DEFAULT NULL,
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
-- SQLINES DEMO *** table `journals`
--

LOCK TABLES journals WRITE;
/* SQLINES DEMO *** LE `journals` DISABLE KEYS */;
INSERT INTO journals VALUES (1,1,'Welcome to BigWater Digital Weekly Journal','Introduction to our digital platform and what to expect','<h1>Welcome to BigWater</h1><p>This is our first weekly journal. We are excited to bring you the latest insights and updates.</p>','PUBLISHED',1,'welcome,introduction',0,NULL,1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(2,2,'Market Trends and Analysis','Weekly market analysis and trends','<h1>Market Trends</h1><p>This week we analyze the latest market trends and provide insights for investors.</p>','PUBLISHED',0,'market,trends,analysis',0,NULL,1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(3,3,'Technology Updates','Latest technology news and updates','<h1>Technology Updates</h1><p>Stay updated with the latest technology news and developments.</p>','DRAFT',0,'technology,updates,news',0,NULL,1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(7,38,'test changes','tttt','ttttt','DRAFT',0,'',0,'2025-08-25 07:00:00',1,'2025-08-25 13:09:56','2025-08-25 13:10:22');
/* SQLINES DEMO *** LE `journals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `random_drawings`
--

DROP TABLE IF EXISTS random_drawings;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE random_drawings (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  drawing_name varchar(255) NOT NULL,
  drawing_type varchar(30) check (drawing_type in ('SWEEPSTAKES','RAFFLE','CONTEST')) CHARACTER SET utf8mb4 NOT NULL,
  week_number int NOT NULL,
  drawing_date timestamp(0) NOT NULL,
  prize_pool decimal(15,2) NOT NULL DEFAULT '0.00',
  winning_numbers json DEFAULT NULL,
  status varchar(30) check (status in ('PENDING','COMPLETED','CANCELLED')) NOT NULL DEFAULT 'PENDING',
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
-- SQLINES DEMO *** table `random_drawings`
--

LOCK TABLES random_drawings WRITE;
/* SQLINES DEMO *** LE `random_drawings` DISABLE KEYS */;
INSERT INTO random_drawings VALUES (9,'sept 5','RAFFLE',1,'2025-09-06 00:19:00',1000000.00,'[2, 4, 14, 47, 67, 12]','PENDING',0,1,'2025-09-04 00:19:26','2025-09-04 00:25:22'),(10,'9-12-2025','SWEEPSTAKES',1,'2025-09-13 01:47:00',3000000.00,'[34, 37, 39, 50, 67, 24]','PENDING',0,1,'2025-09-04 01:47:50','2025-09-04 01:47:50');
/* SQLINES DEMO *** LE `random_drawings` ENABLE KEYS */;
UNLOCK TABLES;

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
  status varchar(30) check (status in ('ACTIVE','INACTIVE','CANCELLED')) NOT NULL DEFAULT 'ACTIVE',
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
-- SQLINES DEMO *** table `referral_relationships`
--

LOCK TABLES referral_relationships WRITE;
/* SQLINES DEMO *** LE `referral_relationships` DISABLE KEYS */;
/* SQLINES DEMO *** LE `referral_relationships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `system_settings`
--

DROP TABLE IF EXISTS system_settings;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE system_settings (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  setting_key varchar(100) NOT NULL,
  setting_value text NOT NULL,
  setting_type varchar(30) check (setting_type in ('STRING','NUMBER','BOOLEAN','JSON')) NOT NULL DEFAULT 'STRING',
  description text,
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
-- SQLINES DEMO *** table `system_settings`
--

LOCK TABLES system_settings WRITE;
/* SQLINES DEMO *** LE `system_settings` DISABLE KEYS */;
INSERT INTO system_settings VALUES (1,'company_referral_code','COMPANY001','STRING','Default company referral code',1,'2025-08-24 21:57:53','2025-08-24 21:57:53'),(2,'default_commission_rate','0.1000','NUMBER','Default referral commission rate (10%)',1,'2025-08-24 21:57:53','2025-08-24 21:57:53'),(3,'min_withdrawal_amount','10.00','NUMBER','Minimum withdrawal amount in USDT',1,'2025-08-24 21:57:53','2025-08-24 21:57:53'),(4,'max_referral_levels','3','NUMBER','Maximum referral levels for commission',1,'2025-08-24 21:57:53','2025-08-24 21:57:53'),(5,'drawing_entry_fee','1.00','NUMBER','Default entry fee for drawings',1,'2025-08-24 21:57:53','2025-08-24 21:57:53'),(6,'system_maintenance_mode','false','BOOLEAN','System maintenance mode',1,'2025-08-24 21:57:53','2025-08-24 21:57:53'),(7,'max_wallets_per_user','5','NUMBER','Maximum number of wallets per user',1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(8,'certificate_purchase_limit','10','NUMBER','Maximum certificates a user can purchase',1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(9,'drawing_participation_limit','100','NUMBER','Maximum participants per drawing',1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(10,'referral_bonus_usdt','5.00','NUMBER','Bonus USDT for successful referrals',1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(11,'weekly_newsletter_enabled','true','BOOLEAN','Enable weekly newsletter feature',1,'2025-08-24 21:57:58','2025-08-24 21:57:58'),(12,'maintenance_mode_message','System is under maintenance. Please try again later.','STRING','Message shown during maintenance mode',1,'2025-08-24 21:57:58','2025-08-24 21:57:58');
/* SQLINES DEMO *** LE `system_settings` ENABLE KEYS */;
UNLOCK TABLES;

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
  transaction_hash varchar(255) DEFAULT NULL,
  status varchar(30) check (status in ('PENDING','COMPLETED','FAILED','CANCELLED')) NOT NULL DEFAULT 'PENDING',
  description text,
  related_entity_type varchar(50) DEFAULT NULL,
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
-- SQLINES DEMO *** table `transactions`
--

LOCK TABLES transactions WRITE;
/* SQLINES DEMO *** LE `transactions` DISABLE KEYS */;
INSERT INTO transactions VALUES (14,1,NULL,'DEPOSIT',13333333333.00000000,NULL,'COMPLETED','购买令牌',NULL,NULL,NULL,'2025-09-03 16:48:07','2025-09-03 16:49:32',15),(15,1,NULL,'WINNING',131313.00000000,NULL,'PENDING','subscribe for 9',NULL,NULL,NULL,'2025-09-04 03:08:23','2025-09-04 03:08:23',15);
/* SQLINES DEMO *** LE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `usdt_wallets`
--

DROP TABLE IF EXISTS usdt_wallets;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE usdt_wallets (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint NOT NULL,
  wallet_address varchar(255) NOT NULL,
  wallet_name varchar(100) DEFAULT 'Main Wallet',
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
-- SQLINES DEMO *** table `usdt_wallets`
--

LOCK TABLES usdt_wallets WRITE;
/* SQLINES DEMO *** LE `usdt_wallets` DISABLE KEYS */;
INSERT INTO usdt_wallets VALUES (1,1,'MAIN_1','C','COMPANY',190.00000000,1,0,'2025-08-25 13:33:21','2025-09-03 13:52:32'),(3,1,'bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq','C3','COMPANY',0.00000000,1,0,'2025-08-25 13:33:40','2025-09-03 13:46:18'),(4,1,'TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBe','Company Fund','COMPANY',0.00000000,1,0,'2025-08-25 13:33:47','2025-09-03 13:49:04'),(5,1,'TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBz','Company Fund Wallet','COMPANY',849.75000000,0,0,'2025-08-25 13:43:27','2025-09-05 22:19:53'),(6,1,'COMPANY_FUND_002','Company Fund Wallet 2','COMPANY',0.00000000,1,0,'2025-08-25 13:43:34','2025-09-05 22:19:50'),(8,1,'updated123','Updated Test Wallet','COMPANY',150.75000000,1,0,'2025-08-25 13:59:23','2025-09-03 01:37:05'),(10,1,'test789','Updated Test Wallet','COMPANY',600.25000000,1,0,'2025-08-25 14:22:14','2025-09-03 01:37:05'),(12,1,'test_nullable_001','Auto-created Wallet','MEMBER',900.00000000,0,0,'2025-08-25 15:57:43','2025-09-03 01:44:06'),(15,1,'0x742d35Cc6634C0532925a3b844Bc454e4438f44e','C1','COMPANY',3000000.00000000,1,0,'2025-08-25 16:23:02','2025-09-04 03:22:14'),(20,1,'','','COMPANY',0.00000000,1,0,'2025-09-05 22:08:51','2025-09-05 22:08:51');
/* SQLINES DEMO *** LE `usdt_wallets` ENABLE KEYS */;
UNLOCK TABLES;

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
  status varchar(30) check (status in ('ACTIVE','EXPIRED','CANCELLED')) NOT NULL DEFAULT 'ACTIVE',
  purchase_amount_usdt decimal(15,2) NOT NULL,
  transaction_hash varchar(255) DEFAULT NULL,
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
-- SQLINES DEMO *** table `user_certificates`
--

LOCK TABLES user_certificates WRITE;
/* SQLINES DEMO *** LE `user_certificates` DISABLE KEYS */;
/* SQLINES DEMO *** LE `user_certificates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- SQLINES DEMO *** or table `users`
--

DROP TABLE IF EXISTS users;
/* SQLINES DEMO *** d_cs_client     = @@character_set_client */;
/* SQLINES DEMO *** cter_set_client = utf8mb4 */;
CREATE TABLE users (
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  email varchar(255) NOT NULL,
  password_hash varchar(255) NOT NULL,
  first_name varchar(100) DEFAULT NULL,
  last_name varchar(100) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  role varchar(30) check (role in ('SUBSCRIBER','ADMIN','SUPER_ADMIN')) NOT NULL DEFAULT 'SUBSCRIBER',
  status varchar(30) check (status in ('ACTIVE','INACTIVE','SUSPENDED')) NOT NULL DEFAULT 'ACTIVE',
  referral_code varchar(20) NOT NULL,
  referred_by_code varchar(20) DEFAULT 'COMPANY001',
  join_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_login timestamp(0) NULL DEFAULT NULL,
  email_verified_at timestamp(0) NULL DEFAULT NULL,
  created_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP /* ON UPDATE CURRENT_TIMESTAMP */,
  level varchar(20) DEFAULT 'CHIEF',
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
-- SQLINES DEMO *** table `users`
--

LOCK TABLES users WRITE;
/* SQLINES DEMO *** LE `users` DISABLE KEYS */;
INSERT INTO users VALUES (1,'company@bigwater.com','5EZ3F7u6Jt7TpRa96eWY5SR/ZQg7wFLzV1U=','BigWater','Company','+1234567890','SUPER_ADMIN','ACTIVE','COMPANY001',NULL,'2025-08-24 21:57:53','2025-09-08 19:42:25',NULL,'2025-08-24 21:57:53','2025-09-08 19:42:25','PRESIDENT'),(40,'america@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','2PTWDLY0','COMPANY001','2025-09-06 00:00:03',NULL,NULL,'2025-09-06 00:00:03','2025-09-06 00:04:37','PRESIDENT'),(41,'asia@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','9B0BDPM4','COMPANY001','2025-09-06 00:01:51',NULL,NULL,'2025-09-06 00:01:51','2025-09-06 00:05:10','PRESIDENT'),(42,'eu@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','9ENAB8X2','COMPANY001','2025-09-06 00:02:24',NULL,NULL,'2025-09-06 00:02:24','2025-09-06 00:05:37','PRESIDENT'),(43,'africa@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','4MZB14RE','COMPANY001','2025-09-06 00:02:41',NULL,NULL,'2025-09-06 00:02:41','2025-09-06 00:06:01','PRESIDENT'),(44,'australia@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','BFCFZT9V','COMPANY001','2025-09-06 00:03:03',NULL,NULL,'2025-09-06 00:03:03','2025-09-06 00:06:19','PRESIDENT'),(45,'china@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','VDBC2PX0','9B0BDPM4','2025-09-06 00:07:23','2025-09-06 15:52:29',NULL,'2025-09-06 00:07:23','2025-09-06 15:52:29','PRESIDENT'),(46,'ne-china@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','80J0VU99','VDBC2PX0','2025-09-06 00:09:42',NULL,NULL,'2025-09-06 00:09:42','2025-09-06 00:12:40','PRESIDENT'),(47,'w-china@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','A0QTPOO7','VDBC2PX0','2025-09-06 00:12:09',NULL,NULL,'2025-09-06 00:12:09','2025-09-06 00:12:29','PRESIDENT'),(48,'e-china@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','LXKDTWTG','VDBC2PX0','2025-09-06 00:13:07',NULL,NULL,'2025-09-06 00:13:07','2025-09-06 00:14:30','PRESIDENT'),(49,'s-china@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','0NJTNK63','VDBC2PX0','2025-09-06 00:14:12',NULL,NULL,'2025-09-06 00:14:12','2025-09-06 00:14:21','PRESIDENT'),(50,'c-china@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','A13GUTE3','VDBC2PX0','2025-09-06 00:14:59',NULL,NULL,'2025-09-06 00:14:59','2025-09-06 00:15:10','PRESIDENT'),(51,'beijing@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','EW4V4T5Z','80J0VU99','2025-09-06 00:15:52',NULL,NULL,'2025-09-06 00:15:52','2025-09-06 00:20:02','PRESIDENT'),(52,'shanghai@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','LRXN36VE','LXKDTWTG','2025-09-06 00:22:42',NULL,NULL,'2025-09-06 00:22:42','2025-09-06 00:22:42','PRESIDENT'),(53,'guangdong@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','72UP9QWL','0NJTNK63','2025-09-06 00:39:54',NULL,NULL,'2025-09-06 00:39:54','2025-09-06 09:46:16','PRESIDENT'),(54,'tianjing@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','32L7R0TM','80J0VU99','2025-09-06 00:40:53',NULL,NULL,'2025-09-06 00:40:53','2025-09-06 00:40:53','PRESIDENT'),(55,'heilongjiang@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','06RTWOCA','80J0VU99','2025-09-06 00:41:41',NULL,NULL,'2025-09-06 00:41:41','2025-09-06 00:41:41','PRESIDENT'),(56,'jilin@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ICIR3DDW','80J0VU99','2025-09-06 00:42:08',NULL,NULL,'2025-09-06 00:42:08','2025-09-06 00:42:08','PRESIDENT'),(57,'liaoning@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','DB1P6LTF','80J0VU99','2025-09-06 00:42:27',NULL,NULL,'2025-09-06 00:42:27','2025-09-06 00:42:27','PRESIDENT'),(58,'shandong@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','2YDKE8HL','LXKDTWTG','2025-09-06 00:42:52',NULL,NULL,'2025-09-06 00:42:52','2025-09-06 00:42:52','PRESIDENT'),(59,'jiangxi@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','AADYEG3K','A13GUTE3','2025-09-06 00:43:20',NULL,NULL,'2025-09-06 00:43:20','2025-09-06 10:02:58','PRESIDENT'),(60,'japan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','41Q77MGY','9B0BDPM4','2025-09-06 00:57:10',NULL,NULL,'2025-09-06 00:57:10','2025-09-06 00:57:10','PRESIDENT'),(61,'taiwan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','R3NG9KO4','9B0BDPM4','2025-09-06 00:57:45',NULL,NULL,'2025-09-06 00:57:45','2025-09-06 00:57:45','PRESIDENT'),(62,'chongqing@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','OEVZ8LES','A0QTPOO7','2025-09-06 08:11:53',NULL,NULL,'2025-09-06 08:11:53','2025-09-06 08:11:53','PRESIDENT'),(63,'hainan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','EWHTU1VU','0NJTNK63','2025-09-06 08:34:52',NULL,NULL,'2025-09-06 08:34:52','2025-09-06 09:46:55','PRESIDENT'),(64,'hubei@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','PCFI8NBW','A13GUTE3','2025-09-06 09:04:44',NULL,NULL,'2025-09-06 09:04:44','2025-09-06 09:19:59','PRESIDENT'),(65,'northamerica@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','IFWL9FOF','2PTWDLY0','2025-09-06 09:10:08',NULL,NULL,'2025-09-06 09:10:08','2025-09-06 09:10:08','PRESIDENT'),(66,'centralamerica@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','YFKPEVAW','2PTWDLY0','2025-09-06 09:10:38',NULL,NULL,'2025-09-06 09:10:38','2025-09-06 09:10:38','PRESIDENT'),(67,'southamerica@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','397S4FB6','2PTWDLY0','2025-09-06 09:11:01',NULL,NULL,'2025-09-06 09:11:01','2025-09-06 09:11:01','PRESIDENT'),(68,'na1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','PCNRPXSA','IFWL9FOF','2025-09-06 09:11:26',NULL,NULL,'2025-09-06 09:11:26','2025-09-06 11:43:41','PRESIDENT'),(69,'na2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','C9RH0L7N','IFWL9FOF','2025-09-06 09:11:50',NULL,NULL,'2025-09-06 09:11:50','2025-09-06 11:43:51','PRESIDENT'),(70,'hunan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','MRI1N5EA','A13GUTE3','2025-09-06 09:24:16',NULL,NULL,'2025-09-06 09:24:16','2025-09-06 09:24:16','PRESIDENT'),(71,'jiangsu@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','FML2MOGT','LXKDTWTG','2025-09-06 09:26:09',NULL,NULL,'2025-09-06 09:26:09','2025-09-06 09:26:09','PRESIDENT'),(72,'zhejiang@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','SYXJ2O8I','LXKDTWTG','2025-09-06 09:26:47',NULL,NULL,'2025-09-06 09:26:47','2025-09-06 09:26:47','PRESIDENT'),(73,'anhui@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','15L6C50F','A13GUTE3','2025-09-06 09:27:06',NULL,NULL,'2025-09-06 09:27:06','2025-09-06 09:54:48','PRESIDENT'),(74,'fujian@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','O3I47AB7','LXKDTWTG','2025-09-06 09:32:51',NULL,NULL,'2025-09-06 09:32:51','2025-09-06 09:32:51','PRESIDENT'),(75,'sichuan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','GLULP00F','A0QTPOO7','2025-09-06 09:34:40',NULL,NULL,'2025-09-06 09:34:40','2025-09-06 09:34:40','PRESIDENT'),(76,'neimeng@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','05VY3HRT','A0QTPOO7','2025-09-06 09:35:25',NULL,NULL,'2025-09-06 09:35:25','2025-09-06 09:35:25','PRESIDENT'),(77,'guangxi@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','475HTONC','0NJTNK63','2025-09-06 09:35:44',NULL,NULL,'2025-09-06 09:35:44','2025-09-06 14:59:03','PRESIDENT'),(78,'guizhou@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','4HC93HP7','0NJTNK63','2025-09-06 09:36:24',NULL,NULL,'2025-09-06 09:36:24','2025-09-06 15:32:43','PRESIDENT'),(79,'yunnan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','DV35D4PG','0NJTNK63','2025-09-06 09:36:44',NULL,NULL,'2025-09-06 09:36:44','2025-09-06 15:41:56','PRESIDENT'),(80,'xizang@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','T0ZHJPER','A0QTPOO7','2025-09-06 09:37:03',NULL,NULL,'2025-09-06 09:37:03','2025-09-06 09:37:03','PRESIDENT'),(81,'shannxi@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','SNA0AL47','A0QTPOO7','2025-09-06 09:37:46',NULL,NULL,'2025-09-06 09:37:46','2025-09-06 09:37:46','PRESIDENT'),(82,'gansu@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','D6JXC6IQ','A0QTPOO7','2025-09-06 09:38:16',NULL,NULL,'2025-09-06 09:38:16','2025-09-06 09:38:16','PRESIDENT'),(83,'qinghai@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','5QC5FC4R','A0QTPOO7','2025-09-06 09:38:40',NULL,NULL,'2025-09-06 09:38:40','2025-09-06 09:38:40','PRESIDENT'),(84,'ningxia@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','FM91O26R','A0QTPOO7','2025-09-06 09:39:31',NULL,NULL,'2025-09-06 09:39:31','2025-09-06 09:39:31','PRESIDENT'),(85,'xinjiang@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','I0EQ4MAG','A0QTPOO7','2025-09-06 09:39:53',NULL,NULL,'2025-09-06 09:39:53','2025-09-06 09:39:53','PRESIDENT'),(86,'henan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','1P5CCP4G','A13GUTE3','2025-09-06 10:07:00',NULL,NULL,'2025-09-06 10:07:00','2025-09-06 10:07:00','PRESIDENT'),(87,'shanxi@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','UND3GW7D','A13GUTE3','2025-09-06 10:07:22',NULL,NULL,'2025-09-06 10:07:22','2025-09-06 10:07:22','PRESIDENT'),(88,'hebei@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','C55LDCBW','80J0VU99','2025-09-06 10:10:11',NULL,NULL,'2025-09-06 10:10:11','2025-09-06 10:10:11','PRESIDENT'),(89,'singapore@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','93X90L69','9B0BDPM4','2025-09-06 10:12:33',NULL,NULL,'2025-09-06 10:12:33','2025-09-06 10:12:33','PRESIDENT'),(90,'korean@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','RI6DERSJ','9B0BDPM4','2025-09-06 10:13:26',NULL,NULL,'2025-09-06 10:13:26','2025-09-06 10:13:26','PRESIDENT'),(91,'germany@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','1SI0MAUI','9ENAB8X2','2025-09-06 10:15:25',NULL,NULL,'2025-09-06 10:15:25','2025-09-06 10:15:25','PRESIDENT'),(92,'france@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','WR692RM4','9ENAB8X2','2025-09-06 10:15:50',NULL,NULL,'2025-09-06 10:15:50','2025-09-06 10:15:50','PRESIDENT'),(93,'italy@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','3ZY7898L','9ENAB8X2','2025-09-06 10:16:15',NULL,NULL,'2025-09-06 10:16:15','2025-09-06 10:16:15','PRESIDENT'),(94,'spain@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','JM4C5NE1','9ENAB8X2','2025-09-06 10:17:09',NULL,NULL,'2025-09-06 10:17:09','2025-09-06 10:17:09','PRESIDENT'),(95,'poland@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','35TYENZG','9ENAB8X2','2025-09-06 10:17:28',NULL,NULL,'2025-09-06 10:17:28','2025-09-06 10:17:28','PRESIDENT'),(96,'east-eu@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','E3PX5EGK','9ENAB8X2','2025-09-06 10:18:25',NULL,NULL,'2025-09-06 10:18:25','2025-09-06 10:18:25','PRESIDENT'),(97,'southafrica@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','S85I12D3','4MZB14RE','2025-09-06 10:20:58',NULL,NULL,'2025-09-06 10:20:58','2025-09-06 10:20:58','PRESIDENT'),(98,'niriliya@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','2IGD9KIA','4MZB14RE','2025-09-06 10:23:12',NULL,NULL,'2025-09-06 10:23:12','2025-09-06 10:23:12','PRESIDENT'),(99,'esaiebiya@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ET520TU8','4MZB14RE','2025-09-06 10:23:54',NULL,NULL,'2025-09-06 10:23:54','2025-09-06 10:23:54','PRESIDENT'),(100,'egypt@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','IL2JZ19C','4MZB14RE','2025-09-06 10:24:27',NULL,NULL,'2025-09-06 10:24:27','2025-09-06 10:24:27','PRESIDENT'),(101,'gangguo@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','RWVN0U2I','4MZB14RE','2025-09-06 10:25:02',NULL,NULL,'2025-09-06 10:25:02','2025-09-06 10:25:02','PRESIDENT'),(102,'xinxilan@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','1J551XAW','BFCFZT9V','2025-09-06 10:26:24',NULL,NULL,'2025-09-06 10:26:24','2025-09-06 10:26:24','PRESIDENT'),(103,'a1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','GFO5Y45G','BFCFZT9V','2025-09-06 10:26:58',NULL,NULL,'2025-09-06 10:26:58','2025-09-06 10:27:57','PRESIDENT'),(104,'a2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','TQRLRF0B','BFCFZT9V','2025-09-06 10:27:37',NULL,NULL,'2025-09-06 10:27:37','2025-09-06 10:27:37','PRESIDENT'),(105,'a3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','H6AS822L','BFCFZT9V','2025-09-06 10:28:19',NULL,NULL,'2025-09-06 10:28:19','2025-09-06 10:28:19','PRESIDENT'),(106,'a4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','BESRNZ29','BFCFZT9V','2025-09-06 10:29:10',NULL,NULL,'2025-09-06 10:29:10','2025-09-06 10:29:10','PRESIDENT'),(107,'brazil@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','AJJFM3KF','2PTWDLY0','2025-09-06 10:29:50',NULL,NULL,'2025-09-06 10:29:50','2025-09-06 10:29:50','PRESIDENT'),(108,'peru@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','NG557IEK','2PTWDLY0','2025-09-06 10:30:15',NULL,NULL,'2025-09-06 10:30:15','2025-09-06 10:30:15','PRESIDENT'),(109,'ax1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','59312BM5','1J551XAW','2025-09-06 10:41:23',NULL,NULL,'2025-09-06 10:41:23','2025-09-06 10:41:23','PRESIDENT'),(110,'ax2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','RDDRZOSB','1J551XAW','2025-09-06 10:42:05',NULL,NULL,'2025-09-06 10:42:05','2025-09-06 10:42:05','PRESIDENT'),(111,'ax3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','HX95J47Y','1J551XAW','2025-09-06 10:42:28',NULL,NULL,'2025-09-06 10:42:28','2025-09-06 10:42:28','PRESIDENT'),(112,'ax4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','MPDQZBHC','1J551XAW','2025-09-06 10:42:45',NULL,NULL,'2025-09-06 10:42:45','2025-09-06 10:42:45','PRESIDENT'),(113,'ax5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','T0C5SWIJ','1J551XAW','2025-09-06 10:43:04',NULL,NULL,'2025-09-06 10:43:04','2025-09-06 10:43:04','PRESIDENT'),(114,'aa11@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','443MZ2FH','GFO5Y45G','2025-09-06 10:44:25',NULL,NULL,'2025-09-06 10:44:25','2025-09-06 10:46:47','PRESIDENT'),(115,'aa12@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','7TI4PQBU','GFO5Y45G','2025-09-06 10:44:43',NULL,NULL,'2025-09-06 10:44:43','2025-09-06 10:52:12','PRESIDENT'),(116,'aa13@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','FYYYSHBF','GFO5Y45G','2025-09-06 10:45:05',NULL,NULL,'2025-09-06 10:45:05','2025-09-06 10:52:24','PRESIDENT'),(117,'aa14@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','PDLHWL5U','GFO5Y45G','2025-09-06 10:45:22',NULL,NULL,'2025-09-06 10:45:22','2025-09-06 10:52:36','PRESIDENT'),(118,'aa15@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','8Z93O579','GFO5Y45G','2025-09-06 10:45:40',NULL,NULL,'2025-09-06 10:45:40','2025-09-06 10:52:56','PRESIDENT'),(119,'a21@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','GNXTZ09A','TQRLRF0B','2025-09-06 11:21:18',NULL,NULL,'2025-09-06 11:21:18','2025-09-06 11:21:18','CHIEF'),(120,'a22@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','HNEGEKEQ','TQRLRF0B','2025-09-06 11:23:07',NULL,NULL,'2025-09-06 11:23:07','2025-09-06 11:23:07','CHIEF'),(121,'a23@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','LLKNJUNM','TQRLRF0B','2025-09-06 11:23:55',NULL,NULL,'2025-09-06 11:23:55','2025-09-06 11:23:55','CHIEF'),(122,'a24@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','98ZOWKAS','TQRLRF0B','2025-09-06 11:24:13',NULL,NULL,'2025-09-06 11:24:13','2025-09-06 11:24:13','CHIEF'),(123,'a25@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','8GU2RE2L','TQRLRF0B','2025-09-06 11:25:23',NULL,NULL,'2025-09-06 11:25:23','2025-09-06 11:25:23','CHIEF'),(124,'a31@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','UDDMS9CA','H6AS822L','2025-09-06 11:27:48',NULL,NULL,'2025-09-06 11:27:48','2025-09-06 11:27:48','CHIEF'),(125,'a32@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','E7QQ0KMO','H6AS822L','2025-09-06 11:28:32',NULL,NULL,'2025-09-06 11:28:32','2025-09-06 11:28:32','CHIEF'),(126,'a33@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','EDEZPMFS','H6AS822L','2025-09-06 11:29:00',NULL,NULL,'2025-09-06 11:29:00','2025-09-06 11:29:00','CHIEF'),(127,'a34@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','SOSVBEUE','H6AS822L','2025-09-06 11:29:23',NULL,NULL,'2025-09-06 11:29:23','2025-09-06 11:29:23','CHIEF'),(128,'a35@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','IR71MKKK','H6AS822L','2025-09-06 11:30:37',NULL,NULL,'2025-09-06 11:30:37','2025-09-06 11:30:37','CHIEF'),(129,'a41@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','0O5L41AQ','BESRNZ29','2025-09-06 11:33:55',NULL,NULL,'2025-09-06 11:33:55','2025-09-06 11:33:55','CHIEF'),(130,'a42@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','5WS1IH6E','BESRNZ29','2025-09-06 11:34:23',NULL,NULL,'2025-09-06 11:34:23','2025-09-06 11:34:23','CHIEF'),(131,'a43@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','05Q8N9CG','BESRNZ29','2025-09-06 11:34:45',NULL,NULL,'2025-09-06 11:34:45','2025-09-06 11:34:45','CHIEF'),(132,'a44@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','1O6GRQPC','BESRNZ29','2025-09-06 11:35:16',NULL,NULL,'2025-09-06 11:35:16','2025-09-06 11:35:16','CHIEF'),(133,'a45@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==',NULL,NULL,NULL,'SUBSCRIBER','ACTIVE','KS1V7WZ7','BESRNZ29','2025-09-06 11:35:43',NULL,NULL,'2025-09-06 11:35:43','2025-09-06 11:35:43','CHIEF'),(134,'na3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','TUM0YPC0','IFWL9FOF','2025-09-06 11:37:52',NULL,NULL,'2025-09-06 11:37:52','2025-09-06 11:37:52','PRESIDENT'),(135,'na4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','RQQT788K','IFWL9FOF','2025-09-06 11:38:08',NULL,NULL,'2025-09-06 11:38:08','2025-09-06 11:38:08','PRESIDENT'),(136,'na5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','6IL8O6BT','IFWL9FOF','2025-09-06 11:38:25',NULL,NULL,'2025-09-06 11:38:25','2025-09-06 11:38:25','PRESIDENT'),(137,'k1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','LPGH0O36','RI6DERSJ','2025-09-06 11:46:06',NULL,NULL,'2025-09-06 11:46:06','2025-09-06 11:46:06','PRESIDENT'),(138,'k2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','CQJO5T6P','RI6DERSJ','2025-09-06 11:46:23',NULL,NULL,'2025-09-06 11:46:23','2025-09-06 11:46:23','PRESIDENT'),(139,'k3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','WKJZ8BV3','RI6DERSJ','2025-09-06 11:46:41',NULL,NULL,'2025-09-06 11:46:41','2025-09-06 11:46:41','PRESIDENT'),(140,'k4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','HHADLAAE','RI6DERSJ','2025-09-06 11:46:56',NULL,NULL,'2025-09-06 11:46:56','2025-09-06 11:46:56','PRESIDENT'),(141,'k5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','','','SUBSCRIBER','ACTIVE','A63RIYJV','AGTYZFVE','2025-09-06 11:47:14',NULL,NULL,'2025-09-06 11:47:14','2025-09-06 15:46:10','PRESIDENT'),(142,'shenzhen@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','OG9G0JCD','0NJTNK63','2025-09-06 11:49:27',NULL,NULL,'2025-09-06 11:49:27','2025-09-06 11:49:27','PRESIDENT'),(143,'hk@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','AGTYZFVE','9B0BDPM4','2025-09-06 11:51:12',NULL,NULL,'2025-09-06 11:51:12','2025-09-06 11:51:12','PRESIDENT'),(144,'hk1@6768688.com','thspU++7lyte2Re25HY3zXHWfjXr','','',NULL,'SUBSCRIBER','ACTIVE','I1JHOTKV','AGTYZFVE','2025-09-06 11:51:32',NULL,NULL,'2025-09-06 11:51:32','2025-09-06 11:51:32','PRESIDENT'),(145,'hk2@6768688.com','thspU++7lyte2Re25HY3zXHWfjXr','','',NULL,'SUBSCRIBER','ACTIVE','KRBJ0O5J','AGTYZFVE','2025-09-06 11:51:48',NULL,NULL,'2025-09-06 11:51:48','2025-09-06 11:51:48','PRESIDENT'),(146,'hk3@6768688.com','thspU++7lyte2Re25HY3zXHWfjXr','','',NULL,'SUBSCRIBER','ACTIVE','VVJXVXOU','AGTYZFVE','2025-09-06 11:52:02',NULL,NULL,'2025-09-06 11:52:02','2025-09-06 11:52:02','PRESIDENT'),(147,'hk4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','UEUFR8FM','AGTYZFVE','2025-09-06 11:52:19',NULL,NULL,'2025-09-06 11:52:19','2025-09-06 11:52:19','PRESIDENT'),(148,'hk5@6768688.com','thspU++7lyte2Re25HY3zXHWfjXr','','','','SUBSCRIBER','ACTIVE','QIFBWXWC','RI6DERSJ','2025-09-06 11:52:32',NULL,NULL,'2025-09-06 11:52:32','2025-09-06 15:45:28','PRESIDENT'),(149,'tw1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','4W2BHJOI','R3NG9KO4','2025-09-06 11:53:08',NULL,NULL,'2025-09-06 11:53:08','2025-09-06 11:53:08','PRESIDENT'),(150,'tw2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','3CT5DGWL','R3NG9KO4','2025-09-06 11:53:28',NULL,NULL,'2025-09-06 11:53:28','2025-09-06 11:53:28','PRESIDENT'),(151,'tw3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','X9X04R94','R3NG9KO4','2025-09-06 11:53:41',NULL,NULL,'2025-09-06 11:53:41','2025-09-06 11:53:41','PRESIDENT'),(152,'tw4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','QEOTDP4I','R3NG9KO4','2025-09-06 11:53:56',NULL,NULL,'2025-09-06 11:53:56','2025-09-06 11:53:56','PRESIDENT'),(153,'tw5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','7V20XONT','R3NG9KO4','2025-09-06 11:54:10',NULL,NULL,'2025-09-06 11:54:10','2025-09-06 11:54:10','PRESIDENT'),(154,'sg1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','D4QLZ5PN','93X90L69','2025-09-06 11:54:41',NULL,NULL,'2025-09-06 11:54:41','2025-09-06 11:54:41','PRESIDENT'),(155,'sg2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','V6CU2BNM','93X90L69','2025-09-06 11:54:56',NULL,NULL,'2025-09-06 11:54:56','2025-09-06 11:54:56','PRESIDENT'),(156,'sg3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','T9KBGSPK','93X90L69','2025-09-06 11:55:09',NULL,NULL,'2025-09-06 11:55:09','2025-09-06 11:55:09','PRESIDENT'),(157,'sg4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','28JX3X2X','93X90L69','2025-09-06 11:55:23',NULL,NULL,'2025-09-06 11:55:23','2025-09-06 11:55:23','PRESIDENT'),(158,'sg5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','W9N6Q4PF','93X90L69','2025-09-06 11:55:37',NULL,NULL,'2025-09-06 11:55:37','2025-09-06 11:55:37','PRESIDENT'),(159,'jp1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','RS4EU3T0','41Q77MGY','2025-09-06 11:55:58',NULL,NULL,'2025-09-06 11:55:58','2025-09-06 11:55:58','PRESIDENT'),(160,'jp2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','2UPRYK9B','41Q77MGY','2025-09-06 11:56:10',NULL,NULL,'2025-09-06 11:56:10','2025-09-06 11:56:10','PRESIDENT'),(161,'jp3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','W5AQ090M','41Q77MGY','2025-09-06 11:56:26',NULL,NULL,'2025-09-06 11:56:26','2025-09-06 11:56:26','PRESIDENT'),(162,'jp4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','VDKTR5LO','41Q77MGY','2025-09-06 11:56:39',NULL,NULL,'2025-09-06 11:56:39','2025-09-06 11:56:39','PRESIDENT'),(163,'jp5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','BDW4D9E8','41Q77MGY','2025-09-06 11:56:52',NULL,NULL,'2025-09-06 11:56:52','2025-09-06 11:56:52','PRESIDENT'),(164,'gmn1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','S6CB5XVP','1SI0MAUI','2025-09-06 11:58:35',NULL,NULL,'2025-09-06 11:58:35','2025-09-06 11:58:35','PRESIDENT'),(165,'gm2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','N2TGB4FT','1SI0MAUI','2025-09-06 11:58:50',NULL,NULL,'2025-09-06 11:58:50','2025-09-06 11:58:50','PRESIDENT'),(166,'gmn3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','FDLZGIDD','1SI0MAUI','2025-09-06 11:59:08',NULL,NULL,'2025-09-06 11:59:08','2025-09-06 11:59:08','PRESIDENT'),(167,'gmn4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ROMQF0G8','1SI0MAUI','2025-09-06 11:59:23',NULL,NULL,'2025-09-06 11:59:23','2025-09-06 11:59:23','PRESIDENT'),(168,'gmn5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ODGMNRIS','1SI0MAUI','2025-09-06 11:59:37',NULL,NULL,'2025-09-06 11:59:37','2025-09-06 11:59:37','PRESIDENT'),(169,'schina1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','SJKGFU6A','0NJTNK63','2025-09-06 12:01:08',NULL,NULL,'2025-09-06 12:01:08','2025-09-06 12:01:08','PRESIDENT'),(170,'schina2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','9IVEND74','0NJTNK63','2025-09-06 12:01:27',NULL,NULL,'2025-09-06 12:01:27','2025-09-06 12:01:27','PRESIDENT'),(171,'ca1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','99WHIO0W','YFKPEVAW','2025-09-06 12:14:41',NULL,NULL,'2025-09-06 12:14:41','2025-09-06 12:14:41','PRESIDENT'),(172,'ca2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','CKODSGQ5','YFKPEVAW','2025-09-06 12:14:57',NULL,NULL,'2025-09-06 12:14:57','2025-09-06 12:14:57','PRESIDENT'),(173,'ca3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','45J7XIC0','YFKPEVAW','2025-09-06 12:15:10',NULL,NULL,'2025-09-06 12:15:10','2025-09-06 12:15:10','PRESIDENT'),(174,'ca4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','CD7W6IBH','YFKPEVAW','2025-09-06 12:15:37',NULL,NULL,'2025-09-06 12:15:37','2025-09-06 12:15:37','PRESIDENT'),(175,'ca5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','MDZG97DB','YFKPEVAW','2025-09-06 12:15:56',NULL,NULL,'2025-09-06 12:15:56','2025-09-06 12:15:56','PRESIDENT'),(176,'sa1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','85R3RYWT','397S4FB6','2025-09-06 12:16:27',NULL,NULL,'2025-09-06 12:16:27','2025-09-06 12:16:27','PRESIDENT'),(177,'sa2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','MEOEM18W','397S4FB6','2025-09-06 12:16:49',NULL,NULL,'2025-09-06 12:16:49','2025-09-06 12:16:49','PRESIDENT'),(178,'sa3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','VK9GOKIG','397S4FB6','2025-09-06 12:17:06',NULL,NULL,'2025-09-06 12:17:06','2025-09-06 12:17:06','PRESIDENT'),(179,'sa4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','V4RCR2IS','397S4FB6','2025-09-06 12:17:50',NULL,NULL,'2025-09-06 12:17:50','2025-09-06 12:17:50','PRESIDENT'),(180,'sa5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','BBS9Q3DM','397S4FB6','2025-09-06 12:18:05',NULL,NULL,'2025-09-06 12:18:05','2025-09-06 12:18:05','PRESIDENT'),(181,'br1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','BFZAQPZ2','AJJFM3KF','2025-09-06 12:18:42',NULL,NULL,'2025-09-06 12:18:42','2025-09-06 12:18:42','PRESIDENT'),(182,'br2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','M5Z84SV9','AJJFM3KF','2025-09-06 12:18:58',NULL,NULL,'2025-09-06 12:18:58','2025-09-06 12:18:58','PRESIDENT'),(183,'br3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','BQY0TYWH','AJJFM3KF','2025-09-06 12:19:14',NULL,NULL,'2025-09-06 12:19:14','2025-09-06 12:19:14','PRESIDENT'),(184,'br4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','XQU3Y3RK','AJJFM3KF','2025-09-06 12:19:29',NULL,NULL,'2025-09-06 12:19:29','2025-09-06 12:19:29','PRESIDENT'),(185,'br5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','E5B2XWQG','AJJFM3KF','2025-09-06 12:19:45',NULL,NULL,'2025-09-06 12:19:45','2025-09-06 12:19:45','PRESIDENT'),(186,'pr1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','57AANV6V','NG557IEK','2025-09-06 12:20:11',NULL,NULL,'2025-09-06 12:20:11','2025-09-06 12:20:11','PRESIDENT'),(187,'pr2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','AL1REOUG','NG557IEK','2025-09-06 12:20:29',NULL,NULL,'2025-09-06 12:20:29','2025-09-06 12:20:29','PRESIDENT'),(188,'pr3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','517KHMNP','NG557IEK','2025-09-06 12:20:45',NULL,NULL,'2025-09-06 12:20:45','2025-09-06 12:20:45','PRESIDENT'),(189,'pr4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','9WFFAOVR','NG557IEK','2025-09-06 12:20:58',NULL,NULL,'2025-09-06 12:20:58','2025-09-06 12:20:58','PRESIDENT'),(190,'pr5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','U63N9DJR','NG557IEK','2025-09-06 12:21:12',NULL,NULL,'2025-09-06 12:21:12','2025-09-06 12:21:12','PRESIDENT'),(191,'fr1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','PRG0JF9N','WR692RM4','2025-09-06 12:22:47',NULL,NULL,'2025-09-06 12:22:47','2025-09-06 12:22:47','PRESIDENT'),(192,'fr2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','44MFTK88','WR692RM4','2025-09-06 12:23:02',NULL,NULL,'2025-09-06 12:23:02','2025-09-06 12:23:02','PRESIDENT'),(193,'fr3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','KQ7U5IBT','WR692RM4','2025-09-06 12:23:22',NULL,NULL,'2025-09-06 12:23:22','2025-09-06 12:23:22','PRESIDENT'),(194,'fr4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','3GHSF400','WR692RM4','2025-09-06 12:23:39',NULL,NULL,'2025-09-06 12:23:39','2025-09-06 12:23:39','PRESIDENT'),(195,'fr5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','4RUP5N8Z','WR692RM4','2025-09-06 12:23:56',NULL,NULL,'2025-09-06 12:23:56','2025-09-06 12:23:56','PRESIDENT'),(196,'it1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','QRF2NCMH','3ZY7898L','2025-09-06 12:32:38',NULL,NULL,'2025-09-06 12:32:38','2025-09-06 12:32:38','PRESIDENT'),(197,'it2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','OSR2YFQ1','3ZY7898L','2025-09-06 12:32:54',NULL,NULL,'2025-09-06 12:32:54','2025-09-06 12:32:54','PRESIDENT'),(198,'it3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','FAKIDCAV','3ZY7898L','2025-09-06 12:33:09',NULL,NULL,'2025-09-06 12:33:09','2025-09-06 12:33:09','PRESIDENT'),(199,'it4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','OUZT1GY2','3ZY7898L','2025-09-06 12:33:26',NULL,NULL,'2025-09-06 12:33:26','2025-09-06 12:33:26','PRESIDENT'),(200,'it5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','I6JCUKH9','3ZY7898L','2025-09-06 12:33:48',NULL,NULL,'2025-09-06 12:33:48','2025-09-06 12:33:48','PRESIDENT'),(201,'sp1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','X5356OFZ','JM4C5NE1','2025-09-06 12:43:46',NULL,NULL,'2025-09-06 12:43:46','2025-09-06 12:43:46','PRESIDENT'),(202,'sp2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ALT66RTW','JM4C5NE1','2025-09-06 12:44:15',NULL,NULL,'2025-09-06 12:44:15','2025-09-06 12:44:15','PRESIDENT'),(203,'sp3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','W3T7EZOZ','JM4C5NE1','2025-09-06 12:44:30',NULL,NULL,'2025-09-06 12:44:30','2025-09-06 12:44:30','PRESIDENT'),(204,'sp4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','M5K7NLS8','JM4C5NE1','2025-09-06 12:44:47',NULL,NULL,'2025-09-06 12:44:47','2025-09-06 12:44:47','PRESIDENT'),(205,'sp5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','HVS09UWQ','JM4C5NE1','2025-09-06 12:45:09',NULL,NULL,'2025-09-06 12:45:09','2025-09-06 12:45:09','PRESIDENT'),(206,'po1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','Y7M4CUV6','35TYENZG','2025-09-06 12:46:25',NULL,NULL,'2025-09-06 12:46:25','2025-09-06 12:46:25','PRESIDENT'),(207,'po2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','SWRJGVTC','35TYENZG','2025-09-06 12:46:37',NULL,NULL,'2025-09-06 12:46:37','2025-09-06 12:46:37','PRESIDENT'),(208,'po3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','TI89NQ6Q','35TYENZG','2025-09-06 12:46:51',NULL,NULL,'2025-09-06 12:46:51','2025-09-06 12:46:51','PRESIDENT'),(209,'po4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','LIJBEBIQ','35TYENZG','2025-09-06 12:47:04',NULL,NULL,'2025-09-06 12:47:04','2025-09-06 12:47:04','PRESIDENT'),(210,'po5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','MJRF6CN6','35TYENZG','2025-09-06 12:47:17',NULL,NULL,'2025-09-06 12:47:17','2025-09-06 12:47:17','PRESIDENT'),(211,'ee1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','TGWBWNBF','E3PX5EGK','2025-09-06 12:47:54',NULL,NULL,'2025-09-06 12:47:54','2025-09-06 12:47:54','PRESIDENT'),(212,'ee2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','49EZWS11','E3PX5EGK','2025-09-06 12:48:07',NULL,NULL,'2025-09-06 12:48:07','2025-09-06 12:48:07','PRESIDENT'),(213,'ee3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','GUAP0VNS','E3PX5EGK','2025-09-06 12:48:21',NULL,NULL,'2025-09-06 12:48:21','2025-09-06 12:48:21','PRESIDENT'),(214,'ee4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','IM531VEX','E3PX5EGK','2025-09-06 12:48:33',NULL,NULL,'2025-09-06 12:48:33','2025-09-06 12:48:33','PRESIDENT'),(215,'ee5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','YDBASDR4','E3PX5EGK','2025-09-06 12:48:45',NULL,NULL,'2025-09-06 12:48:45','2025-09-06 12:48:45','PRESIDENT'),(216,'safrica1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','PJIYC2X1','S85I12D3','2025-09-06 12:53:32',NULL,NULL,'2025-09-06 12:53:32','2025-09-06 12:53:32','PRESIDENT'),(217,'safrica2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','U73N4W4R','S85I12D3','2025-09-06 12:53:49',NULL,NULL,'2025-09-06 12:53:49','2025-09-06 12:53:49','PRESIDENT'),(218,'safrica3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','KQSLC8M4','S85I12D3','2025-09-06 12:54:07',NULL,NULL,'2025-09-06 12:54:07','2025-09-06 12:54:07','PRESIDENT'),(219,'safrica4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','A81TOOHY','S85I12D3','2025-09-06 12:54:22',NULL,NULL,'2025-09-06 12:54:22','2025-09-06 12:54:22','PRESIDENT'),(220,'safrica5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ASDVANGN','S85I12D3','2025-09-06 12:54:39',NULL,NULL,'2025-09-06 12:54:39','2025-09-06 12:54:39','PRESIDENT'),(221,'nr1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','N1A3WJS8','2IGD9KIA','2025-09-06 12:55:26',NULL,NULL,'2025-09-06 12:55:26','2025-09-06 12:55:26','PRESIDENT'),(222,'nr2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','4JXU0YF8','2IGD9KIA','2025-09-06 12:55:41',NULL,NULL,'2025-09-06 12:55:41','2025-09-06 12:55:41','PRESIDENT'),(223,'nr3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','I4SZ4KHL','2IGD9KIA','2025-09-06 12:55:57',NULL,NULL,'2025-09-06 12:55:57','2025-09-06 12:55:57','PRESIDENT'),(224,'nr4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','EVDLYRQ8','2IGD9KIA','2025-09-06 12:56:19',NULL,NULL,'2025-09-06 12:56:19','2025-09-06 12:56:19','PRESIDENT'),(225,'nr5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','CCYEY1M7','2IGD9KIA','2025-09-06 12:56:35',NULL,NULL,'2025-09-06 12:56:35','2025-09-06 12:56:35','PRESIDENT'),(226,'es1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','KD0MJ5T6','ET520TU8','2025-09-06 12:57:04',NULL,NULL,'2025-09-06 12:57:04','2025-09-06 12:57:04','PRESIDENT'),(227,'es2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','60EPLWS9','ET520TU8','2025-09-06 12:57:28',NULL,NULL,'2025-09-06 12:57:28','2025-09-06 12:57:28','PRESIDENT'),(228,'es3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','UUKMK85Q','ET520TU8','2025-09-06 12:57:43',NULL,NULL,'2025-09-06 12:57:43','2025-09-06 12:57:43','PRESIDENT'),(229,'es4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','NZRP14KX','ET520TU8','2025-09-06 12:58:01',NULL,NULL,'2025-09-06 12:58:01','2025-09-06 12:58:01','PRESIDENT'),(230,'es5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','SE7QUHEX','ET520TU8','2025-09-06 12:58:18',NULL,NULL,'2025-09-06 12:58:18','2025-09-06 12:58:18','PRESIDENT'),(231,'eg1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','Q2STL7ES','IL2JZ19C','2025-09-06 12:58:50',NULL,NULL,'2025-09-06 12:58:50','2025-09-06 12:58:50','PRESIDENT'),(232,'eg2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','9XBGG7ZI','IL2JZ19C','2025-09-06 12:59:05',NULL,NULL,'2025-09-06 12:59:05','2025-09-06 12:59:05','PRESIDENT'),(233,'eg3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','DPA4ZC9I','IL2JZ19C','2025-09-06 12:59:20',NULL,NULL,'2025-09-06 12:59:20','2025-09-06 12:59:20','PRESIDENT'),(234,'eg4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','4ONXZIO9','IL2JZ19C','2025-09-06 12:59:37',NULL,NULL,'2025-09-06 12:59:37','2025-09-06 12:59:37','PRESIDENT'),(235,'eg5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','9D7BCTLP','IL2JZ19C','2025-09-06 13:00:28',NULL,NULL,'2025-09-06 13:00:28','2025-09-06 13:00:28','PRESIDENT'),(236,'gg1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','8KICZ0JO','RWVN0U2I','2025-09-06 13:01:10',NULL,NULL,'2025-09-06 13:01:10','2025-09-06 13:01:10','PRESIDENT'),(237,'gg2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','0X09DOZM','RWVN0U2I','2025-09-06 13:01:25',NULL,NULL,'2025-09-06 13:01:25','2025-09-06 13:01:25','PRESIDENT'),(238,'gg3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','0E9HAOC8','RWVN0U2I','2025-09-06 13:01:45',NULL,NULL,'2025-09-06 13:01:45','2025-09-06 13:01:45','PRESIDENT'),(239,'gg4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','8TZFCMVT','RWVN0U2I','2025-09-06 13:02:03',NULL,NULL,'2025-09-06 13:02:03','2025-09-06 13:02:03','PRESIDENT'),(240,'gg5@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','ULM4TIYV','RWVN0U2I','2025-09-06 13:02:21',NULL,NULL,'2025-09-06 13:02:21','2025-09-06 13:02:21','PRESIDENT'),(241,'extra@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','B4ZDQSU9','COMPANY001','2025-09-06 18:25:05',NULL,NULL,'2025-09-06 18:25:05','2025-09-06 18:25:05','PRESIDENT'),(242,'extra1@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','KH83K3RR','B4ZDQSU9','2025-09-06 18:26:51',NULL,NULL,'2025-09-06 18:26:51','2025-09-06 18:26:51','PRESIDENT'),(243,'extra2@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','X97A1ZLM','B4ZDQSU9','2025-09-06 18:27:58',NULL,NULL,'2025-09-06 18:27:58','2025-09-06 18:27:58','PRESIDENT'),(244,'extra3@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','5IEIOXSH','B4ZDQSU9','2025-09-06 18:32:48',NULL,NULL,'2025-09-06 18:32:48','2025-09-06 18:32:48','PRESIDENT'),(245,'extra4@6768688.com','thspU+/iVbmpyRApzS4w0jMBPhFzgQ==','','',NULL,'SUBSCRIBER','ACTIVE','E2PIQVUI','B4ZDQSU9','2025-09-06 18:33:26',NULL,NULL,'2025-09-06 18:33:26','2025-09-06 18:33:26','PRESIDENT');
/* SQLINES DEMO *** LE `users` ENABLE KEYS */;
UNLOCK TABLES;

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
-- SQLINES DEMO *** table `wallet_payments`
--

LOCK TABLES wallet_payments WRITE;
/* SQLINES DEMO *** LE `wallet_payments` DISABLE KEYS */;
/* SQLINES DEMO *** LE `wallet_payments` ENABLE KEYS */;
UNLOCK TABLES;
/* SQLINES DEMO *** ZONE=@OLD_TIME_ZONE */;

/* SQLINES DEMO *** ODE=@OLD_SQL_MODE */;
/* SQLINES DEMO *** GN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/* SQLINES DEMO *** E_CHECKS=@OLD_UNIQUE_CHECKS */;
/* SQLINES DEMO *** CTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/* SQLINES DEMO *** CTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/* SQLINES DEMO *** TION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/* SQLINES DEMO *** OTES=@OLD_SQL_NOTES */;

-- SQLINES DEMO ***  2025-09-18  6:09:23

