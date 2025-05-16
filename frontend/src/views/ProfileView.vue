<script setup>
import { useUserStore } from '../stores/userStore'
import { mapState } from 'pinia'
</script>

<template>
  <MainLayout>
    <h1>Profile</h1>

    <h2>{{ user.fullName }}</h2>
    <h2>{{ user.username }}</h2>

    <h2>{{ rolePretty }}</h2>

    <div v-if="user.role === 'SPECIALIST'">
      <h2>Специальности:</h2>
      <ul>
        <li v-for="specialty in user.specialties" :key="specialty.id">
          {{ specialty.name }}
        </li>
      </ul>

      <p class="price">Price: {{ user.price }}</p>
      <p class="rating">Rating: {{ user.rating }}</p>
      <p class="bio">Bio: {{ user.bio }}</p>

      <UpdateSpecialtiesForm />
    </div>

    <DeleteProfileForm />
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'
import DeleteProfileForm from '@/components/forms/DeleteProfileForm.vue'
import UpdateSpecialtiesForm from '@/components/forms/UpdateSpecialtiesForm.vue'

export default {
  name: 'ProfileView',
  components: { DeleteProfileForm, UpdateSpecialtiesForm },
  computed: {
    ...mapState(useUserStore, {
      error: (state) => state.requestData.error,
      user: 'user',
      rolePretty: 'rolePretty'
    })
  }
}
</script>
