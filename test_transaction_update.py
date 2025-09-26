#!/usr/bin/env python3

import requests
import json
import time

def test_transaction_update():
    base_url = "http://localhost:9999/bw-api"
    
    print("Testing transaction status update and total_pay functionality...")
    
    # Wait for backend to be ready
    print("Waiting for backend to start...")
    for i in range(30):
        try:
            response = requests.get(f"{base_url}/transactions", timeout=5)
            if response.status_code == 200:
                print("Backend is ready!")
                break
        except:
            time.sleep(1)
    else:
        print("Backend is not responding. Please start it first.")
        return
    
    # Get transactions
    print("\n1. Getting transactions...")
    response = requests.get(f"{base_url}/transactions")
    if response.status_code != 200:
        print(f"Failed to get transactions: {response.status_code}")
        return
    
    transactions = response.json().get('data', [])
    if not transactions:
        print("No transactions found. Please create a transaction first.")
        return
    
    transaction = transactions[0]
    transaction_id = transaction['id']
    user_id = transaction.get('userId')
    
    print(f"Found transaction ID: {transaction_id}, User ID: {user_id}")
    
    # Get user before update
    print("\n2. Getting user before update...")
    response = requests.get(f"{base_url}/users")
    if response.status_code != 200:
        print(f"Failed to get users: {response.status_code}")
        return
    
    users = response.json().get('data', [])
    user = next((u for u in users if u['id'] == user_id), None)
    if not user:
        print(f"User {user_id} not found")
        return
    
    old_total_pay = user.get('totalPay', 0)
    print(f"User {user_id} current total_pay: {old_total_pay}")
    
    # Update transaction status to COMPLETED
    print(f"\n3. Updating transaction {transaction_id} status to COMPLETED...")
    update_data = {"status": "COMPLETED"}
    response = requests.put(f"{base_url}/transactions/{transaction_id}", 
                           json=update_data,
                           headers={"Content-Type": "application/json"})
    
    if response.status_code != 200:
        print(f"Failed to update transaction: {response.status_code} - {response.text}")
        return
    
    print("Transaction status updated successfully!")
    
    # Wait a moment for the update to process
    time.sleep(2)
    
    # Get user after update
    print("\n4. Getting user after update...")
    response = requests.get(f"{base_url}/users")
    if response.status_code != 200:
        print(f"Failed to get users: {response.status_code}")
        return
    
    users = response.json().get('data', [])
    user = next((u for u in users if u['id'] == user_id), None)
    if not user:
        print(f"User {user_id} not found")
        return
    
    new_total_pay = user.get('totalPay', 0)
    print(f"User {user_id} new total_pay: {new_total_pay}")
    
    # Check if total_pay changed
    if new_total_pay != old_total_pay:
        print(f"✅ SUCCESS: total_pay changed from {old_total_pay} to {new_total_pay}")
    else:
        print(f"❌ FAILED: total_pay did not change (still {old_total_pay})")
    
    # Test changing back to PENDING
    print(f"\n5. Testing change back to PENDING...")
    update_data = {"status": "PENDING"}
    response = requests.put(f"{base_url}/transactions/{transaction_id}", 
                           json=update_data,
                           headers={"Content-Type": "application/json"})
    
    if response.status_code != 200:
        print(f"Failed to update transaction: {response.status_code} - {response.text}")
        return
    
    print("Transaction status updated to PENDING!")
    
    # Wait a moment for the update to process
    time.sleep(2)
    
    # Get user after second update
    print("\n6. Getting user after second update...")
    response = requests.get(f"{base_url}/users")
    if response.status_code != 200:
        print(f"Failed to get users: {response.status_code}")
        return
    
    users = response.json().get('data', [])
    user = next((u for u in users if u['id'] == user_id), None)
    if not user:
        print(f"User {user_id} not found")
        return
    
    final_total_pay = user.get('totalPay', 0)
    print(f"User {user_id} final total_pay: {final_total_pay}")
    
    # Check if total_pay changed back
    if final_total_pay == old_total_pay:
        print(f"✅ SUCCESS: total_pay correctly reverted from {new_total_pay} to {final_total_pay}")
    else:
        print(f"❌ FAILED: total_pay did not revert correctly (expected {old_total_pay}, got {final_total_pay})")

if __name__ == "__main__":
    test_transaction_update()
