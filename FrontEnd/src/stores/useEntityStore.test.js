import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createEntityStore } from './useEntityStore'
import { createPinia, setActivePinia } from 'pinia'

vi.mock('@/api/index', () => ({
  default: {
    warehouse: {
      list: vi.fn()
    }
  }
}))

import api from '@/api/index'

describe('stores/useEntityStore.js', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  describe('createEntityStore', () => {
    it('should create a store with correct initial state', () => {
      const mockApiFunction = vi.fn()
      const useTestStore = createEntityStore(
        'test',
        mockApiFunction,
        '测试实体',
        'testList',
        'fetchTestList',
        'clearTests'
      )
      const store = useTestStore()

      expect(store.testList).toEqual([])
      expect(store.loading).toBe(false)
      expect(store.error).toBe(null)
    })

    it('should fetch data successfully', async () => {
      const mockData = [
        { id: 1, name: '测试1' },
        { id: 2, name: '测试2' }
      ]
      api.warehouse.list.mockResolvedValue({ code: 200, data: mockData })

      const useWarehouseStore = createEntityStore(
        'warehouse',
        api.warehouse.list,
        '仓库',
        'warehouseList',
        'fetchWarehouseList',
        'clearWarehouses'
      )
      const store = useWarehouseStore()

      await store.fetchWarehouseList()

      expect(store.loading).toBe(false)
      expect(store.error).toBe(null)
      expect(store.warehouseList).toEqual(mockData)
      expect(api.warehouse.list).toHaveBeenCalledTimes(1)
    })

    it('should handle fetch error', async () => {
      const errorMessage = '网络错误'
      api.warehouse.list.mockRejectedValue(new Error(errorMessage))

      const useWarehouseStore = createEntityStore(
        'warehouse',
        api.warehouse.list,
        '仓库',
        'warehouseList',
        'fetchWarehouseList',
        'clearWarehouses'
      )
      const store = useWarehouseStore()

      await store.fetchWarehouseList()

      expect(store.loading).toBe(false)
      expect(store.error).toBe(errorMessage)
      expect(store.warehouseList).toEqual([])
    })

    it('should handle empty response data', async () => {
      api.warehouse.list.mockResolvedValue({ code: 200, data: null })

      const useWarehouseStore = createEntityStore(
        'warehouse',
        api.warehouse.list,
        '仓库',
        'warehouseList',
        'fetchWarehouseList',
        'clearWarehouses'
      )
      const store = useWarehouseStore()

      await store.fetchWarehouseList()

      expect(store.loading).toBe(false)
      expect(store.error).toBe(null)
      expect(store.warehouseList).toEqual([])
    })

    it('should handle response with non-200 code', async () => {
      api.warehouse.list.mockResolvedValue({ code: 500, message: '服务器错误' })

      const useWarehouseStore = createEntityStore(
        'warehouse',
        api.warehouse.list,
        '仓库',
        'warehouseList',
        'fetchWarehouseList',
        'clearWarehouses'
      )
      const store = useWarehouseStore()

      await store.fetchWarehouseList()

      expect(store.loading).toBe(false)
      expect(store.warehouseList).toEqual([])
    })

    it('should clear data correctly', async () => {
      const mockData = [{ id: 1, name: '测试1' }]
      api.warehouse.list.mockResolvedValue({ code: 200, data: mockData })

      const useWarehouseStore = createEntityStore(
        'warehouse',
        api.warehouse.list,
        '仓库',
        'warehouseList',
        'fetchWarehouseList',
        'clearWarehouses'
      )
      const store = useWarehouseStore()

      await store.fetchWarehouseList()
      expect(store.warehouseList).toEqual(mockData)

      store.clearWarehouses()

      expect(store.warehouseList).toEqual([])
      expect(store.error).toBe(null)
    })

    it('should set loading state during fetch', async () => {
      let resolveFetch
      api.warehouse.list.mockReturnValue(new Promise(resolve => {
        resolveFetch = resolve
      }))

      const useWarehouseStore = createEntityStore(
        'warehouse',
        api.warehouse.list,
        '仓库',
        'warehouseList',
        'fetchWarehouseList',
        'clearWarehouses'
      )
      const store = useWarehouseStore()

      const fetchPromise = store.fetchWarehouseList()

      expect(store.loading).toBe(true)

      resolveFetch({ code: 200, data: [] })
      await fetchPromise

      expect(store.loading).toBe(false)
    })

    it('should handle custom store configuration', async () => {
      const mockApiFunction = vi.fn()
      const mockData = [{ id: 1, name: '自定义' }]
      mockApiFunction.mockResolvedValue({ code: 200, data: mockData })

      const useCustomStore = createEntityStore(
        'custom',
        mockApiFunction,
        '自定义实体',
        'customList',
        'fetchCustomList',
        'clearCustoms'
      )
      const store = useCustomStore()

      await store.fetchCustomList()

      expect(store.customList).toEqual(mockData)
      expect(mockApiFunction).toHaveBeenCalledTimes(1)
    })
  })
})
