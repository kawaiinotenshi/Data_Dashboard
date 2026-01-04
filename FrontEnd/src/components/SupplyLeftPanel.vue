<template>
  <div class="content-left">
    <div class="left-top">
      <div class="title">
        <span>供应商分布</span>
      </div>
      <div
        ref="supplierChartRef"
        class="allnav echart"
      />
    </div>
    <div class="left-center">
      <div class="title">
        <span>采购趋势</span>
      </div>
      <div
        ref="purchaseChartRef"
        class="allnav echart"
      />
    </div>
    <div class="left-bottom">
      <div class="title">
        <span>供应商评级</span>
      </div>
      <div
        ref="ratingChartRef"
        class="allnav echart"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const supplierChartRef = ref(null)
const purchaseChartRef = ref(null)
const ratingChartRef = ref(null)
let chart1 = null
let chart2 = null
let chart3 = null
let resizeObserver1 = null
let resizeObserver2 = null
let resizeObserver3 = null

const initSupplierChart = async () => {
  chart1 = echarts.init(supplierChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      textStyle: {
        color: '#fff'
      }
    },
    series: [
      {
        name: '供应商分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 1048, name: '华东地区' },
          { value: 735, name: '华南地区' },
          { value: 580, name: '华北地区' },
          { value: 484, name: '华中地区' },
          { value: 300, name: '西南地区' }
        ]
      }
    ]
  }
  
  chart1.setOption(option)
}

const initPurchaseChart = async () => {
  chart2 = echarts.init(purchaseChartRef.value)
  
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
    xAxis: [
      {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
        axisTick: {
          alignWithLabel: true
        },
        axisLabel: {
          color: '#fff'
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        axisLabel: {
          color: '#fff'
        }
      }
    ],
    series: [
      {
        name: '采购金额',
        type: 'bar',
        barWidth: '60%',
        data: [120, 132, 101, 134, 90, 230, 210, 180, 200, 220, 190, 230],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  }
  
  chart2.setOption(option)
}

const initRatingChart = async () => {
  chart3 = echarts.init(ratingChartRef.value)
  
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
      type: 'value',
      axisLabel: {
        color: '#fff'
      }
    },
    yAxis: {
      type: 'category',
      data: ['A级', 'B级', 'C级', 'D级'],
      axisLabel: {
        color: '#fff'
      }
    },
    series: [
      {
        name: '供应商数量',
        type: 'bar',
        data: [45, 32, 18, 8],
        itemStyle: {
          color: function(params) {
            const colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666']
            return colors[params.dataIndex]
          }
        }
      }
    ]
  }
  
  chart3.setOption(option)
}

onMounted(() => {
  initSupplierChart()
  initPurchaseChart()
  initRatingChart()
  
  resizeObserver1 = new ResizeObserver(() => chart1 && chart1.resize())
  resizeObserver2 = new ResizeObserver(() => chart2 && chart2.resize())
  resizeObserver3 = new ResizeObserver(() => chart3 && chart3.resize())
  
  if (supplierChartRef.value) resizeObserver1.observe(supplierChartRef.value)
  if (purchaseChartRef.value) resizeObserver2.observe(purchaseChartRef.value)
  if (ratingChartRef.value) resizeObserver3.observe(ratingChartRef.value)
})

onUnmounted(() => {
  if (chart1) chart1.dispose()
  if (chart2) chart2.dispose()
  if (chart3) chart3.dispose()
  
  if (resizeObserver1) resizeObserver1.disconnect()
  if (resizeObserver2) resizeObserver2.disconnect()
  if (resizeObserver3) resizeObserver3.disconnect()
})
</script>

<style scoped>
.content-left {
  width: 28%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.left-top,
.left-center,
.left-bottom {
  flex: 1;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  padding: 15px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10px;
  border-left: 4px solid #00f2ff;
  padding-left: 10px;
}

.allnav {
  width: 100%;
  height: calc(100% - 40px);
}

.echart {
  min-height: 200px;
}
</style>
