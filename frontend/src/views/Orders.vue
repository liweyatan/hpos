<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3 class="text-primary"><i class="fas fa-list-alt me-2"></i>我的预约</h3>
      <div>
        <a href="/appointments" class="btn btn-outline-primary me-2" @click.prevent="loadOrders"><i class="fas fa-sync-alt me-1"></i>刷新</a>
        <a href="/register" class="btn btn-primary" @click.prevent="$router.push('/register')"><i class="fas fa-plus me-2"></i>新建预约</a>
      </div>
    </div>
    <div class="row mb-4">
      <div class="col-12">
        <div class="card">
          <div class="card-body">
            <div class="d-flex flex-wrap gap-2">
              <button v-for="f in statusFilters" :key="f.value" class="btn" :class="statusFilter === f.value ? 'btn-primary' : 'btn-outline-primary'" @click="statusFilter = f.value">{{ f.label }}</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="loading" class="text-center py-5"><div class="spinner-border text-primary"></div></div>
    <div v-else-if="filtered.length === 0" class="text-center py-5">
      <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
      <h5 class="text-muted">暂无预约记录</h5>
      <p class="text-muted">您还没有任何预约记录，快去预约吧！</p>
      <a href="/register" class="btn btn-primary" @click.prevent="$router.push('/register')">立即预约</a>
    </div>
    <div v-else>
      <div v-for="a in filtered" :key="a.id" class="card mb-3 appointment-item">
        <div class="card-header appointment-header">
          <div class="d-flex justify-content-between align-items-center">
            <strong>GH{{ a.id }}</strong>
            <span class="badge" :class="getStatusClass(a.status)">{{ getStatusText(a.status) }}</span>
          </div>
        </div>
        <div class="card-body appointment-content">
          <div class="row">
            <div class="col-md-6">
              <p><i class="fas fa-hospital me-2 text-muted"></i><strong>科室：</strong>{{ a.departmentName }}</p>
              <p><i class="fas fa-user-md me-2 text-muted"></i><strong>医生：</strong>{{ a.doctorName }}</p>
            </div>
            <div class="col-md-6">
              <p><i class="fas fa-clock me-2 text-muted"></i><strong>时间：</strong>{{ a.registerTime }}</p>
              <p><i class="fas fa-user me-2 text-muted"></i><strong>就诊人：</strong>{{ a.patientName }}</p>
              <p v-if="a.symptoms"><i class="fas fa-notes-medical me-2 text-muted"></i><strong>症状：</strong>{{ a.symptoms }}</p>
            </div>
          </div>
        </div>
        <div class="card-footer appointment-actions">
          <div class="d-flex justify-content-between align-items-center">
            <span class="text-muted small">{{ a.appointmentNo || '' }}</span>
            <div>
              <button v-if="a.status === 'PENDING' || a.status === 'CONFIRMED'" class="btn btn-sm btn-outline-danger" @click="cancelAppointment(a.id)">
                <i class="fas fa-times me-1"></i>取消
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getOrders, cancelOrder } from '@/api/index.js'

export default {
  name: 'OrdersView',
  data() {
    return {
      orders: [], loading: false, statusFilter: '',
      statusFilters: [
        { value: '', label: '全部' },
        { value: 'PENDING', label: '待处理' },
        { value: 'CONFIRMED', label: '已确认' },
        { value: 'COMPLETED', label: '已完成' },
        { value: 'CANCELLED', label: '已取消' }
      ]
    }
  },
  computed: {
    patientId() { return localStorage.getItem('patientId') },
    filtered() {
      if (!this.statusFilter) return this.orders
      return this.orders.filter(o => o.status === this.statusFilter)
    },
  },
  methods: {
    getStatusClass(s) {
      const map = { PENDING: 'bg-warning', CONFIRMED: 'bg-primary', COMPLETED: 'bg-info', CANCELLED: 'bg-secondary' }
      return map[s] || 'bg-secondary'
    },
    getStatusText(s) {
      const map = { PENDING: '待处理', CONFIRMED: '已确认', COMPLETED: '已完成', CANCELLED: '已取消' }
      return map[s] || s
    },
    async loadOrders() {
      this.loading = true
      try {
        if (!this.patientId) { this.orders = []; return }
        const res = await getOrders(this.patientId)
        this.orders = res.data || res || []
      }
      catch (e) { console.error(e) }
      finally { this.loading = false }
    },
    async cancelAppointment(id) {
      if (!confirm('确定要取消这个预约吗？')) return
      try { await cancelOrder(id); await this.loadOrders() }
      catch (e) { alert(e.message || '取消失败') }
    },
  },
  mounted() { this.loadOrders() },
}
</script>

<style scoped>
.appointment-item { border-left: 4px solid #1e90ff; }
.appointment-item:hover { border-color: #1e90ff; box-shadow: 0 5px 15px rgba(30, 144, 255, 0.1); }
.appointment-header { background: linear-gradient(135deg, #f0f8ff 0%, #e0f0ff 100%); border-bottom: 1px solid #e0f0ff; }
.appointment-content { padding: 20px; }
.appointment-actions { border-top: 1px solid #f0f0f0; background-color: #fafafa; }
</style>
