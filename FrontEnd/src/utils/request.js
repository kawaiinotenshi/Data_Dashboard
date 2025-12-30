import axios from 'axios'
import { handleApiError, ErrorCode } from './errorHandler'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 5000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

request.interceptors.request.use(
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

request.interceptors.response.use(
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
  error => {
    return handleApiError(error)
  }
)

export default request
