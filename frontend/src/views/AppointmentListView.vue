<template>
  <MainLayout>
    <h1>Appointments</h1>
    <DefaultLoader v-if="requestData.loading" />
    <div class="error" v-else-if="requestData.error !== null">
      {{ requestData.error.message }}
    </div>
    <div class="appointment-data" v-else>
      <div class="appointments">
        <div v-for="appointment in appointments" :key="appointment.id" class="appointment">
          <div v-if="user.role === 'CLIENT'">
            <div>
              Specialist:
              <RouterLink :to="{ name: 'Specialist', params: { id: appointment.specialist.id } }">
                {{ appointment.specialist.fullName }}
              </RouterLink>
              <button @click="openChat(appointment.specialist)">Chat</button>
            </div>
          </div>
          <div v-else>
            <div>
              Client: {{ appointment.client.fullName }}
              <button @click="openChat(appointment.client)">Chat</button>
            </div>
            <div v-if="!appointment.isApproved">
              <button class="approve-button" @click="approve(appointment.id)">Approve</button>
            </div>
          </div>
          <div>Price: {{ appointment.price }}</div>
          <div>Scheduled at: {{ appointment.scheduledAt.slice(0, 16) }}</div>
          <div>
            <span v-if="appointment.isApproved" class="approved">Approved</span>
            <span v-else class="not-approved">Not approved yet</span>
          </div>
        </div>
      </div>
    </div>

    <ChatWidget
        v-if="selectedInterlocutor"
        :interlocutor="selectedInterlocutor"
        @close="closeChat"
    />
  </MainLayout>
</template>

<script>
import MainLayout from '../components/blocks/MainLayout.vue'
import ChatWidget from '../components/blocks/ChatWidget.vue'
import DefaultLoader from '../components/utils/DefaultLoader.vue'
import { useAppointmentStore } from '../stores/appointmentStore.js'
import { useUserStore } from '../stores/userStore.js'
import { mapActions, mapState } from 'pinia'

export default {
  name: 'AppointmentListView',
  components: {
    MainLayout,
    DefaultLoader,
    ChatWidget
  },
  data() {
    return {
      selectedInterlocutor: null
    }
  },
  async beforeMount() {
    this.fetchAppointments()
  },
  methods: {
    ...mapActions(useAppointmentStore, ['fetchAppointments', 'approveAppointment']),
    async approve(appointmentId) {
      await this.approveAppointment(appointmentId)
    },
    openChat(user) {
      console.log('openChat called with', user)
      this.selectedInterlocutor = user
    },
    closeChat() {
      this.selectedInterlocutor = null
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
  padding: 10px;
  margin-bottom: 10px;
}

.approved {
  background-color: lightgreen;
}

.not-approved {
  background-color: yellow;
}
</style>
