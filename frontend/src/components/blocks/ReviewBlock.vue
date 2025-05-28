<template>
  <div class="review-card">
    <div class="review-header">
      <span class="label">От: <span class="client-name">{{ review.client?.fullName ?? 'Аноним' }}</span></span>
    </div>

    <div class="review-specialist" v-if="review.specialist">
      <span class="label">Тренеру:</span>
      <RouterLink class="specialist-link" :to="{ name: 'Specialist', params: { id: review.specialist.id } }">
        {{ review.specialist.fullName }}
      </RouterLink>
    </div>

    <div class="review-rating">
      <span class="label">Оценка:</span> {{ review.rating }}/5
    </div>

    <div class="review-comment">
      <span class="label">Комментарий:</span>
      <p class="comment-body">{{ review.comment }}</p>
    </div>

    <div class="review-date">
      <span class="label">Дата:</span> {{ formatDate(review.createdAt) }}
    </div>
  </div>
</template>

<script>
import { RouterLink } from 'vue-router'

export default {
  name: 'ReviewBlock',
  components: { RouterLink },
  props: ['review'],
  methods: {
    formatDate(dateStr) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' }
      return new Date(dateStr).toLocaleDateString('ru-RU', options)
    }
  }
}
</script>

<style src="@/assets/reviewBlock.css"></style>
