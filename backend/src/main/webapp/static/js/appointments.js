/**
 * 我的预约页面Vue应用 - 修复版
 * 主要修复：解决用户ID与患者ID不匹配导致的加载失败问题
 */

// 我的预约Vue应用
const MyAppointmentsApp = {
    data() {
        return {
            // 当前用户
            currentUser: null,
            // 当前患者（可能与用户不同）
            currentPatient: null,
            // 预约数据
            appointments: [],
            // 筛选状态
            currentFilter: '',
            // 页面状态
            loading: true,
            // 消息提示
            errorMessage: '',
            successMessage: '',
            showErrorMessage: false,
            showSuccessMessage: false,
            messageTimer: null
        }
    },

    computed: {
        // 筛选后的预约
        filteredAppointments() {
            if (!this.currentFilter) {
                return this.appointments;
            }
            return this.appointments.filter(app => app.status === this.currentFilter);
        },

        // 状态筛选器
        statusFilters() {
            const statusCounts = {};
            this.appointments.forEach(app => {
                statusCounts[app.status] = (statusCounts[app.status] || 0) + 1;
            });

            return [
                { label: '全部', value: '', count: this.appointments.length },
                {label: '待处理', value: 'PENDING', count: statusCounts['PENDING'] || 0},
                {label: '已确认', value: 'CONFIRMED', count: statusCounts['CONFIRMED'] || 0},
                {label: '已完成', value: 'COMPLETED', count: statusCounts['COMPLETED'] || 0},
                {label: '已取消', value: 'CANCELLED', count: statusCounts['CANCELLED'] || 0}
            ];
        }
    },

    async mounted() {
        console.log('我的预约页面初始化完成');
        await this.initializeApp();
    },

    methods: {
        /**
         * 初始化应用 - 修复：增加患者信息获取步骤
         */
        async initializeApp() {
            try {
                // 检查登录状态
                await this.checkLoginStatus();

                // 获取或创建患者信息 - 关键修复：确保有有效的患者ID
                await this.ensurePatientExists();

                // 加载预约数据
                await this.loadAppointments();

                console.log('我的预约页面初始化成功');
            } catch (error) {
                console.error('初始化失败:', error);
                this.showError('页面初始化失败：' + error.message);
            }
        },

        /**
         * 检查登录状态
         */
        async checkLoginStatus() {
            try {
                // 使用统一的认证管理器检查登录状态
                if (typeof AuthManager === 'undefined') {
                    throw new Error('认证系统未加载');
                }
                
                if (AuthManager.isLoggedIn()) {
                    this.currentUser = AuthManager.getUser();
                    console.log('用户已登录:', this.currentUser.username);
                } else {
                    // 如果没有登录，重定向到登录页面
                    console.log('用户未登录，重定向到登录页面');
                    if (confirm('您尚未登录，是否前往登录页面？')) {
                        window.location.href = '/login';
                    } else {
                        window.location.href = '/';
                    }
                    throw new Error('用户未登录');
                }
            } catch (error) {
                console.error('检查登录状态失败:', error);
                
                if (!error.message.includes('用户未登录')) {
                    // 如果是其他错误，重定向到登录页面
                    if (confirm('用户信息异常，是否重新登录？')) {
                        window.location.href = '/login';
                    } else {
                        window.location.href = '/';
                    }
                }
                throw error;
            }
        },

        /**
         * 确保患者信息存在 - 新增方法：解决用户/患者ID不匹配问题
         */
        async ensurePatientExists() {
            try {
                console.log('开始验证患者信息...');

                // 方法1：尝试根据当前用户信息查找患者
                let patient = await this.findPatientByUserInfo();

                if (!patient) {
                    // 方法2：如果找不到，创建新的患者记录
                    console.log('未找到关联患者，创建新患者记录...');
                    patient = await this.createPatientFromUser();
                }

                if (patient && patient.id) {
                    this.currentPatient = patient;
                    console.log('患者信息确认完成:', patient);
                    return true;
                } else {
                    throw new Error('无法获取有效的患者信息');
                }

            } catch (error) {
                console.error('确保患者信息存在失败:', error);
                this.showError('患者信息初始化失败：' + error.message);
                throw error;
            }
        },

        /**
         * 根据用户信息查找患者 - 新增方法
         */
        async findPatientByUserInfo() {
            try {
                // 尝试通过手机号查找患者（假设用户手机号与患者手机号一致）
                if (this.currentUser.phone) {
                    const response = await fetch(`/api/patients/phone/${this.currentUser.phone}`);
                    if (response.ok) {
                        const patient = await response.json();
                        if (patient && patient.id) {
                            console.log('通过手机号找到患者:', patient);
                            return patient;
                        }
                    }
                }

                // 尝试通过用户名查找（假设用户名与患者名有某种关联）
                if (this.currentUser.username) {
                    const response = await fetch(`/api/patients/search?name=${this.currentUser.username}`);
                    if (response.ok) {
                        const patients = await response.json();
                        if (patients && patients.length > 0) {
                            console.log('通过用户名找到患者:', patients[0]);
                            return patients[0];
                        }
                    }
                }

                return null;
            } catch (error) {
                console.warn('查找患者信息时出错:', error);
                return null;
            }
        },

        /**
         * 根据用户信息创建患者 - 新增方法
         */
        async createPatientFromUser() {
            try {
                const patientData = {
                    name: this.currentUser.realName || this.currentUser.username,
                    phone: this.currentUser.phone,
                    idCard: this.currentUser.idCard || '' // 如果没有身份证信息，可能需要用户补充
                };

                const response = await fetch('/api/patients', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(patientData)
                });

                if (response.ok) {
                    const newPatient = await response.json();
                    console.log('创建新患者成功:', newPatient);
                    return newPatient;
                } else {
                    const errorData = await response.json().catch(() => ({}));
                    throw new Error(errorData.message || '创建患者失败');
                }
            } catch (error) {
                console.error('创建患者失败:', error);
                throw new Error('无法创建患者记录：' + error.message);
            }
        },

        /**
         * 加载预约数据 - 修复：使用正确的患者ID
         */
        async loadAppointments() {
            this.loading = true;

            try {
                // 关键修复：使用患者ID而不是用户ID
                if (!this.currentPatient || !this.currentPatient.id) {
                    throw new Error('患者信息不完整，无法加载预约记录');
                }

                const patientId = this.currentPatient.id;
                console.log('正在加载患者ID为', patientId, '的预约记录');

                const response = await fetch(`/api/registration-orders/patient/${patientId}`);

                if (response.ok) {
                    const data = await response.json();
                    this.appointments = data;
                    console.log('加载预约数据成功:', this.appointments.length, '条记录');

                    if (this.appointments.length === 0) {
                        this.showInfo('您还没有任何预约记录');
                    }
                } else {
                    console.error('加载预约数据失败: HTTP状态码', response.status);

                    // 更详细的错误处理
                    let errorMsg = '加载预约信息失败';
                    try {
                        const errorData = await response.json();
                        errorMsg = errorData.message || errorMsg;
                    } catch (e) {
                        // 如果无法解析JSON错误信息，使用状态码判断
                        if (response.status === 404) {
                            errorMsg = '未找到预约记录';
                        } else if (response.status === 401) {
                            errorMsg = '请重新登录';
                            setTimeout(() => window.location.href = '/login', 2000);
                        } else if (response.status >= 500) {
                            errorMsg = '服务器错误，请稍后重试';
                        }
                    }
                    this.showError(errorMsg);
                }

            } catch (error) {
                console.error('加载预约数据失败:', error);
                let errorMsg = '网络错误，请检查网络连接后重试';
                if (error.message.includes('患者信息不完整')) {
                    errorMsg = error.message;
                }
                this.showError(errorMsg);
            } finally {
                this.loading = false;
            }
        },

        /**
         * 设置筛选条件
         */
        setFilter(status) {
            this.currentFilter = this.currentFilter === status ? '' : status;
        },

        /**
         * 刷新预约列表
         */
        async refreshAppointments() {
            await this.loadAppointments();
            this.showSuccess('预约列表已刷新');
        },

        /**
         * 取消预约
         */
        async cancelAppointment(id) {
            if (!confirm('确定要取消这个预约吗？')) {
                return;
            }

            try {
                const response = await fetch(`/api/registration-orders/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    const result = await response.json();
                    if (result.success) {
                        // 更新本地数据
                        this.appointments = this.appointments.map(app => {
                            if (app.id === id) {
                                return {...app, status: 'CANCELLED'};
                            }
                            return app;
                        });
                        this.showSuccess(result.message || '预约取消成功');
                    } else {
                        this.showError(result.message || '取消预约失败');
                    }
                } else {
                    this.showError('取消预约失败，请稍后重试');
                }

            } catch (error) {
                console.error('取消预约失败:', error);
                this.showError('网络错误，请稍后重试');
            }
        },

        /**
         * 查看预约详情
         */
        viewAppointmentDetail(id) {
            const appointment = this.appointments.find(app => app.id === id);
            if (appointment) {
                const detail = `
预约号: ${appointment.appointmentNo || '无'}
科室: ${appointment.departmentName}
医生: ${appointment.doctorName}
时间: ${this.formatDateTime(appointment.registerTime)}
就诊人: ${appointment.patientName}
症状: ${appointment.symptoms || '无'}
状态: ${appointment.status}
创建时间: ${this.formatDateTime(appointment.createTime)}
                `;
                alert(detail);
            }
        },

        /**
         * 格式化日期时间
         */
        formatDateTime(dateTime) {
            if (!dateTime) return '';
            try {
                const date = new Date(dateTime);
                return date.toLocaleString('zh-CN');
            } catch (error) {
                return dateTime;
            }
        },

        /**
         * 获取状态徽章样式类名
         */
        getStatusBadgeClass(status) {
            switch(status) {
                case 'PENDING':
                    return 'status-pending';
                case 'CONFIRMED':
                    return 'status-reserved';
                case 'COMPLETED':
                    return 'status-completed';
                case 'CANCELLED':
                    return 'status-cancelled';
                default: return 'bg-secondary';
            }
        },

        /**
         * 显示成功消息
         */
        showSuccess(message) {
            this.clearMessageTimer();

            this.successMessage = message;
            this.errorMessage = '';
            this.showSuccessMessage = true;
            this.showErrorMessage = false;

            this.messageTimer = setTimeout(() => {
                this.hideMessages();
            }, 5000);
        },

        /**
         * 显示错误消息
         */
        showError(message) {
            this.clearMessageTimer();

            this.errorMessage = message;
            this.successMessage = '';
            this.showErrorMessage = true;
            this.showSuccessMessage = false;

            this.messageTimer = setTimeout(() => {
                this.hideMessages();
            }, 5000);
        },

        /**
         * 显示信息消息
         */
        showInfo(message) {
            this.showSuccess(message);
        },

        /**
         * 清理消息定时器
         */
        clearMessageTimer() {
            if (this.messageTimer) {
                clearTimeout(this.messageTimer);
            }
        },

        /**
         * 隐藏所有消息
         */
        hideMessages() {
            this.showErrorMessage = false;
            this.showSuccessMessage = false;
            this.errorMessage = '';
            this.successMessage = '';
            this.clearMessageTimer();
        }
    }
};

// 页面加载完成后初始化Vue应用
document.addEventListener('DOMContentLoaded', function() {
    if (typeof Vue !== 'undefined') {
        const app = Vue.createApp(MyAppointmentsApp);
        app.mount('#my-appointments-app');
        console.log('我的预约Vue应用挂载成功 - 已修复患者ID问题');
    } else {
        console.error('Vue.js 未加载，请检查网络连接');
    }
});