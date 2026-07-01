// 管理员后台JavaScript功能

// 全局变量
let currentEditingId = null;

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function () {
    // 检查管理员权限
    checkAdminAuth();

    loadAllData();

    // 监听标签页切换事件
    document.getElementById('adminTabs').addEventListener('shown.bs.tab', function (event) {
        const target = event.target.getAttribute('data-bs-target');
        refreshTabData(target);
    });
});

// 检查管理员权限
function checkAdminAuth() {
    // 使用通用认证管理器检查登录状态
    if (typeof AuthManager === 'undefined') {
        console.error('认证管理器未加载，请检查auth.js文件');
        window.location.href = '/login';
        return false;
    }

    // 检查是否已登录
    if (!AuthManager.isLoggedIn()) {
        alert('请先登录系统');
        window.location.href = '/login';
        return false;
    }

    // 检查是否为管理员
    if (!AuthManager.isAdmin()) {
        alert('权限不足，只有管理员可以访问此页面');
        window.location.href = '/';
        return false;
    }

    // 更新UI显示
    AuthManager.updateUI();
    console.log('管理员权限检查通过');
    return true;
}

// 退出登录
function logout() {
    if (confirm('确定要退出登录吗？')) {
        // 使用通用认证管理器退出登录
        if (typeof AuthManager !== 'undefined') {
            AuthManager.clearUser();
        }
        
        // 跳转到首页
        window.location.href = '/';
    }
}

// 加载所有数据
function loadAllData() {
    loadDepartments();
    loadDoctors();
    loadAppointments();
    loadUsers();
}

// 刷新当前标签页数据
function refreshTabData(tabId) {
    switch (tabId) {
        case '#department':
            loadDepartments();
            break;
        case '#doctor':
            loadDoctors();
            break;
        case '#appointment':
            loadAppointments();
            break;
        case '#user':
            loadUsers();
            break;
    }
}

// 科室管理功能
function loadDepartments() {
    fetch('/api/admin/departments')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('departmentTableBody');
            tbody.innerHTML = '';

            if (data && data.length > 0) {
                data.forEach(dept => {
                    const row = createDepartmentRow(dept);
                    tbody.appendChild(row);
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4">暂无科室数据</td></tr>';
            }
        })
        .catch(error => {
            console.error('加载科室数据失败:', error);
            showAlert('加载科室数据失败', 'danger');
        });
}

function createDepartmentRow(dept) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${escapeHtml(dept.name || '')}</td>
        <td>${escapeHtml(dept.director || '')}</td>
        <td>${escapeHtml(dept.phone || '')}</td>
        <td>${escapeHtml(dept.location || '')}</td>
        <td>${createStatusBadge(dept.active)}</td>
        <td>
            <div class="btn-group btn-group-sm">
                <button class="btn btn-outline-primary" onclick="editDepartment(${dept.id})" title="编辑">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-outline-danger" onclick="deleteDepartment(${dept.id})" title="删除">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </td>
    `;
    return row;
}

function showDepartmentModal(dept = null) {
    const modal = new bootstrap.Modal(document.getElementById('departmentModal'));
    const form = document.getElementById('departmentForm');

    if (dept) {
        // 编辑模式
        document.getElementById('departmentModalLabel').textContent = '编辑科室';
        document.getElementById('departmentId').value = dept.id;
        document.getElementById('departmentName').value = dept.name || '';
        document.getElementById('departmentDescription').value = dept.description || '';
        document.getElementById('departmentDirector').value = dept.director || '';
        document.getElementById('departmentPhone').value = dept.phone || '';
        document.getElementById('departmentLocation').value = dept.location || '';

        if (dept.active === 0) {
            document.getElementById('departmentInactive').checked = true;
        } else {
            document.getElementById('departmentActive').checked = true;
        }
    } else {
        // 新增模式
        document.getElementById('departmentModalLabel').textContent = '新增科室';
        form.reset();
        document.getElementById('departmentId').value = '';
        document.getElementById('departmentActive').checked = true;
    }

    modal.show();
}

function saveDepartment() {
    const formData = new FormData(document.getElementById('departmentForm'));
    const data = Object.fromEntries(formData);

    // 表单验证
    if (!data.name || data.name.trim() === '') {
        showAlert('请输入科室名称', 'danger');
        return;
    }

    const url = data.id ? `/api/admin/departments/${data.id}` : '/api/admin/departments';
    const method = data.id ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                showAlert('科室保存成功', 'success');
                bootstrap.Modal.getInstance(document.getElementById('departmentModal')).hide();
                loadDepartments();
            } else {
                showAlert(result.message || '保存失败', 'danger');
            }
        })
        .catch(error => {
            console.error('保存科室失败:', error);
            showAlert('保存失败', 'danger');
        });
}

function editDepartment(id) {
    fetch(`/api/admin/departments/${id}`)
        .then(response => response.json())
        .then(dept => {
            showDepartmentModal(dept);
        })
        .catch(error => {
            console.error('获取科室数据失败:', error);
            showAlert('获取科室数据失败', 'danger');
        });
}

function deleteDepartment(id) {
    if (confirm('确定要删除这个科室吗？此操作不可恢复！')) {
        fetch(`/api/admin/departments/${id}`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    showAlert('科室删除成功', 'success');
                    loadDepartments();
                } else {
                    showAlert(result.message || '删除失败', 'danger');
                }
            })
            .catch(error => {
                console.error('删除科室失败:', error);
                showAlert('删除失败', 'danger');
            });
    }
}

// 医生管理功能
function loadDoctors() {
    fetch('/api/admin/doctors')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('doctorTableBody');
            tbody.innerHTML = '';

            if (data && data.length > 0) {
                data.forEach(doctor => {
                    const row = createDoctorRow(doctor);
                    tbody.appendChild(row);
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4">暂无医生数据</td></tr>';
            }
        })
        .catch(error => {
            console.error('加载医生数据失败:', error);
            showAlert('加载医生数据失败', 'danger');
        });
}

function createDoctorRow(doctor) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${escapeHtml(doctor.name || '')}</td>
        <td>${escapeHtml(doctor.title || '')}</td>
        <td>${escapeHtml(doctor.departmentName || '')}</td>
        <td>${escapeHtml(doctor.specialty || '')}</td>
        <td>${doctor.maxPatients || 0}</td>
        <td>${doctor.currentPatients || 0}</td>
        <td>${createStatusBadge(doctor.available)}</td>
        <td>
            <div class="btn-group btn-group-sm">
                <button class="btn btn-outline-primary" onclick="editDoctor(${doctor.id})" title="编辑">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-outline-danger" onclick="deleteDoctor(${doctor.id})" title="删除">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </td>
    `;
    return row;
}

