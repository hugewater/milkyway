# 合约地址配置化改进

## 📋 概述

我们已经成功将合约地址从硬编码改为配置化管理，现在可以通过修改 `application.properties` 文件来更新合约地址，无需重新编译代码。

## 🏗️ 架构改进

### 后端配置 (Java)

**文件位置**: `bigwater-backend/quarkus-api/src/main/java/com/app6768688/config/ContractAddresses.java`

```java
@ApplicationScoped
public class ContractAddresses {
    @Inject
    @ConfigProperty(name = "bw.contract.usdt.polygon", defaultValue = "0xc2132D05D31c914a87C6611C10748AEb04B58e8F")
    String polygonUsdtContract;
    
    @Inject
    @ConfigProperty(name = "bw.contract.usdt.tron", defaultValue = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t")
    String tronUsdtContract;
    
    // ... 其他合约地址配置
}
```

**配置文件**: `application.properties`

```properties
# Contract Addresses Configuration
# USDT Contract Addresses
bw.contract.usdt.polygon=0xc2132D05D31c914a87C6611C10748AEb04B58e8F
bw.contract.usdt.tron=TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t
bw.contract.usdt.ethereum=0xdAC17F958D2ee523a2206206994597C13D831ec7
bw.contract.usdt.bsc=0x55d398326f99059fF775485246999027B3197955

# ACT Token Contract Addresses
bw.contract.act.polygon=0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082
```

### 前端配置 (JavaScript)

**文件位置**: `bigwater-ui/vue3/src/config/contractAddresses.js`

```javascript
// 支持从环境变量或配置对象加载合约地址
export const CONTRACT_ADDRESSES = {
  POLYGON_USDT: getContractAddress('POLYGON_USDT', DEFAULT_CONTRACT_ADDRESSES.USDT_POLYGON),
  TRON_USDT: getContractAddress('TRON_USDT', DEFAULT_CONTRACT_ADDRESSES.USDT_TRON),
  // ... 其他合约地址
}
```

## 🔄 更新内容

### 后端更新

1. **重构了 `ContractAddresses.java`**
   - 从静态常量改为配置注入
   - 支持从 `application.properties` 读取配置
   - 提供默认值作为后备

2. **更新了 `application.properties`**
   - 添加了所有合约地址配置
   - 支持不同环境的配置

3. **更新了 `TransactionVerificationService.java`**
   - 移除了对静态常量的引用
   - 使用注入的 `ContractAddresses` 实例
   - 更新了所有引用合约地址的地方

### 前端更新

1. **创建了 `config/contractAddresses.js`**
   - 支持从环境变量或配置对象加载
   - 提供默认值作为后备
   - 支持运行时初始化

2. **更新了 `utils/contractAddresses.js`**
   - 改为从配置模块重新导出
   - 保持向后兼容性

## 📍 当前合约地址配置

| 网络 | 代币类型 | 合约地址 | 配置键 |
|------|----------|----------|--------|
| Polygon | USDT | `0xc2132D05D31c914a87C6611C10748AEb04B58e8F` | `bw.contract.usdt.polygon` |
| TRON | USDT | `TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t` | `bw.contract.usdt.tron` |
| Ethereum | USDT | `0xdAC17F958D2ee523a2206206994597C13D831ec7` | `bw.contract.usdt.ethereum` |
| BSC | USDT | `0x55d398326f99059fF775485246999027B3197955` | `bw.contract.usdt.bsc` |
| Polygon Amoy | ACT | `0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082` | `bw.contract.act.polygon` |

## ✅ 优势

1. **无需重新编译**: 修改合约地址只需要更新配置文件
2. **环境配置**: 支持不同环境使用不同的合约地址
3. **默认值**: 提供默认值确保系统正常运行
4. **向后兼容**: 保持现有API不变
5. **类型安全**: 提供工具方法确保正确使用
6. **可扩展性**: 容易添加新的合约地址

## 🚀 使用方法

### 后端使用

```java
// 直接使用配置类
@Inject
ContractAddresses contractAddresses;

// 获取特定合约地址
String polygonUsdt = contractAddresses.getPolygonUsdtContract();
String tronUsdt = contractAddresses.getTronUsdtContract();

// 使用工具方法
String address = contractAddresses.getContractAddress("USDT", "POL");
```

### 前端使用

```javascript
// 直接使用常量
import { USDT_CONTRACTS } from './utils/contractAddresses.js'

const polygonUsdt = USDT_CONTRACTS.POL;

// 使用工具方法
import { getContractAddress } from './utils/contractAddresses.js'
const address = getContractAddress('USDT', 'POL');
```

## 🔧 维护指南

1. **添加新合约**: 在配置文件中添加新的合约地址配置
2. **修改合约地址**: 只需要在配置文件中更新
3. **环境切换**: 通过配置文件或环境变量控制
4. **测试**: 确保所有使用合约地址的地方都正常工作

## 📝 配置示例

### 开发环境配置

```properties
# 开发环境合约地址
bw.contract.usdt.polygon=0xc2132D05D31c914a87C6611C10748AEb04B58e8F
bw.contract.usdt.tron=TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t
```

### 生产环境配置

```properties
# 生产环境合约地址
bw.contract.usdt.polygon=0xc2132D05D31c914a87C6611C10748AEb04B58e8F
bw.contract.usdt.tron=TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t
```

## 🔄 迁移指南

### 从硬编码迁移

1. **后端迁移**:
   ```java
   // 旧方式
   private static final String POLYGON_USDT_CONTRACT = "0xc2132D05D31c914a87C6611C10748AEb04B58e8F";
   
   // 新方式
   @Inject
   ContractAddresses contractAddresses;
   String address = contractAddresses.getPolygonUsdtContract();
   ```

2. **前端迁移**:
   ```javascript
   // 旧方式
   const USDT_CONTRACTS = {
     'POL': '0xc2132D05D31c914a87C6611C10748AEb04B58e8F'
   }
   
   // 新方式
   import { USDT_CONTRACTS } from './utils/contractAddresses.js'
   const address = USDT_CONTRACTS.POL;
   ```

## 🎯 最佳实践

1. **配置管理**: 将所有合约地址集中在配置文件中
2. **环境隔离**: 不同环境使用不同的配置文件
3. **默认值**: 始终提供合理的默认值
4. **文档更新**: 添加新合约时更新文档
5. **测试覆盖**: 确保所有配置路径都有测试覆盖

现在合约地址完全配置化，无需重新编译即可更新合约地址！
