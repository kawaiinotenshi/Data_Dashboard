<template>
  <div class="content-right">
    <div class="right-top">
      <div id="tab_header">
        <ul>
          <li
            :class="{ selected: activeTab === 0 }"
            @click="activeTab = 0"
          >
            仓库信息
          </li>
          <li
            :class="{ selected: activeTab === 1 }"
            @click="activeTab = 1"
          >
            进关信息
          </li>
        </ul>
      </div>
      <div id="tab_content">
        <div
          v-show="activeTab === 0"
          class="dom"
        >
          <ul>
            <li>
              <p>仓库数</p>
              <span>365</span>
              <label>个</label>
            </li>
            <li>
              <p>总面积</p>
              <span>50万</span>
              <label>平方</label>
            </li>
            <li>
              <p>总流转率</p>
              <span>150万</span>
              <label>顿</label>
            </li>
          </ul>
          <div class="list-t">
            <ul>
              <li>
                <span>150万</span>
                <p>大连仓</p>
              </li>
              <li>
                <span>50万</span>
                <p>青岛仓</p>
              </li>
              <li>
                <span>60万</span>
                <p>宁波仓</p>
              </li>
              <li>
                <span>250万</span>
                <p>义乌仓</p>
              </li>
              <li>
                <span>230万</span>
                <p>长沙仓</p>
              </li>
              <li>
                <span>110万</span>
                <p>黄埔仓</p>
              </li>
            </ul>
          </div>
        </div>
        <div
          v-show="activeTab === 1"
          class="dom"
        >
          <div class="listStyle">
            <span>美国：<strong>560</strong>万单</span>
            <span>日本：<strong>36</strong>万单</span>
            <span>沙特：<strong>540</strong>万单</span>
            <span>韩国：<strong>15</strong>万单</span>
            <span>俄罗斯：<strong>20</strong>万单</span>
            <span>德国：<strong>29</strong>万单</span>
            <span>法国：<strong>10</strong>万单</span>
            <span>新加坡：<strong>120</strong>万单</span>
          </div>
        </div>
      </div>
    </div>
    <div class="right-center">
      <div class="title">
        <span>BBC清关（当月）</span>
      </div>
      <div class="echart wenzi">
        <div class="gun">
          <span>名称</span>
          <span>同比</span>
          <span>数量</span>
        </div>
        <div
          id="FontScroll"
          class="myscroll"
        >
          <ul>
            <li
              v-for="(item, index) in scrollData"
              :key="index"
            >
              <div class="fontInner clearfix">
                <span>
                  <b>{{ item.name }}</b>
                </span>
                <span>{{ item.rate }}</span>
                <span>{{ item.amount }}</span>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="right-bottom">
      <div class="title">
        <span>仓库利用比</span>
      </div>
      <div class="right-bottom-t">
        <div
          ref="ceshi3Ref"
          class="b-left echart"
        />
        <div
          ref="ceshi4Ref"
          class="b-cent echart"
        />
        <div
          ref="ceshi5Ref"
          class="b-right echart"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { api } from '@/api/index'

const activeTab = ref(0)
const ceshi3Ref = ref(null)
const ceshi4Ref = ref(null)
const ceshi5Ref = ref(null)
let chart3 = null
let chart4 = null
let chart5 = null
let resizeObserver3 = null
let resizeObserver4 = null
let resizeObserver5 = null

const scrollData = ref([
  { name: '1.日用化工', rate: '2.3%', amount: '45顿' },
  { name: '2.普通金属成品', rate: '2.2%', amount: '44.5顿' },
  { name: '3.金属性原料', rate: '2.15%', amount: '44.3顿' },
  { name: '4.普通化工原料', rate: '2.1%', amount: '43.5顿' },
  { name: '5.芯片', rate: '2%', amount: '43顿' },
  { name: '6.大型机械', rate: '1.95%', amount: '42.6顿' },
  { name: '7.水产', rate: '1.93%', amount: '42.3顿' },
  { name: '8.主粮', rate: '1.9%', amount: '42顿' },
  { name: '9.水果', rate: '1.8%', amount: '41顿' },
  { name: '10.蔬菜', rate: '1.8%', amount: '41顿' }
])

