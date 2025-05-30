import { setActivePinia, createPinia } from 'pinia'
import { useModeratorStore } from '@/stores/moderatorStore.js'
import { vi, describe, it, expect, beforeEach } from 'vitest'


vi.mock('@/services/api.js', () => ({
    apiGetAllUsers: vi.fn(),
    apiUpdateUserAuthority: vi.fn(),
    apiDeleteUser: vi.fn()
}))


vi.mock('@/stores/userStore.js', () => ({
    useUserStore: () => ({
        user: { email: 'moderator@test.com' }
    })
}))

import { apiGetAllUsers, apiUpdateUserAuthority, apiDeleteUser } from '@/services/api.js'

describe('moderatorStore', () => {
    let store

    beforeEach(() => {
        setActivePinia(createPinia())
        store = useModeratorStore()
    })

    it('fetchAllUsers sets users from API', async () => {
        const mockUsers = [{ email: 'user1@test.com', authority: 'USER' }]
        apiGetAllUsers.mockResolvedValueOnce(mockUsers)

        await store.fetchAllUsers()

        expect(store.users).toEqual(mockUsers)
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(null)
    })

    it('updateUserAuthority updates user authority in state', async () => {
        store.users = [{ email: 'user1@test.com', authority: 'USER' }]
        apiUpdateUserAuthority.mockResolvedValueOnce()

        await store.updateUserAuthority('user1@test.com', 'MODERATOR')

        expect(store.users[0].authority).toBe('MODERATOR')
    })

    it('deleteUser removes user from state', async () => {
        store.users = [
            { email: 'user1@test.com' },
            { email: 'user2@test.com' }
        ]
        apiDeleteUser.mockResolvedValueOnce()

        await store.deleteUser('user1@test.com')

        expect(store.users).toHaveLength(1)
        expect(store.users[0].email).toBe('user2@test.com')
    })

    it('currentModeratorEmail getter returns email from user store', () => {
        expect(store.currentModeratorEmail).toBe('moderator@test.com')
    })
})
