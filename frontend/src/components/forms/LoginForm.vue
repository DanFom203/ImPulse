<template>
  <div class="auth-container">
    <BaseAuthForm @submit.prevent="submit" :error="error" title="Вход">
      <template v-slot:fields>
        <FormField
            name="email"
            v-model="form.email"
            :errors="v$.form.email.$errors"
            label="Электронная почта"
            type="email"
            required
            @input="touchField('email')"
        />
        <FormField
            name="password"
            v-model="form.password"
            :errors="v$.form.password.$errors"
            label="Пароль"
            type="password"
            required
            @input="touchField('password')"
        />
      </template>

      <template v-slot:submit-button>
        <FormSubmit value="Войти" />
      </template>

      <template v-slot:links>
        <router-link class="router-link" :to="{ name: 'Registration' }">Зарегистрироваться</router-link>
      </template>
    </BaseAuthForm>
  </div>
</template>
<script>
import BaseAuthForm from './BaseAuthForm.vue'
import FormField from './FormField.vue'
import useVuelidate from '@vuelidate/core'
import { email, maxLength, minLength, required, helpers } from '@vuelidate/validators'
import { useUserStore } from '@/stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import FormSubmit from './FormSubmit.vue'
import { LoginDto } from '@/models/dto.js'

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
        email: '',
        password: ''
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
        required: helpers.withMessage('Обязательное поле', required),
        email: helpers.withMessage('Некорректный email', email)
      },
      password: {
        required: helpers.withMessage('Обязательное поле', required),
        minLength: helpers.withMessage('Минимум 5 символов', minLength(5)),
        maxLength: helpers.withMessage('Максимум 255 символов', maxLength(255))
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

<style src="@/assets/auth.css"></style>
