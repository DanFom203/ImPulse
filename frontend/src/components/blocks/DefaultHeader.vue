<template>
  <header class="header">
    <h3 class="home-link">
      <router-link class="text-dark text-decoration-none" :to="{ name: 'Home' }">
        TherapyHub
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
import { useUserStore } from '../../stores/userStore.js'

export default {
  name: 'DefaultHeader',
  data() {
    return {
      routes: [
        {
          name: 'Home',
          text: 'Home'
        },
        {
          name: 'Profile',
          text: 'My profile',
          authRequired: true
        },
        {
          name: 'SpecialistSearch',
          text: 'Find specialist',
          authRequired: true
        },
        {
          name: 'AppointmentList',
          text: 'My appointments',
          authRequired: true
        },
        {
          name: 'Reviews',
          text: 'My reviews',
          authRequired: true
        },
        {
          name: 'Login',
          text: 'Sign in',
          guestRequired: true
        },
        {
          name: 'Logout',
          text: 'Logout',
          authRequired: true
        },
        {
          name: 'Registration',
          text: 'Sign up',
          guestRequired: true
        }
      ],
      activeRoutes: []
    }
  },
  computed: {
    ...mapState(useUserStore, ['isAuthenticated']),
    getRoutes() {
      if (this.isAuthenticated) {
        return this.routes.filter((route) => !route.guestRequired)
      } else {
        return this.routes.filter((route) => !route.authRequired)
      }
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  /* flex-wrap: wrap; */
  justify-content: space-between;
  border-bottom: 1px solid black;
  padding: 15px;
}

.home-link {
  font-size: 25px;
}

.nav {
  display: flex;
}

.nav-item {
  list-style-type: none;
  font-size: 25px;
  margin-right: 25px;
}
</style>
