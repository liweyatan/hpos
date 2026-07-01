<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>预约挂号 - ${empty systemName ? '智慧医院管理系统' : systemName}</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Vue.js Production Build -->
    <script src="https://unpkg.com/vue@3/dist/vue.global.prod.js"></script>
    <!-- 自定义样式 -->
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/appointment.css" rel="stylesheet">
</head>
<body>
<!-- 公共导航栏 -->
<%@ include file="./common/header.jsp" %>

<!-- 消息提示 -->
<div id="appointment-app">
    <!-- 错误消息 -->
    <div v-if="showErrorMessage" class="container mt-3">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i>
            <strong>{{ errorMessage }}</strong>
            <button type="button" class="btn-close" @click="hideMessages"></button>
        </div>
    </div>

    <!-- 成功消息 -->
    <div v-if="showSuccessMessage" class="container mt-3">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i>
            <strong>{{ successMessage }}</strong>
            <button type="button" class="btn-close" @click="hideMessages"></button>
        </div>
    </div>

    <!-- 页面内容 -->
    <div class="container mt-4">
        <div class="row">
            <!-- 左侧内容区 -->
            <div class="col-lg-8">
                <!-- 步骤指示器 -->
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="steps">
                            <div class="step-item" :class="{ active: currentStep === 1, completed: currentStep > 1 }">
                                <div class="step-number">1</div>
                                <div class="step-label">选择科室</div>
                            </div>
                            <div class="step-item" :class="{ active: currentStep === 2, completed: currentStep > 2 }">
                                <div class="step-number">2</div>
                                <div class="step-label">选择医生</div>
                            </div>
                            <div class="step-item" :class="{ active: currentStep === 3, completed: currentStep > 3 }">
                                <div class="step-number">3</div>
                                <div class="step-label">选择时间</div>
                            </div>
                            <div class="step-item" :class="{ active: currentStep === 4 }">
                                <div class="step-number">4</div>
                                <div class="step-label">确认信息</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 步骤1：选择科室 -->
                <div v-if="currentStep === 1" class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-hospital me-2"></i>选择科室
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div v-for="dept in departments" :key="dept.id" class="col-md-6 mb-3">
                                <div class="department-card"
                                     :class="{ selected: selectedDepartment && selectedDepartment.id === dept.id }"
                                     @click="selectDepartment(dept)">
                                    <div class="department-icon">
                                        <i class="fas fa-stethoscope"></i>
                                    </div>
                                    <div class="department-info">
                                        <h6>{{ dept.name }}</h6>
                                        <p class="text-muted small">{{ dept.description || '专业医疗科室' }}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 步骤2：选择医生 -->
                <div v-if="currentStep === 2" class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-user-md me-2"></i>选择医生
                        </h5>
                    </div>
                    <div class="card-body">
                        <!-- 筛选条件 -->
                        <div class="row mb-4">
                            <div class="col-md-4">
                                <label class="form-label">职称</label>
                                <select class="form-select" v-model="filter.title">
                                    <option value="">全部职称</option>
                                    <option value="主任医师">主任医师</option>
                                    <option value="副主任医师">副主任医师</option>
                                    <option value="主治医师">主治医师</option>
                                    <option value="住院医师">住院医师</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">挂号费</label>
                                <select class="form-select" v-model="filter.feeRange">
                                    <option value="">全部费用</option>
                                    <option value="0-50">0-50元</option>
                                    <option value="50-100">50-100元</option>
                                    <option value="100-200">100-200元</option>
                                    <option value="200-">200元以上</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">评分</label>
                                <select class="form-select" v-model="filter.rating">
                                    <option value="0">全部评分</option>
                                    <option value="4">4星以上</option>
                                    <option value="3">3星以上</option>
                                </select>
                            </div>
                        </div>

                        <!-- 医生列表 -->
                        <div class="row">
                            <div v-for="doctor in filteredDoctors" :key="doctor.id" class="col-md-6 mb-3">
                                <div class="doctor-card"
                                     :class="{ selected: selectedDoctor && selectedDoctor.id === doctor.id }"
                                     @click="selectDoctor(doctor)">
                                    <div class="doctor-avatar">
                                        <i class="fas fa-user-md"></i>
                                    </div>
                                    <div class="doctor-info">
                                        <h6>{{ doctor.name }}</h6>
                                        <p class="text-muted small mb-1">{{ doctor.title }} | {{ doctor.departmentName
                                            }}</p>
                                        <p class="text-primary small mb-1">
                                            <i class="fas fa-star text-warning"></i> {{ doctor.rating || '5.0' }}
                                            <span class="ms-2">
                                                <i class="fas fa-money-bill-wave"></i> {{ doctor.fee || 50 }}元
                                            </span>
                                        </p>
                                        <p class="text-muted small">{{ doctor.specialty || '专业医疗' }}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 步骤3：选择时间 -->
                <div v-if="currentStep === 3" class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-calendar me-2"></i>选择时间
                        </h5>
                    </div>
                    <div class="card-body">
                        <!-- 日期选择 -->
                        <div class="mb-4">
                            <h6 class="mb-3">选择日期</h6>
                            <div class="row">
                                <div v-for="date in availableDates" :key="date.date" class="col-md-3 mb-2">
                                    <div class="date-card"
                                         :class="{ selected: selectedDate === date.date, unavailable: !date.available }"
                                         @click="date.available && selectDate(date.date)">
                                        <div class="date-day">{{ date.day }}</div>
                                        <div class="date-number">{{ date.formattedDate }}</div>
                                        <div v-if="!date.available" class="date-status">不可约</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 时间段选择 -->
                        <div>
                            <h6 class="mb-3">选择时间段</h6>
                            <div class="row">
                                <div v-for="slot in timeSlots" :key="slot.time" class="col-md-4 mb-2">
                                    <div class="time-slot"
                                         :class="{ selected: selectedTime === slot.time, unavailable: !slot.available }"
                                         @click="slot.available && selectTime(slot.time)">
                                        {{ slot.time }}
                                        <span v-if="!slot.available" class="badge bg-secondary ms-1">已满</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 步骤4：填写就诊人信息 -->
                <div v-if="currentStep === 4" class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-user me-2"></i>填写就诊人信息
                        </h5>
                    </div>
                    <div class="card-body">
                        <!-- 预约信息汇总 -->
                        <div class="alert alert-info mb-4">
                            <h6 class="alert-heading">预约信息汇总</h6>
                            <div class="row">
                                <div class="col-md-6">
                                    <small><strong>科室：</strong>{{ selectedDepartment?.name }}</small><br>
                                    <small><strong>医生：</strong>{{ selectedDoctor?.name }}</small>
                                </div>
                                <div class="col-md-6">
                                    <small><strong>时间：</strong>{{ selectedDate }} {{ selectedTime }}</small><br>
                                    <small><strong>费用：</strong>{{ totalFee }}元</small>
                                </div>
                            </div>
                        </div>

                        <!-- 就诊人信息表单 -->
                        <h6 class="text-primary mb-3">就诊人信息</h6>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label class="form-label">就诊人姓名 <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" v-model="appointmentInfo.patientName"
                                           placeholder="请输入就诊人姓名" required>
                                    <div class="form-text">
                                        <i class="fas fa-info-circle me-1"></i>
                                        当前登录用户：{{ currentUser?.name || '未登录' }}
                                        <button type="button" class="btn btn-sm btn-outline-primary ms-2"
                                                @click="useCurrentUserInfo">
                                            使用当前用户信息
                                        </button>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">手机号 <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <input type="tel" class="form-control" v-model="appointmentInfo.phone"
                                               placeholder="请输入手机号" required>
                                        <button class="btn btn-outline-secondary" type="button" 
                                                @click="searchPatientByPhone"
                                                :disabled="!appointmentInfo.phone">
                                            <i class="fas fa-search"></i> 查询
                                        </button>
                                    </div>
                                    <div class="form-text">
                                        <i class="fas fa-info-circle me-1"></i>
                                        输入手机号后点击查询按钮，可自动填充已存在的病人信息
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label class="form-label">身份证号</label>
                                    <input type="text" class="form-control" v-model="appointmentInfo.idCard"
                                           placeholder="请输入身份证号">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">症状描述</label>
                                    <textarea class="form-control" v-model="appointmentInfo.symptoms" rows="3"
                                              placeholder="请简要描述您的症状..."></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 右侧信息栏 -->
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header bg-light">
                        <h6 class="mb-0">
                            <i class="fas fa-info-circle me-2"></i>预约信息
                        </h6>
                    </div>
                    <div class="card-body">
                        <div v-if="selectedDepartment" class="info-section">
                            <h6>科室信息</h6>
                            <p class="small text-muted">{{ selectedDepartment.name }}</p>
                            <p v-if="selectedDepartment.description" class="small">{{ selectedDepartment.description
                                }}</p>
                        </div>

                        <div v-if="selectedDoctor" class="info-section mt-3">
                            <h6>医生信息</h6>
                            <p class="small text-muted">{{ selectedDoctor.name }} - {{ selectedDoctor.title }}</p>
                            <p v-if="selectedDoctor.specialty" class="small">{{ selectedDoctor.specialty }}</p>
                            <p class="small text-primary">
                                <i class="fas fa-star text-warning"></i> {{ selectedDoctor.rating || '5.0' }}
                                <span class="ms-2">
                                    <i class="fas fa-money-bill-wave"></i> {{ selectedDoctor.fee || 50 }}元
                                </span>
                            </p>
                        </div>

                        <div v-if="selectedDate && selectedTime" class="info-section mt-3">
                            <h6>预约时间</h6>
                            <p class="small text-muted">{{ selectedDate }} {{ selectedTime }}</p>
                        </div>

                        <div class="info-section mt-3 border-top pt-3">
                            <div class="d-flex justify-content-between">
                                <span>挂号费：</span>
                                <span>{{ selectedDoctor?.fee || 50 }}元</span>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span>服务费：</span>
                                <span>5元</span>
                            </div>
                            <div class="d-flex justify-content-between border-top pt-2">
                                <strong>总计：</strong>
                                <strong class="text-primary">{{ totalFee }}元</strong>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 导航按钮 -->
                <div class="card mt-3">
                    <div class="card-body">
                        <div class="d-grid gap-2">
                            <button v-if="currentStep > 1" class="btn btn-outline-secondary"
                                    @click="goToStep(currentStep - 1)">
                                <i class="fas fa-arrow-left me-2"></i>上一步
                            </button>
                            <button v-if="currentStep < 4" class="btn btn-primary" @click="goToStep(currentStep + 1)"
                                    :disabled="!canProceed">
                                下一步 <i class="fas fa-arrow-right ms-2"></i>
                            </button>
                            <button v-if="currentStep === 4" class="btn btn-success" @click="confirmAppointment"
                                    :disabled="loading">
                                <i v-if="loading" class="fas fa-spinner fa-spin me-2"></i>
                                <i v-else class="fas fa-check me-2"></i>
                                {{ loading ? '提交中...' : '确认预约' }}
                            </button>
                            <a href="/" class="btn btn-outline-secondary">
                                <i class="fas fa-times me-2"></i>取消
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div>

