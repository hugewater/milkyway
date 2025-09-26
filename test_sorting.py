#!/usr/bin/env python3
import requests
import json

# Test the sorting functionality
base_url = "http://localhost:9999/bw-api"

def test_user_sorting():
    try:
        # Test getting users with total_pay sorting
        response = requests.get(f"{base_url}/users/paged", params={
            "offset": 0,
            "limit": 10,
            "sort": "total_pay",
            "order": "desc"
        })
        
        if response.status_code == 200:
            data = response.json()
            print("Users sorted by total_pay (desc):")
            for user in data.get('data', []):
                print(f"ID: {user.get('id')}, Total Pay: {user.get('totalPay', 0)}")
        else:
            print(f"Error: {response.status_code} - {response.text}")
            
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    test_user_sorting()
