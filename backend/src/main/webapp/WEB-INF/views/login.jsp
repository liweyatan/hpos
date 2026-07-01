<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户登录 - ${empty systemName ? '智慧医院管理系统' : systemName}</title>

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

<!-- 登录表单 -->
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
                        <form id="loginForm">
                            <div class="mb-3">
                                <label class="form-label">用户名/手机号</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-user"></i>
                                    </span>
                                    <input type="text" class="form-control" name="username"
                                           placeholder="请输入用户名或手机号" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">密码</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-lock"></i>
                                    </span>
                                    <input type="password" class="form-control" name="password" placeholder="请输入密码"
                                           required>
                                </div>
                            </div>

                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                                <label class="form-check-label" for="rememberMe">记住我</label>
                                <a href="#" class="float-end text-decoration-none">忘记密码？</a>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-sign-in-alt me-2"></i>登录
                                </button>
                            </div>
                        </form>

                        <hr class="my-4">

                        <div class="text-center">
                            <p class="text-muted mb-0">还没有账号？</p>
                            <a href="/register" class="btn btn-outline-primary btn-sm mt-2">
                                <i class="fas fa-user-plus me-1"></i>立即注册
                            </a>
                        </div>
                    </div>
            </div>

            <!-- 测试账户提示 -->
            <div class="alert alert-info mt-3">
                <h6 class="alert-heading">测试账户：</h6>
                <p class="mb-1">用户名: <strong>testuser</strong></p>
                <p class="mb-0">密码: <strong>123456</strong></p>
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

<!-- 登录处理 -->
<script>
    document.getElementById('loginForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const formData = new FormData(this);
        const username = formData.get('username');
        const password = formData.get('password');
        const rememberMe = formData.get('rememberMe');

        // 禁用提交按钮防止重复提交
        const submitBtn = this.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>登录中...';
        submitBtn.disabled = true;

        try {
            // 调用后端登录API
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username, password})
            });

            const result = await response.json();

                if (result.success) {
                // 统一用户信息存储格式
                const userInfo = {
                    id: result.user.id,
                    username: result.user.username,
                    realName: result.user.realName || result.user.username,
                    phone: result.user.phone,
                    email: result.user.email,
                    role: result.user.role
                };

                // 使用统一的认证管理器存储用户信息
                if (typeof AuthManager !== 'undefined') {
                    AuthManager.setUser(userInfo, rememberMe);
                } else {
                    // 备用方案：直接存储
                    if (rememberMe) {
                        localStorage.setItem('hospital_user', JSON.stringify(userInfo));
                    } else {
                        sessionStorage.setItem('hospital_user', JSON.stringify(userInfo));
                    }
                }

                // 显示个性化的登录成功消息
                const displayName = userInfo.realName || userInfo.username;
                const roleText = userInfo.role === 'ADMIN' ? '管理员' : '用户';
                alert(`登录成功！欢迎 ${displayName}（${roleText}）`);

                // 根据角色跳转到不同页面
                if (userInfo.role === 'ADMIN') {
                    window.location.href = '/admin';
                } else {
                    window.location.href = '/';
                }
            } else {
                alert(result.message || '用户名或密码错误！');
            }
        } catch (error) {
            console.error('登录失败:', error);
            alert('网络错误，请检查网络连接后重试');
        } finally {
            // 恢复按钮状态
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }
    });

    // 页面加载时检查是否已登录 - 仅显示提示，不自动跳转
    document.addEventListener('DOMContentLoaded', function () {
        // 使用auth.js的认证管理器来检查登录状态
        if (typeof AuthManager !== 'undefined' && AuthManager.isLoggedIn()) {
            const user = AuthManager.getUser();
            const displayName = user.realName || user.username || '用户';
            
            // 显示提示信息，但不自动跳转，让用户决定
            console.log(`用户 ${displayName} 已登录，当前在登录页面`);
            
            // 可选：显示一个非阻塞的提示
            setTimeout(() => {
                if (confirm(`您已登录为 ${displayName}，是否返回首页？`)) {
                    window.location.href = '/';
                }
            }, 500);
        }
    });
</script>
</body>
</html>