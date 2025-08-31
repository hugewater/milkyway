# BigWater Quarkus API

BigWater Digital Weekly Journal 后端API的Quarkus版本。

## 技术栈

- **Framework**: Quarkus 3.x
- **Language**: Java 17
- **Database**: MySQL 8.0+
- **ORM**: Hibernate ORM with Panache
- **Authentication**: JWT (可扩展)
- **API Documentation**: Swagger UI
- **Build Tool**: Maven

## 快速开始

### 1. 开发模式运行

```bash
# 进入项目目录
cd bigwater-backend/quarkus-api

# 开发模式运行
./mvnw quarkus:dev
```

### 2. 构建和运行

```bash
# 编译项目
./mvnw compile

# 打包项目
./mvnw package

# 运行打包后的应用
java -jar target/quarkus-app/quarkus-run.jar
```

### 3. Docker运行

```bash
# 构建Docker镜像
docker build -f src/main/docker/Dockerfile.jvm -t quarkus-api .

# 运行容器
docker run -p 8080:8080 quarkus-api
```

### 4. 使用Docker Compose

```bash
# 启动所有服务
cd script/docker
docker-compose up -d

# 查看日志
docker-compose logs -f quarkus-api
```

## API端点

### 基础端点
- `GET /bw-api` - API信息
- `GET /bw-api/health` - 健康检查
- `GET /bw-api/version` - 版本信息

### Swagger UI
- `GET /swagger-ui` - API文档界面

## 配置

主要配置在 `src/main/resources/application.properties` 文件中：

```properties
# 数据库配置
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=root
quarkus.datasource.password=123456
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/bwdb

# HTTP配置
quarkus.http.port=8080
quarkus.http.host=0.0.0.0

# CORS配置
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000
```

## 开发

### 添加新的API端点

1. 在 `src/main/java/com/app6768688/` 目录下创建新的资源类
2. 使用 `@Path("/bw-api/your-endpoint")` 注解
3. 实现相应的HTTP方法

示例：
```java
@Path("/bw-api/users")
public class UserResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        // 实现逻辑
    }
}
```

### 数据库操作

使用Hibernate ORM with Panache进行数据库操作：

```java
@Entity
public class User extends PanacheEntity {
    public String email;
    public String name;
}

// 在资源类中使用
@GET
@Path("/users")
public List<User> getUsers() {
    return User.listAll();
}
```

## 部署

### 生产环境

```bash
# 构建生产版本
./mvnw clean package -Dquarkus.package.type=uber-jar

# 运行
java -jar target/quarkus-api-1.0.0-runner.jar
```

### Docker部署

```bash
# 构建镜像
docker build -f src/main/docker/Dockerfile.jvm -t bigwater-quarkus-api .

# 运行容器
docker run -p 8080:8080 \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:mysql://your-db:3306/bwdb \
  bigwater-quarkus-api
```

## 监控

- **健康检查**: `GET /q/health`
- **指标**: `GET /q/metrics`
- **信息**: `GET /q/info`

## 许可证

MIT License
