<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-8 col-lg-6">
        <div class="card shadow">
          <div class="card-header bg-primary text-white text-center">
            <h4 class="mb-0"><i class="fas fa-user-plus me-2"></i>用户注册</h4>
          </div>
          <div class="card-body p-4">
            <form @submit.prevent="handleRegister">
              <div class="mb-3">
                <label class="form-label">用户名 <span class="text-danger">*</span></label>
                <div class="input-group">
                  <span class="input-group-text"><i class="fas fa-user"></i></span>
                  <input type="text" class="form-control" v-model="form.username" placeholder="请输入用户名" required>
                </div>
                <div class="form-text">用户名长度2-20位，只能包含字母、数字和下划线</div>
              </div>
              <div class="mb-3">
                <label class="form-label">手机号 <span class="text-danger">*</span></label>
                <div class="input-group">
                  <span class="input-group-text"><i class="fas fa-phone"></i></span>
                  <input type="tel" class="form-control" v-model="form.phone" placeholder="请输入手机号" required>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">密码 <span class="text-danger">*</span></label>
                <div class="input-group">
                  <span class="input-group-text"><i class="fas fa-lock"></i></span>
                  <input type="password" class="form-control" v-model="form.password" placeholder="请输入密码" required>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">确认密码 <span class="text-danger">*</span></label>
                <div class="input-group">
                  <span class="input-group-text"><i class="fas fa-lock"></i></span>
                  <input type="password" class="form-control" v-model="form.confirmPassword" placeholder="请再次输入密码" required>
                </div>
              </div>
              <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="agreeTerms" v-model="form.agreeTerms" required>
                <label class="form-check-label" for="agreeTerms">我已阅读并同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a></label>
              </div>
              <div v-if="errorMsg" class="alert alert-danger py-2 small">{{ errorMsg }}</div>
              <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary btn-lg" :disabled="loading">
                  <i v-if="loading" class="fas fa-spinner fa-spin me-2"></i>
                  <i v-else class="fas fa-user-plus me-2"></i>{{ loading ? '注册中...' : '立即注册' }}
                </button>
                <a href="/login" class="btn btn-outline-secondary" @click.prevent="$router.push('/login')">
                  <i class="fas fa-sign-in-alt me-2"></i>已有账号？立即登录
                </a>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { register } from '@/api/index.js'

export default {
  name: 'RegisterUser',
  data() {
    return {
      form: { username: '', phone: '', password: '', confirmPassword: '', agreeTerms: false },
      loading: false,
      errorMsg: ''
    }
  },
  methods: {
    validateForm() {
      if (!/^[a-zA-Z0-9_]{2,20}$/.test(this.form.username)) { this.errorMsg = '用户名格式错误：长度2-20位，只能包含字母、数字和下划线'; return false }
      if (this.form.password.length < 6) { this.errorMsg = '密码长度不能少于6位'; return false }
      if (this.form.password !== this.form.confirmPassword) { this.errorMsg = '两次输入的密码不一致'; return false }
      if (!/^1[3-9]\d{9}$/.test(this.form.phone)) { this.errorMsg = '请输入正确的手机号格式'; return false }
      return true
    },
    async handleRegister() {
      this.errorMsg = ''
      if (!this.validateForm()) return
      this.loading = true
      try {
        await register({ username: this.form.username, password: this.form.password, confirmPassword: this.form.confirmPassword, phone: this.form.phone })
        alert('注册成功！即将跳转到登录页面')
        this.$router.push('/login')
      } catch (e) { this.errorMsg = e.message || '注册失败，请重试' }
      finally { this.loading = false }
    }
  }
}
</script>
