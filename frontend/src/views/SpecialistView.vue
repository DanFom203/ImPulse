<template>
  <MainLayout>
    <DefaultLoader v-if="requestData.loading" />
    <div class="error" v-else-if="requestData.error !== null">
      {{ requestData.error.message }}
    </div>
    <div class="specialist-data" v-else>
      <div class="specialist">
        <h2 class="name">
          {{ currentSpecialist.fullName }}
        </h2>
        <h3 class="bio">
          {{ currentSpecialist.bio }}
        </h3>
        <div class="price">Price: {{ currentSpecialist.price }}</div>

        <div class="rating">Rating: {{ currentSpecialist.rating }}</div>
        <div class="review-list">
          <div class="review-list-title">Reviews:</div>
          <ReviewBlock
            v-for="review in currentSpecialist.reviews"
            v-bind:key="review.id"
            :review="review"
          />
        </div>
      </div>

      <CreateAppointmentForm
        :specialistId="parseInt($route.params.id)"
        v-if="user.role === 'CLIENT'"
      />

      <CreateReviewForm
        :specialistId="parseInt($route.params.id)"
        v-if="user.role === 'CLIENT' && !hasAlreadyWrittenReview"
      />
    </div>
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'
import ReviewBlock from '../components/blocks/ReviewBlock.vue'
import { useSpecialistStore } from '../stores/specialistStore.js'
import { useUserStore } from '../stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import DefaultLoader from '../components/utils/DefaultLoader.vue'
import CreateAppointmentForm from '../components/forms/CreateAppointmentForm.vue'
import CreateReviewForm from '../components/forms/CreateReviewForm.vue'

export default {
  name: 'SpecialistView',
  components: { MainLayout, DefaultLoader, CreateAppointmentForm, ReviewBlock, CreateReviewForm },
  async beforeMount() {
    this.fetchSpecialist(this.$route.params.id)
  },
  methods: {
    ...mapActions(useSpecialistStore, ['fetchSpecialist'])
  },
  computed: {
    ...mapState(useSpecialistStore, {
      requestData: (state) => state.requestData,
      currentSpecialist: (state) => state.currentSpecialist
    }),
    ...mapState(useUserStore, ['user']),
    hasAlreadyWrittenReview() {
      for (let review of this.currentSpecialist.reviews) {
        if (review.client.fullName === this.user.fullName) {
          return true
        }
      }
      return false
    }
  }
}
</script>
