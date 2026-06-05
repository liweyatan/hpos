<template>
  <div class="registration-form">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-8">
          <div class="card shadow">
            <div class="card-header bg-primary text-white">
              <h3 class="card-title mb-0">在线挂号</h3>
            </div>

            <div class="card-body">
              <form @submit.prevent="submitForm">
                <!-- 患者信息 -->
                <div class="mb-4">
                  <h5 class="mb-3"><i class="bi bi-person me-2"></i>患者信息</h5>
                  <div class="row">
                    <div class="col-md-6 mb-3">
                      <label for="name" class="form-label">姓名 *</label>
                      <input
                        type="text"
                        class="form-control"
                        id="name"
                        v-model="formData.patientName"
                        required
                      >
                    </div>
                    <div class="col-md-6 mb-3">
                      <label for="idCard" class="form-label">身份证号 *</label>
                      <input
                        type="text"
                        class="form-control"
                        id="idCard"
                        v-model="formData.idCard"
                        required
                      >
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-6 mb-3">
                      <label for="phone" class="form-label">手机号 *</label>
                      <input
                        type="tel"
                        class="form-control"
                        id="phone"
                        v-model="formData.phone"
                        required
                      >
                    </div>
                    <div class="col-md-6 mb-3">
                      <label for="gender" class="form-label">性别 *</label>
                      <select class="form-select" id="gender" v-model="formData.gender" required>
                        <option value="">请选择</option>
                        <option value="1">男</option>
                        <option value="2">女</option>
                      </select>
                    </div>
                  </div>
                </div>

                <!-- 挂号信息 -->
                <div class="mb-4">
                  <h5 class="mb-3"><i class="bi bi-calendar3 me-2"></i>挂号信息</h5>

                  <div class="row mb-3">
                    <div class="col-md-6">
                      <label for="department" class="form-label">选择科室 *</label>
                      <select
                        class="form-select"
                        id="department"
                        v-model="formData.departmentId"
                        @change="onDeptChange"
                        required
                      >
                        <option value="">请选择科室</option>
                        <option
                          v-for="dept in departments"
                          :key="dept.id"
                          :value="dept.id"
                        >
                          {{ dept.deptName }}
                        </option>
                      </select>
                    </div>

                    <div class="col-md-6">
                      <label for="doctor" class="form-label">选择医生 *</label>
                      <select
                        class="form-select"
                        id="doctor"
                        v-model="formData.doctorId"
                        :disabled="!formData.departmentId"
                        @change="onDoctorChange"
                        required
                      >
                        <option value="">请先选择科室</option>
                        <option
                          v-for="doctor in filteredDoctors"
                          :key="doctor.id"
                          :value="doctor.id"
                        >
                          {{ doctor.realName }} ({{ doctor.title }})
                        </option>
                      </select>
                    </div>
                  </div>

                  <div class="row mb-3">
                    <div class="col-md-6">
                      <label for="date" class="form-label">预约日期 *</label>
                      <input
                        type="date"
                        class="form-control"
                        id="date"
                        v-model="formData.date"
                        :min="today"
                        @change="onDateChange"
                        required
                      >
                    </div>

                    <div class="col-md-6">
                      <label for="timeSlot" class="form-label">预约时段 *</label>
                      <select
                        class="form-select"
                        id="timeSlot"
                        v-model="formData.timeSlot"
                        @change="onSlotChange"
                        required
                      >
                        <option value="">请选择时段</option>
                        <option value="1">上午 (08:00-12:00)</option>
                        <option value="2">下午 (13:00-17:00)</option>
                      </select>
                    </div>
                  </div>

                  <div class="row mb-3">
                    <div class="col-md-6">
                      <label class="form-label">挂号费用</label>
                      <div class="form-control-plaintext fw-bold text-primary">
                        ¥{{ selectedFee }}
                      </div>
                    </div>
                  </div>
                </div>

                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                  <button type="button" class="btn btn-secondary me-md-2" @click="resetForm">
                    重置
                  </button>
                  <button type="submit" class="btn btn-primary" :disabled="submitting">
                    {{ submitting ? '提交中...' : '提交挂号' }}
                  </button>
                </div>
              </form>
            </div>
          </div>

          <!-- 挂号记录 -->
          <div class="card shadow mt-4" v-if="registrations.length > 0">
            <div class="card-header bg-light">
              <h5 class="mb-0">我的挂号记录</h5>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>订单号</th>
                      <th>患者姓名</th>
                      <th>科室</th>
                      <th>医生</th>
                      <th>就诊时间</th>
                      <th>费用</th>
                      <th>状态</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="reg in registrations" :key="reg.id">
                      <td>{{ reg.orderNo }}</td>
                      <td>{{ reg.patientName }}</td>
                      <td>{{ reg.deptName }}</td>
                      <td>{{ reg.doctorName }} ({{ reg.doctorTitle }})</td>
                      <td>{{ reg.workDate }} {{ reg.periodText }}</td>
                      <td>¥{{ reg.fee }}</td>
                      <td>
                        <span :class="'badge ' + statusBadge(reg.status)">
                          {{ reg.statusText }}
                        </span>
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
  getDepartments,
  getDoctorsByDeptId,
  getDoctorSchedule,
  createOrder,
  getOrders
} from '../api/index.js';

