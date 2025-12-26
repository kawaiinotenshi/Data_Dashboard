import request from '@/utils/request'

export function getInventoryList() {
  return request({
    url: '/inventory/list',
    method: 'get'
  })
}

export function getInventoryByWarehouse(warehouseId) {
  return request({
    url: `/inventory/warehouse/${warehouseId}`,
    method: 'get'
  })
}
