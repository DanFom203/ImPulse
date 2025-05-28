<template>
  <DefaultLoader v-if="requestData.loading" />
  <div class="text-red-500 text-center" v-else-if="requestData.error !== null">
    {{ requestData.error.message }}
  </div>

  <div class="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 p-4" v-else>
    <div
        class="bg-white rounded-2xl shadow-md hover:shadow-lg transition-shadow duration-300 p-6 flex flex-col justify-between"
        v-for="specialist in specialists"
        :key="specialist.id"
    >
      <div>
        <h2 class="text-xl font-semibold text-gray-800 mb-2">{{ specialist.name }}</h2>
        <p class="text-gray-600 mb-3">{{ specialist.bio }}</p>

        <div class="text-sm text-gray-500 mb-2">
          💵 <span class="font-medium">Price:</span> {{ specialist.price }} ₽
        </div>
        <div class="text-sm text-gray-500 mb-2">
          ⭐ <span class="font-medium">Rating:</span> {{ specialist.rating }}
        </div>

        <div class="text-sm text-gray-500 mb-2">
          🛠 <span class="font-medium">Specialties:</span>
          <ul class="list-disc list-inside mt-1">
            <li v-for="specialty in specialist.specialties" :key="specialty.id">
              {{ specialty.name }}
            </li>
          </ul>
        </div>
      </div>

      <RouterLink
          :to="{ name: 'Specialist', params: { id: specialist.id } }"
          class="mt-4 inline-block text-center bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition duration-300"
      >
        Check out more
      </RouterLink>
    </div>
  </div>
</template>

<script>
import { useSpecialistStore } from '../../stores/specialistStore.js'
import DefaultLoader from '../utils/DefaultLoader.vue'
import { mapState } from 'pinia'
export default {
  name: 'SpecialistList',
  components: { DefaultLoader },
  data() {
    return {}
  },
  computed: {
    ...mapState(useSpecialistStore, ['specialists', 'requestData'])
  }
}
</script>

<style scoped>
.specialist {
  border: 1px solid black;
}
</style>
