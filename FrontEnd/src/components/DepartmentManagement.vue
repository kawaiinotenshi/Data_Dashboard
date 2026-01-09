<template>
  <div class="department-management">
    <div class="page-header">
      <h2>部门信息管理</h2>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="search-bar">
        <input
          v-model="searchKeyword"
          type="text"
          :placeholder="'搜索部门名称'"
          class="search-input"
          @input="handleSearch"
        >
      </div>
      <button
        class="btn-primary"
        @click="handleAdd"
      >
        <span>+ 添加部门</span>
      </button>
    </div>

    <!-- 部门表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>部门编号</th>
            <th>部门名称</th>
            <th>部门描述</th>
            <th>员工数量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="department in filteredItems"
            :key="department.id"
          >
            <td>{{ department.id }}</td>
            <td>{{ department.name }}</td>
            <td>{{ department.description || '-' }}</td>
            <td>{{ getDepartmentEmployeeCount(department.id) }}</td>
            <td class="action-cell">
              <button
                class="btn-edit"
                @click="handleEdit(department)"
              >
                编辑
              </button>
              <button
                class="btn-delete"
                @click="handleDelete(department)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
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

    <!-- 模态框 -->
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
          <h3>{{ isEditMode ? '编辑' : '添加' }}部门</h3>
          <button
            class="modal-close"
            @click="handleCloseModal"
          >
            ×
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <!-- 部门表单 -->
            <div class="form-group">
              <label>部门名称 *</label>
              <input
                v-model="formData.name"
                type="text"
                required
                placeholder="请输入部门名称"
              >
            </div>
            <div class="form-group">
              <label>部门描述</label>
              <textarea
                v-model="formData.description"
                rows="3"
                placeholder="请输入部门描述"
              />
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
import api from '../api'

const departments = ref([])
const employees = ref([])
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showModal = ref(false)
const isEditMode = ref(false)
const formData = ref({})

// 过滤后的列表
const filteredItems = computed(() => {
  let items = departments.value
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    items = items.filter(
      dept => dept.name?.toLowerCase().includes(keyword)
    )
  }

  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return items.slice(start, end)
})

const total = computed(() => departments.value.length)
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

// 获取部门员工数量
const getDepartmentEmployeeCount = (departmentId) => {
  return employees.value.filter(emp => emp.departmentId === departmentId).length
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
}

// 分页
const handlePageChange = (page) => {
  currentPage.value = page
}

// 加载数据
const loadData = async () => {
  try {
    const [departmentRes, employeeRes] = await Promise.all([
      api.department.list(),
      api.employee.list()
    ])
    
    if (departmentRes.code === 200) {
      departments.value = departmentRes.data
    }
    
    if (employeeRes.code === 200) {
      employees.value = employeeRes.data
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

// 添加
const handleAdd = () => {
  isEditMode.value = false
  formData.value = {
    name: '',
    description: ''
  }
  showModal.value = true
}

// 编辑
const handleEdit = (item) => {
  isEditMode.value = true
  formData.value = { ...item }
  showModal.value = true
}

// 删除
const handleDelete = async (item) => {
  if (!confirm(`确定要删除部门${item.name}吗？`)) {
    return
  }

  try {
    const res = await api.department.delete(item.id)

    if (res.code === 200) {
      await loadData() // 重新加载数据
      alert('部门删除成功')
    } else {
      alert(`部门删除失败: ${res.msg}`)
    }
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败，请重试')
  }
}

// 关闭模态框
const handleCloseModal = () => {
  showModal.value = false
}

// 提交表单
const handleSubmit = async () => {
  try {
    let res
    
    if (isEditMode.value) {
      res = await api.department.update(formData.value.id, formData.value)
    } else {
      res = await api.department.create(formData.value)
    }

    if (res.code === 200) {
      await loadData() // 重新加载数据
      handleCloseModal()
      alert(`部门${isEditMode.value ? '编辑' : '添加'}成功`)
    } else {
      alert(`${isEditMode.value ? '编辑' : '添加'}失败: ${res.msg}`)
    }
  } catch (error) {
    console.error('操作失败:', error)
    alert('操作失败，请重试')
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.department-management {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.search-bar {
  flex: 1;
  max-width: 300px;
}

.search-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-input:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.1);
}

.btn-primary {
  padding: 8px 16px;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.btn-primary:hover {
  background-color: #3a7bc8;
}

.btn-secondary {
  padding: 8px 16px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s ease;
  margin-right: 10px;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}

.table-container {
  margin-bottom: 20px;
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.data-table th,
.data-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.data-table td {
  font-size: 14px;
  color: #666;
}

.action-cell {
  display: flex;
  gap: 8px;
}

.btn-edit {
  padding: 4px 10px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.3s ease;
}

.btn-edit:hover {
  background-color: #218838;
}

.btn-delete {
  padding: 4px 10px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.3s ease;
}

.btn-delete:hover {
  background-color: #c82333;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  font-size: 14px;
  color: #666;
}

.pagination-info {
  flex: 1;
}

.pagination-controls {
  display: flex;
  gap: 8px;
}

.pagination-btn {
  padding: 4px 12px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.3s ease;
}

.pagination-btn:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.pagination-btn:disabled {
  opacity: 0.6;
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
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.modal-close:hover {
  background-color: #f0f0f0;
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>