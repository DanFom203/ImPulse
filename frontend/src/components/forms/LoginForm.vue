<template>
  <div class="auth-container">
    <BaseAuthForm @submit.prevent="submit" :error="error" title="Sign in">
      <template v-slot:fields>
        <FormField
            name="email"
            v-model="form.email"
            :errors="v$.form.email.$errors"
            label="Email"
            type="email"
            required
            @input="touchField('email')"
        />
        <FormField
            name="password"
            v-model="form.password"
            :errors="v$.form.password.$errors"
            label="Password"
            type="password"
            required
            @input="touchField('password')"
        />
      </template>

      <template v-slot:submit-button>
        <FormSubmit value="Sign in" />
      </template>

      <template v-slot:links>
        <router-link class="router-link" :to="{ name: 'Registration' }">Sign up</router-link>
      </template>
    </BaseAuthForm>
  </div>
</template>

<script>
import BaseAuthForm from './BaseAuthForm.vue'
import FormField from './FormField.vue'
import useVuelidate from '@vuelidate/core'
import { email, maxLength, minLength, required, helpers } from '@vuelidate/validators'
import { useUserStore } from '../../stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import { LoginDto } from '../../models/dto.js'

export default {
  name: 'LoginForm',
  components: { FormSubmit, FormField, BaseAuthForm },
  setup() {
    const v$ = useVuelidate()
    return { v$ }
  },
  data() {
    return {
      form: {
        email: 'ilya@ilya.com',
        password: '123123'
      }
    }
  },
  computed: {
    ...mapState(useUserStore, {
      error: (state) => state.requestData.error
    })
  },
  validations: {
    form: {
      email: {
        required: helpers.withMessage('This field is required', required),
        email: helpers.withMessage('Not a valid email', email)
      },
      password: {
        required: helpers.withMessage('This field is required', required),
        minLength: helpers.withMessage('Min length is 5 characters', minLength(5)),
        maxLength: helpers.withMessage('Max length is 255 characters', maxLength(255))
      }
    }
  },
  methods: {
    ...mapActions(useUserStore, ['login']),
    async submit() {
      this.v$.form.$touch()
      if (this.v$.form.$error) {
        return
      }
      const loginDto = new LoginDto(this.form.email, this.form.password)
      await this.login(loginDto)
      if (!this.error) {
        this.$router.push({ name: 'Home' })
      }
    },
    touchField(fieldName) {
      this.v$.form[fieldName].$touch()
    }
  }
}
</script>

<style src="./src/assets/Auth.css"></style>
