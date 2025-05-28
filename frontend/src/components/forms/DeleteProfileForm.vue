<template>
  <form action="POST" @submit.prevent="this.submit()" class="form">
    <div class="title">Хотите удалить свой профиль?</div>

    <FormSubmit value="Delete profile" />
  </form>
</template>

<script>
import { useUserStore } from '../../stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'

export default {
  name: 'DeleteProfileForm',
  components: { FormSubmit },
  computed: {
    ...mapState(useUserStore, {
      error: (state) => state.requestData.error
    })
  },
  methods: {
    ...mapActions(useUserStore, ['deleteProfile']),
    async submit() {
      if (confirm("Are you sure you want to delete your profile? It can't be undone")) {
        this.deleteProfile()
        this.$router.push({ name: 'Home' })
      }
    }
  }
}
</script>

<style scoped>
.form {
  border: 1px solid black;
}
</style>
