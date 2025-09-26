#!/bin/bash

echo "Simple test for total_pay update..."

# Test if backend is running
echo "Testing backend connection..."
if curl -s http://localhost:9999/bw-api/transactions > /dev/null 2>&1; then
    echo "Backend is running on port 9999"
    PORT=9999
elif curl -s http://localhost:9998/bw-api/transactions > /dev/null 2>&1; then
    echo "Backend is running on port 9998"
    PORT=9998
else
    echo "Backend is not running. Please start it first."
    echo "Run: cd /Users/superman/hugewater-milkyway/dev/bigwater-backend/quarkus-api && mvn quarkus:dev"
    exit 1
fi

echo "Backend is running on port $PORT"

# Get transactions
echo "Getting transactions..."
TRANSACTIONS=$(curl -s "http://localhost:$PORT/bw-api/transactions")
echo "Transactions: $TRANSACTIONS"

# Get users
echo "Getting users..."
USERS=$(curl -s "http://localhost:$PORT/bw-api/users")
echo "Users: $USERS"

echo "Test completed."
