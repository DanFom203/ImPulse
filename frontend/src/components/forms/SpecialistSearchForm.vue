<template>
  <form @submit.prevent="this.submit" method="post" novalidate class="search-form">
    <input type="text" placeholder="Specialist name" v-model="form.searchString" />

    <div class="filter">
      <h3>Filter</h3>

      <div class="options-list filter__options-list">
        <div class="option filter__price">
          <p>Price</p>
          <div class="filter__price-item">
            <label for="filter-price-max">Max</label>
            <input
              type="number"
              name="filter-price-max"
              id="filter-price-max"
              v-model="form.filterParams.price.max"
            />
          </div>

          <div class="filter__price-item">
            <label for="filter-price-min">Min</label>
            <input
              type="number"
              name="filter-price-min"
              id="filter-price-min"
              v-model="form.filterParams.price.min"
            />
          </div>
        </div>

        <div class="option filter__rating">
          <p>Rating</p>
          <div class="filter__rating-item">
            <label for="filter-rating-min">Min</label>
            <input
              type="number"
              name="filter-rating"
              id="filter-rating-min"
              v-model="form.filterParams.minRating"
            />
          </div>
        </div>
      </div>
    </div>

    <div class="sort">
      <h3>Sort</h3>

      <div class="options-list sort__options-list">
        <div class="option sort__by">
          <p>Sort by</p>
          <div class="sort__by-item">
            <label for="sort-by-rating">Rating</label>
            <input
              type="radio"
              name="sort-by"
              id="sort-by-rating"
              value="rating"
              v-model="form.order.by"
            />
          </div>

          <div class="sort__by-item">
            <label for="sort-by-price">Price</label>
            <input
              type="radio"
              name="sort-by"
              id="sort-by-price"
              value="price"
              v-model="form.order.by"
            />
          </div>
        </div>

        <div class="option sort__type">
          <p>Sort type</p>
          <div class="sort__type-descending">
            <label for="sort-type-descending">Descending</label>
            <input
              type="radio"
              name="sort-type"
              id="sort-type-descending"
              value="descending"
              v-model="form.order.type"
            />
          </div>

          <div class="sort__type-ascending">
            <label for="sort-type-ascending">Ascending</label>
            <input
              type="radio"
              name="sort-type"
              id="sort-type-ascending"
              value="ascending"
              v-model="form.order.type"
            />
          </div>
        </div>
      </div>
    </div>

    <div class="specialty">
      <DefaultLoader v-if="specialtyListRequestData.loading" />
      <div class="error" v-else-if="specialtyListRequestData.error !== null">
        {{ specialtyListRequestData.error.message }}
      </div>
      <div class="specialtyList" v-else>
        <div class="specialty" v-for="specialty in specialtyList" v-bind:key="specialty.id">
          <label :for="`specialty-${specialty.id}`">{{ specialty.name }}</label>
          <input
            type="checkbox"
            name="specialtyList"
            :value="specialty.id"
            v-model="form.filterParams.specialtyList"
            :id="`specialty-${specialty.id}`"
          />
        </div>
      </div>
    </div>

    <input type="submit" value="Search" />
  </form>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import DefaultLoader from '../utils/DefaultLoader.vue'
import { SearchFilterDto } from '../../models/dto.js'
import { useSpecialtyStore } from '../../stores/specialtyStore.js'
import { useSpecialistStore } from '../../stores/specialistStore.js'
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

<style scoped>
.search-form {
  font-size: 20px;
}

.options-list {
  display: flex;
}

.option {
  border: 1px solid black;
  padding: 5px;
  border-radius: 5px;
  margin-right: 5px;
}
</style>
