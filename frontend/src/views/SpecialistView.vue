<template>
  <MainLayout>
    <DefaultLoader v-if="requestData.loading" />

    <div class="error" v-else-if="requestData.error !== null">
      {{ requestData.error.message }}
    </div>

    <div v-else>
      <div class="card-container trainer-card">
        <h2 class="trainer-name">{{ currentSpecialist.fullName }}</h2>
        <p class="trainer-bio">{{ currentSpecialist.bio }}</p>

        <div class="trainer-meta">
          <span class="trainer-price">💵 {{ currentSpecialist.price }}</span>
          <span class="trainer-rating">⭐ {{ currentSpecialist.rating }}</span>
        </div>

        <div class="trainer-reviews" v-if="currentSpecialist.reviews?.length">
          <h3 class="review-title">Отзывы</h3>
          <ReviewBlock
              v-for="review in currentSpecialist.reviews"
              :key="review.id"
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

<style>
.card-container {
  width: 2000px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #1f1f1f;
  color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 255, 153, 0.1);
  border: 1px solid hsla(160, 100%, 37%, 0.4);
}
.trainer-card {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #1f1f1f;
  color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 255, 153, 0.1);
  font-size: 1rem;
  border: 1px solid hsla(160, 100%, 37%, 0.4);
}

.trainer-name {
  font-size: 1.8rem;
  font-weight: 600;
  color: hsla(160, 100%, 37%, 1);
  margin-bottom: 0.5rem;
}

.trainer-bio {
  font-size: 1.1rem;
  color: #ccc;
  margin-bottom: 1.5rem;
}

.trainer-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1.5rem;
  font-size: 1.1rem;
}

.trainer-price,
.trainer-rating {
  color: hsla(160, 100%, 37%, 1);
}

.trainer-reviews {
  margin-top: 2rem;
}

.review-title {
  font-size: 1.3rem;
  margin-bottom: 1rem;
  color: #fff;
}

</style>

