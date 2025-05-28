<template>
  <div class="chat-widget">
    <div class="chat-header">
      <h3>Chat with {{ interlocutor.fullName }}</h3>
      <button class="close-button" @click="$emit('close')">×</button>
    </div>
    <div class="chat-messages" ref="chatContainer">
      <p v-for="(msg, index) in messages" :key="index">
        <strong>{{ msg.senderId === currentUserId ? 'You' : interlocutor.fullName }}</strong>: {{ msg.body }}
      </p>
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


<script src="@/services/websocketService.js"></script>
<style src="@/assets/chatWidget.css" scoped></style>