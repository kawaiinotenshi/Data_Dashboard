# 更新日志

## [2025-12-30]

### 项目结构优化与代码冗余减少
- 前端API结构优化
  - 整合API文件，将customer.js、supplier.js、order.js、warehouse.js、transport.js、inventory.js合并到index.js
  - 更新所有store文件使用统一的API接口（api.customer.list、api.supplier.list等）
  - 更新所有组件使用统一的API接口
  - 删除冗余的API文件，减少代码重复

- 前端Store优化
  - 创建通用的实体Store工厂（useEntityStore.js）
  - 实现统一的CRUD操作、批量操作、状态管理
  - 重构customer.js、supplier.js、order.js、warehouse.js、transport.js、inventory.js使用工厂模式
  - 减少代码重复，提高可维护性

- 后端Service层优化
  - 创建BaseEntityServiceImpl基类，提供通用的CRUD操作和实体转换方法
  - 重构CustomerServiceImpl、SupplierServiceImpl、WarehouseServiceImpl、OrderServiceImpl、TransportServiceImpl、InventoryServiceImpl
  - 减少重复代码，统一异常处理和事务管理

- 安全优化
  - 优化SqlInjectionUtils.java，删除130+重复的Pattern.compile条目
  - 减少文件大小约65%，保持安全防护效果

- 文档清理
  - 删除冗余文档文件（MCP_MYSQL_FIX.md、环境配置参考.md）
  - 保留核心文档（CHANGELOG.md、DEPLOY.md、DEPLOY_LOCAL.md、DOCKER_DB.md、LAUNCHER_GUIDE.md、PROJECT_MANUAL.md、MYBATIS_PLUS_REFACTOR.md）

### 项目测试
- 全面测试所有API端点功能
- 验证前端与后端集成
- 确认数据流正确性

## [2025-12-29]

### SQL防注入实现
- 实现SQL注入检测和防护工具类（SqlInjectionUtils.java）
  - 检测SQL注入关键词（SELECT, INSERT, UPDATE, DELETE, DROP等）
  - 检测SQL注释符号（--, #, /* */）
  - 检测SQL逻辑操作符（OR, AND, UNION）
  - 提供输入清理和验证方法
- 实现XSS过滤器（XssFilter.java）
  - 过滤HTML标签和JavaScript代码
  - 转义特殊字符（<, >, ', ", (, )）
  - 清除eval和javascript:等危险代码
  - 实现请求包装器（XssRequestWrapper）
- 添加参数验证到VO类
  - 使用@NotBlank, @NotNull, @Size等注解
  - 实现输入长度和格式验证
  - 添加自定义错误消息

### 编译错误修复
- 修复MyBatis-Plus代码生成器API更新问题
  - 修正CodeGenerator.java中的enableSwagger()方法调用
  - 修正EnhancedCodeGenerator.java中的enableSwagger()方法调用
  - 移除过时的boolean参数

### 依赖更新
- 添加Jakarta Servlet API依赖
- 添加Jakarta Validation API依赖
- 更新Spring Boot版本兼容性

### Git版本控制
- 解决GitHub推送被拒绝问题（敏感信息暴露）
- 添加日志文件到.gitignore
- 清理包含敏感信息的提交历史
- 重新提交安全增强代码

## [2025-12-28]

### 项目启动器开发
- 开发可视化项目启动器（launcher.py）
- 实现环境验证功能（Java、Node.js、MySQL、Docker、Maven、Nginx）
- 实现配置管理功能（路径、端口、数据库配置）
- 实现服务控制功能（启动/停止/重启服务）
- 实现日志监控功能（实时日志显示和过滤）
- 添加端口冲突检测和自动进程清理
- 添加启动器使用指南文档（LAUNCHER_GUIDE.md）

### MCP MySQL配置修复
- 修复MCP MySQL连接问题（spawn uvx ENOENT错误）
- 安装uv工具包（python -m pip install uv）
- 配置正确的MCP启动命令（python -m uv tool run --from mysql-mcp-server mysql_mcp_server）
- 验证MCP MySQL连接成功（端口3307，数据库logistics_db）
- 添加MCP配置修复文档（MCP_MYSQL_FIX.md）

