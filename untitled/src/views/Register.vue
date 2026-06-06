<template>
  <div class="register-view py-4">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-9">
          <!-- 步骤指示器 -->
          <div class="steps d-flex justify-content-center mb-4">
            <div class="step" :class="{ active: step === 1, done: step > 1 }">
              <div class="step-circle">1</div>
              <span>选择科室医生</span>
            </div>
            <div class="step-connector"></div>
            <div class="step" :class="{ active: step === 2, done: step > 2 }">
              <div class="step-circle">2</div>
              <span>选择时段</span>
            </div>
            <div class="step-connector"></div>
            <div class="step" :class="{ active: step === 3, done: step > 3 }">
              <div class="step-circle">3</div>
              <span>确认挂号</span>
            </div>
          </div>

          <!-- 步骤1：选择科室 & 医生 -->
          <div v-show="step === 1" class="card shadow-sm border-0 rounded-3 mb-4">
            <div class="card-header bg-white py-3">
              <h5 class="mb-0"><i class="bi bi-hospital me-2 text-primary"></i>选择科室与医生</h5>
            </div>
            <div class="card-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label fw-medium">科室</label>
                  <div class="dept-grid">
                    <button v-for="dept in departments" :key="dept.id"
                      class="btn btn-outline-primary dept-btn"
                      :class="{ active: formData.departmentId === dept.id }"
                      @click="selectDept(dept)">
                      <i class="bi bi-building me-1"></i>{{ dept.deptName }}
                    </button>
                  </div>
                  <div v-if="departments.length === 0" class="text-center py-4">
                    <div class="spinner-border text-primary" role="status">
                      <span class="visually-hidden">加载中...</span>
                    </div>
                    <p class="mt-2 text-muted small">正在加载科室列表...</p>
                  </div>
                </div>

                <div class="col-md-6">
                  <label class="form-label fw-medium">医生</label>
                  <div v-if="!formData.departmentId" class="text-center py-5 text-muted">
                    <i class="bi bi-arrow-left fs-2"></i>
                    <p class="mt-2">请先选择科室</p>
                  </div>
                  <div v-else-if="doctorsLoading" class="text-center py-4">
                    <div class="spinner-border text-primary" role="status">
                      <span class="visually-hidden">加载中...</span>
                    </div>
                  </div>
                  <div v-else-if="doctors.length === 0" class="text-center py-5 text-muted">
                    <i class="bi bi-emoji-frown fs-2"></i>
                    <p class="mt-2">该科室暂无医生</p>
                  </div>
                  <div v-else class="doctor-list">
                    <div v-for="doc in doctors" :key="doc.id"
                      class="doctor-card"
                      :class="{ selected: formData.doctorId === doc.id }"
                      @click="selectDoctor(doc)">
                      <div class="d-flex align-items-center">
                        <div class="doctor-avatar">
                          <i class="bi bi-person-circle fs-3"></i>
                        </div>
                        <div class="ms-2 flex-grow-1">
                          <div class="fw-bold">{{ doc.realName }}</div>
                          <span class="badge bg-info bg-opacity-10 text-info">{{ doc.title }}</span>
                        </div>
                        <i v-if="formData.doctorId === doc.id" class="bi bi-check-circle-fill text-primary fs-5"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="text-end mt-3">
                <button class="btn btn-primary" :disabled="!formData.doctorId" @click="step = 2">
                  下一步 <i class="bi bi-arrow-right"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- 步骤2：选择时段 -->
          <div v-show="step === 2" class="card shadow-sm border-0 rounded-3 mb-4">
            <div class="card-header bg-white py-3">
              <h5 class="mb-0">
                <i class="bi bi-calendar3 me-2 text-primary"></i>选择预约时段
                <small class="text-muted ms-2">
                  {{ selectedDoctorName }} | {{ selectedDeptName }}
                </small>
              </h5>
            </div>
            <div class="card-body">
              <div v-if="scheduleLoading" class="text-center py-5">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2 text-muted">正在加载排班信息...</p>
              </div>
              <div v-else-if="scheduleList.length === 0" class="text-center py-5 text-muted">
                <i class="bi bi-calendar-x fs-1"></i>
                <p class="mt-2">该医生暂无可用排班</p>
              </div>
              <div v-else>
                <div class="row g-3">
                  <div v-for="slot in scheduleList" :key="slot.sourceId" class="col-md-4 col-sm-6">
                    <div class="slot-card"
                      :class="{
                        selected: formData.sourceId === slot.sourceId,
                        low: slot.availableCount > 0 && slot.availableCount <= 5,
                        full: slot.availableCount <= 0
                      }"
                      @click="selectSlot(slot)">
                      <div class="slot-date">{{ formatDate(slot.workDate) }}</div>
                      <div class="slot-period">{{ slot.periodText }}</div>
                      <div class="slot-fee">¥{{ slot.fee }}</div>
                      <div class="slot-avail">
                        <span v-if="slot.availableCount > 0" class="badge"
                          :class="slot.availableCount <= 5 ? 'bg-warning text-dark' : 'bg-success'">
                          余 {{ slot.availableCount }} 号
                        </span>
                        <span v-else class="badge bg-secondary">已满</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="d-flex justify-content-between mt-3">
                <button class="btn btn-outline-secondary" @click="step = 1">
                  <i class="bi bi-arrow-left"></i> 上一步
                </button>
                <button class="btn btn-primary" :disabled="!formData.sourceId" @click="step = 3">
                  下一步 <i class="bi bi-arrow-right"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- 步骤3：确认信息 + 填写患者信息 -->
          <div v-show="step === 3" class="card shadow-sm border-0 rounded-3 mb-4">
            <div class="card-header bg-white py-3">
              <h5 class="mb-0"><i class="bi bi-pencil-square me-2 text-primary"></i>确认挂号信息</h5>
            </div>
            <div class="card-body">
              <!-- 挂号信息预览 -->
              <div class="bg-light rounded-3 p-3 mb-4">
                <div class="row g-2 text-sm">
                  <div class="col-md-4"><span class="text-muted">科室：</span>{{ selectedDeptName }}</div>
                  <div class="col-md-4"><span class="text-muted">医生：</span>{{ selectedDoctorName }} ({{ selectedDoctorTitle }})</div>
                  <div class="col-md-4"><span class="text-muted">日期：</span>{{ formData.workDate }} {{ formData.periodText }}</div>
                  <div class="col-md-4"><span class="text-muted">挂号费：</span><strong class="text-primary">¥{{ selectedFee }}</strong></div>
                </div>
              </div>

              <h6 class="fw-bold mb-3"><i class="bi bi-person me-1"></i>患者信息</h6>
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label">患者姓名 <span class="text-danger">*</span></label>
                  <input type="text" class="form-control" v-model="formData.patientName" placeholder="请输入姓名" required>
                </div>
                <div class="col-md-6">
                  <label class="form-label">身份证号 <span class="text-danger">*</span></label>
                  <input type="text" class="form-control" v-model="formData.idCard" placeholder="请输入身份证号" required>
                </div>
                <div class="col-md-6">
                  <label class="form-label">手机号 <span class="text-danger">*</span></label>
                  <div class="input-group">
                    <input type="tel" class="form-control" v-model="formData.phone" placeholder="请输入手机号" required @blur="lookupPatient">
                    <button class="btn btn-outline-primary" type="button" @click="lookupPatient" :disabled="!formData.phone">
                      <i class="bi bi-search"></i>
                    </button>
                  </div>
                  <small class="text-muted">输入手机号后点击查询可自动填充</small>
                </div>
                <div class="col-md-6">
                  <label class="form-label">性别 <span class="text-danger">*</span></label>
                  <select class="form-select" v-model="formData.gender" required>
                    <option value="">请选择</option>
                    <option value="1">男</option>
                    <option value="2">女</option>
                  </select>
                </div>
              </div>

              <div class="d-flex justify-content-between mt-4">
                <button class="btn btn-outline-secondary" @click="step = 2">
                  <i class="bi bi-arrow-left"></i> 上一步
                </button>
                <button class="btn btn-success btn-lg" :disabled="submitting" @click="submitOrder">
                  <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
                  {{ submitting ? '提交中...' : '确认挂号' }}
                </button>
              </div>
            </div>
          </div>

          <!-- 成功提示 -->
          <div v-if="showSuccess" class="alert alert-success text-center py-4 rounded-3">
            <i class="bi bi-check-circle-fill fs-1 d-block mb-2"></i>
            <h5>挂号成功！</h5>
            <p class="mb-1">订单号：<strong>{{ orderNo }}</strong></p>
            <p class="small text-muted">请记住订单号，就诊时需出示</p>
            <div class="mt-3">
              <button class="btn btn-outline-primary me-2" @click="goOrders">查看我的挂号</button>
              <button class="btn btn-primary" @click="resetAll">继续挂号</button>
            </div>
          </div>

          <!-- 我的挂号记录 -->
          <div class="card shadow-sm border-0 rounded-3 mt-4" v-if="orders.length > 0">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
              <h5 class="mb-0"><i class="bi bi-list-ul me-2 text-primary"></i>最近挂号记录</h5>
              <button class="btn btn-sm btn-outline-primary" @click="loadOrders">
                <i class="bi bi-arrow-clockwise"></i> 刷新
              </button>
            </div>
            <div class="card-body p-0">
              <div class="table-responsive">
                <table class="table table-hover mb-0">
                  <thead class="table-light">
                    <tr>
                      <th>订单号</th>
                      <th>医生</th>
                      <th>日期</th>
                      <th>费用</th>
                      <th>状态</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="reg in orders.slice(0, 5)" :key="reg.id">
                      <td><small>{{ reg.orderNo }}</small></td>
                      <td>{{ reg.doctorName }}</td>
                      <td>{{ reg.workDate }} {{ reg.periodText }}</td>
                      <td>¥{{ reg.fee }}</td>
                      <td><span :class="'badge ' + statusBadge(reg.status)">{{ reg.statusText }}</span></td>
                      <td>
                        <button v-if="reg.status === 0" class="btn btn-sm btn-outline-danger"
                          @click="handleCancel(reg.id)">取消</button>
                        <span v-else class="text-muted small">--</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getDepartments, getDoctorsByDeptId, getDoctorSchedule,
  createOrder, getOrders, cancelOrder, findPatientByPhone
} from '@/api/index.js'

