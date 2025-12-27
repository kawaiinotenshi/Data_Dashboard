# MyBatis-Plus架构规范化需求文档与实现方案

## 一、需求分析

### 1.1 项目背景
当前物流仓储大数据展示系统后端采用MyBatis-Plus框架，但架构不够规范，存在以下问题：
- 缺少统一的Entity基类，公共字段重复定义
- VO层未实现，直接暴露Entity给前端
- 缺少统一异常处理机制
- 缺少自动填充功能
- 缺少分页、乐观锁等插件配置
- 缺少事务管理注解
- 缺少批量操作实现
- 缺少复杂查询的XML映射文件

### 1.2 改进目标
实现MyBatis-Plus最佳实践，提升代码质量和可维护性：
1. 统一使用MyBatis-Plus：尽量使用MyBatis-Plus提供的CRUD方法，减少自定义SQL
2. 复杂查询使用XML：对于复杂的多表关联查询，使用XML映射文件
3. 合理使用VO：区分Entity、DTO、VO，避免直接暴露Entity
4. 事务管理：在Service层使用@Transactional注解
5. 异常处理：统一异常处理机制
6. 日志记录：使用MyBatis-Plus的日志功能
7. 性能优化：合理使用索引、批量操作、分页查询
8. 代码生成：可以使用MyBatis-Plus代码生成器快速生成基础代码

### 1.3 技术栈
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3.1
- MySQL 8.0
- Lombok
- Java 8

## 二、架构设计

### 2.1 分层架构
```
backend/
└── src/
    └── main/
        ├── java/com/logistics/
        │   ├── config/                      # 配置层
        │   │   ├── MyBatisPlusConfig.java   # MyBatis-Plus配置
        │   │   └── GlobalExceptionHandler.java # 全局异常处理
        │   ├── entity/                      # 实体层
        │   │   ├── base/                   # 基础实体类
        │   │   │   └── BaseEntity.java     # 公共字段
        │   │   ├── Warehouse.java
        │   │   ├── Inventory.java
        │   │   ├── Order.java
        │   │   ├── Customer.java
        │   │   ├── Supplier.java
        │   │   └── Transport.java
        │   ├── vo/                         # 视图对象层
        │   │   ├── request/                # 请求VO
        │   │   └── response/               # 响应VO
        │   ├── mapper/                     # 数据访问层
        │   ├── service/                    # 服务层接口
        │   ├── service/impl/               # 服务层实现
        │   ├── controller/                 # 控制器层
        │   ├── common/                     # 通用模块
        │   │   ├── Result.java            # 统一响应结果
        │   │   ├── PageResult.java        # 分页结果
        │   │   └── exception/             # 异常类
        │   └── handler/                   # MyBatis处理器
        │       └── MetaObjectHandler.java # 自动填充处理器
        └── resources/
            ├── mapper/                    # MyBatis XML映射文件
            ├── application.yml
            ├── application-dev.yml
            └── application-prod.yml
```

### 2.2 核心组件

#### 2.2.1 BaseEntity
提供公共字段：id, createdTime, updatedTime, isDeleted, version

#### 2.2.2 MyBatisPlusConfig
配置分页插件、乐观锁插件、防止全表更新删除插件

#### 2.2.3 MetaObjectHandler
自动填充createdTime和updatedTime

#### 2.2.4 GlobalExceptionHandler
统一异常处理，返回标准错误响应

#### 2.2.5 VO层
- RequestVO：接收前端请求参数
- ResponseVO：返回给前端的数据

## 三、具体实现

### 3.1 Entity层实现

#### 3.1.1 创建BaseEntity
```java
package com.logistics.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    @Version
    private Integer version;
}
```

#### 3.1.2 修改现有Entity继承BaseEntity
- Warehouse.java
- Inventory.java
- Order.java
- Customer.java
- Supplier.java
- Transport.java

### 3.2 Mapper层实现

#### 3.2.1 为现有Mapper添加自定义方法
- selectWarehouseStatistics：仓库统计查询
- selectInventoryStatistics：库存统计查询
- selectOrderStatistics：订单统计查询

#### 3.2.2 创建XML映射文件
- WarehouseMapper.xml
- InventoryMapper.xml
- OrderMapper.xml

### 3.3 Service层实现

#### 3.3.1 添加@Transactional注解
在所有写操作方法上添加@Transactional注解

#### 3.3.2 实现批量操作
- batchInsert：批量插入
- batchUpdate：批量更新
- batchDelete：批量删除

