<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>科室介绍 - ${empty systemName ? '智慧医院管理系统' : systemName}</title>

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

<!-- 页面内容 -->
<div class="container mt-5">
    <div class="row">
        <div class="col-12">
            <h3 class="text-primary mb-4">
                <i class="fas fa-building me-2"></i>科室介绍
            </h3>

            <!-- 搜索栏 -->
            <div class="row mb-4">
                <div class="col-md-6">
                        <div class="input-group">
                            <input type="text" class="form-control" id="searchInput" placeholder="搜索科室...">
                            <button class="btn btn-primary" onclick="searchDepartments()">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>

            <!-- 科室列表 -->
            <div class="row" id="departmentsList">
                <!-- 科室卡片将通过JavaScript动态生成 -->
                <div class="col-12 text-center py-5" id="loadingMessage">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">加载中...</span>
                    </div>
                    <p class="mt-2 text-muted">正在加载科室数据...</p>
                </div>
            </div>
        </div>
    </div>
</div>

    <!-- 页脚 -->
<footer class="bg-dark text-white py-4 mt-5">
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

<!-- 科室列表JavaScript -->
<script src="${pageContext.request.contextPath}/static/js/department-list.js"></script>
</body>
</html>