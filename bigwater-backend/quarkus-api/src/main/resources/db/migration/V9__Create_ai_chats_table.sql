-- Create table for AI chats
CREATE TABLE IF NOT EXISTS ai_chats (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  agent_id BIGINT NOT NULL REFERENCES ai_agents(id) ON DELETE CASCADE,
  user_id BIGINT NULL REFERENCES users(id) ON DELETE SET NULL,
  title VARCHAR(200) NOT NULL,
  last_message TEXT,
  status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','ARCHIVED')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ai_chats_agent ON ai_chats (agent_id);
CREATE INDEX IF NOT EXISTS idx_ai_chats_status ON ai_chats (status);
CREATE INDEX IF NOT EXISTS idx_ai_chats_user ON ai_chats (user_id);


