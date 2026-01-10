<template>
  <div class="order-management">
    <el-page-header
      @back="$router.go(-1)"
      content="订单管理"
    />

    <el-card>
      <div class="card-header">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索订单编号、客户名称"
          style="width: 300px; margin-right: 10px;"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleAddOrder">
          <el-icon><Plus /></el-icon>
          新增订单
        </el-button>
        <el-upload
          v-model:file-list="fileList"
          class="upload-demo"
          drag
          action=""
          :auto-upload="false"
          :on-change="handleFileChange"
          :before-upload="beforeUpload"
          accept=".xlsx,.xls"
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持上传 .xlsx / .xls 格式的订单数据文件
            </div>
          </template>
        </el-upload>
        <el-button type="success" @click="handleImportExcel" :disabled="!fileList.length">
          <el-icon><Document /></el-icon>
          导入Excel
        </el-button>
      </div>

      <el-table
        :data="filteredOrders"
        style="width: 100%"
        border
        stripe
        v-loading="loading"
      >
        <el-table-column prop="id" label="订单ID" width="80" />
        <el-table-column prop="orderNumber" label="订单编号" width="180" />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="orderDate" label="订单日期" width="150" />
        <el-table-column prop="totalAmount" label="订单金额" width="120" :formatter="formatCurrency" />
        <el-table-column prop="status" label="订单状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态" width="120">
          <template #default="scope">
            <el-tag :type="getPaymentStatusType(scope.row.paymentStatus)">
              {{ scope.row.paymentStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditOrder(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteOrder(scope.row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 订单表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditMode ? '编辑订单' : '新增订单'"
      width="600px"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-form-item label="订单编号" prop="orderNumber" v-if="isEditMode">
          <el-input v-model="formData.orderNumber" placeholder="订单编号" readonly />
        </el-form-item>
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="formData.customerId" placeholder="请选择客户">
            <el-option
              v-for="customer in customers"
              :key="customer.id"
              :label="customer.name"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库" prop="warehouseId">
          <el-select v-model="formData.warehouseId" placeholder="请选择仓库">
            <el-option
              v-for="warehouse in warehouses"
              :key="warehouse.id"
              :label="warehouse.name"
              :value="warehouse.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品" prop="productId">
          <el-select v-model="formData.productId" placeholder="请选择产品" @change="handleProductChange">
            <el-option
              v-for="product in products"
              :key="product.id"
              :label="product.name"
              :value="product.id"
              :data-price="product.price"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number
            v-model="formData.quantity"
            :min="1"
            :step="1"
            style="width: 100%"
            placeholder="请输入数量"
            @change="handleQuantityChange"
          />
        </el-form-item>
        <el-form-item label="订单日期" prop="orderDate">
          <el-date-picker
            v-model="formData.orderDate"
            type="date"
            placeholder="选择订单日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="订单金额" prop="totalAmount">
          <el-input-number
            v-model="formData.totalAmount"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="订单金额"
            readonly
          />
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="formData.status" placeholder="选择订单状态">
            <el-option label="待处理" value="待处理" />
            <el-option label="已确认" value="已确认" />
            <el-option label="已发货" value="已发货" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态" prop="paymentStatus">
          <el-select v-model="formData.paymentStatus" placeholder="选择支付状态">
            <el-option label="未支付" value="未支付" />
            <el-option label="已支付" value="已支付" />
            <el-option label="部分支付" value="部分支付" />
            <el-option label="已退款" value="已退款" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search, Plus, Edit, Delete, UploadFilled, Document } from '@element-plus/icons-vue'
import { ElMessage, ElLoading } from 'element-plus'
import { api } from '../api'

// 响应式数据
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const dialogVisible = ref(false)
const isEditMode = ref(false)
const formData = ref({})
const formRef = ref(null)
const orders = ref([])
const fileList = ref([])

// 选择器数据源
const warehouses = ref([])
const customers = ref([])
const products = ref([])

// 表单规则 - 使用计算属性动态生成
const formRules = computed(() => {
  return {
    orderNumber: [
      { required: isEditMode.value, message: '订单编号不能为空', trigger: 'blur' },
      { pattern: /^ORD\d{12}$/, message: '订单编号格式应为ORD+12位数字', trigger: 'blur' },
      {
        validator: async (rule, value, callback) => {
          if (isEditMode.value && value) {
            try {
              const response = await api.order.checkNumberUnique(value, formData.value.id)
              if (!response.data) {
                callback(new Error('该订单编号已存在'))
              } else {
                callback()
              }
            } catch (error) {
              callback(new Error('验证订单编号失败'))
            }
          } else {
            callback()
          }
        },
        trigger: 'blur'
      }
    ],
    customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
    warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
    productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
    quantity: [
      { required: true, message: '请输入数量', trigger: 'blur' },
      { type: 'number', min: 1, message: '数量必须大于0', trigger: 'blur' }
    ],
    orderDate: [{ required: true, message: '请选择订单日期', trigger: 'change' }],
    totalAmount: [
      { required: true, message: '订单金额不能为空', trigger: 'blur' },
      { type: 'number', min: 0.01, message: '订单金额必须大于0', trigger: 'blur' }
    ],
    status: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
    paymentStatus: [{ required: true, message: '请选择支付状态', trigger: 'change' }]
  }
})

// 计算属性
const filteredOrders = computed(() => {
  let result = orders.value
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(order => 
      order.orderNumber.toLowerCase().includes(keyword) || 
      order.customerName.toLowerCase().includes(keyword)
    )
  }
  return result
})

const total = computed(() => filteredOrders.value.length)

// 方法
const getOrders = async () => {
  loading.value = true
  try {
    const response = await api.order.list()
    orders.value = response.data
  } catch (error) {
    ElMessage.error('获取订单列表失败')
    console.error('获取订单列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAddOrder = () => {
  isEditMode.value = false
  formData.value = {
    customerId: '',
    warehouseId: '',
    productId: '',
    quantity: 1,
    orderDate: new Date(),
    totalAmount: 0,
    status: '待处理',
    paymentStatus: '未支付'
  }
  dialogVisible.value = true
}

const handleEditOrder = (row) => {
  isEditMode.value = true
  formData.value = { ...row }
  // 将日期字符串转换为Date对象
  if (row.orderDate) {
    formData.value.orderDate = new Date(row.orderDate)
  }
  // 如果订单没有quantity字段，默认设置为1
  if (!formData.value.quantity) {
    formData.value.quantity = 1
  }
  dialogVisible.value = true
}

const handleDeleteOrder = async (row) => {
  try {
    // 1. 获取订单详情，以便后续进行库存回滚
    const orderDetails = await api.order.getById(row.id)
    
    // 2. 删除订单
    await api.order.delete(row.id)
    
    // 3. 调用库存回滚API（如果后端提供了该接口）
    try {
      await api.order.rollbackInventory(row.id)
      console.log('订单库存回滚成功')
    } catch (rollbackError) {
      console.warn('订单库存回滚失败:', rollbackError)
      // 库存回滚失败不影响订单删除的成功提示
    }
    
    ElMessage.success('删除订单成功')
    await getOrders()
  } catch (error) {
    ElMessage.error('删除订单失败')
    console.error('删除订单失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEditMode.value) {
          await api.order.update(formData.value.id, formData.value)
          ElMessage.success('编辑订单成功')
        } else {
          await api.order.create(formData.value)
          ElMessage.success('新增订单成功')
        }
        dialogVisible.value = false
        await getOrders()
      } catch (error) {
        ElMessage.error(isEditMode.value ? '编辑订单失败' : '新增订单失败')
        console.error('保存订单失败:', error)
      }
    }
  })
}

const formatCurrency = (value) => {
  if (value === undefined || value === null || isNaN(Number(value))) {
    return '¥0.00'
  }
  return `¥${Number(value).toFixed(2)}`
}

const getStatusType = (status) => {
  const statusMap = {
    '待处理': 'warning',
    '已确认': 'info',
    '已发货': 'success',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return statusMap[status] || 'info'
}

const getPaymentStatusType = (status) => {
  const statusMap = {
    '未支付': 'warning',
    '已支付': 'success',
    '部分支付': 'info',
    '已退款': 'danger'
  }
  return statusMap[status] || 'info'
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

// 产品选择变化处理
const handleProductChange = () => {
  // 当产品变化时，重新计算总价
  calculateTotalAmount()
}

// 数量变化处理
const handleQuantityChange = () => {
  // 当数量变化时，重新计算总价
  calculateTotalAmount()
}

// 计算订单总价
const calculateTotalAmount = () => {
  if (!formData.value.productId || !formData.value.quantity) {
    formData.value.totalAmount = 0
    return
  }
  
  // 找到选中的产品
  const selectedProduct = products.value.find(p => p.id === formData.value.productId)
  if (selectedProduct && selectedProduct.price) {
    // 计算总价
    formData.value.totalAmount = selectedProduct.price * formData.value.quantity
  } else {
    formData.value.totalAmount = 0
  }
}

// 文件上传相关方法
const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件！')
  }
  return isExcel
}

const handleFileChange = (file, fileList) => {
  // 限制只能上传一个文件
  if (fileList.length > 1) {
    fileList = [fileList[fileList.length - 1]]
  }
  return fileList
}

const handleImportExcel = async () => {
  if (fileList.value.length === 0) {
    ElMessage.warning('请选择要导入的文件')
    return
  }

  const file = fileList.value[0].raw
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '正在导入数据，请稍候...',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  try {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await api.order.importExcel(formData)
    
    if (response.success) {
      ElMessage.success(`成功导入 ${response.data.successCount} 条订单数据，失败 ${response.data.failCount} 条`)
      if (response.data.failMessages && response.data.failMessages.length > 0) {
        console.warn('导入失败的记录:', response.data.failMessages)
        ElMessage.warning(`部分记录导入失败，请查看控制台日志`)
      }
      await getOrders()
    } else {
      ElMessage.error('导入失败: ' + response.message)
    }
  } catch (error) {
    console.error('Excel导入失败:', error)
    ElMessage.error('导入失败，请检查文件格式是否正确')
  } finally {
    loadingInstance.close()
    fileList.value = []
  }
}

// 加载选择器数据源
const loadSelectData = async () => {
  try {
    // 防御性编程：检查api对象及其属性是否存在
    if (!api) {
      console.error('API对象未定义')
      return
    }
    
    // 加载仓库列表
    if (api.warehouse && api.warehouse.list) {
      try {
        const warehouseResponse = await api.warehouse.list()
        warehouses.value = warehouseResponse?.data || []
      } catch (error) {
        console.error('加载仓库列表失败:', error)
        warehouses.value = []
      }
    }
    
    // 加载客户列表
    if (api.customer && api.customer.list) {
      try {
        const customerResponse = await api.customer.list()
        customers.value = customerResponse?.data || []
      } catch (error) {
        console.error('加载客户列表失败:', error)
        customers.value = []
      }
    }
    
    // 加载产品列表
    if (api.product && api.product.list) {
      try {
        const productResponse = await api.product.list()
        products.value = productResponse?.data || []
      } catch (error) {
        console.error('加载产品列表失败:', error)
        products.value = []
      }
    }
  } catch (error) {
    console.error('加载选择器数据失败:', error)
    ElMessage.warning('部分选择器数据加载失败')
  }
}

// 生命周期
onMounted(async () => {
  await getOrders()
  await loadSelectData()
})
</script>

<style scoped>
.order-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>