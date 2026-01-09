import axios from 'axios'
import { handleApiError, ErrorCode } from './errorHandler'

// 使用Map存储正在进行的请求Promise，而不是取消令牌
const pendingRequests = new Map()

function generateRequestKey(config) {
  const { method, url, params, data } = config
  return [method, url, JSON.stringify(params), JSON.stringify(data)].join('&')
}

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api',
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  },
  retry: 3,
  retryDelay: 1000
})

// 创建请求拦截器
axiosInstance.interceptors.request.use(
  config => {
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

// 创建响应拦截器
axiosInstance.interceptors.response.use(
  response => {
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
    const { config, response } = error
    
    if (!config) {
      return handleApiError(error)
    }
    
    if (response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      // 当当前页面不是登录页面时，才跳转到登录页面
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
    
    if (config.retry && config.retry > 0 && !axios.isCancel(error)) {
      config.retry -= 1
      const delay = config.retryDelay || 1000
      console.log(`请求失败，${delay}ms后重试，剩余重试次数: ${config.retry}`)
      
      await new Promise(resolve => setTimeout(resolve, delay))
      
      return axiosInstance(config)
    }
    
    return handleApiError(error)
  }
)

// 包装axiosInstance，实现请求合并
const request = (config) => {
  const requestKey = generateRequestKey(config)
  
  // 检查是否有相同请求正在进行
  if (pendingRequests.has(requestKey)) {
    return pendingRequests.get(requestKey)
  }
  
  // 发起新请求并将Promise存储到pendingRequests中
  const requestPromise = axiosInstance(config)
    .finally(() => {
      // 请求完成后从pendingRequests中移除
      if (pendingRequests.get(requestKey) === requestPromise) {
        pendingRequests.delete(requestKey)
      }
    })
  
  // 存储请求Promise
  pendingRequests.set(requestKey, requestPromise)
  
  return requestPromise
}

// 添加HTTP方法快捷方式，以兼容现有代码
request.get = (url, config = {}) => request({ method: 'get', url, ...config })
request.post = (url, data = {}, config = {}) => request({ method: 'post', url, data, ...config })
request.put = (url, data = {}, config = {}) => request({ method: 'put', url, data, ...config })
request.delete = (url, config = {}) => request({ method: 'delete', url, ...config })
request.patch = (url, data = {}, config = {}) => request({ method: 'patch', url, data, ...config })
request.head = (url, config = {}) => request({ method: 'head', url, ...config })
request.options = (url, config = {}) => request({ method: 'options', url, ...config })

export default request
