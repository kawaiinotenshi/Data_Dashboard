import request from '@/utils/request'

export function getTransportList() {
  return request({
    url: '/transport/list',
    method: 'get'
  })
}

export function getTransportsByStatus(status) {
  return request({
    url: `/transport/status/${status}`,
    method: 'get'
  })
}
