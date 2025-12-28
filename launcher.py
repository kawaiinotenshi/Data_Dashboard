"""
物流仓储大数据面板 - 项目启动器
提供可视化界面进行环境验证、配置管理和服务启动
"""

import os
import sys
import subprocess
import threading
import time
import json
from tkinter import *
from tkinter import ttk, messagebox, scrolledtext
from pathlib import Path


class ProjectLauncher:
    def __init__(self, root):
        self.root = root
        self.root.title("物流仓储大数据面板 - 项目启动器")
        self.root.geometry("900x700")
        self.root.resizable(True, True)
        
        self.backend_process = None
        self.frontend_process = None
        self.mysql_process = None
        
        self.config_file = Path(__file__).parent / "launcher_config.json"
        self.load_config()
        
        self.setup_ui()
        self.check_environment()
    
    def load_config(self):
        default_config = {
            "backend_path": str(Path(__file__).parent / "backend"),
            "frontend_path": str(Path(__file__).parent / "FrontEnd"),
            "backend_port": 8080,
            "frontend_port": 3000,
            "mysql_port": 3307,
            "mysql_user": "root",
            "mysql_password": "root",
            "database_name": "logistics_db"
        }
        
        if self.config_file.exists():
            with open(self.config_file, 'r', encoding='utf-8') as f:
                self.config = json.load(f)
        else:
            self.config = default_config
            self.save_config()
    
    def save_config(self):
        with open(self.config_file, 'w', encoding='utf-8') as f:
            json.dump(self.config, f, indent=2, ensure_ascii=False)
    
    def setup_ui(self):
        notebook = ttk.Notebook(self.root)
        notebook.pack(fill=BOTH, expand=True, padx=10, pady=10)
        
        self.setup_environment_tab(notebook)
        self.setup_config_tab(notebook)
        self.setup_control_tab(notebook)
        self.setup_logs_tab(notebook)
    
    def setup_environment_tab(self, notebook):
        env_frame = ttk.Frame(notebook)
        notebook.add(env_frame, text="环境验证")
        
        ttk.Label(env_frame, text="环境依赖检查", font=("Arial", 14, "bold")).pack(pady=10)
        
        self.env_status = {
            "Java": {"status": "pending", "version": ""},
            "Node.js": {"status": "pending", "version": ""},
            "MySQL": {"status": "pending", "version": ""},
            "Docker": {"status": "pending", "version": ""},
            "Maven": {"status": "pending", "version": ""},
            "Nginx": {"status": "pending", "version": ""}
        }
        
        status_frame = ttk.LabelFrame(env_frame, text="依赖状态", padding=10)
        status_frame.pack(fill=X, padx=20, pady=10)
        
        self.env_labels = {}
        for i, (name, info) in enumerate(self.env_status.items()):
            frame = ttk.Frame(status_frame)
            frame.pack(fill=X, pady=5)
            
            ttk.Label(frame, text=f"{name}:", width=12).pack(side=LEFT)
            self.env_labels[name] = ttk.Label(frame, text="检查中...", width=30)
            self.env_labels[name].pack(side=LEFT)
        
        ttk.Button(env_frame, text="重新检查环境", command=self.check_environment).pack(pady=10)
        
        details_frame = ttk.LabelFrame(env_frame, text="环境详情", padding=10)
        details_frame.pack(fill=BOTH, expand=True, padx=20, pady=10)
        
        self.env_details = scrolledtext.ScrolledText(details_frame, height=15, wrap=WORD)
        self.env_details.pack(fill=BOTH, expand=True)
    
    def setup_config_tab(self, notebook):
        config_frame = ttk.Frame(notebook)
        notebook.add(config_frame, text="配置管理")
        
        ttk.Label(config_frame, text="项目配置", font=("Arial", 14, "bold")).pack(pady=10)
        
        settings_frame = ttk.LabelFrame(config_frame, text="路径配置", padding=10)
        settings_frame.pack(fill=X, padx=20, pady=10)
        
        self.config_vars = {}
        
        configs = [
            ("后端路径", "backend_path"),
            ("前端路径", "frontend_path")
        ]
        
        for i, (label, key) in enumerate(configs):
            frame = ttk.Frame(settings_frame)
            frame.pack(fill=X, pady=5)
            
            ttk.Label(frame, text=label, width=15).pack(side=LEFT)
            var = StringVar(value=self.config.get(key, ""))
            self.config_vars[key] = var
            entry = ttk.Entry(frame, textvariable=var, width=50)
            entry.pack(side=LEFT, padx=5)
            ttk.Button(frame, text="浏览", command=lambda k=key: self.browse_path(k)).pack(side=LEFT)
        
        port_frame = ttk.LabelFrame(config_frame, text="端口配置", padding=10)
        port_frame.pack(fill=X, padx=20, pady=10)
        
        port_configs = [
            ("后端端口", "backend_port"),
            ("前端端口", "frontend_port"),
            ("MySQL端口", "mysql_port")
        ]
        
        for label, key in port_configs:
            frame = ttk.Frame(port_frame)
            frame.pack(fill=X, pady=5)
            
            ttk.Label(frame, text=label, width=15).pack(side=LEFT)
            var = StringVar(value=str(self.config.get(key, "")))
            self.config_vars[key] = var
            ttk.Entry(frame, textvariable=var, width=20).pack(side=LEFT, padx=5)
        
        db_frame = ttk.LabelFrame(config_frame, text="数据库配置", padding=10)
        db_frame.pack(fill=X, padx=20, pady=10)
        
        db_configs = [
            ("用户名", "mysql_user"),
            ("密码", "mysql_password"),
            ("数据库名", "database_name")
        ]
        
        for label, key in db_configs:
            frame = ttk.Frame(db_frame)
            frame.pack(fill=X, pady=5)
            
            ttk.Label(frame, text=label, width=15).pack(side=LEFT)
            var = StringVar(value=self.config.get(key, ""))
            self.config_vars[key] = var
            ttk.Entry(frame, textvariable=var, width=30).pack(side=LEFT, padx=5)
        
        ttk.Button(config_frame, text="保存配置", command=self.save_settings).pack(pady=10)
    
    def setup_control_tab(self, notebook):
        control_frame = ttk.Frame(notebook)
        notebook.add(control_frame, text="服务控制")
        
        ttk.Label(control_frame, text="服务管理", font=("Arial", 14, "bold")).pack(pady=10)
        
        services_frame = ttk.LabelFrame(control_frame, text="服务列表", padding=10)
        services_frame.pack(fill=X, padx=20, pady=10)
        
        self.service_status = {
            "MySQL": {"running": False, "process": None},
            "后端": {"running": False, "process": None},
            "前端": {"running": False, "process": None}
        }
        
        for service in self.service_status.keys():
            frame = ttk.Frame(services_frame)
            frame.pack(fill=X, pady=10)
            
            ttk.Label(frame, text=service, width=10, font=("Arial", 11, "bold")).pack(side=LEFT, padx=5)
            
            status_var = StringVar(value="未启动")
            status_label = ttk.Label(frame, textvariable=status_var, width=10, foreground="red")
            status_label.pack(side=LEFT, padx=5)
            
            start_btn = ttk.Button(frame, text="启动", command=lambda s=service: self.start_service(s))
            start_btn.pack(side=LEFT, padx=5)
            
            stop_btn = ttk.Button(frame, text="停止", command=lambda s=service: self.stop_service(s))
            stop_btn.pack(side=LEFT, padx=5)
            
            self.service_status[service]["status_var"] = status_var
            self.service_status[service]["status_label"] = status_label
        
        actions_frame = ttk.LabelFrame(control_frame, text="快捷操作", padding=10)
        actions_frame.pack(fill=X, padx=20, pady=10)
        
        ttk.Button(actions_frame, text="一键启动所有服务", command=self.start_all_services).pack(side=LEFT, padx=5)
        ttk.Button(actions_frame, text="一键停止所有服务", command=self.stop_all_services).pack(side=LEFT, padx=5)
        ttk.Button(actions_frame, text="重启所有服务", command=self.restart_all_services).pack(side=LEFT, padx=5)
        
        access_frame = ttk.LabelFrame(control_frame, text="访问地址", padding=10)
        access_frame.pack(fill=X, padx=20, pady=10)
        
        ttk.Label(access_frame, text="前端地址: http://localhost:3000").pack(anchor=W, pady=2)
        ttk.Label(access_frame, text="后端API: http://localhost:8080").pack(anchor=W, pady=2)
        ttk.Button(access_frame, text="打开前端页面", command=self.open_frontend).pack(pady=5)
    
    def setup_logs_tab(self, notebook):
        logs_frame = ttk.Frame(notebook)
        notebook.add(logs_frame, text="运行日志")
        
        ttk.Label(logs_frame, text="服务日志", font=("Arial", 14, "bold")).pack(pady=10)
        
        log_selector = ttk.Frame(logs_frame)
        log_selector.pack(fill=X, padx=20, pady=5)
        
        self.log_source = StringVar(value="全部")
        ttk.Label(log_selector, text="日志来源:").pack(side=LEFT, padx=5)
        ttk.Combobox(log_selector, textvariable=self.log_source, 
                     values=["全部", "后端", "前端", "MySQL"], width=15, state="readonly").pack(side=LEFT, padx=5)
        ttk.Button(log_selector, text="清空日志", command=self.clear_logs).pack(side=LEFT, padx=5)
        ttk.Button(log_selector, text="刷新日志", command=self.refresh_logs).pack(side=LEFT, padx=5)
        
        self.log_text = scrolledtext.ScrolledText(logs_frame, height=25, wrap=WORD, font=("Consolas", 9))
        self.log_text.pack(fill=BOTH, expand=True, padx=20, pady=10)
        
        self.log_text.tag_config("INFO", foreground="blue")
        self.log_text.tag_config("SUCCESS", foreground="green")
        self.log_text.tag_config("ERROR", foreground="red")
        self.log_text.tag_config("WARNING", foreground="orange")
    
    def check_environment(self):
        self.log_message("INFO", "开始检查环境依赖...")
        
        threads = []
        for name in self.env_status.keys():
            thread = threading.Thread(target=self._check_dependency, args=(name,))
            thread.start()
            threads.append(thread)
        
        for thread in threads:
            thread.join()
        
        self.log_message("SUCCESS", "环境检查完成")
        self.update_env_display()
    
    def _check_dependency(self, name):
        try:
            if name == "Java":
                result = subprocess.run(["java", "-version"], capture_output=True, text=True)
                if result.returncode == 0:
                    version = result.stderr.split('\n')[0] if result.stderr else result.stdout.split('\n')[0]
                    self.env_status[name] = {"status": "success", "version": version}
                else:
                    self.env_status[name] = {"status": "error", "version": "未安装"}
            
            elif name == "Node.js":
                result = subprocess.run(["node", "--version"], capture_output=True, text=True)
                if result.returncode == 0:
                    self.env_status[name] = {"status": "success", "version": result.stdout.strip()}
                else:
                    self.env_status[name] = {"status": "error", "version": "未安装"}
            
            elif name == "MySQL":
                result = subprocess.run(["mysql", "--version"], capture_output=True, text=True)
                if result.returncode == 0:
                    self.env_status[name] = {"status": "success", "version": result.stdout.strip()}
                else:
                    self.env_status[name] = {"status": "warning", "version": "未安装或不在PATH中"}
            
            elif name == "Docker":
                result = subprocess.run(["docker", "--version"], capture_output=True, text=True)
                if result.returncode == 0:
                    self.env_status[name] = {"status": "success", "version": result.stdout.strip()}
                else:
                    self.env_status[name] = {"status": "warning", "version": "未安装"}
            
            elif name == "Maven":
                result = subprocess.run(["mvn", "--version"], capture_output=True, text=True)
                if result.returncode == 0:
                    version_line = result.stdout.split('\n')[0]
                    self.env_status[name] = {"status": "success", "version": version_line}
                else:
                    self.env_status[name] = {"status": "warning", "version": "未安装"}
            
            elif name == "Nginx":
                result = subprocess.run(["nginx", "-v"], capture_output=True, text=True)
                if result.returncode == 0:
                    version = result.stderr.strip() if result.stderr else result.stdout.strip()
                    self.env_status[name] = {"status": "success", "version": version}
                else:
                    self.env_status[name] = {"status": "warning", "version": "未安装"}
        
        except Exception as e:
            self.env_status[name] = {"status": "error", "version": f"检查失败: {str(e)}"}
    
    def update_env_display(self):
        details = "环境检查结果:\n" + "="*60 + "\n\n"
        
        for name, info in self.env_status.items():
            status_text = info["version"]
            if info["status"] == "success":
                self.env_labels[name].config(text=f"✓ {status_text}", foreground="green")
                details += f"✓ {name}: {status_text}\n"
            elif info["status"] == "warning":
                self.env_labels[name].config(text=f"⚠ {status_text}", foreground="orange")
                details += f"⚠ {name}: {status_text}\n"
            else:
                self.env_labels[name].config(text=f"✗ {status_text}", foreground="red")
                details += f"✗ {name}: {status_text}\n"
        
        self.env_details.delete(1.0, END)
        self.env_details.insert(END, details)
    
    def browse_path(self, key):
        from tkinter import filedialog
        path = filedialog.askdirectory()
        if path:
            self.config_vars[key].set(path)
    
    def save_settings(self):
        for key, var in self.config_vars.items():
            self.config[key] = var.get()
        
        self.save_config()
        messagebox.showinfo("成功", "配置已保存！")
        self.log_message("SUCCESS", "配置已保存")
    
    def start_service(self, service):
        if self.service_status[service]["running"]:
            messagebox.showwarning("警告", f"{service} 服务已在运行中")
            return
        
        self.log_message("INFO", f"正在启动 {service} 服务...")
        
        try:
            if service == "MySQL":
                self._start_mysql()
            elif service == "后端":
                self._start_backend()
            elif service == "前端":
                self._start_frontend()
            
            self.service_status[service]["status_var"].set("运行中")
            self.service_status[service]["status_label"].config(foreground="green")
            self.service_status[service]["running"] = True
            self.log_message("SUCCESS", f"{service} 服务启动成功")
        
        except Exception as e:
            self.log_message("ERROR", f"{service} 服务启动失败: {str(e)}")
            messagebox.showerror("错误", f"{service} 服务启动失败: {str(e)}")
    
    def _start_mysql(self):
        try:
            subprocess.run(["docker", "start", "logistics-mysql"], check=True, capture_output=True)
            self.log_message("INFO", "MySQL Docker 容器已启动")
        except subprocess.CalledProcessError:
            try:
                subprocess.run(["docker", "run", "-d", "--name", "logistics-mysql",
                              "-e", "MYSQL_ROOT_PASSWORD=root123",
                              "-e", "MYSQL_DATABASE=logistics_db",
                              "-p", f"{self.config['mysql_port']}:3306",
                              "mysql:8.0"], check=True, capture_output=True)
                self.log_message("INFO", "MySQL Docker 容器已创建并启动")
            except:
                raise Exception("无法启动 MySQL，请检查 Docker 是否正常运行")
    
    def _start_backend(self):
        backend_path = self.config["backend_path"]
        if not os.path.exists(backend_path):
            raise Exception(f"后端路径不存在: {backend_path}")
        
        backend_port = self.config["backend_port"]
        
        if self._is_port_in_use(backend_port):
            self.log_message("WARNING", f"端口 {backend_port} 已被占用，尝试自动杀掉占用进程...")
            if not self._kill_process_on_port(backend_port):
                raise Exception(f"无法释放端口 {backend_port}，请手动停止占用该端口的服务")
            if self._is_port_in_use(backend_port):
                raise Exception(f"端口 {backend_port} 仍被占用，请手动停止占用该端口的服务")
        
        jar_file = os.path.join(backend_path, "target", "logistics-dashboard-1.0.0.jar")
        if os.path.exists(jar_file):
            self.backend_process = subprocess.Popen(
                ["java", "-jar", jar_file],
                cwd=backend_path,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                text=True,
                bufsize=1,
                universal_newlines=True
            )
            threading.Thread(target=self._read_backend_logs, daemon=True).start()
        else:
            self.log_message("WARNING", "JAR 文件不存在，尝试使用 Maven 启动...")
            self.backend_process = subprocess.Popen(
                ["mvn", "spring-boot:run"],
                cwd=backend_path,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                text=True,
                bufsize=1,
                universal_newlines=True
            )
            threading.Thread(target=self._read_backend_logs, daemon=True).start()
    
    def _start_frontend(self):
        frontend_path = self.config["frontend_path"]
        if not os.path.exists(frontend_path):
            raise Exception(f"前端路径不存在: {frontend_path}")
        
        frontend_port = self.config["frontend_port"]
        
        if self._is_port_in_use(frontend_port):
            self.log_message("WARNING", f"端口 {frontend_port} 已被占用，尝试自动杀掉占用进程...")
            if not self._kill_process_on_port(frontend_port):
                raise Exception(f"无法释放端口 {frontend_port}，请手动停止占用该端口的服务")
            if self._is_port_in_use(frontend_port):
                raise Exception(f"端口 {frontend_port} 仍被占用，请手动停止占用该端口的服务")
        
        npm_cmd = self._find_npm_command()
        if not npm_cmd:
            raise Exception("未找到 npm 命令，请确保 Node.js 已正确安装并添加到 PATH")
        
        self.frontend_process = subprocess.Popen(
            [npm_cmd, "run", "dev"],
            cwd=frontend_path,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            bufsize=1,
            universal_newlines=True
        )
        threading.Thread(target=self._read_frontend_logs, daemon=True).start()
    
    def _read_backend_logs(self):
        if self.backend_process:
            for line in iter(self.backend_process.stdout.readline, ''):
                if line:
                    self.log_message("INFO", f"[后端] {line.strip()}")
    
    def _read_frontend_logs(self):
        if self.frontend_process:
            for line in iter(self.frontend_process.stdout.readline, ''):
                if line:
                    self.log_message("INFO", f"[前端] {line.strip()}")
    
    def stop_service(self, service):
        if not self.service_status[service]["running"]:
            messagebox.showwarning("警告", f"{service} 服务未运行")
            return
        
        self.log_message("INFO", f"正在停止 {service} 服务...")
        
        try:
            if service == "MySQL":
                subprocess.run(["docker", "stop", "logistics-mysql"], check=True, capture_output=True)
            elif service == "后端" and self.backend_process:
                self.backend_process.terminate()
                self.backend_process.wait(timeout=5)
            elif service == "前端" and self.frontend_process:
                self.frontend_process.terminate()
                self.frontend_process.wait(timeout=5)
            
            self.service_status[service]["status_var"].set("已停止")
            self.service_status[service]["status_label"].config(foreground="red")
            self.service_status[service]["running"] = False
            self.log_message("SUCCESS", f"{service} 服务已停止")
        
        except Exception as e:
            self.log_message("ERROR", f"{service} 服务停止失败: {str(e)}")
            messagebox.showerror("错误", f"{service} 服务停止失败: {str(e)}")
    
    def start_all_services(self):
        self.log_message("INFO", "开始启动所有服务...")
        threading.Thread(target=self._start_all_services_thread, daemon=True).start()
    
    def _start_all_services_thread(self):
        time.sleep(1)
        self.start_service("MySQL")
        time.sleep(5)
        self.start_service("后端")
        time.sleep(10)
        self.start_service("前端")
        self.log_message("SUCCESS", "所有服务启动完成")
    
    def stop_all_services(self):
        self.log_message("INFO", "开始停止所有服务...")
        for service in ["前端", "后端", "MySQL"]:
            if self.service_status[service]["running"]:
                self.stop_service(service)
        self.log_message("SUCCESS", "所有服务已停止")
    
    def restart_all_services(self):
        self.stop_all_services()
        time.sleep(3)
        self.start_all_services()
    
    def open_frontend(self):
        import webbrowser
        webbrowser.open("http://localhost:3000")
    
    def log_message(self, level, message):
        timestamp = time.strftime("%Y-%m-%d %H:%M:%S")
        log_line = f"[{timestamp}] [{level}] {message}\n"
        
        self.root.after(0, lambda: self._append_log(log_line, level))
    
    def _append_log(self, log_line, level):
        self.log_text.insert(END, log_line, level)
        self.log_text.see(END)
    
    def clear_logs(self):
        self.log_text.delete(1.0, END)
        self.log_message("INFO", "日志已清空")
    
    def refresh_logs(self):
        self.log_message("INFO", "日志已刷新")
    
    def _is_port_in_use(self, port):
        try:
            import socket
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.settimeout(1)
                result = s.connect_ex(('localhost', port))
                return result == 0
        except:
            return False
    
    def _kill_process_on_port(self, port):
        try:
            result = subprocess.run(
                ["netstat", "-ano"],
                capture_output=True,
                text=True
            )
            
            for line in result.stdout.split('\n'):
                if f":{port}" in line and "LISTENING" in line:
                    parts = line.split()
                    if len(parts) >= 5:
                        pid = parts[-1]
                        try:
                            subprocess.run(
                                ["taskkill", "/PID", pid, "/F"],
                                capture_output=True
                            )
                            self.log_message("INFO", f"已杀掉占用端口 {port} 的进程 (PID: {pid})")
                            time.sleep(1)
                            return True
                        except:
                            self.log_message("WARNING", f"无法杀掉进程 {pid}")
            return False
        except Exception as e:
            self.log_message("WARNING", f"查找端口 {port} 占用进程失败: {str(e)}")
            return False
    
    def _find_npm_command(self):
        try:
            result = subprocess.run(["npm", "--version"], capture_output=True, text=True)
            if result.returncode == 0:
                return "npm"
        except:
            pass
        
        try:
            result = subprocess.run(["npm.cmd", "--version"], capture_output=True, text=True)
            if result.returncode == 0:
                return "npm.cmd"
        except:
            pass
        
        return None


def main():
    root = Tk()
    app = ProjectLauncher(root)
    root.mainloop()


if __name__ == "__main__":
    main()
