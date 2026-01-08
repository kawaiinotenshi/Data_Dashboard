import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import './style.css'
// 导入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 导入WebSocket服务
import websocketService from './utils/websocket'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.mount('#app')

// WebSocket连接将在登录成功后初始化
