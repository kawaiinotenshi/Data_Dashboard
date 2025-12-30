import { createEntityStore } from './useEntityStore'
import api from '@/api/index'

export const useSupplierStore = createEntityStore('supplier', api.supplier.list, '供应商', 'supplierList', 'fetchSupplierList', 'clearSuppliers')
