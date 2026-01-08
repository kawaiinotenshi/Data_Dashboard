import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useWebSocketStore = defineStore('websocket', () => {
  // 库存预警列表
  const inventoryAlerts = ref([])
  
  // WebSocket连接状态
  const isConnected = ref(false)
  
  // 添加库存预警
  const addInventoryAlert = (alert) => {
    // 检查是否已存在相同ID的预警，如果存在则更新，否则添加
    const existingIndex = inventoryAlerts.value.findIndex(item => item.id === alert.id)
    if (existingIndex !== -1) {
      inventoryAlerts.value[existingIndex] = alert
    } else {
      inventoryAlerts.value.unshift(alert) // 添加到列表开头
      
      // 限制预警数量，只保留最新的20条
      if (inventoryAlerts.value.length > 20) {
        inventoryAlerts.value.pop()
      }
    }
  }
  
  // 批量添加库存预警
  const addInventoryAlerts = (alerts) => {
    alerts.forEach(alert => {
      addInventoryAlert(alert)
    })
  }
  
  // 清空库存预警
  const clearInventoryAlerts = () => {
    inventoryAlerts.value = []
  }
  
  // 设置WebSocket连接状态
  const setConnected = (connected) => {
    isConnected.value = connected
  }
  
  return {
    inventoryAlerts,
    isConnected,
    addInventoryAlert,
    addInventoryAlerts,
    clearInventoryAlerts,
    setConnected
  }
})
