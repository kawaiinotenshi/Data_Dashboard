<template>
  <div class="admin-dashboard">
    <AdminHeader />
    <div class="admin-container">
      <AdminSidebar />
      <div class="admin-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import AdminHeader from '../components/AdminHeader.vue'
import AdminSidebar from '../components/AdminSidebar.vue'

// 引入统一的管理界面样式
import '../assets/admin-styles.css'
</script>

<style scoped>
.admin-dashboard {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.admin-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.admin-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f5f5;
}
</style>