const initChart3 = async () => {
  chart3 = echarts.init(ceshi3Ref.value)
  
  let chartData = { name: '大连仓', value: 75 }
  
  try {
    const response = await api.warehouse.list()
    if (response.code === 200 && response.data && response.data.length > 0) {
      const warehouse = response.data.find(w => w.name === '大连仓') || response.data[0]
      chartData = {
        name: warehouse.name || '大连仓',
        value: warehouse.utilizationRate || 75
      }
    }
  } catch (error) {
    console.error('获取仓库数据失败:', error)
  }
  
  const option = {
    tooltip: {
      formatter: '{a} <br/>{b} : {c}%'
    },
    series: [
      {
        name: 'Pressure',
        type: 'gauge',
        radius: '90%',
        center: ['50%', '55%'],
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 5,
        itemStyle: {
          color: '#58D9F9',
          shadowColor: 'rgba(0,138,255,0.45)',
          shadowBlur: 10,
          shadowOffsetX: 2,
          shadowOffsetY: 2
        },
        progress: {
          show: true,
          roundCap: true,
          width: 8
        },
        pointer: {
          show: true,
          length: '60%',
          width: 4,
          itemStyle: {
            color: 'auto'
          }
        },
        axisLine: {
          lineStyle: {
            width: 8,
            color: [
              [0.3, '#67e0e3'],
              [0.7, '#37a2da'],
              [1, '#fd666d']
            ]
          }
        },
        axisTick: {
          distance: -12,
          length: 5,
          lineStyle: {
            color: '#fff',
            width: 1
          }
        },
        splitLine: {
          distance: -16,
          length: 8,
          lineStyle: {
            color: '#fff',
            width: 2
          }
        },
        axisLabel: {
          color: 'inherit',
          distance: 20,
          fontSize: 10
        },
        detail: {
          backgroundColor: 'inherit',
          borderColor: '#999',
          borderWidth: 0,
          width: '60%',
          lineHeight: 20,
          height: 20,
          borderRadius: 8,
          offsetCenter: [0, '35%'],
          valueAnimation: true,
          formatter: function (value) {
            return '{value|' + value.toFixed(0) + '}{unit|%}'
          },
          rich: {
            value: {
              fontSize: 20,
              fontWeight: 'bolder',
              color: '#fff',
              fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
            },
            unit: {
              fontSize: 12,
              color: '#fff',
              padding: [0, 0, 0, 2],
              fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
            }
          }
        },
        title: {
          offsetCenter: [0, '-20%'],
          fontSize: 12,
          color: '#fff',
          fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
        },
        data: [chartData]
      }
    ]
  }
  chart3.setOption(option)

  resizeObserver3 = new ResizeObserver(() => {
    chart3.resize()
  })
  resizeObserver3.observe(ceshi3Ref.value)
}

const initChart4 = async () => {
  chart4 = echarts.init(ceshi4Ref.value)
  
  let chartData = { name: '青岛仓', value: 82 }
  
  try {
    const response = await api.warehouse.list()
    if (response.code === 200 && response.data && response.data.length > 0) {
      const warehouse = response.data.find(w => w.name === '青岛仓') || response.data[1] || response.data[0]
      chartData = {
        name: warehouse.name || '青岛仓',
        value: warehouse.utilizationRate || 82
      }
    }
  } catch (error) {
    console.error('获取仓库数据失败:', error)
  }
  
  const option = {
    tooltip: {
      formatter: '{a} <br/>{b} : {c}%'
    },
    series: [
      {
        name: 'Pressure',
        type: 'gauge',
        radius: '90%',
        center: ['50%', '55%'],
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 5,
        itemStyle: {
          color: '#58D9F9',
          shadowColor: 'rgba(0,138,255,0.45)',
          shadowBlur: 10,
          shadowOffsetX: 2,
          shadowOffsetY: 2
        },
        progress: {
          show: true,
          roundCap: true,
          width: 8
        },
        pointer: {
          show: true,
          length: '60%',
          width: 4,
          itemStyle: {
            color: 'auto'
          }
        },
        axisLine: {
          lineStyle: {
            width: 8,
            color: [
              [0.3, '#67e0e3'],
              [0.7, '#37a2da'],
              [1, '#fd666d']
            ]
          }
        },
        axisTick: {
          distance: -12,
          length: 5,
          lineStyle: {
            color: '#fff',
            width: 1
          }
        },
        splitLine: {
          distance: -16,
          length: 8,
          lineStyle: {
            color: '#fff',
            width: 2
          }
        },
        axisLabel: {
          color: 'inherit',
          distance: 20,
          fontSize: 10
        },
        detail: {
          backgroundColor: 'inherit',
          borderColor: '#999',
          borderWidth: 0,
          width: '60%',
          lineHeight: 20,
          height: 20,
          borderRadius: 8,
          offsetCenter: [0, '35%'],
          valueAnimation: true,
          formatter: function (value) {
            return '{value|' + value.toFixed(0) + '}{unit|%}'
          },
          rich: {
            value: {
              fontSize: 20,
              fontWeight: 'bolder',
              color: '#fff',
              fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
            },
            unit: {
              fontSize: 12,
              color: '#fff',
              padding: [0, 0, 0, 2],
              fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
            }
          }
        },
        title: {
          offsetCenter: [0, '-20%'],
          fontSize: 12,
          color: '#fff',
          fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
        },
        data: [chartData]
      }
    ]
  }
  chart4.setOption(option)

  resizeObserver4 = new ResizeObserver(() => {
    chart4.resize()
  })
  resizeObserver4.observe(ceshi4Ref.value)
}

