<template>
  <div class="form-floating">
    <input
        :class="getClass"
        :name="name"
        :type="type"
        :id="id"
        :value="modelValue"
        :placeholder="placeholder"
        :required="required"
        @input="onInput"
    />
    <label :for="id">{{ displayLabel }}</label>
  </div>
</template>

<script>
export default {
  name: 'FormField',
  emits: ['update:modelValue', 'input'],
  props: {
    name: { type: String, required: true },
    required: { type: Boolean, default: false },
    type: { type: String, default: 'text' },
    id: { type: String, default: (props) => props.name },
    label: { type: String, default: (props) => props.name },
    placeholder: { type: String, default: (props) => props.label },
    modelValue: { type: String, default: '' },
    errors: { type: Array, default: () => [] }
  },
  computed: {
    getClass() {
      return 'form-control' + (this.errors.length > 0 ? ' is-invalid' : '')
    },
    displayLabel() {
      return this.errors.length > 0 ? this.errors[0].$message : this.label
    }
  },
  methods: {
    onInput(event) {
      const value = event.target.value
      this.$emit('update:modelValue', value)
      this.$emit('input', value)
    }
  }
}
</script>

<style scoped>
.form-floating {
  margin-bottom: 12px;
}

input {
  min-height: 48px; /* Защита от сдвига */
}

label {
  color: #aaa;
  font-size: 0.9rem;
  transition: color 0.2s;
}

.is-invalid + label {
  color: #ff6b6b;
}
</style>
