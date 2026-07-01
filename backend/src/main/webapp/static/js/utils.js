/**
 * 通用工具函数库
 * 提供项目中常用的JavaScript工具函数
 */

// 消息管理模块
const MessageUtils = {
    timer: null,

    /**
     * 显示消息
     * @param {Object} context Vue实例上下文
     * @param {string} type 消息类型：'error' | 'success' | 'info'
     * @param {string} message 消息内容
     * @param {number} duration 显示时长（毫秒，默认5000）
     */
    showMessage(context, type, message, duration = 5000) {
        // 清除之前的定时器
        if (this.timer) {
            clearTimeout(this.timer);
            this.timer = null;
        }

        // 设置消息内容
        if (type === 'error') {
            context.errorMessage = message;
            context.successMessage = '';
            context.showErrorMessage = true;
            context.showSuccessMessage = false;
            console.error('错误消息:', message);
        } else if (type === 'success' || type === 'info') {
            context.successMessage = message;
            context.errorMessage = '';
            context.showSuccessMessage = true;
            context.showErrorMessage = false;
            console.log('成功消息:', message);
        }

        // 设置定时器自动隐藏消息
        if (duration > 0) {
            this.timer = setTimeout(() => {
                this.hideMessages(context);
            }, duration);
        }
    },

    /**
     * 显示错误消息
     */
    showError(context, message, duration = 5000) {
        this.showMessage(context, 'error', message, duration);
    },

    /**
     * 显示成功消息
     */
    showSuccess(context, message, duration = 5000) {
        this.showMessage(context, 'success', message, duration);
    },

    /**
     * 显示信息消息
     */
    showInfo(context, message, duration = 3000) {
        this.showMessage(context, 'info', message, duration);
    },

    /**
     * 隐藏所有消息
     */
    hideMessages(context) {
        if (this.timer) {
            clearTimeout(this.timer);
            this.timer = null;
        }
        context.showErrorMessage = false;
        context.showSuccessMessage = false;
        context.errorMessage = '';
        context.successMessage = '';
    }
};

// 验证工具模块
const ValidationUtils = {
    /**
     * 验证手机号格式
     */
    isValidPhone(phone) {
        if (!phone) return false;
        return /^1[3-9]\d{9}$/.test(phone);
    },

    /**
     * 验证邮箱格式
     */
    isValidEmail(email) {
        if (!email) return false;
        return /^[A-Za-z0-9+_.-]+@(.+)$/.test(email);
    },

    /**
     * 验证用户名格式
     */
    isValidUsername(username) {
        if (!username) return false;
        return /^[a-zA-Z][a-zA-Z0-9_]{2,19}$/.test(username);
    },

    /**
     * 验证密码强度
     */
    isValidPassword(password) {
        if (!password) return false;
        return password.length >= 6 &&
            /[a-zA-Z]/.test(password) &&
            /\d/.test(password);
    },

    /**
     * 验证身份证号格式
     */
    isValidIdCard(idCard) {
        if (!idCard) return false;
        return /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(idCard) ||
            /^[1-9]\d{7}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}$/.test(idCard);
    },

    /**
     * 验证必填字段
     */
    validateRequired(fieldValue, fieldName) {
        if (!fieldValue || fieldValue.trim().length === 0) {
            throw new Error(fieldName + '不能为空');
        }
        return true;
    }
};

// 存储工具模块
const StorageUtils = {
    /**
     * 获取用户信息
     */
    getUser() {
        try {
            const userData = localStorage.getItem('hospital_user') || sessionStorage.getItem('hospital_user');
            return userData ? JSON.parse(userData) : null;
        } catch (error) {
            console.error('获取用户信息失败:', error);
            return null;
        }
    },

    /**
     * 保存用户信息
     */
    saveUser(user, rememberMe = false) {
        try {
            const storage = rememberMe ? localStorage : sessionStorage;
            storage.setItem('hospital_user', JSON.stringify(user));
            return true;
        } catch (error) {
            console.error('保存用户信息失败:', error);
            return false;
        }
    },

    /**
     * 清除用户信息
     */
    clearUser() {
        try {
            localStorage.removeItem('hospital_user');
            sessionStorage.removeItem('hospital_user');
            return true;
        } catch (error) {
            console.error('清除用户信息失败:', error);
            return false;
        }
    }
};

// API请求工具模块
const ApiUtils = {
    /**
     * 发送API请求
     */
    async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'same-origin'
        };

        const config = {...defaultOptions, ...options};

        try {
            const response = await fetch(url, config);

            if (!response.ok) {
                throw new Error(`HTTP错误! 状态码: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API请求失败:', error);
            throw error;
        }
    },

    /**
     * 发送POST请求
     */
    async post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    /**
     * 发送GET请求
     */
    async get(url) {
        return this.request(url, {method: 'GET'});
    }
};

// 导出所有工具模块
window.HospitalUtils = {
    Message: MessageUtils,
    Validation: ValidationUtils,
    Storage: StorageUtils,
    Api: ApiUtils
};

console.log('医院系统工具库已加载');