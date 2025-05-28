<template>
  <DefaultLoader v-if="requestData.loading" />
  <div
      class="specialist-card"
      v-for="specialist in specialists"
      :key="specialist.id"
  >
    <div>
      <RouterLink
          :to="{ name: 'Specialist', params: { id: specialist.id } }"
          class="specialist-name block cursor-pointer"
      >
        {{ specialist.name }}
      </RouterLink>

      <p class="specialist-bio">{{ specialist.bio }}</p>

      <div class="specialist-meta text-sm">
        💵 <span class="font-medium">Price:</span> {{ specialist.price }} ₽
      </div>
      <div class="specialist-meta text-sm">
        ⭐ <span class="font-medium">Rating:</span> {{ specialist.rating }}
      </div>

      <div class="specialist-meta text-sm">
        🛠 <span class="font-medium">Specialties:</span>
        <ul class="specialty-list">
          <li v-for="specialty in specialist.specialties" :key="specialty.id">
            {{ specialty.name }}
          </li>
        </ul>
      </div>
    </div>

    <RouterLink
        :to="{ name: 'Specialist', params: { id: specialist.id } }"
        class="view-link"
    >
      Check out more
    </RouterLink>
  </div>
</template>

<script>
import { useSpecialistStore } from '@/stores/specialistStore.js'
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
.specialist-card {
  background-color: #1f1f1f; /* как фон формы */
  color: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 0 12px hsla(160, 100%, 37%, 0.3);
  border: 1px solid hsla(160, 100%, 37%, 0.4);
  transition: box-shadow 0.3s ease, transform 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.specialist-card:hover {
  box-shadow: 0 0 24px hsla(160, 100%, 47%, 0.8);
  transform: translateY(-4px);
}

.specialist-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: hsla(160, 100%, 37%, 1);
  margin-bottom: 0.5rem;
  transition: color 0.3s;
}

.specialist-name:hover {
  color: hsla(160, 100%, 47%, 1);
}

.specialist-bio,
.specialist-meta {
  color: #ccc;
  margin-bottom: 0.75rem;
}

.specialty-list {
  list-style: disc;
  list-style-position: inside;
  margin-top: 0.5rem;
  color: #bbb;
}

.view-link {
  background-color: hsla(160, 100%, 37%, 1);
  color: #000;
  font-weight: 600;
  text-align: center;
  padding: 0.5rem 1rem;
  border-radius: 12px;
  margin-top: 1rem;
  transition: background-color 0.3s;
}

.view-link:hover {
  background-color: hsla(160, 100%, 47%, 1);
  color: #000;
}
</style>