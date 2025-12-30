"""
字段翻译配置
将数据库字段名翻译成中文，方便管理人员查看
"""

# 数据表字段翻译映射
FIELD_TRANSLATIONS = {
    'orders': {
        'table_name': '订单',
        'fields': {
            'id': '订单ID',
            'order_number': '订单编号',
            'customer_name': '客户名称',
            'product_name': '产品名称',
            'quantity': '数量',
            'price': '价格',
            'status': '状态',
            'order_date': '订单日期',
            'delivery_date': '配送日期',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'inventory': {
        'table_name': '库存',
        'fields': {
            'id': '库存ID',
            'product_name': '产品名称',
            'quantity': '数量',
            'warehouse_id': '仓库ID',
            'location': '位置',
            'status': '状态',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'inventory_ratio': {
        'table_name': '库存占比',
        'fields': {
            'id': 'ID',
            'enterprise_name': '企业名称',
            'ratio': '占比',
            'month': '月份',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'warehouses': {
        'table_name': '仓库',
        'fields': {
            'id': '仓库ID',
            'name': '仓库名称',
            'location': '位置',
            'area': '面积',
            'utilization_rate': '利用率',
            'capacity': '容量',
            'throughput': '吞吐量',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'transport': {
        'table_name': '运输',
        'fields': {
            'id': '运输ID',
            'order_id': '订单ID',
            'vehicle_number': '车辆编号',
            'driver_name': '司机姓名',
            'status': '状态',
            'departure_time': '出发时间',
            'arrival_time': '到达时间',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'suppliers': {
        'table_name': '供应商',
        'fields': {
            'id': '供应商ID',
            'name': '名称',
            'contact': '联系方式',
            'address': '地址',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'customers': {
        'table_name': '客户',
        'fields': {
            'id': '客户ID',
            'name': '名称',
            'phone': '电话',
            'email': '邮箱',
            'address': '地址',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'warehouse_utilization': {
        'table_name': '仓库利用率',
        'fields': {
            'id': 'ID',
            'warehouse_name': '仓库名称',
            'utilization_rate': '利用率',
            'month': '月份',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'transport_efficiency': {
        'table_name': '运输效率',
        'fields': {
            'id': 'ID',
            'vehicle_number': '车辆编号',
            'efficiency': '效率',
            'month': '月份',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'order_statistics': {
        'table_name': '订单统计',
        'fields': {
            'id': 'ID',
            'month': '月份',
            'total_orders': '总订单数',
            'completed_orders': '完成订单数',
            'pending_orders': '待处理订单数',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    }
}

# 通用字段翻译
COMMON_FIELD_TRANSLATIONS = {
    'id': 'ID',
    'name': '名称',
    'created_time': '创建时间',
    'updated_time': '更新时间',
    'is_deleted': '是否删除',
    'version': '版本',
    'status': '状态'
}

# 状态值翻译
STATUS_TRANSLATIONS = {
    'pending': '待处理',
    'processing': '处理中',
    'completed': '已完成',
    'cancelled': '已取消',
    'delivered': '已配送',
    'shipped': '已发货',
    'in_stock': '有库存',
    'out_of_stock': '缺货',
    'low_stock': '库存不足',
    '0': '否',
    '1': '是'
}

# 获取数据表的中文名称
def get_table_name(table_name):
    """获取数据表的中文显示名称"""
    if table_name in FIELD_TRANSLATIONS:
        return FIELD_TRANSLATIONS[table_name]['table_name']
    return table_name

# 获取字段的中文名称
def get_field_name(table_name, field_name):
    """获取字段的中文显示名称"""
    if table_name in FIELD_TRANSLATIONS and field_name in FIELD_TRANSLATIONS[table_name]['fields']:
        return FIELD_TRANSLATIONS[table_name]['fields'][field_name]
    if field_name in COMMON_FIELD_TRANSLATIONS:
        return COMMON_FIELD_TRANSLATIONS[field_name]
    return field_name

# 获取状态值的中文显示
def get_status_value(status_value):
    """获取状态值的中文显示"""
    if status_value in STATUS_TRANSLATIONS:
        return STATUS_TRANSLATIONS[status_value]
    return status_value

# 获取数据表的所有字段翻译
def get_table_fields(table_name):
    """获取数据表的所有字段翻译"""
    if table_name in FIELD_TRANSLATIONS:
        return FIELD_TRANSLATIONS[table_name]['fields']
    return {}
