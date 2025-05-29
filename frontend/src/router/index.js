import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import RegistrationView from '../views/RegistrationView.vue'
import LoginView from '../views/LoginView.vue'
import ProfileView from '@/views/ProfileView.vue'
import LogoutView from '@/views/LogoutView.vue'
import { useUserStore } from '@/stores/userStore'
import SpecialistSearchView from '@/views/SpecialistSearchView.vue'
import SpecialistView from '@/views/SpecialistView.vue'
import AppointmentListView from '@/views/AppointmentListView.vue'
import ReviewsView from '@/views/ReviewsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'About',

      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/registration',
      name: 'Registration',
      component: RegistrationView,
      meta: {
        requiresGuest: true
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
      meta: {
        requiresGuest: true
      }
    },
    {
      path: '/logout',
      name: 'Logout',
      component: LogoutView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/profile',
      name: 'Profile',
      component: ProfileView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/search',
      name: 'SpecialistSearch',
      component: SpecialistSearchView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/specialist/:id',
      name: 'Specialist',
      component: SpecialistView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/appointments',
      name: 'AppointmentList',
      component: AppointmentListView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/my-reviews',
      name: 'Reviews',
      component: ReviewsView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/moderation/accounts',
      name: 'ModerationPage',
      component: () => import('@/views/ModerationView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
})

router.beforeEach((to) => {
  const logged = useUserStore().isAuthenticated
  const authRequired = to.meta['requiresAuth'] || false
  const guestRequired = to.meta['requiresGuest'] || false

  if (!logged && authRequired) {
    return { name: 'Login' }
  }
  if (logged && guestRequired) {
    return { name: 'Home' }
  }
})

export default router
