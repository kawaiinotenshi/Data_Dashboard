<template>
  <div class="user-management">
    <div class="page-header">
      <h2>用户管理</h2>
      <button
        class="btn-primary"
        @click="handleAddUser"
      >
        <span>+ 添加用户</span>
      </button>
    </div>

    <div class="search-bar">
      <input
        v-model="searchKeyword"
        type="text"
        placeholder="搜索用户名、邮箱或手机号"
        class="search-input"
        @input="handleSearch"
      >
    </div>

    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>手机号</th>
            <th>角色</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="user in filteredUsers"
            :key="user.id"
          >
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.phone }}</td>
            <td>
              <span
                class="role-badge"
                :class="user.role"
              >{{ user.role }}</span>
            </td>
            <td>
              <span
                class="status-badge"
                :class="user.status"
              >
                {{ user.status === 'active' ? '启用' : '禁用' }}
              </span>
            </td>
            <td>{{ formatDate(user.createdAt) }}</td>
            <td class="action-cell">
              <button
                class="btn-edit"
                @click="handleEditUser(user)"
              >
                编辑
              </button>
              <button
                class="btn-delete"
                @click="handleDeleteUser(user)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <div class="pagination-info">
        共 {{ total }} 条记录，第 {{ currentPage }} / {{ totalPages }} 页
      </div>
      <div class="pagination-controls">
        <button
          class="pagination-btn"
          :disabled="currentPage === 1"
          @click="handlePageChange(currentPage - 1)"
        >
          上一页
        </button>
        <button
          class="pagination-btn"
          :disabled="currentPage === totalPages"
          @click="handlePageChange(currentPage + 1)"
        >
          下一页
        </button>
      </div>
    </div>

    <div
      v-if="showModal"
      class="modal-overlay"
      @click="handleCloseModal"
    >
      <div
        class="modal-content"
        @click.stop
      >
        <div class="modal-header">
          <h3>{{ isEditMode ? '编辑用户' : '添加用户' }}</h3>
          <button
            class="modal-close"
            @click="handleCloseModal"
          >
            ×
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label>用户名 *</label>
              <input
                v-model="formData.username"
                type="text"
                required
              >
            </div>
            <div class="form-group">
              <label>邮箱 *</label>
              <input
                v-model="formData.email"
                type="email"
                required
              >
            </div>
            <div class="form-group">
              <label>手机号 *</label>
              <input
                v-model="formData.phone"
                type="tel"
                required
              >
            </div>
            <div class="form-group">
              <label>密码 *</label>
              <input
                v-model="formData.password"
                type="password"
                :required="!isEditMode"
              >
            </div>
            <div class="form-group">
              <label>角色 *</label>
              <select
                v-model="formData.role"
                required
              >
                <option value="admin">
                  管理员
                </option>
                <option value="user">
                  普通用户
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>状态 *</label>
              <select
                v-model="formData.status"
                required
              >
                <option value="active">
                  启用
                </option>
                <option value="inactive">
                  禁用
                </option>
              </select>
            </div>
            <div class="form-actions">
              <button
                type="button"
                class="btn-secondary"
                @click="handleCloseModal"
              >
                取消
              </button>
              <button
                type="submit"
                class="btn-primary"
              >
                确定
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const users = ref([
  {
    id: 1,
    username: 'admin',
    email: 'admin@example.com',
    phone: '13800138000',
    role: 'admin',
    status: 'active',
    createdAt: '2024-01-01T00:00:00Z'
  },
  {
    id: 2,
    username: 'user1',
    email: 'user1@example.com',
    phone: '13800138001',
    role: 'user',
    status: 'active',
    createdAt: '2024-01-02T00:00:00Z'
  },
  {
    id: 3,
    username: 'user2',
    email: 'user2@example.com',
    phone: '13800138002',
    role: 'user',
    status: 'inactive',
    createdAt: '2024-01-03T00:00:00Z'
  }
])

