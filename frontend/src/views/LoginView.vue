<script setup>
import LoginForm from '../components/forms/LoginForm.vue'
</script>

<template>
  <MainLayout>
    <h1>Login</h1>

    <LoginForm />
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'

export default {
  name: 'LoginView',
  components: { MainLayout }
}
</script>
