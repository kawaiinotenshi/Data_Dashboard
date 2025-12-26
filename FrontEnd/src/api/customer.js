import request from '@/utils/request'

export function getCustomerList() {
  return request({
    url: '/customer/list',
    method: 'get'
  })
}
