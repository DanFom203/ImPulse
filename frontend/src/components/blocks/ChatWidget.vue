<template>
  <div class="chat-widget">
    <div class="chat-header">
      <h3>Chat with {{ interlocutor.fullName }}</h3>
      <button @click="$emit('close')">×</button>
    </div>
    <div class="chat-messages">
      <p>Пример сообщения</p>
    </div>
    <div class="chat-input">
      <input type="text" placeholder="Type a message..." />
      <button>Send</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatWidget',
  props: {
    interlocutor: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>
.chat-widget {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 300px;
  border: 1px solid #ccc;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border-radius: 8px;
  overflow: hidden;
  z-index: 999;
}

.chat-header {
  background: #f5f5f5;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.chat-messages {
  padding: 10px;
  height: 200px;
  overflow-y: auto;
  background: #fafafa;
}

.chat-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #eee;
}

.chat-input input {
  flex: 1;
  padding: 5px;
  margin-right: 5px;
}

.chat-input button {
  padding: 5px 10px;
}
</style>
