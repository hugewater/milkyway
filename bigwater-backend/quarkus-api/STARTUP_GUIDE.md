# BigWater API 启动指南

## 🚀 快速启动

### 1. 启动数据库
```bash
# 确保MySQL数据库正在运行
docker exec -i bw-mysql mysql -u root -p123456 bwdb -e "SELECT 1;"
```

### 2. 启动后端API
```bash
cd /Users/superman/bigwater-cursor/bigwater-backend/quarkus-api

# 清理并编译
./mvnw clean compile

# 启动开发服务器
./mvnw quarkus:dev -Dquarkus.http.port=9999
```

### 3. 启动前端应用
```bash
cd /Users/superman/bigwater-cursor/bigwater-ui/vue3

# 启动开发服务器
npm run dev
```

## 🔧 故障排除

### 端口被占用
```bash
# 检查端口占用
lsof -i :9999

# 杀死占用进程
lsof -ti:9999 | xargs kill -9
```

### 数据库连接问题
```bash
# 检查数据库状态
docker ps | grep mysql

# 重启数据库
docker restart bw-mysql
```

### 编译错误
```bash
# 清理并重新编译
./mvnw clean compile
```

## 📋 测试账户

### 公司管理员账户
- **邮箱**: `company@bigwater.com`
- **密码**: `company123`
- **角色**: `SUPER_ADMIN`

### 测试API
```bash
# 运行测试脚本
chmod +x test_api.sh
./test_api.sh
```

## 🌐 访问地址

- **前端应用**: http://localhost:5175/
- **后端API**: http://localhost:9999/bw-api
- **API文档**: http://localhost:9999/swagger-ui

## 📝 常见问题

### 1. "Failed to fetch" 错误
- 检查后端API是否正在运行
- 确认端口9999没有被占用
- 检查CORS配置

### 2. 数据库连接失败
- 确认MySQL容器正在运行
- 检查数据库凭据
- 确认数据库表已创建

### 3. 编译失败
- 检查Java版本 (需要Java 17+)
- 清理并重新编译项目
- 检查依赖项是否正确

## 🔍 日志查看

### 后端日志
```bash
# 查看Quarkus日志
tail -f target/quarkus.log
```

### 前端日志
```bash
# 在浏览器开发者工具中查看
F12 -> Console
```

## 📞 支持

如果遇到问题，请检查：
1. 所有服务是否正在运行
2. 端口是否被正确配置
3. 数据库连接是否正常
4. 网络连接是否正常
