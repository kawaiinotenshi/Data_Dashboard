"""
主题管理器模块
处理AB测试、灰度测试和主题切换功能
"""

import json
import os
from datetime import datetime
from config import AB_TESTING, GRAYSCALE_TESTING, THEMES, APP_CONFIG, get_theme, get_ab_test_group

class ThemeManager:
    """主题管理器类，负责处理主题切换和AB测试功能"""
    
    def __init__(self):
        self.current_theme = APP_CONFIG['theme']
        self.ab_test_group = AB_TESTING['current_group']
        self.user_id = self._generate_user_id()
        self.settings_file = 'user_settings.json'
        self.load_user_settings()
    
    def _generate_user_id(self):
        """生成唯一的用户ID"""
        import uuid
        return str(uuid.uuid4())
    
    def load_user_settings(self):
        """加载用户设置"""
        if os.path.exists(self.settings_file):
            try:
                with open(self.settings_file, 'r', encoding='utf-8') as f:
                    settings = json.load(f)
                    self.current_theme = settings.get('theme', self.current_theme)
                    self.ab_test_group = settings.get('ab_test_group', self.ab_test_group)
            except (json.JSONDecodeError, IOError):
                pass
        
        # 如果启用了AB测试，分配用户到测试组
        if AB_TESTING['enabled']:
            self._assign_ab_test_group()
    
    def save_user_settings(self):
        """保存用户设置"""
        settings = {
            'user_id': self.user_id,
            'theme': self.current_theme,
            'ab_test_group': self.ab_test_group,
            'last_updated': datetime.now().isoformat()
        }
        
        try:
            with open(self.settings_file, 'w', encoding='utf-8') as f:
                json.dump(settings, f, ensure_ascii=False, indent=2)
        except IOError:
            pass
    
    def _assign_ab_test_group(self):
        """根据分配比例将用户分配到AB测试组"""
        if not AB_TESTING['enabled']:
            return
        
        # 使用用户ID确保一致性分配
        hash_value = hash(self.user_id) % 100
        cumulative = 0
        
        for group, ratio in AB_TESTING['allocation_ratio'].items():
            cumulative += ratio * 100
            if hash_value < cumulative:
                self.ab_test_group = group
                break
    
    def get_current_theme(self):
        """获取当前主题配置"""
        # 如果启用了AB测试，使用测试组的主题
        if AB_TESTING['enabled']:
            group_config = get_ab_test_group()
            theme_name = group_config['features']['theme']
            return get_theme(theme_name)
        
        return get_theme(self.current_theme)
    
    def get_available_themes(self):
        """获取可用主题列表"""
        return [
            {
                'id': theme_id,
                'name': theme_config['name'],
                'description': theme_config['description']
            }
            for theme_id, theme_config in THEMES.items()
        ]
    
    def set_theme(self, theme_name):
        """设置主题"""
        if theme_name in THEMES:
            self.current_theme = theme_name
            self.save_user_settings()
            logger.info(f"主题已设置为: {theme_name}")
        else:
            logger.warning(f"未找到主题: {theme_name}")
    
    def is_feature_enabled(self, feature_name):
        """检查灰度测试功能是否启用"""
        if not GRAYSCALE_TESTING['enabled']:
            return GRAYSCALE_TESTING['features'][feature_name]['enabled']
        
        # 根据用户ID决定是否启用功能
        user_hash = hash(self.user_id)
        feature_config = GRAYSCALE_TESTING['features'][feature_name]
        rollout_percentage = feature_config['rollout_percentage']
        
        # 简单的哈希算法，确保一致性
        return (user_hash % 100) < rollout_percentage
    
    def get_ab_test_info(self):
        """获取AB测试信息"""
        if not AB_TESTING['enabled']:
            return None
        
        group_config = get_ab_test_group()
        return {
            'enabled': True,
            'current_group': self.ab_test_group,
            'group_name': group_config['name'],
            'group_description': group_config['description'],
            'features': group_config['features']
        }
    
    def get_grayscale_test_info(self):
        """获取灰度测试信息"""
        if not GRAYSCALE_TESTING['enabled']:
            return None
        
        features_info = {}
        for feature_id, feature_config in GRAYSCALE_TESTING['features'].items():
            features_info[feature_id] = {
                'name': feature_config['name'],
                'description': feature_config['description'],
                'enabled': feature_config['enabled'],
                'rollout_percentage': feature_config['rollout_percentage'],
                'user_has_access': self.is_feature_enabled(feature_id)
            }
        
        return {
            'enabled': True,
            'features': features_info
        }
    
    def get_ui_config(self):
        """获取UI配置，结合主题和功能设置"""
        theme = self.get_current_theme()
        
        ui_config = {
            'theme': theme,
            'animations': True,
            'layout': 'standard'
        }
        
        # 如果启用了AB测试，应用测试组设置
        if AB_TESTING['enabled']:
            group_config = get_ab_test_group()
            ui_config['animations'] = group_config['features'].get('animations', True)
            ui_config['layout'] = group_config['features'].get('layout', 'standard')
        
        return ui_config
    
    def apply_theme_to_widget(self, widget, widget_type='default'):
        """将主题应用到tkinter控件"""
        theme = self.get_current_theme()
        colors = theme['colors']
        fonts = theme['fonts']
        
        # 基础样式
        if widget_type == 'window':
            widget.configure(bg=colors['background'])
        elif widget_type == 'frame':
            widget.configure(bg=colors['surface'], relief=tk.RAISED, bd=1)
        elif widget_type == 'button':
            widget.configure(
                bg=colors['primary'],
                fg=colors['light'],
                font=(fonts['family'], fonts['size']['normal'], 'bold'),
                relief=tk.FLAT,
                activebackground=colors['secondary'],
                activeforeground=colors['light'],
                bd=0,
                padx=10,
                pady=5
            )
        elif widget_type == 'entry':
            widget.configure(
                bg=colors['surface'],
                fg=colors['text'],
                font=(fonts['family'], fonts['size']['normal']),
                relief=tk.SOLID,
                bd=1,
                insertbackground=colors['primary']
            )
        elif widget_type == 'label':
            widget.configure(
                bg=colors['surface'],
                fg=colors['text'],
                font=(fonts['family'], fonts['size']['normal'])
            )
        elif widget_type == 'title':
            widget.configure(
                bg=colors['surface'],
                fg=colors['primary'],
                font=(fonts['family'], fonts['size']['xlarge'], 'bold')
            )
        elif widget_type == 'tree':
            widget.configure(
                bg=colors['surface'],
                fg=colors['text'],
                font=(fonts['family'], fonts['size']['small']),
                selectbackground=colors['primary'],
                selectforeground=colors['light']
            )
    
    def get_color(self, color_name):
        """获取主题颜色"""
        theme = self.get_current_theme()
        return theme['colors'].get(color_name, '#000000')
    
    def get_font(self, size_name='normal', bold=False):
        """获取主题字体"""
        theme = self.get_current_theme()
        fonts = theme['fonts']
        size = fonts['size'].get(size_name, fonts['size']['normal'])
        weight = 'bold' if bold else 'normal'
        return (fonts['family'], size, weight)
    
    def get_spacing(self, size_name='normal'):
        """获取主题间距"""
        theme = self.get_current_theme()
        return theme['spacing'].get(size_name, theme['spacing']['normal'])
    
    def get_border_radius(self):
        """获取主题边框圆角"""
        theme = self.get_current_theme()
        return theme.get('border_radius', 0)

# 全局主题管理器实例
theme_manager = ThemeManager()