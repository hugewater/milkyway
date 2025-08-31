#!/bin/bash

echo "=== BigWater API 测试脚本 ==="
echo ""

# 测试基础API
echo "1. 测试基础API..."
curl -X GET http://localhost:9999/bw-api
echo ""
echo ""

# 测试健康检查
echo "2. 测试健康检查..."
curl -X GET http://localhost:9999/bw-api/health
echo ""
echo ""

# 测试公司用户登录
echo "3. 测试公司用户登录..."
curl -X POST http://localhost:9999/bw-api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "company@bigwater.com",
    "password": "company123"
  }'
echo ""
echo ""

# 测试用户统计
echo "4. 测试用户统计..."
curl -X GET http://localhost:9999/bw-api/users/stats
echo ""
echo ""

echo "=== 测试完成 ==="
