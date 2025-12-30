import tkinter as tk
from tkinter import messagebox
from config import ADMIN_CREDENTIALS
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


class LoginWindow:
    def __init__(self, root, on_login_success):
        self.root = root
        self.on_login_success = on_login_success
        
        self.root.title("物流管理系统 - 登录")
        self.root.geometry("450x350")
        self.root.resizable(False, False)
        self.root.configure(bg="#f0f0f0")
        
        self.root.protocol("WM_DELETE_WINDOW", self.on_close)
        
        self.create_widgets()

    def create_widgets(self):
        self.root.configure(bg="#f0f0f0")
        
        title_label = tk.Label(
            self.root, 
            text="物流管理系统", 
            font=("微软雅黑", 24, "bold"),
            fg="#2c3e50",
            bg="#f0f0f0"
        )
        title_label.pack(pady=40)

        frame = tk.Frame(self.root, bg="white", padx=30, pady=30, relief=tk.RAISED, bd=2)
        frame.pack(pady=20, padx=40)

        tk.Label(frame, text="用户名:", font=("微软雅黑", 11), bg="white", fg="#333").grid(row=0, column=0, padx=10, pady=15, sticky="e")
        self.username_entry = tk.Entry(frame, font=("微软雅黑", 11), width=20, relief=tk.SOLID, bd=1)
        self.username_entry.grid(row=0, column=1, padx=10, pady=15)

        tk.Label(frame, text="密码:", font=("微软雅黑", 11), bg="white", fg="#333").grid(row=1, column=0, padx=10, pady=15, sticky="e")
        self.password_entry = tk.Entry(frame, font=("微软雅黑", 11), width=20, show="*", relief=tk.SOLID, bd=1)
        self.password_entry.grid(row=1, column=1, padx=10, pady=15)

        button_frame = tk.Frame(self.root, bg="#f0f0f0")
        button_frame.pack(pady=30)

        login_button = tk.Button(
            button_frame,
            text="登录",
            font=("微软雅黑", 12, "bold"),
            bg="#3498db",
            fg="white",
            width=12,
            height=2,
            relief=tk.FLAT,
            cursor="hand2",
            activebackground="#2980b9",
            command=self.login
        )
        login_button.pack(side=tk.LEFT, padx=15)

        exit_button = tk.Button(
            button_frame,
            text="退出",
            font=("微软雅黑", 12, "bold"),
            bg="#e74c3c",
            fg="white",
            width=12,
            height=2,
            relief=tk.FLAT,
            cursor="hand2",
            activebackground="#c0392b",
            command=self.root.quit
        )
        exit_button.pack(side=tk.LEFT, padx=15)

        self.root.bind('<Return>', lambda e: self.login())

    def on_close(self):
        logger.info("用户关闭登录窗口，退出应用程序")
        self.root.quit()

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
