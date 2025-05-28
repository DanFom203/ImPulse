<template>
  <div class="auth-container">
    <BaseAuthForm @submit.prevent="submit" :error="error" title="Sign up">
      <template v-slot:fields>
        <FormField
            name="email"
            v-model="form.email"
            :errors="v$.form.email.$errors"
            label="Email"
            type="email"
            required
            @input="v$.form.email.$touch()"
        />

        <FormField
            name="fullName"
            v-model="form.fullName"
            :errors="v$.form.fullName.$errors"
            label="Name"
            required
            @input="v$.form.fullName.$touch()"
        />

        <FormField
            name="password"
            v-model="form.password"
            :errors="v$.form.password.$errors"
            label="Password"
            type="password"
            required
            @input="v$.form.password.$touch(); v$.form.passwordRepeat.$touch()"
        />

        <FormField
            name="passwordRepeat"
            v-model="form.passwordRepeat"
            :errors="v$.form.passwordRepeat.$errors"
            label="Password again"
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
            Client
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
            Specialist
          </label>

          <input
              type="radio"
              name="role"
              id="role-moderator"
              value="MODERATOR"
              v-model="form.role"
              hidden
          />
          <label :class="{ active: form.role === 'MODERATOR' }" for="role-moderator">
            Moderator
          </label>
        </div>

      </template>

      <template v-slot:submit-button>
        <FormSubmit value="Sign up"/>
      </template>

      <template v-slot:links>
        <router-link :to="{ name: 'Login' }">Sign in</router-link>
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
          required: helpers.withMessage('This field is required', required),
          email: helpers.withMessage('Not a valid email', email)
        },
        fullName: {
          required: helpers.withMessage('This field is required', required),
          minLength: helpers.withMessage('Min length is 1 character', minLength(1)),
          maxLength: helpers.withMessage('Max length is 255 characters', maxLength(255))
        },
        password: {
          required: helpers.withMessage('This field is required', required),
          minLength: helpers.withMessage('Min length is 5 characters', minLength(5)),
          maxLength: helpers.withMessage('Max length is 255 characters', maxLength(255))
        },
        passwordRepeat: {
          required: helpers.withMessage('This field is required', required),
          sameAsPassword: helpers.withMessage(
              'Must be the same as password',
              sameAs(computed(() => form.password))
          )
        },
        role: {
          required: helpers.withMessage('This field is required', required)
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
