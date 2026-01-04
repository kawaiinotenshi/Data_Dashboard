<template>
  <div class="nav-menu">
    <div
      v-for="item in menuItems"
      :key="item.path"
      class="nav-item"
      :class="{ active: currentPath === item.path }"
      @click="navigateTo(item.path)"
    >
      {{ item.name }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const menuItems = [
  { name: '物流看板', path: '/dashboard' },
  { name: '供应链看板', path: '/supply-chain' },
  { name: '管理系统', path: '/admin' }
]

const currentPath = computed(() => route.path)

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.nav-menu {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-item {
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
  font-size: 14px;
  color: #ffffff;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.nav-item.active {
  background: rgba(0, 255, 255, 0.3);
  border-color: rgba(0, 255, 255, 0.5);
  box-shadow: 0 0 10px rgba(0, 255, 255, 0.3);
}
</style>
