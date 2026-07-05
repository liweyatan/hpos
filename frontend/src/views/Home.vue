<template>
  <div>
    <!-- 医院Banner -->
    <div class="hospital-banner">
      <div class="container">
        <div class="row align-items-center min-vh-50">
          <div class="col-md-6">
            <h1 class="display-4 fw-bold text-white mb-3">专业医疗 用心服务</h1>
            <p class="lead text-white mb-4">提供便捷、高效的在线挂号服务，让您就医更轻松</p>
            <a href="/register" class="btn btn-light btn-lg" @click.prevent="$router.push('/register')">
              <i class="fas fa-plus-circle me-2"></i>立即预约
            </a>
          </div>
          <div class="col-md-6">
            <div class="row text-center text-white mt-4 mt-md-0">
              <div class="col-4">
                <div class="stat-item">
                  <i class="fas fa-user-md fa-2x mb-2"></i>
                  <h3 class="fw-bold mb-0">50+</h3>
                  <small class="opacity-75">专业医生</small>
                </div>
              </div>
              <div class="col-4">
                <div class="stat-item">
                  <i class="fas fa-hospital fa-2x mb-2"></i>
                  <h3 class="fw-bold mb-0">6</h3>
                  <small class="opacity-75">重点科室</small>
                </div>
              </div>
              <div class="col-4">
                <div class="stat-item">
                  <i class="fas fa-smile fa-2x mb-2"></i>
                  <h3 class="fw-bold mb-0">10000+</h3>
                  <small class="opacity-75">服务患者</small>
                </div>
              </div>
            </div>
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
              <a href="/register" class="btn btn-primary" @click.prevent="$router.push('/register')">开始预约</a>
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
              <a href="/appointments" class="btn btn-primary" @click.prevent="$router.push('/orders')">查看记录</a>
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
              <a href="/departments" class="btn btn-primary" @click.prevent="$router.push('/departments')">查看科室</a>
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
          <i class="fas fa-copyright me-1"></i>2026
          <strong>智慧医院管理系统</strong> 版权所有
        </p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
.stat-item {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px 10px;
  backdrop-filter: blur(5px);
}
.stat-item h3 {
  color: #fff;
}
.stat-item small {
  color: rgba(255, 255, 255, 0.75);
}
</style>
