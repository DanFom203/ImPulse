<template>
  <input type="submit" :value="value" class="submit-button" />
</template>

<script>
export default {
  name: 'FormSubmit',
  props: ['value']
}
</script>

<style scoped>
.submit-button {
  padding: 10px;
  border-radius: 3px;
}
</style>
