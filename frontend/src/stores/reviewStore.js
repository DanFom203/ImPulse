import { defineStore } from 'pinia'
import { RequestData } from '@/models/util.js'
import { apiFetchClientReviews, apiCreateReview } from '@/services/api'

export const useReviewStore = defineStore('reviewStore', {
  state: () => ({
    reviews: [],
    requestData: new RequestData(false, null)
  }),
  actions: {
    async fetchReviews() {
      this.requestData.startLoading()

      try {
        this.reviews = await apiFetchClientReviews()
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    },
    async createReview(specialistId, createReviewDto) {
      this.requestData.startLoading()

      try {
        await apiCreateReview(specialistId, createReviewDto)
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    }
  }
})
