import { createRouter, createWebHashHistory } from 'vue-router'
import { getAuthState } from '@/api/auth.js'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import RegisterUser from '@/views/RegisterUser.vue'
import Orders from '@/views/Orders.vue'
import DepartmentList from '@/views/DepartmentList.vue'
import Admin from '@/views/Admin.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/home', redirect: '/' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register-user', name: 'RegisterUser', component: RegisterUser },
  { path: '/register', name: 'Register', component: Register },
  { path: '/orders', name: 'Orders', component: Orders },
  { path: '/departments', name: 'DepartmentList', component: DepartmentList },
  { path: '/admin', name: 'Admin', component: Admin, meta: { requiresAdmin: true } },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const auth = getAuthState()
  const publicPages = ['Home', 'Login', 'RegisterUser', 'DepartmentList']

  if (publicPages.includes(to.name)) {
    next()
  } else if (!auth.loggedIn) {
    alert('请先登录系统')
    next('/login')
  } else if (to.meta.requiresAdmin && auth.role !== 'ADMIN') {
    alert('权限不足，只有管理员可以访问此页面')
    next('/')
  } else {
    next()
  }
})

export default router
