<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的预约 - ${empty systemName ? '智慧医院管理系统' : systemName}</title>

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
    <!-- 页面标题和操作区 -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="text-primary">
            <i class="fas fa-list-alt me-2"></i>我的预约
        </h3>
        <div>
            <a href="/appointments" class="btn btn-outline-primary me-2">
                <i class="fas fa-sync-alt me-1"></i>刷新
            </a>
            <a href="/appointment" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i>新建预约
            </a>
        </div>
    </div>

    <!-- 消息提示 -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-message mt-3">
            <div class="d-flex align-items-center">
                <i class="fas fa-exclamation-triangle me-3"></i>
                <div class="flex-grow-1">
                    <strong>错误提示</strong>
                    <p class="mb-0">${errorMessage}</p>
                    </div>
                <button type="button" class="btn-close"
                        onclick="this.parentElement.parentElement.style.display='none'"></button>
                </div>
        </div>
    </c:if>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-message mt-3">
            <div class="d-flex align-items-center">
                <i class="fas fa-check-circle me-3"></i>
                <div class="flex-grow-1">
                    <strong>操作成功</strong>
                    <p class="mb-0">${successMessage}</p>
                </div>
                <button type="button" class="btn-close"
                        onclick="this.parentElement.parentElement.style.display='none'"></button>
            </div>
        </div>
    </c:if>

    <!-- 状态筛选器 -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex flex-wrap gap-2">
                        <a href="/appointments"
                           class="btn ${empty param.status ? 'btn-primary' : 'btn-outline-primary'}">
                            全部 (${not empty appointments ? appointments.size() : 0})
                        </a>
                        <a href="/appointments?status=PENDING"
                           class="btn ${param.status == 'PENDING' ? 'btn-primary' : 'btn-outline-primary'}">
                            待处理 (${pendingCount})
                        </a>
                        <a href="/appointments?status=CONFIRMED"
                           class="btn ${param.status == 'CONFIRMED' ? 'btn-primary' : 'btn-outline-primary'}">
                            已确认 (${confirmedCount})
                        </a>
                        <a href="/appointments?status=COMPLETED"
                           class="btn ${param.status == 'COMPLETED' ? 'btn-primary' : 'btn-outline-primary'}">
                            已完成 (${completedCount})
                        </a>
                        <a href="/appointments?status=CANCELLED"
                           class="btn ${param.status == 'CANCELLED' ? 'btn-primary' : 'btn-outline-primary'}">
                            已取消 (${cancelledCount})
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 预约列表 -->
    <c:choose>
        <c:when test="${not empty appointments and appointments.size() > 0}">
            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                            <tr>
                                <th>预约号</th>
                                <th>科室</th>
                                <th>医生</th>
                                <th>预约时间</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="appointment" items="${appointments}">
                                <tr>
                                    <td><strong>GH${appointment.id}</strong></td>
                                    <td>${appointment.departmentName}</td>
                                    <td>${appointment.doctorName}</td>
                                    <td>
                                            ${appointment.registerTime}
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:toUpperCase(appointment.status) == 'PENDING'}">
                                                <span class="badge bg-warning">待处理</span>
                                            </c:when>
                                            <c:when test="${fn:toUpperCase(appointment.status) == 'CONFIRMED'}">
                                                <span class="badge bg-success">已确认</span>
                                            </c:when>
                                            <c:when test="${fn:toUpperCase(appointment.status) == 'COMPLETED'}">
                                                <span class="badge bg-info">已完成</span>
                                            </c:when>
                                            <c:when test="${fn:toUpperCase(appointment.status) == 'CANCELLED'}">
                                                <span class="badge bg-secondary">已取消</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${appointment.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary me-1"
                                                onclick="viewAppointmentDetail(${appointment.id}, '${fn:replace(appointment.departmentName, "'", "\\'")}', '${fn:replace(appointment.doctorName, "'", "\\'")}', '${fn:replace(appointment.patientName, "'", "\\'")}', '${fn:replace(appointment.symptoms, "'", "\\'")}', '${appointment.status}')">
                                            <i class="fas fa-eye"></i> 查看
                                        </button>
                                        <c:if test="${fn:toUpperCase(appointment.status) == 'PENDING' or fn:toUpperCase(appointment.status) == 'CONFIRMED'}">
                                            <form action="/appointments/cancel" method="post"
                                                  style="display: inline-block;">
                                                <input type="hidden" name="id" value="${appointment.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger"
                                                        onclick="return confirm('确定要取消这个预约吗？')">
                                                    <i class="fas fa-times"></i> 取消
                                                </button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!-- 空状态提示 -->
            <div class="text-center py-5">
                <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
                <h5 class="text-muted">暂无预约记录</h5>
                <p class="text-muted">您还没有任何预约记录，快去预约吧！</p>
                <a href="/appointment" class="btn btn-primary">立即预约</a>
            </div>
        </c:otherwise>
    </c:choose>
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

<!-- 简单JavaScript -->
<script>
    // 查看预约详情
    function viewAppointmentDetail(id, department, doctor, patient, symptoms, status) {
        const statusText = getStatusText(status);
        const detail = `预约号: GH${id}
科室: ${department}
医生: ${doctor}
就诊人: ${patient}
症状: ${symptoms || '无'}
状态: ${statusText}`;
        alert(detail);
    }

    // 获取状态文本
    function getStatusText(status) {
        switch (status) {
            case 'pending':
                return '待处理';
            case 'confirmed':
                return '已确认';
            case 'completed':
                return '已完成';
            case 'cancelled':
                return '已取消';
            default:
                return status;
        }
    }

    // 自动隐藏消息提示
    setTimeout(function () {
        const messages = document.querySelectorAll('.alert-message');
        messages.forEach(function (message) {
            message.style.display = 'none';
        });
    }, 5000);
</script>
</body>
</html>