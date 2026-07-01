<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty systemName ? '智慧医院管理系统' : systemName}</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- 自定义样式 -->
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
</head>
<body>
<!-- 公共导航栏 -->
<%@ include file="./common/header.jsp" %>

<!-- 医院Banner -->
<div class="hospital-banner">
    <div class="container">
        <div class="row align-items-center min-vh-50">
            <div class="col-md-6">
                <h1 class="display-4 fw-bold text-white mb-3">
                    ${empty welcomeMessage ? '专业医疗 用心服务' : welcomeMessage}
                </h1>
                <p class="lead text-white mb-4">
                    提供便捷、高效的在线挂号服务，让您就医更轻松
                </p>
                <a href="/appointment" class="btn btn-light btn-lg">
                    <i class="fas fa-plus-circle me-2"></i>立即预约
                </a>
            </div>
        </div>
    </div>
</div>

<!-- 主要功能区域 -->
<div class="container mt-5">
        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card h-100 text-center">
                    <div class="card-body">
                        <div class="feature-icon">
                            <i class="fas fa-calendar-check fa-3x text-primary"></i>
                        </div>
                        <h5 class="card-title mt-3">在线预约</h5>
                        <p class="card-text">选择科室医生，轻松完成挂号</p>
                        <a href="/appointment" class="btn btn-primary">开始预约</a>
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-4">
                <div class="card h-100 text-center">
                    <div class="card-body">
                        <div class="feature-icon">
                            <i class="fas fa-list-alt fa-3x text-primary"></i>
                        </div>
                        <h5 class="card-title mt-3">我的预约</h5>
                        <p class="card-text">查看和管理您的预约记录</p>
                        <a href="/appointments" class="btn btn-primary">查看记录</a>
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-4">
                <div class="card h-100 text-center">
                    <div class="card-body">
                        <div class="feature-icon">
                            <i class="fas fa-building fa-3x text-primary"></i>
                        </div>
                        <h5 class="card-title mt-3">科室介绍</h5>
                        <p class="card-text">了解各科室特色和医生信息</p>
                        <a href="/department/list" class="btn btn-primary">查看科室</a>
                    </div>
                </div>
            </div>
        </div>
</div>

<!-- 快速链接 -->
<div class="bg-light py-5 mt-5">
    <div class="container">
        <div class="row">
            <div class="col-md-3 text-center">
                <i class="fas fa-phone fa-2x text-primary mb-3"></i>
                <h6>咨询电话</h6>
                <p class="text-muted">400-123-4567</p>
                </div>
            <div class="col-md-3 text-center">
                <i class="fas fa-clock fa-2x text-primary mb-3"></i>
                <h6>服务时间</h6>
                <p class="text-muted">周一至周日 8:00-17:00</p>
            </div>
            <div class="col-md-3 text-center">
                <i class="fas fa-map-marker-alt fa-2x text-primary mb-3"></i>
                <h6>医院地址</h6>
                <p class="text-muted">xxxx</p>
            </div>
            <div class="col-md-3 text-center">
                <i class="fas fa-info-circle fa-2x text-primary mb-3"></i>
                <h6>关于我们</h6>
                <p class="text-muted">专业医疗团队为您服务</p>
                </div>
            </div>
        </div>
    </div>

    <!-- 页脚 -->
<footer class="bg-dark text-white py-4">
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
</body>
</html>