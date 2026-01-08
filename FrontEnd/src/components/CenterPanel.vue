<template>
  <div class="content-center">
    <div class="center-top">
      <div class="top-title">
        <ul>
          <li>
            <p>全国仓库</p>
            <span>3</span>
            <span>6</span>
            <span>5</span>
            <b>个</b>
          </li>
          <li>
            <p>总利用率</p>
            <span>8</span>
            <span>3</span>
            <b>%</b>
          </li>
        </ul>
      </div>
      <div class="top-bottom">
        <div
          ref="ceshi8Ref"
          class="allnav echart"
        />
      </div>
    </div>
    <div class="center-bottom">
      <div class="bottom-left">
        <div
          ref="ceshi6Ref"
          class="allnav echart"
        />
      </div>
      <div class="bottom-right">
        <div
          ref="ceshi7Ref"
          class="allnav echart"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import { useWarehouseStore } from '@/stores/warehouse'
import { api } from '@/api/index'

const ceshi8Ref = ref(null)
const ceshi6Ref = ref(null)
const ceshi7Ref = ref(null)
let chart8 = null
let chart6 = null
let chart7 = null
let resizeObserver8 = null
let resizeObserver6 = null
let resizeObserver7 = null

// 使用Pinia store获取仓库数据
const warehouseStore = useWarehouseStore()
const warehouseData = computed(() => warehouseStore.entityList)
const warehouseLoading = computed(() => warehouseStore.loading)

const initChart8 = async () => {
  chart8 = echarts.init(ceshi8Ref.value)
  try {
    const response = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')
    const chinaJson = await response.json()
    echarts.registerMap('china', chinaJson)
  } catch (error) {
    console.error('加载中国地图数据失败:', error)
  }
  
  let mapData = [
    { name: '北京', value: 1500 },
    { name: '天津', value: 800 },
    { name: '上海', value: 1800 },
    { name: '重庆', value: 900 },
    { name: '河北', value: 600 },
    { name: '河南', value: 700 },
    { name: '云南', value: 400 },
    { name: '辽宁', value: 650 },
    { name: '黑龙江', value: 500 },
    { name: '湖南', value: 550 },
    { name: '安徽', value: 480 },
    { name: '山东', value: 1200 },
    { name: '新疆', value: 300 },
    { name: '江苏', value: 1600 },
    { name: '浙江', value: 1400 },
    { name: '江西', value: 450 },
    { name: '湖北', value: 580 },
    { name: '广西', value: 420 },
    { name: '甘肃', value: 320 },
    { name: '山西', value: 380 },
    { name: '内蒙古', value: 350 },
    { name: '陕西', value: 460 },
    { name: '吉林', value: 520 },
    { name: '福建', value: 850 },
    { name: '贵州', value: 390 },
    { name: '广东', value: 1900 },
    { name: '青海', value: 280 },
    { name: '西藏', value: 250 },
    { name: '四川', value: 750 },
    { name: '宁夏', value: 270 },
    { name: '海南', value: 310 },
    { name: '台湾', value: 600 },
    { name: '香港', value: 1100 },
    { name: '澳门', value: 500 }
  ]
  
  try {
    // 使用缓存的仓库数据，确保warehouseData.value是数组
    if (Array.isArray(warehouseData.value) && warehouseData.value.length > 0) {
      const cityMap = {
        '北京': '北京', '天津': '天津', '上海': '上海', '重庆': '重庆',
        '河北': '河北', '河南': '河南', '云南': '云南', '辽宁': '辽宁',
        '黑龙江': '黑龙江', '湖南': '湖南', '安徽': '安徽', '山东': '山东',
        '新疆': '新疆', '江苏': '江苏', '浙江': '浙江', '江西': '江西',
        '湖北': '湖北', '广西': '广西', '甘肃': '甘肃', '山西': '山西',
        '内蒙古': '内蒙古', '陕西': '陕西', '吉林': '吉林', '福建': '福建',
        '贵州': '贵州', '广东': '广东', '青海': '青海', '西藏': '西藏',
        '四川': '四川', '宁夏': '宁夏', '海南': '海南', '台湾': '台湾',
        '香港': '香港', '澳门': '澳门'
      }
      
      mapData = mapData.map(item => {
        const warehouse = warehouseData.value.find(w => w.location && cityMap[w.location] === item.name)
        return {
          name: item.name,
          value: warehouse ? warehouse.capacity || item.value : item.value
        }
      })
    }
  } catch (error) {
    console.error('获取仓库数据失败:', error)
  }
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    visualMap: {
      min: 0,
      max: 2000,
      left: 'left',
      top: 'bottom',
      text: ['高', '低'],
      inRange: {
        color: ['#e0f3f8', '#abd9e9', '#74add1', '#4575b4', '#313695']
      },
      calculable: true
    },
    series: [
      {
        name: '仓库分布',
        type: 'map',
        mapType: 'china',
        roam: true,
        label: {
          show: true,
          color: '#fff'
        },
        itemStyle: {
          borderColor: '#389BB7',
          areaColor: '#389BB7'
        },
        emphasis: {
          itemStyle: {
            areaColor: '#2a333d'
          }
        },
        data: mapData
      }
    ]
  }
  chart8.setOption(option)

  resizeObserver8 = new ResizeObserver(() => {
    chart8.resize()
  })
  resizeObserver8.observe(ceshi8Ref.value)
}

