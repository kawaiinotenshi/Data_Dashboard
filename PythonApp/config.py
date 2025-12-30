"""
应用程序配置文件
包含数据库连接配置、应用程序设置和优化功能配置
"""

import os
from datetime import datetime

# 数据库配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 3307,
    'user': 'root',
    'password': 'root',
    'database': 'logistics_db',
    'charset': 'utf8mb4'
}

# 管理员凭证
ADMIN_CREDENTIALS = {
    'username': 'root',
    'password': 'root'
}

# 应用程序配置
APP_CONFIG = {
    'name': '物流管理系统',
    'version': '2.0.0',
    'author': '物流管理团队',
    'description': '高效、安全的物流数据管理解决方案',
    'icon': None,  # 可以设置为图标文件路径
    'theme': 'lightness',  # lightness, darkness
    'language': 'zh_CN',
    'auto_save': True,
    'auto_save_interval': 300,  # 秒
    'max_records_per_page': 100,
    'export_format': 'csv',
    'log_level': 'INFO',
    'log_file': 'app.log',
    'backup_enabled': True,
    'backup_interval': 86400,  # 秒 (24小时)
    'backup_path': 'backups/'
}

# AB测试配置
AB_TESTING = {
    'enabled': False,
    'test_groups': {
        'control': {
            'name': '控制组',
            'description': '使用明亮风格界面',
            'features': {
                'theme': 'lightness',
                'layout': 'standard',
                'animations': False
            }
        },
        'variant_a': {
            'name': '实验组A',
            'description': '使用暗黑风格界面',
            'features': {
                'theme': 'darkness',
                'layout': 'compact',
                'animations': True
            }
        }
    },
    'current_group': 'control',  # 默认分组
    'allocation_ratio': {
        'control': 0.5,
        'variant_a': 0.5
    }
}

# 灰度测试配置
GRAYSCALE_TESTING = {
    'enabled': True,
    'features': {
        'advanced_search': {
            'name': '高级搜索功能',
            'description': '提供更强大的搜索和过滤功能',
            'enabled': True,
            'rollout_percentage': 100  # 0-100
        },
        'data_visualization': {
            'name': '数据可视化功能',
            'description': '提供图表和统计可视化',
            'enabled': True,
            'rollout_percentage': 100  # 0-100
        },
        'batch_operations': {
            'name': '批量操作功能',
            'description': '支持批量数据处理',
            'enabled': True,
            'rollout_percentage': 100  # 0-100
        },
        'auto_backup': {
            'name': '自动备份功能',
            'description': '自动备份重要数据',
            'enabled': True,
            'rollout_percentage': 50  # 0-100
        }
    }
}

