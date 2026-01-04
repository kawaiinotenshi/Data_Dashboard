# 更新日志

## [2026-01-04]

### Docker容器化部署实现
- 实现完整的Docker容器化部署方案
  - 创建docker-compose.yml配置文件，定义MySQL、Backend、Frontend三个服务
  - 配置服务依赖关系和健康检查机制
  - 实现数据持久化（MySQL数据卷）
  - 配置服务间网络通信（logistics-network）

- 后端容器化（backend/Dockerfile）
  - 基于eclipse-temurin:11-jre-jammy镜像
  - 多阶段构建：Maven构建阶段 + JRE运行阶段
  - 配置生产环境启动参数
  - 实现非root用户运行安全配置

- 前端容器化（FrontEnd/Dockerfile）
  - 基于node:18-alpine镜像构建
  - 基于nginx:alpine镜像运行
  - 配置nginx反向代理，解决前后端通信问题
  - 实现静态资源优化配置

- Docker网络和代理配置
  - 实现nginx反向代理配置（FrontEnd/nginx.conf）
  - 配置前端环境变量（.env.production），使用相对API路径
  - 解决容器间网络隔离问题
  - 配置跨域支持和请求转发

### 密码统一配置
- 统一所有服务密码为"root"
  - MySQL root用户密码：root
  - MySQL logistics用户密码：root
  - 更新docker-compose.yml环境变量配置
  - 更新后端application-prod.yml数据库连接配置

### 项目结构优化
- 实现本地版和Docker版项目结构分离
  - 创建deploy目录，存放Docker相关配置
  - 保持原有backend、FrontEnd目录结构不变
  - 创建启动脚本：
    - start-docker.bat/sh - 启动Docker容器
    - stop-docker.bat/sh - 停止Docker容器
  - 添加Docker部署文档（DOCKER_DEPLOY.md）

### 数据库Schema修复
- 修复inventory_ratio表结构问题
  - 添加缺失的BaseEntity字段：updated_time, is_deleted, version
  - 更新INSERT语句，匹配新的表结构
  - 解决"Column count doesn't match value count"错误
  - 确保所有实体类与数据库表结构一致

### API测试验证
- 全面测试所有API端点
  - ✅ /api/inventory/list - 库存占比数据（已修复）
  - ✅ /api/warehouse/list - 仓库列表数据
  - ✅ /api/transport/list - 运输数据
  - ❌ /api/dashboard/overview - 仪表盘概览（端点不存在）
  - ❌ /api/delivery/list - 配送列表（端点不存在）
  - ❌ /api/inbound/list - 入库列表（端点不存在）
  - ❌ /api/outbound/list - 出库列表（端点不存在）

- 创建API测试脚本
  - test_apis.py - Python API测试脚本
  - test_apis.ps1 - PowerShell API测试脚本
  - test_all_apis.ps1 - 完整API测试套件

### 前端功能验证
- 验证前端页面完整功能
  - ✅ 物流仓储大数据展示页面正常加载
  - ✅ 所有数据面板正确显示
  - ✅ 图表组件正常渲染（ECharts）
  - ✅ 无控制台错误或警告
  - ✅ API数据正确获取和展示

- 验证数据完整性
  - ✅ 仓库信息：5个仓库（大连仓、青岛仓、宁波仓、上海仓、广州仓）
  - ✅ 库存占比：6个企业（顺丰物流、京东物流、德邦物流、中通快递、圆通速递、其他）
  - ✅ 运输数据：5种车辆类型（货车、卡车、厢式车、冷藏车、特种车）
  - ✅ 进关信息：8个国家数据（美国、日本、沙特、韩国、俄罗斯、德国、法国、新加坡）
  - ✅ BBC清关数据：10种商品分类

### Docker部署文档
- 创建完整的Docker部署文档（deploy/DOCKER_DEPLOY.md）
  - 环境要求说明
  - 快速启动指南
  - 服务访问地址
  - 常见问题排查
  - 数据持久化说明

### 数据库验证工具
- 创建Python数据库验证脚本
  - check_database_structure.py - 检查数据库表结构
  - check_data.py - 验证数据完整性
  - check_user.py - 验证用户权限配置
  - check_bind.py - 检查端口绑定状态

## [2026-01-04]

