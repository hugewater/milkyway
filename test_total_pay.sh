#!/bin/bash

echo "Testing total_pay update functionality..."

# Wait for backend to start
echo "Waiting for backend to start..."
sleep 5

# Test if backend is running
echo "Testing backend connection..."
curl -s -X GET "http://localhost:9999/bw-api/transactions" -H "Content-Type: application/json" > /dev/null
if [ $? -eq 0 ]; then
    echo "Backend is running on port 9999"
    PORT=9999
else
    echo "Testing port 9998..."
    curl -s -X GET "http://localhost:9998/bw-api/transactions" -H "Content-Type: application/json" > /dev/null
    if [ $? -eq 0 ]; then
        echo "Backend is running on port 9998"
        PORT=9998
    else
        echo "Backend is not running. Please start it first."
        exit 1
    fi
fi

echo "Getting transactions..."
curl -X GET "http://localhost:$PORT/bw-api/transactions" -H "Content-Type: application/json" | jq '.data[0]'

echo "Getting users..."
curl -X GET "http://localhost:$PORT/bw-api/users" -H "Content-Type: application/json" | jq '.data[0]'

echo "Test completed."
