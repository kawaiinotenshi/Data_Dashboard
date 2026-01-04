<template>
  <div class="admin-header">
    <div class="header-left">
      <h1>大数据可视化管理系统</h1>
    </div>
    <div class="header-right">
      <div class="user-info">
        <span class="user-avatar">👤</span>
        <span class="user-name">{{ userName }}</span>
      </div>
      <div class="header-actions">
        <button
          class="action-btn"
          @click="handleLogout"
        >
          <span>退出登录</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const userName = ref('管理员')

onMounted(() => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    try {
      const user = JSON.parse(userInfo)
      userName.value = user.username || '管理员'
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }
})

const handleLogout = () => {
  if (confirm('确定要退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }
}
</script>

<style scoped>
.admin-header {
  width: 100%;
  height: 60px;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 100;
}

.header-left h1 {
  margin: 0;
  color: #1a1a2e;
  font-size: 20px;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  background-color: #f5f5f5;
  border-radius: 20px;
}

.user-avatar {
  font-size: 20px;
}

.user-name {
  color: #1a1a2e;
  font-size: 14px;
  font-weight: 500;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  padding: 8px 20px;
  background-color: #4a90e2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.action-btn:hover {
  background-color: #357abd;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(74, 144, 226, 0.3);
}

.action-btn:active {
  transform: translateY(0);
}
</style>
