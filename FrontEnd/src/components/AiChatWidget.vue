<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref } from 'vue'

import { api } from '../api'

const chatOpen = ref(false)
const chatInput = ref('')
const chatSending = ref(false)
const waitSeconds = ref(0)
let waitTimerId = null

// 聊天消息列表
const chatMessages = ref([
  {
    id: `m_${Date.now()}`,
    role: 'assistant',
    content: '你好，我是 AI 助手。你可以在这里提问。',
    ts: Date.now(),
  },
])
// 聊天列表容器元素引用（用于滚动到底部）
const chatListEl = ref(null)

// 滚动聊天列表到底部（等待下一次 DOM 更新）
const scrollChatToBottom = async () => {
  await nextTick()
  const el = chatListEl.value
  if (!el) return
  el.scrollTop = el.scrollHeight
}

// 切换聊天窗口打开/关闭状态
const toggleChat = async () => {
  chatOpen.value = !chatOpen.value
  if (chatOpen.value) await scrollChatToBottom()
}

// 关闭聊天窗口
const closeChat = () => {
  chatOpen.value = false
}

// 启动等待计时器
const startWaitTimer = () => {
  stopWaitTimer()
  waitSeconds.value = 0
  waitTimerId = window.setInterval(() => {
    waitSeconds.value += 1
  }, 1000)
}

// 停止等待计时器
const stopWaitTimer = () => {
  if (waitTimerId != null) {
    clearInterval(waitTimerId)
    waitTimerId = null
  }
  waitSeconds.value = 0
}

// 发送聊天消息（并等待回复）
const sendChat = async () => {
  const text = String(chatInput.value || '').trim()
  if (!text || chatSending.value) return

  chatSending.value = true
  chatInput.value = ''

// 把用户消息追加到消息列表里，并滚动到底部
  chatMessages.value.push({
    id: `m_${Date.now()}_u`,
    role: 'user',
    content: text,
    ts: Date.now(),
  })

  // 在消息列表里先放一个“等待回复”的占位气泡，并启动计时
  const waitingId = `m_${Date.now()}_w`
  chatMessages.value.push({
    id: waitingId,
    role: 'assistant',
    content: '正在等待 AI 回复...（已等待 0s）',
    ts: Date.now(),
  })

// 发送消息，并更新等待计时器（每秒更新一次）
  startWaitTimer()
//   等待下一次 DOM 更新，确保气泡已经渲染出来
  await scrollChatToBottom()

//   调用后端 AI 聊天接口
  try {
    // 发送消息，并等待回复（此处仅为示意，实际项目中应替换为你的后端接口调用）
    const res = await api.ai.chat({ message: text, history: chatMessages.value })
    const reply = 
      res?.data?.reply ??
      res?.data?.message ??
      (typeof res?.data === 'string' ? res.data : '')

    // 用真实回复替换“等待中”气泡
    const idx = chatMessages.value.findIndex((m) => m.id === waitingId)
    const finalText = reply || '（已发送，但后端未返回可用内容）'
    if (idx >= 0) {
      chatMessages.value[idx] = {
        ...chatMessages.value[idx],
        content: finalText,
        ts: Date.now(),
      }
    } else {
      chatMessages.value.push({
        id: `m_${Date.now()}_a`,
        role: 'assistant',
        content: finalText,
        ts: Date.now(),
      })
    }
  } catch (err) {
    // 尽量把后端真实错误信息展示出来，方便联调定位
    // - 后端 400/500 会让 Axios 走 reject(err)，此处可以从 err.response.data 取到 message
    const status = err?.response?.status
    const data = err?.response?.data
    const backendMsg = 
      (typeof data === 'string' ? data : '') ||
      data?.message ||
      data?.reply ||
      err?.message

    const detail = backendMsg ? String(backendMsg) : '（AI 请求失败）'
    const prefix = status ? `（HTTP ${status}）` : ''

    const errText = `${prefix}${detail}` || '（AI 接口未配置或请求失败：请确认后端服务与接口地址）'
    // 失败时同样替换“等待中”气泡（如果没找到则追加）
    const idx = chatMessages.value.findIndex((m) => m.id === waitingId)
    if (idx >= 0) {
      chatMessages.value[idx] = {
        ...chatMessages.value[idx],
        content: errText,
        ts: Date.now(),
      }
    } else {
      chatMessages.value.push({
        id: `m_${Date.now()}_e`,
        role: 'assistant',
        content: errText,
        ts: Date.now(),
      })
    }
  } finally {
    chatSending.value = false
    stopWaitTimer()
    await scrollChatToBottom()
  }
}

// 每秒更新“等待中”气泡的文字
// 这里不额外加新 UI，只是更新那条占位消息的内容。
onMounted(() => {
  // no-op: waitSeconds is used via interval, content update below
})

// 更新等待气泡内容
const updateWaitingBubble = () => {
  if (!chatSending.value) return
  const last = chatMessages.value[chatMessages.value.length - 1]
  if (!last) return
  // 只更新最后一条是“正在等待...”的 assistant 消息，避免误改历史内容
  if (last.role === 'assistant' && String(last.content || '').includes('正在等待 AI 回复')) {
    last.content = `正在等待 AI 回复...（已等待 ${waitSeconds.value}s）`
  }
}