export default {
  name: 'RegisterView',
  data() {
    return {
      step: 1,
      departments: [],
      doctors: [],
      scheduleList: [],
      orders: [],
      submitting: false,
      showSuccess: false,
      orderNo: '',
      doctorsLoading: false,
      scheduleLoading: false,
      formData: {
        departmentId: '',
        doctorId: '',
        sourceId: '',
        workDate: '',
        periodText: '',
        patientName: '',
        idCard: '',
        phone: localStorage.getItem('phone') || '',
        gender: '',
      },
    }
  },
  computed: {
    selectedDeptName() {
      const d = this.departments.find(x => x.id === this.formData.departmentId)
      return d ? d.deptName : ''
    },
    selectedDoctorName() {
      const d = this.doctors.find(x => x.id === this.formData.doctorId)
      return d ? d.realName : ''
    },
    selectedDoctorTitle() {
      const d = this.doctors.find(x => x.id === this.formData.doctorId)
      return d ? d.title : ''
    },
    selectedFee() {
      if (!this.formData.sourceId) return '0.00'
      const s = this.scheduleList.find(x => x.sourceId === this.formData.sourceId)
      return s ? s.fee : '0.00'
    },
    patientId() {
      return parseInt(localStorage.getItem('patientId') || '1')
    },
  },
  methods: {
    formatDate(dateStr) {
      const d = new Date(dateStr)
      const weekdays = ['日', '一', '二', '三', '四', '五', '六']
      return `${dateStr} 周${weekdays[d.getDay()]}`
    },
    statusBadge(status) {
      const map = { 0: 'bg-warning text-dark', 1: 'bg-info text-dark', 2: 'bg-secondary', 3: 'bg-success' }
      return map[status] || 'bg-secondary'
    },
    async selectDept(dept) {
      this.formData.departmentId = dept.id
      this.formData.doctorId = ''
      this.formData.sourceId = ''
      this.scheduleList = []
      this.doctorsLoading = true
      try {
        this.doctors = await getDoctorsByDeptId(dept.id)
      } catch (e) {
        console.error(e)
      } finally {
        this.doctorsLoading = false
      }
    },
    async selectDoctor(doc) {
      this.formData.doctorId = doc.id
      this.formData.sourceId = ''
      this.scheduleLoading = true
      try {
        const today = new Date().toISOString().split('T')[0]
        const end = new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0]
        this.scheduleList = await getDoctorSchedule(doc.id, today, end)
      } catch (e) {
        console.error(e)
      } finally {
        this.scheduleLoading = false
      }
    },
    selectSlot(slot) {
      if (slot.availableCount <= 0) return
      this.formData.sourceId = slot.sourceId
      this.formData.workDate = slot.workDate
      this.formData.periodText = slot.periodText
    },
    async lookupPatient() {
      if (!this.formData.phone || this.formData.phone.length < 11) return
      try {
        const patient = await findPatientByPhone(this.formData.phone)
        if (patient) {
          this.formData.patientName = patient.realName || this.formData.patientName
          this.formData.idCard = patient.idCard || this.formData.idCard
          this.formData.gender = patient.gender != null ? String(patient.gender) : this.formData.gender
        }
      } catch (e) {
        // not found, let user fill manually
      }
    },
    async submitOrder() {
      if (!this.formData.patientName || !this.formData.phone) {
        alert('请填写患者信息')
        return
      }
      this.submitting = true
      try {
        const no = await createOrder({
          patientName: this.formData.patientName,
          idCard: this.formData.idCard,
          phone: this.formData.phone,
          gender: parseInt(this.formData.gender),
          deptId: this.formData.departmentId,
          doctorId: this.formData.doctorId,
          sourceId: this.formData.sourceId,
          workDate: this.formData.workDate,
          period: this.formData.periodText === '上午' ? 1 : 2,
        })
        this.orderNo = no
        this.showSuccess = true
        this.loadOrders()
      } catch (e) {
        alert('挂号失败：' + (e.message || '请稍后重试'))
      } finally {
        this.submitting = false
      }
    },
    async handleCancel(id) {
      if (!confirm('确定取消该挂号？')) return
      try {
        await cancelOrder(id, this.patientId)
        this.loadOrders()
      } catch (e) {
        alert(e.message || '取消失败')
      }
    },
    async loadOrders() {
      try {
        this.orders = await getOrders(this.patientId)
      } catch (e) {
        console.error(e)
      }
    },
    goOrders() {
      this.$router.push('/orders')
    },
    resetAll() {
      this.step = 1
      this.showSuccess = false
      this.orderNo = ''
      this.formData.departmentId = ''
      this.formData.doctorId = ''
      this.formData.sourceId = ''
      this.formData.workDate = ''
      this.formData.periodText = ''
      this.formData.patientName = ''
      this.formData.idCard = ''
      this.formData.gender = ''
      this.doctors = []
      this.scheduleList = []
    },
  },
  mounted() {
    getDepartments().then(d => { this.departments = d }).catch(console.error)
    this.loadOrders()
  },
}
</script>