#### 3.3.3 实现分页查询
使用MyBatis-Plus的分页插件实现分页查询

### 3.4 VO层实现

#### 3.4.1 创建RequestVO
- WarehouseQueryVO：仓库查询VO
- WarehouseSaveVO：仓库保存VO
- WarehouseUpdateVO：仓库更新VO

#### 3.4.2 创建ResponseVO
- WarehouseListVO：仓库列表VO
- WarehouseDetailVO：仓库详情VO
- WarehouseStatisticsVO：仓库统计VO

### 3.5 Controller层实现

#### 3.5.1 修改Controller使用VO
将Entity替换为VO，避免直接暴露Entity

#### 3.5.2 添加参数校验
使用@Validated和@Valid注解进行参数校验

### 3.6 配置层实现

#### 3.6.1 创建MyBatisPlusConfig
```java
package com.logistics.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}
```

#### 3.6.2 创建MetaObjectHandler
```java
package com.logistics.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

#### 3.6.3 创建GlobalExceptionHandler
```java
package com.logistics.config;

import com.logistics.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统异常：" + e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return Result.error(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return Result.error(message);
    }
}
```

### 3.7 Common层实现

#### 3.7.1 创建PageResult
```java
package com.logistics.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    
    private Long total;
    
    private List<T> records;
    
    private Long current;
    
    private Long size;
    
    private Long pages;
    
    public static <T> PageResult<T> of(Long total, List<T> records, Long current, Long size) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        pageResult.setCurrent(current);
        pageResult.setSize(size);
        pageResult.setPages((total + size - 1) / size);
        return pageResult;
    }
}
```

### 3.8 配置文件更新

#### 3.8.1 更新application.yml
```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: isDeleted
      logic-delete-value: 1
      logic-not-delete-value: 0
    banner: false
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.logistics.entity
```

## 四、实施步骤

### 4.1 第一阶段：基础架构搭建
1. 创建BaseEntity基类
2. 创建MyBatisPlusConfig配置类
3. 创建MetaObjectHandler自动填充处理器
4. 创建GlobalExceptionHandler全局异常处理器
5. 创建PageResult分页结果类

### 4.2 第二阶段：Entity层改造
1. 修改所有Entity继承BaseEntity
2. 移除Entity中的重复公共字段
3. 添加必要的注解

### 4.3 第三阶段：VO层创建
1. 创建RequestVO类
2. 创建ResponseVO类
3. 添加参数校验注解

### 4.4 第四阶段：Service层改造
1. 添加@Transactional注解
2. 实现批量操作方法
3. 实现分页查询方法
4. 使用LambdaQueryWrapper简化查询

### 4.5 第五阶段：Controller层改造
1. 修改Controller使用VO
2. 添加参数校验
3. 统一返回格式

### 4.6 第六阶段：Mapper层改造
1. 添加自定义查询方法
2. 创建XML映射文件
3. 实现复杂查询

### 4.7 第七阶段：配置文件更新
1. 更新application.yml
2. 更新application-dev.yml
3. 更新application-prod.yml

### 4.8 第八阶段：测试验证
1. 单元测试
2. 集成测试
3. 性能测试

## 五、测试计划

### 5.1 功能测试
- CRUD操作测试
- 分页查询测试
- 批量操作测试
- 参数校验测试
- 异常处理测试

### 5.2 性能测试
- 查询性能测试
- 批量操作性能测试
- 分页查询性能测试

### 5.3 集成测试
- 前后端集成测试
- 数据库集成测试

## 六、预期效果

### 6.1 代码质量提升
- 减少代码重复
- 提高代码可读性
- 提高代码可维护性

### 6.2 性能提升
- 优化查询性能
- 支持批量操作
- 支持分页查询

### 6.3 开发效率提升
- 统一异常处理
- 自动填充公共字段
- 简化CRUD操作

## 七、注意事项

### 7.1 兼容性
- 保持向后兼容
- 不影响现有功能

### 7.2 数据迁移
- 确保数据完整性
- 备份重要数据

### 7.3 测试覆盖
- 充分测试所有改动
- 确保系统稳定运行

## 八、后续优化

### 8.1 代码生成器
- 集成MyBatis-Plus代码生成器
- 自动生成基础代码

### 8.2 多数据源
- 支持多数据源配置
- 实现读写分离

### 8.3 缓存集成
- 集成Redis缓存
- 优化查询性能
