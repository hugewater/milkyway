#!/bin/bash

echo "=== Backend Service Test ==="

echo "1. Checking if port 9999 is in use:"
lsof -i :9999

echo -e "\n2. Checking Java processes:"
ps aux | grep java | grep -v grep

echo -e "\n3. Testing API endpoint:"
curl -s "http://localhost:9999/bw-api/wallets?type=COMPANY&active=true" || echo "API call failed"

echo -e "\n4. Testing health endpoint:"
curl -s "http://localhost:9999/bw-api/health" || echo "Health check failed"

echo -e "\n5. Checking if service is responding:"
curl -s "http://localhost:9999/" || echo "Root endpoint failed"
