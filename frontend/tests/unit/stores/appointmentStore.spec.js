

import { setActivePinia, createPinia } from 'pinia'
import { useAppointmentStore } from '@/stores/appointmentStore'
import { vi, describe, it, expect, beforeEach } from 'vitest'


vi.mock('@/services/api', () => ({
    apiFetchAppointments: vi.fn().mockResolvedValue([
        { id: 1, isApproved: false },
        { id: 2, isApproved: true }
    ]),
    apiCreateAppointment: vi.fn().mockResolvedValue({}),
    apiApproveAppointment: vi.fn().mockResolvedValue({})
}))

describe('Appointment Store', () => {
    beforeEach(() => {
        setActivePinia(createPinia())
    })

    it('fetchAppointments загружает список', async () => {
        const store = useAppointmentStore()
        await store.fetchAppointments()

        expect(store.appointments).toHaveLength(2)
        expect(store.appointments[0].id).toBe(1)
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(null)
    })

    it('approveAppointment обновляет статус записи', async () => {
        const store = useAppointmentStore()
        store.appointments = [
            { id: 1, isApproved: false }
        ]

        await store.approveAppointment(1)

        expect(store.appointments[0].isApproved).toBe(true)
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(null)
    })
})
