// 通用认证状态管理 - 智慧医院管理系统

// 用户信息存储和获取
const AuthManager = {
    // 存储用户信息
    setUser: function (user, rememberMe = false) {
        if (user && typeof user === 'object') {
            // 根据是否记住我选择存储方式
            if (rememberMe) {
                localStorage.setItem('hospital_user', JSON.stringify(user));
                sessionStorage.removeItem('hospital_user');
            } else {
                sessionStorage.setItem('hospital_user', JSON.stringify(user));
                localStorage.removeItem('hospital_user');
            }
            this.updateUI();
            console.log('用户信息已保存:', user, '记住我:', rememberMe);
        }
    },

    // 获取用户信息
    getUser: function () {
        const userStr = localStorage.getItem('hospital_user') || sessionStorage.getItem('hospital_user');
        if (userStr) {
            try {
                return JSON.parse(userStr);
            } catch (e) {
                console.error('解析用户信息失败:', e);
                // 清除无效的用户信息
                localStorage.removeItem('hospital_user');
                sessionStorage.removeItem('hospital_user');
                return null;
            }
        }
        return null;
    },

    // 清除用户信息
    clearUser: function () {
        localStorage.removeItem('hospital_user');
        sessionStorage.removeItem('hospital_user');
        this.updateUI();
        console.log('用户信息已清除');
    },

    // 检查是否已登录
    isLoggedIn: function () {
        return this.getUser() !== null;
    },

    // 检查是否为管理员
    isAdmin: function () {
        const user = this.getUser();
        return user && user.role === 'ADMIN';
    },

    // 更新UI显示
    updateUI: function () {
        const user = this.getUser();
        const loginButton = document.getElementById('loginButton');
        const userDropdownContainer = document.getElementById('userDropdownContainer');
        const userNameDisplay = document.getElementById('userNameDisplay');
        const userInfoDisplay = document.getElementById('userInfoDisplay');
        const adminLink = document.getElementById('adminLink');

        if (user) {
            // 用户已登录状态 - 隐藏登录按钮，显示下拉菜单
            if (loginButton) loginButton.style.display = 'none';
            if (userDropdownContainer) userDropdownContainer.style.display = 'block';

            // 更新用户信息显示
            if (userNameDisplay) {
                userNameDisplay.textContent = user.realName || user.username || '用户';
            }

            if (userInfoDisplay) {
                const roleText = this.getRoleText(user.role);
                const isAdmin = this.isAdmin();
                const adminBadge = isAdmin ? '<span class="admin-badge">管理员</span>' : '';
                userInfoDisplay.innerHTML = `<i class="fas fa-user me-1"></i>${user.realName || user.username} (${roleText}) ${adminBadge}`;
            }

            // 如果是管理员，显示管理员后台入口
            if (adminLink && this.isAdmin()) {
                adminLink.style.display = 'block';
            } else if (adminLink) {
                adminLink.style.display = 'none';
            }

            console.log('UI已更新为登录状态:', user);
        } else {
            // 用户未登录状态 - 显示登录按钮，隐藏下拉菜单
            if (loginButton) loginButton.style.display = 'block';
            if (userDropdownContainer) userDropdownContainer.style.display = 'none';

            console.log('UI已更新为未登录状态');
        }
    },

    // 获取角色显示文本
    getRoleText: function (role) {
        const roleMap = {
            'ADMIN': '管理员',
            'DOCTOR': '医生',
            'PATIENT': '患者',
            'USER': '用户'
        };
        return roleMap[role] || role;
    }
};

// 页面加载完成后初始化认证状态
document.addEventListener('DOMContentLoaded', function () {
    console.log('初始化通用认证状态管理...');
    AuthManager.updateUI();

    // 为退出链接添加事件监听
    const logoutLink = document.getElementById('logoutLink');
    if (logoutLink) {
        logoutLink.addEventListener('click', function (e) {
            e.preventDefault();
            if (confirm('确定要退出登录吗？')) {
                AuthManager.clearUser();
                // 跳转到首页
                window.location.href = '/';
            }
        });
    }

    console.log('通用认证状态管理初始化完成');
});

// 模拟登录功能（用于测试）
function simulateLogin(userData) {
    AuthManager.setUser(userData);
    return true;
}

// 模拟管理员登录
function simulateAdminLogin() {
    const adminUser = {
        id: 1,
        username: 'admin',
        realName: '系统管理员',
        email: 'admin@hospital.com',
        phone: '13800138000',
        role: 'ADMIN',
        enabled: true,
        loginTime: new Date().toISOString()
    };
    return simulateLogin(adminUser);
}

// 模拟患者登录
function simulatePatientLogin() {
    const patientUser = {
        id: 1001,
        username: 'patient001',
        realName: '张三',
        email: 'zhangsan@example.com',
        phone: '13912345678',
        role: 'PATIENT',
        enabled: true,
        loginTime: new Date().toISOString()
    };
    return simulateLogin(patientUser);
}

// 导出到全局作用域
window.AuthManager = AuthManager;
window.simulateLogin = simulateLogin;
window.simulateAdminLogin = simulateAdminLogin;
window.simulatePatientLogin = simulatePatientLogin;