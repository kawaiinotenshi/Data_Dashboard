import pymysql
import os

sql_file = r'c:\Users\15071\Documents\大数据面板\logistics_db.sql'

try:
    conn = pymysql.connect(host='localhost', port=3306, user='root', password='root', database='logistics_db')
    cursor = conn.cursor()

    with open(sql_file, 'r', encoding='utf-8') as f:
        sql_content = f.read()

    statements = sql_content.split(';')
    for statement in statements:
        statement = statement.strip()
        if statement and not statement.startswith('--') and not statement.startswith('/*'):
            try:
                cursor.execute(statement)
                conn.commit()
            except Exception as e:
                if 'Duplicate entry' not in str(e):
                    print(f"执行SQL出错: {e}")
                    print(f"SQL: {statement[:100]}")

    cursor.close()
    conn.close()
    print("数据导入成功！")

except Exception as e:
    print(f"导入失败: {e}")
