<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-12">
        <h3 class="text-primary mb-4"><i class="fas fa-building me-2"></i>科室介绍</h3>
        <div class="row mb-4">
          <div class="col-md-6">
            <div class="input-group">
              <input type="text" class="form-control" id="searchInput" v-model="searchKeyword" placeholder="搜索科室...">
              <button class="btn btn-primary" @click="searchDepartments"><i class="fas fa-search"></i></button>
            </div>
          </div>
        </div>
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status"><span class="visually-hidden">加载中...</span></div>
          <p class="mt-2 text-muted">正在加载科室数据...</p>
        </div>
        <div v-else class="row" id="departmentsList">
          <div v-for="dept in filteredDepartments" :key="dept.id" class="col-md-6 col-lg-4 mb-4">
            <div class="card h-100">
              <div class="card-body text-center">
                <div class="feature-icon mb-3">
                  <i :class="getDeptIcon(dept.name)" class="fa-2x text-primary"></i>
                </div>
                <h5 class="card-title text-primary">{{ dept.name }}</h5>
                <p class="card-text text-muted">{{ dept.description || '暂无科室描述' }}</p>
                <div class="department-info">
                  <div class="mb-2"><small class="text-muted"><i class="fas fa-user-md me-1"></i>负责人：{{ dept.director || '暂无' }}</small></div>
                  <div class="mb-2"><small class="text-muted"><i class="fas fa-map-marker-alt me-1"></i>位置：{{ dept.location || '暂无' }}</small></div>
                  <div class="mb-2"><small class="text-muted"><i class="fas fa-phone me-1"></i>电话：{{ dept.phone || '暂无' }}</small></div>
                </div>
              </div>
              <div class="card-footer bg-transparent text-center">
                <a href="/register" class="btn btn-primary btn-sm" @click.prevent="$router.push('/register')">
                  <i class="fas fa-calendar-plus me-1"></i>预约挂号
                </a>
              </div>
            </div>
          </div>
          <div v-if="filteredDepartments.length === 0" class="col-12 text-center py-5">
            <i class="fas fa-search fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">暂无科室数据</h5>
            <p class="text-muted">请联系管理员添加科室信息</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getDepartments } from '@/api/index.js'

export default {
  name: 'DepartmentList',
  data() {
    return { departments: [], searchKeyword: '', loading: false }
  },
  computed: {
    filteredDepartments() {
      if (!this.searchKeyword) return this.departments
      const term = this.searchKeyword.toLowerCase()
      return this.departments.filter(d => d.name.toLowerCase().includes(term) || (d.description && d.description.toLowerCase().includes(term)) || (d.director && d.director.toLowerCase().includes(term)))
    }
  },
  methods: {
    getDeptIcon(name) {
      const icons = { '内科': 'fas fa-heartbeat', '外科': 'fas fa-syringe', '全科': 'fas fa-user-md', '儿科': 'fas fa-child', '妇产科': 'fas fa-baby', '眼科': 'fas fa-eye', '口腔科': 'fas fa-tooth' }
      return icons[name] || 'fas fa-hospital'
    },
    async loadDepartments() {
      this.loading = true
      try { const res = await getDepartments(); this.departments = res.data || res || [] } catch (e) { console.error(e) }
      finally { this.loading = false }
    },
    searchDepartments() { /* 通过计算属性自动过滤 */ }
  },
  mounted() { this.loadDepartments() }
}
</script>
