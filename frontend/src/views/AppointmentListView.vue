<template>
  <MainLayout>
    <h1>Appointments</h1>
    <DefaultLoader v-if="requestData.loading" />
    <div class="error" v-else-if="requestData.error !== null">
      {{ requestData.error.message }}
    </div>
    <div class="appointment-data" v-else>
      <div class="appointments">
        <div v-for="appointment in appointments" v-bind:key="appointment.id" class="appointment">
          <div class="appointment-for-client" v-if="user.role === 'CLIENT'">
            <div class="appointment-specialist">
              <div class="specialist-name">
                Specialist:
                <RouterLink :to="{ name: 'Specialist', params: { id: appointment.specialist.id } }">
                  {{ appointment.specialist.fullName }}
                </RouterLink>
              </div>
              <div class="specialist-rating">Rating: {{ appointment.specialist.rating }}</div>
            </div>
          </div>
          <div class="appointment-for-specialist" v-else>
            <div class="appointment-client">
              <div class="client-name">Client: {{ appointment.client.fullName }}</div>
            </div>

            <div class="appointment-approve" v-if="!appointment.isApproved">
              <button class="approve-button" @click="approve(appointment.id)">Approve</button>
            </div>
          </div>

          <div class="appointment-price">Price: {{ appointment.price }}</div>

          <div class="appointment-scheduled">
            Scheduled at: {{ appointment.scheduledAt.slice(0, 16) }}
          </div>

          <div class="appointment-status">
            <span class="approved" v-if="appointment.isApproved">Approved</span>
            <span class="not-approved" v-else>Not approved yet</span>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'
import { useAppointmentStore } from '../stores/appointmentStore.js'
import { useUserStore } from '../stores/userStore.js'
import { mapActions, mapState } from 'pinia'
import DefaultLoader from '../components/utils/DefaultLoader.vue'

export default {
  name: 'AppointmentListView',
  components: { MainLayout, DefaultLoader },
  async beforeMount() {
    this.fetchAppointments()
  },
  methods: {
    ...mapActions(useAppointmentStore, ['fetchAppointments', 'approveAppointment']),
    async approve(appointmentId) {
      this.approveAppointment(appointmentId)
    }
  },
  computed: {
    ...mapState(useAppointmentStore, {
      requestData: (state) => state.requestData,
      appointments: (state) => state.appointments
    }),
    ...mapState(useUserStore, ['user'])
  }
}
</script>

<style scoped>
.appointment {
  border: 1px solid black;
}

.approved {
  background-color: lightgreen;
}

.not-approved {
  background-color: yellow;
}
</style>
