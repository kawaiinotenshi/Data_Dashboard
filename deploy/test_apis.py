# -*- coding: utf-8 -*-
import requests
import json

base_url = "http://localhost:8080/api"

print("=" * 50)
print("开始测试所有API接口")
print("=" * 50)

apis = [
    {"name": "订单列表", "method": "GET", "url": "/order/list", "expected_fields": ["id", "orderNo", "status", "amount"]},
    {"name": "订单统计", "method": "GET", "url": "/order/statistics", "expected_fields": ["totalOrders", "pendingOrders", "completedOrders", "totalAmount"]},
    {"name": "仓库列表", "method": "GET", "url": "/warehouse/list", "expected_fields": ["id", "name", "location", "capacity"]},
    {"name": "仓库统计", "method": "GET", "url": "/warehouse/statistics", "expected_fields": ["totalWarehouses", "totalCapacity", "usedCapacity", "availableCapacity"]},
    {"name": "客户列表", "method": "GET", "url": "/customer/list", "expected_fields": ["id", "name", "phone", "email"]},
    {"name": "供应商列表", "method": "GET", "url": "/supplier/list", "expected_fields": ["id", "name", "contact", "phone"]},
    {"name": "运输列表", "method": "GET", "url": "/transport/list", "expected_fields": ["id", "vehicleNo", "driver", "status"]},
    {"name": "运输统计", "method": "GET", "url": "/transport/statistics", "expected_fields": ["totalTransports", "inProgress", "completed"]},
    {"name": "仪表盘数据", "method": "GET", "url": "/dashboard/data", "expected_fields": ["orderCount", "warehouseCount", "transportCount"]}
]

success_count = 0
fail_count = 0

for api in apis:
    print(f"\n测试: {api['name']}")
    print(f"URL: {base_url}{api['url']}")
    
    try:
        response = requests.get(f"{base_url}{api['url']}", timeout=10)
        result = response.json()
        
        if result.get('code') == 200 or result.get('success') == True:
            print("✓ 状态: 成功")
            
            data = result.get('data')
            if data:
                if isinstance(data, list):
                    print(f"✓ 数据类型: 数组，条数: {len(data)}")
                    if len(data) > 0:
                        first_item = data[0]
                        missing_fields = [f for f in api['expected_fields'] if f not in first_item]
                        if not missing_fields:
                            print("✓ 字段验证: 通过")
                        else:
                            print(f"✗ 字段验证: 缺少字段 {', '.join(missing_fields)}")
                else:
                    print("✓ 数据类型: 对象")
                    missing_fields = [f for f in api['expected_fields'] if f not in data]
                    if not missing_fields:
                        print("✓ 字段验证: 通过")
                    else:
                        print(f"✗ 字段验证: 缺少字段 {', '.join(missing_fields)}")
                
                print(f"数据预览: {json.dumps(data, ensure_ascii=False, indent=2)[:500]}")
            else:
                print("⚠ 数据为空")
            
            success_count += 1
        else:
            print(f"✗ 状态: 失败 - {result.get('message', '未知错误')}")
            fail_count += 1
            
    except Exception as e:
        print(f"✗ 异常: {str(e)}")
        fail_count += 1

print("\n" + "=" * 50)
print("测试完成")
print(f"成功: {success_count} / {len(apis)}")
print(f"失败: {fail_count} / {len(apis)}")
print("=" * 50)
