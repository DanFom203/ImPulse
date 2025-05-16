import { defineStore } from 'pinia'
import { RequestData } from '@/models/util.js'
import { apiFetchAppointments, apiCreateAppointment, apiApproveAppointment } from '@/services/api'

export const useAppointmentStore = defineStore('appointmentStore', {
  state: () => ({
    appointments: [],
    requestData: new RequestData(false, null)
  }),
  actions: {
    async fetchAppointments() {
      this.requestData.startLoading()

      try {
        this.appointments = await apiFetchAppointments()
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    },
    async createAppointment(createAppointmentDto) {
      this.requestData.startLoading()

      try {
        await apiCreateAppointment(createAppointmentDto)
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    },
    async approveAppointment(appointmentId) {
      this.requestData.startLoading()

      try {
        await apiApproveAppointment(appointmentId)

        for (let appointment of this.appointments) {
          if (appointment.id === appointmentId) {
            appointment.isApproved = true
          }
        }
      } catch (error) {
        this.requestData.setError(error)
      }

      this.requestData.stopLoading()
    }
  }
})