export default {
  name: 'RegistrationForm',
  data() {
    return {
      // ===== 下拉列表数据 =====
      departments: [],           // 科室列表
      doctors: [],               // 医生列表
      scheduleList: [],          // 号源排班

      // ===== 表单数据 =====
      formData: {
        patientName: '',
        idCard: '',
        phone: '',
        gender: '',
        departmentId: '',
        doctorId: '',
        date: '',
        timeSlot: '',
        sourceId: ''             // 选中的号源ID（由 date + timeSlot 确定）
      },

      // ===== 状态 =====
      registrations: [],         // 挂号记录
      submitting: false,         // 是否正在提交
      currentPatientId: null     // 当前患者ID（模拟登录，实际由登录接口提供）
    };
  },

  computed: {
    // 根据选择的科室过滤医生
    filteredDoctors() {
      if (!this.formData.departmentId) return [];
      return this.doctors;
    },

    // 今日日期（用于日期输入框的 min 属性）
    today() {
      return new Date().toISOString().split('T')[0];
    },

    // 选中号源的费用
    selectedFee() {
      if (!this.formData.sourceId) return '0.00';
      const source = this.scheduleList.find(
        s => s.sourceId === parseInt(this.formData.sourceId)
      );
      return source ? source.fee : '0.00';
    }
  },

  methods: {
    // ========== 初始化数据 ==========

    /** 加载科室列表 */
    async loadDepartments() {
      try {
        this.departments = await getDepartments();
      } catch (e) {
        console.error('加载科室失败', e);
        alert('加载科室列表失败，请确保后端已启动');
      }
    },

    /** 加载挂号记录 */
    async loadOrders() {
      if (!this.currentPatientId) return;
      try {
        this.registrations = await getOrders(this.currentPatientId);
      } catch (e) {
        console.error('加载挂号记录失败', e);
      }
    },

    // ========== 选择事件 ==========

    /** 科室切换时：加载该科室的医生 */
    async onDeptChange() {
      this.formData.doctorId = '';
      this.formData.sourceId = '';
      this.scheduleList = [];

      if (!this.formData.departmentId) return;

      try {
        this.doctors = await getDoctorsByDeptId(this.formData.departmentId);
      } catch (e) {
        console.error('加载医生失败', e);
      }
    },

    /** 医生切换时：加载排班 */
    async onDoctorChange() {
      this.formData.sourceId = '';
      this.scheduleList = [];

      if (!this.formData.doctorId) return;

      try {
        const today = this.today;
        const endDate = new Date();
        endDate.setDate(endDate.getDate() + 7);
        const endStr = endDate.toISOString().split('T')[0];
        this.scheduleList = await getDoctorSchedule(this.formData.doctorId, today, endStr);
      } catch (e) {
        console.error('加载排班失败', e);
      }
    },

    /** 日期切换时：尝试匹配号源 */
    onDateChange() {
      this.matchSource();
    },

    /** 时段切换时：尝试匹配号源 */
    onSlotChange() {
      this.matchSource();
    },

    /** 根据选中的日期和时段，匹配号源ID */
    matchSource() {
      if (!this.formData.date || !this.formData.timeSlot) {
        this.formData.sourceId = '';
        return;
      }

      const source = this.scheduleList.find(s => {
        return s.workDate === this.formData.date
          && s.period === parseInt(this.formData.timeSlot)
          && s.availableCount > 0;
      });

      this.formData.sourceId = source ? String(source.sourceId) : '';
      if (!this.formData.sourceId) {
        alert('该时段无可预约号源，请选择其他日期或时段');
      }
    },

    // ========== 提交挂号 ==========

    async submitForm() {
      // 表单验证
      if (!this.formData.patientName || !this.formData.phone) {
        alert('请填写患者信息');
        return;
      }
      if (!this.formData.sourceId) {
        alert('请选择可预约的时段');
        return;
      }

      this.submitting = true;
      try {
        const orderNo = await createOrder({
          patientName: this.formData.patientName,
          idCard: this.formData.idCard,
          phone: this.formData.phone,
          gender: parseInt(this.formData.gender),
          deptId: parseInt(this.formData.departmentId),
          doctorId: parseInt(this.formData.doctorId),
          sourceId: parseInt(this.formData.sourceId),
          workDate: this.formData.date,
          period: parseInt(this.formData.timeSlot)
        });

        alert('挂号成功！订单号：' + orderNo);
        this.resetForm();

        // 刷新挂号记录
        await this.loadOrders();
      } catch (e) {
        alert('挂号失败：' + (e.message || '请稍后重试'));
      } finally {
        this.submitting = false;
      }
    },

    /** 重置表单 */
    resetForm() {
      this.formData = {
        patientName: '',
        idCard: '',
        phone: '',
        gender: '',
        departmentId: '',
        doctorId: '',
        date: '',
        timeSlot: '',
        sourceId: ''
      };
      this.scheduleList = [];
      this.setDefaultDate();
    },

    /** 设置默认日期（明天） */
    setDefaultDate() {
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);
      this.formData.date = tomorrow.toISOString().split('T')[0];
    },

    /** 状态标签样式 */
    statusBadge(status) {
      const map = {
        0: 'bg-warning text-dark',    // 待支付
        1: 'bg-info text-dark',       // 已支付
        2: 'bg-secondary',            // 已取消
        3: 'bg-success'               // 已就诊
      };
      return map[status] || 'bg-secondary';
    }
  },

  mounted() {
    // 组件挂载时加载数据
    this.loadDepartments();
    this.setDefaultDate();

    // 模拟当前患者ID（后续由登录接口获取）
    // 这里临时设置为 1（测试数据中的张三）
    this.currentPatientId = 1;
    this.loadOrders();
  }
}
</script>

<style scoped>
.registration-form {
  padding: 2rem 0;
}

.card {
  border: none;
  border-radius: 10px;
  overflow: hidden;
}

.card-header {
  border-radius: 0 !important;
}

.form-label {
  font-weight: 500;
  margin-bottom: 0.5rem;
}
</style>
