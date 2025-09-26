# API URLs 集中管理配置

## 📋 概述

为了避免API URL重复和错误，我们创建了统一的API URL管理系统，将所有的外部API URL集中在一个地方进行管理。

## 🏗️ 架构设计

### 后端配置 (Java)

**文件位置**: `bigwater-backend/quarkus-api/src/main/java/com/app6768688/config/ApiUrls.java`

```java
@ApplicationScoped
public class ApiUrls {
    @Inject
    @ConfigProperty(name = "bw.api.polygon.url", defaultValue = "https://api.polygonscan.com/api")
    String polygonApiUrl;
    
    @Inject
    @ConfigProperty(name = "bw.api.tron.url", defaultValue = "https://api.trongrid.io")
    String tronApiUrl;
    
    // ... 其他API URL配置
}
```

**配置文件**: `application.properties`

```properties
# Blockchain API URLs
bw.api.polygon.url=https://api.polygonscan.com/api
bw.api.tron.url=https://api.trongrid.io
bw.api.polygon.amoy.url=https://api-amoy.polygonscan.com/api
bw.api.alchemy.polygon.url=https://polygon-mainnet.g.alchemy.com/v2
bw.api.moralis.polygon.url=https://deep-index.moralis.io/api/v2
```

### 前端配置 (JavaScript)

**文件位置**: `bigwater-ui/vue3/src/utils/apiUrls.js`

```javascript
export const BACKEND_API_URLS = {
  BASE: process.env.NODE_ENV === 'production' 
    ? 'https://your-production-api.com/bw-api' 
    : 'http://localhost:9999/bw-api',
  
  // Authentication endpoints
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  VALIDATE: '/auth/validate',
  
  // Wallet endpoints
  WALLETS: '/wallets',
  WALLETS_USER: '/wallets/user',
  WALLETS_COMPANY: '/wallets/company',
  
  // Transaction endpoints
  TRANSACTIONS: '/transactions',
  TRANSACTIONS_VERIFY: '/transactions/verify'
}
```

## 🔄 更新内容

### 后端更新

1. **创建了 `ApiUrls.java` 配置类**
   - 集中管理所有API URL
   - 从 `application.properties` 读取配置
   - 提供工具方法获取URL

2. **更新了 `application.properties`**
   - 添加了所有区块链API URL配置
   - 支持不同环境的配置

3. **更新了 `TransactionVerificationService.java`**
   - 移除了硬编码的URL常量
   - 使用 `ApiUrls` 类中的URL
   - 更新了所有引用URL的地方

### 前端更新

1. **创建了 `apiUrls.js` 配置文件**
   - 集中管理前端API URL
   - 支持生产/开发环境切换
   - 提供工具函数获取URL

2. **更新了 `api.js`**
   - 导入统一的URL配置
   - 使用配置化的URL而不是硬编码

## 📍 API URL列表

### 后端API URLs

| 服务 | 网络 | URL | 用途 |
|------|------|-----|------|
| Polygonscan | Polygon | `https://api.polygonscan.com/api` | Polygon主网API |
| Polygonscan | Polygon Amoy | `https://api-amoy.polygonscan.com/api` | Polygon测试网API |
| TronGrid | TRON | `https://api.trongrid.io` | TRON网络API |
| Alchemy | Polygon | `https://polygon-mainnet.g.alchemy.com/v2` | Alchemy Polygon API |
| Moralis | Polygon | `https://deep-index.moralis.io/api/v2` | Moralis Polygon API |

### 前端API URLs

| 端点 | URL | 用途 |
|------|-----|------|
| 登录 | `/auth/login` | 用户登录 |
| 注册 | `/auth/register` | 用户注册 |
| 验证 | `/auth/validate` | Token验证 |
| 钱包 | `/wallets` | 钱包管理 |
| 交易 | `/transactions` | 交易管理 |
| 验证 | `/transactions/verify` | 交易验证 |

## ✅ 优势

1. **统一管理**: 所有API URL集中在一个地方
2. **环境配置**: 支持不同环境的URL配置
3. **避免错误**: 减少重复定义和拼写错误
4. **易于维护**: 修改URL只需要在配置文件中更新
5. **类型安全**: 提供工具方法确保正确使用
6. **可扩展性**: 容易添加新的API服务

## 🚀 使用方法

### 后端使用

```java
// 直接使用配置类
@Inject
ApiUrls apiUrls;

// 获取特定API URL
String polygonUrl = apiUrls.getPolygonApiUrl();
String tronUrl = apiUrls.getTronApiUrl();

// 使用工具方法
String url = apiUrls.getApiUrl("polygon", "mainnet");
```

### 前端使用

```javascript
// 直接使用常量
import { BACKEND_API_URLS } from './apiUrls.js'

const loginUrl = BACKEND_API_URLS.LOGIN;

// 使用工具方法
import { getApiUrl } from './apiUrls.js'
const fullUrl = getApiUrl('/auth/login');
```

## 🔧 维护指南

1. **添加新API**: 在 `ApiUrls.java` 和 `apiUrls.js` 中添加新URL
2. **修改URL**: 只需要在配置文件中更新
3. **环境切换**: 通过配置文件或环境变量控制
4. **测试**: 确保所有使用URL的地方都正常工作

## 📝 注意事项

- 确保前后端URL配置保持一致
- 添加新URL时更新文档
- 测试所有相关功能
- 考虑向后兼容性
- 生产环境URL需要正确配置

## 🔄 迁移指南

### 从硬编码URL迁移

1. **后端迁移**:
   ```java
   // 旧方式
   private static final String POLYGON_API_URL = "https://api.polygonscan.com/api";
   
   // 新方式
   @Inject
   ApiUrls apiUrls;
   String url = apiUrls.getPolygonApiUrl();
   ```

2. **前端迁移**:
   ```javascript
   // 旧方式
   const API_BASE_URL = '/bw-api'
   
   // 新方式
   import { BACKEND_API_URLS } from './apiUrls.js'
   const baseUrl = BACKEND_API_URLS.BASE;
   ```

现在所有的API URL都集中管理，避免了重复和错误的问题！
