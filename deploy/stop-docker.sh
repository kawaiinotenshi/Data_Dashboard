#!/bin/bash

echo "========================================"
echo "物流仓储大数据展示系统 - Docker停止"
echo "========================================"
echo ""

cd "$(dirname "$0")"

echo "[1/2] 停止所有服务..."
docker-compose down
if [ $? -ne 0 ]; then
    echo "警告: 停止服务时出现错误"
    echo ""
fi
echo ""

echo "[2/2] 清理未使用的资源..."
echo "可选: 是否删除数据卷（会删除所有数据库数据）？"
read -p "输入 y 删除数据卷，其他键保留数据: " delete_volumes
if [ "$delete_volumes" = "y" ] || [ "$delete_volumes" = "Y" ]; then
    echo "正在删除数据卷..."
    docker-compose down -v
    echo "数据卷已删除"
else
    echo "数据卷已保留"
fi
echo ""

echo "========================================"
echo "停止完成！"
echo "========================================"
echo ""
echo "如需重新启动，请运行: ./start-docker.sh"
echo ""
