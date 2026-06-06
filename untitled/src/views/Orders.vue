<template>
  <div class="orders-view py-4">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <div class="card shadow-sm border-0 rounded-3">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center flex-wrap gap-2">
              <h5 class="mb-0"><i class="bi bi-list-ul me-2 text-primary"></i>我的挂号记录</h5>
              <div class="d-flex gap-2">
                <select class="form-select form-select-sm" style="width:auto" v-model="statusFilter" @change="filterOrders">
                  <option value="">全部状态</option>
                  <option value="0">待支付</option>
                  <option value="1">已支付</option>
                  <option value="2">已取消</option>
                  <option value="3">已就诊</option>
                </select>
                <button class="btn btn-sm btn-outline-primary" @click="loadOrders" :disabled="loading">
                  <span v-if="loading" class="spinner-border spinner-border-sm"></span>
                  <i v-else class="bi bi-arrow-clockwise"></i> 刷新
                </button>
              </div>
            </div>

            <div class="card-body p-0">
              <div v-if="loading && filteredList.length === 0" class="text-center py-5">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2 text-muted">加载中...</p>
              </div>
              <div v-else-if="filteredList.length === 0" class="text-center py-5 text-muted">
                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                <p>暂无挂号记录</p>
                <router-link to="/register" class="btn btn-primary btn-sm">去挂号</router-link>
              </div>
              <div v-else class="table-responsive">
                <table class="table table-hover mb-0">
                  <thead class="table-light">
                    <tr>
                      <th>订单号</th>
                      <th>患者</th>
                      <th>科室</th>
                      <th>医生</th>
                      <th>就诊时间</th>
                      <th>费用</th>
                      <th>状态</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="reg in filteredList" :key="reg.id">
                      <td><small class="text-muted">{{ reg.orderNo }}</small></td>
                      <td>{{ reg.patientName }}</td>
                      <td>{{ reg.deptName }}</td>
                      <td>{{ reg.doctorName }} <small class="text-muted">({{ reg.doctorTitle }})</small></td>
                      <td>{{ reg.workDate }} {{ reg.periodText }}</td>
                      <td class="fw-bold text-primary">¥{{ reg.fee }}</td>
                      <td><span :class="'badge ' + statusBadge(reg.status)">{{ reg.statusText }}</span></td>
                      <td>
                        <button v-if="reg.status === 0" class="btn btn-sm btn-outline-danger"
                          @click="confirmCancel(reg)">取消</button>
                        <button v-else-if="reg.status === 1" class="btn btn-sm btn-outline-success"
                          @click="alert('功能开发中，敬请期待')">去支付</button>
                        <span v-else class="text-muted small">--</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="filteredList.length > 0" class="card-footer bg-white text-muted small text-end py-2">
              共 {{ filteredList.length }} 条记录
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
      orders: [],
      loading: false,
      statusFilter: '',
    }
  },
  computed: {
    patientId() {
      return parseInt(localStorage.getItem('patientId') || '1')
    },
    filteredList() {
      if (!this.statusFilter) return this.orders
      return this.orders.filter(o => o.status === parseInt(this.statusFilter))
    },
  },
  methods: {
    statusBadge(status) {
      const map = { 0: 'bg-warning text-dark', 1: 'bg-info text-dark', 2: 'bg-secondary', 3: 'bg-success' }
      return map[status] || 'bg-secondary'
    },
    async loadOrders() {
      this.loading = true
      try {
        this.orders = await getOrders(this.patientId)
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    async confirmCancel(reg) {
      if (!confirm(`确定取消挂号「${reg.doctorName} ${reg.workDate} ${reg.periodText}」？`)) return
      try {
        await cancelOrder(reg.id, this.patientId)
        await this.loadOrders()
      } catch (e) {
        alert(e.message || '取消失败')
      }
    },
  },
  mounted() {
    this.loadOrders()
  },
}
</script>
