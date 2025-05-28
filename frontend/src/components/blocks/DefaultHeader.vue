<template>
  <header class="header">
    <h3 class="home-link">
      <router-link class="text-dark text-decoration-none" :to="{ name: 'Home' }">
        ImPulse
      </router-link>
    </h3>
    <ul class="nav">
      <li class="nav-item" v-for="route in this.getRoutes" :key="route.name">
        <router-link class="nav-item__inner" aria-current="page" :to="{ name: route.name }">
          {{ route.text }}
        </router-link>
      </li>
    </ul>
  </header>
</template>

<script>
import { mapState } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'

export default {
  name: 'DefaultHeader',
  data() {
    return {
      routes: [
        { name: 'Home', text: 'Главная' },
        { name: 'Profile', text: 'Профиль', authRequired: true },
        { name: 'SpecialistSearch', text: 'Найти тренера', authRequired: true },
        { name: 'AppointmentList', text: 'Мои записи', authRequired: true },
        { name: 'Reviews', text: 'Мои отзывы', authRequired: true },
        { name: 'Login', text: 'Вход', guestRequired: true },
        { name: 'Logout', text: 'Выход', authRequired: true },
        { name: 'Registration', text: 'Регистрация', guestRequired: true }
      ]
    }
  },
  computed: {
    ...mapState(useUserStore, ['isAuthenticated', 'user']),
    getRoutes() {
      const isTrainer = this.user?.role === 'SPECIALIST'

      return this.routes.filter(route => {
        if (!this.isAuthenticated && route.authRequired) return false
        if (this.isAuthenticated && route.guestRequired) return false
        if (isTrainer && route.name === 'Reviews') return false
        return true
      })
    }
  }
}
</script>
<style src="@/assets/header.css" scoped></style>
