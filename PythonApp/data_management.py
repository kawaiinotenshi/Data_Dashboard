import tkinter as tk
from tkinter import ttk, messagebox, filedialog
from datetime import datetime
from theme_manager import ThemeManager
from logger_config import setup_logger
import csv
import os

logger = setup_logger('data_management', 'app.log')


class DataManagementWindow:
    def __init__(self, root, db_manager, theme_manager):
        self.root = root
        self.db_manager = db_manager
        self.theme_manager = theme_manager
        self.current_table = None
        
        self.root.title("物流管理系统 - 数据管理")
        self.root.geometry("1200x700")
        
        self.apply_theme()
        self.create_widgets()
        self.load_tables()

    def apply_theme(self):
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        
        self.root.configure(bg=colors['background'])

    def create_widgets(self):
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        fonts = theme['fonts']
        spacing = theme['spacing']
        
        self.root.configure(bg=colors['background'])
        
        main_frame = tk.Frame(self.root, bg=colors['background'])
        main_frame.pack(fill=tk.BOTH, expand=True, padx=spacing['large'], pady=spacing['large'])

        left_frame = tk.Frame(main_frame, width=220, bg=colors['surface'], relief=tk.RAISED, bd=1)
        left_frame.pack(side=tk.LEFT, fill=tk.Y, padx=(0, spacing['normal']))
        left_frame.pack_propagate(False)

        tk.Label(left_frame, text="数据表", font=(fonts['family'], fonts['size']['large'], 'bold'), bg=colors['primary'], fg=colors['light']).pack(fill=tk.X, pady=spacing['normal'])
        
        self.table_listbox = tk.Listbox(left_frame, font=(fonts['family'], fonts['size']['normal']), width=28, relief=tk.FLAT, bd=0, selectbackground=colors['accent'], selectforeground=colors['light'])
        self.table_listbox.pack(fill=tk.BOTH, expand=True, padx=spacing['small'], pady=spacing['small'])
        self.table_listbox.bind('<<ListboxSelect>>', self.on_table_select)

        right_frame = tk.Frame(main_frame, bg=colors['surface'], relief=tk.RAISED, bd=1)
        right_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)

        search_frame = tk.Frame(right_frame, bg=colors['light'], height=60)
        search_frame.pack(fill=tk.X, padx=spacing['normal'], pady=spacing['normal'])
        search_frame.pack_propagate(False)

        tk.Label(search_frame, text="搜索:", font=(fonts['family'], fonts['size']['normal']), bg=colors['light'], fg=colors['primary']).pack(side=tk.LEFT, padx=spacing['normal'])
        self.search_entry = tk.Entry(search_frame, font=(fonts['family'], fonts['size']['small']), width=35, relief=tk.SOLID, bd=1)
        self.search_entry.pack(side=tk.LEFT, padx=spacing['small'])
        self.search_entry.bind('<Return>', lambda e: self.search_data())
        
        search_button = tk.Button(
            search_frame,
            text="搜索",
            font=(fonts['family'], fonts['size']['small']),
            bg=colors['accent'],
            fg=colors['light'],
            width=8,
            relief=tk.FLAT,
            cursor="hand2",
            activebackground=colors['secondary'],
            activeforeground=colors['light'],
            command=self.search_data
        )
        search_button.pack(side=tk.LEFT, padx=spacing['small'])

        refresh_button = tk.Button(
            search_frame,
            text="刷新",
            font=(fonts['family'], fonts['size']['small']),
            bg=colors['text_secondary'],
            fg=colors['light'],
            width=8,
            relief=tk.FLAT,
            cursor="hand2",
            activebackground=colors['secondary'],
            activeforeground=colors['light'],
            command=self.refresh_data
        )
        refresh_button.pack(side=tk.LEFT, padx=spacing['small'])

        tree_frame = tk.Frame(right_frame, bg=colors['surface'])
        tree_frame.pack(fill=tk.BOTH, expand=True, padx=spacing['normal'], pady=(0, spacing['normal']))

        style = ttk.Style()
        style.configure("Treeview", 
                       font=(fonts['family'], fonts['size']['small']),
                       rowheight=25,
                       background=colors['surface'],
                       fieldbackground=colors['surface'],
                       foreground=colors['text'])
        style.configure("Treeview.Heading", 
                       font=(fonts['family'], fonts['size']['normal'], 'bold'),
                       background=colors['primary'],
                       foreground=colors['light'])
        style.map("Treeview", 
                 background=[('selected', colors['accent'])],
                 foreground=[('selected', colors['light'])])

        self.tree = ttk.Treeview(tree_frame, show='headings', selectmode='extended', style="Treeview")
        self.tree.pack(fill=tk.BOTH, expand=True)
        
        scrollbar_y = ttk.Scrollbar(tree_frame, orient=tk.VERTICAL, command=self.tree.yview)
        scrollbar_y.pack(side=tk.RIGHT, fill=tk.Y)
        self.tree.configure(yscrollcommand=scrollbar_y.set)

        scrollbar_x = ttk.Scrollbar(tree_frame, orient=tk.HORIZONTAL, command=self.tree.xview)
        scrollbar_x.pack(side=tk.BOTTOM, fill=tk.X)
        self.tree.configure(xscrollcommand=scrollbar_x.set)

        button_frame = tk.Frame(right_frame, bg=colors['light'], height=60)
        button_frame.pack(fill=tk.X, padx=spacing['normal'], pady=spacing['normal'])
        button_frame.pack_propagate(False)

        tk.Button(button_frame, text="新增", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['success'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.add_data).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="修改", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['accent'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.edit_data).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="删除", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['danger'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.delete_data).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="批量删除", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['danger'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.batch_delete).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="统计", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['accent'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.show_statistics).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="导出", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['warning'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.export_data).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="设置", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['primary'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.show_settings).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="退出", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['text_secondary'], fg=colors['light'], width=10, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=self.root.quit).pack(side=tk.RIGHT, padx=spacing['small'])

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
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        fonts = theme['fonts']
        spacing = theme['spacing']
        
        dialog = tk.Toplevel(self.root)
        dialog.title(title)
        dialog.geometry("600x500")
        dialog.resizable(False, False)
        dialog.configure(bg=colors['background'])
        dialog.transient(self.root)
        dialog.grab_set()

        frame = tk.Frame(dialog, bg=colors['surface'], padx=spacing['xlarge'], pady=spacing['xlarge'], relief=tk.RAISED, bd=1)
        frame.pack(fill=tk.BOTH, expand=True, padx=spacing['large'], pady=spacing['large'])

        columns = self.tree['columns']
        entries = {}

        for i, col in enumerate(columns):
            row_frame = tk.Frame(frame, bg=colors['surface'])
            row_frame.pack(fill=tk.X, pady=spacing['normal'])

            tk.Label(row_frame, text=f"{col}:", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['surface'], fg=colors['text'], width=18, anchor='e').pack(side=tk.LEFT, padx=(0, spacing['small']))
            
            entry = tk.Entry(row_frame, font=(fonts['family'], fonts['size']['small']), relief=tk.SOLID, bd=1)
            entry.pack(side=tk.LEFT, fill=tk.X, expand=True, padx=(0, spacing['small']))
            
            if values and i < len(values):
                entry.insert(0, str(values[i]))
            
            entries[col] = entry

        button_frame = tk.Frame(dialog, bg=colors['background'])
        button_frame.pack(pady=spacing['normal'])

        tk.Button(button_frame, text="保存", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['success'], fg=colors['light'], width=12, height=2, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=lambda: save_data()).pack(side=tk.LEFT, padx=spacing['small'])
        tk.Button(button_frame, text="取消", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['text_secondary'], fg=colors['light'], width=12, height=2, relief=tk.FLAT, cursor="hand2", command=dialog.destroy).pack(side=tk.LEFT, padx=spacing['small'])

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

        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        fonts = theme['fonts']
        spacing = theme['spacing']
        
        dialog = tk.Toplevel(self.root)
        dialog.title(f"数据统计 - {self.current_table}")
        dialog.geometry("600x500")
        dialog.resizable(False, False)
        dialog.configure(bg=colors['background'])
        dialog.transient(self.root)
        dialog.grab_set()

        frame = tk.Frame(dialog, bg=colors['surface'], padx=spacing['xlarge'], pady=spacing['xlarge'], relief=tk.RAISED, bd=1)
        frame.pack(fill=tk.BOTH, expand=True, padx=spacing['large'], pady=spacing['large'])

        tk.Label(frame, text=f"数据表: {self.current_table}", font=(fonts['family'], fonts['size']['xlarge'], 'bold'), bg=colors['surface'], fg=colors['primary']).pack(pady=(0, spacing['large']))

        total_count = len(data)
        columns = list(data[0].keys())

        stats_text = tk.Text(frame, font=(fonts['family'], fonts['size']['normal']), bg=colors['light'], relief=tk.SOLID, bd=1, height=15, width=50)
        stats_text.pack(fill=tk.BOTH, expand=True, pady=spacing['normal'])

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

        button_frame = tk.Frame(dialog, bg=colors['background'])
        button_frame.pack(pady=spacing['normal'])

        tk.Button(button_frame, text="关闭", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['text_secondary'], fg=colors['light'], width=12, height=2, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=dialog.destroy).pack()

    def show_settings(self):
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        fonts = theme['fonts']
        spacing = theme['spacing']
        
        dialog = tk.Toplevel(self.root)
        dialog.title("系统设置")
        dialog.geometry("500x600")
        dialog.resizable(False, False)
        dialog.configure(bg=colors['background'])
        dialog.transient(self.root)
        dialog.grab_set()

        frame = tk.Frame(dialog, bg=colors['surface'], padx=spacing['xlarge'], pady=spacing['xlarge'], relief=tk.RAISED, bd=1)
        frame.pack(fill=tk.BOTH, expand=True, padx=spacing['large'], pady=spacing['large'])

        tk.Label(frame, text="系统设置", font=(fonts['family'], fonts['size']['xlarge'], 'bold'), bg=colors['surface'], fg=colors['primary']).pack(pady=(0, spacing['large']))

        tk.Label(frame, text=f"当前主题: {theme['name']}", font=(fonts['family'], fonts['size']['normal']), bg=colors['surface'], fg=colors['text']).pack(pady=spacing['small'])
        tk.Label(frame, text=f"AB测试组: {self.theme_manager.ab_test_group}", font=(fonts['family'], fonts['size']['normal']), bg=colors['surface'], fg=colors['text']).pack(pady=spacing['small'])
        tk.Label(frame, text=f"用户ID: {self.theme_manager.user_id}", font=(fonts['family'], fonts['size']['small']), bg=colors['surface'], fg=colors['text_secondary']).pack(pady=spacing['small'])

        tk.Label(frame, text="主题切换:", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['surface'], fg=colors['primary']).pack(pady=(spacing['large'], spacing['normal']))

        theme_frame = tk.Frame(frame, bg=colors['surface'])
        theme_frame.pack(fill=tk.X, pady=spacing['normal'])

        for theme_name in ['industrial', 'modern', 'classic']:
            btn = tk.Button(
                theme_frame,
                text=theme_name.upper(),
                font=(fonts['family'], fonts['size']['small']),
                bg=colors['accent'] if theme_name == self.theme_manager.current_theme else colors['text_secondary'],
                fg=colors['light'],
                width=10,
                relief=tk.FLAT,
                cursor="hand2",
                activebackground=colors['secondary'],
                activeforeground=colors['light'],
                command=lambda t=theme_name: self.change_theme(t, dialog)
            )
            btn.pack(side=tk.LEFT, padx=spacing['small'])

        tk.Label(frame, text="灰度测试功能:", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['surface'], fg=colors['primary']).pack(pady=(spacing['large'], spacing['normal']))

        from config import GRAYSCALE_TESTING
        
        for feature_name, feature in GRAYSCALE_TESTING['features'].items():
            status = "启用" if self.theme_manager.is_feature_enabled(feature_name) else "禁用"
            status_color = colors['success'] if self.theme_manager.is_feature_enabled(feature_name) else colors['danger']
            
            feature_frame = tk.Frame(frame, bg=colors['surface'])
            feature_frame.pack(fill=tk.X, pady=spacing['small'])
            
            tk.Label(
                feature_frame,
                text=f"{feature['name']}: ",
                font=(fonts['family'], fonts['size']['small']),
                bg=colors['surface'],
                fg=colors['text']
            ).pack(side=tk.LEFT)
            
            tk.Label(
                feature_frame,
                text=status,
                font=(fonts['family'], fonts['size']['small'], 'bold'),
                bg=colors['surface'],
                fg=status_color
            ).pack(side=tk.LEFT)

        button_frame = tk.Frame(dialog, bg=colors['background'])
        button_frame.pack(pady=spacing['normal'])

        tk.Button(button_frame, text="关闭", font=(fonts['family'], fonts['size']['normal'], 'bold'), bg=colors['text_secondary'], fg=colors['light'], width=12, height=2, relief=tk.FLAT, cursor="hand2", activebackground=colors['secondary'], activeforeground=colors['light'], command=dialog.destroy).pack()

    def change_theme(self, theme_name, dialog):
        self.theme_manager.set_theme(theme_name)
        logger.info(f"主题已切换为: {theme_name}")
        
        self.apply_theme()
        
        dialog.destroy()
        
        messagebox.showinfo("成功", f"主题已切换为 {theme_name}\n请重启应用程序以完全应用新主题")
