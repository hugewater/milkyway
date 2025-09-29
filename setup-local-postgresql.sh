#!/bin/bash

# PostgreSQL Local Setup Script for BigWater Affiliate System
# This script helps you set up the affiliate tables in your local PostgreSQL database

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}===================================================${NC}"
    echo -e "${BLUE} $1${NC}"
    echo -e "${BLUE}===================================================${NC}"
}

# Function to get database connection details
get_db_info() {
    print_header "PostgreSQL Database Setup"
    
    read -p "Enter PostgreSQL username (default: postgres): " DB_USER
    DB_USER=${DB_USER:-postgres}
    
    read -p "Enter PostgreSQL host (default: localhost): " DB_HOST
    DB_HOST=${DB_HOST:-localhost}
    
    read -p "Enter PostgreSQL port (default: 5432): " DB_PORT
    DB_PORT=${DB_PORT:-5432}
    
    read -p "Enter database name (default: bigwater_affiliate): " DB_NAME
    DB_NAME=${DB_NAME:-bigwater_affiliate}
    
    print_status "Using connection: $DB_USER@$DB_HOST:$DB_PORT/$DB_NAME"
}

# Function to test database connection
test_connection() {
    print_header "Testing Database Connection"
    
    if psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -c "SELECT version();" >/dev/null 2>&1; then
        print_status "Database connection successful!"
        return 0
    else
        print_error "Failed to connect to database. Please check your credentials and try again."
        return 1
    fi
}

# Function to create database if it doesn't exist
create_database() {
    print_header "Creating Database (if needed)"
    
    # Try to connect to the target database
    if ! psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -c "SELECT 1;" >/dev/null 2>&1; then
        print_warning "Database '$DB_NAME' doesn't exist. Creating it..."
        
        # Connect to postgres database to create the target database
        if psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d postgres -c "CREATE DATABASE $DB_NAME;" >/dev/null 2>&1; then
            print_status "Database '$DB_NAME' created successfully!"
        else
            print_error "Failed to create database '$DB_NAME'. Please create it manually."
            return 1
        fi
    else
        print_status "Database '$DB_NAME' already exists."
    fi
}

# Function to apply the schema
apply_schema() {
    print_header "Applying Affiliate System Schema"
    
    SCHEMA_FILE="AFFILIATE_DATABASE_SCHEMA_POSTGRESQL.sql"
    
    if [ ! -f "$SCHEMA_FILE" ]; then
        print_error "Schema file '$SCHEMA_FILE' not found in current directory."
        print_error "Please make sure you're running this script from the project root directory."
        return 1
    fi
    
    print_status "Applying schema from $SCHEMA_FILE..."
    
    if psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -f "$SCHEMA_FILE"; then
        print_status "Schema applied successfully!"
        return 0
    else
        print_error "Failed to apply schema. Please check the error messages above."
        return 1
    fi
}

# Function to verify the installation
verify_installation() {
    print_header "Verifying Installation"
    
    # Check if tables were created
    TABLES=$(psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -t -c "SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename LIKE '%affiliate%';" 2>/dev/null | wc -l)
    
    if [ "$TABLES" -gt 0 ]; then
        print_status "Found $TABLES affiliate-related tables:"
        psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -c "SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename LIKE '%affiliate%' ORDER BY tablename;"
        
        # Check custom types
        print_status "Custom types created:"
        psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -c "SELECT typname FROM pg_type WHERE typname LIKE '%_type' OR typname LIKE '%_enum' ORDER BY typname;"
        
        # Check functions
        print_status "Custom functions created:"
        psql -U "$DB_USER" -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -c "SELECT proname FROM pg_proc WHERE proname LIKE '%affiliate%' ORDER BY proname;"
        
        return 0
    else
        print_error "No affiliate tables found. Installation may have failed."
        return 1
    fi
}

# Function to show sample usage
show_sample_usage() {
    print_header "Sample Usage"
    
    echo "You can now test the affiliate system with these sample commands:"
    echo
    echo "1. Connect to your database:"
    echo "   psql -U $DB_USER -h $DB_HOST -p $DB_PORT -d $DB_NAME"
    echo
    echo "2. View all tables:"
    echo "   \\dt"
    echo
    echo "3. Check sample affiliates:"
    echo "   SELECT * FROM affiliates;"
    echo
    echo "4. Test hierarchy function:"
    echo "   SELECT * FROM get_affiliate_upline(1);"
    echo
    echo "5. Insert a test affiliate:"
    echo "   INSERT INTO affiliates (email, first_name, last_name) VALUES ('test@example.com', 'Test', 'User');"
}

# Main function
main() {
    print_header "BigWater Affiliate System - Local PostgreSQL Setup"
    
    # Check if psql is available
    if ! command -v psql >/dev/null 2>&1; then
        print_error "psql command not found. Please install PostgreSQL client tools."
        exit 1
    fi
    
    get_db_info
    
    if ! test_connection; then
        print_error "Cannot proceed without database connection."
        exit 1
    fi
    
    create_database
    
    if apply_schema; then
        if verify_installation; then
            print_status "✅ Affiliate system setup completed successfully!"
            show_sample_usage
        else
            print_warning "Setup completed but verification failed. Please check manually."
        fi
    else
        print_error "❌ Setup failed. Please check the errors above and try again."
        exit 1
    fi
}

# Run main function
main "$@"