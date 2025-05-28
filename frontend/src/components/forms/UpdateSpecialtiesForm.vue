<template>
  <form action="POST" @submit.prevent="this.submit()" class="form">
    <div class="title">Хотите обновить свою тренерскую специализацию?</div>

    <DefaultLoader v-if="specialtyStoreRequestData.loading" />
    <div class="specialties" v-else>
      <div class="scrollable-specialties">
        <div class="specialty" v-for="specialty in allSpecialties" :key="specialty.id">
          <label :for="`specialty-${specialty.id}`">{{ specialty.name }}</label>
          <input
              type="checkbox"
              :id="`specialty-${specialty.id}`"
              :value="specialty.id"
              v-model="selectedSpecialtyIds"
          />
        </div>
      </div>
    </div>

    <DefaultLoader v-if="userStoreRequestData.loading" />
    <FormSubmit value="Update specialties" v-else />

  </form>

  <div class="custom-specialty">
    <label for="newSpecialty">Добавить другую специализацию:</label>
    <input
        id="newSpecialty"
        type="text"
        v-model="newSpecialty"
        placeholder="Введите специализацию"
    />
    <button type="button" @click="addCustomSpecialty">Добавить</button>
  </div>
</template>

<script>
import { useSpecialtyStore } from '@/stores/specialtyStore.js'
import { useUserStore } from '@/stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import DefaultLoader from '../utils/DefaultLoader.vue'
import {apiEditSpecialtiesList} from "@/services/api.js";

export default {
  name: 'UpdateSpecialtiesForm',
  components: { FormSubmit, DefaultLoader },
  data() {
    return {
      form: {
        specialties: useUserStore().user.specialties || []
      },
      selectedSpecialtyIds: useUserStore().user.specialties?.map(s => s.id) || [],
      newSpecialty: ''
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
      const selected = this.allSpecialties.filter(s => this.selectedSpecialtyIds.includes(s.id))

      const allSelected = [
        ...selected,
        ...(this.form.specialties.filter(s => s.isCustom) || [])
      ]

      await this.updateSpecialties(allSelected)
    },
    async addCustomSpecialty() {
      const trimmed = this.newSpecialty.trim()
      if (!trimmed) return

      const exists = (this.form.specialties || []).some(
          s => s.name.toLowerCase() === trimmed.toLowerCase()
      )
      if (exists) return

      try {
        const updatedSpecialties = await apiEditSpecialtiesList(trimmed)

        const specialtyStore = useSpecialtyStore()
        specialtyStore.specialtyList = updatedSpecialties

        const justAdded = updatedSpecialties.find(
            s => s.name.toLowerCase() === trimmed.toLowerCase()
        )

        if (justAdded) {
          if (!this.selectedSpecialtyIds.includes(justAdded.id)) {
            this.selectedSpecialtyIds.push(justAdded.id)
          }
        }

        this.newSpecialty = ''
      } catch (e) {
        console.error('Ошибка при добавлении специализации:', e)
      }
    }
  }
}
</script>

<style scoped>
.form {
  border: 1px solid black;
}

.scrollable-specialties {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #ccc;
  padding: 8px;
  margin-bottom: 16px;
}
</style>
