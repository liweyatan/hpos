<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户注册 - ${empty systemName ? '智慧医院管理系统' : systemName}</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- 自定义样式 -->
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
</head>
<body>
<!-- 公共导航栏 -->
<%@ include file="./common/header.jsp" %>

<!-- 注册表单 -->
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-header bg-primary text-white text-center">
                    <h4 class="mb-0">
                        <i class="fas fa-user-plus me-2"></i>用户注册
                    </h4>
                </div>
                <div class="card-body p-4">
                    <form id="registerForm">
                        <div class="mb-3">
                            <label class="form-label">用户名 <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-user"></i>
                                </span>
                                <input type="text" class="form-control" name="username"
                                       placeholder="请输入用户名" required>
                            </div>
                            <div class="form-text">用户名长度2-20位，只能包含字母、数字和下划线</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">手机号 <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-phone"></i>
                                </span>
                                <input type="tel" class="form-control" name="phone"
                                       placeholder="请输入手机号" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">密码 <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-lock"></i>
                                </span>
                                <input type="password" class="form-control" name="password"
                                       placeholder="请输入密码" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">确认密码 <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-lock"></i>
                                </span>
                                <input type="password" class="form-control" name="confirmPassword"
                                       placeholder="请再次输入密码" required>
                            </div>
                        </div>

                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="agreeTerms" required>
                            <label class="form-check-label" for="agreeTerms">
                                我已阅读并同意 <a href="#" class="text-decoration-none">用户协议</a> 和 <a href="#"
                                                                                                           class="text-decoration-none">隐私政策</a>
                            </label>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-user-plus me-2"></i>立即注册
                            </button>
                            <a href="/login" class="btn btn-outline-secondary">
                                <i class="fas fa-sign-in-alt me-2"></i>已有账号？立即登录
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 页脚 -->
<footer class="bg-dark text-white py-4 mt-5">
    <div class="container text-center">
        <p class="mb-0">
            <i class="fas fa-copyright me-1"></i>2025
            <strong>${empty systemName ? '智慧医院管理系统' : systemName}</strong>
            版权所有
        </p>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- 通用认证状态管理 -->
<script src="${pageContext.request.contextPath}/static/js/auth.js"></script>
<!-- 管理员权限检查 -->
<script src="${pageContext.request.contextPath}/static/js/admin-check.js"></script>

<!-- 注册处理 -->
<script>
    document.getElementById('registerForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const formData = new FormData(this);
        const data = {
            username: formData.get('username').trim(),
            password: formData.get('password'),
            confirmPassword: formData.get('confirmPassword'),
            phone: formData.get('phone').trim()
        };

        // 客户端验证
        if (!validateForm(data)) {
            return;
        }

        const submitBtn = this.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>注册中...';
        submitBtn.disabled = true;

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            const result = await response.json();

            if (result.success) {
                alert('注册成功！即将跳转到登录页面');
                window.location.href = '/login';
            } else {
                alert(result.message || '注册失败，请重试');
            }
        } catch (error) {
            alert('网络错误，请检查网络连接后重试');
            console.error('注册失败:', error);
        } finally {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }
    });

    // 表单验证函数
    function validateForm(data) {
        // 用户名验证
        if (!/^[a-zA-Z0-9_]{2,20}$/.test(data.username)) {
            alert('用户名格式错误：长度2-20位，只能包含字母、数字和下划线');
            return false;
        }

        // 密码验证
        if (data.password.length < 6) {
            alert('密码长度不能少于6位');
            return false;
        }

        // 密码确认
        if (data.password !== data.confirmPassword) {
            alert('两次输入的密码不一致');
            return false;
        }

        // 手机号验证
        if (!/^1[3-9]\d{9}$/.test(data.phone)) {
            alert('请输入正确的手机号格式');
            return false;
        }

        // 邮箱验证（可选）
        if (data.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
            alert('请输入正确的邮箱格式');
            return false;
        }

        return true;
    }

    // 实时验证
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('blur', function () {
            const value = this.value.trim();
            const name = this.name;

            if (name === 'username' && value) {
                if (!/^[a-zA-Z0-9_]{2,20}$/.test(value)) {
                    this.classList.add('is-invalid');
                } else {
                    this.classList.remove('is-invalid');
                }
            }

            if (name === 'phone' && value) {
                if (!/^1[3-9]\d{9}$/.test(value)) {
                    this.classList.add('is-invalid');
                } else {
                    this.classList.remove('is-invalid');
                }
            }

            if (name === 'email' && value) {
                if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                    this.classList.add('is-invalid');
                } else {
                    this.classList.remove('is-invalid');
                }
            }
        });
    });

    // 页面加载时检查是否已登录
    document.addEventListener('DOMContentLoaded', function () {
        // 使用统一的认证管理器检查登录状态
        if (typeof AuthManager !== 'undefined' && AuthManager.isLoggedIn()) {
            const user = AuthManager.getUser();
            const displayName = user.realName || user.username || '用户';
            if (confirm(`您已登录为 ${displayName}，是否返回首页？`)) {
                window.location.href = '/';
            }
        }
    });
</script>
</body>
</html>