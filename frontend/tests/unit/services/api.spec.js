import { describe, it, expect, vi, beforeEach } from 'vitest'
import * as api from '@/services/api.js'

vi.mock('axios', () => {
    const get = vi.fn()
    const post = vi.fn()
    const interceptors = {
        request: { use: vi.fn() },
        response: { use: vi.fn() },
    }
    const create = vi.fn(() => ({
        get,
        post,
        interceptors,
    }))

    return {
        default: {
            create,
        }
    }
})

describe('api.js', () => {
    const axios = require('axios').default

    beforeEach(() => {

        axios.create().get.mockReset()
        axios.create().post.mockReset()
    })

    it('apiGetAllUsers возвращает данные', async () => {
        const fakeUsers = [{ id: 1, name: 'John' }, { id: 2, name: 'Jane' }]
        axios.create().get.mockResolvedValue({ data: { results: fakeUsers } })

        const users = await api.apiGetAllUsers()
        expect(users).toEqual(fakeUsers)
    })

    it('apiUpdateUserAuthority делает POST с email и authority', async () => {
        const responseData = { success: true }
        axios.create().post.mockResolvedValue({ data: responseData })

        const result = await api.apiUpdateUserAuthority('email@test.com', 'admin')
        expect(result).toEqual(responseData)
        expect(axios.create().post).toHaveBeenCalledWith(
            '/updateAuthority',
            { email: 'email@test.com', authority: 'admin' }
        )
    })

    it('apiDeleteUser делает POST с email', async () => {
        mockInstancePost.mockResolvedValue({})

        await api.apiDeleteUser('a@b.com')

        expect(mockInstancePost).toHaveBeenCalledWith('/moderation/delete', {
            email: 'a@b.com',
        })
    })

    it('apiUploadProfilePhoto загружает фото и возвращает data', async () => {
        const responseData = { url: 'url' }
        mockInstancePost.mockResolvedValue({ data: responseData })

        const result = await api.apiUploadProfilePhoto('url')

        expect(mockInstancePost).toHaveBeenCalledWith('/profile/update/avatar', {
            avatar: 'url',
        })
        expect(result).toEqual(responseData)
    })

    it('apiLogin логинит и возвращает user и token', async () => {
        const token = 'tok'
        const userData = { id: 1 }
        const loginDto = { toRepresentation: () => ({ login: 'test' }) }
        mockNoAuthPost.mockResolvedValue({ data: { token, user: userData } })
        User.fromMap.mockReturnValue(userData)

        const result = await api.apiLogin(loginDto)

        expect(mockNoAuthPost).toHaveBeenCalledWith('/auth/login', { login: 'test' })
        expect(User.fromMap).toHaveBeenCalledWith(userData)
        expect(result).toEqual({ user: userData, token })
    })

    it('apiRegister регистрирует и возвращает user и token', async () => {
        const token = 'tok'
        const userData = { id: 1 }
        const registrationDto = { toRepresentation: () => ({ reg: 'data' }) }
        mockNoAuthPost.mockResolvedValue({ data: { token, user: userData } })
        User.fromMap.mockReturnValue(userData)

        const result = await api.apiRegister(registrationDto)

        expect(mockNoAuthPost).toHaveBeenCalledWith('/auth/register', { reg: 'data' })
        expect(User.fromMap).toHaveBeenCalledWith(userData)
        expect(result).toEqual({ user: userData, token })
    })

    it('apiFetchSpecialtyList возвращает список специальностей', async () => {
        const data = ['spec1', 'spec2']
        mockInstanceGet.mockResolvedValue({ data })

        const result = await api.apiFetchSpecialtyList()

        expect(mockInstanceGet).toHaveBeenCalledWith('/specialty/list')
        expect(result).toEqual(data)
    })

    it('apiFetchSpecialists делает POST с фильтром и возвращает данные', async () => {
        const data = ['spec1', 'spec2']
        const filterDto = { some: 'filter' }
        mockInstancePost.mockResolvedValue({ data })

        const result = await api.apiFetchSpecialists(filterDto)

        expect(mockInstancePost).toHaveBeenCalledWith('/specialist/find', filterDto)
        expect(result).toEqual(data)
    })

    it('apiFetchSpecialist возвращает специалиста по id', async () => {
        const data = { id: 123 }
        mockInstanceGet.mockResolvedValue({ data })

        const result = await api.apiFetchSpecialist(123)

        expect(mockInstanceGet).toHaveBeenCalledWith('/specialist/123')
        expect(result).toEqual(data)
    })

    it('apiFetchAppointments возвращает все записи', async () => {
        const data = [{ id: 1 }]
        mockInstanceGet.mockResolvedValue({ data })

        const result = await api.apiFetchAppointments()

        expect(mockInstanceGet).toHaveBeenCalledWith('/appointment/all')
        expect(result).toEqual(data)
    })

    it('apiCreateAppointment создаёт запись', async () => {
        const createAppointmentDto = { toRepresentation: () => ({ some: 'data' }) }
        mockInstancePost.mockResolvedValue({})

        await api.apiCreateAppointment(createAppointmentDto)

        expect(mockInstancePost).toHaveBeenCalledWith('/appointment/new', { some: 'data' })
    })

    it('apiApproveAppointment подтверждает запись по id', async () => {
        mockInstancePost.mockResolvedValue({})

        await api.apiApproveAppointment(1)

        expect(mockInstancePost).toHaveBeenCalledWith('/appointment/approve/1')
    })

    it('apiFetchClientReviews возвращает отзывы клиента', async () => {
        const data = [{ id: 1 }]
        mockInstanceGet.mockResolvedValue({ data })

        const result = await api.apiFetchClientReviews()

        expect(mockInstanceGet).toHaveBeenCalledWith('/review/by-me')
        expect(result).toEqual(data)
    })

    it('apiCreateReview создаёт отзыв', async () => {
        const createReviewDto = { text: 'nice' }
        mockInstancePost.mockResolvedValue({})

        await api.apiCreateReview(10, createReviewDto)

        expect(mockInstancePost).toHaveBeenCalledWith('/review/new/10', createReviewDto)
    })

    it('apiDeleteProfile удаляет профиль', async () => {
        mockInstancePost.mockResolvedValue({})

        await api.apiDeleteProfile()

        expect(mockInstancePost).toHaveBeenCalledWith('/profile/delete')
    })

    it('apiUpdateSpecialties обновляет специальности', async () => {
        const specialties = ['spec1']
        mockInstancePost.mockResolvedValue({})

        await api.apiUpdateSpecialties(specialties)

        expect(mockInstancePost).toHaveBeenCalledWith('/profile/update/specialty', specialties)
    })

    it('apiEditSpecialtiesList редактирует список специальностей и возвращает данные', async () => {
        const data = { success: true }
        mockInstancePost.mockResolvedValue({ data })

        const result = await api.apiEditSpecialtiesList('specNew')

        expect(mockInstancePost).toHaveBeenCalledWith('/specialty/edit', { newSpecialty: 'specNew' })
        expect(result).toEqual(data)
    })

    it('apiEditProfileInfo редактирует профиль и возвращает данные', async () => {
        const data = { success: true }
        mockInstancePost.mockResolvedValue({ data })

        const result = await api.apiEditProfileInfo('bio text', 100)

        expect(mockInstancePost).toHaveBeenCalledWith('/profile/update/info', {
            specialistBio: 'bio text',
            specialistPrice: 100,
        })
        expect(result).toEqual(data)
    })
})
