<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { storeToRefs } from 'pinia'
import UpdateSpecialtiesForm from '@/components/forms/UpdateSpecialtiesForm.vue'
import DeleteProfileForm from '@/components/forms/DeleteProfileForm.vue'

const userStore = useUserStore()
const { user } = storeToRefs(userStore)

const customBackgroundColor = ref(localStorage.getItem('profileBgColor') || '')

function applyColor() {
  document.documentElement.style.setProperty('--profile-custom-bg', customBackgroundColor.value)
  localStorage.setItem('profileBgColor', customBackgroundColor.value)
}
</script>

<template>
  <div class="user-settings-panel">
    <h3>Настройки профиля</h3>

    <label for="bg-color">Цвет фона:</label>
    <input id="bg-color" type="color" v-model="customBackgroundColor" @input="applyColor" />

    <UpdateSpecialtiesForm v-if="user.role === 'SPECIALIST'" />
    <DeleteProfileForm />
  </div>
</template>

<style scoped>
.user-settings-panel {
  padding: 16px;
  border-radius: 8px;
  background-color: var(--profile-custom-bg);
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  max-width: 300px;
}
</style>