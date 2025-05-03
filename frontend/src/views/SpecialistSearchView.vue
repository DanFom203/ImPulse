<script setup>
import MainLayout from '../components/blocks/MainLayout.vue'
import SpecialistSearchForm from '../components/forms/SpecialistSearchForm.vue'
import SpecialistList from '../components/lists/SpecialistList.vue'
</script>

<template>
  <MainLayout>
    <div class="about">
      <h1>Search for specialist</h1>
      <SpecialistSearchForm />
      <SpecialistList />
    </div>
  </MainLayout>
</template>

<script>
export default {
  name: 'SpecialistSearchView',
  components: { MainLayout }
}
</script>
