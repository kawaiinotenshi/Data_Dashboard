# 物流仓储大数据展示 - Vue 3版本

## 项目介绍
这是一个基于Vue 3 + Vite + ECharts的物流仓储大数据可视化展示面板。

## 技术栈
- Vue 3 (Composition API)
- Vite
- ECharts 5
- jQuery (兼容原项目)

## 安装依赖
```bash
cd FrontEnd
npm install
```

## 启动开发服务器
```bash
npm run dev
```

## 构建生产版本
```bash
npm run build
```

## 项目结构
```
FrontEnd/
├── public/          # 静态资源
├── src/
│   ├── components/  # Vue组件
│   │   ├── LeftPanel.vue
│   │   ├── CenterPanel.vue
│   │   └── RightPanel.vue
│   ├── App.vue      # 主应用组件
│   ├── main.js      # 入口文件
│   └── style.css    # 全局样式
├── index.html       # HTML入口
├── package.json     # 项目配置
├── vite.config.js   # Vite配置
└── project.log      # 项目日志
```

## 功能模块
1. **头部** - 实时时间显示
2. **左侧面板** - 基本信息展示、企业库存占比饼图、BBC清关柱状图
3. **中间面板** - 全国仓库地图、入库出库趋势、季度数据统计
4. **右侧面板** - 仓库/进关信息切换、BBC清关滚动列表、仓库利用比仪表盘
