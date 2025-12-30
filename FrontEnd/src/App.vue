<template>
  <div ref="mainRef" class="main">
    <div class="header">
      <div id="time" class="header-left">
        {{ currentTime }}
      </div>
      <div class="header-cen">
        <h1>物流仓储大数据展示</h1>
      </div>
      <div class="header-right" />
    </div>

    <div class="content">
      <LeftPanel />
      <CenterPanel />
      <RightPanel />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import LeftPanel from './components/LeftPanel.vue'
import CenterPanel from './components/CenterPanel.vue'
import RightPanel from './components/RightPanel.vue'

const mainRef = ref(null)
const currentTime = ref('')

const updateTime = () => {
  const myDate = new Date()
  const myYear = myDate.getFullYear()
  const myMonth = myDate.getMonth() + 1
  const myToday = myDate.getDate()
  const myDay = myDate.getDay()
  const myHour = myDate.getHours()
  const myMinute = myDate.getMinutes()
  const mySecond = myDate.getSeconds()
  const week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']

  const fillZero = str => {
    return str < 10 ? '0' + str : str
  }

  currentTime.value = `${myYear}-${fillZero(myMonth)}-${fillZero(myToday)} ${fillZero(myHour)}:${fillZero(myMinute)}:${fillZero(mySecond)} ${week[myDay]}`
}

const setScale = () => {
  const designWidth = 1920
  const designHeight = 1080
  const clientWidth = document.documentElement.clientWidth
  const clientHeight = document.documentElement.clientHeight
  const scaleX = clientWidth / designWidth
  const scaleY = clientHeight / designHeight
  
  const scale = scaleX * 0.95

  if (mainRef.value) {
    mainRef.value.style.transform = `scale(${scale})`
    mainRef.value.style.transformOrigin = 'center center'
    mainRef.value.style.width = `${designWidth}px`
    mainRef.value.style.height = `${designHeight}px`
    mainRef.value.style.position = 'absolute'
    mainRef.value.style.left = '50%'
    mainRef.value.style.top = '50%'
    mainRef.value.style.marginLeft = `-${designWidth / 2}px`
    mainRef.value.style.marginTop = `-${designHeight / 2}px`
  }
}

let timer = null

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  setScale()
  window.addEventListener('resize', setScale)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
  window.removeEventListener('resize', setScale)
})
</script>

<style scoped>
.main {
  width: 1920px;
  height: 1080px;
  background: url('/bj.png') no-repeat center center;
  background-size: cover;
  transition: transform 0.1s ease-out;
}
</style>
