<template>
  <MainLayout>
    <div class="card-container">
      <h1 class="page-title">Записи</h1>

      <DefaultLoader v-if="requestData.loading" />
      <div class="error" v-else-if="requestData.error !== null">
        {{ requestData.error.message }}
      </div>

      <div class="appointments" v-else>
        <div
            v-for="appointment in appointments"
            :key="appointment.id"
            class="appointment-item"
        >
          <div v-if="user.role === 'CLIENT'">
            Тренер:
            <RouterLink :to="{ name: 'Specialist', params: { id: appointment.specialist.id } }">
              {{ appointment.specialist.fullName }}
            </RouterLink>
            <button @click="openChat(appointment.specialist)">Чат</button>
          </div>
          <div v-else>
            Клиент: {{ appointment.client.fullName }}
            <button @click="openChat(appointment.client)">Чат</button>
            <div v-if="!appointment.isApproved">
              <button class="approve-button" @click="approve(appointment.id)">
                Approve
              </button>
            </div>
          </div>

          <div class="meta">
            <div>💰 <strong>{{ appointment.price }}</strong></div>
            <div>🕒 {{ appointment.scheduledAt.slice(0, 16) }}</div>
          </div>

          <div>
            <span v-if="appointment.isApproved" class="approved">✅ Подтверждено</span>
            <span v-else class="not-approved">⏳ Не подтверждено</span>
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
  components: { MainLayout, DefaultLoader, ChatWidget },
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

<style src="@/assets/appointmentsList.css" scoped></style>
