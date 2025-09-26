# 合约地址集中管理配置

## 📋 概述

为了避免合约地址重复和错误，我们创建了统一的合约地址管理系统，将所有的区块链合约地址集中在一个地方进行管理。

## 🏗️ 架构设计

### 后端配置 (Java)

**文件位置**: `bigwater-backend/quarkus-api/src/main/java/com/app6768688/config/ContractAddresses.java`

```java
@ApplicationScoped
public class ContractAddresses {
    // USDT Contract Addresses
    public static final String POLYGON_USDT_CONTRACT = "0xc2132D05D31c914a87C6611C10748AEb04B58e8F";
    public static final String TRON_USDT_CONTRACT = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
    public static final String ETHEREUM_USDT_CONTRACT = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
    public static final String BSC_USDT_CONTRACT = "0x55d398326f99059fF775485246999027B3197955";
    
    // ACT Token Contract Addresses
    public static final String ACT_TOKEN_CONTRACT = "0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082";
}
```

### 前端配置 (JavaScript)

**文件位置**: `bigwater-ui/vue3/src/utils/contractAddresses.js`

```javascript
export const USDT_CONTRACTS = {
  'POL': '0xc2132D05D31c914a87C6611C10748AEb04B58e8F',  // Polygon USDT
  'TRX': 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t',         // TRON USDT
  'ETH': '0xdAC17F958D2ee523a2206206994597C13D831ec7',  // Ethereum USDT
  'BSC': '0x55d398326f99059fF775485246999027B3197955',  // BSC USDT
  'ACT': '0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082'   // ACT token (Polygon Amoy)
}
```

## 🔄 更新内容

### 后端更新

1. **创建了 `ContractAddresses.java` 配置类**
   - 集中管理所有合约地址
   - 提供工具方法获取合约地址
   - 支持按token类型和网络获取地址

2. **更新了 `TransactionVerificationService.java`**
   - 移除了本地合约地址常量
   - 使用 `ContractAddresses` 类中的地址
   - 更新了所有引用合约地址的地方

### 前端更新

1. **创建了 `contractAddresses.js` 配置文件**
   - 集中管理前端合约地址
   - 提供工具函数获取合约地址
   - 支持按钱包类型获取地址

2. **更新了所有Vue组件**
   - `MyWallets.vue`
   - `MyTransactions.vue`
   - `AdminTransactions.vue`
   - 移除了本地合约地址定义
   - 导入统一的合约地址配置

## 📍 合约地址列表

| Token | Network | Contract Address | 用途 |
|-------|---------|------------------|------|
| USDT | Polygon | `0xc2132D05D31c914a87C6611C10748AEb04B58e8F` | Polygon USDT |
| USDT | TRON | `TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t` | TRON USDT |
| USDT | Ethereum | `0xdAC17F958D2ee523a2206206994597C13D831ec7` | Ethereum USDT |
| USDT | BSC | `0x55d398326f99059fF775485246999027B3197955` | BSC USDT |
| ACT | Polygon Amoy | `0x738f3Cd37cC9CCeBd397057F465Cf3C59658D082` | ACT Token (测试网) |

## ✅ 优势

1. **统一管理**: 所有合约地址集中在一个地方
2. **避免错误**: 减少重复定义和拼写错误
3. **易于维护**: 修改地址只需要在一个地方更新
4. **类型安全**: 提供工具方法确保正确使用
5. **可扩展性**: 容易添加新的token和网络支持

## 🚀 使用方法

### 后端使用

```java
// 直接使用常量
String polygonUsdt = ContractAddresses.POLYGON_USDT_CONTRACT;

// 使用工具方法
String contractAddress = ContractAddresses.getContractAddress("USDT", "POL");
```

### 前端使用

```javascript
// 直接使用常量
const polygonUsdt = USDT_CONTRACTS.POL;

// 使用工具方法
const contractAddress = getContractAddressByWalletType('POL');
```

## 🔧 维护指南

1. **添加新token**: 在 `ContractAddresses.java` 和 `contractAddresses.js` 中添加新常量
2. **修改地址**: 只需要在两个配置文件中更新
3. **添加网络**: 在工具方法中添加新的网络支持
4. **测试**: 确保所有使用合约地址的地方都正常工作

## 📝 注意事项

- 确保前后端合约地址保持一致
- 添加新地址时更新文档
- 测试所有相关功能
- 考虑向后兼容性
