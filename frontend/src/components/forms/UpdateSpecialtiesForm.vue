<template>
  <form action="POST" @submit.prevent="this.submit()" class="form">
    <div class="title">Хотите обновить свою тренерскую специализацию?</div>

    <DefaultLoader v-if="specialtyStoreRequestData.loading" />
    <div class="specialties" v-else>
      <div class="specialty" v-for="specialty in allSpecialties" v-bind:key="specialty.id">
        <label :for="`specialty-${specialty.id}`">{{ specialty.name }}</label>
        <input
          type="checkbox"
          name="specialties"
          :id="`specialty-${specialty.id}`"
          :value="specialty"
          v-model="form.specialties"
        />
      </div>
    </div>

    <DefaultLoader v-if="userStoreRequestData.loading" />
    <FormSubmit value="Update specialties" v-else />
  </form>
</template>

<script>
import { useSpecialtyStore } from '@/stores/specialtyStore.js'
import { useUserStore } from '@/stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import DefaultLoader from '../utils/DefaultLoader.vue'

export default {
  name: 'UpdateSpecialtiesForm',
  components: { FormSubmit, DefaultLoader },
  data() {
    return {
      form: {
        specialties: useUserStore().user.specialties
      }
    }
  },
  computed: {
    ...mapState(useSpecialtyStore, {
      specialtyStoreRequestData: (state) => state.requestData,
      allSpecialties: (state) => state.specialtyList
    }),
    ...mapState(useUserStore, {
      userStoreRequestData: (state) => state.requestData,
      user: (state) => state.user
    })
  },
  async beforeMount() {
    this.ensureLoaded()
  },
  methods: {
    ...mapActions(useSpecialtyStore, ['ensureLoaded']),
    ...mapActions(useUserStore, ['updateSpecialties']),
    async submit() {
      await this.updateSpecialties(this.form.specialties)
    }
  }
}
</script>

<style scoped>
.form {
  border: 1px solid black;
}
</style>
