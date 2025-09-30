-- Fix PostgreSQL permissions for BigWater affiliate system
-- Run this as a PostgreSQL superuser (usually postgres)

-- Connect to the bigwater_affiliate database first
\c bigwater_affiliate;

-- Grant all privileges on all tables to the bigwater user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO bigwater;

-- Grant all privileges on all sequences to the bigwater user
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO bigwater;

-- Grant usage on schema
GRANT USAGE ON SCHEMA public TO bigwater;

-- Grant create privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO bigwater;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO bigwater;

-- Grant execute on all functions
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO bigwater;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT EXECUTE ON FUNCTIONS TO bigwater;

-- Grant usage on custom types
GRANT USAGE ON TYPE affiliate_level_type TO bigwater;
GRANT USAGE ON TYPE commission_type_enum TO bigwater;
GRANT USAGE ON TYPE commission_status_enum TO bigwater;
GRANT USAGE ON TYPE transaction_type_enum TO bigwater;
GRANT USAGE ON TYPE transaction_status_enum TO bigwater;
GRANT USAGE ON TYPE promotion_type_enum TO bigwater;
GRANT USAGE ON TYPE calculation_status_enum TO bigwater;

-- Verify permissions
\dt+
\dp affiliates
