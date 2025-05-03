<template>
  <MainLayout>
    <h1>My Reviews</h1>
    <DefaultLoader v-if="requestData.loading" />
    <div class="error" v-else-if="requestData.error !== null">
      {{ requestData.error.message }}
    </div>
    <div class="review-list" v-else>
      <ReviewBlock v-for="review in reviews" v-bind:key="review.id" :review="review" />
    </div>
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'
import ReviewBlock from '../components/blocks/ReviewBlock.vue'
import { useReviewStore } from '../stores/reviewStore.js'
import { useUserStore } from '../stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import DefaultLoader from '../components/utils/DefaultLoader.vue'

export default {
  name: 'SpecialistView',
  components: { MainLayout, DefaultLoader, ReviewBlock },
  async beforeMount() {
    this.fetchReviews()
  },
  methods: {
    ...mapActions(useReviewStore, ['fetchReviews'])
  },
  computed: {
    ...mapState(useReviewStore, {
      requestData: (state) => state.requestData,
      reviews: (state) => state.reviews
    }),
    ...mapState(useUserStore, ['user'])
  }
}
</script>
