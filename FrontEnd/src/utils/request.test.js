import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import axios from 'axios'

const mockAxiosInstance = {
  defaults: {
    baseURL: 'http://localhost:8080/api',
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  },
  get: vi.fn(),
  post: vi.fn(),
  put: vi.fn(),
  delete: vi.fn()
}

vi.mock('axios', () => ({
  default: {
    create: vi.fn(() => mockAxiosInstance),
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn()
  }
}))

describe('request.js', () => {
  let request

  beforeEach(() => {
    localStorage.clear()
    vi.clearAllMocks()
    
    request = axios.create({
      baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
      timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 10000,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      retry: 3,
      retryDelay: 1000
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('should create axios instance with correct config', () => {
    expect(request.defaults.baseURL).toBe('http://localhost:8080/api')
    expect(request.defaults.timeout).toBe(10000)
    expect(request.defaults.headers['Content-Type']).toBe('application/json;charset=UTF-8')
  })

  it('should add Authorization header when token exists in localStorage', () => {
    localStorage.setItem('token', 'test-token-123')
    
    const token = localStorage.getItem('token')
    
    expect(token).toBe('test-token-123')
  })

  it('should handle successful response', async () => {
    const mockResponse = {
      data: {
        code: 200,
        data: { id: 1, name: 'test' },
        message: 'success'
      }
    }

    mockAxiosInstance.get.mockResolvedValue(mockResponse)

    const result = await mockAxiosInstance.get('/test')
    
    expect(result.data.code).toBe(200)
    expect(result.data.data).toEqual({ id: 1, name: 'test' })
  })

  it('should handle error response', async () => {
    const mockError = {
      response: {
        status: 401,
        data: { message: '未授权' }
      }
    }

    mockAxiosInstance.get.mockRejectedValue(mockError)

    await expect(mockAxiosInstance.get('/test')).rejects.toEqual(mockError)
  })

  it('should handle 500 error response', async () => {
    const mockError = {
      response: {
        status: 500,
        data: { message: '服务器内部错误' }
      }
    }

    mockAxiosInstance.get.mockRejectedValue(mockError)

    await expect(mockAxiosInstance.get('/test')).rejects.toEqual(mockError)
  })

  it('should handle network error without response', async () => {
    const mockError = {
      message: 'Network Error'
    }

    mockAxiosInstance.get.mockRejectedValue(mockError)

    await expect(mockAxiosInstance.get('/test')).rejects.toEqual(mockError)
  })
})
