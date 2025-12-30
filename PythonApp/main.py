import tkinter as tk
from login_window import LoginWindow
from data_management import DataManagementWindow
from database import DatabaseManager
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


class LogisticsApp:
    def __init__(self):
        logger.info("启动物流管理系统")
        self.root = tk.Tk()
        self.root.withdraw()
        
        self.db_manager = DatabaseManager()
        
        self.show_login()

    def show_login(self):
        logger.info("显示登录窗口")
        login_root = tk.Toplevel(self.root)
        LoginWindow(login_root, self.on_login_success)
        self.root.wait_window(login_root)
        
        if not login_root.winfo_exists():
            logger.info("登录窗口被关闭，退出应用程序")
            self.root.quit()

    def on_login_success(self):
        logger.info("登录成功，尝试连接数据库")
        success, message = self.db_manager.connect()
        if success:
            logger.info("数据库连接成功，显示数据管理窗口")
            self.show_data_management()
        else:
            logger.error(f"数据库连接失败: {message}")
            tk.messagebox.showerror("错误", message)
            self.root.quit()

    def show_data_management(self):
        DataManagementWindow(self.root, self.db_manager)

    def run(self):
        logger.info("进入主循环")
        self.root.mainloop()
        logger.info("应用程序退出，断开数据库连接")
        self.db_manager.disconnect()


if __name__ == "__main__":
    app = LogisticsApp()
    app.run()
