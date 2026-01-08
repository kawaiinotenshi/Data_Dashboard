<template>
  <div class="admin-sidebar">
    <div class="sidebar-header">
      <h2>后台管理系统</h2>
    </div>
    <div class="sidebar-menu">
      <div
        v-for="item in menuItems"
        :key="item.path"
        class="menu-item"
        :class="{ active: activeMenu === item.path }"
        @click="handleMenuClick(item)"
      >
        <span class="menu-icon">{{ item.icon }}</span>
        <span class="menu-text">{{ item.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const menuItems = [
  { name: '用户管理', path: '/admin/users', icon: '👤' },
  { name: '员工管理', path: '/admin/employees', icon: '👥' },
  { name: '部门管理', path: '/admin/departments', icon: '🏢' },
  // 新增数据管理菜单项
  { name: '订单管理', path: '/admin/orders', icon: '📦' },
  { name: '仓库管理', path: '/admin/warehouses', icon: '🏪' },
  { name: '物流任务管理', path: '/admin/logistics-tasks', icon: '🚚' },
  { name: '数据看板', path: '/dashboard', icon: '📊' },
  { name: '供应链看板', path: '/supply-chain', icon: '🔗' }
]

const activeMenu = computed(() => {
  return route.path
})

const handleMenuClick = (item) => {
  router.push(item.path)
}
</script>

<style scoped>
.admin-sidebar {
  width: 240px;
  height: 100%;
  background-color: #1a1a2e;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #16213e;
}

.sidebar-header h2 {
  margin: 0;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
}

.sidebar-menu {
  flex: 1;
  padding: 10px 0;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #a0a0a0;
  cursor: pointer;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.menu-item:hover {
  background-color: #16213e;
  color: #fff;
}

.menu-item.active {
  background-color: #16213e;
  color: #fff;
  border-left-color: #4a90e2;
}

.menu-icon {
  font-size: 20px;
  margin-right: 12px;
  width: 24px;
  text-align: center;
}

.menu-text {
  font-size: 14px;
  font-weight: 500;
}
</style>
