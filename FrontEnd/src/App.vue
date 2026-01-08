<template>
  <router-view />
  <AiChatWidget />
</template>

<script setup>
import AiChatWidget from './components/AiChatWidget.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  overflow: hidden;
}
</style>
