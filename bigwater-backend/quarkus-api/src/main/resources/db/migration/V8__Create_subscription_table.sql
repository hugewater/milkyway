-- Create subscription table to store member subscription information
CREATE TABLE subscription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    payment DECIMAL(20,8) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    payment_date DATE NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key constraint
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    -- Indexes for better performance
    INDEX idx_subscription_user_id (user_id),
    INDEX idx_subscription_status (status),
    INDEX idx_subscription_dates (from_date, to_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