function showDoctorModal() {
    // 这里需要先加载科室数据用于下拉选择
    loadDepartmentsForSelect().then(departments => {
        // 创建医生模态框HTML
        const modalHtml = `
            <div class="modal fade" id="doctorModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">医生信息</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form id="doctorForm">
                                <input type="hidden" id="doctorId" name="id">
                                <div class="mb-3">
                                    <label class="form-label">姓名</label>
                                    <input type="text" class="form-control" name="name" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">职称</label>
                                    <input type="text" class="form-control" name="title">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">所属科室</label>
                                    <select class="form-control" name="departmentId" required>
                                        <option value="">请选择科室</option>
                                        ${departments.map(dept => `<option value="${dept.id}">${dept.name}</option>`).join('')}
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">专长</label>
                                    <textarea class="form-control" name="specialty" rows="3"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">最大接诊数</label>
                                    <input type="number" class="form-control" name="maxPatients" value="20" min="1">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">状态</label>
                                    <div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="available" value="true" checked>
                                            <label class="form-check-label">可预约</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="available" value="false">
                                            <label class="form-check-label">不可预约</label>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" onclick="saveDoctor()">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // 如果模态框已存在，先移除
        const existingModal = document.getElementById('doctorModal');
        if (existingModal) {
            existingModal.remove();
        }

        // 添加模态框到页面
        document.body.insertAdjacentHTML('beforeend', modalHtml);

        // 显示模态框
        const modal = new bootstrap.Modal(document.getElementById('doctorModal'));
        modal.show();
    });
}

function loadDepartmentsForSelect() {
    return fetch('/api/admin/departments')
        .then(response => response.json())
        .then(data => data || []);
}

// 预约管理功能
function loadAppointments() {
    fetch('/api/admin/appointments')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('appointmentTableBody');
            tbody.innerHTML = '';

            if (data && data.length > 0) {
                data.forEach(appointment => {
                    const row = createAppointmentRow(appointment);
                    tbody.appendChild(row);
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4">暂无预约数据</td></tr>';
            }
        })
        .catch(error => {
            console.error('加载预约数据失败:', error);
            showAlert('加载预约数据失败', 'danger');
        });
}

function createAppointmentRow(appointment) {
    const row = document.createElement('tr');
    const registerTime = new Date(appointment.registerTime).toLocaleString('zh-CN');
    const createTime = new Date(appointment.createTime).toLocaleString('zh-CN');

    row.innerHTML = `
        <td>${appointment.appointmentNo || 'N/A'}</td>
        <td>${escapeHtml(appointment.patientName || '')}</td>
        <td>${escapeHtml(appointment.doctorName || '')}</td>
        <td>${escapeHtml(appointment.departmentName || '')}</td>
        <td>${registerTime}</td>
        <td>${escapeHtml(appointment.symptoms || '')}</td>
        <td>${createAppointmentStatusBadge(appointment.status)}</td>
        <td>
            <div class="btn-group btn-group-sm">
                <button class="btn btn-outline-primary" onclick="updateAppointmentStatus(${appointment.id}, 'CONFIRMED')" title="确认">
                    <i class="fas fa-check"></i>
                </button>
                <button class="btn btn-outline-warning" onclick="updateAppointmentStatus(${appointment.id}, 'CANCELLED')" title="取消">
                    <i class="fas fa-times"></i>
                </button>
                <button class="btn btn-outline-success" onclick="updateAppointmentStatus(${appointment.id}, 'COMPLETED')" title="完成">
                    <i class="fas fa-flag-checkered"></i>
                </button>
            </div>
        </td>
    `;
    return row;
}

function updateAppointmentStatus(id, status) {
    const statusText = {
        'PENDING': '待处理',
        'CONFIRMED': '已确认',
        'CANCELLED': '已取消',
        'COMPLETED': '已完成'
    }[status] || status;

    if (confirm(`确定要将预约状态修改为"${statusText}"吗？`)) {
        fetch(`/api/admin/appointments/${id}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({status: status})
        })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    showAlert('预约状态更新成功', 'success');
                    loadAppointments();
                } else {
                    showAlert(result.message || '更新失败', 'danger');
                }
            })
            .catch(error => {
                console.error('更新预约状态失败:', error);
                showAlert('更新失败', 'danger');
            });
    }
}

