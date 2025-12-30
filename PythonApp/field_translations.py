"""
字段翻译配置
将数据库字段名翻译成中文，方便管理人员查看
"""

# 数据表字段翻译映射
FIELD_TRANSLATIONS = {
    'business_quarterly': {
        'table_name': '业务季度数据',
        'fields': {
            'id': 'ID',
            'year': '年份',
            'quarter': '季度',
            'business_type': '业务类型',
            'amount': '金额',
            'growth_rate': '增长率',
            'created_time': '创建时间'
        }
    },
    'customer': {
        'table_name': '客户',
        'fields': {
            'id': '客户ID',
            'name': '客户名称',
            'contact_person': '联系人',
            'phone': '电话',
            'address': '地址',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'customs_clearance': {
        'table_name': '海关清关',
        'fields': {
            'id': 'ID',
            'country': '国家',
            'product_type': '产品类型',
            'quantity': '数量',
            'year_month': '年月',
            'growth_rate': '增长率',
            'created_time': '创建时间'
        }
    },
    'in_out_record': {
        'table_name': '进出记录',
        'fields': {
            'id': 'ID',
            'record_date': '记录日期',
            'type': '类型',
            'quantity': '数量',
            'warehouse_id': '仓库ID',
            'created_time': '创建时间'
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
    'orders': {
        'table_name': '订单',
        'fields': {
            'id': '订单ID',
            'order_no': '订单编号',
            'customer_name': '客户名称',
            'order_amount': '订单金额',
            'order_date': '订单日期',
            'status': '状态',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'supplier': {
        'table_name': '供应商',
        'fields': {
            'id': '供应商ID',
            'name': '供应商名称',
            'contact_person': '联系人',
            'phone': '电话',
            'address': '地址',
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
            'vehicle_type': '车辆类型',
            'vehicle_count': '车辆数量',
            'total_distance': '总距离',
            'month': '月份',
            'status': '状态',
            'created_time': '创建时间',
            'updated_time': '更新时间',
            'is_deleted': '是否删除',
            'version': '版本'
        }
    },
    'warehouse': {
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
    'warehouse_distribution': {
        'table_name': '仓库分布',
        'fields': {
            'id': 'ID',
            'province': '省份',
            'city': '城市',
            'warehouse_count': '仓库数量',
            'total_capacity': '总容量',
            'created_time': '创建时间'
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
