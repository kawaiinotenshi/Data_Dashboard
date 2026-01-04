# -*- coding: utf-8 -*-
import pymysql

try:
    conn = pymysql.connect(host='localhost', port=3306, user='root', password='root')
    cursor = conn.cursor()
    cursor.execute('SELECT VERSION()')
    version = cursor.fetchone()[0]
    print(f"MySQL版本: {version}")
    
    cursor.execute('SHOW VARIABLES LIKE "version_comment"')
    comment = cursor.fetchone()[1]
    print(f"MySQL类型: {comment}")
    
    cursor.close()
    conn.close()
except Exception as e:
    print(f"Error: {e}")
