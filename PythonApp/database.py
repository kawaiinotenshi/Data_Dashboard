import mysql.connector
from mysql.connector import Error
import re
from config import DB_CONFIG
from logger_config import setup_logger

logger = setup_logger('database', 'database.log')


class DatabaseManager:
    def __init__(self):
        self.connection = None

    def connect(self):
        try:
            logger.info(f"尝试连接数据库: {DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}")
            self.connection = mysql.connector.connect(**DB_CONFIG)
            logger.info("数据库连接成功")
            return True, "数据库连接成功"
        except Error as e:
            error_msg = f"数据库连接失败: {e}"
            logger.error(error_msg)
            return False, error_msg

    def disconnect(self):
        if self.connection and self.connection.is_connected():
            self.connection.close()
            logger.info("数据库连接已关闭")

    def validate_table_name(self, table_name):
        if not table_name:
            return False, "表名不能为空"
        if not re.match(r'^[a-zA-Z_][a-zA-Z0-9_]*$', table_name):
            return False, "表名格式不正确"
        return True, None

    def validate_field_name(self, field_name):
        if not field_name:
            return False, "字段名不能为空"
        if not re.match(r'^[a-zA-Z_][a-zA-Z0-9_]*$', field_name):
            return False, "字段名格式不正确"
        return True, None

    def execute_query(self, query, params=None, fetch_all=True):
        try:
            logger.debug(f"执行查询: {query[:100]}{'...' if len(query) > 100 else ''}")
            cursor = self.connection.cursor(dictionary=True)
            cursor.execute(query, params or ())
            
            if fetch_all:
                result = cursor.fetchall()
            else:
                result = cursor.fetchone()
                
            cursor.close()
            logger.debug(f"查询成功，返回 {len(result) if isinstance(result, list) else 1} 条记录")
            return True, result
        except Error as e:
            error_msg = f"查询执行失败: {e}"
            logger.error(f"{error_msg}\n查询语句: {query}")
            return False, error_msg

    def execute_update(self, query, params=None):
        try:
            logger.debug(f"执行更新: {query[:100]}{'...' if len(query) > 100 else ''}")
            cursor = self.connection.cursor()
            cursor.execute(query, params or ())
            affected_rows = cursor.rowcount
            self.connection.commit()
            cursor.close()
            logger.info(f"更新成功，影响 {affected_rows} 行")
            return True, f"操作成功，影响 {affected_rows} 行"
        except Error as e:
            self.connection.rollback()
            error_msg = f"操作失败: {e}"
            logger.error(f"{error_msg}\n更新语句: {query}")
            return False, error_msg

    def get_table_data(self, table_name):
        is_valid, error = self.validate_table_name(table_name)
        if not is_valid:
            return False, error
        
        query = "SELECT * FROM `{}`".format(table_name)
        return self.execute_query(query)

    def insert_data(self, table_name, data):
        is_valid, error = self.validate_table_name(table_name)
        if not is_valid:
            return False, error
        
        columns = ', '.join([f"`{k}`" for k in data.keys()])
        placeholders = ', '.join(['%s'] * len(data))
        query = "INSERT INTO `{}` ({}) VALUES ({})".format(table_name, columns, placeholders)
        return self.execute_update(query, tuple(data.values()))

    def update_data(self, table_name, data, condition_field, condition_value):
        is_valid, error = self.validate_table_name(table_name)
        if not is_valid:
            return False, error
        
        is_valid, error = self.validate_field_name(condition_field)
        if not is_valid:
            return False, error
        
        set_clause = ', '.join([f"`{k}` = %s" for k in data.keys()])
        query = "UPDATE `{}` SET {} WHERE `{}` = %s".format(table_name, set_clause, condition_field)
        params = tuple(data.values()) + (condition_value,)
        return self.execute_update(query, params)

    def delete_data(self, table_name, condition_field, condition_value):
        is_valid, error = self.validate_table_name(table_name)
        if not is_valid:
            return False, error
        
        is_valid, error = self.validate_field_name(condition_field)
        if not is_valid:
            return False, error
        
        query = "DELETE FROM `{}` WHERE `{}` = %s".format(table_name, condition_field)
        return self.execute_update(query, (condition_value,))

    def search_data(self, table_name, search_field, search_value):
        is_valid, error = self.validate_table_name(table_name)
        if not is_valid:
            return False, error
        
        is_valid, error = self.validate_field_name(search_field)
        if not is_valid:
            return False, error
        
        query = "SELECT * FROM `{}` WHERE `{}` LIKE %s".format(table_name, search_field)
        return self.execute_query(query, (f"%{search_value}%",))

    def get_table_columns(self, table_name):
        is_valid, error = self.validate_table_name(table_name)
        if not is_valid:
            return False, error
        
        query = "DESCRIBE `{}`".format(table_name)
        return self.execute_query(query)
