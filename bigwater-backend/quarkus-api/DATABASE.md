# BigWater Database Schema Documentation

## 概述

BigWater Digital Weekly Journal 使用MySQL数据库，包含以下主要功能模块：

## 数据库表结构

### 1. 用户管理 (Users)

#### `users` - 用户表
- **主要字段**: id, email, password_hash, first_name, last_name, phone
- **角色**: SUBSCRIBER, ADMIN, SUPER_ADMIN
- **状态**: ACTIVE, INACTIVE, SUSPENDED
- **推荐系统**: referral_code, referred_by_code
- **时间戳**: join_date, last_login, created_at, updated_at

#### `usdt_wallets` - USDT钱包表
- **关联**: 每个用户可以有多个钱包
- **钱包类型**: MAIN, TRADING, STAKING, REWARDS
- **余额**: 精确到8位小数
- **状态**: is_active, is_verified

### 2. 内容管理 (Content)

#### `journals` - 周刊表
- **主要字段**: week_number, title, excerpt, content
- **状态**: DRAFT, PUBLISHED, SCHEDULED
- **特性**: featured, tags, views
- **时间**: publish_date

#### `journal_views` - 周刊浏览记录
- **记录**: 用户浏览行为
- **匿名支持**: user_id可为NULL
- **信息**: ip_address, user_agent

### 3. 抽奖系统 (Drawings)

#### `random_drawings` - 抽奖表
- **类型**: POWERBALL, LOTTERY, RAFFLE
- **信息**: drawing_name, week_number, drawing_date
- **奖金**: prize_pool
- **状态**: PENDING, COMPLETED, CANCELLED
- **中奖号码**: winning_numbers (JSON)

#### `drawing_participants` - 参与者表
- **关联**: drawing_id, user_id
- **参与**: participant_numbers (JSON), entry_fee
- **结果**: is_winner, prize_amount

### 4. 证书系统 (Certificates)

#### `certificates` - 证书定义表
- **类型**: BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
- **价格**: price_usdt
- **有效期**: duration_days
- **福利**: benefits (JSON)
- **供应**: max_supply, current_supply

#### `user_certificates` - 用户证书表
- **关联**: user_id, certificate_id
- **购买**: purchase_date, purchase_amount_usdt
- **状态**: ACTIVE, EXPIRED, CANCELLED
- **过期**: expiry_date

### 5. 推荐系统 (Referrals)

#### `referral_relationships` - 推荐关系表
- **关系**: referrer_id, referred_id
- **层级**: referral_level (1=直接, 2=间接)
- **佣金**: commission_rate, total_earnings
- **状态**: ACTIVE, INACTIVE, CANCELLED

### 6. 交易系统 (Transactions)

#### `transactions` - 交易表
- **类型**: DEPOSIT, WITHDRAWAL, PURCHASE, REFUND, REWARD, COMMISSION
- **金额**: amount_usdt (精确到8位小数)
- **状态**: PENDING, COMPLETED, FAILED, CANCELLED
- **关联**: related_entity_type, related_entity_id
- **元数据**: metadata (JSON)

### 7. 日志系统 (Logging)

#### `access_logs` - 访问日志
- **记录**: 所有API访问
- **信息**: ip_address, user_agent, request_method, request_url
- **性能**: response_status, response_time_ms

#### `error_logs` - 错误日志
- **级别**: DEBUG, INFO, WARNING, ERROR, CRITICAL
- **信息**: error_type, error_message, stack_trace
- **上下文**: request_url, ip_address, additional_data

### 8. 系统设置 (Settings)

#### `system_settings` - 系统设置表
- **键值对**: setting_key, setting_value
- **类型**: STRING, NUMBER, BOOLEAN, JSON
- **可见性**: is_public

## 数据库关系图

```
users (1) ←→ (N) usdt_wallets
users (1) ←→ (N) journals
users (1) ←→ (N) journal_views
users (1) ←→ (N) user_certificates
users (1) ←→ (N) drawing_participants
users (1) ←→ (N) transactions
users (1) ←→ (N) access_logs
users (1) ←→ (N) error_logs

certificates (1) ←→ (N) user_certificates
random_drawings (1) ←→ (N) drawing_participants
journals (1) ←→ (N) journal_views

referral_relationships (N) ←→ (1) users (as referrer)
referral_relationships (N) ←→ (1) users (as referred)

usdt_wallets (1) ←→ (N) transactions
usdt_wallets (1) ←→ (N) user_certificates
```

## 索引策略

### 主要索引
- **用户表**: email, referral_code, status, role
- **钱包表**: user_id, wallet_address, wallet_type
- **周刊表**: week_number, status, featured, created_by
- **交易表**: user_id, transaction_type, status, created_at
- **日志表**: user_id, created_at, error_level

### 复合索引
- **推荐关系**: (referrer_id, referred_id) UNIQUE
- **参与者**: (drawing_id, user_id) UNIQUE
- **用户证书**: (user_id, certificate_id)

## 数据完整性

### 外键约束
- 所有关联表都有适当的外键约束
- 级联删除策略：删除用户时删除相关数据
- 级联更新策略：更新主键时更新外键

### 检查约束
- 金额字段：非负数
- 状态字段：枚举值验证
- 时间字段：逻辑验证

## 性能优化

### 查询优化
- 使用适当的索引
- 避免N+1查询问题
- 使用分页查询

### 存储优化
- 使用适当的数据类型
- JSON字段用于灵活数据
- 文本字段使用TEXT/LONGTEXT

## 安全考虑

### 数据保护
- 密码哈希存储
- 敏感信息加密
- 访问日志记录

### 权限控制
- 基于角色的访问控制
- API级别的权限验证
- 数据级别的权限检查

## 迁移策略

### 版本控制
- 使用Flyway进行数据库版本控制
- 每个版本都有对应的迁移脚本
- 支持向前和向后兼容

### 数据备份
- 定期数据库备份
- 事务日志备份
- 灾难恢复计划

## 监控和维护

### 性能监控
- 慢查询监控
- 连接池监控
- 资源使用监控

### 数据维护
- 定期清理过期日志
- 数据归档策略
- 统计信息更新
