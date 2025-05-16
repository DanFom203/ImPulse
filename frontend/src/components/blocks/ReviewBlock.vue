<template>
  <div class="review">
    <div class="by">By: {{ review.client.fullName }}</div>

    <div class="for" v-if="review.specialist !== undefined">
      For:
      <RouterLink :to="{ name: 'Specialist', params: { id: review.specialist.id } }">
        {{ review.specialist.fullName }}
      </RouterLink>
    </div>

    <div class="rating">Rating: {{ review.rating }}</div>

    <div class="comment">
      Comment:
      <div class="comment-body">
        {{ review.comment }}
      </div>
    </div>

    <div class="created-at">Created at: {{ review.createdAt }}</div>
  </div>
</template>

<script>
import { RouterLink } from 'vue-router'

export default {
  name: 'ReviewBlock',
  components: { RouterLink },
  props: ['review']
}
</script>

<style scoped>
.review {
  background-color: aquamarine;
  border: 1px solid darkgreen;
}
</style>