// 用户管理功能
function loadUsers() {
    fetch('/api/admin/users')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('userTableBody');
            tbody.innerHTML = '';

            if (data && data.length > 0) {
                data.forEach(user => {
                    const row = createUserRow(user);
                    tbody.appendChild(row);
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4">暂无用户数据</td></tr>';
            }
        })
        .catch(error => {
            console.error('加载用户数据失败:', error);
            showAlert('加载用户数据失败', 'danger');
        });
}

function createUserRow(user) {
    const createTime = new Date(user.createTime).toLocaleString('zh-CN');

    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${escapeHtml(user.username || '')}</td>
        <td>${escapeHtml(user.realName || '')}</td>
        <td>${escapeHtml(user.email || '')}</td>
        <td>${escapeHtml(user.phone || '')}</td>
        <td>${createRoleBadge(user.role)}</td>
        <td>${createStatusBadge(user.enabled)}</td>
        <td>${createTime}</td>
        <td>
            <div class="btn-group btn-group-sm">
                <button class="btn btn-outline-primary" onclick="editUser(${user.id})" title="编辑">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-outline-danger" onclick="deleteUser(${user.id})" title="删除">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </td>
    `;
    return row;
}

// 工具函数
function createStatusBadge(status) {
    if (status === true || status === 1 || status === 'true' || status === '1') {
        return '<span class="badge bg-success">启用</span>';
    } else {
        return '<span class="badge bg-danger">禁用</span>';
    }
}

function createAppointmentStatusBadge(status) {
    const statusMap = {
        'PENDING': {class: 'bg-warning', text: '待处理'},
        'CONFIRMED': {class: 'bg-primary', text: '已确认'},
        'CANCELLED': {class: 'bg-danger', text: '已取消'},
        'COMPLETED': {class: 'bg-success', text: '已完成'}
    };

    const statusInfo = statusMap[status] || {class: 'bg-secondary', text: status};
    return `<span class="badge ${statusInfo.class}">${statusInfo.text}</span>`;
}

function createRoleBadge(role) {
    const roleMap = {
        'ADMIN': {class: 'bg-danger', text: '管理员'},
        'DOCTOR': {class: 'bg-primary', text: '医生'},
        'PATIENT': {class: 'bg-success', text: '患者'},
        'USER': {class: 'bg-secondary', text: '用户'}
    };

    const roleInfo = roleMap[role] || {class: 'bg-secondary', text: role};
    return `<span class="badge ${roleInfo.class}">${roleInfo.text}</span>`;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showAlert(message, type) {
    // 移除现有的警告框
    const existingAlert = document.querySelector('.alert-dismissible');
    if (existingAlert) {
        existingAlert.remove();
    }

    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;

    // 在页面顶部显示警告
    document.querySelector('.container.mt-5').insertAdjacentHTML('beforebegin', alertHtml);

    // 5秒后自动消失
    setTimeout(() => {
        const alert = document.querySelector('.alert-dismissible');
        if (alert) {
            bootstrap.Alert.getInstance(alert).close();
        }
    }, 5000);
}

// 模态框隐藏时的事件处理
document.addEventListener('hidden.bs.modal', function (event) {
    // 清理医生模态框
    if (event.target.id === 'doctorModal') {
        event.target.remove();
    }
});