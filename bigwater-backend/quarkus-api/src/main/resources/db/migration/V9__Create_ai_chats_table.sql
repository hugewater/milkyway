-- Create table for AI chats
CREATE TABLE IF NOT EXISTS ai_chats (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  agent_id BIGINT NOT NULL,
  user_id BIGINT NULL,
  title VARCHAR(200) NOT NULL,
  last_message TEXT,
  status ENUM('ACTIVE','ARCHIVED') NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_ai_chats_agent FOREIGN KEY (agent_id) REFERENCES ai_agents(id) ON DELETE CASCADE,
  INDEX idx_ai_chats_agent (agent_id),
  INDEX idx_ai_chats_status (status),
  INDEX idx_ai_chats_user (user_id)
);


