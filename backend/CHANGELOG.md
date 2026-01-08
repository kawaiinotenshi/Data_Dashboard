# 物流大数据仪表盘项目变更日志

## [1.0.0] - 2026-01-07

### 新增功能
- **员工管理模块**：
  - 员工实体类（Employee）及相关VO/RequestVO
  - 员工Mapper接口及XML配置
  - 员工服务层实现（CRUD、批量操作、高级查询）
  - 员工RESTful API控制器
  - 员工服务单元测试

- **部门管理模块**：
  - 部门实体类（Department）及相关VO/RequestVO
  - 部门Mapper接口及XML配置
  - 部门服务层实现（CRUD、批量操作）
  - 部门与员工关联查询功能

### 修复问题
- 修复了application.yml中YAML解析错误（key-prefix配置）
- 解决了MyBatis配置类Bean冲突问题
- 解决了SecurityConfig循环依赖问题
- 修复了Mapper XML中的特殊字符转义问题
- 修复了EmployeeMapper参数映射问题

### 优化改进
- 创建WebMvcConfig.java分离WebMvc配置，提高代码模块化
- 重命名模板文件避免MyBatis扫描冲突
- 统一了VO/RequestVO命名规范
- 遵循项目现有架构模式，确保代码风格一致性

### 技术实现
- 使用MyBatis-Plus ORM框架实现数据访问
- 采用VO/RequestVO模式进行数据传输
- 实现RESTful API设计风格
- 使用Lombok简化代码
- 实现@Transactional事务管理
- 支持批量操作与分页查询

### 集成测试
- 成功将新模块集成到现有项目中
- 所有Mapper文件（包括新添加的）均能被正确解析
- 上下文初始化完成，应用能够正常启动（端口冲突除外）