import { createEntityStore } from './useEntityStore'
import api from '@/api/index'

export const useWarehouseStore = createEntityStore('warehouse', api.warehouse.list, '仓库', 'warehouseList', 'fetchWarehouseList', 'clearWarehouses', 'updateWarehouses')
