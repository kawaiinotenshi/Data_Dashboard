"""
日志配置模块
提供统一的日志配置和管理功能
"""

import logging
import os
from datetime import datetime

def setup_logger(name, log_file=None, level=logging.INFO):
    """
    设置统一的日志配置
    
    Args:
        name: 日志器名称
        log_file: 日志文件名，如果为None则使用默认名称
        level: 日志级别
    
    Returns:
        配置好的logger实例
    """
    if log_file is None:
        log_file = f"{name}.log"
    
    # 创建logs目录（如果不存在）
    log_dir = os.path.dirname(log_file) if os.path.dirname(log_file) else 'logs'
    if not os.path.exists(log_dir):
        os.makedirs(log_dir)
    
    # 如果是相对路径，添加logs目录前缀
    if not os.path.isabs(log_file) and not log_file.startswith('logs/'):
        log_file = os.path.join('logs', log_file)
    
    logger = logging.getLogger(name)
    
    # 如果logger已经有处理器，说明已经配置过，直接返回
    if logger.handlers:
        return logger
    
    logger.setLevel(level)
    
    # 创建格式化器
    formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    
    # 创建文件处理器
    file_handler = logging.FileHandler(log_file, encoding='utf-8')
    file_handler.setLevel(level)
    file_handler.setFormatter(formatter)
    logger.addHandler(file_handler)
    
    # 创建控制台处理器
    console_handler = logging.StreamHandler()
    console_handler.setLevel(level)
    console_handler.setFormatter(formatter)
    logger.addHandler(console_handler)
    
    return logger

def get_logger(name):
    """
    获取已配置的logger
    
    Args:
        name: 日志器名称
    
    Returns:
        logger实例
    """
    return logging.getLogger(name)