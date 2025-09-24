-- PostgreSQL: add level column if not exists and index
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name='users' AND column_name='level'
    ) THEN
        ALTER TABLE users ADD COLUMN level TEXT NOT NULL DEFAULT 'bronze';
    END IF;
END$$;

CREATE INDEX IF NOT EXISTS idx_level ON users(level);
