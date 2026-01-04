import pymysql

try:
    connection = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='root',
        database='logistics_db',
        charset='utf8mb4'
    )
    
    cursor = connection.cursor()
    
    tables = ['orders', 'warehouse', 'customer', 'supplier', 'transport']
    
    for table in tables:
        cursor.execute(f"SELECT COUNT(*) FROM {table}")
        count = cursor.fetchone()[0]
        print(f"{table}: {count} 条记录")
    
    print("\norders表前3条记录:")
    cursor.execute("SELECT * FROM orders LIMIT 3")
    for row in cursor.fetchall():
        print(f"  {row}")
    
    cursor.close()
    connection.close()
    
except Exception as e:
    print(f"错误: {e}")