# 主题配置
THEMES = {
    'lightness': {
        'name': '明亮风格',
        'description': '明亮、清晰的界面设计',
        'colors': {
            'primary': '#2196F3',
            'secondary': '#757575',
            'accent': '#FF4081',
            'success': '#4CAF50',
            'warning': '#FF9800',
            'danger': '#F44336',
            'light': '#ecf0f1',
            'dark': '#2c3e50',
            'background': '#FFFFFF',
            'surface': '#F5F5F5',
            'card': '#FFFFFF',
            'dialog': '#FFFFFF',
            'text': '#212121',
            'text_secondary': '#757575',
            'header': '#0066cc',
            'border_light': '#E0E0E0',
            'border_normal': '#BDBDBD',
            'border_dark': '#616161',
            'hover_light': '#F5F5F5',
            'hover_dark': '#E0E0E0',
            'status_active': '#2196F3',
            'status_inactive': '#BDBDBD'
        },
        'fonts': {
            'family': 'Microsoft YaHei',
            'size': {
                'small': 10,
                'normal': 12,
                'large': 14,
                'xlarge': 16,
                'title': 16,
                'heading': 18
            },
            'weight': {
                'normal': 'normal',
                'bold': 'bold'
            }
        },
        'spacing': {
            'tiny': 2,
            'small': 4,
            'normal': 8,
            'medium': 12,
            'large': 16,
            'xlarge': 24
        },
        'border_radius': 6
    },
    'darkness': {
        'name': '暗黑风格',
        'description': '暗黑、舒适的界面设计',
        'colors': {
            'primary': '#64B5F6',
            'secondary': '#9E9E9E',
            'accent': '#FF80AB',
            'success': '#81C784',
            'warning': '#FFB74D',
            'danger': '#E57373',
            'light': '#ecf0f1',
            'dark': '#2c3e50',
            'background': '#121212',
            'surface': '#1E1E1E',
            'card': '#2D2D2D',
            'dialog': '#2D2D2D',
            'text': '#FFFFFF',
            'text_secondary': '#B0B0B0',
            'header': '#4da6ff',
            'border_light': '#424242',
            'border_normal': '#616161',
            'border_dark': '#757575',
            'hover_light': '#2D2D2D',
            'hover_dark': '#424242',
            'status_active': '#64B5F6',
            'status_inactive': '#616161'
        },
        'fonts': {
            'family': 'Microsoft YaHei',
            'size': {
                'small': 10,
                'normal': 12,
                'large': 14,
                'xlarge': 16,
                'title': 16,
                'heading': 18
            },
            'weight': {
                'normal': 'normal',
                'bold': 'bold'
            }
        },
        'spacing': {
            'tiny': 2,
            'small': 4,
            'normal': 8,
            'medium': 12,
            'large': 16,
            'xlarge': 24
        },
        'border_radius': 6
    }
}

# 性能配置
PERFORMANCE = {
    'cache_enabled': True,
    'cache_size': 100,  # MB
    'connection_pool_size': 5,
    'query_timeout': 30,  # 秒
    'max_retry_attempts': 3,
    'lazy_loading': True,
    'pagination': True,
    'index_optimization': True
}

# 安全配置
SECURITY = {
    'password_policy': {
        'min_length': 8,
        'require_uppercase': True,
        'require_lowercase': True,
        'require_numbers': True,
        'require_special_chars': False
    },
    'session_timeout': 3600,  # 秒
    'max_login_attempts': 5,
    'lockout_duration': 900,  # 秒
    'audit_log': True,
    'encryption': 'AES-256'
}

# 用户体验配置
UX = {
    'tooltips': True,
    'shortcuts': True,
    'animations': True,
    'confirmations': True,
    'progress_indicators': True,
    'error_recovery': True,
    'help_system': True,
    'tutorials': True
}

# 获取当前主题配置
def get_theme(theme_name=None):
    """获取指定主题的配置，如果未指定则返回当前主题"""
    if theme_name is None:
        theme_name = APP_CONFIG['theme']
    
    return THEMES.get(theme_name, THEMES['lightness'])

# 获取当前AB测试组配置
def get_ab_test_group():
    """获取当前AB测试组配置"""
    if not AB_TESTING['enabled']:
        return AB_TESTING['test_groups']['control']
    
    return AB_TESTING['test_groups'][AB_TESTING['current_group']]

# 检查功能是否启用（灰度测试）
def is_feature_enabled(feature_name):
    """检查指定功能是否在灰度测试中启用"""
    if not GRAYSCALE_TESTING['enabled']:
        return False
    
    feature = GRAYSCALE_TESTING['features'].get(feature_name)
    if not feature:
        return False
    
    return feature['enabled']

# 获取功能启用百分比（灰度测试）
def get_feature_rollout_percentage(feature_name):
    """获取指定功能的灰度测试启用百分比"""
    if not GRAYSCALE_TESTING['enabled']:
        return 0
    
    feature = GRAYSCALE_TESTING['features'].get(feature_name)
    if not feature:
        return 0
    
    return feature['rollout_percentage']

# 创建必要的目录
def ensure_directories():
    """确保必要的目录存在"""
    directories = [
        APP_CONFIG['backup_path'],
        os.path.dirname(APP_CONFIG['log_file']) if os.path.dirname(APP_CONFIG['log_file']) else '.'
    ]
    
    for directory in directories:
        if not os.path.exists(directory):
            os.makedirs(directory)

# 初始化配置
ensure_directories()