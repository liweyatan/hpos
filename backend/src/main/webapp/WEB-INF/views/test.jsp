<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>系统测试页面</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://unpkg.com/vue@3/dist/vue.global.prod.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<!-- 公共导航栏 -->
<%@ include file="./common/header.jsp" %>

<div class="container mt-5" id="test-app">
    <h2 class="mb-4">智慧医院管理系统 - 测试页面</h2>

    <div class="card">
        <div class="card-header">
            <h5>API接口测试</h5>
        </div>
        <div class="card-body">
            <div class="mb-3">
                <button class="btn btn-primary me-2" @click="testPatientAPI">测试患者API</button>
                <button class="btn btn-success me-2" @click="testAppointmentAPI">测试预约API</button>
                <button class="btn btn-info me-2" @click="testAuthAPI">测试认证API</button>
                <button class="btn btn-warning" @click="clearResults">清空结果</button>
            </div>

            <div v-if="loading" class="alert alert-info">
                <i class="fas fa-spinner fa-spin"></i> 正在测试...
            </div>

            <div v-if="testResults.length > 0">
                <h6>测试结果：</h6>
                <div v-for="(result, index) in testResults" :key="index" class="alert"
                     :class="result.success ? 'alert-success' : 'alert-danger'">
                    <strong>{{ result.api }}</strong>: {{ result.message }}
                </div>
            </div>
        </div>
    </div>

    <div class="card mt-4">
        <div class="card-header">
            <h5>快速导航</h5>
        </div>
        <div class="card-body">
            <div class="d-grid gap-2 d-md-flex">
                <a href="/" class="btn btn-outline-primary">首页</a>
                <a href="/appointments" class="btn btn-outline-success">我的预约</a>
                <a href="/appointment" class="btn btn-outline-info">预约挂号</a>
                <a href="/login" class="btn btn-outline-warning">登录页面</a>
            </div>
        </div>
    </div>
</div>

<script>
    const TestApp = {
        data() {
            return {
                loading: false,
                testResults: []
            }
        },

        methods: {
            async testPatientAPI() {
                this.loading = true;
                try {
                    // 测试根据手机号查找患者
                    const response = await fetch('/api/patients/phone/13800138001');
                    const data = await response.json();

                    this.testResults.push({
                        api: '患者API（手机号查询）',
                        success: response.ok,
                        message: response.ok ? `成功获取患者: ${data.name || '未知'}` : `失败: ${response.status}`
                    });

                } catch (error) {
                    this.testResults.push({
                        api: '患者API（手机号查询）',
                        success: false,
                        message: `错误: ${error.message}`
                    });
                }
                this.loading = false;
            },

            async testAppointmentAPI() {
                this.loading = true;
                try {
                    // 测试获取患者预约（使用测试患者ID 1）
                    const response = await fetch('/api/registration-orders/patient/1');
                    const data = await response.json();

                    this.testResults.push({
                        api: '预约API',
                        success: response.ok,
                        message: response.ok ? `成功获取 ${data.length} 条预约记录` : `失败: ${response.status}`
                    });

                } catch (error) {
                    this.testResults.push({
                        api: '预约API',
                        success: false,
                        message: `错误: ${error.message}`
                    });
                }
                this.loading = false;
            },

            async testAuthAPI() {
                this.loading = true;
                try {
                    // 测试认证API
                    const response = await fetch('/api/auth/check');
                    const data = await response.json();

                    this.testResults.push({
                        api: '认证API',
                        success: response.ok,
                        message: response.ok ? '认证服务正常' : `失败: ${response.status}`
                    });

                } catch (error) {
                    this.testResults.push({
                        api: '认证API',
                        success: false,
                        message: `错误: ${error.message}`
                    });
                }
                this.loading = false;
            },

            clearResults() {
                this.testResults = [];
            }
        }
    };

    document.addEventListener('DOMContentLoaded', function () {
        if (typeof Vue !== 'undefined') {
            const app = Vue.createApp(TestApp);
            app.mount('#test-app');
        }
    });
</script>
</body>
</html>