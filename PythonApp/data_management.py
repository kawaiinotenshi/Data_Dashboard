import tkinter as tk
from tkinter import ttk, messagebox, filedialog
from datetime import datetime
import csv
import os
import logging

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('app.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)


class DataManagementWindow:
    def __init__(self, root, db_manager):
        self.root = root
        self.db_manager = db_manager
        self.current_table = None
        
        self.root.title("物流管理系统 - 数据管理")
        self.root.geometry("1200x700")
        
        self.create_widgets()
        self.load_tables()

    def create_widgets(self):
        self.root.configure(bg="#f0f0f0")
        
        main_frame = tk.Frame(self.root, bg="#f0f0f0")
        main_frame.pack(fill=tk.BOTH, expand=True, padx=15, pady=15)

        left_frame = tk.Frame(main_frame, width=220, bg="white", relief=tk.RAISED, bd=1)
        left_frame.pack(side=tk.LEFT, fill=tk.Y, padx=(0, 10))
        left_frame.pack_propagate(False)

        tk.Label(left_frame, text="数据表", font=("微软雅黑", 14, "bold"), bg="#34495e", fg="white").pack(fill=tk.X, pady=10)
        
        self.table_listbox = tk.Listbox(left_frame, font=("微软雅黑", 10), width=28, relief=tk.FLAT, bd=0, selectbackground="#3498db", selectforeground="white")
        self.table_listbox.pack(fill=tk.BOTH, expand=True, padx=5, pady=5)
        self.table_listbox.bind('<<ListboxSelect>>', self.on_table_select)

        right_frame = tk.Frame(main_frame, bg="white", relief=tk.RAISED, bd=1)
        right_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)

        search_frame = tk.Frame(right_frame, bg="#ecf0f1", height=60)
        search_frame.pack(fill=tk.X, padx=10, pady=10)
        search_frame.pack_propagate(False)

        tk.Label(search_frame, text="搜索:", font=("微软雅黑", 11), bg="#ecf0f1", fg="#2c3e50").pack(side=tk.LEFT, padx=10)
        self.search_entry = tk.Entry(search_frame, font=("微软雅黑", 10), width=35, relief=tk.SOLID, bd=1)
        self.search_entry.pack(side=tk.LEFT, padx=5)
        self.search_entry.bind('<Return>', lambda e: self.search_data())
        
        search_button = tk.Button(
            search_frame,
            text="搜索",
            font=("微软雅黑", 10),
            bg="#3498db",
            fg="white",
            width=8,
            relief=tk.FLAT,
            cursor="hand2",
            command=self.search_data
        )
        search_button.pack(side=tk.LEFT, padx=5)

        refresh_button = tk.Button(
            search_frame,
            text="刷新",
            font=("微软雅黑", 10),
            bg="#95a5a6",
            fg="white",
            width=8,
            relief=tk.FLAT,
            cursor="hand2",
            command=self.refresh_data
        )
        refresh_button.pack(side=tk.LEFT, padx=5)

        tree_frame = tk.Frame(right_frame, bg="white")
        tree_frame.pack(fill=tk.BOTH, expand=True, padx=10, pady=(0, 10))

        self.tree = ttk.Treeview(tree_frame, show='headings', selectmode='extended')
        self.tree.pack(fill=tk.BOTH, expand=True)
        
        scrollbar_y = ttk.Scrollbar(tree_frame, orient=tk.VERTICAL, command=self.tree.yview)
        scrollbar_y.pack(side=tk.RIGHT, fill=tk.Y)
        self.tree.configure(yscrollcommand=scrollbar_y.set)

        scrollbar_x = ttk.Scrollbar(tree_frame, orient=tk.HORIZONTAL, command=self.tree.xview)
        scrollbar_x.pack(side=tk.BOTTOM, fill=tk.X)
        self.tree.configure(xscrollcommand=scrollbar_x.set)

        button_frame = tk.Frame(right_frame, bg="#ecf0f1", height=60)
        button_frame.pack(fill=tk.X, padx=10, pady=10)
        button_frame.pack_propagate(False)

        tk.Button(button_frame, text="新增", font=("微软雅黑", 10, "bold"), bg="#27ae60", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.add_data).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="修改", font=("微软雅黑", 10, "bold"), bg="#2980b9", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.edit_data).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="删除", font=("微软雅黑", 10, "bold"), bg="#c0392b", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.delete_data).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="批量删除", font=("微软雅黑", 10, "bold"), bg="#e74c3c", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.batch_delete).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="统计", font=("微软雅黑", 10, "bold"), bg="#8e44ad", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.show_statistics).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="导出", font=("微软雅黑", 10, "bold"), bg="#f39c12", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.export_data).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="退出", font=("微软雅黑", 10, "bold"), bg="#7f8c8d", fg="white", width=10, relief=tk.FLAT, cursor="hand2", command=self.root.quit).pack(side=tk.RIGHT, padx=5)

        self.tree.bind('<Double-Button-1>', lambda e: self.edit_data())

    def load_tables(self):
        logger.info("开始加载数据表列表")
        success, result = self.db_manager.execute_query("SHOW TABLES")
        if success:
            self.tables = [list(row.values())[0] for row in result]
            for table in self.tables:
                self.table_listbox.insert(tk.END, table)
            logger.info(f"成功加载 {len(self.tables)} 个数据表")
        else:
            logger.error(f"加载数据表列表失败: {result}")
            messagebox.showerror("错误", f"加载数据表列表失败: {result}")

    def on_table_select(self, event):
        selection = self.table_listbox.curselection()
        if selection:
            self.current_table = self.table_listbox.get(selection[0])
            logger.info(f"选择数据表: {self.current_table}")
            self.refresh_data()

    def refresh_data(self):
        if not self.current_table:
            return

        logger.info(f"刷新数据表: {self.current_table}")
        success, result = self.db_manager.get_table_data(self.current_table)
        if success:
            self.display_data(result)
            logger.info(f"成功加载 {len(result)} 条记录")
        else:
            logger.error(f"刷新数据失败: {result}")
            messagebox.showerror("错误", result)

    def search_data(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        search_value = self.search_entry.get().strip()
        if not search_value:
            self.refresh_data()
            return

        logger.info(f"搜索数据: 表={self.current_table}, 关键词={search_value}")
        success, result = self.db_manager.search_data(self.current_table, 'name', search_value)
        if not success:
            success, result = self.db_manager.search_data(self.current_table, 'location', search_value)
        
        if success:
            self.display_data(result)
            logger.info(f"搜索完成，找到 {len(result)} 条记录")
        else:
            logger.error(f"搜索失败: {result}")
            messagebox.showerror("错误", result)

    def display_data(self, data):
        self.tree.delete(*self.tree.get_children())
        
        if not data:
            return

        columns = list(data[0].keys())
        self.tree['columns'] = columns
        
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=100, anchor='center')

        for row in data:
            self.tree.insert('', tk.END, values=list(row.values()))

    def add_data(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        self.open_data_dialog("新增数据")

    def edit_data(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        selection = self.tree.selection()
        if not selection:
            messagebox.showwarning("警告", "请选择要修改的数据")
            return

        item = self.tree.item(selection[0])
        self.open_data_dialog("修改数据", item['values'])

    def delete_data(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        selection = self.tree.selection()
        if not selection:
            messagebox.showwarning("警告", "请选择要删除的数据")
            return

        item = self.tree.item(selection[0])
        columns = self.tree['columns']
        id_value = item['values'][0]
        
        if not messagebox.askyesno("确认", f"确定要删除 {id_value} 这条数据吗？"):
            return

        logger.info(f"删除数据: 表={self.current_table}, ID={id_value}")
        success, result = self.db_manager.delete_data(self.current_table, columns[0], id_value)
        if success:
            messagebox.showinfo("成功", "删除成功")
            logger.info("删除成功")
            self.refresh_data()
        else:
            logger.error(f"删除失败: {result}")
            messagebox.showerror("错误", result)

    def open_data_dialog(self, title, values=None):
        dialog = tk.Toplevel(self.root)
        dialog.title(title)
        dialog.geometry("600x500")
        dialog.resizable(False, False)
        dialog.configure(bg="#f0f0f0")
        dialog.transient(self.root)
        dialog.grab_set()

        frame = tk.Frame(dialog, bg="white", padx=30, pady=30, relief=tk.RAISED, bd=1)
        frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)

        columns = self.tree['columns']
        entries = {}

        for i, col in enumerate(columns):
            row_frame = tk.Frame(frame, bg="white")
            row_frame.pack(fill=tk.X, pady=8)

            tk.Label(row_frame, text=f"{col}:", font=("微软雅黑", 11, "bold"), bg="white", fg="#2c3e50", width=18, anchor='e').pack(side=tk.LEFT, padx=(0, 10))
            
            entry = tk.Entry(row_frame, font=("微软雅黑", 10), relief=tk.SOLID, bd=1)
            entry.pack(side=tk.LEFT, fill=tk.X, expand=True, padx=(0, 10))
            
            if values and i < len(values):
                entry.insert(0, str(values[i]))
            
            entries[col] = entry

        button_frame = tk.Frame(dialog, bg="#f0f0f0")
        button_frame.pack(pady=10)

        tk.Button(button_frame, text="保存", font=("微软雅黑", 11, "bold"), bg="#27ae60", fg="white", width=12, height=2, relief=tk.FLAT, cursor="hand2", command=lambda: save_data()).pack(side=tk.LEFT, padx=10)
        tk.Button(button_frame, text="取消", font=("微软雅黑", 11, "bold"), bg="#7f8c8d", fg="white", width=12, height=2, relief=tk.FLAT, cursor="hand2", command=dialog.destroy).pack(side=tk.LEFT, padx=10)

        def save_data():
            data = {}
            for col in columns:
                value = entries[col].get().strip()
                if not value:
                    messagebox.showwarning("警告", f"{col}不能为空")
                    return
                data[col] = value

            if values:
                id_value = values[0]
                logger.info(f"更新数据: 表={self.current_table}, ID={id_value}, 数据={data}")
                success, result = self.db_manager.update_data(self.current_table, data, columns[0], id_value)
            else:
                logger.info(f"新增数据: 表={self.current_table}, 数据={data}")
                success, result = self.db_manager.insert_data(self.current_table, data)

            if success:
                logger.info("数据保存成功")
                messagebox.showinfo("成功", "保存成功")
                dialog.destroy()
                self.refresh_data()
            else:
                logger.error(f"数据保存失败: {result}")
                messagebox.showerror("错误", result)

    def batch_delete(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        selections = self.tree.selection()
        if not selections:
            messagebox.showwarning("警告", "请选择要删除的数据")
            return

        if not messagebox.askyesno("确认", f"确定要删除选中的 {len(selections)} 条数据吗？"):
            return

        columns = self.tree['columns']
        id_column = columns[0]
        deleted_count = 0
        failed_count = 0
        errors = []

        logger.info(f"开始批量删除: 表={self.current_table}, 数量={len(selections)}")

        for selection in selections:
            item = self.tree.item(selection)
            id_value = item['values'][0]
            
            success, result = self.db_manager.delete_data(self.current_table, id_column, id_value)
            if success:
                deleted_count += 1
                logger.debug(f"删除成功: ID={id_value}")
            else:
                failed_count += 1
                errors.append(f"ID {id_value}: {result}")
                logger.error(f"删除失败: ID={id_value}, 错误={result}")

        if deleted_count > 0:
            self.refresh_data()

        logger.info(f"批量删除完成: 成功={deleted_count}, 失败={failed_count}")

        message = f"删除完成\n\n成功: {deleted_count} 条"
        if failed_count > 0:
            message += f"\n失败: {failed_count} 条"
            if errors:
                message += "\n\n失败详情:\n" + "\n".join(errors[:5])
                if len(errors) > 5:
                    message += f"\n... 还有 {len(errors) - 5} 条错误"

        if failed_count == 0:
            messagebox.showinfo("成功", message)
        else:
            messagebox.showwarning("部分失败", message)

    def export_data(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        file_path = filedialog.asksaveasfilename(
            defaultextension=".csv",
            filetypes=[("CSV文件", "*.csv"), ("所有文件", "*.*")],
            initialfile=f"{self.current_table}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv",
            title="导出数据"
        )

        if not file_path:
            return

        try:
            logger.info(f"开始导出数据: 表={self.current_table}, 文件={file_path}")
            success, data = self.db_manager.get_table_data(self.current_table)
            if not success:
                logger.error(f"获取数据失败: {data}")
                messagebox.showerror("错误", f"获取数据失败: {data}")
                return

            if not data:
                messagebox.showwarning("警告", "没有数据可导出")
                return

            columns = list(data[0].keys())
            
            with open(file_path, 'w', newline='', encoding='utf-8-sig') as csvfile:
                writer = csv.writer(csvfile)
                writer.writerow(columns)
                for row in data:
                    writer.writerow([str(row[col]) for col in columns])

            logger.info(f"数据导出成功: 记录数={len(data)}, 文件={file_path}")
            messagebox.showinfo("成功", f"数据已成功导出到:\n{file_path}")
        except Exception as e:
            logger.error(f"导出失败: {str(e)}")
            messagebox.showerror("错误", f"导出失败: {str(e)}")

    def show_statistics(self):
        if not self.current_table:
            messagebox.showwarning("警告", "请先选择数据表")
            return

        logger.info(f"显示数据统计: 表={self.current_table}")

        success, data = self.db_manager.get_table_data(self.current_table)
        if not success:
            logger.error(f"获取数据失败: {data}")
            messagebox.showerror("错误", f"获取数据失败: {data}")
            return

        if not data:
            messagebox.showinfo("提示", "当前表没有数据")
            return

        dialog = tk.Toplevel(self.root)
        dialog.title(f"数据统计 - {self.current_table}")
        dialog.geometry("600x500")
        dialog.resizable(False, False)
        dialog.configure(bg="#f0f0f0")
        dialog.transient(self.root)
        dialog.grab_set()

        frame = tk.Frame(dialog, bg="white", padx=30, pady=30, relief=tk.RAISED, bd=1)
        frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)

        tk.Label(frame, text=f"数据表: {self.current_table}", font=("微软雅黑", 16, "bold"), bg="white", fg="#2c3e50").pack(pady=(0, 20))

        total_count = len(data)
        columns = list(data[0].keys())

        stats_text = tk.Text(frame, font=("微软雅黑", 11), bg="#f8f9fa", relief=tk.SOLID, bd=1, height=15, width=50)
        stats_text.pack(fill=tk.BOTH, expand=True, pady=10)

        stats_text.insert(tk.END, f"总记录数: {total_count}\n")
        stats_text.insert(tk.END, f"字段数量: {len(columns)}\n")
        stats_text.insert(tk.END, f"字段列表: {', '.join(columns)}\n\n")

        stats_text.insert(tk.END, "=" * 50 + "\n\n")

        for col in columns:
            non_null_count = sum(1 for row in data if row[col] is not None and str(row[col]).strip() != '')
            null_count = total_count - non_null_count
            unique_values = len(set(str(row[col]) for row in data if row[col] is not None))

            stats_text.insert(tk.END, f"字段: {col}\n")
            stats_text.insert(tk.END, f"  非空记录: {non_null_count} ({non_null_count/total_count*100:.1f}%)\n")
            stats_text.insert(tk.END, f"  空值记录: {null_count} ({null_count/total_count*100:.1f}%)\n")
            stats_text.insert(tk.END, f"  唯一值数: {unique_values}\n\n")

        stats_text.config(state=tk.DISABLED)

        button_frame = tk.Frame(dialog, bg="#f0f0f0")
        button_frame.pack(pady=10)

        tk.Button(button_frame, text="关闭", font=("微软雅黑", 11, "bold"), bg="#7f8c8d", fg="white", width=12, height=2, relief=tk.FLAT, cursor="hand2", command=dialog.destroy).pack()
