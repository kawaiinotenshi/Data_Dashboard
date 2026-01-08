import axios from 'axios'
import { handleApiError, ErrorCode } from './errorHandler'

const pendingRequests = new Map()

function generateRequestKey(config) {
  const { method, url, params, data } = config
  return [method, url, JSON.stringify(params), JSON.stringify(data)].join('&')
}

// 简化请求去重逻辑：将相同请求合并为一个，避免取消操作
function addPendingRequest(config) {
  const requestKey = generateRequestKey(config)
  
  // 如果已经有相同请求正在进行，直接取消新请求
  // 这样可以确保只有第一个请求会执行，其他重复请求会被取消
  // 组件可以通过Pinia store共享已获取的数据
  if (pendingRequests.has(requestKey)) {
    const cancel = pendingRequests.get(requestKey)
    cancel(requestKey)
  }
  
  // 为新请求创建取消令牌并存储
  const cancelToken = new axios.CancelToken((cancel) => {
    pendingRequests.set(requestKey, cancel)
  })
  
  config.cancelToken = cancelToken
}

function removePendingRequest(config) {
  const requestKey = generateRequestKey(config)
  if (pendingRequests.has(requestKey)) {
    pendingRequests.delete(requestKey)
  }
}

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  },
  retry: 3,
  retryDelay: 1000
})

request.interceptors.request.use(
  config => {
    removePendingRequest(config)
    addPendingRequest(config)
    
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    config.metadata = { startTime: new Date() }
    
    return config
  },
  error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    removePendingRequest(response.config)
    
    const endTime = new Date()
    const duration = endTime - response.config.metadata.startTime
    
    if (duration > 1000) {
      console.warn(`请求耗时过长: ${response.config.url} - ${duration}ms`)
    }
    
    const data = response.data
    
    if (data.code === ErrorCode.SUCCESS) {
      return data
    } else {
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  async error => {
    if (error.config) {
      removePendingRequest(error.config)
    }
    
    const { config, response } = error
    
    if (!config) {
      return handleApiError(error)
    }
    
    if (response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
      return Promise.reject(error)
    }
    
    if (config.retry && config.retry > 0 && !axios.isCancel(error)) {
      config.retry -= 1
      const delay = config.retryDelay || 1000
      console.log(`请求失败，${delay}ms后重试，剩余重试次数: ${config.retry}`)
      
      await new Promise(resolve => setTimeout(resolve, delay))
      
      return request(config)
    }
    
    return handleApiError(error)
  }
)

export default request
