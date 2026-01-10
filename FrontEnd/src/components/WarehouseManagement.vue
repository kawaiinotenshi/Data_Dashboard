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
      <el-table-column prop="location" label="位置">
        <template #default="scope">
          {{ scope.row.location || '未分配' }}
        </template>
      </el-table-column>
      <el-table-column prop="area" label="面积">
        <template #default="scope">
          {{ scope.row.area || '未分配' }}
        </template>
      </el-table-column>
      <el-table-column prop="capacity" label="容量">
        <template #default="scope">
          {{ scope.row.capacity || '未分配' }}
        </template>
      </el-table-column>
      <el-table-column prop="throughput" label="吞吐量">
        <template #default="scope">
          {{ scope.row.throughput || '未分配' }}
        </template>
      </el-table-column>
      <el-table-column prop="utilizationRate" label="利用率" width="100">
        <template #default="scope">
          <el-progress
            type="line"
            :percentage="scope.row.utilizationRate ? Number(scope.row.utilizationRate) * 100 : 0"
            :color="getProgressColor(scope.row.utilizationRate ? Number(scope.row.utilizationRate) * 100 : 0)"
            :stroke-width="8"
            :show-text="true"
          />
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag
            :type="scope.row.status === 0 ? 'success' : scope.row.status === 1 ? 'warning' : 'danger'"
            size="small"
          >
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="创建时间" width="180">
        <template #default="scope">
          {{ scope.row.createdTime || '未分配' }}
        </template>
      </el-table-column>
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
        <el-form-item label="面积" prop="area">
          <el-input-number
            v-model="warehouseForm.area"
            :min="0"
            :step="0.1"
            placeholder="请输入面积"
          />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input-number
            v-model="warehouseForm.capacity"
            :min="0"
            :step="100"
            placeholder="请输入容量"
          />
        </el-form-item>
        <el-form-item label="吞吐量" prop="throughput">
          <el-input-number
            v-model="warehouseForm.throughput"
            :min="0"
            :step="10"
            placeholder="请输入吞吐量"
          />
        </el-form-item>
        <el-form-item label="利用率" prop="utilizationRate">
          <el-input-number
            v-model="warehouseForm.utilizationRate"
            :min="0"
            :max="1"
            :step="0.01"
            placeholder="请输入利用率（0-1之间）"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="warehouseForm.status" placeholder="请选择状态">
            <el-option label="正常" value="0" />
            <el-option label="低库存警告" value="1" />
            <el-option label="高库存警告" value="2" />
          </el-select>
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
  area: 0,
  capacity: 0,
  throughput: 0,
  utilizationRate: 0,
  status: 0
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入仓库名称', trigger: 'blur' },
    { min: 2, max: 50, message: '仓库名称长度应在2-50个字符之间', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入仓库位置', trigger: 'blur' },
    { min: 5, max: 100, message: '仓库位置长度应在5-100个字符之间', trigger: 'blur' }
  ],
  area: [
    { required: true, message: '请输入面积', trigger: 'blur' },
    { type: 'number', min: 0.1, message: '面积必须大于0', trigger: 'blur' }
  ],
  capacity: [
    { required: true, message: '请输入容量', trigger: 'blur' },
    { type: 'number', min: 100, message: '容量必须大于等于100', trigger: 'blur' }
  ],
  throughput: [
    { required: true, message: '请输入吞吐量', trigger: 'blur' },
    { type: 'number', min: 0, message: '吞吐量不能为负数', trigger: 'blur' }
  ],
  utilizationRate: [
    { required: true, message: '请输入利用率', trigger: 'blur' },
    { type: 'number', min: 0, max: 1, message: '利用率必须在0-1之间', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 过滤后的仓库数据
const filteredWarehouses = computed(() => {
  if (!searchQuery.value) {
    return warehouses.value
  }
  return warehouses.value.filter(warehouse => 
    warehouse.name.includes(searchQuery.value) ||
    warehouse.location.includes(searchQuery.value)
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

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '正常',
    1: '低库存警告',
    2: '高库存警告'
  }
  return statusMap[status] || '未知状态'
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
    area: 0,
    capacity: 0,
    throughput: 0,
    utilizationRate: 0,
    status: 0
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
    
    // 将状态转换为数字类型
    const data = {
      ...warehouseForm.value,
      status: Number(warehouseForm.value.status)
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