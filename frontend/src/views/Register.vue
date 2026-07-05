<template>
  <div class="container mt-4">
    <div class="row">
      <div class="col-lg-8">
        <div v-if="showErrorMessage" class="alert alert-danger alert-dismissible fade show">
          <i class="fas fa-exclamation-triangle me-2"></i><strong>{{ errorMessage }}</strong>
          <button type="button" class="btn-close" @click="hideMessages"></button>
        </div>
        <div v-if="showSuccessMessage" class="alert alert-success alert-dismissible fade show">
          <i class="fas fa-check-circle me-2"></i><strong>{{ successMessage }}</strong>
          <button type="button" class="btn-close" @click="hideMessages"></button>
        </div>

        <!-- 步骤指示器 -->
        <div class="card mb-4">
          <div class="card-body">
            <div class="steps">
              <div class="step-item" :class="{ active: currentStep === 1, completed: currentStep > 1 }"><div class="step-number">1</div><div class="step-label">选择科室</div></div>
              <div class="step-item" :class="{ active: currentStep === 2, completed: currentStep > 2 }"><div class="step-number">2</div><div class="step-label">选择医生</div></div>
              <div class="step-item" :class="{ active: currentStep === 3, completed: currentStep > 3 }"><div class="step-number">3</div><div class="step-label">选择时间</div></div>
              <div class="step-item" :class="{ active: currentStep === 4 }"><div class="step-number">4</div><div class="step-label">确认信息</div></div>
            </div>
          </div>
        </div>

        <!-- 步骤1：选择科室 -->
        <div v-if="currentStep === 1" class="card">
          <div class="card-header bg-primary text-white"><h5 class="mb-0"><i class="fas fa-hospital me-2"></i>选择科室</h5></div>
          <div class="card-body">
            <div class="row">
              <div v-for="dept in departments" :key="dept.id" class="col-md-6 mb-3">
                <div class="department-card" :class="{ selected: selectedDepartment && selectedDepartment.id === dept.id }" @click="selectDepartment(dept)">
                  <div class="department-icon"><i class="fas fa-stethoscope"></i></div>
                  <div class="department-info"><h6>{{ dept.name }}</h6><p class="text-muted small">{{ dept.description || '专业医疗科室' }}</p></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 步骤2：选择医生 -->
        <div v-if="currentStep === 2" class="card">
          <div class="card-header bg-primary text-white"><h5 class="mb-0"><i class="fas fa-user-md me-2"></i>选择医生</h5></div>
          <div class="card-body">
            <div class="row mb-4">
              <div class="col-md-4"><label class="form-label">职称</label>
                <select class="form-select" v-model="filter.title"><option value="">全部职称</option><option>主任医师</option><option>副主任医师</option><option>主治医师</option><option>住院医师</option></select>
              </div>
              <div class="col-md-4"><label class="form-label">挂号费</label>
                <select class="form-select" v-model="filter.feeRange"><option value="">全部费用</option><option value="0-50">0-50元</option><option value="50-100">50-100元</option><option value="100-200">100-200元</option><option value="200-">200元以上</option></select>
              </div>
              <div class="col-md-4"><label class="form-label">评分</label>
                <select class="form-select" v-model="filter.rating"><option value="0">全部评分</option><option value="4">4星以上</option><option value="3">3星以上</option></select>
              </div>
            </div>
            <div class="row">
              <div v-for="doctor in filteredDoctors" :key="doctor.id" class="col-md-6 mb-3">
                <div class="doctor-card" :class="{ selected: selectedDoctor && selectedDoctor.id === doctor.id }" @click="selectDoctor(doctor)">
                  <div class="doctor-avatar"><i class="fas fa-user-md"></i></div>
                  <div class="doctor-info">
                    <h6>{{ doctor.name }}</h6>
                    <p class="text-muted small mb-1">{{ doctor.title }} | {{ doctor.departmentName }}</p>
                    <p class="text-primary small mb-1"><i class="fas fa-star text-warning"></i> {{ doctor.rating || '5.0' }} <span class="ms-2"><i class="fas fa-money-bill-wave"></i> {{ doctor.fee || 50 }}元</span></p>
                    <p class="text-muted small">{{ doctor.specialty || '专业医疗' }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 步骤3：选择时间 -->
        <div v-if="currentStep === 3" class="card">
          <div class="card-header bg-primary text-white"><h5 class="mb-0"><i class="fas fa-calendar me-2"></i>选择时间</h5></div>
          <div class="card-body">
            <div class="mb-4">
              <h6 class="mb-3">选择日期</h6>
              <div class="row">
                <div v-for="date in availableDates" :key="date.date" class="col-md-3 mb-2">
                  <div class="date-card" :class="{ selected: selectedDate === date.date, unavailable: !date.available }" @click="date.available && selectDate(date.date)">
                    <div class="date-day">{{ date.day }}</div>
                    <div class="date-number">{{ date.formattedDate }}</div>
                    <div v-if="!date.available" class="date-status">不可约</div>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <h6 class="mb-3">选择时间段</h6>
              <div class="row">
                <div v-for="slot in timeSlots" :key="slot.time" class="col-md-4 mb-2">
                  <div class="time-slot" :class="{ selected: selectedTime === slot.time, unavailable: !slot.available }" @click="slot.available && selectTime(slot.time)">
                    {{ slot.time }}
                    <span v-if="!slot.available" class="badge bg-secondary ms-1">已满</span>
                    <span v-else class="badge bg-success ms-1">余{{ slot.remaining }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 步骤4：填写就诊人信息 -->
        <div v-if="currentStep === 4" class="card">
          <div class="card-header bg-primary text-white"><h5 class="mb-0"><i class="fas fa-user me-2"></i>填写就诊人信息</h5></div>
          <div class="card-body">
            <div class="alert alert-info mb-4">
              <h6 class="alert-heading">预约信息汇总</h6>
              <div class="row">
                <div class="col-md-6"><small><strong>科室：</strong>{{ selectedDepartment?.name }}</small><br><small><strong>医生：</strong>{{ selectedDoctor?.name }}</small></div>
                <div class="col-md-6"><small><strong>时间：</strong>{{ selectedDate }} {{ selectedTime }}</small><br><small><strong>费用：</strong>{{ totalFee }}元</small></div>
              </div>
            </div>
            <h6 class="text-primary mb-3">就诊人信息</h6>
            <div class="row">
              <div class="col-md-6">
                <div class="mb-3"><label class="form-label">就诊人姓名 <span class="text-danger">*</span></label><input type="text" class="form-control" v-model="appointmentInfo.patientName" placeholder="请输入就诊人姓名">
                  <div class="form-text"><button type="button" class="btn btn-sm btn-outline-primary ms-2" @click="useCurrentUserInfo">使用当前用户信息</button></div>
                </div>
                <div class="mb-3"><label class="form-label">手机号 <span class="text-danger">*</span></label>
                  <div class="input-group"><input type="tel" class="form-control" v-model="appointmentInfo.phone" placeholder="请输入手机号"><button class="btn btn-outline-secondary" type="button" @click="searchPatientByPhone" :disabled="!appointmentInfo.phone"><i class="fas fa-search"></i> 查询</button></div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="mb-3"><label class="form-label">身份证号</label><input type="text" class="form-control" v-model="appointmentInfo.idCard" placeholder="请输入身份证号"></div>
                <div class="mb-3"><label class="form-label">症状描述</label><textarea class="form-control" v-model="appointmentInfo.symptoms" rows="3" placeholder="请简要描述您的症状..."></textarea></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧信息栏 -->
      <div class="col-lg-4">
        <div class="card">
          <div class="card-header bg-light"><h6 class="mb-0"><i class="fas fa-info-circle me-2"></i>预约信息</h6></div>
          <div class="card-body">
            <div v-if="selectedDepartment" class="info-section"><h6>科室信息</h6><p class="small text-muted">{{ selectedDepartment.name }}</p></div>
            <div v-if="selectedDoctor" class="info-section mt-3"><h6>医生信息</h6><p class="small text-muted">{{ selectedDoctor.name }} - {{ selectedDoctor.title }}</p><p class="small text-primary"><i class="fas fa-star text-warning"></i> {{ selectedDoctor.rating || '5.0' }} <span class="ms-2"><i class="fas fa-money-bill-wave"></i> {{ selectedDoctor.fee || 50 }}元</span></p></div>
            <div v-if="selectedDate && selectedTime" class="info-section mt-3"><h6>预约时间</h6><p class="small text-muted">{{ selectedDate }} {{ selectedTime }}</p></div>
            <div class="info-section mt-3 border-top pt-3">
              <div class="d-flex justify-content-between"><span>挂号费：</span><span>{{ selectedDoctor?.fee || 50 }}元</span></div>
              <div class="d-flex justify-content-between"><span>服务费：</span><span>5元</span></div>
              <div class="d-flex justify-content-between border-top pt-2"><strong>总计：</strong><strong class="text-primary">{{ totalFee }}元</strong></div>
            </div>
          </div>
        </div>
        <div class="card mt-3">
          <div class="card-body">
            <div class="d-grid gap-2">
              <button v-if="currentStep > 1" class="btn btn-outline-secondary" @click="goToStep(currentStep - 1)"><i class="fas fa-arrow-left me-2"></i>上一步</button>
              <button v-if="currentStep < 4" class="btn btn-primary" @click="goToStep(currentStep + 1)" :disabled="!canProceed">下一步 <i class="fas fa-arrow-right ms-2"></i></button>
              <button v-if="currentStep === 4" class="btn btn-success" @click="confirmAppointment" :disabled="loading || submitted"><i v-if="loading" class="fas fa-spinner fa-spin me-2"></i>{{ loading ? '提交中...' : (submitted ? '已提交' : '确认预约') }}</button>
              <a href="/" class="btn btn-outline-secondary" @click.prevent="$router.push('/')"><i class="fas fa-times me-2"></i>取消</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getDepartments, getDoctorsByDepartment, createOrder, findPatientByPhone, getAvailableSlots } from '@/api/index.js'

export default {
  name: 'RegisterView',
  data() {
    return {
      currentStep: 1, departments: [], doctors: [], selectedDepartment: null, selectedDoctor: null,
      availableDates: [], selectedDate: null, timeSlots: [], selectedTime: null,
      filter: { title: '', feeRange: '', rating: 0 },
      appointmentInfo: { patientName: '', phone: localStorage.getItem('phone') || '', idCard: '', symptoms: '' },
      loading: false, submitted: false, errorMessage: '', successMessage: '', showErrorMessage: false, showSuccessMessage: false, messageTimer: null
    }
  },
  computed: {
    canProceed() {
      if (this.currentStep === 1) return this.selectedDepartment !== null
      if (this.currentStep === 2) return this.selectedDoctor !== null
      if (this.currentStep === 3) return this.selectedDate !== null && this.selectedTime !== null
      return false
    },
    filteredDoctors() {
      let result = this.doctors
      if (this.filter.title) result = result.filter(d => d.title === this.filter.title)
      if (this.filter.feeRange) { const [min, max] = this.filter.feeRange.split('-').map(Number); result = max ? result.filter(d => d.fee >= min && d.fee <= max) : result.filter(d => d.fee >= min) }
      if (this.filter.rating > 0) result = result.filter(d => d.rating >= this.filter.rating)
      return result
    },
    totalFee() { return (this.selectedDoctor?.fee || 0) + 5 }
  },
  methods: {
    async loadDepartments() { try { const res = await getDepartments(); this.departments = res.data || res || [] } catch (e) { this.showError('加载科室数据失败') } },
    async selectDepartment(dept) { this.selectedDepartment = dept; this.selectedDoctor = null; try { const res = await getDoctorsByDepartment(dept.id); this.doctors = res.data || res || [] } catch (e) { this.showError('加载医生数据失败') } },
    async selectDoctor(doctor) { this.selectedDoctor = doctor; if (this.selectedDate) await this.loadBookedSlots() },
    async loadBookedSlots() {
      if (!this.selectedDoctor || !this.selectedDate) return
      try {
        const res = await getAvailableSlots(this.selectedDoctor.id, this.selectedDate)
        const slots = res.data || res || []
        const hourMap = {}
        for (const s of slots) {
          const hour = s.time.split(':')[0]
          if (!hourMap[hour]) {
            hourMap[hour] = { time: hour + ':00-' + this.addMinutes(hour + ':00', 60), available: s.available, remaining: s.remaining }
          } else {
            hourMap[hour].available = hourMap[hour].available || s.available
            hourMap[hour].remaining = Math.max(hourMap[hour].remaining, s.remaining)
          }
        }
        this.timeSlots = Object.values(hourMap)
      } catch (e) {
        this.timeSlots = []
      }
    },
    addMinutes(time, mins) {
      const [h, m] = time.split(':').map(Number)
      const total = h * 60 + m + mins
      return String(Math.floor(total / 60)).padStart(2, '0') + ':' + String(total % 60).padStart(2, '0')
    },
    async loadAvailableDates() {
      const today = new Date(); const dates = []
      for (let i = 0; i < 7; i++) { const d = new Date(today); d.setDate(today.getDate() + i); const days = ['日', '一', '二', '三', '四', '五', '六']; dates.push({ date: d.toISOString().split('T')[0], formattedDate: `${d.getMonth()+1}月${d.getDate()}日`, day: `星期${days[d.getDay()]}`, available: i > 0 }) }
      this.availableDates = dates; this.selectedDate = dates[1]?.date
    },
    async selectDate(date) { this.selectedDate = date; this.selectedTime = null; await this.loadBookedSlots() },
    selectTime(time) { this.selectedTime = time },
    goToStep(step) { this.currentStep = step; window.scrollTo(0, 0) },
    useCurrentUserInfo() { this.appointmentInfo.patientName = localStorage.getItem('username') || ''; this.appointmentInfo.phone = localStorage.getItem('phone') || '' },
    async searchPatientByPhone() {
      if (!this.appointmentInfo.phone) { this.showError('请输入手机号'); return }
      try { const p = await findPatientByPhone(this.appointmentInfo.phone); if (p && p.id) { this.appointmentInfo.patientName = p.name || ''; this.appointmentInfo.idCard = p.idCard || ''; this.showSuccess('已找到病人信息，已自动填充') } else { this.showSuccess('未找到该手机号对应的病人信息') } } catch (e) { this.showError('查询病人信息失败') }
    },
    async confirmAppointment() {
      this.loading = true
      try {
        if (!this.appointmentInfo.patientName) throw new Error('请输入就诊人姓名')
        if (!this.appointmentInfo.phone) throw new Error('请输入手机号')
        await createOrder({ doctorId: this.selectedDoctor.id, registerTime: `${this.selectedDate}T${this.selectedTime.split('-')[0]}:00`, symptoms: this.appointmentInfo.symptoms, notes: '', patientName: this.appointmentInfo.patientName, patientPhone: this.appointmentInfo.phone, patientIdCard: this.appointmentInfo.idCard, patientGender: null })
        this.submitted = true
        this.showSuccess('预约成功！')
        setTimeout(() => { this.$router.push('/orders') }, 800)
      } catch (e) { this.showError(e.message || '预约失败') }
      finally { this.loading = false }
    },
    showSuccess(msg) { if (this.messageTimer) clearTimeout(this.messageTimer); this.successMessage = msg; this.showSuccessMessage = true; this.messageTimer = setTimeout(() => this.hideMessages(), 5000) },
    showError(msg) { if (this.messageTimer) clearTimeout(this.messageTimer); this.errorMessage = msg; this.showErrorMessage = true; this.messageTimer = setTimeout(() => this.hideMessages(), 5000) },
    hideMessages() { this.showErrorMessage = false; this.showSuccessMessage = false; if (this.messageTimer) { clearTimeout(this.messageTimer); this.messageTimer = null } }
  },
  mounted() { this.loadDepartments(); this.loadAvailableDates() }
}
</script>
