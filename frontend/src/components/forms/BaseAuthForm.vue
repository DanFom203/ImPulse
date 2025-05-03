<template>
  <form
    @submit.prevent="this.$emit('submit.prevent')"
    method="post"
    novalidate
    class="base-auth-form"
  >
    <h3>{{ this.title }}</h3>
    <div class="fields-row">
      <slot name="fields"> </slot>
    </div>

    <DefaultLoader v-if="requestData.loading" />
    <slot name="submit-button" v-else> </slot>

    <div class="links-row mt-3">
      <slot name="links"> </slot>
    </div>

    <div class="error" v-if="this.error">
      {{ this.error.message }}
    </div>
  </form>
</template>

<script>
import { useUserStore } from '../../stores/userStore.js'
import { mapState } from 'pinia'
import DefaultLoader from '../utils/DefaultLoader.vue'

export default {
  name: 'BaseAuthForm',
  components: { DefaultLoader },
  props: ['error', 'title'],
  computed: {
    ...mapState(useUserStore, ['requestData'])
  }
}
</script>

<style scoped>
.base-auth-form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.error {
  color: red;
  font-weight: bold;
}
</style>
