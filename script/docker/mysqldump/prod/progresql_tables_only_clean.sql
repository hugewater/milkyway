-- Ensure UTC-ish timestamps on the server session (optional)
SET TIME ZONE 'UTC';

-- Helper: auto-update updated_at columns
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  NEW.updated_at := NOW();
  RETURN NEW;
END$$;

-- ─────────────────────────────────────────────────────────────
-- Base table: users (create first; many FKs reference it)
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.users CASCADE;
CREATE TABLE public.users (
  id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email             VARCHAR(255) NOT NULL,
  password_hash     VARCHAR(255) NOT NULL,
  first_name        VARCHAR(100),
  last_name         VARCHAR(100),
  phone             VARCHAR(20),
  role              VARCHAR(30)  NOT NULL CHECK (role IN ('SUBSCRIBER','ADMIN','SUPER_ADMIN')) DEFAULT 'SUBSCRIBER',
  status            VARCHAR(30)  NOT NULL CHECK (status IN ('ACTIVE','INACTIVE','SUSPENDED'))     DEFAULT 'ACTIVE',
  referral_code     VARCHAR(20)  NOT NULL,
  referred_by_code  VARCHAR(20)  DEFAULT 'COMPANY001',
  join_date         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  last_login        TIMESTAMPTZ,
  email_verified_at TIMESTAMPTZ,
  created_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  level             VARCHAR(20)  DEFAULT 'CHIEF',
  CONSTRAINT users_email_unique UNIQUE (email),
  CONSTRAINT users_referral_code_unique UNIQUE (referral_code)
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email            ON public.users(email);
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_referral_code    ON public.users(referral_code);
CREATE INDEX IF NOT EXISTS idx_users_referred_by_code        ON public.users(referred_by_code);
CREATE INDEX IF NOT EXISTS idx_users_status                  ON public.users(status);
CREATE INDEX IF NOT EXISTS idx_users_role                    ON public.users(role);
CREATE INDEX IF NOT EXISTS idx_users_join_date               ON public.users(join_date);

-- Restart identity as requested
ALTER SEQUENCE public.users_id_seq RESTART WITH 246;

-- updated_at trigger
DROP TRIGGER IF EXISTS trg_users_updated_at ON public.users;
CREATE TRIGGER trg_users_updated_at
BEFORE UPDATE ON public.users
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- ai_agents
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.ai_agents CASCADE;
CREATE TABLE public.ai_agents (
  id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name         VARCHAR(100) NOT NULL,
  provider     VARCHAR(50)  NOT NULL,
  model        VARCHAR(100) NOT NULL,
  api_key      VARCHAR(255),
  webhook_url  VARCHAR(255),
  role         VARCHAR(50),
  enabled      SMALLINT NOT NULL DEFAULT 1,
  status       VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE','INACTIVE')) DEFAULT 'ACTIVE',
  description  TEXT,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_ai_agents_name UNIQUE (name)
);
CREATE INDEX IF NOT EXISTS idx_ai_agents_status  ON public.ai_agents(status);
CREATE INDEX IF NOT EXISTS idx_ai_agents_enabled ON public.ai_agents(enabled);

ALTER SEQUENCE public.ai_agents_id_seq RESTART WITH 3;

DROP TRIGGER IF EXISTS trg_ai_agents_updated_at ON public.ai_agents;
CREATE TRIGGER trg_ai_agents_updated_at
BEFORE UPDATE ON public.ai_agents
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- ai_chats
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.ai_chats CASCADE;
CREATE TABLE public.ai_chats (
  id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  agent_id      BIGINT NOT NULL,
  user_id       BIGINT,
  title         VARCHAR(200) NOT NULL,
  last_message  TEXT,
  status        VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE','ARCHIVED')) DEFAULT 'ACTIVE',
  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT fk_ai_chats_agent FOREIGN KEY (agent_id) REFERENCES public.ai_agents(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_ai_chats_agent ON public.ai_chats(agent_id);
CREATE INDEX IF NOT EXISTS idx_ai_chats_status ON public.ai_chats(status);
CREATE INDEX IF NOT EXISTS idx_ai_chats_user ON public.ai_chats(user_id);

ALTER SEQUENCE public.ai_chats_id_seq RESTART WITH 2;

DROP TRIGGER IF EXISTS trg_ai_chats_updated_at ON public.ai_chats;
CREATE TRIGGER trg_ai_chats_updated_at
BEFORE UPDATE ON public.ai_chats
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- certificates
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.certificates CASCADE;
CREATE TABLE public.certificates (
  id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  certificate_name VARCHAR(255) NOT NULL,
  certificate_type VARCHAR(30)  NOT NULL CHECK (certificate_type IN ('BRONZE','SILVER','GOLD','PLATINUM','DIAMOND')),
  description      TEXT,
  price_usdt       NUMERIC(15,2) NOT NULL,
  duration_days    INT NOT NULL DEFAULT 365,
  benefits         JSONB,
  is_active        SMALLINT NOT NULL DEFAULT 1,
  max_supply       INT,
  current_supply   INT NOT NULL DEFAULT 0,
  created_by       BIGINT NOT NULL,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT certificates_ibfk_1 FOREIGN KEY (created_by) REFERENCES public.users(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_certificate_type ON public.certificates(certificate_type);
CREATE INDEX IF NOT EXISTS idx_price_usdt       ON public.certificates(price_usdt);
CREATE INDEX IF NOT EXISTS idx_is_active        ON public.certificates(is_active);
CREATE INDEX IF NOT EXISTS idx_cert_created_by  ON public.certificates(created_by);

ALTER SEQUENCE public.certificates_id_seq RESTART WITH 20;

DROP TRIGGER IF EXISTS trg_certificates_updated_at ON public.certificates;
CREATE TRIGGER trg_certificates_updated_at
BEFORE UPDATE ON public.certificates
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- random_drawings
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.random_drawings CASCADE;
CREATE TABLE public.random_drawings (
  id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  drawing_name       VARCHAR(255) NOT NULL,
  drawing_type       VARCHAR(30)  NOT NULL CHECK (drawing_type IN ('SWEEPSTAKES','RAFFLE','CONTEST')),
  week_number        INT NOT NULL,
  drawing_date       TIMESTAMPTZ NOT NULL,
  prize_pool         NUMERIC(15,2) NOT NULL DEFAULT 0.00,
  winning_numbers    JSONB,
  status             VARCHAR(30) NOT NULL CHECK (status IN ('PENDING','COMPLETED','CANCELLED')) DEFAULT 'PENDING',
  total_participants INT NOT NULL DEFAULT 0,
  created_by         BIGINT NOT NULL,
  created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT random_drawings_ibfk_1 FOREIGN KEY (created_by) REFERENCES public.users(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_random_drawing_type ON public.random_drawings(drawing_type);
CREATE INDEX IF NOT EXISTS idx_random_week_number  ON public.random_drawings(week_number);
CREATE INDEX IF NOT EXISTS idx_random_drawing_date ON public.random_drawings(drawing_date);
CREATE INDEX IF NOT EXISTS idx_random_status       ON public.random_drawings(status);
CREATE INDEX IF NOT EXISTS idx_random_created_by   ON public.random_drawings(created_by);

ALTER SEQUENCE public.random_drawings_id_seq RESTART WITH 11;

DROP TRIGGER IF EXISTS trg_random_drawings_updated_at ON public.random_drawings;
CREATE TRIGGER trg_random_drawings_updated_at
BEFORE UPDATE ON public.random_drawings
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- drawing_participants
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.drawing_participants CASCADE;
CREATE TABLE public.drawing_participants (
  id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  drawing_id           BIGINT NOT NULL,
  user_id              BIGINT NOT NULL,
  participant_numbers  JSONB,
  entry_fee            NUMERIC(10,2) NOT NULL DEFAULT 0.00,
  is_winner            SMALLINT NOT NULL DEFAULT 0,
  prize_amount         NUMERIC(15,2),
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_drawing_user UNIQUE (drawing_id, user_id),
  CONSTRAINT drawing_participants_ibfk_1 FOREIGN KEY (drawing_id) REFERENCES public.random_drawings(id) ON DELETE CASCADE,
  CONSTRAINT drawing_participants_ibfk_2 FOREIGN KEY (user_id)    REFERENCES public.users(id)          ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_dp_drawing_id ON public.drawing_participants(drawing_id);
CREATE INDEX IF NOT EXISTS idx_dp_user_id    ON public.drawing_participants(user_id);
CREATE INDEX IF NOT EXISTS idx_dp_is_winner  ON public.drawing_participants(is_winner);

-- ─────────────────────────────────────────────────────────────
-- usdt_wallets
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.usdt_wallets CASCADE;
CREATE TABLE public.usdt_wallets (
  id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id        BIGINT NOT NULL,
  wallet_address VARCHAR(255) NOT NULL,
  wallet_name    VARCHAR(100) DEFAULT 'Main Wallet',
  wallet_type    VARCHAR(30)  NOT NULL CHECK (wallet_type IN ('COMPANY','MEMBER','TESTING','MAIN','TRADING','STAKING','REWARDS')) DEFAULT 'COMPANY',
  balance        NUMERIC(20,8) NOT NULL DEFAULT 0.00000000,
  is_active      SMALLINT NOT NULL DEFAULT 1,
  is_verified    SMALLINT NOT NULL DEFAULT 0,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT wallet_address_unique UNIQUE (wallet_address),
  CONSTRAINT usdt_wallets_ibfk_1 FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_usdt_user_id      ON public.usdt_wallets(user_id);
CREATE INDEX IF NOT EXISTS idx_usdt_wallet_addr  ON public.usdt_wallets(wallet_address);
CREATE INDEX IF NOT EXISTS idx_usdt_wallet_type  ON public.usdt_wallets(wallet_type);
CREATE INDEX IF NOT EXISTS idx_usdt_is_active    ON public.usdt_wallets(is_active);

ALTER SEQUENCE public.usdt_wallets_id_seq RESTART WITH 21;

DROP TRIGGER IF EXISTS trg_usdt_wallets_updated_at ON public.usdt_wallets;
CREATE TRIGGER trg_usdt_wallets_updated_at
BEFORE UPDATE ON public.usdt_wallets
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- transactions
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.transactions CASCADE;
CREATE TABLE public.transactions (
  id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id              BIGINT NOT NULL,
  wallet_id            BIGINT,
  transaction_type     VARCHAR(30) NOT NULL CHECK (transaction_type IN ('WINNING','DEPOSIT','WITHDRAWAL','PURCHASE','REFUND','REWARD','COMMISSION')),
  amount_usdt          NUMERIC(20,8) NOT NULL,
  transaction_hash     VARCHAR(255),
  status               VARCHAR(30) NOT NULL CHECK (status IN ('PENDING','COMPLETED','FAILED','CANCELLED')) DEFAULT 'PENDING',
  description          TEXT,
  related_entity_type  VARCHAR(50),
  related_entity_id    BIGINT,
  metadata             JSONB,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  to_wallet_id         BIGINT NOT NULL CHECK (to_wallet_id > 0),
  CONSTRAINT transactions_ibfk_1 FOREIGN KEY (user_id)  REFERENCES public.users(id)         ON DELETE CASCADE,
  CONSTRAINT transactions_ibfk_2 FOREIGN KEY (wallet_id) REFERENCES public.usdt_wallets(id) ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS idx_trx_user_id          ON public.transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_trx_wallet_id        ON public.transactions(wallet_id);
CREATE INDEX IF NOT EXISTS idx_trx_type             ON public.transactions(transaction_type);
CREATE INDEX IF NOT EXISTS idx_trx_status           ON public.transactions(status);
CREATE INDEX IF NOT EXISTS idx_trx_created_at       ON public.transactions(created_at);

ALTER SEQUENCE public.transactions_id_seq RESTART WITH 17;

DROP TRIGGER IF EXISTS trg_transactions_updated_at ON public.transactions;
CREATE TRIGGER trg_transactions_updated_at
BEFORE UPDATE ON public.transactions
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- user_certificates
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.user_certificates CASCADE;
CREATE TABLE public.user_certificates (
  id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id               BIGINT NOT NULL,
  certificate_id        BIGINT NOT NULL,
  purchase_date         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expiry_date           TIMESTAMPTZ NOT NULL,
  status                VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE','EXPIRED','CANCELLED')) DEFAULT 'ACTIVE',
  purchase_amount_usdt  NUMERIC(15,2) NOT NULL,
  transaction_hash      VARCHAR(255),
  wallet_id             BIGINT,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT user_certificates_ibfk_1 FOREIGN KEY (user_id)        REFERENCES public.users(id)         ON DELETE CASCADE,
  CONSTRAINT user_certificates_ibfk_2 FOREIGN KEY (certificate_id) REFERENCES public.certificates(id)  ON DELETE CASCADE,
  CONSTRAINT user_certificates_ibfk_3 FOREIGN KEY (wallet_id)      REFERENCES public.usdt_wallets(id)  ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS idx_uc_wallet_id      ON public.user_certificates(wallet_id);
CREATE INDEX IF NOT EXISTS idx_uc_user_id        ON public.user_certificates(user_id);
CREATE INDEX IF NOT EXISTS idx_uc_certificate_id ON public.user_certificates(certificate_id);
CREATE INDEX IF NOT EXISTS idx_uc_status         ON public.user_certificates(status);
CREATE INDEX IF NOT EXISTS idx_uc_expiry_date    ON public.user_certificates(expiry_date);
CREATE INDEX IF NOT EXISTS idx_uc_purchase_date  ON public.user_certificates(purchase_date);

DROP TRIGGER IF EXISTS trg_user_certificates_updated_at ON public.user_certificates;
CREATE TRIGGER trg_user_certificates_updated_at
BEFORE UPDATE ON public.user_certificates
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- access_logs
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.access_logs CASCADE;
CREATE TABLE public.access_logs (
  id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id          BIGINT,
  ip_address       VARCHAR(45) NOT NULL,
  user_agent       TEXT,
  request_method   VARCHAR(10) NOT NULL,
  request_url      VARCHAR(500) NOT NULL,
  request_params   TEXT,
  response_status  INT NOT NULL,
  response_time_ms INT,
  session_id       VARCHAR(255),
  accessed_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT access_logs_ibfk_1 FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS idx_access_user_id     ON public.access_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_access_ip_address  ON public.access_logs(ip_address);
CREATE INDEX IF NOT EXISTS idx_access_method      ON public.access_logs(request_method);
CREATE INDEX IF NOT EXISTS idx_access_status      ON public.access_logs(response_status);
CREATE INDEX IF NOT EXISTS idx_accessed_at        ON public.access_logs(accessed_at);

-- ─────────────────────────────────────────────────────────────
-- error_logs
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.error_logs CASCADE;
CREATE TABLE public.error_logs (
  id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id         BIGINT,
  error_level     VARCHAR(30) NOT NULL CHECK (error_level IN ('DEBUG','INFO','WARNING','ERROR','CRITICAL')),
  error_type      VARCHAR(100) NOT NULL,
  error_message   TEXT NOT NULL,
  stack_trace     TEXT,
  request_url     VARCHAR(500),
  request_method  VARCHAR(10),
  ip_address      VARCHAR(45),
  user_agent      TEXT,
  additional_data JSONB,
  created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT error_logs_ibfk_1 FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS idx_error_user_id     ON public.error_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_error_level       ON public.error_logs(error_level);
CREATE INDEX IF NOT EXISTS idx_error_type        ON public.error_logs(error_type);
CREATE INDEX IF NOT EXISTS idx_error_created_at  ON public.error_logs(created_at);

-- ─────────────────────────────────────────────────────────────
-- journals
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.journals CASCADE;
CREATE TABLE public.journals (
  id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  week_number   INT NOT NULL,
  title         VARCHAR(255) NOT NULL,
  excerpt       TEXT,
  content       TEXT NOT NULL,
  status        VARCHAR(30) NOT NULL CHECK (status IN ('DRAFT','PUBLISHED','SCHEDULED')) DEFAULT 'DRAFT',
  featured      SMALLINT NOT NULL DEFAULT 0,
  tags          VARCHAR(500),
  views         INT NOT NULL DEFAULT 0,
  publish_date  TIMESTAMPTZ,
  created_by    BIGINT NOT NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT journals_week_number_unique UNIQUE (week_number),
  CONSTRAINT journals_ibfk_1 FOREIGN KEY (created_by) REFERENCES public.users(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_journals_week_number ON public.journals(week_number);
CREATE INDEX IF NOT EXISTS idx_journals_status      ON public.journals(status);
CREATE INDEX IF NOT EXISTS idx_journals_featured    ON public.journals(featured);
CREATE INDEX IF NOT EXISTS idx_journals_publish_date ON public.journals(publish_date);
CREATE INDEX IF NOT EXISTS idx_journals_created_by  ON public.journals(created_by);

ALTER SEQUENCE public.journals_id_seq RESTART WITH 8;

DROP TRIGGER IF EXISTS trg_journals_updated_at ON public.journals;
CREATE TRIGGER trg_journals_updated_at
BEFORE UPDATE ON public.journals
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- journal_views
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.journal_views CASCADE;
CREATE TABLE public.journal_views (
  id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  journal_id  BIGINT NOT NULL,
  user_id     BIGINT,
  ip_address  VARCHAR(45),
  user_agent  TEXT,
  viewed_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT journal_views_ibfk_1 FOREIGN KEY (journal_id) REFERENCES public.journals(id) ON DELETE CASCADE,
  CONSTRAINT journal_views_ibfk_2 FOREIGN KEY (user_id)    REFERENCES public.users(id)    ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS idx_journal_views_journal_id ON public.journal_views(journal_id);
CREATE INDEX IF NOT EXISTS idx_journal_views_user_id    ON public.journal_views(user_id);
CREATE INDEX IF NOT EXISTS idx_journal_views_viewed_at  ON public.journal_views(viewed_at);

-- ─────────────────────────────────────────────────────────────
-- referral_relationships
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.referral_relationships CASCADE;
CREATE TABLE public.referral_relationships (
  id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  referrer_id      BIGINT NOT NULL,
  referred_id      BIGINT NOT NULL,
  referral_level   INT NOT NULL DEFAULT 1,
  commission_rate  NUMERIC(5,4) NOT NULL DEFAULT 0.1000,
  status           VARCHAR(30) NOT NULL CHECK (status IN ('ACTIVE','INACTIVE','CANCELLED')) DEFAULT 'ACTIVE',
  total_earnings   NUMERIC(15,2) NOT NULL DEFAULT 0.00,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_referrer_referred UNIQUE (referrer_id, referred_id),
  CONSTRAINT rr_ibfk_1 FOREIGN KEY (referrer_id) REFERENCES public.users(id) ON DELETE CASCADE,
  CONSTRAINT rr_ibfk_2 FOREIGN KEY (referred_id) REFERENCES public.users(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_rr_referrer_id    ON public.referral_relationships(referrer_id);
CREATE INDEX IF NOT EXISTS idx_rr_referred_id    ON public.referral_relationships(referred_id);
CREATE INDEX IF NOT EXISTS idx_rr_referral_level ON public.referral_relationships(referral_level);
CREATE INDEX IF NOT EXISTS idx_rr_status         ON public.referral_relationships(status);

DROP TRIGGER IF EXISTS trg_referral_relationships_updated_at ON public.referral_relationships;
CREATE TRIGGER trg_referral_relationships_updated_at
BEFORE UPDATE ON public.referral_relationships
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- system_settings
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.system_settings CASCADE;
CREATE TABLE public.system_settings (
  id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  setting_key    VARCHAR(100) NOT NULL,
  setting_value  TEXT NOT NULL,
  setting_type   VARCHAR(30) NOT NULL CHECK (setting_type IN ('STRING','NUMBER','BOOLEAN','JSON')) DEFAULT 'STRING',
  description    TEXT,
  is_public      SMALLINT NOT NULL DEFAULT 0,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT system_settings_key_unique UNIQUE (setting_key)
);
CREATE INDEX IF NOT EXISTS idx_system_settings_key      ON public.system_settings(setting_key);
CREATE INDEX IF NOT EXISTS idx_system_settings_ispublic ON public.system_settings(is_public);

ALTER SEQUENCE public.system_settings_id_seq RESTART WITH 13;

DROP TRIGGER IF EXISTS trg_system_settings_updated_at ON public.system_settings;
CREATE TRIGGER trg_system_settings_updated_at
BEFORE UPDATE ON public.system_settings
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ─────────────────────────────────────────────────────────────
-- wallet_payments
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS public.wallet_payments CASCADE;
CREATE TABLE public.wallet_payments (
  id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  from_address VARCHAR(255) NOT NULL,
  to_wallet_id BIGINT NOT NULL,
  amount      NUMERIC(20,8) NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT fk_wallet_payments_wallet FOREIGN KEY (to_wallet_id) REFERENCES public.usdt_wallets(id)
);
CREATE INDEX IF NOT EXISTS idx_wallet_payments_wallet ON public.wallet_payments(to_wallet_id);
