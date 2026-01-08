import { defineStore } from 'pinia'
import { ref } from 'vue'

// 创建一个全局的请求缓存对象，用于存储不同store的请求Promise
const globalRequestCache = new Map()

export function createEntityStore(storeName, apiFunction, entityName, dataListName = 'dataList', fetchFunctionName = 'fetchDataList', clearFunctionName = 'clearData', updateFunctionName = 'updateDataList') {
  return defineStore(storeName, () => {
    const dataList = ref([])
    const loading = ref(false)
    const error = ref(null)

    const fetchDataList = async () => {
      // 生成缓存键，确保不同store的请求不会互相影响
      const cacheKey = `entityStore_${storeName}_fetch`
      
      // 如果已经有请求正在进行，直接返回现有的Promise
      if (globalRequestCache.has(cacheKey)) {
        return globalRequestCache.get(cacheKey)
      }

      loading.value = true
      error.value = null
      
      try {
        // 创建新的Promise并缓存到全局请求缓存对象中
        const fetchPromise = apiFunction()
        globalRequestCache.set(cacheKey, fetchPromise)
        
        const response = await fetchPromise
        if (response.code === 200) {
          dataList.value = response.data || []
        }
        return response
      } catch (err) {
        error.value = err.message || `获取${entityName}数据失败`
        console.error(`获取${entityName}数据失败:`, err)
        throw err
      } finally {
        loading.value = false
        // 无论请求成功还是失败，都清空缓存的Promise
        globalRequestCache.delete(cacheKey)
      }
    }

    const clearData = () => {
      dataList.value = []
      error.value = null
    }

    const updateDataList = (newData) => {
      if (Array.isArray(newData)) {
        dataList.value = newData
      } else {
        console.error(`更新${entityName}数据失败：数据格式错误`)
      }
    }

    const result = {
      [dataListName]: dataList,
      loading,
      error,
      [fetchFunctionName]: fetchDataList,
      [clearFunctionName]: clearData,
      [updateFunctionName]: updateDataList
    }

    return result
  })
}
