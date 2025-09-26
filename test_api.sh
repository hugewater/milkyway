#!/bin/bash

echo "Testing Company Wallets API..."
echo "URL: http://localhost:9999/bw-api/wallets?type=COMPANY&active=true"
echo ""

echo "Testing with curl:"
curl -v "http://localhost:9999/bw-api/wallets?type=COMPANY&active=true" 2>&1

echo ""
echo "Testing with simplified parameters:"
curl -v "http://localhost:9999/bw-api/wallets?type=COMPANY" 2>&1

echo ""
echo "Testing all wallets:"
curl -v "http://localhost:9999/bw-api/wallets" 2>&1
