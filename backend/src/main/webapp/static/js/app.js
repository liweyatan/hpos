/**
 * 智慧医院管理系统 - 修复版JS文件
 * 修复消息框不自动消失的问题，应用蓝白主题
 */

(function() {
    'use strict';

    /**
     * 智慧医院管理系统Vue应用
     */
    window.hospitalApp = {
        // 应用数据
        data() {
            return {
                // 用户信息
                currentUser: null,
                isLoggedIn: false,

                // 页面状态
                loading: false,
                errorMessage: '',
                successMessage: '',
                showErrorMessage: false,
                showSuccessMessage: false,

                // 消息框定时器
                messageTimer: null,

                // 通用数据
                departments: [],
                doctors: [],
                appointments: [],

                // 系统信息
                systemName: '智慧医院管理系统',
                welcomeMessage: '欢迎使用智慧医院管理系统'
            }
        },

        // 应用挂载时执行
        mounted() {
            console.log('智慧医院管理系统初始化...');
            this.initApp();
        },

        // 组件销毁时清理
        beforeUnmount() {
            // 清理定时器
            if (this.messageTimer) {
                clearTimeout(this.messageTimer);
            }
        },

        // 方法定义
        methods: {
            /**
             * 初始化应用程序
             */
            initApp() {
                console.log('系统初始化...');
                this.checkLoginStatus();
                this.loadCommonData();

                // 页面加载完成后执行
                this.$nextTick(() => {
                    console.log('页面渲染完成');
                });
            },

            /**
             * 检查用户登录状态
             */
            checkLoginStatus() {
                try {
                    // 使用统一的认证管理器检查登录状态
                    if (typeof AuthManager !== 'undefined' && AuthManager.isLoggedIn()) {
                        this.currentUser = AuthManager.getUser();
                        this.isLoggedIn = true;
                        console.log('用户已登录:', this.currentUser);
                    } else {
                        console.log('用户未登录');
                    }
                } catch (error) {
                    console.error('检查登录状态失败:', error);
                }
            },

            /**
             * 加载通用数据（科室、医生等）
             */
            async loadCommonData() {
                try {
                    this.loading = true;
                    console.log('开始加载通用数据...');

                    // 模拟数据加载 - 延迟500ms
                    await new Promise(resolve => setTimeout(resolve, 500));

                    // 模拟科室数据
                    this.departments = [
                        {
                            id: 1,
                            name: '内科',
                            description: '负责消化、呼吸、心血管等系统疾病的诊疗',
                            icon: 'fas fa-heartbeat'
                        },
                        {
                            id: 2,
                            name: '外科',
                            description: '负责手术及外伤处理',
                            icon: 'fas fa-user-md'
                        },
                        {
                            id: 3,
                            name: '全科',
                            description: '常见病、多发病的初步诊疗和健康管理',
                            icon: 'fas fa-stethoscope'
                        },
                        {
                            id: 4,
                            name: '儿科',
                            description: '儿童疾病诊疗与健康管理',
                            icon: 'fas fa-child'
                        },
                        {
                            id: 5,
                            name: '妇产科',
                            description: '女性疾病诊疗与生育健康',
                            icon: 'fas fa-female'
                        },
                        {
                            id: 6,
                            name: '眼科',
                            description: '眼部疾病诊疗与视力保健',
                            icon: 'fas fa-eye'
                        }
                    ];

                    // 模拟医生数据
                    this.doctors = [
                        { id: 1, name: '张医生', departmentId: 1, available: true },
                        { id: 2, name: '李医生', departmentId: 2, available: true },
                        { id: 3, name: '王医生', departmentId: 3, available: true }
                    ];

                    console.log('数据加载完成:', this.departments.length, '个科室');

                } catch (error) {
                    console.error('加载数据失败:', error);
                    this.showError('数据加载失败，请刷新页面重试');
                } finally {
                    this.loading = false;
                }
            },

            /**
             * 显示错误消息 - 修复：确保自动消失
             */
            showError(message) {
                // 清除之前的定时器
                if (this.messageTimer) {
                    clearTimeout(this.messageTimer);
                }

                this.errorMessage = message;
                this.successMessage = '';
                this.showErrorMessage = true;
                this.showSuccessMessage = false;

                console.error('错误消息:', message);

                // 5秒后自动消失 - 修复：使用箭头函数保持this指向
                this.messageTimer = setTimeout(() => {
                    this.hideMessages();
                }, 5000);
            },

            /**
             * 显示成功消息 - 修复：确保自动消失
             */
            showSuccess(message) {
                // 清除之前的定时器
                if (this.messageTimer) {
                    clearTimeout(this.messageTimer);
                }

                this.successMessage = message;
                this.errorMessage = '';
                this.showSuccessMessage = true;
                this.showErrorMessage = false;

                console.log('成功消息:', message);

                // 5秒后自动消失
                this.messageTimer = setTimeout(() => {
                    this.hideMessages();
                }, 5000);
            },

            /**
             * 显示信息消息
             */
            showInfo(message) {
                // 清除之前的定时器
                if (this.messageTimer) {
                    clearTimeout(this.messageTimer);
                }

                this.successMessage = message;
                this.errorMessage = '';
                this.showSuccessMessage = true;
                this.showErrorMessage = false;

                console.log('信息消息:', message);

                // 5秒后自动消失
                this.messageTimer = setTimeout(() => {
                    this.hideMessages();
                }, 5000);
            },

            /**
             * 隐藏所有消息
             */
            hideMessages() {
                this.showErrorMessage = false;
                this.showSuccessMessage = false;
                this.errorMessage = '';
                this.successMessage = '';

                if (this.messageTimer) {
                    clearTimeout(this.messageTimer);
                    this.messageTimer = null;
                }

                console.log('消息已隐藏');
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
             * 格式化日期
             */
            formatDate(date) {
                if (!date) return '';
                try {
                    const d = new Date(date);
                    return d.toLocaleDateString('zh-CN');
                } catch (error) {
                    return date;
                }
            },

            /**
             * 导航到页面
             */
            navigateTo(path) {
                console.log('导航到:', path);
                window.location.href = path;
            },

            /**
             * 模拟测试消息
             */
            testSuccessMessage() {
                this.showSuccess('这是一个测试成功消息，将在5秒后自动消失！');
            },

            /**
             * 模拟测试错误
             */
            testErrorMessage() {
                this.showError('这是一个测试错误消息，将在5秒后自动消失！');
            },

            /**
             * 模拟测试信息
             */
            testInfoMessage() {
                this.showInfo('这是一个测试信息消息，将在5秒后自动消失！');
            }
        },

        // 计算属性
        computed: {
            /**
             * 获取可预约的医生列表
             */
            availableDoctors() {
                return this.doctors.filter(doctor => doctor.available);
            },

            /**
             * 按科室分组的医生
             */
            doctorsByDepartment() {
                const grouped = {};
                this.departments.forEach(dept => {
                    grouped[dept.id] = this.doctors.filter(doctor =>
                        doctor.departmentId === dept.id && doctor.available
                    );
                });
                return grouped;
            },

            /**
             * 获取系统名称
             */
            getSystemName() {
                return this.systemName || '智慧医院管理系统';
            }
        }
    };

    /**
     * 通用工具函数
     */
    window.HospitalUtils = {
        /**
         * 验证手机号格式
         */
        validatePhone(phone) {
            const regex = /^1[3-9]\d{9}$/;
            return regex.test(phone);
        },

        /**
         * 验证身份证格式
         */
        validateIdCard(idCard) {
            const regex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            return regex.test(idCard);
        },

        /**
         * 验证邮箱格式
         */
        validateEmail(email) {
            const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return regex.test(email);
        },

        /**
         * 防抖函数
         */
        debounce(func, wait) {
            let timeout;
            return function executedFunction(...args) {
                const later = () => {
                    clearTimeout(timeout);
                    func(...args);
                };
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
            };
        },

        /**
         * 节流函数
         */
        throttle(func, wait) {
            let timeout = null;
            return function executedFunction(...args) {
                if (timeout === null) {
                    func(...args);
                    timeout = setTimeout(() => {
                        timeout = null;
                    }, wait);
                }
            };
        },

        /**
         * 显示加载提示
         */
        showLoading(message = '加载中...') {
            // 可以在这里实现加载动画
            const loader = document.createElement('div');
            loader.className = 'loader-overlay';
            loader.innerHTML = `
                <div class="loader-content">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">${message}</span>
                    </div>
                    <p class="mt-2">${message}</p>
                </div>
            `;
            document.body.appendChild(loader);
        },

        /**
         * 隐藏加载提示
         */
        hideLoading() {
            const loader = document.querySelector('.loader-overlay');
            if (loader) {
                loader.remove();
            }
        }
    };

    console.log('智慧医院管理系统JS加载完成 - 蓝白主题修复版');
})();

/**
 * Vue应用初始化
 */
window.initVueApp = function() {
    document.addEventListener('DOMContentLoaded', function() {
        // 检查Vue是否加载成功
        if (typeof Vue === 'undefined') {
            console.error('Vue.js 加载失败，请检查网络连接');
            return;
        }

        // 检查hospitalApp是否定义
        if (typeof hospitalApp === 'undefined') {
            console.error('hospitalApp 未定义，请检查app.js文件');
            return;
        }

        try {
            // 创建Vue应用实例
            const app = Vue.createApp({
                ...hospitalApp,
                data() {
                    return {
                        ...hospitalApp.data()
                    }
                },
                mounted() {
                    // 调用初始化方法
                    if (typeof this.initApp === 'function') {
                        this.initApp();
                    }

                    // 测试消息 - 页面加载完成后显示欢迎消息
                    setTimeout(() => {
                        this.showInfo('欢迎使用智慧医院管理系统！');
                    }, 1000);
                },
                methods: {
                    ...hospitalApp.methods
                }
            });

            // 挂载应用
            app.mount('#hospital-app');
            console.log('Vue应用挂载成功 - 蓝白主题');

        } catch (error) {
            console.error('Vue应用初始化失败:', error);
        }
    });
};

// 自动初始化
initVueApp();