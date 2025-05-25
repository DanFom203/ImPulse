<template>
  <div class="chat-widget">
    <div class="chat-header">
      <h3>Chat with {{ interlocutor.fullName }}</h3>
      <button @click="$emit('close')">×</button>
    </div>
    <div class="chat-messages">
      <p v-for="(msg, index) in messages" :key="index">{{ msg.body }}</p>
    </div>
    <div class="chat-input">
      <input
          type="text"
          v-model="messageText"
          @keyup.enter="sendMessage"
          placeholder="Type a message..."
      />
      <button @click="sendMessage">Send</button>
    </div>
  </div>
</template>
<script>
import { Stomp } from '@stomp/stompjs'
import { getTokenFromStorage } from '@/services/localData.js' // поправь путь

export default {
  props: {
    interlocutor: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      stompClient: null,
      connected: false,
      messages: [],
      messageText: '',
    }
  },
  mounted() {
    this.connect()
  },
  methods: {
    connect() {
      const token = getTokenFromStorage();
      if (!token) {
        alert('Not authenticated');
        return;
      }

      this.stompClient = Stomp.client('ws://localhost:8080/ws');
      this.stompClient.connect(
          { Authorization: `Bearer ${token}` },
          (frame) => {
            this.connected = true;
            this.stompClient.subscribe('/user/queue/reply', (message) => {
              const msg = JSON.parse(message.body);
              this.messages.push(msg);
            });
          },
          (error) => {
            console.error('STOMP connection error:', error);
          }
      );
    },

    sendMessage() {
      if (!this.connected) return;

      const token = getTokenFromStorage();
      if (!token) {
        alert('Not authenticated');
        return;
      }

      const payload = {
        receiverId: this.interlocutor.id,
        body: this.messageText,
      };

      this.stompClient.send(
          '/app/chat',
          { Authorization: `Bearer ${token}` },  // <-- Передаём токен в заголовке
          JSON.stringify(payload)
      );

      this.messageText = '';
    }

  }}


</script>

<style scoped>
.chat-widget {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 300px;
  border: 1px solid #ccc;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
