<template>
  <DefaultLoader v-if="requestData.loading" />
  <div class="error" v-else-if="requestData.error !== null">
    {{ requestData.error.message }}
  </div>
  <div class="specialistList" v-else>
    <div class="specialist" v-for="specialist in specialists" v-bind:key="specialist.id">
      <div class="link">
        <RouterLink :to="{ name: 'Specialist', params: { id: specialist.id } }"
          >Check out more</RouterLink
        >
      </div>
      <div class="name">Name: {{ specialist.name }}</div>
      <div class="bio">Bio: {{ specialist.bio }}</div>
      <div class="price">Price: {{ specialist.price }} рублёв</div>
      <div class="rating">Рейтинг: {{ specialist.rating }}</div>
      <div class="specialtyList">
        <div>Специальности:</div>
        <ul class="specialtyListUl">
          <li
            class="specialty"
            v-for="specialty in specialist.specialties"
            v-bind:key="specialty.id"
          >
            {{ specialty.name }}
          </li>
        </ul>
      </div>
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
