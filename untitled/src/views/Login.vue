<template>
  <div class="login-page">
    <div class="container">
      <div class="row justify-content-center min-vh-100 align-items-center">
        <div class="col-md-5 col-lg-4">
          <div class="card shadow-lg border-0 rounded-4">
            <div class="card-body p-4">
              <div class="text-center mb-4">
                <i class="bi bi-hospital fs-1 text-primary"></i>
                <h4 class="mt-2">智慧医院挂号系统</h4>
                <p class="text-muted small">请登录后使用挂号服务</p>
              </div>

              <form @submit.prevent="handleLogin">
                <div class="mb-3">
                  <label class="form-label">用户名</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-person"></i></span>
                    <input type="text" class="form-control" v-model="username" placeholder="请输入用户名" required>
                  </div>
                </div>

                <div class="mb-3">
                  <label class="form-label">密码</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-lock"></i></span>
                    <input :type="showPwd ? 'text' : 'password'" class="form-control" v-model="password"
                      placeholder="请输入密码" required>
                    <button class="btn btn-outline-secondary" type="button" @click="showPwd = !showPwd">
                      <i :class="showPwd ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                    </button>
                  </div>
                </div>

                <div v-if="errorMsg" class="alert alert-danger py-2 small">{{ errorMsg }}</div>

                <button type="submit" class="btn btn-primary w-100 btn-lg" :disabled="loading">
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                  {{ loading ? '登录中...' : '登 录' }}
                </button>

                <div class="text-center mt-3">
                  <small class="text-muted">测试账号: admin / 123456</small>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { login, findPatientByPhone } from '@/api/index.js'

export default {
  name: 'LoginPage',
  data() {
    return {
      username: '',
      password: '',
      showPwd: false,
      loading: false,
      errorMsg: '',
    }
  },
  methods: {
    async handleLogin() {
      this.errorMsg = ''
      this.loading = true
      try {
        const res = await login(this.username, this.password)
        localStorage.setItem('token', res.token)
        localStorage.setItem('userId', String(res.userId))
        localStorage.setItem('username', res.username)
        localStorage.setItem('phone', res.phone || '')

        // 根据手机号查找患者ID
        if (res.phone) {
          try {
            const patient = await findPatientByPhone(res.phone)
            if (patient) {
              localStorage.setItem('patientId', String(patient.id))
              localStorage.setItem('patientName', patient.realName || '')
            }
          } catch (e) {
            // 患者不存在也没关系
          }
        }

        this.$router.push('/register')
      } catch (e) {
        this.errorMsg = e.message || '登录失败，请检查用户名密码'
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.login-page {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}
.card {
  backdrop-filter: blur(10px);
}
</style>
