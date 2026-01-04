import pymysql
import sys

try:
    connection = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='root',
        charset='utf8mb4'
    )
    
    print("✓ MySQL连接成功")
    
    cursor = connection.cursor()
    
    cursor.execute("SHOW DATABASES")
    databases = cursor.fetchall()
    print(f"\n当前数据库列表:")
    for db in databases:
        print(f"  - {db[0]}")
    
    if 'logistics_db' in [db[0] for db in databases]:
        print("\n✓ logistics_db 数据库已存在")
        cursor.execute("USE logistics_db")
        cursor.execute("SHOW TABLES")
        tables = cursor.fetchall()
        print(f"\nlogistics_db 中的表:")
        for table in tables:
            print(f"  - {table[0]}")
    else:
        print("\n✗ logistics_db 数据库不存在")
    
    cursor.close()
    connection.close()
    
except Exception as e:
    print(f"✗ 连接失败: {e}")
    sys.exit(1)