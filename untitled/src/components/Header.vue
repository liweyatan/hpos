<template>
  <header class="hospital-header">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <router-link class="navbar-brand" to="/">
          <i class="bi bi-hospital me-2"></i>
          智慧医院挂号系统
        </router-link>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navMenu">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link class="nav-link" to="/register" :class="{ active: $route.path === '/register' }">
                <i class="bi bi-plus-circle me-1"></i>在线挂号
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/orders" :class="{ active: $route.path === '/orders' }">
                <i class="bi bi-list-ul me-1"></i>我的挂号
              </router-link>
            </li>
          </ul>

          <ul class="navbar-nav" v-if="isLoggedIn">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                <i class="bi bi-person-circle me-1"></i>{{ username }}
              </a>
              <ul class="dropdown-menu dropdown-menu-end">
                <li><a class="dropdown-item" href="#" @click.prevent="logout">退出登录</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
export default {
  name: 'Header',
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('token')
    },
    username() {
      return localStorage.getItem('username') || '用户'
    },
  },
  methods: {
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('phone')
      this.$router.push('/login')
    },
  },
}
</script>

<style scoped>
.hospital-header { box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
</style>
