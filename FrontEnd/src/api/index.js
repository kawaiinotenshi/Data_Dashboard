import request from '../utils/request'

export const api = {
  order: {
    list: () => request.get('/order/list'),
    getById: (id) => request.get(`/order/${id}`),
    create: (data) => request.post('/order', data),
    update: (id, data) => request.put(`/order/${id}`, data),
    delete: (id) => request.delete(`/order/${id}`),
    batchDelete: (ids) => request.delete('/order/batch', { data: ids }),
    statistics: () => request.get('/order/statistics'),
    getByStatus: (status) => request.get(`/order/status/${status}`),
    getByDateRange: (startDate, endDate) => request.get('/order/date-range', { params: { startDate, endDate } }),
    getByCustomer: (customerId) => request.get(`/order/customer/${customerId}`),
    getTrend: () => request.get('/order/trend'),
    getCustomerOrderCount: () => request.get('/order/customer-order-count')
  },
  
  warehouse: {
    list: () => request.get('/warehouse/list'),
    getById: (id) => request.get(`/warehouse/${id}`),
    create: (data) => request.post('/warehouse', data),
    update: (id, data) => request.put(`/warehouse/${id}`, data),
    delete: (id) => request.delete(`/warehouse/${id}`),
    batchDelete: (ids) => request.delete('/warehouse/batch', { data: ids }),
    statistics: () => request.get('/warehouse/statistics'),
    getByName: (name) => request.get(`/warehouse/name/${name}`),
    getByLocation: (location) => request.get(`/warehouse/location/${location}`),
    getByCapacityRange: (minCapacity, maxCapacity) => request.get('/warehouse/capacity-range', { params: { minCapacity, maxCapacity } }),
    getUsageRate: () => request.get('/warehouse/usage-rate')
  },
  
  inventory: {
    list: () => request.get('/inventory/list'),
    getById: (id) => request.get(`/inventory/${id}`),
    create: (data) => request.post('/inventory', data),
    update: (id, data) => request.put(`/inventory/${id}`, data),
    delete: (id) => request.delete(`/inventory/${id}`),
    batchDelete: (ids) => request.delete('/inventory/batch', { data: ids }),
    statistics: () => request.get('/inventory/statistics'),
    getByWarehouse: (warehouseId) => request.get(`/inventory/warehouse/${warehouseId}`),
    getByProduct: (productId) => request.get(`/inventory/product/${productId}`),
    getLowStock: () => request.get('/inventory/low-stock'),
    getByCategory: (category) => request.get(`/inventory/category/${category}`),
    getEnterpriseRanking: () => request.get('/inventory/enterprise-ranking'),
    getEnterpriseRatio: () => request.get('/inventory/enterprise-ratio')
  },
  
  customer: {
    list: () => request.get('/customer/list'),
    getById: (id) => request.get(`/customer/${id}`),
    create: (data) => request.post('/customer', data),
    update: (id, data) => request.put(`/customer/${id}`, data),
    delete: (id) => request.delete(`/customer/${id}`),
    batchDelete: (ids) => request.delete('/customer/batch', { data: ids }),
    statistics: () => request.get('/customer/statistics'),
    getByName: (name) => request.get(`/customer/name/${name}`),
    getByAddress: (address) => request.get(`/customer/address/${address}`),
    getOrderCount: () => request.get('/customer/order-count')
  },
  
  supplier: {
    list: () => request.get('/supplier/list'),
    getById: (id) => request.get(`/supplier/${id}`),
    create: (data) => request.post('/supplier', data),
    update: (id, data) => request.put(`/supplier/${id}`, data),
    delete: (id) => request.delete(`/supplier/${id}`),
    batchDelete: (ids) => request.delete('/supplier/batch', { data: ids }),
    statistics: () => request.get('/supplier/statistics'),
    getByName: (name) => request.get(`/supplier/name/${name}`),
    getByAddress: (address) => request.get(`/supplier/address/${address}`),
    getContactRanking: () => request.get('/supplier/contact-ranking')
  },
  
  transport: {
    list: () => request.get('/transport/list'),
    getById: (id) => request.get(`/transport/${id}`),
    getByStatus: (status) => request.get(`/transport/status/${status}`),
    create: (data) => request.post('/transport', data),
    update: (id, data) => request.put(`/transport/${id}`, data),
    delete: (id) => request.delete(`/transport/${id}`),
    batchDelete: (ids) => request.delete('/transport/batch', { data: ids }),
    statistics: () => request.get('/transport/statistics'),
    getByMonth: (month) => request.get(`/transport/month/${month}`),
    getByVehicleType: (vehicleType) => request.get(`/transport/vehicle-type/${vehicleType}`),
    getTrend: () => request.get('/transport/trend'),
    getByVehicleTypeGroup: () => request.get('/transport/vehicle-type-group')
  },
  
  employee: {
    list: () => request.get('/employee/list'),
    getById: (id) => request.get(`/employee/${id}`),
    create: (data) => request.post('/employee', data),
    update: (id, data) => request.put(`/employee/${id}`, data),
    delete: (id) => request.delete(`/employee/${id}`),
    batchDelete: (ids) => request.delete('/employee/batch', { data: ids }),
    search: (params) => request.get('/employee/search', { params }),
    getHighestPaidEmployee: () => request.get('/employee/highest-paid'),
    getDepartmentStatistics: () => request.get('/employee/department-statistics'),
    getWithDepartment: () => request.get('/employee/list-with-department')
  },
  
  department: {
    list: () => request.get('/department/list'),
    getById: (id) => request.get(`/department/${id}`),
    create: (data) => request.post('/department', data),
    update: (id, data) => request.put(`/department/${id}`, data),
    delete: (id) => request.delete(`/department/${id}`),
    batchDelete: (ids) => request.delete('/department/batch', { data: ids })
  },
  
  ai: {
    chat: (data) => request.post('/ai/chat', data, { timeout: 30000 })
  }
}

export default api