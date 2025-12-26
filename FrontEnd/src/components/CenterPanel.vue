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
        <div ref="ceshi8Ref" class="allnav echart" />
      </div>
    </div>
    <div class="center-bottom">
      <div class="bottom-left">
        <div ref="ceshi6Ref" class="allnav echart" />
      </div>
      <div class="bottom-right">
        <div ref="ceshi7Ref" class="allnav echart" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const ceshi8Ref = ref(null)
const ceshi6Ref = ref(null)
const ceshi7Ref = ref(null)
let chart8 = null
let chart6 = null
let chart7 = null
let resizeObserver8 = null
let resizeObserver6 = null
let resizeObserver7 = null

const initChart8 = async () => {
  chart8 = echarts.init(ceshi8Ref.value)
  try {
    const response = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')
    const chinaJson = await response.json()
    echarts.registerMap('china', chinaJson)
  } catch (error) {
    console.error('加载中国地图数据失败:', error)
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
          normal: {
            borderColor: '#389BB7',
            areaColor: '#389BB7'
          },
          emphasis: {
            areaColor: '#2a333d'
          }
        },
        data: [
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
      }
    ]
  }
  chart8.setOption(option)

  resizeObserver8 = new ResizeObserver(() => {
    chart8.resize()
  })
  resizeObserver8.observe(ceshi8Ref.value)
}

const initChart6 = () => {
  chart6 = echarts.init(ceshi6Ref.value)
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
        data: [120, 132, 101, 134, 90, 230, 210],
        itemStyle: {
          color: '#5bc0de'
        }
      },
      {
        name: '出库量',
        type: 'line',
        data: [220, 182, 191, 234, 290, 330, 310],
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

const initChart7 = () => {
  chart7 = echarts.init(ceshi7Ref.value)
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
        data: [320, 302, 301, 334],
        itemStyle: {
          color: '#8d7fec'
        }
      },
      {
        name: '运输',
        type: 'bar',
        stack: 'total',
        data: [120, 132, 101, 134],
        itemStyle: {
          color: '#5085f2'
        }
      },
      {
        name: '配送',
        type: 'bar',
        stack: 'total',
        data: [220, 182, 191, 234],
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

onMounted(() => {
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