<style scoped>
.steps { gap: 0; }
.step { display: flex; flex-direction: column; align-items: center; }
.step-circle {
  width: 36px; height: 36px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  background: #e9ecef; color: #6c757d; font-weight: bold; font-size: 14px;
}
.step.active .step-circle { background: #0d6efd; color: #fff; }
.step.done .step-circle { background: #198754; color: #fff; }
.step span { font-size: 12px; margin-top: 4px; }
.step-connector { width: 60px; height: 2px; background: #e9ecef; align-self: center; margin: 0 8px; }

.dept-grid { display: flex; flex-wrap: wrap; gap: 8px; }
.dept-btn { flex: 1 0 calc(50% - 8px); min-width: 0; }

.doctor-list { display: flex; flex-direction: column; gap: 8px; max-height: 320px; overflow-y: auto; }
.doctor-card {
  padding: 10px 12px; border: 1px solid #dee2e6; border-radius: 8px; cursor: pointer;
  transition: all 0.2s;
}
.doctor-card:hover { border-color: #0d6efd; background: #f0f6ff; }
.doctor-card.selected { border-color: #0d6efd; background: #e7f1ff; }
.doctor-avatar { width: 40px; height: 40px; border-radius: 50%; background: #e9ecef; display: flex; align-items: center; justify-content: center; }

.slot-card {
  padding: 16px; border: 1px solid #dee2e6; border-radius: 10px; text-align: center;
  cursor: pointer; transition: all 0.2s;
}
.slot-card:hover { border-color: #0d6efd; box-shadow: 0 2px 8px rgba(13,110,253,0.1); }
.slot-card.selected { border-color: #0d6efd; background: #e7f1ff; }
.slot-card.full { opacity: 0.5; cursor: not-allowed; }
.slot-card.low { border-color: #ffc107; }
.slot-date { font-weight: 600; font-size: 14px; margin-bottom: 4px; }
.slot-period { font-size: 13px; color: #6c757d; }
.slot-fee { font-size: 16px; font-weight: bold; color: #0d6efd; margin: 6px 0; }
.slot-avail { font-size: 12px; }

.text-sm { font-size: 14px; }
</style>
