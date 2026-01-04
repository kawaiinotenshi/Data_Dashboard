import pymysql

try:
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', password='root')
    cursor = conn.cursor()
    
    cursor.execute('SELECT user, host, plugin FROM mysql.user WHERE user="root"')
    result = cursor.fetchall()
    print("MySQL用户配置:")
    for row in result:
        print(f"  用户: {row[0]}, 主机: {row[1]}, 认证插件: {row[2]}")
    
    cursor.execute('SHOW VARIABLES LIKE "default_authentication_plugin"')
    result = cursor.fetchone()
    print(f"\n默认认证插件: {result[1]}")
    
    cursor.execute('SELECT @@version')
    version = cursor.fetchone()[0]
    print(f"MySQL版本: {version}")
    
    cursor.close()
    conn.close()
except Exception as e:
    print(f"Error: {e}")