const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showModal = ref(false)
const isEditMode = ref(false)
const formData = ref({
  id: null,
  username: '',
  email: '',
  phone: '',
  password: '',
  role: 'user',
  status: 'active'
})

const filteredUsers = computed(() => {
  let result = users.value

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(
      user =>
        user.username.toLowerCase().includes(keyword) ||
        user.email.toLowerCase().includes(keyword) ||
        user.phone.includes(keyword)
    )
  }

  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return result.slice(start, end)
})

const total = computed(() => users.value.length)

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

const handleSearch = () => {
  currentPage.value = 1
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleAddUser = () => {
  isEditMode.value = false
  formData.value = {
    id: null,
    username: '',
    email: '',
    phone: '',
    password: '',
    role: 'user',
    status: 'active'
  }
  showModal.value = true
}

const handleEditUser = (user) => {
  isEditMode.value = true
  formData.value = {
    ...user,
    password: ''
  }
  showModal.value = true
}

const handleDeleteUser = (user) => {
  if (confirm(`确定要删除用户 ${user.username} 吗？`)) {
    const index = users.value.findIndex(u => u.id === user.id)
    if (index > -1) {
      users.value.splice(index, 1)
    }
  }
}

const handleCloseModal = () => {
  showModal.value = false
}

const handleSubmit = () => {
  if (isEditMode.value) {
    const index = users.value.findIndex(u => u.id === formData.value.id)
    if (index > -1) {
      users.value[index] = {
        ...formData.value,
        password: formData.value.password || users.value[index].password
      }
    }
  } else {
    const newUser = {
      ...formData.value,
      id: Math.max(...users.value.map(u => u.id)) + 1,
      createdAt: new Date().toISOString()
    }
    users.value.push(newUser)
  }
  showModal.value = false
}

onMounted(() => {
  console.log('用户管理组件已加载')
})
</script>

<style scoped>
.user-management {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #1a1a2e;
  font-size: 24px;
  font-weight: 600;
}

.btn-primary {
  padding: 10px 20px;
  background-color: #4a90e2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: #357abd;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(74, 144, 226, 0.3);
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
}

.table-container {
  overflow-x: auto;
  margin-bottom: 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table thead {
  background-color: #f5f5f5;
}

.data-table th {
  padding: 12px;
  text-align: left;
  font-weight: 600;
  color: #1a1a2e;
  border-bottom: 2px solid #e0e0e0;
}

.data-table td {
  padding: 12px;
  border-bottom: 1px solid #e0e0e0;
  color: #333;
}

.data-table tbody tr:hover {
  background-color: #f9f9f9;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.role-badge.admin {
  background-color: #e3f2fd;
  color: #1976d2;
}

.role-badge.user {
  background-color: #f5f5f5;
  color: #666;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background-color: #e8f5e9;
  color: #4caf50;
}

.status-badge.inactive {
  background-color: #ffebee;
  color: #f44336;
}

.action-cell {
  display: flex;
  gap: 8px;
}

.btn-edit {
  padding: 6px 12px;
  background-color: #ff9800;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-edit:hover {
  background-color: #f57c00;
}

.btn-delete {
  padding: 6px 12px;
  background-color: #f44336;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-delete:hover {
  background-color: #d32f2f;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.pagination-info {
  color: #666;
  font-size: 14px;
}

.pagination-controls {
  display: flex;
  gap: 10px;
}

.pagination-btn {
  padding: 8px 16px;
  background-color: #fff;
  color: #4a90e2;
  border: 1px solid #4a90e2;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.pagination-btn:hover:not(:disabled) {
  background-color: #4a90e2;
  color: #fff;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  border-radius: 8px;
  width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.modal-header h3 {
  margin: 0;
  color: #1a1a2e;
  font-size: 18px;
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-close:hover {
  color: #1a1a2e;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 24px;
}

.btn-secondary {
  flex: 1;
  padding: 10px 20px;
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}

.btn-primary {
  flex: 1;
}
</style>