const initChart6 = async () => {
  chart6 = echarts.init(ceshi6Ref.value)
  
  let inboundData = [120, 132, 101, 134, 90, 230, 210]
  let outboundData = [220, 182, 191, 234, 290, 330, 310]
  
  try {
    const response = await api.transport.list()
    if (response.code === 200 && response.data) {
      inboundData = response.data.slice(0, 7).map(item => item.vehicleCount || 0)
      outboundData = response.data.slice(0, 7).map(item => item.totalDistance || 0)
    }
  } catch (error) {
    console.error('获取运输数据失败:', error)
  }
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['入库量', '出库量'],
      textStyle: {
        color: '#fff'
      }
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      axisLabel: {
        color: '#fff'
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: '#fff'
      }
    },
    series: [
      {
        name: '入库量',
        type: 'line',
        data: inboundData,
        itemStyle: {
          color: '#5bc0de'
        }
      },
      {
        name: '出库量',
        type: 'line',
        data: outboundData,
        itemStyle: {
          color: '#f59a8f'
        }
      }
    ]
  }
  chart6.setOption(option)

  resizeObserver6 = new ResizeObserver(() => {
    chart6.resize()
  })
  resizeObserver6.observe(ceshi6Ref.value)
}

const initChart7 = async () => {
  chart7 = echarts.init(ceshi7Ref.value)
  
  let warehouseData = [320, 302, 301, 334]
  let transportData = [120, 132, 101, 134]
  let deliveryData = [220, 182, 191, 234]
  
  try {
    const response = await api.order.list()
    if (response.code === 200 && response.data) {
      const q1Data = response.data.filter(item => {
        if (!item.orderDate) return false
        const date = new Date(item.orderDate)
        return date.getMonth() >= 0 && date.getMonth() <= 2
      })
      const q2Data = response.data.filter(item => {
        if (!item.orderDate) return false
        const date = new Date(item.orderDate)
        return date.getMonth() >= 3 && date.getMonth() <= 5
      })
      const q3Data = response.data.filter(item => {
        if (!item.orderDate) return false
        const date = new Date(item.orderDate)
        return date.getMonth() >= 6 && date.getMonth() <= 8
      })
      const q4Data = response.data.filter(item => {
        if (!item.orderDate) return false
        const date = new Date(item.orderDate)
        return date.getMonth() >= 9 && date.getMonth() <= 11
      })
      
      warehouseData = [
        q1Data.reduce((sum, item) => sum + (item.orderAmount || 0), 0),
        q2Data.reduce((sum, item) => sum + (item.orderAmount || 0), 0),
        q3Data.reduce((sum, item) => sum + (item.orderAmount || 0), 0),
        q4Data.reduce((sum, item) => sum + (item.orderAmount || 0), 0)
      ]
      
      transportData = [
        q1Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.3, 0),
        q2Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.3, 0),
        q3Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.3, 0),
        q4Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.3, 0)
      ]
      
      deliveryData = [
        q1Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.2, 0),
        q2Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.2, 0),
        q3Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.2, 0),
        q4Data.reduce((sum, item) => sum + (item.orderAmount || 0) * 0.2, 0)
      ]
    }
  } catch (error) {
    console.error('获取订单数据失败:', error)
  }
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['仓储', '运输', '配送'],
      textStyle: {
        color: '#fff'
      }
    },
    xAxis: {
      type: 'category',
      data: ['Q1', 'Q2', 'Q3', 'Q4'],
      axisLabel: {
        color: '#fff'
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: '#fff'
      }
    },
    series: [
      {
        name: '仓储',
        type: 'bar',
        stack: 'total',
        data: warehouseData,
        itemStyle: {
          color: '#8d7fec'
        }
      },
      {
        name: '运输',
        type: 'bar',
        stack: 'total',
        data: transportData,
        itemStyle: {
          color: '#5085f2'
        }
      },
      {
        name: '配送',
        type: 'bar',
        stack: 'total',
        data: deliveryData,
        itemStyle: {
          color: '#e75fc3'
        }
      }
    ]
  }
  chart7.setOption(option)

  resizeObserver7 = new ResizeObserver(() => {
    chart7.resize()
  })
  resizeObserver7.observe(ceshi7Ref.value)
}

const handleResize = () => {
  chart8 && chart8.resize()
  chart6 && chart6.resize()
  chart7 && chart7.resize()
}

onMounted(async () => {
  // 先从store获取仓库数据
  await warehouseStore.fetchWarehouseList()
  // 然后初始化图表
  initChart8()
  initChart6()
  initChart7()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  resizeObserver8 && resizeObserver8.disconnect()
  resizeObserver6 && resizeObserver6.disconnect()
  resizeObserver7 && resizeObserver7.disconnect()
  chart8 && chart8.dispose()
  chart6 && chart6.dispose()
  chart7 && chart7.dispose()
})
</script>
