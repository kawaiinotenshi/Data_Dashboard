<template>
  <div class="content-left">
    <div class="left-top">
      <div class="title">
        <span>基本信息</span>
      </div>
      <div class="list">
        <p>
          托各国丰富的贸易资源、交通网络资源以及先进的仓储设施资源而搭建，集跨境资源协调、优化、运作于一体，涵盖高效仓储服务、便捷物流运输、跨国贸易供应链整合。
        </p>
        <ul>
          <li>建筑面积：11.68万平方</li>
          <li>总数：365</li>
          <li>容积总数：13万</li>
          <li>年总产值：4546万</li>
        </ul>
      </div>
    </div>
    <div class="left-center">
      <div class="title">
        <span>各个企业的库存占比</span>
      </div>
      <div ref="ceshiRef" class="allnav echart" />
    </div>
    <div class="left-bottom">
      <div class="title">
        <span>BBC清关(按年)</span>
      </div>
      <div ref="ceshi2Ref" class="allnav echart" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { api } from '@/api/index'
import { useOrderStore } from '@/stores/order'

const ceshiRef = ref(null)
const ceshi2Ref = ref(null)
const orderStore = useOrderStore()
let chart1 = null
let chart2 = null
let resizeObserver1 = null
let resizeObserver2 = null

const initChart1 = async () => {
  chart1 = echarts.init(ceshiRef.value)
  let ydata = [
    { name: '京东', value: 18 },
    { name: '拼多多', value: 16 },
    { name: '淘宝', value: 15 },
    { name: '唯品会', value: 14 },
    { name: '跨境仓1', value: 10 },
    { name: '东南亚仓1', value: 7.9 },
    { name: '西欧仓1', value: 6.7 },
    { name: '北欧仓1', value: 6 },
    { name: '航空专用仓', value: 4.5 },
    { name: '大型设备专用仓', value: 3 }
  ]
  try {
    const response = await api.inventory.getEnterpriseRatio()
    console.log('企业库存占比API响应:', response)
    if (response.code === 200 && response.data && response.data.length > 0) {
      ydata = response.data.map(item => ({
        name: item.enterprise_name || item.enterpriseName || '未知企业',
        value: item.ratio || 0
      }))
      console.log('处理后的图表数据:', ydata)
    }
  } catch (error) {
    console.error('获取企业库存占比失败:', error)
  }
  const color = [
    '#8d7fec',
    '#5085f2',
    '#e75fc3',
    '#f87be2',
    '#f2719a',
    '#fca4bb',
    '#f59a8f',
    '#fdb301',
    '#57e7ec',
    '#cf9ef1'
  ]
  const xdata = ydata.map(item => item.name)

  const option = {
    color: color,
    legend: {
      orient: 'vertical',
      x: 'left',
      top: 'center',
      left: '53%',
      bottom: '0%',
      data: xdata,
      itemWidth: 8,
      itemHeight: 8,
      textStyle: {
        color: '#fff',
        fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
      },
      formatter: function (name) {
        return '' + name
      }
    },
    series: [
      {
        type: 'pie',
        clockwise: false,
        minAngle: 2,
        radius: ['20%', '60%'],
        center: ['30%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderColor: '#ffffff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center',
          fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '12',
            fontWeight: 'bold',
            fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
          }
        },
        labelLine: {
          show: false
        },
        data: ydata
      }
    ]
  }
  chart1.setOption(option)

  resizeObserver1 = new ResizeObserver(() => {
    chart1.resize()
  })
  resizeObserver1.observe(ceshiRef.value)
}

const initChart2 = async () => {
  chart2 = echarts.init(ceshi2Ref.value)
  let barData = [120, 132, 101, 134, 90, 230, 210, 180, 200, 190, 220, 250]
  if (orderStore.orderList.length > 0) {
    barData = orderStore.orderList.slice(0, 12).map(item => item.orderAmount || 0)
  }
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
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
        name: '清关量',
        type: 'bar',
        data: barData,
        itemStyle: {
          color: '#5bc0de'
        }
      }
    ]
  }
  chart2.setOption(option)

  resizeObserver2 = new ResizeObserver(() => {
    chart2.resize()
  })
  resizeObserver2.observe(ceshi2Ref.value)
}

const handleResize = () => {
  chart1 && chart1.resize()
  chart2 && chart2.resize()
}

onMounted(async () => {
  initChart1()
  initChart2()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  resizeObserver1 && resizeObserver1.disconnect()
  resizeObserver2 && resizeObserver2.disconnect()
  chart1 && chart1.dispose()
  chart2 && chart2.dispose()
})
</script>
