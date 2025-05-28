import {defineStore} from 'pinia'
import {RequestData} from '@/models/util.js'
import {apiFetchSpecialtyList} from '@/services/api'

export const useSpecialtyStore = defineStore('specialtyStore', {
  state: () => ({
    specialtyList: [],
    requestData: new RequestData(false, null)
  }),
  actions: {
    async ensureLoaded() {
      if (this.specialtyList.length === 0) {
        this.fetchSpecialtyList()
      }
    },
    async fetchSpecialtyList() {
      this.requestData.startLoading()

      try {
        this.specialtyList = await apiFetchSpecialtyList()
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    }
  }
})
