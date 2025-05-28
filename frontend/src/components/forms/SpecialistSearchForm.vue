<template>
  <form @submit.prevent="this.submit" method="post" novalidate class="search-form">
    <input class="search-input" type="text" placeholder="Имя тренера" v-model="form.searchString" />

    <div class="filter card">
      <div class="options-list">
        <div class="option">
          <p>Цена</p>
          <div class="filter__price-item">
            <label for="filter-price-max">Максимум</label>
            <input type="number" id="filter-price-max" v-model="form.filterParams.price.max" />
          </div>
          <div class="filter__price-item">
            <label for="filter-price-min">Минимум</label>
            <input type="number" id="filter-price-min" v-model="form.filterParams.price.min" />
          </div>
        </div>

        <div class="option">
          <p>Рейтинг</p>
          <div class="filter__rating-item">
            <label for="filter-rating-min">Минимум</label>
            <input type="number" id="filter-rating-min" v-model="form.filterParams.minRating" />
          </div>
        </div>
      </div>
    </div>

    <div class="sort card">
      <div class="options-list">
        <div class="option">
          <p>Сортировать по:</p>
          <div>
            <label><input type="radio" value="rating" v-model="form.order.by" /> Рейтингу</label>
            <label><input type="radio" value="price" v-model="form.order.by" /> Цене</label>
          </div>
        </div>

        <div class="option">
          <p>Тип сортировки</p>
          <div>
            <label><input type="radio" value="descending" v-model="form.order.type" /> По убыванию</label>
            <label><input type="radio" value="ascending" v-model="form.order.type" /> ПО возрастанию</label>
          </div>
        </div>
      </div>
    </div>

    <div class="specialty card">
      <DefaultLoader v-if="specialtyListRequestData.loading" />
      <div class="error" v-else-if="specialtyListRequestData.error !== null">
        {{ specialtyListRequestData.error.message }}
      </div>
      <div v-else>
        <h3>Специальности</h3>
        <div class="scrollable-specialties">
          <div class="specialty-item" v-for="specialty in specialtyList" :key="specialty.id">
            <label :for="`specialty-${specialty.id}`">{{ specialty.name }}</label>
            <input type="checkbox" :value="specialty.id" v-model="form.filterParams.specialtyList" :id="`specialty-${specialty.id}`" />
          </div>
        </div>
      </div>
    </div>

    <button type="submit" class="submit-button">Поиск</button>
  </form>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import DefaultLoader from '../utils/DefaultLoader.vue'
import { SearchFilterDto } from '@/models/dto.js'
import { useSpecialtyStore } from '@/stores/specialtyStore.js'
import { useSpecialistStore } from '@/stores/specialistStore.js'
import { mapActions, mapState } from 'pinia'

export default {
  name: 'SpecialistSearchForm',
  components: { DefaultLoader },
  methods: {
    async submit() {
      this.fetchSpecialists(this.form)
    },
    ...mapActions(useSpecialtyStore, ['ensureLoaded']),
    ...mapActions(useSpecialistStore, ['fetchSpecialists'])
  },
  async beforeMount() {
    this.ensureLoaded()
  },
  setup() {
    return {
      v$: useVuelidate()
    }
  },
  data() {
    return {
      form: new SearchFilterDto()
    }
  },
  computed: {
    ...mapState(useSpecialtyStore, {
      specialtyListRequestData: (state) => state.requestData,
      specialtyList: (state) => state.specialtyList
    })
  }
}
</script>

<style src="@/assets/specialistSearchForm.css" scoped></style>
