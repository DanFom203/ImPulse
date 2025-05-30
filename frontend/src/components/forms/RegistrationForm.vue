<template>
  <div class="auth-container">
    <BaseAuthForm @submit.prevent="submit" :error="error" title="Регистрация">
      <template v-slot:fields>
        <FormField
            name="email"
            v-model="form.email"
            :errors="v$.form.email.$errors"
            label="Электронная почта"
            type="email"
            required
            @input="v$.form.email.$touch()"
        />

        <FormField
            name="fullName"
            v-model="form.fullName"
            :errors="v$.form.fullName.$errors"
            label="ФИО"
            required
            @input="v$.form.fullName.$touch()"
        />

        <FormField
            name="password"
            v-model="form.password"
            :errors="v$.form.password.$errors"
            label="Пароль"
            type="password"
            required
            @input="v$.form.password.$touch(); v$.form.passwordRepeat.$touch()"
        />

        <FormField
            name="passwordRepeat"
            v-model="form.passwordRepeat"
            :errors="v$.form.passwordRepeat.$errors"
            label="Повторите пароль"
            type="password"
            required
            @input="v$.form.passwordRepeat.$touch()"
        />

        <div class="role-selector">
          <input
              type="radio"
              name="role"
              id="role-client"
              value="CLIENT"
              v-model="form.role"
              hidden
          />
          <label :class="{ active: form.role === 'CLIENT' }" for="role-client">
            Клиент
          </label>

          <input
              type="radio"
              name="role"
              id="role-specialist"
              value="SPECIALIST"
              v-model="form.role"
              hidden
          />
          <label :class="{ active: form.role === 'SPECIALIST' }" for="role-specialist">
            Специалист
          </label>
        </div>
      </template>

      <template v-slot:submit-button>
        <FormSubmit value="Зарегистрироваться" />
      </template>

      <template v-slot:links>
        <router-link :to="{ name: 'Login' }">Уже есть аккаунт? Войти</router-link>
      </template>
    </BaseAuthForm>
  </div>
</template>

<script>
import {computed, reactive} from 'vue'
import useVuelidate from '@vuelidate/core'
import {
  email,
  helpers,
  maxLength,
  minLength,
  required,
  sameAs
} from '@vuelidate/validators'
import BaseAuthForm from './BaseAuthForm.vue'
import FormField from './FormField.vue'
import FormSubmit from './FormSubmit.vue'
import {mapActions, mapState} from 'pinia'
import {useUserStore} from '@/stores/userStore.js'
import {RegistrationDto} from '@/models/dto.js'

export default {
  name: 'RegistrationForm',
  components: {BaseAuthForm, FormField, FormSubmit},
  setup() {
    const form = reactive({
      email: '',
      fullName: '',
      password: '',
      passwordRepeat: '',
      role: ''
    })

    const rules = computed(() => ({
      form: {
        email: {
          required: helpers.withMessage('Обязательное поле', required),
          email: helpers.withMessage('Некорректный email', email)
        },
        fullName: {
          required: helpers.withMessage('Обязательное поле', required),
          minLength: helpers.withMessage('Минимум 1 символ', minLength(1)),
          maxLength: helpers.withMessage('Максимум 255 символов', maxLength(255))
        },
        password: {
          required: helpers.withMessage('Обязательное поле', required),
          minLength: helpers.withMessage('Минимум 5 символов', minLength(5)),
          maxLength: helpers.withMessage('Максимум 255 символов', maxLength(255))
        },
        passwordRepeat: {
          required: helpers.withMessage('Обязательное поле', required),
          sameAsPassword: helpers.withMessage(
              'Пароли не совпадают',
              sameAs(computed(() => form.password))
          )
        },
        role: {
          required: helpers.withMessage('Выберите роль', required)
        }
      }
    }))

    const v$ = useVuelidate(rules, {form}, {$autoDirty: true})

    return {
      form,
      v$
    }
  },
  data() {
    return {}
  },
  computed: {
    ...mapState(useUserStore, {
      error: (state) => state.requestData.error
    })
  },
  methods: {
    ...mapActions(useUserStore, ['register']),
    async submit() {
      this.v$.form.$touch()
      if (this.v$.form.$error) return

      const dto = new RegistrationDto(
          this.form.email,
          this.form.fullName,
          this.form.password,
          this.form.passwordRepeat,
          this.form.role
      )

      await this.register(dto)

      if (!this.error) {
        this.$router.push({name: 'Home'})
      }
    }
  }
}
</script>
