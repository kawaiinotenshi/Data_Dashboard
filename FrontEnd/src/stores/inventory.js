import { createEntityStore } from './useEntityStore'
import api from '@/api/index'

export const useInventoryStore = createEntityStore('inventory', api.inventory.list, '库存', 'inventoryList', 'fetchInventoryList', 'clearInventory')
