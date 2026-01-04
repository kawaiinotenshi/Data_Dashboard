import pymysql

try:
    conn = pymysql.connect(host='localhost', port=3306, user='root', password='root', database='logistics_db')
    cursor = conn.cursor()

    migrations = [
        "ALTER TABLE orders ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE orders ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE orders ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE warehouse ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE warehouse ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE customer ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE customer ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE customer ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE supplier ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE supplier ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE supplier ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE transport ADD COLUMN status varchar(20) DEFAULT '运输中' AFTER month",
        "ALTER TABLE transport ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER status",
        "ALTER TABLE transport ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE transport ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE business_quarterly ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE business_quarterly ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE business_quarterly ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE customs_clearance ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE customs_clearance ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE customs_clearance ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE in_out_record ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE in_out_record ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE in_out_record ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE inventory_ratio ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE inventory_ratio ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE inventory_ratio ADD COLUMN version int DEFAULT 0 AFTER is_deleted",

        "ALTER TABLE warehouse_distribution ADD COLUMN updated_time datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_time",
        "ALTER TABLE warehouse_distribution ADD COLUMN is_deleted tinyint DEFAULT 0 AFTER updated_time",
        "ALTER TABLE warehouse_distribution ADD COLUMN version int DEFAULT 0 AFTER is_deleted",
    ]

    for sql in migrations:
        try:
            cursor.execute(sql)
            conn.commit()
            print(f"✓ 执行成功: {sql[:60]}...")
        except Exception as e:
            if "Duplicate column name" in str(e):
                print(f"⊘ 字段已存在，跳过: {sql[:60]}...")
            else:
                print(f"✗ 执行失败: {sql[:60]}...")
                print(f"  错误: {e}")

    cursor.close()
    conn.close()
    print("\n数据库迁移完成！")

except Exception as e:
    print(f"迁移失败: {e}")
