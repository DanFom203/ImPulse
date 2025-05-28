<template>
  <form @submit.prevent="submit()" class="card-container appointment-form">
    <h3 class="form-title">Записаться к специалисту</h3>

    <div class="form-group">
      <label for="datetime">Дата и время приёма</label>
      <input
          type="datetime-local"
          name="datetime"
          id="datetime"
          class="form-input"
          :value="form.datetime.toISOString().slice(0, 16)"
          @input="form.datetime = new Date($event.target.value)"
      />
      <div class="form-error" v-if="this.v$.form.datetime.$errors[0]">
        {{ this.v$.form.datetime.$errors[0].$message }}
      </div>
    </div>

    <FormSubmit value="Записаться" class="submit-button" />
  </form>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import { minValue, required, helpers } from '@vuelidate/validators'
import { useAppointmentStore } from '@/stores/appointmentStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import { CreateAppointmentDto } from '@/models/dto.js'

export default {
  name: 'CreateAppointmentForm',
  components: { FormSubmit },
  setup() {
    return {
      v$: useVuelidate()
    }
  },
  data() {
    return {
      form: {
        datetime: new Date()
      }
    }
  },
  props: {
    specialistId: Number
  },
  computed: {
    ...mapState(useAppointmentStore, {
      error: (state) => state.requestData.error
    })
  },
  validations: {
    form: {
      datetime: {
        required: helpers.withMessage('This field is required', required),
        minValue: helpers.withMessage('Datetime must be in the future', minValue(new Date()))
      }
    }
  },
  methods: {
    ...mapActions(useAppointmentStore, ['createAppointment']),
    async submit() {
      this.v$.form.$touch()
      if (this.v$.form.$error) {
        return
      }
      let createAppointmentDto = new CreateAppointmentDto(this.specialistId, this.form.datetime)
      await this.createAppointment(createAppointmentDto)
      if (!this.error) {
        this.$router.push({ name: 'AppointmentList' })
      }
    }
  }
}
</script>

<style src="@/assets/createAppointmentForm.css" scoped></style>