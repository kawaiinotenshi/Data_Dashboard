import pymysql

hosts = ['localhost', '127.0.0.1', '0.0.0.0']

for host in hosts:
    try:
        conn = pymysql.connect(host=host, port=3306, user='root', password='root', database='logistics_db')
        cursor = conn.cursor()
        cursor.execute('SELECT 1')
        result = cursor.fetchone()
        print(f"✓ 成功连接到 {host}:3306")
        cursor.close()
        conn.close()
    except Exception as e:
        print(f"✗ 连接 {host}:3306 失败: {e}")
