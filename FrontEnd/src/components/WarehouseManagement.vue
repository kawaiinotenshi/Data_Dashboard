<template>
  <div class="warehouse-management">
    <h2>仓库管理</h2>
    
    <!-- 搜索和添加按钮 -->
    <div class="top-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索仓库名称"
        style="width: 200px; margin-right: 10px"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button type="success" @click="handleAdd">新增仓库</el-button>
    </div>

    <!-- 仓库表格 -->
    <el-table
      v-loading="loading"
      :data="filteredWarehouses"
      style="width: 100%"
      stripe
      border
    >
      <el-table-column prop="id" label="仓库ID" width="80" />
      <el-table-column prop="name" label="仓库名称" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="totalCapacity" label="总容量" />
      <el-table-column prop="usedCapacity" label="已用容量" />
      <el-table-column prop="remainingCapacity" label="剩余容量" />
      <el-table-column prop="utilizationRate" label="利用率" width="100">
        <template #default="scope">
          <el-progress
            type="line"
            :percentage="Number(scope.row.utilizationRate)"
            :color="getProgressColor(Number(scope.row.utilizationRate))"
            :stroke-width="8"
            :show-text="true"
          />
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag
            :type="scope.row.status === '正常' ? 'success' : 'danger'"
            size="small"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="managerName" label="负责人" />
      <el-table-column prop="contactNumber" label="联系电话" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredWarehouses.length"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑仓库对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        ref="warehouseFormRef"
        :model="warehouseForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="仓库名称" prop="name">
          <el-input v-model="warehouseForm.name" placeholder="请输入仓库名称" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="warehouseForm.location" placeholder="请输入仓库位置" />
        </el-form-item>
        <el-form-item label="总容量" prop="totalCapacity">
          <el-input-number
            v-model="warehouseForm.totalCapacity"
            :min="0"
            :step="100"
            placeholder="请输入总容量"
          />
        </el-form-item>
        <el-form-item label="已用容量" prop="usedCapacity">
          <el-input-number
            v-model="warehouseForm.usedCapacity"
            :min="0"
            :max="warehouseForm.totalCapacity"
            :step="100"
            placeholder="请输入已用容量"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="warehouseForm.status" placeholder="请选择状态">
            <el-option label="正常" value="正常" />
            <el-option label="异常" value="异常" />
            <el-option label="维护中" value="维护中" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="managerName">
          <el-input v-model="warehouseForm.managerName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactNumber">
          <el-input v-model="warehouseForm.contactNumber" placeholder="请输入联系电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { api } from '../api'

// 表格数据
const warehouses = ref([])
const loading = ref(false)
const searchQuery = ref('')

// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框数据
const dialogVisible = ref(false)
const dialogTitle = ref('')
const warehouseFormRef = ref(null)
const warehouseForm = ref({
  id: null,
  name: '',
  location: '',
  totalCapacity: 0,
  usedCapacity: 0,
  status: '正常',
  managerName: '',
  contactNumber: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入仓库名称', trigger: 'blur' },
    { min: 2, max: 50, message: '仓库名称长度应在2-50个字符之间', trigger: 'blur' },
    {
      validator: async (rule, value, callback) => {
        try {
          const response = await api.warehouse.checkNameUnique(value, warehouseForm.value.id)
          if (!response.data) {
            callback(new Error('该仓库名称已存在'))
          } else {
            callback()
          }
        } catch (error) {
          callback(new Error('验证仓库名称失败'))
        }
      },
      trigger: 'blur'
    }
  ],
  location: [
    { required: true, message: '请输入仓库位置', trigger: 'blur' },
    { min: 5, max: 100, message: '仓库位置长度应在5-100个字符之间', trigger: 'blur' }
  ],
  totalCapacity: [
    { required: true, message: '请输入总容量', trigger: 'blur' },
    { type: 'number', min: 100, message: '总容量必须大于等于100', trigger: 'blur' }
  ],
  usedCapacity: [
    { required: true, message: '请输入已用容量', trigger: 'blur' },
    { type: 'number', min: 0, message: '已用容量不能为负数', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (warehouseForm.value.totalCapacity > 0 && value > warehouseForm.value.totalCapacity) {
          callback(new Error('已用容量不能超过总容量'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  managerName: [
    { required: true, message: '请输入负责人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '负责人姓名长度应在2-20个字符之间', trigger: 'blur' }
  ],
  contactNumber: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 过滤后的仓库数据
const filteredWarehouses = computed(() => {
  if (!searchQuery.value) {
    return warehouses.value
  }
  return warehouses.value.filter(warehouse => 
    warehouse.name.includes(searchQuery.value) ||
    warehouse.location.includes(searchQuery.value) ||
    warehouse.managerName.includes(searchQuery.value)
  )
})

// 分页数据
const paginatedWarehouses = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  return filteredWarehouses.value.slice(startIndex, startIndex + pageSize.value)
})

// 获取进度条颜色
const getProgressColor = (percentage) => {
  if (percentage < 50) return '#67C23A'
  if (percentage < 80) return '#E6A23C'
  return '#F56C6C'
}

// 加载仓库数据
const loadWarehouses = async () => {
  loading.value = true
  try {
    const response = await api.warehouse.list()
    warehouses.value = response.data || []
  } catch (error) {
    ElMessage.error('加载仓库数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
}

// 新增仓库
const handleAdd = () => {
  dialogTitle.value = '新增仓库'
  warehouseForm.value = {
    id: null,
    name: '',
    location: '',
    totalCapacity: 0,
    usedCapacity: 0,
    status: '正常',
    managerName: '',
    contactNumber: ''
  }
  dialogVisible.value = true
}

// 编辑仓库
const handleEdit = (row) => {
  dialogTitle.value = '编辑仓库'
  warehouseForm.value = { ...row }
  dialogVisible.value = true
}

// 删除仓库
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该仓库吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await api.warehouse.delete(id)
    ElMessage.success('删除成功')
    loadWarehouses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!warehouseFormRef.value) return
  
  try {
    await warehouseFormRef.value.validate()
    
    // 计算剩余容量
    const remainingCapacity = warehouseForm.value.totalCapacity - warehouseForm.value.usedCapacity
    const data = {
      ...warehouseForm.value,
      remainingCapacity
    }
    
    if (data.id) {
      // 编辑
      await api.warehouse.update(data.id, data)
      ElMessage.success('更新成功')
    } else {
      // 新增
      await api.warehouse.create(data)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadWarehouses()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  }
}

// 分页事件
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

// 初始化数据
onMounted(() => {
  loadWarehouses()
})
</script>

<style scoped>
.warehouse-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.top-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>