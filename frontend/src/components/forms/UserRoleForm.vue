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
  border-radius: 12px;
  background-color: var(--profile-custom-bg);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  max-width: 250px;
  transition: box-shadow 0.3s;
}

.user-role-card:hover {
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.1);
}

.user-role-card h3 {
  font-size: 1.2rem;
  margin-bottom: 12px;
}

.user-role-card p {
  font-size: 1rem;
  margin: 6px 0;
}

.admin-badge {
  display: inline-block;
  margin-top: 12px;
  padding: 6px 12px;
  background-color: crimson;
  color: white;
  font-weight: 600;
  text-decoration: none;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.admin-badge:hover {
  background-color: darkred;
}
</style>