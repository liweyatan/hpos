<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty systemName ? '医院管理系统' : systemName} - 管理员后台</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- 自定义样式 -->
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/admin.css" rel="stylesheet">
</head>
<body>
<!-- 公共导航栏 -->
<%@ include file="./common/header.jsp" %>

<!-- 管理员Banner -->
<div class="hospital-banner">
    <div class="container">
        <div class="row align-items-center min-vh-50">
            <div class="col-md-6">
                <h1 class="display-4 fw-bold text-white mb-3">
                    管理员后台管理系统
                </h1>
                <p class="lead text-white mb-4">
                    全面管理医院各项业务数据，提供高效的管理工具
                </p>
            </div>
        </div>
    </div>
</div>

<!-- 管理功能导航 -->
<div class="container mt-5">
    <div class="row">
        <div class="col-12">
            <ul class="nav nav-tabs" id="adminTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="department-tab" data-bs-toggle="tab"
                            data-bs-target="#department" type="button" role="tab" aria-controls="department"
                            aria-selected="true">
                        <i class="fas fa-building me-2"></i>科室管理
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="doctor-tab" data-bs-toggle="tab"
                            data-bs-target="#doctor" type="button" role="tab" aria-controls="doctor"
                            aria-selected="false">
                        <i class="fas fa-user-md me-2"></i>医生管理
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="appointment-tab" data-bs-toggle="tab"
                            data-bs-target="#appointment" type="button" role="tab" aria-controls="appointment"
                            aria-selected="false">
                        <i class="fas fa-calendar-check me-2"></i>预约管理
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="user-tab" data-bs-toggle="tab"
                            data-bs-target="#user" type="button" role="tab" aria-controls="user"
                            aria-selected="false">
                        <i class="fas fa-users me-2"></i>用户管理
                    </button>
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- 管理内容区域 -->
<div class="container mt-4">
    <div class="tab-content" id="adminTabContent">

        <!-- 科室管理 -->
        <div class="tab-pane fade show active" id="department" role="tabpanel" aria-labelledby="department-tab">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0"><i class="fas fa-building me-2"></i>科室管理</h5>
                    <button class="btn btn-primary" onclick="showDepartmentModal()">
                        <i class="fas fa-plus me-2"></i>新增科室
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="departmentTable">
                            <thead>
                            <tr>
                                <th>科室名称</th>
                                <th>负责人</th>
                                <th>联系电话</th>
                                <th>科室位置</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="departmentTableBody">
                            <!-- 数据将通过JavaScript动态加载 -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- 医生管理 -->
        <div class="tab-pane fade" id="doctor" role="tabpanel" aria-labelledby="doctor-tab">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0"><i class="fas fa-user-md me-2"></i>医生管理</h5>
                    <button class="btn btn-primary" onclick="showDoctorModal()">
                        <i class="fas fa-plus me-2"></i>新增医生
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="doctorTable">
                            <thead>
                            <tr>
                                <th>医生姓名</th>
                                <th>职称</th>
                                <th>所属科室</th>
                                <th>专长</th>
                                <th>最大接诊数</th>
                                <th>当前挂号数</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="doctorTableBody">
                            <!-- 数据将通过JavaScript动态加载 -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- 预约管理 -->
        <div class="tab-pane fade" id="appointment" role="tabpanel" aria-labelledby="appointment-tab">
            <div class="card">
                <div class="card-header">
                    <h5 class="card-title mb-0"><i class="fas fa-calendar-check me-2"></i>预约管理</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="appointmentTable">
                            <thead>
                            <tr>
                                <th>预约号</th>
                                <th>患者姓名</th>
                                <th>医生姓名</th>
                                <th>科室</th>
                                <th>预约时间</th>
                                <th>症状描述</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="appointmentTableBody">
                            <!-- 数据将通过JavaScript动态加载 -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- 用户管理 -->
        <div class="tab-pane fade" id="user" role="tabpanel" aria-labelledby="user-tab">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0"><i class="fas fa-users me-2"></i>用户管理</h5>
                    <button class="btn btn-primary" onclick="showUserModal()">
                        <i class="fas fa-plus me-2"></i>新增用户
                    </button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover" id="userTable">
                            <thead>
                            <tr>
                                <th>用户名</th>
                                <th>真实姓名</th>
                                <th>邮箱</th>
                                <th>手机号</th>
                                <th>角色</th>
                                <th>状态</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="userTableBody">
                            <!-- 数据将通过JavaScript动态加载 -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 模态框区域 -->
<div class="modal fade" id="departmentModal" tabindex="-1" aria-labelledby="departmentModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="departmentModalLabel">科室信息</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="departmentForm">
                    <input type="hidden" id="departmentId" name="id">
                    <div class="mb-3">
                        <label for="departmentName" class="form-label">科室名称</label>
                        <input type="text" class="form-control" id="departmentName" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label for="departmentDescription" class="form-label">科室描述</label>
                        <textarea class="form-control" id="departmentDescription" name="description"
                                  rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="departmentDirector" class="form-label">负责人</label>
                        <input type="text" class="form-control" id="departmentDirector" name="director">
                    </div>
                    <div class="mb-3">
                        <label for="departmentPhone" class="form-label">联系电话</label>
                        <input type="tel" class="form-control" id="departmentPhone" name="phone">
                    </div>
                    <div class="mb-3">
                        <label for="departmentLocation" class="form-label">科室位置</label>
                        <input type="text" class="form-control" id="departmentLocation" name="location">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">状态</label>
                        <div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="active" id="departmentActive"
                                       value="1" checked>
                                <label class="form-check-label" for="departmentActive">启用</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="active" id="departmentInactive"
                                       value="0">
                                <label class="form-check-label" for="departmentInactive">禁用</label>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="saveDepartment()">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 其他模态框类似，此处省略... -->

<!-- 页脚 -->
<footer class="bg-dark text-white py-4 mt-5">
    <div class="container text-center">
        <p class="mb-0">
            <i class="fas fa-copyright me-1"></i>2025
            <strong>${empty systemName ? '医院管理系统' : systemName}</strong>
            版权所有
        </p>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- 通用认证状态管理 -->
<script src="${pageContext.request.contextPath}/static/js/auth.js"></script>
<!-- 自定义JavaScript -->
<script src="${pageContext.request.contextPath}/static/js/admin.js"></script>
</body>
</html>