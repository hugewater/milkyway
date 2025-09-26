-- V27: Fix transactions metadata column type from JSONB to VARCHAR
BEGIN;

-- Change metadata column from JSONB to VARCHAR(255)
ALTER TABLE transactions ALTER COLUMN metadata TYPE VARCHAR(255);

COMMIT;
