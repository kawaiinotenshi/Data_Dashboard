import tkinter as tk
from login_window import LoginWindow
from data_management import DataManagementWindow
from database import DatabaseManager
from theme_manager import ThemeManager
from logger_config import setup_logger

logger = setup_logger('app', 'app.log')


class LogisticsApp:
    def __init__(self):
        logger.info("启动物流管理系统")
        self.root = tk.Tk()
        self.root.withdraw()
        
        self.theme_manager = ThemeManager()
        logger.info(f"主题管理器初始化完成 - 当前主题: {self.theme_manager.current_theme}, AB测试组: {self.theme_manager.ab_test_group}")
        
        self.db_manager = DatabaseManager()
        self.login_success = False
        
        self.show_login()

    def show_login(self):
        logger.info("显示登录窗口")
        login_root = tk.Toplevel(self.root)
        LoginWindow(login_root, self.on_login_success, self.theme_manager)
        self.root.wait_window(login_root)
        
        if not self.login_success:
            logger.info("登录窗口被关闭，退出应用程序")
            self.root.quit()

    def on_login_success(self):
        logger.info("登录成功，尝试连接数据库")
        self.login_success = True
        success, message = self.db_manager.connect()
        if success:
            logger.info("数据库连接成功，显示数据管理窗口")
            self.show_data_management()
        else:
            logger.error(f"数据库连接失败: {message}")
            tk.messagebox.showerror("错误", message)
            self.root.quit()

    def show_data_management(self):
        DataManagementWindow(self.root, self.db_manager, self.theme_manager)

    def run(self):
        logger.info("进入主循环")
        self.root.mainloop()
        logger.info("应用程序退出，断开数据库连接")
        self.db_manager.disconnect()


if __name__ == "__main__":
    app = LogisticsApp()
    app.run()
