<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <a class="navbar-brand" href="/" @click.prevent="$router.push('/')">
        <i class="fas fa-hospital me-2"></i>
        <strong>智慧医院管理系统</strong>
      </a>

      <div class="navbar-nav ms-auto">
        <a class="nav-link" href="/" @click.prevent="$router.push('/')">
          <i class="fas fa-home me-1"></i>首页
        </a>
        <a class="nav-link" href="/orders" @click.prevent="$router.push('/orders')">
          <i class="fas fa-list-alt me-1"></i>我的预约
        </a>

        <div v-if="!auth.loggedIn">
          <a class="nav-link" href="/login" @click.prevent="$router.push('/login')">
            <i class="fas fa-sign-in-alt me-1"></i>登录
          </a>
        </div>

        <div v-if="auth.loggedIn" class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
            <i class="fas fa-user me-1"></i>
            <span>{{ displayName }}</span>
          </a>
          <ul class="dropdown-menu dropdown-menu-end">
            <li>
              <span class="dropdown-item-text small">
                <i class="fas fa-user me-1"></i>{{ displayName }} ({{ roleText }}) <span v-if="isAdmin" class="admin-badge">管理员</span>
              </span>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item admin-item" href="#" @click.prevent="goAdmin"><i class="fas fa-cog me-1"></i>管理员后台</a></li>
            <li><a class="dropdown-item logout-item" href="#" @click.prevent="handleLogout"><i class="fas fa-sign-out-alt me-1"></i>退出登录</a></li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import { getAuthState, setLogout } from '@/api/auth.js'

export default {
  name: 'AppHeader',
  computed: {
    auth() { return getAuthState() },
    displayName() { return this.auth.realName || this.auth.username || '用户' },
    roleText() {
      return { ADMIN: '管理员', DOCTOR: '医生', PATIENT: '患者', USER: '用户' }[this.auth.role] || '用户'
    },
    isAdmin() { return this.auth.role === 'ADMIN' },
  },
  methods: {
    goAdmin() {
      if (!this.isAdmin) {
        const roleText = { ADMIN: '管理员', DOCTOR: '医生', PATIENT: '患者', USER: '用户' }[this.auth.role] || '用户'
        alert(`权限不足！您只是${roleText}，想什么呢？`)
        return
      }
      this.$router.push('/admin')
    },
    handleLogout() {
      if (confirm('确定要退出登录吗？')) {
        setLogout()
        this.$router.push('/')
      }
    },
  },
}
</script>
