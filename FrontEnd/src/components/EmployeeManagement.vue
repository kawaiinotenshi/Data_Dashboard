<template>
  <div class="employee-management">
    <div class="page-header">
      <h2>员工信息管理</h2>
    </div>

    <!-- 统计图表 -->
    <div class="charts-container">
      <div class="chart-card">
        <h3>部门人数统计</h3>
        <div
          id="departmentChart"
          class="chart-content"
          style="height: 200px;"
        />
      </div>
      <div class="chart-card">
        <h3>员工薪资分布</h3>
        <div
          id="salaryChart"
          class="chart-content"
          style="height: 200px;"
        />
      </div>
    </div>

    <!-- 标签页 -->
    <div class="tab-container">
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'employee' }"
        @click="activeTab = 'employee'"
      >
        员工管理
      </button>
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'department' }"
        @click="activeTab = 'department'"
      >
        部门管理
      </button>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="search-bar">
        <input
          v-model="searchKeyword"
          type="text"
          :placeholder="`搜索${activeTab === 'employee' ? '员工姓名' : '部门名称'}`"
          class="search-input"
          @input="handleSearch"
        >
      </div>
      <button
        class="btn-primary"
        @click="handleAdd"
      >
        <span>+ 添加{{ activeTab === 'employee' ? '员工' : '部门' }}</span>
      </button>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <!-- 员工表格 -->
      <table
        v-if="activeTab === 'employee'"
        class="data-table"
      >
        <thead>
          <tr>
            <th>员工编号</th>
            <th>员工姓名</th>
            <th>所属部门</th>
            <th>薪资</th>
            <th>入职日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="employee in filteredItems"
            :key="employee.id"
          >
            <td>{{ employee.id }}</td>
            <td>{{ employee.name }}</td>
            <td>{{ employee.departmentName || '无部门' }}</td>
            <td>¥{{ employee.salary.toFixed(2) }}</td>
            <td>{{ formatDate(employee.hireDate) }}</td>
            <td class="action-cell">
              <button
                class="btn-edit"
                @click="handleEdit(employee)"
              >
                编辑
              </button>
              <button
                class="btn-delete"
                @click="handleDelete(employee)"
              >
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 部门表格 -->
      <table
        v-else
        class="data-table"
      >
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
          <h3>{{ isEditMode ? '编辑' : '添加' }}{{ activeTab === 'employee' ? '员工' : '部门' }}</h3>
          <button
            class="modal-close"
            @click="handleCloseModal"
          >
            ×
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <!-- 员工表单 -->
            <template v-if="activeTab === 'employee'">
              <div class="form-group">
                <label>员工姓名 *</label>
                <input
                  v-model="formData.name"
                  type="text"
                  required
                  placeholder="请输入员工姓名"
                >
              </div>
              <div class="form-group">
                <label>所属部门</label>
                <select v-model="formData.departmentId">
                  <option value="">
                    无部门
                  </option>
                  <option
                    v-for="dept in departments"
                    :key="dept.id"
                    :value="dept.id"
                  >
                    {{ dept.name }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label>薪资 *</label>
                <input
                  v-model="formData.salary"
                  type="number"
                  step="0.01"
                  min="0"
                  required
                  placeholder="请输入薪资"
                >
              </div>
              <div class="form-group">
                <label>入职日期 *</label>
                <input
                  v-model="formData.hireDate"
                  type="date"
                  required
                >
              </div>
            </template>

            <!-- 部门表单 -->
            <template v-else>
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
            </template>

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
import { ref, computed, onMounted, watch } from 'vue'
import api from '../api'

const activeTab = ref('employee')
const employees = ref([])
const departments = ref([])
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showModal = ref(false)
const isEditMode = ref(false)
const formData = ref({})

// 过滤后的列表
const filteredItems = computed(() => {
  let items = activeTab.value === 'employee' ? employees.value : departments.value
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    
    if (activeTab.value === 'employee') {
      items = items.filter(
        emp =>
          emp.name?.toLowerCase().includes(keyword) ||
          emp.email?.toLowerCase().includes(keyword) ||
          emp.phone?.includes(keyword) ||
          emp.departmentName?.toLowerCase().includes(keyword)
      )
    } else {
      items = items.filter(
        dept => dept.name?.toLowerCase().includes(keyword)
      )
    }
  }

  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return items.slice(start, end)
})

const total = computed(() => {
  return activeTab.value === 'employee' ? employees.value.length : departments.value.length
})

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

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
    const [employeeRes, departmentRes] = await Promise.all([
      api.employee.list(),
      api.department.list()
    ])
    
    if (employeeRes.code === 200) {
      employees.value = employeeRes.data
    }
    
    if (departmentRes.code === 200) {
      departments.value = departmentRes.data
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

// 添加
const handleAdd = () => {
  isEditMode.value = false
  formData.value = activeTab.value === 'employee' ? {
    name: '',
    email: '',
    phone: '',
    departmentId: '',
    position: '',
    salary: 0,
    hireDate: new Date().toISOString().split('T')[0]
  } : {
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
  if (!confirm(`确定要删除${activeTab.value === 'employee' ? '员工' : '部门'}${item.name}吗？`)) {
    return
  }

  try {
    let res
    if (activeTab.value === 'employee') {
      res = await api.employee.delete(item.id)
    } else {
      res = await api.department.delete(item.id)
    }

    if (res.code === 200) {
      await loadData() // 重新加载数据
      alert(`${activeTab.value === 'employee' ? '员工' : '部门'}删除成功`)
    } else {
      alert(`${activeTab.value === 'employee' ? '员工' : '部门'}删除失败: ${res.msg}`)
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
    
    if (activeTab.value === 'employee') {
      if (isEditMode.value) {
        res = await api.employee.update(formData.value.id, formData.value)
      } else {
        res = await api.employee.create(formData.value)
      }
    } else {
      if (isEditMode.value) {
        res = await api.department.update(formData.value.id, formData.value)
      } else {
        res = await api.department.create(formData.value)
      }
    }

    if (res.code === 200) {
      await loadData() // 重新加载数据
      showModal.value = false
      alert(`${isEditMode.value ? '编辑' : '添加'}${activeTab.value === 'employee' ? '员工' : '部门'}成功`)
    } else {
      alert(`${isEditMode.value ? '编辑' : '添加'}${activeTab.value === 'employee' ? '员工' : '部门'}失败: ${res.msg}`)
    }
  } catch (error) {
    console.error('提交失败:', error)
    alert('操作失败，请重试')
  }
}

// 监听标签切换
watch(activeTab, () => {
  currentPage.value = 1
  searchKeyword.value = ''
})

// 组件挂载时加载数据
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.employee-management {
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

.tab-container {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 2px solid #e0e0e0;
}

.tab-btn {
  padding: 10px 20px;
  background: none;
  border: none;
  font-size: 16px;
  font-weight: 500;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-right: 20px;
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: #4a90e2;
  border-bottom-color: #4a90e2;
}

.tab-btn:hover {
  color: #4a90e2;
}

/* 图表容器样式 */
.charts-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.chart-card {
  flex: 1;
  min-width: 300px;
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chart-card h3 {
  margin-top: 0;
  margin-bottom: 16px;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

/* 操作栏样式 */
.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 16px;
}

.search-bar {
  flex: 1;
  min-width: 200px;
  margin-bottom: 0;
}

.btn-primary {
  min-width: 120px;
  max-width: 150px;
  width: auto;
  flex: none;
  height: 60px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-group textarea {
  resize: vertical;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
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
</style>