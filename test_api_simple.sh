#!/bin/bash

echo "Testing API endpoints..."

echo "1. Testing health check:"
curl -s "http://localhost:9999/bw-api/health" || echo "Health check failed"

echo -e "\n2. Testing wallets endpoint:"
curl -s "http://localhost:9999/bw-api/wallets?type=COMPANY&active=true" || echo "Wallets API failed"

echo -e "\n3. Testing all wallets:"
curl -s "http://localhost:9999/bw-api/wallets" || echo "All wallets API failed"

echo -e "\n4. Checking if service is running:"
lsof -i :9999 || echo "No process on port 9999"
