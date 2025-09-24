-- Create table for managing AI agents
CREATE TABLE IF NOT EXISTS ai_agents (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  provider VARCHAR(50) NOT NULL,
  model VARCHAR(100) NOT NULL,
  api_key VARCHAR(255),
  webhook_url VARCHAR(255),
  role VARCHAR(50),
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
  description TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_ai_agents_name ON ai_agents (name);
CREATE INDEX IF NOT EXISTS idx_ai_agents_status ON ai_agents (status);
CREATE INDEX IF NOT EXISTS idx_ai_agents_enabled ON ai_agents (enabled);

-- Subscription table (moved from separate V8 to avoid duplicate version)
CREATE TABLE IF NOT EXISTS subscription (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    payment NUMERIC(20,8) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    payment_date DATE NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','EXPIRED','CANCELLED')),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_subscription_user_id ON subscription (user_id);
CREATE INDEX IF NOT EXISTS idx_subscription_status ON subscription (status);
CREATE INDEX IF NOT EXISTS idx_subscription_dates ON subscription (from_date, to_date);


