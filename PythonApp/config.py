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
    'theme': 'industrial',  # industrial, modern, classic
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
    'enabled': True,
    'test_groups': {
        'control': {
            'name': '控制组',
            'description': '使用标准界面',
            'features': {
                'theme': 'industrial',
                'layout': 'standard',
                'animations': False
            }
        },
        'variant_a': {
            'name': '实验组A',
            'description': '使用现代化界面',
            'features': {
                'theme': 'modern',
                'layout': 'compact',
                'animations': True
            }
        },
        'variant_b': {
            'name': '实验组B',
            'description': '使用经典界面',
            'features': {
                'theme': 'classic',
                'layout': 'expanded',
                'animations': False
            }
        }
    },
    'current_group': 'control',  # 默认分组
    'allocation_ratio': {
        'control': 0.5,
        'variant_a': 0.25,
        'variant_b': 0.25
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
    'industrial': {
        'name': '工业风格',
        'description': '简洁、专业的工业风格界面',
        'colors': {
            'primary': '#2c3e50',
            'secondary': '#34495e',
            'accent': '#3498db',
            'success': '#27ae60',
            'warning': '#f39c12',
            'danger': '#e74c3c',
            'light': '#ecf0f1',
            'dark': '#2c3e50',
            'background': '#f5f5f5',
            'surface': '#ffffff',
            'text': '#2c3e50',
            'text_secondary': '#7f8c8d'
        },
        'fonts': {
            'family': '微软雅黑',
            'size': {
                'small': 9,
                'normal': 10,
                'large': 12,
                'xlarge': 14
            }
        },
        'spacing': {
            'small': 5,
            'normal': 10,
            'large': 15,
            'xlarge': 20
        },
        'border_radius': 2
    },
    'modern': {
        'name': '现代风格',
        'description': '清新、现代的界面设计',
        'colors': {
            'primary': '#3498db',
            'secondary': '#2980b9',
            'accent': '#e74c3c',
            'success': '#2ecc71',
            'warning': '#f1c40f',
            'danger': '#e74c3c',
            'light': '#ecf0f1',
            'dark': '#34495e',
            'background': '#ffffff',
            'surface': '#f8f9fa',
            'text': '#2c3e50',
            'text_secondary': '#7f8c8d'
        },
        'fonts': {
            'family': '微软雅黑',
            'size': {
                'small': 9,
                'normal': 10,
                'large': 12,
                'xlarge': 14
            }
        },
        'spacing': {
            'small': 8,
            'normal': 12,
            'large': 18,
            'xlarge': 24
        },
        'border_radius': 6
    },
    'classic': {
        'name': '经典风格',
        'description': '传统、稳重的界面设计',
        'colors': {
            'primary': '#34495e',
            'secondary': '#2c3e50',
            'accent': '#3498db',
            'success': '#27ae60',
            'warning': '#f39c12',
            'danger': '#c0392b',
            'light': '#ecf0f1',
            'dark': '#2c3e50',
            'background': '#f5f5f5',
            'surface': '#ffffff',
            'text': '#2c3e50',
            'text_secondary': '#7f8c8d'
        },
        'fonts': {
            'family': '宋体',
            'size': {
                'small': 9,
                'normal': 11,
                'large': 13,
                'xlarge': 15
            }
        },
        'spacing': {
            'small': 5,
            'normal': 10,
            'large': 15,
            'xlarge': 20
        },
        'border_radius': 0
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
    
    return THEMES.get(theme_name, THEMES['industrial'])

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