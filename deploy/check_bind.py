# -*- coding: utf-8 -*-
import pymysql

try:
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', password='root')
    cursor = conn.cursor()
    cursor.execute('SHOW VARIABLES LIKE "bind_address"')
    result = cursor.fetchall()
    print(f"MySQL bind_address: {result}")
    cursor.close()
    conn.close()
except Exception as e:
    print(f"Error: {e}")
