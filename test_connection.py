#!/usr/bin/env python3
import requests
import psycopg2
import time

def test_api():
    """测试API端点是否正常工作"""
    try:
        url = "http://localhost:9999/bw-api/wallets?type=COMPANY&active=true"
        response = requests.get(url, timeout=10)
        print(f"API Response Status: {response.status_code}")
        print(f"API Response Content: {response.text[:200]}")
        return response.status_code == 200
    except Exception as e:
        print(f"API Test Failed: {e}")
        return False

def test_database():
    """测试数据库连接是否正常"""
    try:
        conn = psycopg2.connect(
            host="localhost",
            port=5432,
            user="ac1413",
            password="",  # 可能需要密码
            database="ac1413"
        )
        cursor = conn.cursor()
        cursor.execute("SELECT COUNT(*) FROM usdt_wallets WHERE is_company = true AND is_active = true")
        count = cursor.fetchone()[0]
        print(f"Database Test: Found {count} active company wallets")
        
        cursor.execute("SELECT id, wallet_name, wallet_address, wallet_type FROM usdt_wallets WHERE is_company = true AND is_active = true LIMIT 5")
        wallets = cursor.fetchall()
        for wallet in wallets:
            print(f"  Wallet: {wallet}")
        
        cursor.close()
        conn.close()
        return True
    except Exception as e:
        print(f"Database Test Failed: {e}")
        return False

if __name__ == "__main__":
    print("=== Testing Connection ===")
    
    # 先测试数据库
    db_ok = test_database()
    print(f"Database Connection: {'OK' if db_ok else 'FAILED'}")
    
    # 等待一下再测试API
    time.sleep(2)
    
    # 测试API
    api_ok = test_api()
    print(f"API Connection: {'OK' if api_ok else 'FAILED'}")
    
    print("=== Test Complete ===")
