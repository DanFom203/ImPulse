import { defineStore } from 'pinia'
import { RequestData } from '@/models/util.js'
import { apiGetAllUsers, apiUpdateUserAuthority, apiDeleteUser } from '@/services/api.js'
import { useUserStore } from '@/stores/userStore.js'

export const useModeratorStore = defineStore('moderatorStore', {
    state: () => ({
        users: [],
        requestData: new RequestData(false, null)
    }),
    getters: {
        currentModeratorEmail: () => {
            const userStore = useUserStore()
            return userStore.user?.email || null
        }
    },
    actions: {
        async fetchAllUsers() {
            this.requestData.startLoading()
            try {
                this.users = await apiGetAllUsers()
            } catch (error) {
                this.requestData.setError(error)
            }
            this.requestData.stopLoading()
        },
        async updateUserAuthority(userId, newAuthority) {
            try {
                await apiUpdateUserAuthority(userId, newAuthority)
                const user = this.users.find(u => u.id === userId)
                if (user) user.authority = newAuthority
            } catch (error) {
                alert('Ошибка при обновлении статуса: ' + error.message)
            }
        },
        async deleteUser(userId) {
            try {
                await apiDeleteUser(userId)
                this.users = this.users.filter(u => u.id !== userId)
            } catch (error) {
                alert('Ошибка при удалении пользователя: ' + error.message)
            }
        }
    }
})
