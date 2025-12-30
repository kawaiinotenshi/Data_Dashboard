import { createEntityStore } from './useEntityStore'
import api from '@/api/index'

export const useCustomerStore = createEntityStore('customer', api.customer.list, '客户', 'customerList', 'fetchCustomerList', 'clearCustomers')
