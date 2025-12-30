import { createEntityStore } from './useEntityStore'
import api from '@/api/index'

export const useOrderStore = createEntityStore('order', api.order.list, '订单', 'orderList', 'fetchOrderList', 'clearOrders')
