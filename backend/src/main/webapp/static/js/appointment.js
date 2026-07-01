// 预约页面Vue应用
const appointmentApp = Vue.createApp({
    data() {
        return {
            // 当前步骤
            currentStep: 1,

            // 当前用户
            currentUser: null,

            // 科室数据
            departments: [],
            selectedDepartment: null,

            // 医生数据
            doctors: [],
            selectedDoctor: null,
            filter: {
                title: '',
                feeRange: '',
                rating: 0
            },

            // 时间选择
            availableDates: [],
            selectedDate: null,
            timeSlots: [],
            selectedTime: null,

            // 预约信息
            appointmentInfo: {
                patientName: '',
                phone: '',
                idCard: '',
                symptoms: ''
            },

            // 状态控制
            loading: false,

            // 消息提示
            errorMessage: '',
            successMessage: '',
            showErrorMessage: false,
            showSuccessMessage: false,
            messageTimer: null
        }
    },
    computed: {
        // 是否可以继续下一步
        canProceed() {
            switch (this.currentStep) {
                case 1:
                    return this.selectedDepartment !== null;
                case 2:
                    return this.selectedDoctor !== null;
                case 3:
                    return this.selectedDate !== null && this.selectedTime !== null;
                case 4:
                    return this.appointmentInfo.patientName && this.appointmentInfo.phone;
                default:
                    return false;
            }
        },
        
        // 根据筛选条件过滤医生
        filteredDoctors() {
            let result = this.doctors;

            // 按科室筛选
            if (this.selectedDepartment) {
                result = result.filter(doctor => doctor.departmentId === this.selectedDepartment.id);
            }

            // 按职称筛选
            if (this.filter.title) {
                result = result.filter(doctor => doctor.title === this.filter.title);
            }

            // 按费用范围筛选
            if (this.filter.feeRange) {
                const [min, max] = this.filter.feeRange.split('-').map(Number);
                if (max) {
                    result = result.filter(doctor => doctor.fee >= min && doctor.fee <= max);
                } else {
                    result = result.filter(doctor => doctor.fee >= min);
                }
            }

            // 按评分筛选
            if (this.filter.rating > 0) {
                result = result.filter(doctor => doctor.rating >= this.filter.rating);
            }

            return result;
        },

        // 总费用计算
        totalFee() {
            return (this.selectedDoctor?.fee || 0) + 5;
        }
    },
    async mounted() {
        console.log('预约页面初始化完成');

        // 检查登录状态
        await this.checkLoginStatus();

        // 加载数据
        await this.loadDepartments();
        await this.loadDoctors();
        await this.loadAvailableDates();
    },
    methods: {
        // 检查登录状态
        async checkLoginStatus() {
            try {
                // 检查AuthManager是否已加载
                if (typeof AuthManager === 'undefined') {
                    console.error('AuthManager未加载，请检查auth.js文件');
                    this.showError('认证系统未加载，请刷新页面重试');
                    return;
                }
                
                // 使用统一的认证管理
                const user = AuthManager.getUser();
                console.log('从AuthManager获取的用户信息:', JSON.stringify(user, null, 2));
                
                if (user) {
                    this.currentUser = {
                        id: user.id,
                        username: user.username,
                        name: user.realName || user.username || '用户', // 使用realName作为显示名称，如果都为null则显示'用户'
                        realName: user.realName || user.username || '用户', // 确保realName不为null
                        phone: user.phone || '', // 确保手机号字段正确映射
                        role: user.role
                    };
                    
                    console.log('当前登录用户信息:', this.currentUser);
                    
                    // 如果已登录，预填充就诊人信息，但允许用户修改
                    if (this.currentUser.name) {
                        this.appointmentInfo.patientName = this.currentUser.name;
                    }
                    if (this.currentUser.phone) {
                        this.appointmentInfo.phone = this.currentUser.phone;
                    } else {
                        console.warn('用户手机号为空，检查数据库数据或登录流程');
                    }
                    // 注意：User实体中没有idCard字段，如果不需要可以删除或从其他接口获取
                    
                    console.log('预填充信息:', this.appointmentInfo);
                } else {
                    // 未登录，跳转到登录页
                    console.error('用户未登录，重定向到登录页面');
                    this.showError('请先登录系统');
                    setTimeout(() => {
                        window.location.href = '/login';
                    }, 2000);
                }
            } catch (error) {
                console.error('检查登录状态失败:', error);
                this.showError('登录状态检查失败，请刷新页面重试');
            }
        },


        // 加载科室数据
        async loadDepartments() {
            try {
                const response = await fetch('/api/departments');
                if (!response.ok) {
                    throw new Error('加载科室数据失败');
                }
                this.departments = await response.json();

            } catch (error) {
                console.error('加载科室数据失败:', error);
                this.showError('加载科室信息失败，请刷新页面重试');
            }
        },

        // 加载医生数据
        async loadDoctors() {
            try {
                let url = '/api/doctors';
                if (this.selectedDepartment) {
                    url = `/api/doctors/department/${this.selectedDepartment.id}`;
                }
                const response = await fetch(url);
                if (!response.ok) {
                    throw new Error('加载医生数据失败');
                }
                this.doctors = await response.json();

            } catch (error) {
                console.error('加载医生数据失败:', error);
                this.showError('加载医生信息失败，请刷新页面重试');
            }
        },

        // 加载可用日期
        async loadAvailableDates() {
            try {
                const today = new Date();
                const dates = [];

                for (let i = 0; i < 7; i++) {
                    const date = new Date(today);
                    date.setDate(today.getDate() + i);

                    const dateStr = date.toISOString().split('T')[0];
                    const dayStr = this.getChineseDay(date.getDay());
                    const formattedDate = `${date.getMonth() + 1}月${date.getDate()}日`;

                    dates.push({
                        date: dateStr,
                        formattedDate: formattedDate,
                        day: dayStr,
                        available: i > 0 // 今天不可预约
                    });
                }

                this.availableDates = dates;
                this.selectedDate = dates[1]?.date; // 默认选择明天

                // 加载对应的时间段
                await this.loadTimeSlots(this.selectedDate);

            } catch (error) {
                console.error('加载日期数据失败:', error);
            }
        },

        // 获取中文星期
        getChineseDay(day) {
            const days = ['日', '一', '二', '三', '四', '五', '六'];
            return `星期${days[day]}`;
        },

        // 加载时间段
        async loadTimeSlots(date) {
            try {
                // 模拟API调用
                await new Promise(resolve => setTimeout(resolve, 300));

                const slots = [
                    { time: '08:00-08:30', available: true },
                    { time: '08:30-09:00', available: true },
                    { time: '09:00-09:30', available: false },
                    { time: '09:30-10:00', available: true },
                    { time: '10:00-10:30', available: true },
                    { time: '10:30-11:00', available: true },
                    { time: '14:00-14:30', available: true },
                    { time: '14:30-15:00', available: true },
                    { time: '15:00-15:30', available: true },
                    { time: '15:30-16:00', available: false },
                    { time: '16:00-16:30', available: true },
                    { time: '16:30-17:00', available: true }
                ];

                this.timeSlots = slots;

            } catch (error) {
                console.error('加载时间段失败:', error);
            }
        },

        // 选择科室
        async selectDepartment(dept) {
            this.selectedDepartment = dept;
            this.selectedDoctor = null; // 重置医生选择
            await this.loadDoctors(); // 根据选择的科室重新加载医生数据
        },

        // 选择医生
        selectDoctor(doctor) {
            this.selectedDoctor = doctor;
        },

        // 选择日期
        async selectDate(date) {
            this.selectedDate = date;
            this.selectedTime = null; // 重置时间选择
            await this.loadTimeSlots(date);
        },

        // 选择时间
        selectTime(time) {
            this.selectedTime = time;
        },

        // 使用当前用户信息
        useCurrentUserInfo() {
            try {
                console.log('开始使用当前用户信息，当前用户:', this.currentUser);
                console.log('当前用户详细信息:', JSON.stringify(this.currentUser, null, 2));
                
                // 双重检查：先检查Vue实例中的currentUser，再检查AuthManager
                if (this.currentUser && this.currentUser.phone) {
                    // 强制更新表单数据
                    this.appointmentInfo.patientName = this.currentUser.name || this.currentUser.realName || '';
                    this.appointmentInfo.phone = this.currentUser.phone || '';
                    
                    // 强制触发Vue的响应式更新
                    this.$forceUpdate();
                    
                    this.showSuccess('已使用当前登录用户信息');
                    console.log('使用用户信息后的表单数据:', this.appointmentInfo);
                    console.log('当前步骤表单字段:', {
                        patientName: this.appointmentInfo.patientName,
                        phone: this.appointmentInfo.phone
                    });
                } else if (typeof AuthManager !== 'undefined') {
                    // 如果Vue实例中没有用户信息或手机号为空，尝试从AuthManager获取
                     const user = AuthManager.getUser();
                     console.log('从AuthManager获取的用户信息:', JSON.stringify(user, null, 2));
                     
                     if (user) {
                         // 确保用户信息结构正确
                         this.currentUser = {
                             id: user.id,
                             username: user.username,
                             name: user.realName || user.username || '用户',
                             realName: user.realName || user.username || '用户', // 确保realName不为null
                             phone: user.phone || '', // 确保手机号字段正确映射
                             role: user.role
                         };
                         
                         // 检查手机号是否为空，如果为空则显示明确的错误信息
                         if (!this.currentUser.phone) {
                             this.showError('当前用户手机号为空，无法自动填充。请在个人中心补充手机号或手动输入。');
                             console.warn('用户手机号为空，无法填充:', this.currentUser);
                             return;
                         }
                         
                         // 强制更新表单数据
                         this.appointmentInfo.patientName = this.currentUser.name || '';
                         this.appointmentInfo.phone = this.currentUser.phone || '';
                         
                         // 强制触发Vue的响应式更新
                         this.$forceUpdate();
                         
                         this.showSuccess('已使用当前登录用户信息');
                         console.log('使用用户信息后的表单数据:', this.appointmentInfo);
                         console.log('当前步骤表单字段:', {
                             patientName: this.appointmentInfo.patientName,
                             phone: this.appointmentInfo.phone
                         });
                     } else {
                         this.showError('未找到用户信息，请重新登录');
                     }
                } else {
                    this.showError('认证系统未加载，请刷新页面重试');
                }
            } catch (error) {
                console.error('使用当前用户信息失败:', error);
                this.showError('获取用户信息失败，请刷新页面重试');
            }
        },

        // 根据手机号查询病人信息
        async searchPatientByPhone() {
            if (!this.appointmentInfo.phone) {
                this.showError('请输入手机号');
                return;
            }

            try {
                const response = await fetch(`/api/patients/phone/${this.appointmentInfo.phone}`);
                if (!response.ok) {
                    throw new Error('查询失败');
                }

                const patient = await response.json();
                
                if (patient && patient.id) {
                    // 如果找到病人信息，自动填充
                    this.appointmentInfo.patientName = patient.name || '';
                    this.appointmentInfo.idCard = patient.idCard || '';
                    this.showSuccess('已找到病人信息，已自动填充');
                } else {
                    this.showInfo('未找到该手机号对应的病人信息，请填写完整信息');
                }
            } catch (error) {
                console.error('查询病人信息失败:', error);
                this.showError('查询病人信息失败，请手动填写信息');
            }
        },

        // 退出登录
        logout(event) {
            event.preventDefault();
            if (confirm('确定要退出登录吗？')) {
                localStorage.removeItem('hospital_user');
                sessionStorage.removeItem('hospital_user');
                window.location.href = '/login';
            }
        },

        // 跳转到指定步骤
        goToStep(step) {
            this.currentStep = step;
            window.scrollTo(0, 0);
        },

        // 确认预约
        async confirmAppointment() {
            this.loading = true;

            try {
                // 验证必填信息
                if (!this.appointmentInfo.patientName) {
                    throw new Error('请输入就诊人姓名');
                }

                if (!this.appointmentInfo.phone) {
                    throw new Error('请输入手机号');
                }

                // 构建预约数据（使用新的支持病人信息自动创建的API）
                const appointmentData = {
                    doctorId: this.selectedDoctor?.id,
                    registerTime: `${this.selectedDate}T${this.selectedTime.split('-')[0]}:00`,
                    symptoms: this.appointmentInfo.symptoms || '',
                    notes: '',
                    // 病人信息
                    patientName: this.appointmentInfo.patientName,
                    patientPhone: this.appointmentInfo.phone,
                    patientIdCard: this.appointmentInfo.idCard || '', // 身份证号改为可选
                    patientGender: null // 可以根据需要添加性别选择
                };

                // 调用新的API保存预约信息（支持病人信息自动创建）
                const response = await fetch('/api/registration-orders/with-patient', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(appointmentData)
                });

                if (!response.ok) {
                    const errorData = await response.json().catch(() => ({}));
                    throw new Error(errorData.message || '预约失败，请稍后重试');
                }

                const result = await response.json();

                this.showSuccess(`预约成功！您的预约号为：${result.appointmentNo}`);

                // 3秒后跳转到预约列表
                setTimeout(() => {
                    window.location.href = '/appointments';
                }, 3000);

            } catch (error) {
                this.showError(error.message || '预约失败，请稍后重试');
            } finally {
                this.loading = false;
            }
        },

        // 显示成功消息
        showSuccess(message) {
            if (this.messageTimer) {
                clearTimeout(this.messageTimer);
            }

            this.successMessage = message;
            this.errorMessage = '';
            this.showSuccessMessage = true;
            this.showErrorMessage = false;

            this.messageTimer = setTimeout(() => {
                this.hideMessages();
            }, 5000);
        },

        // 显示错误消息
        showError(message) {
            if (this.messageTimer) {
                clearTimeout(this.messageTimer);
            }

            this.errorMessage = message;
            this.successMessage = '';
            this.showErrorMessage = true;
            this.showSuccessMessage = false;

            this.messageTimer = setTimeout(() => {
                this.hideMessages();
            }, 5000);
        },

        // 显示信息消息
        showInfo(message) {
            this.showSuccess(message);
        },

        // 隐藏消息
        hideMessages() {
            this.showErrorMessage = false;
            this.showSuccessMessage = false;
            this.errorMessage = '';
            this.successMessage = '';

            if (this.messageTimer) {
                clearTimeout(this.messageTimer);
                this.messageTimer = null;
            }
        }
    }
}).mount('#appointment-app');