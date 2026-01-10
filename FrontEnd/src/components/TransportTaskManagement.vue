<template>
  <div class="transport-task-management">
    <h2>物流任务管理</h2>
    
    <!-- 搜索和添加按钮 -->
    <div class="top-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索任务名称"
        style="width: 200px; margin-right: 10px"
      />
      <el-select
        v-model="statusFilter"
        placeholder="按状态筛选"
        style="width: 150px; margin-right: 10px"
      >
        <el-option label="全部" value="" />
        <el-option label="待分配" value="待分配" />
        <el-option label="运输中" value="运输中" />
        <el-option label="已完成" value="已完成" />
        <el-option label="异常" value="异常" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button type="success" @click="handleAdd">新增任务</el-button>
    </div>

    <!-- 物流任务表格 -->
    <el-table
      v-loading="loading"
      :data="paginatedTasks"
      style="width: 100%"
      stripe
      border
    >
      <el-table-column prop="id" label="任务ID" width="80" />
      <el-table-column prop="taskName" label="任务名称" />
      <el-table-column prop="origin" label="起始地" width="150" />
      <el-table-column prop="destination" label="目的地" width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag
            :type="getStatusType(scope.row.status)"
            size="small"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="vehicleType" label="车辆类型" width="100" />
      <el-table-column prop="driverName" label="驾驶员" />
      <el-table-column prop="transportDate" label="运输日期" width="150" />
      <el-table-column prop="estimatedArrivalTime" label="预计到达" width="150" />
      <el-table-column prop="actualArrivalTime" label="实际到达" width="150" />
      <el-table-column prop="estimatedCost" label="预计成本" width="120" />
      <el-table-column prop="actualCost" label="实际成本" width="120" />
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
        :total="filteredTasks.length"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑任务对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        ref="taskFormRef"
        :model="taskForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="taskForm.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="起始地" prop="origin">
          <el-input v-model="taskForm.origin" placeholder="请输入起始地" />
        </el-form-item>
        <el-form-item label="目的地" prop="destination">
          <el-input v-model="taskForm.destination" placeholder="请输入目的地" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="taskForm.status" placeholder="请选择状态">
            <el-option label="待分配" value="待分配" />
            <el-option label="运输中" value="运输中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="异常" value="异常" />
          </el-select>
        </el-form-item>
        <el-form-item label="车辆类型" prop="vehicleType">
          <el-select v-model="taskForm.vehicleType" placeholder="请选择车辆类型">
            <el-option label="货车" value="货车" />
            <el-option label="卡车" value="卡车" />
            <el-option label="集装箱" value="集装箱" />
            <el-option label="冷藏车" value="冷藏车" />
          </el-select>
        </el-form-item>
        <el-form-item label="驾驶员" prop="driverName">
          <el-input v-model="taskForm.driverName" placeholder="请输入驾驶员姓名" />
        </el-form-item>
        <el-form-item label="运输日期" prop="transportDate">
          <el-date-picker
            v-model="taskForm.transportDate"
            type="date"
            placeholder="选择运输日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预计到达" prop="estimatedArrivalTime">
          <el-date-picker
            v-model="taskForm.estimatedArrivalTime"
            type="datetime"
            placeholder="选择预计到达时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="实际到达" prop="actualArrivalTime">
          <el-date-picker
            v-model="taskForm.actualArrivalTime"
            type="datetime"
            placeholder="选择实际到达时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预计成本" prop="estimatedCost">
          <el-input-number
            v-model="taskForm.estimatedCost"
            :min="0"
            :step="100"
            :precision="2"
            placeholder="请输入预计成本"
          />
        </el-form-item>
        <el-form-item label="实际成本" prop="actualCost">
          <el-input-number
            v-model="taskForm.actualCost"
            :min="0"
            :step="100"
            :precision="2"
            placeholder="请输入实际成本"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="taskForm.remark"
            type="textarea"
            rows="3"
            placeholder="请输入备注信息"
          />
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
const tasks = ref([])
const loading = ref(false)
const searchQuery = ref('')
const statusFilter = ref('')

// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框数据
const dialogVisible = ref(false)
const dialogTitle = ref('新增任务')
const taskFormRef = ref(null)
const taskForm = ref({
  id: null,
  taskName: '',
  origin: '',
  destination: '',
  status: '待分配',
  vehicleType: '',
  driverName: '',
  transportDate: null,
  estimatedArrivalTime: null,
  actualArrivalTime: null,
  estimatedCost: 0,
  actualCost: 0,
  remark: ''
})

// 表单验证规则
const rules = {
  taskName: [
    { required: true, message: '请输入任务名称', trigger: 'blur' },
    { min: 5, max: 100, message: '任务名称长度应在5-100个字符之间', trigger: 'blur' }
  ],
  origin: [
    { required: true, message: '请输入起始地', trigger: 'blur' },
    { min: 2, max: 50, message: '起始地长度应在2-50个字符之间', trigger: 'blur' }
  ],
  destination: [
    { required: true, message: '请输入目的地', trigger: 'blur' },
    { min: 2, max: 50, message: '目的地长度应在2-50个字符之间', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === taskForm.value.origin) {
          callback(new Error('目的地不能与起始地相同'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  vehicleType: [{ required: true, message: '请选择车辆类型', trigger: 'change' }],
  driverName: [
    { required: true, message: '请输入驾驶员姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '驾驶员姓名长度应在2-20个字符之间', trigger: 'blur' }
  ],
  transportDate: [
    { required: true, message: '请选择运输日期', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (value && value < new Date(new Date().setHours(0, 0, 0, 0))) {
          callback(new Error('运输日期不能早于今天'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  estimatedArrivalTime: [
    { required: true, message: '请选择预计到达时间', trigger: 'change' },
    {
      validator: (rule, value, callback) => {
        if (taskForm.value.transportDate && value) {
          const transportDate = new Date(taskForm.value.transportDate)
          const estimatedTime = new Date(value)
          transportDate.setHours(0, 0, 0, 0)
          estimatedTime.setHours(0, 0, 0, 0)
          
          if (estimatedTime < transportDate) {
            callback(new Error('预计到达时间不能早于运输日期'))
          } else if (value <= new Date()) {
            callback(new Error('预计到达时间不能早于当前时间'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  actualArrivalTime: [
    {
      validator: (rule, value, callback) => {
        if (value && taskForm.value.estimatedArrivalTime) {
          const actualTime = new Date(value)
          const estimatedTime = new Date(taskForm.value.estimatedArrivalTime)
          
          if (actualTime < estimatedTime - 86400000) { // 不能早于预计时间24小时以上
            callback(new Error('实际到达时间不能早于预计时间24小时以上'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  estimatedCost: [
    { required: true, message: '请输入预计成本', trigger: 'blur' },
    { type: 'number', min: 100, message: '预计成本必须大于等于100', trigger: 'blur' }
  ],
  actualCost: [
    {
      validator: (rule, value, callback) => {
        if (value !== null && value !== undefined && value < 0) {
          callback(new Error('实际成本不能为负数'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取状态对应的标签类型
const getStatusType = (status) => {
  switch (status) {
    case '待分配': return 'info'
    case '运输中': return 'warning'
    case '已完成': return 'success'
    case '异常': return 'danger'
    default: return ''
  }
}

// 过滤后的任务数据
const filteredTasks = computed(() => {
  let result = tasks.value
  
  // 按名称搜索
  if (searchQuery.value) {
    result = result.filter(task => 
      task.taskName.includes(searchQuery.value) ||
      task.origin.includes(searchQuery.value) ||
      task.destination.includes(searchQuery.value)
    )
  }
  
  // 按状态筛选
  if (statusFilter.value) {
    result = result.filter(task => task.status === statusFilter.value)
  }
  
  return result
})

// 分页数据
const paginatedTasks = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  return filteredTasks.value.slice(startIndex, startIndex + pageSize.value)
})

// 加载任务数据
const loadTasks = async () => {
  loading.value = true
  try {
    const response = await api.transport.list()
    tasks.value = response.data || []
  } catch (error) {
    ElMessage.error('加载物流任务数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
}

// 新增任务
const handleAdd = () => {
  dialogTitle.value = '新增任务'
  taskForm.value = {
    id: null,
    taskName: '',
    origin: '',
    destination: '',
    status: '待分配',
    vehicleType: '',
    driverName: '',
    transportDate: null,
    estimatedArrivalTime: null,
    actualArrivalTime: null,
    estimatedCost: 0,
    actualCost: 0,
    remark: ''
  }
  dialogVisible.value = true
}

// 编辑任务
const handleEdit = (row) => {
  dialogTitle.value = '编辑任务'
  taskForm.value = { ...row }
  // 将日期字符串转换为Date对象
  if (row.transportDate) {
    taskForm.value.transportDate = new Date(row.transportDate)
  }
  if (row.estimatedArrivalTime) {
    taskForm.value.estimatedArrivalTime = new Date(row.estimatedArrivalTime)
  }
  if (row.actualArrivalTime) {
    taskForm.value.actualArrivalTime = new Date(row.actualArrivalTime)
  }
  dialogVisible.value = true
}

// 删除任务
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该物流任务吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await api.transport.delete(id)
    ElMessage.success('删除成功')
    loadTasks()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!taskFormRef.value) return
  
  try {
    await taskFormRef.value.validate()
    
    const data = { ...taskForm.value }
    
    // 数据清洗和格式化
    // 将Date对象转换为字符串
    if (data.transportDate) {
      data.transportDate = data.transportDate.toISOString().split('T')[0]
    } else {
      delete data.transportDate
    }
    
    if (data.estimatedArrivalTime) {
      data.estimatedArrivalTime = data.estimatedArrivalTime.toISOString()
    } else {
      delete data.estimatedArrivalTime
    }
    
    if (data.actualArrivalTime) {
      data.actualArrivalTime = data.actualArrivalTime.toISOString()
    } else {
      delete data.actualArrivalTime
    }
    
    // 确保成本字段是数字
    data.estimatedCost = Number(data.estimatedCost) || 0
    if (data.actualCost !== null && data.actualCost !== undefined) {
      data.actualCost = Number(data.actualCost)
    } else {
      delete data.actualCost
    }
    
    // 确保起始地和目的地是有效的城市名称
    if (!data.origin || data.origin.trim() === '') {
      ElMessage.error('请输入有效的起始地')
      return
    }
    
    if (!data.destination || data.destination.trim() === '') {
      ElMessage.error('请输入有效的目的地')
      return
    }
    
    data.origin = data.origin.trim()
    data.destination = data.destination.trim()
    
    if (data.id) {
      // 编辑
      const response = await api.transport.update(data.id, data)
      if (response.code === 200) {
        ElMessage.success('更新成功')
      } else {
        ElMessage.error(`更新失败: ${response.message || '未知错误'}`)
      }
    } else {
      // 新增
      const response = await api.transport.create(data)
      if (response.code === 200) {
        ElMessage.success('创建成功')
      } else {
        ElMessage.error(`创建失败: ${response.message || '未知错误'}`)
      }
    }
    
    dialogVisible.value = false
    loadTasks()
  } catch (error) {
    if (error !== false) {
      console.error('表单提交错误:', error)
      if (error.response) {
        // API错误
        const message = error.response.data?.message || error.response.statusText || '操作失败'
        ElMessage.error(`操作失败: ${message}`)
      } else if (error.message) {
        // 其他错误
        ElMessage.error(`操作失败: ${error.message}`)
      } else {
        ElMessage.error('操作失败，请稍后重试')
      }
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
  loadTasks()
})
</script>

<style scoped>
.transport-task-management {
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