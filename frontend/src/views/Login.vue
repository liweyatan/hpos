<template>
  <div class="login-page">
    <div class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4">
          <div class="card shadow">
            <div class="card-header bg-primary text-white text-center">
              <h4 class="mb-0">
                <i class="fas fa-user-circle me-2"></i>用户登录
              </h4>
            </div>
            <div class="card-body p-4">
              <form @submit.prevent="handleLogin">
                <div class="mb-3">
                  <label class="form-label">用户名/手机号</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                    <input type="text" class="form-control" v-model="username" placeholder="请输入用户名或手机号" required>
                  </div>
                </div>
                <div class="mb-3">
                  <label class="form-label">密码</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                    <input type="password" class="form-control" v-model="password" placeholder="请输入密码" required>
                  </div>
                </div>
                <div class="mb-3 form-check">
                  <input type="checkbox" class="form-check-input" id="rememberMe" v-model="rememberMe">
                  <label class="form-check-label" for="rememberMe">记住我</label>
                  <a href="#" class="float-end text-decoration-none">忘记密码？</a>
                </div>
                <div v-if="errorMsg" class="alert alert-danger py-2 small">{{ errorMsg }}</div>
                <div class="d-grid gap-2">
                  <button type="submit" class="btn btn-primary btn-lg" :disabled="loading">
                    <i v-if="loading" class="fas fa-spinner fa-spin me-2"></i>
                    <i v-else class="fas fa-sign-in-alt me-2"></i>{{ loading ? '登录中...' : '登录' }}
                  </button>
                </div>
              </form>
              <hr class="my-4">
              <div class="text-center">
                <p class="text-muted mb-0">还没有账号？</p>
                <a href="/register" class="btn btn-outline-primary btn-sm mt-2" @click.prevent="$router.push('/register')">
                  <i class="fas fa-user-plus me-1"></i>立即注册
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <footer class="login-footer bg-dark text-white py-4">
      <div class="container text-center">
        <p class="mb-0"><i class="fas fa-copyright me-1"></i>2026 <strong>智慧医院管理系统</strong> 版权所有</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { login, findPatientByPhone } from '@/api/index.js'
import { setLogin } from '@/api/auth.js'

export default {
  name: 'LoginPage',
  data() {
    return { username: '', password: '', rememberMe: false, loading: false, errorMsg: '' }
  },
  methods: {
    async handleLogin() {
      this.errorMsg = ''
      this.loading = true
      try {
        const res = await login(this.username, this.password)
        const user = res.user
        setLogin(user)
        if (user.phone) {
          try {
            const patient = await findPatientByPhone(user.phone)
            if (patient && patient.id) {
              localStorage.setItem('patientId', String(patient.id))
              localStorage.setItem('patientName', patient.name || '')
            }
          } catch (e) { /* ignore */ }
        }
        const displayName = user.realName || user.username
        const roleText = user.role === 'ADMIN' ? '管理员' : '用户'
        alert(`登录成功！欢迎 ${displayName}（${roleText}）`)
        if (user.role === 'ADMIN') {
          this.$router.push('/admin')
        } else {
          this.$router.push('/')
        }
      } catch (e) {
        this.errorMsg = e.message || '用户名或密码错误！'
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.login-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.login-page .container {
  flex: 1;
}
.login-footer {
  margin-top: auto;
}
</style>
