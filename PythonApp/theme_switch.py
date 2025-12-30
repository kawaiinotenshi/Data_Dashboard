"""
主题切换按钮组件
提供简单的主题切换功能
"""

import tkinter as tk


class ThemeSwitch(tk.Frame):
    def __init__(self, parent, theme_manager, command=None, **kwargs):
        super().__init__(parent, **kwargs)
        self.theme_manager = theme_manager
        self.command = command
        self.is_dark = theme_manager.current_theme == "darkness"
        
        self._init_ui()
    
    def _init_ui(self):
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        
        self.configure(
            bg=colors['background'],
            bd=0,
            highlightthickness=0
        )
        
        self.button = tk.Button(
            self,
            text="🌙" if self.is_dark else "☀",
            font=("Segoe UI Symbol", 14),
            bg=colors['border_normal'],
            fg=colors['text'],
            relief=tk.FLAT,
            bd=0,
            padx=8,
            pady=4,
            cursor='hand2',
            command=self.toggle_switch
        )
        self.button.pack()
    
    def toggle_switch(self):
        self.is_dark = not self.is_dark
        
        if self.command:
            self.command()
        
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        
        self.button.configure(
            text="🌙" if self.is_dark else "☀",
            bg=colors['border_normal'],
            fg=colors['text']
        )
    
    def update_appearance(self):
        theme = self.theme_manager.get_current_theme()
        colors = theme['colors']
        
        self.configure(bg=colors['background'])
        self.button.configure(
            bg=colors['border_normal'],
            fg=colors['text']
        )
