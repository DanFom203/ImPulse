<script setup>
import RegistrationForm from '../components/forms/RegistrationForm.vue'
</script>

<template>
  <MainLayout>
    <h1>Registration</h1>

    <RegistrationForm />
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'

export default {
  name: 'RegistrationView',
  components: { MainLayout }
}
</script>
