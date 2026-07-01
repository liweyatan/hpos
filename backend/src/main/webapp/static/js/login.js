/**
 * 登录页面JavaScript
 */

// 登录页面Vue应用
const LoginApp = {
    data() {
        return {
            username: '',
            password: '',
            loading: false,
            errorMessage: '',
            successMessage: '',
            showErrorMessage: false,
            showSuccessMessage: false
        }
    },

    methods: {
        /**
         * 处理登录表单提交
         */
        async handleLogin() {
            if (!this.username || !this.password) {
                this.showError('请输入用户名和密码');
                return;
            }

            this.loading = true;

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: this.username,
                        password: this.password
                    })
                });

                const result = await response.json();

                if (result.success) {
                    // 登录成功，保存用户信息到localStorage
                    localStorage.setItem('hospital_user', JSON.stringify(result.user));

                    // 显示个性化的登录成功消息
                    const displayName = result.user.name || result.user.username;
                    const roleText = result.user.role === 'ADMIN' ? '管理员' : '用户';
                    this.showSuccess(`登录成功！欢迎 ${displayName}（${roleText}），正在跳转...`);

                    // 2秒后跳转到对应页面
                    setTimeout(() => {
                        if (result.user.role === 'ADMIN') {
                            window.location.href = '/admin';
                        } else {
                            window.location.href = '/';
                        }
                    }, 2000);
                } else {
                    this.showError(result.message || '登录失败');
                }

            } catch (error) {
                console.error('登录失败:', error);
                this.showError('网络错误，请检查网络连接');
            } finally {
                this.loading = false;
            }
        },

        /**
         * 显示错误消息
         */
        showError(message) {
            if (window.HospitalUtils && window.HospitalUtils.Message) {
                window.HospitalUtils.Message.showError(this, message);
            } else {
                // 备用方案
                this.errorMessage = message;
                this.successMessage = '';
                this.showErrorMessage = true;
                this.showSuccessMessage = false;
            }
        },

        /**
         * 显示成功消息
         */
        showSuccess(message) {
            if (window.HospitalUtils && window.HospitalUtils.Message) {
                window.HospitalUtils.Message.showSuccess(this, message);
            } else {
                // 备用方案
                this.successMessage = message;
                this.errorMessage = '';
                this.showSuccessMessage = true;
                this.showErrorMessage = false;
            }
        },

        /**
         * 隐藏消息
         */
        hideMessages() {
            if (window.HospitalUtils && window.HospitalUtils.Message) {
                window.HospitalUtils.Message.hideMessages(this);
            } else {
                // 备用方案
                this.showErrorMessage = false;
                this.showSuccessMessage = false;
                this.errorMessage = '';
                this.successMessage = '';
            }
        }
    }
};

// 页面加载完成后初始化Vue应用
document.addEventListener('DOMContentLoaded', function () {
    if (typeof Vue !== 'undefined') {
        const app = Vue.createApp(LoginApp);
        app.mount('#login-app');
        console.log('登录页面Vue应用挂载成功');
    } else {
        console.error('Vue.js 未加载，请检查网络连接');
    }
});