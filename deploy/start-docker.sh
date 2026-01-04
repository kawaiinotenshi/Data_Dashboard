#!/bin/bash

echo "========================================"
echo "物流仓储大数据展示系统 - Docker快速启动"
echo "========================================"
echo ""

cd "$(dirname "$0")"

echo "[1/5] 检查Docker是否运行..."
if ! docker version > /dev/null 2>&1; then
    echo "错误: Docker未运行或未安装"
    echo "请先启动Docker"
    exit 1
fi
echo "Docker运行正常"
echo ""

echo "[2/5] 停止并删除旧容器..."
docker-compose down
echo ""

echo "[3/5] 构建Docker镜像..."
echo "这可能需要几分钟时间，请耐心等待..."
docker-compose build
if [ $? -ne 0 ]; then
    echo "错误: 镜像构建失败"
    exit 1
fi
echo "镜像构建完成"
echo ""

echo "[4/5] 启动所有服务..."
docker-compose up -d
if [ $? -ne 0 ]; then
    echo "错误: 服务启动失败"
    exit 1
fi
echo ""

echo "[5/5] 等待服务启动..."
echo "正在等待MySQL数据库启动..."
sleep 10

echo "正在等待后端服务启动..."
sleep 20

echo "正在等待前端服务启动..."
sleep 10

echo ""
echo "========================================"
echo "启动完成！"
echo "========================================"
echo ""
echo "访问地址："
echo "  前端应用: http://localhost"
echo "  后端API: http://localhost:8080/api"
echo "  MySQL数据库: localhost:3306"
echo ""
echo "默认数据库账号："
echo "  用户名: root"
echo "  密码: root"
echo ""
echo "常用命令："
echo "  查看日志: docker-compose logs -f"
echo "  停止服务: docker-compose down"
echo "  重启服务: docker-compose restart"
echo ""
echo "详细文档请查看: DOCKER_DEPLOY.md"
echo "========================================"
echo ""
