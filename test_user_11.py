#!/usr/bin/env python3
import requests
import json

BASE_URL = "http://localhost:9999/bw-api"

def get_user_total_pay(user_id):
    try:
        response = requests.get(f"{BASE_URL}/users/{user_id}")
        response.raise_for_status()
        user_data = response.json()
        return user_data.get("totalPay")
    except requests.exceptions.RequestException as e:
        print(f"Error fetching user {user_id} total pay: {e}")
        return None

def update_user_total_pay(user_id, amount):
    try:
        payload = {"totalPay": amount}
        response = requests.put(f"{BASE_URL}/users/{user_id}", json=payload)
        response.raise_for_status()
        print(f"Successfully updated user {user_id} total pay to {amount}")
        return True
    except requests.exceptions.RequestException as e:
        print(f"Error updating user {user_id} total pay: {e}")
        return False

def test_sorting():
    try:
        # Test sorting by total_pay desc
        response = requests.get(f"{BASE_URL}/users", params={
            "offset": 0,
            "limit": 5,
            "sort": "total_pay",
            "order": "desc"
        })
        response.raise_for_status()
        
        data = response.json()
        print(f"\n=== Users sorted by total_pay (desc) ===")
        print(f"Total users: {data.get('total', 0)}")
        
        users = data.get('data', [])
        for i, user in enumerate(users, 1):
            print(f"{i}. ID: {user.get('id')}, Total Pay: {user.get('totalPay', 0)}, Name: {user.get('firstName', '')} {user.get('lastName', '')}")
        
        # Check if user 11 is at the top
        if users and users[0].get('id') == 11:
            print("\n✅ SUCCESS: User 11 is at the top when sorted by total_pay!")
        else:
            print(f"\n❌ ISSUE: User 11 is not at the top. Top user is ID: {users[0].get('id') if users else 'None'}")
            
    except requests.exceptions.RequestException as e:
        print(f"Error testing sorting: {e}")

if __name__ == "__main__":
    user_id_to_test = 11
    expected_total_pay = 1680.00000000
    
    print(f"Checking current total_pay for user {user_id_to_test}...")
    current_pay = get_user_total_pay(user_id_to_test)
    print(f"User {user_id_to_test} current total_pay: {current_pay}")
    
    if current_pay != expected_total_pay:
        print(f"Attempting to update user {user_id_to_test} total_pay to {expected_total_pay}...")
        if update_user_total_pay(user_id_to_test, expected_total_pay):
            print(f"Verification: Fetching total_pay again for user {user_id_to_test}...")
            updated_pay = get_user_total_pay(user_id_to_test)
            print(f"User {user_id_to_test} updated total_pay: {updated_pay}")
            if updated_pay == expected_total_pay:
                print("User total_pay updated successfully!")
            else:
                print("User total_pay update failed or did not reflect correctly.")
        else:
            print("Failed to update user total_pay.")
    else:
        print(f"User {user_id_to_test} total_pay is already {expected_total_pay}.")
    
    # Test sorting functionality
    print("\n" + "="*50)
    test_sorting()