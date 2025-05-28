<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { storeToRefs } from 'pinia'
import UpdateSpecialtiesForm from '@/components/forms/UpdateSpecialtiesForm.vue'
import DeleteProfileForm from '@/components/forms/DeleteProfileForm.vue'

const userStore = useUserStore()
const { user } = storeToRefs(userStore)

const showModal = ref(false)
const specialistBio = ref(user.value.specialistBio || '')
const specialistPrice = ref(user.value.specialistPrice ?? 0)
const errorMessage = ref('')

async function submitInfo() {
  if (specialistPrice.value < 0) {
    errorMessage.value = 'Цена не может быть меньше 0'
    return
  }

  try {
    await userStore.updateProfileInfo(specialistBio.value, specialistPrice.value)

    showModal.value = false
    errorMessage.value = ''
  } catch (e) {
    errorMessage.value = 'Ошибка при обновлении информации'
    console.error(e)
  }
}
</script>

<template>
  <div class="user-settings-panel">
    <h3>Настройки профиля</h3>

    <button v-if="user.role === 'SPECIALIST'" @click="showModal = true">
      Редактировать информацию специалиста
    </button>

    <UpdateSpecialtiesForm v-if="user.role === 'SPECIALIST'" />
    <DeleteProfileForm />

    <!-- Модальное окно -->
    <div v-if="showModal" class="modal-backdrop">
      <div class="modal">
        <h4>Обновить информацию специалиста</h4>

        <label for="bio">О себе:</label>
        <textarea
            id="bio"
            v-model="specialistBio"
            placeholder="Введите подробную информацию о себе"
            rows="8"
        />

        <label for="price">Цена за услугу (₽):</label>
        <input
            id="price"
            type="number"
            v-model.number="specialistPrice"
            min="0"
        />

        <div class="error-message" v-if="errorMessage">{{ errorMessage }}</div>

        <div class="modal-buttons">
          <button @click="submitInfo">Подтвердить</button>
          <button @click="showModal = false">Отмена</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-settings-panel {
  padding: 16px;
  border-radius: 8px;
  background-color: var(--profile-custom-bg, #f5f5f5);
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  max-width: 300px;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal {
  background-color: #fff;
  color: #000;
  padding: 24px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.modal textarea,
.modal input[type="number"] {
  width: 100%;
  resize: vertical;
  padding: 8px;
  margin-bottom: 12px;
  background-color: #fff;
  color: #000;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-buttons {
  display: flex;
  justify-content: space-between;
}

.error-message {
  color: red;
  margin-bottom: 10px;
}

/* 🌙 Темная тема */
@media (prefers-color-scheme: dark) {
  .modal {
    background-color: #1e1e1e;
    color: #f0f0f0;
  }

  .modal textarea,
  .modal input[type="number"] {
    background-color: #2a2a2a;
    color: #f0f0f0;
    border: 1px solid #444;
  }

  .user-settings-panel {
    background-color: #2e2e2e;
    color: #fff;
  }

  .error-message {
    color: #ff8080;
  }
}
</style>