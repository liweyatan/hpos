<!-- src/components/Header.vue -->
<template>
  <header class="hospital-header">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="#">
          <i class="bi bi-hospital me-2"></i>
          智慧医院挂号系统
        </a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link active" href="#">首页</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">科室介绍</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">医生团队</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">我的挂号</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="welcome-banner text-center py-4">
      <div class="container">
        <h1 class="display-6">在线预约挂号</h1>
        <p class="lead">便捷、快速、省时省力</p>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Header'
}
</script>

<style scoped>
.hospital-header {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.welcome-banner {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-bottom: 1px solid #dee2e6;
}
</style>