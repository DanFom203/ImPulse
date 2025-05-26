import axios from 'axios'
import {API_URL} from './consts.js'
import {useUserStore} from '../stores/userStore.js'
import {User} from '@/models/base.js'

const instance = axios.create({
  baseURL: API_URL
})

const noAuthInstance = axios.create({
  baseURL: API_URL
})

instance.interceptors.request.use(
  function (config) {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  function (error) {
    return Promise.reject(error)
  }
)

export async function apiGetAllUsers() {
  const response = await instance.get('/moderation/accounts').catch(defaultApiExceptionHandler)

  return response.data
}

export async function apiUpdateUserAuthority(email, authority) {
  await instance.post(`/moderation/authority`, { email, authority }).catch(defaultApiExceptionHandler)
}

export async function apiDeleteUser(email) {
  await instance.post(`/moderation/delete`, { email }).catch(defaultApiExceptionHandler)
}

export async function apiUploadProfilePhoto(formData) {
  const response = await fetch('/api/users/profile/photo', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`
    },
    body: formData
  })

  if (!response.ok) {
    throw new Error('Не удалось загрузить фото профиля')
  }

  return await response.json()
}

export async function apiLogin(loginDto) {
  const response = await noAuthInstance
    .post('/auth/login', loginDto.toRepresentation())
    .catch(defaultApiExceptionHandler)

  let token = response.data['token']
  let user = User.fromMap(response.data['user'])
  return { user, token }
}

export async function apiRegister(registrationDto) {
  const response = await noAuthInstance
    .post('/auth/register', registrationDto.toRepresentation())
    .catch(defaultApiExceptionHandler)

  let token = response.data['token']
  let user = User.fromMap(response.data['user'])
  return { user, token }
}

export async function apiFetchSpecialtyList() {
  const response = await instance.get('/specialty').catch(defaultApiExceptionHandler)

  return response.data
}

export async function apiFetchSpecialists(searchFilterDto) {
  const response = await instance
    .post('/specialist/find', searchFilterDto)
    .catch(defaultApiExceptionHandler)
  return response.data
}

export async function apiFetchSpecialist(id) {
  const response = await instance.get(`/specialist/${id}`).catch(defaultApiExceptionHandler)
  return response.data
}

export async function apiFetchAppointments() {
  const response = await instance.get('/appointment/all').catch(defaultApiExceptionHandler)
  return response.data
}

export async function apiCreateAppointment(createAppointmentDto) {
  await instance
    .post('/appointment/new', createAppointmentDto.toRepresentation())
    .catch(defaultApiExceptionHandler)
}

export async function apiApproveAppointment(appointmentId) {
  await instance.post(`/appointment/approve/${appointmentId}`).catch(defaultApiExceptionHandler)
}

export async function apiFetchClientReviews() {
  const response = await instance.get('/review/by-me').catch(defaultApiExceptionHandler)
  return response.data
}

export async function apiCreateReview(specialistId, createReviewDto) {
  await instance
    .post(`/review/new/${specialistId}`, createReviewDto)
    .catch(defaultApiExceptionHandler)
}

export async function apiDeleteProfile() {
  await instance.post('/profile/edit/delete').catch(defaultApiExceptionHandler)
}

export async function apiUpdateSpecialties(specialties) {
  await instance.post('/profile/update/specialty', specialties).catch(defaultApiExceptionHandler)
}

export async function apiEditSpecialtiesList(specialty) {
  return instance.post('/profile/edit/specialty', { newSpecialty: specialty})
}

function defaultApiExceptionHandler(error) {
  if (error.response) {
    console.error(error.response)

    if (error.response.status === 401) {
      let err = new Error('Error with authentication, please retry logging in')
      err.statusCode = error.response.status
      throw err
    }

    let err = new Error(error.response.data.detail || 'Unknown error')
    err.statusCode = error.response.status
    throw err
  } else {
    console.error('Unknown error: ', error.message)
    throw new Error('Unknown error, please try again')
  }
}
