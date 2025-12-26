import request from '@/utils/request'

export function getSupplierList() {
  return request({
    url: '/supplier/list',
    method: 'get'
  })
}
