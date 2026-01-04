<template>
  <div class="content-center">
    <div class="center-top">
      <div class="title">
        <span>供应链网络拓扑</span>
      </div>
      <div
        ref="networkChartRef"
        class="allnav echart"
      />
    </div>
    <div class="center-bottom">
      <div class="title">
        <span>关键指标</span>
      </div>
      <div class="metrics-container">
        <div class="metric-item">
          <div class="metric-value">
            1,234
          </div>
          <div class="metric-label">
            供应商总数
          </div>
        </div>
        <div class="metric-item">
          <div class="metric-value">
            5,678
          </div>
          <div class="metric-label">
            采购订单
          </div>
        </div>
        <div class="metric-item">
          <div class="metric-value">
            ¥8,976万
          </div>
          <div class="metric-label">
            采购总额
          </div>
        </div>
        <div class="metric-item">
          <div class="metric-value">
            98.5%
          </div>
          <div class="metric-label">
            准时交付率
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const networkChartRef = ref(null)
let chart = null
let resizeObserver = null

const initNetworkChart = () => {
  chart = echarts.init(networkChartRef.value)
  
  const nodes = [
    { name: '总部', symbolSize: 80, x: 300, y: 300 },
    { name: '华东', symbolSize: 60, x: 150, y: 200 },
    { name: '华南', symbolSize: 60, x: 450, y: 200 },
    { name: '华北', symbolSize: 60, x: 150, y: 400 },
    { name: '华中', symbolSize: 60, x: 450, y: 400 },
    { name: '供应商A', symbolSize: 40, x: 80, y: 150 },
    { name: '供应商B', symbolSize: 40, x: 220, y: 150 },
    { name: '供应商C', symbolSize: 40, x: 380, y: 150 },
    { name: '供应商D', symbolSize: 40, x: 520, y: 150 },
    { name: '供应商E', symbolSize: 40, x: 80, y: 450 },
    { name: '供应商F', symbolSize: 40, x: 220, y: 450 },
    { name: '供应商G', symbolSize: 40, x: 380, y: 450 },
    { name: '供应商H', symbolSize: 40, x: 520, y: 450 }
  ]
  
  const links = [
    { source: '总部', target: '华东' },
    { source: '总部', target: '华南' },
    { source: '总部', target: '华北' },
    { source: '总部', target: '华中' },
    { source: '华东', target: '供应商A' },
    { source: '华东', target: '供应商B' },
    { source: '华南', target: '供应商C' },
    { source: '华南', target: '供应商D' },
    { source: '华北', target: '供应商E' },
    { source: '华北', target: '供应商F' },
    { source: '华中', target: '供应商G' },
    { source: '华中', target: '供应商H' }
  ]
  
  const option = {
    tooltip: {},
    series: [
      {
        type: 'graph',
        layout: 'none',
        symbolSize: 50,
        roam: true,
        label: {
          show: true,
          color: '#fff'
        },
        edgeSymbol: ['circle', 'arrow'],
        edgeSymbolSize: [4, 10],
        edgeLabel: {
          fontSize: 20
        },
        data: nodes,
        links: links,
        lineStyle: {
          opacity: 0.9,
          width: 2,
          curveness: 0
        },
        itemStyle: {
          color: function(params) {
            const colors = ['#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed']
            return colors[params.dataIndex % colors.length]
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
}

onMounted(() => {
  initNetworkChart()
  
  resizeObserver = new ResizeObserver(() => chart && chart.resize())
  if (networkChartRef.value) resizeObserver.observe(networkChartRef.value)
})

onUnmounted(() => {
  if (chart) chart.dispose()
  if (resizeObserver) resizeObserver.disconnect()
})
</script>

<style scoped>
.content-center {
  width: 44%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.center-top {
  flex: 2;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  padding: 15px;
}

.center-bottom {
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
  min-height: 300px;
}

.metrics-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  height: 100%;
}

.metric-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 15px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #00f2ff;
  margin-bottom: 5px;
}

.metric-label {
  font-size: 14px;
  color: #fff;
}
</style>
