<script setup>
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import MainLayout from '@/components/blocks/MainLayout.vue'
import defaultAvatar from '@/assets/logo.svg'
import { useModeratorStore } from '@/stores/moderatorStore'

const moderatorStore = useModeratorStore()
const { users, requestData } = storeToRefs(moderatorStore)

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleDateString('ru-RU', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

function onAuthorityChange(email, newAuthority) {
  moderatorStore.updateUserAuthority(email, newAuthority)
}

function onDeleteUser(email) {
  if (confirm('Вы уверены, что хотите удалить пользователя?')) {
    moderatorStore.deleteUser(email)
  }
}

onMounted(() => {
  moderatorStore.fetchAllUsers()
})
</script>

<template>
  <MainLayout>
    <div class="moderation-container">
      <h1>Панель модерации</h1>

      <div v-if="requestData.loading" class="loading">Загрузка пользователей...</div>
      <div v-else-if="users.length === 0" class="no-users">Пользователи не найдены.</div>

      <div v-else class="user-list">
        <div v-for="user in users" :key="user.email" class="user-card">
          <img
              :src="user.profileImageUrl?.trim() ? user.profileImageUrl : defaultAvatar"
              alt="Profile Photo"
              class="avatar"
          />
          <div class="user-info">
            <h3>{{ user.fullName }}</h3>
            <p><strong>Email:</strong> {{ user.email }}</p>
            <p><strong>Роль:</strong> {{ user.role }}</p>
            <p><strong>Создан:</strong> {{ formatDate(user.createdAt) }}</p>

            <div
                v-if="user.email !== moderatorStore.currentModeratorEmail"
                class="authority-selector"
            >
              <input
                  type="radio"
                  :id="`auth-default-${user.email}`"
                  :name="`authority-${user.email}`"
                  value="DEFAULT"
                  :checked="user.authority === 'DEFAULT'"
                  @change="onAuthorityChange(user.email, 'DEFAULT')"
                  hidden
              />
              <label
                  :for="`auth-default-${user.email}`"
                  :class="{ active: user.authority === 'DEFAULT' }"
              >
                DEFAULT
              </label>

              <input
                  type="radio"
                  :id="`auth-admin-${user.email}`"
                  :name="`authority-${user.email}`"
                  value="ADMIN"
                  :checked="user.authority === 'ADMIN'"
                  @change="onAuthorityChange(user.email, 'ADMIN')"
                  hidden
              />
              <label
                  :for="`auth-admin-${user.email}`"
                  :class="{ active: user.authority === 'ADMIN' }"
              >
                ADMIN
              </label>

            </div>
            <p v-else class="self-authority-warning">
              Нельзя изменить собственный уровень доступа!
            </p>
            <button
                class="delete-btn"
                @click="onDeleteUser(user.email)"
            >
              Удалить профиль
            </button>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style scoped>
.moderation-container {
  padding: 24px;
}

.user-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.user-card {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 8px;
  background-color: var(--profile-custom-bg);
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 16px;
}

.user-info {
  flex: 1;
}

.authority-selector {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin: 1rem 0;
}

.authority-selector label {
  padding: 10px 16px;
  border: 2px solid #666;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  background-color: transparent;
  color: white;
  transition: all 0.2s;
}

.authority-selector label.active {
  background-color: var(--vt-c-green);
  border-color: var(--vt-c-black);
  color: black;
}

.delete-btn {
  margin-top: 12px;
  padding: 8px 12px;
  background-color: #d9534f;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.delete-btn:hover {
  background-color: #c9302c;
}

.loading, .no-users {
  font-size: 18px;
  color: #666;
}

.self-authority-warning {
  margin-top: 1rem;
  font-size: 14px;
  font-style: italic;
  color: #999;
}
</style>
