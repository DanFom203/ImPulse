<script setup>
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/userStore'
import { ref } from 'vue'
import defaultAvatar from '@/assets/logo.svg'
import {uploadToCloudinary} from "@/services/uploadToCloudinary.js";

const { user } = storeToRefs(useUserStore())
const userStore = useUserStore()
const fileInput = ref(null)

function triggerFileInput() {
  fileInput.value.click()
}

async function onFileSelected(event) {
  const file = event.target.files?.[0]
  if (!file) return

  try {
    const imageUrl = await uploadToCloudinary(file)
    await userStore.uploadProfilePhoto(imageUrl)
  } catch (error) {
    console.error('Ошибка при загрузке фото:', error)
  }
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
            :src="user.avatar?.trim() ? user.avatar : defaultAvatar"
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
      <p><strong>Био:</strong> {{ user.bio }}</p>
      <p><strong>Рейтинг:</strong> {{ user.rating ?? '—' }}</p>
      <p><strong>Цена за курс:</strong> {{ user.price ?? '—' }} ₽</p>

      <details>
        <summary>Отзывы о специалисте</summary>
        <ul class="review-list">
          <li v-for="review in user.reviews" :key="review.id" class="review-card">
            <div class="review-header">
              <p class="review-client">{{ review.client.fullName }}</p>
              <span class="review-rating">⭐ {{ review.rating }}/5</span>
            </div>
            <p class="review-comment">{{ review.comment }}</p>
            <small class="review-date">{{ formatDate(review.createdAt) }}</small>
          </li>
        </ul>
      </details>
    </div>
  </div>
</template>

<style scoped src="@/assets/profile.css"></style>