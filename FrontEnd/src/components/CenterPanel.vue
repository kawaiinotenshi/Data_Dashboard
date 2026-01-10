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
const warehouseData = computed(() => warehouseStore.warehouseList)

// 城市坐标映射
  const cityCoordinates = {
    '北京': [116.4074, 39.9042],
    '天津': [117.2008, 39.0842],
    '上海': [121.4737, 31.2304],
    '重庆': [106.5501, 29.5630],
    '深圳': [114.0579, 22.5431],
    '广州': [113.2644, 23.1291],
    '杭州': [120.1551, 30.2741],
    '南京': [118.7969, 32.0603],
    '成都': [104.0668, 30.5728],
    '武汉': [114.3055, 30.5928],
    '西安': [108.9398, 34.3416],
    '苏州': [120.5853, 31.2989],
    '郑州': [113.6254, 34.7466],
    '青岛': [120.3826, 36.0671],
    '宁波': [121.5498, 29.8683],
    '长沙': [112.9388, 28.2282],
    '大连': [121.6147, 38.9140],
    '厦门': [118.0894, 24.4798],
    '济南': [117.1200, 36.6513],
    '哈尔滨': [126.6425, 45.7560],
    '沈阳': [123.4328, 41.8045],
    '福州': [119.2965, 26.0745],
    '长春': [125.3245, 43.8171],
    '石家庄': [114.5149, 38.0428],
    '昆明': [102.7122, 25.0406],
    '南昌': [115.8922, 28.6820],
    '贵阳': [106.7135, 26.5783],
    '太原': [112.5489, 37.8570],
    '南宁': [108.3200, 22.8241],
    '合肥': [117.2831, 31.8612],
    '乌鲁木齐': [87.6168, 43.8256],
    '兰州': [103.8343, 36.0611],
    '呼和浩特': [111.6708, 40.8183],
    '海口': [110.1999, 20.0440],
    '银川': [106.2323, 38.4865],
    '西宁': [101.7782, 36.6172],
    '拉萨': [91.1175, 29.6469],
    '呼和浩特': [111.6708, 40.8183],
    '拉萨': [91.1175, 29.6469]
  }

  // 库存异常的仓库列表
  const alertWarehouses = ref([])
  // 运输路线数据
  const transportRoutes = ref([])

  // 初始化地图
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
    
    // 准备仓库散点数据
    let scatterData = []
    let normalWarehouses = []
    let warningWarehouses = []
    let errorWarehouses = []
    
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
        
        // 更新地图数据
        mapData = mapData.map(item => {
          const warehouse = warehouseData.value.find(w => w.location && cityMap[w.location] === item.name)
          return {
            name: item.name,
            value: warehouse ? warehouse.capacity || item.value : item.value
          }
        })
        
        // 准备散点数据
        warehouseData.value.forEach(warehouse => {
          const city = warehouse.location || '北京'
          const coords = cityCoordinates[city]
          
          // 严格检查坐标有效性
          if (coords && Array.isArray(coords) && coords.length === 2) {
            const [lng, lat] = coords
            // 确保经纬度是有效的数字
            if (typeof lng === 'number' && typeof lat === 'number' && !isNaN(lng) && !isNaN(lat)) {
              const scatterItem = {
                name: warehouse.name,
                value: [lng, lat, warehouse.utilizationRate || 0],
                warehouseId: warehouse.id,
                utilizationRate: warehouse.utilizationRate || 0,
                capacity: warehouse.capacity || 0,
                status: warehouse.status || 0 // 0: 正常, 1: 低库存警告, 2: 高库存警告
              }
              
              scatterData.push(scatterItem)
              
              // 根据仓库状态分类
              if (scatterItem.status === 1) {
                // 低库存警告
                warningWarehouses.push(scatterItem)
              } else if (scatterItem.status === 2) {
                // 高库存警告
                errorWarehouses.push(scatterItem)
              } else {
                // 正常
                normalWarehouses.push(scatterItem)
              }
            }
          }
        })
      }
    } catch (error) {
      console.error('获取仓库数据失败:', error)
    }
    
      // 准备运输路线数据
    let routesData = []
    transportRoutes.value.forEach(route => {
      // 确保origin和destination是城市名称，而不是直接的经纬度
      const originCity = route.origin || '北京'
      const destinationCity = route.destination || '上海'
      
      // 获取对应的经纬度坐标
      const originCoords = cityCoordinates[originCity]
      const destinationCoords = cityCoordinates[destinationCity]
      
      // 严格验证两端城市坐标的有效性
      const isValidOrigin = originCoords && Array.isArray(originCoords) && originCoords.length === 2 && 
                           typeof originCoords[0] === 'number' && typeof originCoords[1] === 'number' && 
                           !isNaN(originCoords[0]) && !isNaN(originCoords[1])
                            
      const isValidDestination = destinationCoords && Array.isArray(destinationCoords) && destinationCoords.length === 2 && 
                               typeof destinationCoords[0] === 'number' && typeof destinationCoords[1] === 'number' && 
                               !isNaN(destinationCoords[0]) && !isNaN(destinationCoords[1])
      
      // 只有当两端城市都有有效坐标时，才添加这条路线
      if (isValidOrigin && isValidDestination) {
        routesData.push({
          coords: [originCoords, destinationCoords],
          lineStyle: {
            color: route.status === 'pending' ? '#faad14' : '#1890ff'
          },
          effect: {
            color: route.status === 'pending' ? '#faad14' : '#1890ff'
          }
        })
      }
    })
    
    // 确保地图数据已经加载完成后再添加飞线系列
    const series = [
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
      },
      {
        name: '正常仓库',
        type: 'scatter',
        coordinateSystem: 'geo',
        symbolSize: 8,
        itemStyle: {
          color: '#52c41a'
        },
        data: normalWarehouses
      },
      {
        name: '低库存警告',
        type: 'scatter',
        coordinateSystem: 'geo',
        symbolSize: 12,
        itemStyle: {
          color: '#faad14',
          borderColor: '#fff',
          borderWidth: 2
        },
        // 闪烁效果
        effect: {
          show: true,
          period: 2,
          trailLength: 0.1,
          symbol: 'circle',
          symbolSize: 20,
          color: '#faad14'
        },
        data: warningWarehouses
      },
      {
        name: '高库存警告',
        type: 'scatter',
        coordinateSystem: 'geo',
        symbolSize: 15,
        itemStyle: {
          color: '#f5222d',
          borderColor: '#fff',
          borderWidth: 2
        },
        // 闪烁效果
        effect: {
          show: true,
          period: 1,
          trailLength: 0.2,
          symbol: 'circle',
          symbolSize: 25,
          color: '#f5222d'
        },
        data: errorWarehouses
      }
    ]
    
    // 只有当有有效路线数据时，才添加飞线系列
    if (routesData.length > 0) {
      series.push({
        name: '运输路线',
        type: 'lines',
        coordinateSystem: 'geo',
        zlevel: 2,
        effect: {
          show: true,
          period: 4,
          trailLength: 0.3,
          symbol: 'arrow',
          symbolSize: 5
        },
        lineStyle: {
          normal: {
            color: '#1890ff',
            width: 2,
            opacity: 0.8,
            curveness: 0.3
          }
        },
        data: routesData
      })
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
      series: series
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
  
  let warehouseSeriesData = [320, 302, 301, 334]
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
      
      warehouseSeriesData = [
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
        data: warehouseSeriesData,
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

// 处理运输路线更新
const handleTransportRoutesUpdate = (event) => {
  transportRoutes.value = event.detail
  initChart8() // 重新初始化地图以显示新的路线
}

onMounted(async () => {
  // 先从store获取仓库数据
  await warehouseStore.fetchWarehouseList()
  // 然后初始化图表
  initChart8()
  initChart6()
  initChart7()
  window.addEventListener('resize', handleResize)
  // 添加运输路线更新事件监听
  window.addEventListener('transportRoutesUpdated', handleTransportRoutesUpdate)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  window.removeEventListener('transportRoutesUpdated', handleTransportRoutesUpdate)
  resizeObserver8 && resizeObserver8.disconnect()
  resizeObserver6 && resizeObserver6.disconnect()
  resizeObserver7 && resizeObserver7.disconnect()
  chart8 && chart8.dispose()
  chart6 && chart6.dispose()
  chart7 && chart7.dispose()
})
</script>
