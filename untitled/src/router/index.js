import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Orders from '@/views/Orders.vue'

const routes = [
  { path: '/', redirect: '/register' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/orders', name: 'Orders', component: Orders },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.name === 'Login' && token) {
    next('/register')
  } else if (to.name !== 'Login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
