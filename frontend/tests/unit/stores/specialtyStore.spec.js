import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useSpecialtyStore } from '@/stores/specialtyStore'
import * as api from '@/services/api'

describe('specialtyStore', () => {
    beforeEach(() => {
        setActivePinia(createPinia())
    })

    it('fetches specialty list successfully', async () => {
        const mockData = ['Yoga', 'Fitness']
        vi.spyOn(api, 'apiFetchSpecialtyList').mockResolvedValue(mockData)

        const store = useSpecialtyStore()
        await store.fetchSpecialtyList()

        expect(store.specialtyList).toEqual(mockData)
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(null)
    })

    it('handles error during fetchSpecialtyList', async () => {
        const error = new Error('Failed to fetch specialties')
        vi.spyOn(api, 'apiFetchSpecialtyList').mockRejectedValue(error)

        const store = useSpecialtyStore()
        await store.fetchSpecialtyList()

        expect(store.specialtyList).toEqual([])
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(error)
    })

    it('does not call fetchSpecialtyList if list already loaded', async () => {
        const spy = vi.spyOn(api, 'apiFetchSpecialtyList')
        const store = useSpecialtyStore()

        store.specialtyList = ['Pilates']
        await store.ensureLoaded()

        expect(spy).not.toHaveBeenCalled()
    })

    it('calls fetchSpecialtyList if list is empty', async () => {
        const mockData = ['Boxing']
        const spy = vi.spyOn(api, 'apiFetchSpecialtyList').mockResolvedValue(mockData)

        const store = useSpecialtyStore()
        await store.ensureLoaded()

        expect(spy).toHaveBeenCalledOnce()
        expect(store.specialtyList).toEqual(mockData)
    })
})
