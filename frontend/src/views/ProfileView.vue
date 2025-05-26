<script setup>
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/userStore'
import UserRoleCard from '@/components/forms/UserRoleForm.vue'
import UserSettingsPanel from '@/components/forms/UserSettingsForm.vue'
import MainLayout from "@/components/blocks/MainLayout.vue";
import { ref } from 'vue'
import defaultAvatar from '@/assets/logo.svg'

const { user } = storeToRefs(useUserStore())
const fileInput = ref(null)

function triggerFileInput() {
  fileInput.value.click()
}

async function onFileSelected(event) {
  const file = event.target.files[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  await userStore.uploadProfilePhoto(formData)
}

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleDateString('ru-RU', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}
</script>

<template>
  <MainLayout>
    <div class="profile-layout">
      <UserRoleCard />

      <div class="profile">
        <div class="profile-header">
          <div class="profile-photo-wrapper">
            <input
                ref="fileInput"
                type="file"
                accept="image/*"
                class="file-input"
                @change="onFileSelected"
            />
            <img
                :src="user.profileImageUrl?.trim() ? user.profileImageUrl : defaultAvatar"
                alt="Profile Photo"
                class="profile-photo clickable"
                @click="triggerFileInput"
            />
          </div>
          <div class="profile-info">
            <h2>{{ user.fullName }}</h2>
            <p>Создан: {{ formatDate(user.createdAt) }}</p>
          </div>
        </div>

        <div v-if="user.role === 'SPECIALIST'" class="specialist-section">
          <h3>Специализация:</h3>
          <ul>
            <li v-for="specialty in user.specialties" :key="specialty.id">{{ specialty.name }}</li>
          </ul>
          <p><strong>Био:</strong> {{ user.specialistBio }}</p>
          <p><strong>Рейтинг:</strong> {{ user.specialistAvgRating ?? '—' }}</p>
          <p><strong>Цена за курс:</strong> {{ user.specialistAppointmentPrice ?? '—' }} ₽</p>

          <details>
            <summary>Отзывы о специалисте</summary>
            <ul>
              <li v-for="review in user.specialistReviews" :key="review.id">
                <p><strong>{{ review.authorName }}:</strong> {{ review.text }}</p>
              </li>
            </ul>
          </details>
        </div>
      </div>

      <UserSettingsPanel />
    </div>
  </MainLayout>
</template>

<style src="@/assets/styles/profileView.css"></style>