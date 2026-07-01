<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 公共导航栏模板 -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="/">
            <i class="fas fa-hospital me-2"></i>
            <strong>${empty systemName ? '智慧医院管理系统' : systemName}</strong>
        </a>

        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="/">
                <i class="fas fa-home me-1"></i>首页
            </a>
            <a class="nav-link" href="/appointments">
                <i class="fas fa-list-alt me-1"></i>我的预约
            </a>

            <!-- 未登录时显示登录按钮 -->
            <div id="loginButton" style="display: block;">
                <a class="nav-link" href="/login">
                    <i class="fas fa-sign-in-alt me-1"></i>登录
                </a>
            </div>

            <!-- 登录后显示用户下拉菜单 -->
            <div class="nav-item dropdown" id="userDropdownContainer" style="display: none;">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" id="userDropdown">
                    <i class="fas fa-user me-1"></i>
                    <span id="userNameDisplay">用户</span>
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                        <span class="dropdown-item-text small" id="userInfoDisplay">
                            <i class="fas fa-user me-1"></i>用户信息
                        </span>
                    </li>
                    <li>
                        <hr class="dropdown-divider">
                    </li>
                    <li><a class="dropdown-item admin-item" href="#" id="adminLink" onclick="checkAdminPermission()">
                        <i class="fas fa-cog me-1"></i>管理员后台
                    </a></li>
            <li><a class="dropdown-item logout-item" href="#" id="logoutLink" onclick="logout()">
                <i class="fas fa-sign-out-alt me-1"></i>退出登录
            </a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>