const initChart5 = async () => {
  chart5 = echarts.init(ceshi5Ref.value)
  
  let chartData = { name: '宁波仓', value: 68 }
  
  try {
    const response = await api.warehouse.list()
    if (response.code === 200 && response.data && response.data.length > 0) {
      const warehouse = response.data.find(w => w.name === '宁波仓') || response.data[2] || response.data[0]
      chartData = {
        name: warehouse.name || '宁波仓',
        value: warehouse.utilizationRate || 68
      }
    }
  } catch (error) {
    console.error('获取仓库数据失败:', error)
  }
  
  const option = {
    tooltip: {
      formatter: '{a} <br/>{b} : {c}%'
    },
    series: [
      {
        name: 'Pressure',
        type: 'gauge',
        radius: '90%',
        center: ['50%', '55%'],
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 5,
        itemStyle: {
          color: '#58D9F9',
          shadowColor: 'rgba(0,138,255,0.45)',
          shadowBlur: 10,
          shadowOffsetX: 2,
          shadowOffsetY: 2
        },
        progress: {
          show: true,
          roundCap: true,
          width: 8
        },
        pointer: {
          show: true,
          length: '60%',
          width: 4,
          itemStyle: {
            color: 'auto'
          }
        },
        axisLine: {
          lineStyle: {
            width: 8,
            color: [
              [0.3, '#67e0e3'],
              [0.7, '#37a2da'],
              [1, '#fd666d']
            ]
          }
        },
        axisTick: {
          distance: -12,
          length: 5,
          lineStyle: {
            color: '#fff',
            width: 1
          }
        },
        splitLine: {
          distance: -16,
          length: 8,
          lineStyle: {
            color: '#fff',
            width: 2
          }
        },
        axisLabel: {
          color: 'inherit',
          distance: 20,
          fontSize: 10
        },
        detail: {
          backgroundColor: 'inherit',
          borderColor: '#999',
          borderWidth: 0,
          width: '60%',
          lineHeight: 20,
          height: 20,
          borderRadius: 8,
          offsetCenter: [0, '35%'],
          valueAnimation: true,
          formatter: function (value) {
            return '{value|' + value.toFixed(0) + '}{unit|%}'
          },
          rich: {
            value: {
              fontSize: 20,
              fontWeight: 'bolder',
              color: '#fff',
              fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
            },
            unit: {
              fontSize: 12,
              color: '#fff',
              padding: [0, 0, 0, 2],
              fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
            }
          }
        },
        title: {
          offsetCenter: [0, '-20%'],
          fontSize: 12,
          color: '#fff',
          fontFamily: '微软雅黑, Microsoft YaHei, sans-serif'
        },
        data: [chartData]
      }
    ]
  }
  chart5.setOption(option)

  resizeObserver5 = new ResizeObserver(() => {
    chart5.resize()
  })
  resizeObserver5.observe(ceshi5Ref.value)
}

const handleResize = () => {
  chart3 && chart3.resize()
  chart4 && chart4.resize()
  chart5 && chart5.resize()
}

onMounted(() => {
  initChart3()
  initChart4()
  initChart5()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  resizeObserver3 && resizeObserver3.disconnect()
  resizeObserver4 && resizeObserver4.disconnect()
  resizeObserver5 && resizeObserver5.disconnect()
  chart3 && chart3.dispose()
  chart4 && chart4.dispose()
  chart5 && chart5.dispose()
})
</script>
