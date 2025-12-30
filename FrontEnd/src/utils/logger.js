class Logger {
  constructor() {
    this.logLevel = import.meta.env.VITE_LOG_LEVEL || 'info'
    this.levels = {
      debug: 0,
      info: 1,
      warn: 2,
      error: 3
    }
    this.logs = []
    this.maxLogs = 100
  }

  shouldLog(level) {
    return this.levels[level] >= this.levels[this.logLevel]
  }

  formatMessage(level, message, data = null) {
    const timestamp = new Date().toISOString()
    const logEntry = {
      timestamp,
      level,
      message,
      data
    }
    return logEntry
  }

  addLog(logEntry) {
    this.logs.push(logEntry)
    if (this.logs.length > this.maxLogs) {
      this.logs.shift()
    }
  }

  debug(message, data = null) {
    if (this.shouldLog('debug')) {
      const logEntry = this.formatMessage('debug', message, data)
      this.addLog(logEntry)
      console.log(`[DEBUG] ${logEntry.timestamp} - ${message}`, data || '')
    }
  }

  info(message, data = null) {
    if (this.shouldLog('info')) {
      const logEntry = this.formatMessage('info', message, data)
      this.addLog(logEntry)
      console.info(`[INFO] ${logEntry.timestamp} - ${message}`, data || '')
    }
  }

  warn(message, data = null) {
    if (this.shouldLog('warn')) {
      const logEntry = this.formatMessage('warn', message, data)
      this.addLog(logEntry)
      console.warn(`[WARN] ${logEntry.timestamp} - ${message}`, data || '')
    }
  }

  error(message, error = null) {
    if (this.shouldLog('error')) {
      const errorData = error ? {
        message: error.message,
        stack: error.stack,
        name: error.name
      } : null
      const logEntry = this.formatMessage('error', message, errorData)
      this.addLog(logEntry)
      console.error(`[ERROR] ${logEntry.timestamp} - ${message}`, error || '')
    }
  }

  getLogs(level = null) {
    if (level) {
      return this.logs.filter(log => log.level === level)
    }
    return [...this.logs]
  }

  clearLogs() {
    this.logs = []
  }

  exportLogs() {
    return JSON.stringify(this.logs, null, 2)
  }
}

export const logger = new Logger()
export default logger
