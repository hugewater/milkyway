-- BigWater Database Initialization Script
-- This script creates the database and initial data

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS bwdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE bwdb;

-- Create tables (this will be handled by Flyway migrations)
-- The V1__Create_initial_tables.sql file contains all table definitions

-- Insert some sample data for testing

-- Insert sample certificates
INSERT INTO certificates (certificate_name, certificate_type, description, price_usdt, duration_days, benefits, is_active, created_by) VALUES
('Bronze Member', 'BRONZE', 'Basic membership with limited benefits', 10.00, 365, '["Weekly newsletter", "Basic support"]', true, 1),
('Silver Member', 'SILVER', 'Enhanced membership with more benefits', 25.00, 365, '["Weekly newsletter", "Priority support", "Exclusive content"]', true, 1),
('Gold Member', 'GOLD', 'Premium membership with full benefits', 50.00, 365, '["Weekly newsletter", "Priority support", "Exclusive content", "VIP events"]', true, 1),
('Platinum Member', 'PLATINUM', 'Elite membership with all benefits', 100.00, 365, '["Weekly newsletter", "Priority support", "Exclusive content", "VIP events", "Personal advisor"]', true, 1),
('Diamond Member', 'DIAMOND', 'Ultimate membership with exclusive benefits', 250.00, 365, '["Weekly newsletter", "Priority support", "Exclusive content", "VIP events", "Personal advisor", "Custom services"]', true, 1);

-- Insert sample journals
INSERT INTO journals (week_number, title, excerpt, content, status, featured, tags, created_by) VALUES
(1, 'Welcome to BigWater Digital Weekly Journal', 'Introduction to our digital platform and what to expect', '<h1>Welcome to BigWater</h1><p>This is our first weekly journal. We are excited to bring you the latest insights and updates.</p>', 'PUBLISHED', true, 'welcome,introduction', 1),
(2, 'Market Trends and Analysis', 'Weekly market analysis and trends', '<h1>Market Trends</h1><p>This week we analyze the latest market trends and provide insights for investors.</p>', 'PUBLISHED', false, 'market,trends,analysis', 1),
(3, 'Technology Updates', 'Latest technology news and updates', '<h1>Technology Updates</h1><p>Stay updated with the latest technology news and developments.</p>', 'DRAFT', false, 'technology,updates,news', 1);

-- Insert sample random drawing
INSERT INTO random_drawings (drawing_name, drawing_type, week_number, drawing_date, prize_pool, status, created_by) VALUES
('Weekly Powerball', 'POWERBALL', 1, DATE_ADD(NOW(), INTERVAL 7 DAY), 1000.00, 'PENDING', 1);

-- Update system settings with more values
INSERT INTO system_settings (setting_key, setting_value, setting_type, description, is_public) VALUES
('max_wallets_per_user', '5', 'NUMBER', 'Maximum number of wallets per user', TRUE),
('certificate_purchase_limit', '10', 'NUMBER', 'Maximum certificates a user can purchase', TRUE),
('drawing_participation_limit', '100', 'NUMBER', 'Maximum participants per drawing', TRUE),
('referral_bonus_usdt', '5.00', 'NUMBER', 'Bonus USDT for successful referrals', TRUE),
('weekly_newsletter_enabled', 'true', 'BOOLEAN', 'Enable weekly newsletter feature', TRUE),
('maintenance_mode_message', 'System is under maintenance. Please try again later.', 'STRING', 'Message shown during maintenance mode', TRUE);
