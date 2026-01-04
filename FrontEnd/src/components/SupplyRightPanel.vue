<template>
  <div class="content-right">
    <div class="right-top">
      <div class="title">
        <span>采购成本分析</span>
      </div>
      <div
        ref="costChartRef"
        class="allnav echart"
      />
    </div>
    <div class="right-center">
      <div class="title">
        <span>供应商绩效</span>
      </div>
      <div
        ref="performanceChartRef"
        class="allnav echart"
      />
    </div>
    <div class="right-bottom">
      <div class="title">
        <span>风险预警</span>
      </div>
      <div class="warning-list">
        <div
          v-for="(item, index) in warnings"
          :key="index"
          class="warning-item"
        >
          <div
            class="warning-icon"
            :class="item.level"
          >
            {{ item.level }}
          </div>
          <div class="warning-content">
            <div class="warning-title">
              {{ item.title }}
            </div>
            <div class="warning-desc">
              {{ item.desc }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const costChartRef = ref(null)
const performanceChartRef = ref(null)
let chart1 = null
let chart2 = null
let resizeObserver1 = null
let resizeObserver2 = null

const warnings = ref([
  { level: 'high', title: '供应商A交货延迟', desc: '延迟3天，影响订单#12345' },
  { level: 'medium', title: '原材料价格波动', desc: '钢材价格上涨15%' },
  { level: 'low', title: '库存预警', desc: '物料B库存低于安全线' },
  { level: 'high', title: '供应商C质量异常', desc: '批次#67890不合格率超标' },
  { level: 'medium', title: '物流延误', desc: '华南地区物流受阻' }
])

const initCostChart = async () => {
  chart1 = echarts.init(costChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      data: ['原材料', '运输', '仓储', '其他'],
      textStyle: {
        color: '#fff'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
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
        name: '原材料',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: [320, 332, 301, 334, 390, 330, 320, 340, 360, 380, 370, 390]
      },
      {
        name: '运输',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: [120, 132, 101, 134, 90, 230, 210, 180, 200, 220, 190, 230]
      },
      {
        name: '仓储',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: [220, 182, 191, 234, 290, 330, 310, 280, 260, 240, 270, 290]
      },
      {
        name: '其他',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: [150, 232, 201, 154, 190, 330, 410, 380, 360, 340, 370, 390]
      }
    ]
  }
  
  chart1.setOption(option)
}

const initPerformanceChart = async () => {
  chart2 = echarts.init(performanceChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    radar: {
      indicator: [
        { name: '质量', max: 100 },
        { name: '交付', max: 100 },
        { name: '成本', max: 100 },
        { name: '服务', max: 100 },
        { name: '创新', max: 100 }
      ],
      axisName: {
        color: '#fff'
      },
      splitArea: {
        areaStyle: {
          color: ['rgba(255,255,255,0.1)', 'rgba(255,255,255,0.05)']
        }
      }
    },
    series: [
      {
        name: '供应商绩效',
        type: 'radar',
        data: [
          {
            value: [90, 85, 80, 88, 75],
            name: '供应商A'
          },
          {
            value: [85, 90, 85, 82, 80],
            name: '供应商B'
          },
          {
            value: [80, 75, 90, 85, 85],
            name: '供应商C'
          }
        ]
      }
    ]
  }
  
  chart2.setOption(option)
}

onMounted(() => {
  initCostChart()
  initPerformanceChart()
  
  resizeObserver1 = new ResizeObserver(() => chart1 && chart1.resize())
  resizeObserver2 = new ResizeObserver(() => chart2 && chart2.resize())
  
  if (costChartRef.value) resizeObserver1.observe(costChartRef.value)
  if (performanceChartRef.value) resizeObserver2.observe(performanceChartRef.value)
})

onUnmounted(() => {
  if (chart1) chart1.dispose()
  if (chart2) chart2.dispose()
  
  if (resizeObserver1) resizeObserver1.disconnect()
  if (resizeObserver2) resizeObserver2.disconnect()
})
</script>

<style scoped>
.content-right {
  width: 28%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.right-top,
.right-center,
.right-bottom {
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

.warning-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  height: calc(100% - 40px);
  overflow-y: auto;
}

.warning-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 5px;
  border-left: 3px solid;
}

.warning-item.high {
  border-left-color: #ff4444;
}

.warning-item.medium {
  border-left-color: #ffbb33;
}

.warning-item.low {
  border-left-color: #00C851;
}

.warning-icon {
  min-width: 50px;
  padding: 5px 10px;
  border-radius: 3px;
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  text-transform: uppercase;
}

.warning-item.high .warning-icon {
  background: #ff4444;
  color: #fff;
}

.warning-item.medium .warning-icon {
  background: #ffbb33;
  color: #fff;
}

.warning-item.low .warning-icon {
  background: #00C851;
  color: #fff;
}

.warning-content {
  flex: 1;
}

.warning-title {
  font-size: 14px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 3px;
}

.warning-desc {
  font-size: 12px;
  color: #ccc;
}
</style>
