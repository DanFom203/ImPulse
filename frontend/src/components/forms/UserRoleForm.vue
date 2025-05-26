<script setup>
import { useUserStore } from '@/stores/userStore'
import { storeToRefs } from 'pinia'

const userStore = useUserStore()
const { user, prettyRole } = storeToRefs(userStore)
</script>

<template>
  <div class="user-role-card">
    <h3>Информация о пользователе</h3>
    <p><strong>Роль:</strong> {{ prettyRole }}</p>

    <router-link
        v-if="user.authority === 'ADMIN'"
        to="/moderation/accounts"
        class="admin-badge"
    >
      Права администратора
    </router-link>
  </div>
</template>

<style scoped>
.user-role-card {
  padding: 16px;
  border-radius: 8px;
  background-color: var(--profile-custom-bg);
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  max-width: 250px;
}

.admin-badge {
  display: inline-block;
  margin-top: 8px;
  padding: 6px 12px;
  background-color: crimson;
  color: white;
  font-weight: bold;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.admin-badge:hover {
  background-color: darkred;
}
</style>