### 供应链可视化面板实现
- 实现完整的供应链可视化面板（SupplyChainDashboard.vue）
  - 创建响应式布局，支持1920x1080设计尺寸
  - 实现实时时间显示功能
  - 集成ECharts图表组件：
    - 饼图：供应商分布、客户分布、运输方式占比
    - 柱状图：仓库库存、订单状态统计
    - 折线图：订单趋势、库存变化趋势
    - 雷达图：供应链综合评估
    - 关系图：供应链网络拓扑
  - 实现图表自适应调整，使用ResizeObserver监听窗口变化
  - 添加深色科技风格UI设计

### 后台管理系统实现
- 用户管理模块（UserManagement.vue）
  - 实现用户列表展示，支持分页和搜索
  - 添加用户创建和编辑功能（模态对话框）
  - 实现用户删除功能，带确认提示
  - 集成用户状态管理（Pinia store）

- 员工管理模块（EmployeeManagement.vue）
  - 实现员工列表展示，支持分页和搜索
  - 添加员工创建和编辑功能
  - 实现员工删除功能
  - 集成员工状态管理（Pinia store）

- 部门管理模块（DepartmentManagement.vue）
  - 实现部门列表展示，支持分页和搜索
  - 添加部门创建和编辑功能
  - 实现部门删除功能
  - 集成部门状态管理（Pinia store）

### 路由和API配置
- 更新路由配置（router/index.js）
  - 添加供应链可视化面板路由
  - 添加后台管理路由（嵌套路由结构）
  - 配置侧边栏导航组件（AdminSidebar.vue）
  - 修复路由导入错误，移除不存在的Dashboard.vue

- 扩展API配置（api/index.js）
  - 添加用户管理API（用户列表、创建、更新、删除）
  - 添加员工管理API（员工列表、创建、更新、删除）
  - 添加部门管理API（部门列表、创建、更新、删除）
  - 统一API接口命名规范

### 状态管理
- 创建用户状态管理（stores/user.js）
  - 实现用户列表状态
  - 实现用户CRUD操作
  - 集成API调用

- 创建员工状态管理（stores/employee.js）
  - 实现员工列表状态
  - 实现员工CRUD操作
  - 集成API调用

### 项目测试和验证
- 验证所有应用模块运行状态
- 测试路由导航功能
- 验证API接口配置
- 确认组件渲染正确

### 代码质量优化
- 修复ESLint配置问题
  - 更新eslint.config.js，添加缺失的全局变量（localStorage, sessionStorage, alert, confirm, prompt, URL）
  - 配置ignores规则，排除node_modules、dist等目录
  - 移除过时的.eslintignore文件

- 修复代码质量问题
  - 移除未使用的变量（clientHeight, scaleY, ref）
  - 移除未使用的导入（flushPromises）
  - 修复所有ESLint错误，确保代码符合规范

### 依赖管理
- 安装项目依赖（npm install）
- 验证node_modules目录完整性
- 确认所有依赖包正确安装

## [2025-12-30]

### Python桌面应用主题切换功能实现
- 实现完整的主题切换系统
  - 创建ThemeSwitch组件（theme_switch.py），提供简单的主题切换按钮
  - 实现明亮/暗黑主题切换功能，支持实时切换
  - 添加主题切换按钮到数据管理界面，位于刷新按钮旁边
  - 使用☀/🌙图标表示主题状态

- 主题管理器优化
  - 完善ThemeManager类，支持主题切换和状态持久化
  - 实现主题配置的动态加载和应用
  - 添加用户主题偏好保存功能（user_settings.json）

- 数据管理界面主题适配
  - 实现update_widgets_theme()方法，支持所有UI组件的主题切换
  - 修复主题切换重复调用问题，确保主题正确切换
  - 优化Treeview样式配置，使用ttk.Style统一管理
  - 确保暗黑模式下所有文本为白色，背景为黑色/灰色

- 字段翻译系统完善
  - 更新field_translations.py，添加所有数据库表的完整中文翻译
  - 修复表头显示不一致问题（全中文/半中文/英文混合）
  - 添加business_quarterly、customs_clearance、in_out_record、warehouse_distribution等表的翻译
  - 确保所有字段都有对应的中文翻译

- 代码质量优化
  - 修复代码冗余，优化模块结构
  - 统一日志配置，使用logger_config.py管理所有日志
  - 改进错误处理和异常捕获
  - 添加详细的日志记录，便于问题追踪

- 数据库验证工具
  - 创建check_db.py工具，用于检查数据库表结构和字段
  - 验证所有10个数据库表的完整性
  - 确认字段类型和约束正确性

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