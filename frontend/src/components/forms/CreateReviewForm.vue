<template>
  <form action="POST" @submit.prevent="this.submit()" class="form">
    <div class="title">Create review for this specialist?</div>
    <div class="comment">
      <label for="comment-input">Comment</label>
      <textarea type="text" name="comment" id="comment-input" v-model="form.comment"></textarea>
      <div v-if="this.v$.form.comment.$errors[0]">
        {{ this.v$.form.comment.$errors[0].$message }}
      </div>
    </div>

    <div class="rating">
      <label for="rating-input">Rating</label>
      <input type="number" min="1" max="5" name="rating" id="rating-input" v-model="form.rating" />
      <div v-if="this.v$.form.rating.$errors[0]">{{ this.v$.form.rating.$errors[0].$message }}</div>
    </div>

    <FormSubmit value="Create review" />
  </form>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import { minValue, required, maxValue, helpers } from '@vuelidate/validators'
import { useReviewStore } from '../../stores/reviewStore.js'
import { useSpecialistStore } from '@/stores/specialistStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import { CreateReviewDto } from '../../models/dto.js'

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

<style scoped>
.form {
  border: 1px solid black;
}
</style>
