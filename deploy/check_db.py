import pymysql

try:
    conn = pymysql.connect(host='localhost', port=3306, user='root', password='root')
    cursor = conn.cursor()

    print('Available databases:')
    cursor.execute('SHOW DATABASES')
    for row in cursor.fetchall():
        print(row)

    print('\nConnecting to logistics_db...')
    conn.select_db('logistics_db')

    print('\nCustomer table structure:')
    cursor.execute('DESCRIBE customer')
    for row in cursor.fetchall():
        print(row)

    print('\nSupplier table structure:')
    cursor.execute('DESCRIBE supplier')
    for row in cursor.fetchall():
        print(row)

    print('\nTransport table structure:')
    cursor.execute('DESCRIBE transport')
    for row in cursor.fetchall():
        print(row)

    print('\nWarehouse table structure:')
    cursor.execute('DESCRIBE warehouse')
    for row in cursor.fetchall():
        print(row)

    print('\nOrders table structure:')
    cursor.execute('DESCRIBE orders')
    for row in cursor.fetchall():
        print(row)

    conn.close()
except Exception as e:
    print(f'Error: {e}')
