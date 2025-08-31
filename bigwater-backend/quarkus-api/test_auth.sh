#!/bin/bash

echo "=== BigWater API 认证测试 ==="
echo ""

# 测试基础API
echo "1. 测试基础API..."
curl -X GET http://localhost:9999/bw-api
echo ""
echo ""

# 测试注册
echo "2. 测试用户注册..."
curl -X POST http://localhost:9999/bw-api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "referralCode": "COMPANY001"
  }'
echo ""
echo ""

# 测试登录
echo "3. 测试用户登录..."
curl -X POST http://localhost:9999/bw-api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
echo ""
echo ""

# 测试错误密码登录
echo "4. 测试错误密码登录..."
curl -X POST http://localhost:9999/bw-api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "wrongpassword"
  }'
echo ""
echo ""

echo "=== 测试完成 ==="