</div>
    <!-- 页脚 -->
<footer class="bg-dark text-white py-4 mt-auto">
    <div class="container text-center">
            <p class="mb-0">
                <i class="fas fa-copyright me-1"></i>2025
                <strong>${empty systemName ? '智慧医院管理系统' : systemName}</strong>
                版权所有
            </p>
        </div>
    </footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- 通用认证状态管理 -->
<script src="${pageContext.request.contextPath}/static/js/auth.js"></script>
<!-- 管理员权限检查 -->
<script src="${pageContext.request.contextPath}/static/js/admin-check.js"></script>

<script>
    // 确保AuthManager加载完成后再加载预约页面脚本
    document.addEventListener('DOMContentLoaded', function() {
        // 检查AuthManager是否已定义
        if (typeof AuthManager !== 'undefined') {
            // 如果用户未登录，重定向到登录页面
            if (!AuthManager.getUser()) {
                console.log('用户未登录，重定向到登录页面');
                window.location.href = '${pageContext.request.contextPath}/login.jsp';
                return;
            }
            
            console.log('用户已登录，加载预约页面脚本');
            // 加载预约页面脚本
            const script = document.createElement('script');
            script.src = '${pageContext.request.contextPath}/static/js/appointment.js';
            document.body.appendChild(script);
        } else {
            // 如果AuthManager未定义，等待加载后再检查
            const checkAuthManager = setInterval(function() {
                if (typeof AuthManager !== 'undefined') {
                    clearInterval(checkAuthManager);
                    if (!AuthManager.getUser()) {
                        console.log('用户未登录，重定向到登录页面');
                        window.location.href = '${pageContext.request.contextPath}/login.jsp';
                        return;
                    }
                    
                    console.log('用户已登录，加载预约页面脚本');
                    const script = document.createElement('script');
                    script.src = '${pageContext.request.contextPath}/static/js/appointment.js';
                    document.body.appendChild(script);
                }
            }, 100);
            
            // 10秒后超时
            setTimeout(function() {
                clearInterval(checkAuthManager);
                console.error('AuthManager加载超时');
            }, 10000);
        }
    });
</script>
</body>
</html>