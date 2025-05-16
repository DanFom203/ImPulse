<template>
  <div class="container">
    <DefaultHeader />

    <main class="main">
      <slot> </slot>
    </main>

    <DefaultFooter />
  </div>
</template>

<script>
import DefaultFooter from './DefaultFooter.vue'
import DefaultHeader from './DefaultHeader.vue'

export default {
  name: 'MainLayout',
  components: { DefaultHeader, DefaultFooter }
}
</script>

<style scoped>
.container {
  max-width: 90%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

main {
  margin: 30px 0;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}
</style>
