-- Create company_wallets table for managing company payment addresses
CREATE TABLE company_wallets (
    id BIGSERIAL PRIMARY KEY,
    wallet_name VARCHAR(255) NOT NULL,
    tron_address VARCHAR(255),
    polygon_address VARCHAR(255),
    tron_balance DECIMAL(20, 8) DEFAULT 0.00000000,
    polygon_balance DECIMAL(20, 8) DEFAULT 0.00000000,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Ensure at least one address is provided
    CONSTRAINT chk_company_wallet_addresses CHECK (
        tron_address IS NOT NULL OR polygon_address IS NOT NULL
    )
);

-- Create unique indexes for addresses (PostgreSQL style)
CREATE UNIQUE INDEX uk_company_wallet_tron ON company_wallets (tron_address) WHERE tron_address IS NOT NULL;
CREATE UNIQUE INDEX uk_company_wallet_polygon ON company_wallets (polygon_address) WHERE polygon_address IS NOT NULL;

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION update_company_wallets_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_company_wallets_updated_at
    BEFORE UPDATE ON company_wallets
    FOR EACH ROW
    EXECUTE FUNCTION update_company_wallets_updated_at();

-- Insert some sample company wallets
INSERT INTO company_wallets (wallet_name, tron_address, polygon_address, tron_balance, polygon_balance) VALUES
('Company Main TRON', 'TXYZopYRdj2D9XRtbG411XZZ3kM5VkAeBe', NULL, 50000.00000000, 0.00000000),
('Company Main POLYGON', NULL, '0x742d35Cc6634C0532925a3b844Bc454e4438f44e', 0.00000000, 75000.00000000),
('Company Dual Wallet', 'TCompanyDual123456789012345678901234', '0x1234567890123456789012345678901234567890', 25000.00000000, 30000.00000000);
