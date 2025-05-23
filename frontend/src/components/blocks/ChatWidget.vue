<template>
  <div class="chat-widget">
    <div class="chat-header">
      <h3>Chat with {{ interlocutor.fullName }}</h3>
      <button @click="$emit('close')">×</button>
    </div>

    <div class="chat-messages">
      <p v-for="msg in messages" :key="msg.id">
        {{ msg.body }}
      </p>
    </div>

    <div class="chat-input">
      <input
          :id="'message-input-${interlocutor.id}'"
          :name="'message-${interlocutor.id}'"
          v-model="newMessage"
          type="text"
          placeholder="Type a message..."
          :disabled="isConnecting"
      @keyup.enter="send"
      >
      <button @click="send" :disabled="isConnecting"> <!-- 5. Используем свойство -->
        {{ isConnecting ? 'Connecting...' : 'Send' }} <!-- 6. Отображаем состояние -->
      </button>
    </div>
  </div>
</template>

<script>
import { connectWebSocket, sendMessage } from '@/services/websocketService';
import { useUserStore } from '@/stores/userStore';
import { getTokenFromStorage } from '@/services/localData.js';

export default {
  name: 'ChatWidget',
  props: {
    interlocutor: Object
  },
  data() {
    return {
      newMessage: '',
      messages: [],
      isConnecting: false // 1. Добавляем свойство в data
    };
  },
  async mounted() {
    try {
      this.isConnecting = true; // 2. Устанавливаем флаг подключения
      await connectWebSocket(this.handleMessage);
    } catch (error) {
      console.error('Connection error:', error);
      this.$router.push('/login');
    } finally {
      this.isConnecting = false; // 3. Сбрасываем флаг в любом случае
    }
  },
  methods: {
    handleMessage(msg) {
      this.messages.push(msg);
    },
    async send() {
      if (!this.newMessage.trim()) return;

      try {
        const messagePayload = {
          receiverId: this.interlocutor.id,
          body: this.newMessage
        };

        await sendMessage(messagePayload);
        this.messages.push({
          id: Date.now(),
          body: this.newMessage,
          sender: 'me'
        });
        this.newMessage = '';
      } catch (error) {
        console.error('Send failed:', error);
        alert('Message sending failed. Please try again.');
      }
    }
  }
};
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
  color: black;
}

.chat-messages {
  padding: 10px;
  height: 200px;
  overflow-y: auto;
  background: #fafafa;
  color: black;
  color: black;
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
