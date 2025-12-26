import request from '@/utils/request'

export function getOrderList() {
  return request({
    url: '/order/list',
    method: 'get'
  })
}

export function getOrdersByStatus(status) {
  return request({
    url: `/order/status/${status}`,
    method: 'get'
  })
}
