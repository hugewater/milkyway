-- Fix PostgreSQL permissions for BigWater affiliate system
-- Run this as a PostgreSQL superuser (usually postgres)
-- For database: ac1413, schema: bigwater, user: ac1413u

-- Connect to the ac1413 database first
\c ac1413;

-- Grant all privileges on the bigwater schema to ac1413u
GRANT ALL PRIVILEGES ON SCHEMA bigwater TO ac1413u;

-- Grant usage on the bigwater schema
GRANT USAGE ON SCHEMA bigwater TO ac1413u;

-- Grant all privileges on all tables in the bigwater schema to ac1413u
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA bigwater TO ac1413u;

-- Grant all privileges on all sequences in the bigwater schema to ac1413u
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA bigwater TO ac1413u;

-- Grant create privileges for future tables in bigwater schema
ALTER DEFAULT PRIVILEGES IN SCHEMA bigwater GRANT ALL ON TABLES TO ac1413u;
ALTER DEFAULT PRIVILEGES IN SCHEMA bigwater GRANT ALL ON SEQUENCES TO ac1413u;

-- Grant execute on all functions in the bigwater schema
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA bigwater TO ac1413u;
ALTER DEFAULT PRIVILEGES IN SCHEMA bigwater GRANT EXECUTE ON FUNCTIONS TO ac1413u;

-- Grant usage on custom types in the bigwater schema
GRANT USAGE ON TYPE bigwater.affiliate_level_type TO ac1413u;
GRANT USAGE ON TYPE bigwater.commission_type_enum TO ac1413u;
GRANT USAGE ON TYPE bigwater.commission_status_enum TO ac1413u;
GRANT USAGE ON TYPE bigwater.transaction_type_enum TO ac1413u;
GRANT USAGE ON TYPE bigwater.transaction_status_enum TO ac1413u;
GRANT USAGE ON TYPE bigwater.promotion_type_enum TO ac1413u;
GRANT USAGE ON TYPE bigwater.calculation_status_enum TO ac1413u;

-- Make sure the user can create objects in the bigwater schema
GRANT CREATE ON SCHEMA bigwater TO ac1413u;

-- Verify permissions
\dt+ bigwater.*
\dp bigwater.affiliates