<template>
  <form @submit.prevent="submit()" class="card-container review-form">
    <h3 class="form-title">Оставить отзыв о специалисте</h3>

    <div class="form-group">
      <label for="comment-input">Комментарий</label>
      <textarea
          id="comment-input"
          class="form-input textarea"
          v-model="form.comment"
          placeholder="Опишите ваш опыт..."
      ></textarea>
      <div class="form-error" v-if="this.v$.form.comment.$errors[0]">
        {{ this.v$.form.comment.$errors[0].$message }}
      </div>
    </div>

    <div class="form-group">
      <label for="rating-input">Оценка (1–5)</label>
      <input
          id="rating-input"
          type="number"
          min="1"
          max="5"
          class="form-input"
          v-model="form.rating"
      />
      <div class="form-error" v-if="this.v$.form.rating.$errors[0]">
        {{ this.v$.form.rating.$errors[0].$message }}
      </div>
    </div>

    <FormSubmit value="Оставить отзыв" class="submit-button" />
  </form>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import { minValue, required, maxValue, helpers } from '@vuelidate/validators'
import { useReviewStore } from '@/stores/reviewStore.js'
import { useSpecialistStore } from '@/stores/specialistStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import { CreateReviewDto } from '@/models/dto.js'

export default {
  name: 'CreateReviewForm',
  components: { FormSubmit },
  setup() {
    return {
      v$: useVuelidate()
    }
  },
  data() {
    return {
      form: {
        comment: '',
        rating: 5
      }
    }
  },
  props: {
    specialistId: Number
  },
  computed: {
    ...mapState(useReviewStore, {
      error: (state) => state.requestData.error
    }),
    ...mapState(useSpecialistStore, ['currentSpecialist'])
  },
  validations: {
    form: {
      rating: {
        required: helpers.withMessage('This field is required', required),
        minValue: helpers.withMessage('Rating must be between 1 and 5', minValue(1)),
        maxValue: helpers.withMessage('Rating must be between 1 and 5', maxValue(5))
      },
      comment: {}
    }
  },
  methods: {
    ...mapActions(useReviewStore, ['createReview']),
    async submit() {
      this.v$.form.$touch()
      if (this.v$.form.$error) {
        console.log('Validation error')
        return
      }
      let createAppointmentDto = new CreateReviewDto(this.form.comment, this.form.rating)
      await this.createReview(this.specialistId, createAppointmentDto)
      if (!this.error) {
        console.log('Created!')
        this.$router.push({ name: 'Reviews' })
      }
    }
  }
}
</script>

<style src="@/assets/createReviewForm.css" scoped></style>
