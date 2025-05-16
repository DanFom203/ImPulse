import { defineStore } from 'pinia'
import { RequestData } from '@/models/util.js'
import { apiFetchSpecialists, apiFetchSpecialist } from '@/services/api'

export const useSpecialistStore = defineStore('specialistStore', {
  state: () => ({
    specialists: [],
    currentSpecialist: null,
    requestData: new RequestData(false, null)
  }),
  actions: {
    async fetchSpecialists(searchFilterDto) {
      this.requestData.startLoading()

      try {
        this.specialists = await apiFetchSpecialists(searchFilterDto)
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    },
    async fetchSpecialist(id) {
      this.requestData.startLoading()

      try {
        this.currentSpecialist = await apiFetchSpecialist(id)
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    }
  }
})
