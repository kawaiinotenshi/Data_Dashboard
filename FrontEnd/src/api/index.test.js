import { describe, it, expect, vi, beforeEach } from 'vitest'
import { api } from '../api'
import request from '../utils/request'

vi.mock('../utils/request')

describe('api/index.js', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('Order API', () => {
    it('should call order.list', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.list()

      expect(request.get).toHaveBeenCalledWith('/order/list')
      expect(result).toEqual(mockData)
    })

    it('should call order.getById', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.getById(1)

      expect(request.get).toHaveBeenCalledWith('/order/1')
      expect(result).toEqual(mockData)
    })

    it('should call order.create', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const orderData = { customerId: 1, productId: 1, quantity: 10 }
      request.post.mockResolvedValue(mockData)

      const result = await api.order.create(orderData)

      expect(request.post).toHaveBeenCalledWith('/order', orderData)
      expect(result).toEqual(mockData)
    })

    it('should call order.update', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const orderData = { quantity: 20 }
      request.put.mockResolvedValue(mockData)

      const result = await api.order.update(1, orderData)

      expect(request.put).toHaveBeenCalledWith('/order/1', orderData)
      expect(result).toEqual(mockData)
    })

    it('should call order.delete', async () => {
      const mockData = { code: 200, message: '删除成功' }
      request.delete.mockResolvedValue(mockData)

      const result = await api.order.delete(1)

      expect(request.delete).toHaveBeenCalledWith('/order/1')
      expect(result).toEqual(mockData)
    })

    it('should call order.batchDelete', async () => {
      const mockData = { code: 200, message: '批量删除成功' }
      const ids = [1, 2, 3]
      request.delete.mockResolvedValue(mockData)

      const result = await api.order.batchDelete(ids)

      expect(request.delete).toHaveBeenCalledWith('/order/batch', { data: ids })
      expect(result).toEqual(mockData)
    })

    it('should call order.statistics', async () => {
      const mockData = { code: 200, data: { total: 100 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.statistics()

      expect(request.get).toHaveBeenCalledWith('/order/statistics')
      expect(result).toEqual(mockData)
    })

    it('should call order.getByStatus', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.getByStatus('pending')

      expect(request.get).toHaveBeenCalledWith('/order/status/pending')
      expect(result).toEqual(mockData)
    })

    it('should call order.getByDateRange', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.getByDateRange('2024-01-01', '2024-12-31')

      expect(request.get).toHaveBeenCalledWith('/order/date-range', {
        params: { startDate: '2024-01-01', endDate: '2024-12-31' }
      })
      expect(result).toEqual(mockData)
    })

    it('should call order.getByCustomer', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.getByCustomer(1)

      expect(request.get).toHaveBeenCalledWith('/order/customer/1')
      expect(result).toEqual(mockData)
    })

    it('should call order.getTrend', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.getTrend()

      expect(request.get).toHaveBeenCalledWith('/order/trend')
      expect(result).toEqual(mockData)
    })

    it('should call order.getCustomerOrderCount', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.order.getCustomerOrderCount()

      expect(request.get).toHaveBeenCalledWith('/order/customer-order-count')
      expect(result).toEqual(mockData)
    })
  })

  describe('Warehouse API', () => {
    it('should call warehouse.list', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.list()

      expect(request.get).toHaveBeenCalledWith('/warehouse/list')
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.getById', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.getById(1)

      expect(request.get).toHaveBeenCalledWith('/warehouse/1')
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.create', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const warehouseData = { name: '大连仓', location: '大连市', capacity: 10000 }
      request.post.mockResolvedValue(mockData)

      const result = await api.warehouse.create(warehouseData)

      expect(request.post).toHaveBeenCalledWith('/warehouse', warehouseData)
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.update', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const warehouseData = { capacity: 15000 }
      request.put.mockResolvedValue(mockData)

      const result = await api.warehouse.update(1, warehouseData)

      expect(request.put).toHaveBeenCalledWith('/warehouse/1', warehouseData)
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.delete', async () => {
      const mockData = { code: 200, message: '删除成功' }
      request.delete.mockResolvedValue(mockData)

      const result = await api.warehouse.delete(1)

      expect(request.delete).toHaveBeenCalledWith('/warehouse/1')
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.batchDelete', async () => {
      const mockData = { code: 200, message: '批量删除成功' }
      const ids = [1, 2, 3]
      request.delete.mockResolvedValue(mockData)

      const result = await api.warehouse.batchDelete(ids)

      expect(request.delete).toHaveBeenCalledWith('/warehouse/batch', { data: ids })
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.statistics', async () => {
      const mockData = { code: 200, data: { total: 10 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.statistics()

      expect(request.get).toHaveBeenCalledWith('/warehouse/statistics')
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.getByName', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.getByName('大连仓')

      expect(request.get).toHaveBeenCalledWith('/warehouse/name/大连仓')
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.getByLocation', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.getByLocation('大连市')

      expect(request.get).toHaveBeenCalledWith('/warehouse/location/大连市')
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.getByCapacityRange', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.getByCapacityRange(5000, 15000)

      expect(request.get).toHaveBeenCalledWith('/warehouse/capacity-range', {
        params: { minCapacity: 5000, maxCapacity: 15000 }
      })
      expect(result).toEqual(mockData)
    })

    it('should call warehouse.getUsageRate', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.warehouse.getUsageRate()

      expect(request.get).toHaveBeenCalledWith('/warehouse/usage-rate')
      expect(result).toEqual(mockData)
    })
  })

  describe('Inventory API', () => {
    it('should call inventory.list', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.list()

      expect(request.get).toHaveBeenCalledWith('/inventory/list')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getById', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getById(1)

      expect(request.get).toHaveBeenCalledWith('/inventory/1')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.create', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const inventoryData = { productId: 1, warehouseId: 1, quantity: 100 }
      request.post.mockResolvedValue(mockData)

      const result = await api.inventory.create(inventoryData)

      expect(request.post).toHaveBeenCalledWith('/inventory', inventoryData)
      expect(result).toEqual(mockData)
    })

    it('should call inventory.update', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const inventoryData = { quantity: 200 }
      request.put.mockResolvedValue(mockData)

      const result = await api.inventory.update(1, inventoryData)

      expect(request.put).toHaveBeenCalledWith('/inventory/1', inventoryData)
      expect(result).toEqual(mockData)
    })

    it('should call inventory.delete', async () => {
      const mockData = { code: 200, message: '删除成功' }
      request.delete.mockResolvedValue(mockData)

      const result = await api.inventory.delete(1)

      expect(request.delete).toHaveBeenCalledWith('/inventory/1')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.batchDelete', async () => {
      const mockData = { code: 200, message: '批量删除成功' }
      const ids = [1, 2, 3]
      request.delete.mockResolvedValue(mockData)

      const result = await api.inventory.batchDelete(ids)

      expect(request.delete).toHaveBeenCalledWith('/inventory/batch', { data: ids })
      expect(result).toEqual(mockData)
    })

    it('should call inventory.statistics', async () => {
      const mockData = { code: 200, data: { total: 50 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.statistics()

      expect(request.get).toHaveBeenCalledWith('/inventory/statistics')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getByWarehouse', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getByWarehouse(1)

      expect(request.get).toHaveBeenCalledWith('/inventory/warehouse/1')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getByProduct', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getByProduct(1)

      expect(request.get).toHaveBeenCalledWith('/inventory/product/1')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getLowStock', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getLowStock()

      expect(request.get).toHaveBeenCalledWith('/inventory/low-stock')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getByCategory', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getByCategory('电子产品')

      expect(request.get).toHaveBeenCalledWith('/inventory/category/电子产品')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getEnterpriseRanking', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getEnterpriseRanking()

      expect(request.get).toHaveBeenCalledWith('/inventory/enterprise-ranking')
      expect(result).toEqual(mockData)
    })

    it('should call inventory.getEnterpriseRatio', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.inventory.getEnterpriseRatio()

      expect(request.get).toHaveBeenCalledWith('/inventory/enterprise-ratio')
      expect(result).toEqual(mockData)
    })
  })

  describe('Customer API', () => {
    it('should call customer.list', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.customer.list()

      expect(request.get).toHaveBeenCalledWith('/customer/list')
      expect(result).toEqual(mockData)
    })

    it('should call customer.getById', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.customer.getById(1)

      expect(request.get).toHaveBeenCalledWith('/customer/1')
      expect(result).toEqual(mockData)
    })

    it('should call customer.create', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const customerData = { name: '张三', address: '北京市', phone: '13800138000' }
      request.post.mockResolvedValue(mockData)

      const result = await api.customer.create(customerData)

      expect(request.post).toHaveBeenCalledWith('/customer', customerData)
      expect(result).toEqual(mockData)
    })

    it('should call customer.update', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const customerData = { phone: '13900139000' }
      request.put.mockResolvedValue(mockData)

      const result = await api.customer.update(1, customerData)

      expect(request.put).toHaveBeenCalledWith('/customer/1', customerData)
      expect(result).toEqual(mockData)
    })

    it('should call customer.delete', async () => {
      const mockData = { code: 200, message: '删除成功' }
      request.delete.mockResolvedValue(mockData)

      const result = await api.customer.delete(1)

      expect(request.delete).toHaveBeenCalledWith('/customer/1')
      expect(result).toEqual(mockData)
    })

    it('should call customer.batchDelete', async () => {
      const mockData = { code: 200, message: '批量删除成功' }
      const ids = [1, 2, 3]
      request.delete.mockResolvedValue(mockData)

      const result = await api.customer.batchDelete(ids)

      expect(request.delete).toHaveBeenCalledWith('/customer/batch', { data: ids })
      expect(result).toEqual(mockData)
    })

    it('should call customer.statistics', async () => {
      const mockData = { code: 200, data: { total: 20 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.customer.statistics()

      expect(request.get).toHaveBeenCalledWith('/customer/statistics')
      expect(result).toEqual(mockData)
    })

    it('should call customer.getByName', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.customer.getByName('张三')

      expect(request.get).toHaveBeenCalledWith('/customer/name/张三')
      expect(result).toEqual(mockData)
    })

    it('should call customer.getByAddress', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.customer.getByAddress('北京市')

      expect(request.get).toHaveBeenCalledWith('/customer/address/北京市')
      expect(result).toEqual(mockData)
    })

    it('should call customer.getOrderCount', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.customer.getOrderCount()

      expect(request.get).toHaveBeenCalledWith('/customer/order-count')
      expect(result).toEqual(mockData)
    })
  })

  describe('Supplier API', () => {
    it('should call supplier.list', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.supplier.list()

      expect(request.get).toHaveBeenCalledWith('/supplier/list')
      expect(result).toEqual(mockData)
    })

    it('should call supplier.getById', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.supplier.getById(1)

      expect(request.get).toHaveBeenCalledWith('/supplier/1')
      expect(result).toEqual(mockData)
    })

    it('should call supplier.create', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const supplierData = { name: '供应商A', address: '上海市', phone: '13800138000' }
      request.post.mockResolvedValue(mockData)

      const result = await api.supplier.create(supplierData)

      expect(request.post).toHaveBeenCalledWith('/supplier', supplierData)
      expect(result).toEqual(mockData)
    })

    it('should call supplier.update', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const supplierData = { phone: '13900139000' }
      request.put.mockResolvedValue(mockData)

      const result = await api.supplier.update(1, supplierData)

      expect(request.put).toHaveBeenCalledWith('/supplier/1', supplierData)
      expect(result).toEqual(mockData)
    })

    it('should call supplier.delete', async () => {
      const mockData = { code: 200, message: '删除成功' }
      request.delete.mockResolvedValue(mockData)

      const result = await api.supplier.delete(1)

      expect(request.delete).toHaveBeenCalledWith('/supplier/1')
      expect(result).toEqual(mockData)
    })

    it('should call supplier.batchDelete', async () => {
      const mockData = { code: 200, message: '批量删除成功' }
      const ids = [1, 2, 3]
      request.delete.mockResolvedValue(mockData)

      const result = await api.supplier.batchDelete(ids)

      expect(request.delete).toHaveBeenCalledWith('/supplier/batch', { data: ids })
      expect(result).toEqual(mockData)
    })

    it('should call supplier.statistics', async () => {
      const mockData = { code: 200, data: { total: 15 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.supplier.statistics()

      expect(request.get).toHaveBeenCalledWith('/supplier/statistics')
      expect(result).toEqual(mockData)
    })

    it('should call supplier.getByName', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.supplier.getByName('供应商A')

      expect(request.get).toHaveBeenCalledWith('/supplier/name/供应商A')
      expect(result).toEqual(mockData)
    })

    it('should call supplier.getByAddress', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.supplier.getByAddress('上海市')

      expect(request.get).toHaveBeenCalledWith('/supplier/address/上海市')
      expect(result).toEqual(mockData)
    })

    it('should call supplier.getContactRanking', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.supplier.getContactRanking()

      expect(request.get).toHaveBeenCalledWith('/supplier/contact-ranking')
      expect(result).toEqual(mockData)
    })
  })

  describe('Transport API', () => {
    it('should call transport.list', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.list()

      expect(request.get).toHaveBeenCalledWith('/transport/list')
      expect(result).toEqual(mockData)
    })

    it('should call transport.getById', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.getById(1)

      expect(request.get).toHaveBeenCalledWith('/transport/1')
      expect(result).toEqual(mockData)
    })

    it('should call transport.getByStatus', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.getByStatus('completed')

      expect(request.get).toHaveBeenCalledWith('/transport/status/completed')
      expect(result).toEqual(mockData)
    })

    it('should call transport.create', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const transportData = { vehicleType: '卡车', driver: '李四', status: 'pending' }
      request.post.mockResolvedValue(mockData)

      const result = await api.transport.create(transportData)

      expect(request.post).toHaveBeenCalledWith('/transport', transportData)
      expect(result).toEqual(mockData)
    })

    it('should call transport.update', async () => {
      const mockData = { code: 200, data: { id: 1 } }
      const transportData = { status: 'completed' }
      request.put.mockResolvedValue(mockData)

      const result = await api.transport.update(1, transportData)

      expect(request.put).toHaveBeenCalledWith('/transport/1', transportData)
      expect(result).toEqual(mockData)
    })

    it('should call transport.delete', async () => {
      const mockData = { code: 200, message: '删除成功' }
      request.delete.mockResolvedValue(mockData)

      const result = await api.transport.delete(1)

      expect(request.delete).toHaveBeenCalledWith('/transport/1')
      expect(result).toEqual(mockData)
    })

    it('should call transport.batchDelete', async () => {
      const mockData = { code: 200, message: '批量删除成功' }
      const ids = [1, 2, 3]
      request.delete.mockResolvedValue(mockData)

      const result = await api.transport.batchDelete(ids)

      expect(request.delete).toHaveBeenCalledWith('/transport/batch', { data: ids })
      expect(result).toEqual(mockData)
    })

    it('should call transport.statistics', async () => {
      const mockData = { code: 200, data: { total: 30 } }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.statistics()

      expect(request.get).toHaveBeenCalledWith('/transport/statistics')
      expect(result).toEqual(mockData)
    })

    it('should call transport.getByMonth', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.getByMonth('2024-01')

      expect(request.get).toHaveBeenCalledWith('/transport/month/2024-01')
      expect(result).toEqual(mockData)
    })

    it('should call transport.getByVehicleType', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.getByVehicleType('卡车')

      expect(request.get).toHaveBeenCalledWith('/transport/vehicle-type/卡车')
      expect(result).toEqual(mockData)
    })

    it('should call transport.getTrend', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.getTrend()

      expect(request.get).toHaveBeenCalledWith('/transport/trend')
      expect(result).toEqual(mockData)
    })

    it('should call transport.getByVehicleTypeGroup', async () => {
      const mockData = { code: 200, data: [] }
      request.get.mockResolvedValue(mockData)

      const result = await api.transport.getByVehicleTypeGroup()

      expect(request.get).toHaveBeenCalledWith('/transport/vehicle-type-group')
      expect(result).toEqual(mockData)
    })
  })
})
