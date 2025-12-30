import logger from './logger'

export const ErrorCode = {
  SUCCESS: 200,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500,
  BAD_REQUEST: 400,
  NETWORK_ERROR: 0,
  TIMEOUT: 408
}

export const ErrorMessage = {
  [ErrorCode.SUCCESS]: '操作成功',
  [ErrorCode.UNAUTHORIZED]: '未授权，请重新登录',
  [ErrorCode.FORBIDDEN]: '拒绝访问',
  [ErrorCode.NOT_FOUND]: '请求的资源不存在',
  [ErrorCode.INTERNAL_SERVER_ERROR]: '服务器内部错误',
  [ErrorCode.BAD_REQUEST]: '请求参数错误',
  [ErrorCode.NETWORK_ERROR]: '网络错误，请检查网络连接',
  [ErrorCode.TIMEOUT]: '请求超时，请稍后重试'
}

export function getErrorMessage(error) {
  if (error.response) {
    const status = error.response.status
    const data = error.response.data
    return data?.message || ErrorMessage[status] || `请求错误: ${status}`
  } else if (error.request) {
    return ErrorMessage[ErrorCode.NETWORK_ERROR]
  } else {
    return ErrorMessage[ErrorCode.BAD_REQUEST]
  }
}

export function showErrorNotification(message, duration = 3000) {
  logger.error('显示错误通知', { message, duration })
  if (typeof window !== 'undefined' && window.$notify) {
    window.$notify.error({
      title: '错误',
      message,
      duration
    })
  } else {
    console.error(message)
  }
}

export function showSuccessNotification(message, duration = 2000) {
  logger.info('显示成功通知', { message, duration })
  if (typeof window !== 'undefined' && window.$notify) {
    window.$notify.success({
      title: '成功',
      message,
      duration
    })
  } else {
    console.log(message)
  }
}

export function showWarningNotification(message, duration = 3000) {
  logger.warn('显示警告通知', { message, duration })
  if (typeof window !== 'undefined' && window.$notify) {
    window.$notify.warning({
      title: '警告',
      message,
      duration
    })
  } else {
    console.warn(message)
  }
}

export function handleApiError(error) {
  logger.error('API错误', { error: error.message, stack: error.stack, response: error.response?.data })
  const message = getErrorMessage(error)
  showErrorNotification(message)
  
  if (error.response?.status === ErrorCode.UNAUTHORIZED) {
    logger.warn('未授权，清除用户信息')
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    if (typeof window !== 'undefined') {
      window.location.href = '/login'
    }
  }
  
  return Promise.reject(error)
}

export function handleApiSuccess(data, message = '操作成功') {
  logger.info('API成功响应', { data, message })
  if (data?.code === ErrorCode.SUCCESS) {
    if (message) {
      showSuccessNotification(message)
    }
    return data
  } else {
    const errorMessage = data?.message || '操作失败'
    logger.error('API返回错误', { data, errorMessage })
    showErrorNotification(errorMessage)
    return Promise.reject(new Error(errorMessage))
  }
}