// 轻量轮询：计时变化时尝试更新占位气泡
let waitTextTimerId = null
onMounted(() => {
  waitTextTimerId = window.setInterval(updateWaitingBubble, 250)
})

// 清理轮询定时器
onBeforeUnmount(() => {
  if (waitTextTimerId != null) {
    clearInterval(waitTextTimerId)
    waitTextTimerId = null
  }
  stopWaitTimer()
})

// 处理聊天输入框的回车键（Enter 发送，Shift+Enter 换行）
const onChatInputKeydown = (evt) => {
  if (evt.key === 'Enter' && !evt.shiftKey) {
    evt.preventDefault()
    void sendChat()
  }
}

// 全局键盘事件监听：按 Esc 键关闭聊天窗口
const onGlobalKeydown = (evt) => {
  if (evt.key === 'Escape' && chatOpen.value) closeChat()
}

// 注册/注销全局键盘事件监听
onMounted(() => {
  window.addEventListener('keydown', onGlobalKeydown)
})

// 注销全局键盘事件监听（组件卸载时）
onBeforeUnmount(() => {
  window.removeEventListener('keydown', onGlobalKeydown)
})
</script>

<template>
  <!-- 右侧浮动 AI 聊天入口/面板（不影响主布局） -->
  <button
    class="chat-fab"
    type="button"
    :aria-expanded="chatOpen ? 'true' : 'false'"
    aria-controls="admin-ai-chat"
    @click="toggleChat"
  >
    AI
  </button>

  <section
    v-if="chatOpen"
    id="admin-ai-chat"
    class="chat-panel"
    role="dialog"
    aria-label="AI 聊天"
  >
    <header class="chat-header">
      <div class="chat-title">
        AI 聊天
      </div>
      <button
        class="btn ghost mini"
        type="button"
        @click="closeChat"
      >
        关闭
      </button>
    </header>

    <div
      ref="chatListEl"
      class="chat-messages"
    >
      <div
        v-for="m in chatMessages"
        :key="m.id"
        class="chat-message"
        :class="m.role === 'user' ? 'me' : 'bot'"
      >
        <div class="chat-bubble">
          {{ m.content }}
        </div>
      </div>
    </div>

    <div class="chat-input">
      <textarea
        v-model="chatInput"
        class="input chat-textarea"
        rows="2"
        :disabled="chatSending"
        placeholder="输入问题，回车发送（Shift+Enter 换行）"
        @keydown="onChatInputKeydown"
      />
      <button
        class="btn primary"
        type="button"
        :disabled="chatSending || !chatInput.trim()"
        @click="sendChat"
      >
        发送
      </button>
    </div>
  </section>
</template>

<style scoped>
.chat-fab {
  position: fixed;
  right: 2rem;
  bottom: 2rem;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background-color: #4f46e5;
  color: white;
  font-size: 1.2rem;
  font-weight: bold;
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  transition: all 0.3s ease;
}

.chat-fab:hover {
  background-color: #4338ca;
  transform: scale(1.05);
}

.chat-panel {
  position: fixed;
  right: 2rem;
  bottom: 6rem;
  width: 320px;
  max-height: 480px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  z-index: 1000;
}

.chat-header {
  padding: 1rem;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f9fafb;
  border-radius: 8px 8px 0 0;
}

.chat-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1f2937;
}

.chat-messages {
  flex: 1;
  padding: 1rem;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.chat-message {
  display: flex;
  max-width: 80%;
}

.chat-message.me {
  align-self: flex-end;
  justify-content: flex-end;
}

.chat-message.bot {
  align-self: flex-start;
  justify-content: flex-start;
}

.chat-bubble {
  padding: 0.75rem 1rem;
  border-radius: 12px;
  font-size: 0.9rem;
  line-height: 1.4;
}

.chat-message.me .chat-bubble {
  background-color: #4f46e5;
  color: white;
  border-bottom-right-radius: 4px;
}

.chat-message.bot .chat-bubble {
  background-color: #f3f4f6;
  color: #374151;
  border-bottom-left-radius: 4px;
}

.chat-input {
  padding: 1rem;
  border-top: 1px solid #e5e7eb;
  display: flex;
  gap: 0.5rem;
  align-items: flex-end;
}

.chat-textarea {
  flex: 1;
  resize: vertical;
  min-height: 40px;
  max-height: 120px;
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.9rem;
  line-height: 1.4;
}

.chat-textarea:focus {
  outline: none;
  border-color: #4f46e5;
  box-shadow: 0 0 0 2px rgba(79, 70, 229, 0.2);
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn.primary {
  background-color: #4f46e5;
  color: white;
}

.btn.primary:hover {
  background-color: #4338ca;
}

.btn.primary:disabled {
  background-color: #c7d2fe;
  cursor: not-allowed;
}

.btn.ghost {
  background-color: transparent;
  color: #6b7280;
}

.btn.ghost:hover {
  background-color: #f3f4f6;
}

.btn.mini {
  padding: 0.25rem 0.5rem;
  font-size: 0.8rem;
}
</style>