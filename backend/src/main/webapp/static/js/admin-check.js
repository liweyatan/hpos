// 管理员权限检查脚本
// 处理非管理员用户点击管理员后台按钮时的提示

/**
 * 检查管理员权限
 * 如果用户不是管理员，显示小丑表情弹窗
 */
function checkAdminPermission() {
    // 获取当前用户信息
    const userData = localStorage.getItem('hospital_user') || sessionStorage.getItem('hospital_user');
    
    if (!userData) {
        // 用户未登录
        showClownAlert('您还没有登录呢！🤡', '请先登录系统');
        return false;
    }
    
    try {
        const currentUser = JSON.parse(userData);
        
        if (currentUser.role === 'ADMIN') {
            // 管理员用户，允许访问后台
            window.location.href = '/admin';
            return true;
        } else {
            // 非管理员用户，显示小丑弹窗
            const roleText = currentUser.role === 'PATIENT' ? '普通患者' : '普通用户';
            showClownAlert(`权限不足！🤡`, `您只是${roleText}，想什么呢？`);
            return false;
        }
    } catch (error) {
        console.error('解析用户信息失败:', error);
        showClownAlert('系统错误！🤡', '无法验证您的权限');
        return false;
    }
}

/**
 * 显示小丑表情弹窗
 * @param {string} title - 弹窗标题
 * @param {string} message - 弹窗消息
 */
function showClownAlert(title, message) {
    // 创建弹窗HTML
    const modalHtml = `
        <div class="modal fade" id="clownModal" tabindex="-1" aria-labelledby="clownModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h5 class="modal-title" id="clownModalLabel">
                            <i class="fas fa-exclamation-triangle me-2"></i>${title}
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-center">
                        <div style="font-size: 4rem; margin-bottom: 1rem;">🤡</div>
                        <p class="mb-0">${message}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary w-100" data-bs-dismiss="modal">
                            知道了，我是小丑 🤡
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    // 如果已存在弹窗，先移除
    const existingModal = document.getElementById('clownModal');
    if (existingModal) {
        existingModal.remove();
    }
    
    // 添加弹窗到页面
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // 显示弹窗
    const clownModal = new bootstrap.Modal(document.getElementById('clownModal'));
    clownModal.show();
    
    // 弹窗关闭后自动移除
    document.getElementById('clownModal').addEventListener('hidden.bs.modal', function() {
        this.remove();
    });
}

/**
 * 页面加载时检查用户权限并更新导航栏显示
 */
document.addEventListener('DOMContentLoaded', function() {
    updateNavbarByUserRole();
});

/**
 * 根据用户角色更新导航栏显示
 */
function updateNavbarByUserRole() {
    const userData = localStorage.getItem('hospital_user') || sessionStorage.getItem('hospital_user');
    const loginButton = document.getElementById('loginButton');
    const userDropdownContainer = document.getElementById('userDropdownContainer');
    
    if (!userData) {
        // 未登录状态
        if (loginButton) loginButton.style.display = 'block';
        if (userDropdownContainer) userDropdownContainer.style.display = 'none';
        return;
    }
    
    try {
        const currentUser = JSON.parse(userData);
        
        // 显示用户下拉菜单
        if (loginButton) loginButton.style.display = 'none';
        if (userDropdownContainer) userDropdownContainer.style.display = 'block';
        
        // 更新用户信息显示
        const userNameDisplay = document.getElementById('userNameDisplay');
        const userInfoDisplay = document.getElementById('userInfoDisplay');
        
        if (userNameDisplay) {
            userNameDisplay.textContent = currentUser.realName || currentUser.username;
        }
        
        if (userInfoDisplay) {
            const roleText = currentUser.role === 'ADMIN' ? '管理员' : 
                           currentUser.role === 'PATIENT' ? '患者' : '用户';
            userInfoDisplay.innerHTML = `<i class="fas fa-user me-1"></i>${currentUser.realName || currentUser.username} (${roleText})`;
        }
        
        // 如果是管理员，显示特殊样式
        const adminLink = document.getElementById('adminLink');
        if (adminLink) {
            if (currentUser.role === 'ADMIN') {
                adminLink.classList.add('text-danger', 'fw-bold');
            } else {
                adminLink.classList.remove('text-danger', 'fw-bold');
            }
        }
        
    } catch (error) {
        console.error('更新导航栏显示失败:', error);
    }
}

/**
 * 退出登录功能
 */
function logout() {
    if (confirm('确定要退出登录吗？')) {
        // 清除用户信息
        localStorage.removeItem('hospital_user');
        sessionStorage.removeItem('hospital_user');
        
        // 刷新页面
        window.location.href = '/';
    }
}