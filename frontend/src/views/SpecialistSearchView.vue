<script setup>
import SpecialistSearchForm from '../components/forms/SpecialistSearchForm.vue'
import SpecialistList from '../components/lists/SpecialistList.vue'
</script>

<template>
  <MainLayout>
    <div class="about">
      <h1>Найти подходящего вам тренера!</h1>
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
<style scoped>


.about h1 {
  align-items: center;
  text-align: center;
  font-size: 2rem;
  margin-bottom: 1.5rem;
}
</style>