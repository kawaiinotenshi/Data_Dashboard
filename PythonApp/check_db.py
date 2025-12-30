"""
检查数据库表和字段
"""

from database import DatabaseManager
from logger_config import setup_logger

logger = setup_logger('check_db', 'app.log')

def check_database():
    db = DatabaseManager()
    success, message = db.connect()
    if not success:
        print(f"数据库连接失败: {message}")
        return
    
    try:
        cursor = db.connection.cursor()
        cursor.execute("SHOW TABLES")
        tables = cursor.fetchall()
        
        print(f"数据库中共有 {len(tables)} 个表:")
        print("=" * 80)
        
        for table in tables:
            table_name = table[0]
            print(f"\n表名: {table_name}")
            print("-" * 80)
            
            cursor.execute(f"DESCRIBE `{table_name}`")
            columns = cursor.fetchall()
            
            for col in columns:
                field_name = col[0]
                field_type = col[1]
                print(f"  {field_name}: {field_type}")
        
        print("\n" + "=" * 80)
        print(f"检查完成！")
        
    except Exception as e:
        print(f"检查失败: {e}")
        logger.error(f"检查数据库失败: {e}")
    finally:
        db.disconnect()

if __name__ == "__main__":
    check_database()
