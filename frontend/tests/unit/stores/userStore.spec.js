
import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '@/stores/userStore'
import * as api from '@/services/api'
import * as localData from '@/services/localData'

const mockUser = { email: 'test@mail.com', role: 'CLIENT', specialties: [], bio: '', price: 0 }
const mockToken = 'mocked-token'

vi.mock('@/services/localData', async () => {
    const actual = await vi.importActual('@/services/localData')
    return {
        ...actual,
        getTokenFromStorage: () => null,
        getUserFromStorage: () => null,
        storeTokenInStorage: vi.fn(),
        storeUserInStorage: vi.fn(),
        clearTokenInStorage: vi.fn(),
        clearUserInStorage: vi.fn(),
    }
})

describe('userStore', () => {
    beforeEach(() => {
        setActivePinia(createPinia())
    })

    it('logs in and stores user/token', async () => {
        vi.spyOn(api, 'apiLogin').mockResolvedValue({ user: mockUser, token: mockToken })

        const store = useUserStore()
        await store.login({ email: 'test@mail.com', password: '123' })

        expect(store.user).toEqual(mockUser)
        expect(store.token).toBe(mockToken)
        expect(localData.storeTokenInStorage).toHaveBeenCalledWith(mockToken)
        expect(localData.storeUserInStorage).toHaveBeenCalledWith(mockUser)
    })

    it('registers and stores user/token', async () => {
        vi.spyOn(api, 'apiRegister').mockResolvedValue({ user: mockUser, token: mockToken })

        const store = useUserStore()
        await store.register({ email: 'test@mail.com', password: '123', fullName: 'Test' })

        expect(store.user).toEqual(mockUser)
        expect(store.token).toBe(mockToken)
        expect(localData.storeTokenInStorage).toHaveBeenCalledWith(mockToken)
        expect(localData.storeUserInStorage).toHaveBeenCalledWith(mockUser)
    })

    it('logs out and clears user/token', () => {
        const store = useUserStore()
        store.user = mockUser
        store.token = mockToken

        store.logout()

        expect(store.user).toBe(null)
        expect(store.token).toBe(null)
        expect(localData.clearTokenInStorage).toHaveBeenCalled()
        expect(localData.clearUserInStorage).toHaveBeenCalled()
    })

    it('sets user with setUser', () => {
        const store = useUserStore()
        store.setUser(mockUser)

        expect(store.user).toEqual(mockUser)
        expect(localData.storeUserInStorage).toHaveBeenCalledWith(mockUser)
    })

    it('isAuthenticated getter works', () => {
        const store = useUserStore()
        store.user = mockUser
        expect(store.isAuthenticated).toBe(true)

        store.user = null
        expect(store.isAuthenticated).toBe(false)
    })

    it('prettyRole getter works', () => {
        const store = useUserStore()
        store.user = { ...mockUser, role: 'SPECIALIST' }
        expect(store.prettyRole).toBe('Специалист')

        store.user = { ...mockUser, role: 'CLIENT' }
        expect(store.prettyRole).toBe('Клиент')
    })
})
