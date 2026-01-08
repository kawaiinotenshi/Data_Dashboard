<template>
  <div class="alert-bar-container">
    <div class="alert-bar-header">
      <i class="el-icon-warning-outline"></i>
      <span class="alert-title">库存预警</span>
    </div>
    <div class="alert-bar-content">
      <div class="alert-scroll-container" ref="scrollContainer">
        <div class="alert-items" ref="alertItems">
          <div 
            v-for="(alert, index) in alertList" 
            :key="alert.id || index"
            :class="['alert-item', `alert-${alert.alertLevel.toLowerCase()}`]"
            @click="handleAlertClick(alert)"
          >
            <i :class="getAlertIcon(alert.alertLevel)"></i>
            <span class="alert-message">{{ alert.alertMessage }}</span>
            <span class="alert-time">{{ formatTime(alert.createTime) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useWebSocketStore } from '@/stores/websocket'
import { useRouter } from 'vue-router'

const router = useRouter()
const websocketStore = useWebSocketStore()
const scrollContainer = ref(null)
const alertItems = ref(null)
const alertList = ref([])
let scrollTimer = null

// 获取预警图标
const getAlertIcon = (level) => {
  switch (level) {
    case 'ERROR':
      return 'el-icon-circle-close'
    case 'WARN':
      return 'el-icon-warning'
    case 'INFO':
      return 'el-icon-info'
    default:
      return 'el-icon-info'
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleTimeString()
}

// 处理预警点击
const handleAlertClick = (alert) => {
  // 根据预警类型跳转到相应页面
  if (alert.alertType.includes('INVENTORY') || alert.alertType.includes('WAREHOUSE')) {
    router.push('/warehouse-management')
  } else if (alert.alertType.includes('ORDER')) {
    router.push('/order-management')
  } else if (alert.alertType.includes('TRANSPORT')) {
    router.push('/transport-task-management')
  }
}

// 开始滚动
const startScroll = () => {
  if (!scrollContainer.value || !alertItems.value) return
  
  const containerHeight = scrollContainer.value.clientHeight
  const contentHeight = alertItems.value.clientHeight
  
  // 如果内容高度小于容器高度，不需要滚动
  if (contentHeight <= containerHeight) {
    return
  }
  
  clearInterval(scrollTimer)
  let scrollPosition = 0
  
  scrollTimer = setInterval(() => {
    scrollPosition += 1
    
    // 滚动到底部后重新开始
    if (scrollPosition >= contentHeight - containerHeight) {
      scrollPosition = 0
    }
    
    scrollContainer.value.scrollTop = scrollPosition
  }, 50)
}

// 停止滚动
const stopScroll = () => {
  clearInterval(scrollTimer)
}

// 监听预警数据变化
watch(
  () => websocketStore.inventoryAlerts,
  (newAlerts) => {
    alertList.value = newAlerts
    // 数据更新后重启滚动
    setTimeout(() => {
      startScroll()
    }, 100)
  },
  { deep: true }
)

onMounted(() => {
  // 初始化时使用store中的预警数据
  alertList.value = websocketStore.inventoryAlerts
  
  // 开始滚动
  setTimeout(() => {
    startScroll()
  }, 500)
  
  // 鼠标悬停停止滚动
  if (scrollContainer.value) {
    scrollContainer.value.addEventListener('mouseenter', stopScroll)
    scrollContainer.value.addEventListener('mouseleave', startScroll)
  }
})

onUnmounted(() => {
  stopScroll()
  if (scrollContainer.value) {
    scrollContainer.value.removeEventListener('mouseenter', stopScroll)
    scrollContainer.value.removeEventListener('mouseleave', startScroll)
  }
})
</script>

<style scoped>
.alert-bar-container {
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 8px;
  padding: 10px;
  color: #fff;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.alert-bar-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 5px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.alert-bar-header i {
  color: #f7ba2a;
  margin-right: 5px;
  font-size: 16px;
}

.alert-title {
  font-size: 14px;
  font-weight: bold;
}

.alert-bar-content {
  flex: 1;
  overflow: hidden;
}

.alert-scroll-container {
  height: 100%;
  overflow: hidden;
  position: relative;
}

.alert-items {
  padding-right: 10px;
}

.alert-item {
  display: flex;
  align-items: center;
  padding: 8px 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 12px;
}

.alert-item:hover {
  transform: translateX(5px);
  opacity: 0.9;
}

.alert-item i {
  margin-right: 8px;
  font-size: 14px;
}

.alert-message {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 10px;
}

.alert-time {
  font-size: 11px;
  opacity: 0.8;
  color: #ccc;
}

/* 不同预警级别的样式 */
.alert-error {
  background-color: rgba(245, 34, 45, 0.2);
  border-left: 3px solid #f5222d;
}

.alert-error i {
  color: #f5222d;
}

.alert-warn {
  background-color: rgba(247, 186, 42, 0.2);
  border-left: 3px solid #f7ba2a;
}

.alert-warn i {
  color: #f7ba2a;
}

.alert-info {
  background-color: rgba(45, 183, 245, 0.2);
  border-left: 3px solid #2db7f5;
}

.alert-info i {
  color: #2db7f5;
}
</style>