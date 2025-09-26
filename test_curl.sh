#!/bin/bash

echo "Testing transaction status update with curl..."

# Wait for backend
echo "Waiting for backend to start..."
for i in {1..30}; do
    if curl -s http://localhost:9999/bw-api/transactions > /dev/null 2>&1; then
        echo "Backend is ready!"
        break
    fi
    sleep 1
done

# Get transactions
echo "Getting transactions..."
TRANSACTIONS=$(curl -s -X GET "http://localhost:9999/bw-api/transactions" -H "Content-Type: application/json")
echo "Transactions response: $TRANSACTIONS"

# Extract first transaction ID
TRANSACTION_ID=$(echo $TRANSACTIONS | jq -r '.data[0].id // empty')
if [ -z "$TRANSACTION_ID" ]; then
    echo "No transactions found"
    exit 1
fi

echo "Found transaction ID: $TRANSACTION_ID"

# Get users
echo "Getting users..."
USERS=$(curl -s -X GET "http://localhost:9999/bw-api/users" -H "Content-Type: application/json")
echo "Users response: $USERS"

# Extract user ID from transaction
USER_ID=$(echo $TRANSACTIONS | jq -r ".data[0].userId // empty")
if [ -z "$USER_ID" ]; then
    echo "No user ID found in transaction"
    exit 1
fi

echo "Found user ID: $USER_ID"

# Get user's current total_pay
OLD_TOTAL_PAY=$(echo $USERS | jq -r ".data[] | select(.id == $USER_ID) | .totalPay // 0")
echo "User $USER_ID current total_pay: $OLD_TOTAL_PAY"

# Update transaction status to COMPLETED
echo "Updating transaction $TRANSACTION_ID status to COMPLETED..."
UPDATE_RESPONSE=$(curl -s -X PUT "http://localhost:9999/bw-api/transactions/$TRANSACTION_ID" \
    -H "Content-Type: application/json" \
    -d '{"status": "COMPLETED"}')
echo "Update response: $UPDATE_RESPONSE"

# Wait a moment
sleep 2

# Get users again
echo "Getting users after update..."
USERS_AFTER=$(curl -s -X GET "http://localhost:9999/bw-api/users" -H "Content-Type: application/json")

# Get user's new total_pay
NEW_TOTAL_PAY=$(echo $USERS_AFTER | jq -r ".data[] | select(.id == $USER_ID) | .totalPay // 0")
echo "User $USER_ID new total_pay: $NEW_TOTAL_PAY"

# Check if total_pay changed
if [ "$NEW_TOTAL_PAY" != "$OLD_TOTAL_PAY" ]; then
    echo "✅ SUCCESS: total_pay changed from $OLD_TOTAL_PAY to $NEW_TOTAL_PAY"
else
    echo "❌ FAILED: total_pay did not change (still $OLD_TOTAL_PAY)"
fi

echo "Test completed."
