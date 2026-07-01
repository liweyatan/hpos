// 科室列表页面JavaScript模块
class DepartmentListManager {
    constructor() {
        this.departmentIcons = {
            '内科': 'fas fa-heartbeat',
            '外科': 'fas fa-syringe',
            '全科': 'fas fa-user-md',
            '儿科': 'fas fa-child',
            '妇产科': 'fas fa-baby',
            '眼科': 'fas fa-eye',
            '口腔科': 'fas fa-tooth',
            'default': 'fas fa-hospital'
        };

        this.init();
    }

    init() {
        this.bindEvents();
        this.loadDepartments();
    }

    bindEvents() {
        // 搜索框实时搜索
        document.getElementById('searchInput').addEventListener('input', () => {
            this.searchDepartments();
        });

        // 搜索按钮点击事件
        document.querySelector('.btn-primary').addEventListener('click', () => {
            this.searchDepartments();
        });
    }

    // 获取科室图标
    getDepartmentIcon(departmentName) {
        return this.departmentIcons[departmentName] || this.departmentIcons.default;
    }

    // 从API获取科室数据
    async fetchDepartments() {
        try {
            const response = await fetch('/api/departments');
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('获取科室数据失败:', error);
            this.showError('获取科室数据失败，请检查网络连接或稍后重试');
            return [];
        }
    }

    // 从API获取指定科室的医生数据
    async fetchDoctorsByDepartment(departmentId) {
        try {
            const response = await fetch(`/api/doctors/department/${departmentId}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('获取医生数据失败:', error);
            return [];
        }
    }

    // 显示错误信息
    showError(message) {
        const container = document.getElementById('departmentsList');
        container.innerHTML = `
            <div class="col-12 text-center py-5">
                <i class="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
                <h5 class="text-warning">数据加载失败</h5>
                <p class="text-muted">${message}</p>
                <button class="btn btn-primary mt-2" onclick="departmentListManager.loadDepartments()">
                    <i class="fas fa-redo me-1"></i>重新加载
                </button>
            </div>
        `;
    }

    // 渲染科室列表
    async renderDepartments(departments) {
        const container = document.getElementById('departmentsList');

        if (!departments || departments.length === 0) {
            container.innerHTML = `
                <div class="col-12 text-center py-5">
                    <i class="fas fa-search fa-3x text-muted mb-3"></i>
                    <h5 class="text-muted">暂无科室数据</h5>
                    <p class="text-muted">请联系管理员添加科室信息</p>
                </div>
            `;
            return;
        }

        // 为每个科室获取医生数据
        const departmentsWithDoctors = await Promise.all(
            departments.map(async dept => {
                const doctors = await this.fetchDoctorsByDepartment(dept.id);
                return {
                    ...dept,
                    doctors: doctors.map(doctor => doctor.name)
                };
            })
        );

        let html = '';
        departmentsWithDoctors.forEach(dept => {
            const iconClass = this.getDepartmentIcon(dept.name);
            const deptName = dept.name || '未知科室';
            const description = dept.description || '暂无科室描述';
            const director = dept.director || '暂无';
            const location = dept.location || '暂无';
            const phone = dept.phone || '暂无';
            const doctors = dept.doctors ? dept.doctors.join('、') : '';
            const hasDoctors = dept.doctors && dept.doctors.length > 0;

            html += '\n            <div class="col-md-6 col-lg-4 mb-4">\n                <div class="card h-100">\n                    <div class="card-body text-center">\n                        <div class="feature-icon mb-3">\n                            <i class="' + iconClass + ' fa-2x text-primary"></i>\n                        </div>\n                        <h5 class="card-title text-primary">' + deptName + '</h5>\n                        <p class="card-text text-muted">' + description + '</p>\n                        \n                        <div class="department-info">\n                            <div class="mb-2">\n                                <small class="text-muted">\n                                    <i class="fas fa-user-md me-1"></i>\n                                    负责人：<span id="director-' + dept.id + '">' + director + '</span>\n                                </small>\n                            </div>\n                            <div class="mb-2">\n                                <small class="text-muted">\n                                    <i class="fas fa-map-marker-alt me-1"></i>\n                                    位置：<span id="location-' + dept.id + '">' + location + '</span>\n                                </small>\n                            </div>\n                            <div class="mb-2" id="doctors-' + dept.id + '" style="display: ' + (hasDoctors ? 'block' : 'none') + ';">\n                                <small class="text-muted">\n                                    <i class="fas fa-user-md me-1"></i>\n                                    医生：<span id="doctors-list-' + dept.id + '">' + doctors + '</span>\n                                </small>\n                            </div>\n                            <div class="mb-3">\n                                <small class="text-muted">\n                                    <i class="fas fa-phone me-1"></i>\n                                    电话：<span id="phone-' + dept.id + '">' + phone + '</span>\n                                </small>\n                            </div>\n                        </div>\n                        \n                        <button class="btn btn-outline-primary btn-sm" onclick="departmentListManager.appointmentToDepartment(\'' + deptName + '\')">\n                            <i class="fas fa-calendar-plus me-1"></i>预约\n                        </button>\n                    </div>\n                </div>\n            </div>\n            ';
        });
        container.innerHTML = html;
    }

    // 搜索科室
    async searchDepartments() {
        const searchTerm = document.getElementById('searchInput').value.toLowerCase();

        if (!searchTerm) {
            await this.loadDepartments();
            return;
        }

        const departments = await this.fetchDepartments();
        const filtered = departments.filter(dept =>
            dept.name.toLowerCase().includes(searchTerm) ||
            (dept.description && dept.description.toLowerCase().includes(searchTerm)) ||
            (dept.director && dept.director.toLowerCase().includes(searchTerm))
        );

        await this.renderDepartments(filtered);
    }

    // 预约到指定科室
    appointmentToDepartment(departmentName) {
        if (confirm(`是否要预约${departmentName}？`)) {
            window.location.href = `/appointment`;
        }
    }

    // 加载科室数据
    async loadDepartments() {
        const container = document.getElementById('departmentsList');
        container.innerHTML = `
            <div class="col-12 text-center py-5" id="loadingMessage">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">加载中...</span>
                </div>
                <p class="mt-2 text-muted">正在加载科室数据...</p>
            </div>
        `;

        const departments = await this.fetchDepartments();
        await this.renderDepartments(departments);
    }
}

// 全局函数，用于从HTML中调用
function searchDepartments() {
    departmentListManager.searchDepartments();
}

function appointmentToDepartment(departmentName) {
    departmentListManager.appointmentToDepartment(departmentName);
}

// 页面加载完成后初始化
let departmentListManager;
document.addEventListener('DOMContentLoaded', function () {
    departmentListManager = new DepartmentListManager();
});