<!-- src/components/Footer.vue -->
<template>
  <footer class="hospital-footer mt-5">
    <div class="container">
      <div class="row py-4">
        <div class="col-md-4">
          <h5>智慧医院</h5>
          <p class="text-muted">
            提供便捷的在线挂号服务，让您就医更轻松。
          </p>
        </div>

        <div class="col-md-4">
          <h5>联系我们</h5>
          <ul class="list-unstyled">
            <li><i class="bi bi-geo-alt me-2"></i> 北京市朝阳区医院路123号</li>
            <li><i class="bi bi-telephone me-2"></i> 010-12345678</li>
            <li><i class="bi bi-envelope me-2"></i> info@hospital.com</li>
          </ul>
        </div>

        <div class="col-md-4">
          <h5>服务时间</h5>
          <ul class="list-unstyled">
            <li>周一至周五: 8:00-17:00</li>
            <li>周六: 8:00-12:00</li>
            <li>周日: 急诊24小时开放</li>
          </ul>
        </div>
      </div>

      <div class="border-top pt-3 text-center text-muted">
        <p>&copy; 2023 智慧医院管理系统. 毕业设计项目.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.hospital-footer {
  background-color: #f8f9fa;
  border-top: 1px solid #dee2e6;
}
</style>