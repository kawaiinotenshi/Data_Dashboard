import { useOrderStore } from '../stores/order'
import { useWarehouseStore } from '../stores/warehouse'
import { useTransportStore } from '../stores/transport'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

class WebSocketService {
  constructor() {
    this.stompClient = null
    this.isConnected = false
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5
    this.reconnectDelay = 3000
    this.connectionTimeout = 10000 // 连接超时时间设置为10秒
  }

  // 连接WebSocket
  connect() {
    if (this.isConnected || this.reconnectAttempts >= this.maxReconnectAttempts) {
      return
    }

    try {
      // 创建SockJS连接，使用完整URL
      const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081'
      const socket = new SockJS(baseUrl + '/ws', null, { timeout: this.connectionTimeout })
      this.stompClient = Stomp.over(socket)

      // 配置连接选项，添加认证令牌
      const headers = {}
      const token = localStorage.getItem('token')
      if (token) {
        headers.Authorization = `Bearer ${token}`
      }

      // 连接成功回调
      this.stompClient.connect(headers, 
        () => {
          console.log('WebSocket连接成功')
          this.isConnected = true
          this.reconnectAttempts = 0
          this.subscribeToTopics()
        }, 
        (error) => {
          console.error('WebSocket连接失败:', error)
          this.isConnected = false
          this.handleReconnect()
        }
      )
    } catch (error) {
      console.error('WebSocket连接异常:', error)
      this.isConnected = false
      this.handleReconnect()
    }
  }

  // 订阅主题
  subscribeToTopics() {
    if (!this.stompClient || !this.isConnected) {
      return
    }

    // 订阅订单更新
    this.stompClient.subscribe('/topic/orders', (message) => {
      console.log('收到订单更新:', message.body)
      const orderStore = useOrderStore()
      try {
        const data = JSON.parse(message.body)
        orderStore.updateOrders(data)
      } catch (error) {
        console.error('解析订单更新数据失败:', error)
      }
    })

    // 订阅仓库更新
    this.stompClient.subscribe('/topic/warehouses', (message) => {
      console.log('收到仓库更新:', message.body)
      const warehouseStore = useWarehouseStore()
      try {
        const data = JSON.parse(message.body)
        warehouseStore.updateWarehouses(data)
      } catch (error) {
        console.error('解析仓库更新数据失败:', error)
      }
    })

    // 订阅物流任务更新
    this.stompClient.subscribe('/topic/transports', (message) => {
        console.log('收到物流任务更新:', message.body)
        const transportStore = useTransportStore()
        try {
            const data = JSON.parse(message.body)
            transportStore.updateTransportTasks(data)
        } catch (error) {
            console.error('解析物流任务更新数据失败:', error)
        }
    })

    // 订阅运输路线更新
    this.stompClient.subscribe('/topic/transportRoutes', (message) => {
        console.log('收到运输路线更新:', message.body)
        try {
            const routes = JSON.parse(message.body)
            // 通知所有组件更新运输路线
            window.dispatchEvent(new CustomEvent('transportRoutesUpdated', { detail: routes }))
        } catch (error) {
            console.error('解析运输路线更新数据失败:', error)
        }
    })

    // 订阅库存预警
    this.stompClient.subscribe('/topic/inventoryAlerts', (message) => {
        console.log('收到库存预警:', message.body)
        // 处理库存预警
    })
  }

  // 处理重连
  handleReconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++
      console.log(`尝试重新连接WebSocket... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)
      setTimeout(() => {
        this.connect()
      }, this.reconnectDelay)
    } else {
      console.error('WebSocket重连失败，已达到最大尝试次数')
    }
  }

  // 断开连接
  disconnect() {
    if (this.stompClient && this.isConnected) {
      this.stompClient.disconnect(() => {
        console.log('WebSocket连接已断开')
        this.isConnected = false
      })
    }
  }

  // 发送消息
  send(destination, headers, body) {
    if (this.stompClient && this.isConnected) {
      this.stompClient.send(destination, headers, body)
    } else {
      console.error('WebSocket未连接，无法发送消息')
    }
  }
}

// 导出单例实例
const websocketService = new WebSocketService()
export default websocketService