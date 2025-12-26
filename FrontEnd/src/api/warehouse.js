import request from '@/utils/request'

export function getWarehouseList() {
  return request({
    url: '/warehouse/list',
    method: 'get'
  })
}

export function getWarehouseById(id) {
  return request({
    url: `/warehouse/${id}`,
    method: 'get'
  })
}
