#!/bin/bash

echo "=== System Status Check ==="

# 检查Java
echo "1. Java Version:"
java -version 2>&1 || echo "Java not found"
echo ""

# 检查端口9999
echo "2. Port 9999 Status:"
netstat -an | grep 9999 || echo "Port 9999 not in use"
lsof -i :9999 || echo "No process listening on port 9999"
echo ""

# 检查Quarkus进程
echo "3. Quarkus Processes:"
ps aux | grep -i quarkus || echo "No Quarkus processes found"
echo ""

# 检查PostgreSQL
echo "4. PostgreSQL Status:"
ps aux | grep -i postgres || echo "PostgreSQL not found"
echo ""

# 检查Docker
echo "5. Docker Status:"
docker ps 2>&1 | head -5 || echo "Docker not available"
echo ""

echo "=== Check Complete ==="