### 数据库优化
- 修复数据库schema问题（添加is_deleted和version字段）
- 创建数据库修复脚本（fix_schema.sql）
- 创建数据库初始化脚本（init.sql）
- 验证数据库表结构和数据完整性

### MyBatis-Plus架构最佳实践
- 实现统一异常处理机制（GlobalExceptionHandler）
- 集成SLF4J日志系统，配置日志级别和输出格式
- 添加性能优化配置（SQL性能分析、分页优化）
- 实现代码生成器最佳实践（CodeGenerator）
- 添加代码生成器使用指南文档（CODE_GENERATOR_GUIDE.md）

### 架构改进
- **GlobalExceptionHandler**: 统一处理所有异常类型
  - 业务异常（BusinessException）
  - 数据库异常（DuplicateKeyException, DataIntegrityViolationException）
  - 参数验证异常（MethodArgumentNotValidException）
  - 系统异常（Exception）
- **日志配置**: 分层日志管理
  - 开发环境：DEBUG级别，控制台输出
  - 生产环境：INFO级别，文件输出
  - SQL日志：MyBatis-Plus SQL性能分析
- **性能优化**:
  - MyBatis-Plus性能分析插件
  - 分页插件优化
  - 批量操作优化

### 代码生成器
- **CodeGenerator**: 自动化代码生成工具
  - Entity实体类生成
  - Mapper接口生成
  - Service接口和实现类生成
  - Controller控制器生成
  - 支持自定义模板和配置
- **生成器配置**: generator.properties
  - 数据库连接配置
  - 包名和模块名配置
  - 表名前缀和后缀配置
  - 作者和日期配置

### 项目维护
- 验证项目运行状态（后端和前端服务正常）
- 清理冗余文件（删除临时日志文件）
- 验证项目结构完整性
- 准备Git版本控制提交

### 文档
- 添加MyBatis-Plus重构文档（MYBATIS_PLUS_REFACTOR.md）
- 添加代码生成器使用指南（CODE_GENERATOR_GUIDE.md）
- 添加项目启动器使用指南（LAUNCHER_GUIDE.md）
- 添加MCP MySQL配置修复文档（MCP_MYSQL_FIX.md）
- 更新项目变更日志（CHANGELOG.md）

## [2025-12-26]

### 新增功能
- 实现完整的Playwright自动化测试框架
- 添加端到端测试套件，覆盖主要功能模块
- 实现API测试验证后端接口
- 添加截图测试功能用于视觉回归测试
- 支持跨浏览器和移动端设备测试

### 测试覆盖
- **test_dashboard.py**: 仪表盘页面加载和面板显示测试
- **test_api.py**: API数据展示验证测试
- **test_screenshot.py**: 页面截图测试
- **test_e2e.py**: 端到端用户流程测试

### 技术改进
- 配置pytest测试框架和Playwright集成
- 添加测试配置文件（pytest.ini, conftest.py）
- 实现测试夹具和页面对象模式
- 修复前端端口配置（从5173改为3000）
- 优化元素定位策略，提高测试稳定性

### 数据集成验证
- 验证前端与后端API的完整集成
- 确认数据从MySQL数据库正确流向前端展示
- 验证所有API端点返回有效数据：
  - `/api/warehouse/list` - 仓库列表数据
  - `/api/inventory/list` - 库存占比数据
  - `/api/order/list` - 订单数据

### 项目优化
- 更新.gitignore排除不必要的文件：
  - SSH密钥文件（*.pem, *.pub）
  - 测试缓存目录（tests/__pycache__/）
  - 截图目录（screenshots/）
  - PowerShell配置脚本（fix-curl-alias.ps1）

### 文档
- 添加完整的测试文档（tests/README.md）
- 提供测试执行指南和故障排除说明
- 记录所有错误修复和解决方案

### 已知问题
- MySQL命令行工具未在系统PATH中配置
- 需要确保前端和后端服务同时运行才能执行完整测试

## [2025-12-XX]

### 初始版本
- 实现物流仓储大数据展示仪表盘
- 前端使用Vue.js + ECharts
- 后端使用Spring Boot + MyBatis
- 数据库使用MySQL
- 实现Docker容器化部署