import { createEntityStore } from './useEntityStore'
import api from '@/api/index'

export const useTransportStore = createEntityStore('transport', api.transport.list, '运输', 'transportList', 'fetchTransportList', 'clearTransports')
