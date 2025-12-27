# MyBatis-Plus代码生成器使用指南

## 概述

本项目使用MyBatis-Plus代码生成器快速生成基础代码，包括Entity、Mapper、Service、ServiceImpl和XML映射文件。

## 功能特性

1. **自动生成基础代码**：根据数据库表结构自动生成Entity、Mapper、Service、ServiceImpl等基础代码
2. **自定义模板**：使用Freemarker模板引擎，支持自定义代码生成模板
3. **配置灵活**：支持通过配置文件或代码配置生成器参数
4. **批量操作支持**：生成的Service层包含批量创建和批量更新方法
5. **统一规范**：生成的代码遵循项目统一的命名规范和代码风格

## 使用方法

### 方式一：使用基础代码生成器

1. 修改 [CodeGenerator.java](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/java/com/logistics/generator/CodeGenerator.java) 中的配置：
   - 数据库连接信息（URL、用户名、密码）
   - 包名配置
   - 表名配置

2. 运行 `CodeGenerator.main()` 方法生成代码

### 方式二：使用增强版代码生成器（推荐）

1. 修改 [generator.properties](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/generator.properties) 配置文件：
   ```properties
   # 数据库配置
   database.url=jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
   database.username=root
   database.password=root
   
   # 生成器配置
   generator.author=Logistics System
   generator.parentPackage=com.logistics
   generator.tables=warehouse,order,inventory,customer,supplier,transport
   ```

2. 运行 [EnhancedCodeGenerator.java](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/java/com/logistics/generator/EnhancedCodeGenerator.java) 的 `main()` 方法

## 生成的代码结构

```
com.logistics
├── entity/              # 实体类
│   ├── Warehouse.java
│   ├── Order.java
│   └── ...
├── mapper/              # Mapper接口
│   ├── WarehouseMapper.java
│   ├── OrderMapper.java
│   └── ...
├── service/             # Service接口
│   ├── IWarehouseService.java
│   ├── IOrderService.java
│   └── ...
├── service.impl/        # Service实现类
│   ├── WarehouseServiceImpl.java
│   ├── OrderServiceImpl.java
│   └── ...
└── mapper.xml/          # XML映射文件
    ├── WarehouseMapper.xml
    ├── OrderMapper.xml
    └── ...
```

## 生成的代码功能

### Entity实体类

- 使用Lombok注解简化代码
- 包含MyBatis-Plus注解（@TableName、@TableId、@TableField等）
- 支持逻辑删除、乐观锁
- 自动填充创建时间和更新时间

### Mapper接口

- 继承BaseMapper，提供基础CRUD方法
- 自定义查询方法：selectByIdWithDetails、selectByCondition、countByCondition

### Service接口

- 继承IService，提供基础服务方法
- 自定义业务方法：getByIdWithDetails、getByCondition、pageByCondition
- 批量操作方法：batchCreate、batchUpdate

### ServiceImpl实现类

- 实现Service接口
- 使用@Transactional注解进行事务管理
- 批量操作使用saveBatch和updateBatchById，批量大小为500

### XML映射文件

- 基础ResultMap和ColumnList
- 自定义SQL查询
- 支持动态SQL条件

## 自定义模板

项目提供了自定义的Freemarker模板，位于 `src/main/resources/templates/` 目录：

- [entity.java](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/templates/entity.java) - 实体类模板
- [mapper.java](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/templates/mapper.java) - Mapper接口模板
- [mapper.xml](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/templates/mapper.xml) - XML映射文件模板
- [service.java](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/templates/service.java) - Service接口模板
- [serviceImpl.java](file:///c:/Users/15071/Documents/大数据面板/backend/src/main/resources/templates/serviceImpl.java) - Service实现类模板

可以根据项目需求修改这些模板文件。

## 注意事项

1. **数据库表设计**：确保数据库表包含以下字段以支持完整功能：
   - `id` - 主键（自增）
   - `create_time` - 创建时间
   - `update_time` - 更新时间
   - `is_deleted` - 逻辑删除标记（可选）
   - `version` - 乐观锁版本号（可选）

2. **代码覆盖**：代码生成器会覆盖已存在的同名文件，请确保在生成前备份重要代码

3. **自定义代码**：生成的代码为基础代码，需要根据业务需求添加自定义逻辑

4. **依赖配置**：确保pom.xml中已添加以下依赖：
   - mybatis-plus-generator
   - freemarker

## 最佳实践

1. **定期使用**：在新增表或修改表结构后，使用代码生成器更新基础代码
2. **版本控制**：将生成的代码纳入版本控制，便于团队协作
3. **代码审查**：生成后进行代码审查，确保符合项目规范
4. **自定义扩展**：在生成的基础上添加业务逻辑，避免直接修改生成的基础代码

## 示例

生成Warehouse相关代码：

```java
// 运行EnhancedCodeGenerator.main()
// 生成器会自动读取generator.properties配置
// 生成以下文件：
// - entity/Warehouse.java
// - mapper/WarehouseMapper.java
// - mapper.xml/WarehouseMapper.xml
// - service/IWarehouseService.java
// - service.impl/WarehouseServiceImpl.java
```

## 相关文档

- [MyBatis-Plus官方文档](https://baomidou.com/)
- [代码生成器配置](https://baomidou.com/pages/779a6e/)
- [Freemarker模板语法](https://freemarker.apache.org/docs/dgui.html)
