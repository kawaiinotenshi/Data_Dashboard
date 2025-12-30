import tkinter as tk
from tkinter import messagebox
from config import ADMIN_CREDENTIALS
from theme_manager import ThemeManager
from logger_config import setup_logger

logger = setup_logger('login', 'app.log')


class LoginWindow:
    def __init__(self, root, on_login_success, theme_manager):
        self.root = root
        self.on_login_success = on_login_success
        self.theme_manager = theme_manager
        
        self.root.title("物流管理系统 - 登录")
        self.root.geometry("450x350")
        self.root.resizable(False, False)
        
        self.root.protocol("WM_DELETE_WINDOW", self.on_close)
        
        self.apply_theme()
        self.create_widgets()

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
        
        title_label = tk.Label(
            self.root, 
            text="物流管理系统", 
            font=(fonts['family'], fonts['size']['xlarge'], 'bold'),
            fg=colors['primary'],
            bg=colors['background']
        )
        title_label.pack(pady=spacing['xlarge'])

        frame = tk.Frame(self.root, bg=colors['surface'], padx=spacing['xlarge'], pady=spacing['xlarge'], relief=tk.RAISED, bd=1)
        frame.pack(pady=spacing['large'], padx=spacing['large'])

        tk.Label(frame, text="用户名:", font=(fonts['family'], fonts['size']['normal']), bg=colors['surface'], fg=colors['text']).grid(row=0, column=0, padx=spacing['normal'], pady=spacing['normal'], sticky="e")
        self.username_entry = tk.Entry(frame, font=(fonts['family'], fonts['size']['normal']), width=20, relief=tk.SOLID, bd=1)
        self.username_entry.grid(row=0, column=1, padx=spacing['normal'], pady=spacing['normal'])

        tk.Label(frame, text="密码:", font=(fonts['family'], fonts['size']['normal']), bg=colors['surface'], fg=colors['text']).grid(row=1, column=0, padx=spacing['normal'], pady=spacing['normal'], sticky="e")
        self.password_entry = tk.Entry(frame, font=(fonts['family'], fonts['size']['normal']), width=20, show="*", relief=tk.SOLID, bd=1)
        self.password_entry.grid(row=1, column=1, padx=spacing['normal'], pady=spacing['normal'])

        button_frame = tk.Frame(self.root, bg=colors['background'])
        button_frame.pack(pady=spacing['xlarge'])

        login_button = tk.Button(
            button_frame,
            text="登录",
            font=(fonts['family'], fonts['size']['normal'], 'bold'),
            bg=colors['accent'],
            fg=colors['light'],
            width=12,
            height=2,
            relief=tk.FLAT,
            cursor="hand2",
            activebackground=colors['secondary'],
            activeforeground=colors['light'],
            command=self.login
        )
        login_button.pack(side=tk.LEFT, padx=spacing['normal'])

        exit_button = tk.Button(
            button_frame,
            text="退出",
            font=(fonts['family'], fonts['size']['normal'], 'bold'),
            bg=colors['danger'],
            fg=colors['light'],
            width=12,
            height=2,
            relief=tk.FLAT,
            cursor="hand2",
            activebackground=colors['secondary'],
            activeforeground=colors['light'],
            command=self.root.quit
        )
        exit_button.pack(side=tk.LEFT, padx=spacing['normal'])

        self.root.bind('<Return>', lambda e: self.login())

    def on_close(self):
        logger.info("用户关闭登录窗口，退出应用程序")
        self.root.destroy()
        self.on_login_success = None

    def login(self):
        username = self.username_entry.get().strip()
        password = self.password_entry.get().strip()

        if not username or not password:
            messagebox.showwarning("警告", "请输入用户名和密码")
            return

        logger.info(f"尝试登录: 用户名={username}")

        if username == ADMIN_CREDENTIALS['username'] and password == ADMIN_CREDENTIALS['password']:
            logger.info("登录成功")
            messagebox.showinfo("成功", "登录成功！")
            self.root.destroy()
            self.on_login_success()
        else:
            logger.warning(f"登录失败: 用户名={username}")
            messagebox.showerror("错误", "用户名或密码错误")
            self.password_entry.delete(0, tk.END)
