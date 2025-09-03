-- Create table for managing AI agents
CREATE TABLE IF NOT EXISTS ai_agents (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  provider VARCHAR(50) NOT NULL,
  model VARCHAR(100) NOT NULL,
  api_key VARCHAR(255),
  webhook_url VARCHAR(255),
  role VARCHAR(50),
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  description TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_ai_agents_name (name),
  KEY idx_ai_agents_status (status),
  KEY idx_ai_agents_enabled (enabled)
);


