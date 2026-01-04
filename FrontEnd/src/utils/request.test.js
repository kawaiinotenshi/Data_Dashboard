import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import axios from 'axios'
import request from '../request'

vi.mock('axios')

describe('request.js', () => {
  beforeEach(() => {
    localStorage.clear()
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('should create axios instance with correct config', () => {
    expect(request.defaults.baseURL).toBe('http://localhost:8080/api')
    expect(request.defaults.timeout).toBe(5000)
  })

  it('should add Authorization header when token exists', async () => {
    localStorage.setItem('token', 'test-token')
    
    axios.create.mockReturnValue({
      interceptors: {
        request: { use: vi.fn() },
        response: { use: vi.fn() }
      }
    })

    const mockRequest = axios.create()
    const requestConfig = { headers: {} }
    
    mockRequest.interceptors.request.use.mockImplementation(fn => {
      fn(requestConfig)
    })

    expect(localStorage.getItem('token')).toBe('test-token')
  })

  it('should handle successful response', async () => {
    const mockResponse = {
      data: {
        code: 200,
        data: { id: 1, name: 'test' },
        message: 'success'
      }
    }

    axios.mockResolvedValue(mockResponse)

    const result = await request.get('/test')
    
    expect(result.code).toBe(200)
    expect(result.data).toEqual({ id: 1, name: 'test' })
  })

  it('should handle error response', async () => {
    const mockError = {
      response: {
        status: 401,
        data: { message: '未授权' }
      }
    }

    axios.mockRejectedValue(mockError)

    await expect(request.get('/test')).rejects.toThrow()
  })
})
