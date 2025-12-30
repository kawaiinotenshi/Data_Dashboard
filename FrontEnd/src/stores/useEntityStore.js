import { defineStore } from 'pinia'
import { ref } from 'vue'

export function createEntityStore(storeName, apiFunction, entityName, dataListName = 'dataList', fetchFunctionName = 'fetchDataList', clearFunctionName = 'clearData') {
  return defineStore(storeName, () => {
    const dataList = ref([])
    const loading = ref(false)
    const error = ref(null)

    const fetchDataList = async () => {
      loading.value = true
      error.value = null
      try {
        const response = await apiFunction()
        if (response.code === 200) {
          dataList.value = response.data || []
        }
      } catch (err) {
        error.value = err.message || `获取${entityName}数据失败`
        console.error(`获取${entityName}数据失败:`, err)
      } finally {
        loading.value = false
      }
    }

    const clearData = () => {
      dataList.value = []
      error.value = null
    }

    const result = {
      [dataListName]: dataList,
      loading,
      error,
      [fetchFunctionName]: fetchDataList,
      [clearFunctionName]: clearData
    }

    return result
  })
}
