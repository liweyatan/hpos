<template>
  <div class="admin-page">
    <div class="hospital-banner">
      <div class="container">
        <div class="row align-items-center min-vh-50">
          <div class="col-md-6">
            <h1 class="display-4 fw-bold text-white mb-3">管理员后台管理系统</h1>
            <p class="lead text-white mb-4">全面管理医院各项业务数据，提供高效的管理工具</p>
          </div>
        </div>
      </div>
    </div>

    <div class="container mt-5">
      <ul class="nav nav-tabs" role="tablist">
        <li class="nav-item">
          <button class="nav-link" :class="{ active: activeTab === 'department' }" @click="switchTab('department')"><i class="fas fa-building me-2"></i>科室管理</button>
        </li>
        <li class="nav-item">
          <button class="nav-link" :class="{ active: activeTab === 'doctor' }" @click="switchTab('doctor')"><i class="fas fa-user-md me-2"></i>医生管理</button>
        </li>
        <li class="nav-item">
          <button class="nav-link" :class="{ active: activeTab === 'appointment' }" @click="switchTab('appointment')"><i class="fas fa-calendar-check me-2"></i>预约管理</button>
        </li>
        <li class="nav-item">
          <button class="nav-link" :class="{ active: activeTab === 'user' }" @click="switchTab('user')"><i class="fas fa-users me-2"></i>用户管理</button>
        </li>
      </ul>

      <div v-if="alertMsg" :class="['alert', 'alert-' + alertType, 'alert-dismissible', 'fade', 'show', 'mt-3']">{{ alertMsg }}<button type="button" class="btn-close" @click="alertMsg = ''"></button></div>

      <!-- 科室管理 -->
      <div v-if="activeTab === 'department'" class="card mt-3">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h5 class="card-title mb-0"><i class="fas fa-building me-2"></i>科室管理</h5>
          <button class="btn btn-primary" @click="showDeptModal()"><i class="fas fa-plus me-2"></i>新增科室</button>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-hover">
              <thead><tr><th>科室名称</th><th>负责人</th><th>联系电话</th><th>科室位置</th><th>状态</th><th>操作</th></tr></thead>
              <tbody>
                <tr v-for="d in departments" :key="d.id">
                  <td>{{ d.name }}</td><td>{{ d.director || '-' }}</td><td>{{ d.phone || '-' }}</td><td>{{ d.location || '-' }}</td>
                  <td><span class="badge" :class="d.active ? 'bg-success' : 'bg-danger'">{{ d.active ? '启用' : '禁用' }}</span></td>
                  <td><div class="btn-group btn-group-sm"><button class="btn btn-outline-primary" @click="showDeptModal(d)" title="编辑"><i class="fas fa-edit"></i></button><button class="btn btn-outline-danger" @click="deleteDept(d.id)" title="删除"><i class="fas fa-trash"></i></button></div></td>
                </tr>
                <tr v-if="departments.length === 0"><td colspan="6" class="text-center py-4">暂无科室数据</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 医生管理 -->
      <div v-if="activeTab === 'doctor'" class="card mt-3">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h5 class="card-title mb-0"><i class="fas fa-user-md me-2"></i>医生管理</h5>
          <button class="btn btn-primary" @click="showDoctorModal()"><i class="fas fa-plus me-2"></i>新增医生</button>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-hover">
              <thead><tr><th>医生姓名</th><th>职称</th><th>所属科室</th><th>专长</th><th>最大接诊数</th><th>当前挂号数</th><th>状态</th><th>操作</th></tr></thead>
              <tbody>
                <tr v-for="doc in doctors" :key="doc.id">
                  <td>{{ doc.name }}</td><td>{{ doc.title || '-' }}</td><td>{{ doc.departmentName || '-' }}</td><td>{{ doc.specialty || '-' }}</td>
                  <td>{{ doc.maxPatients || 0 }}</td><td>{{ doc.currentPatients || 0 }}</td>
                  <td><span class="badge" :class="doc.available ? 'bg-success' : 'bg-danger'">{{ doc.available ? '可预约' : '不可预约' }}</span></td>
                  <td><div class="btn-group btn-group-sm"><button class="btn btn-outline-primary" @click="showDoctorModal(doc)" title="编辑"><i class="fas fa-edit"></i></button><button class="btn btn-outline-danger" @click="deleteDoc(doc.id)" title="删除"><i class="fas fa-trash"></i></button></div></td>
                </tr>
                <tr v-if="doctors.length === 0"><td colspan="8" class="text-center py-4">暂无医生数据</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 预约管理 -->
      <div v-if="activeTab === 'appointment'" class="card mt-3">
        <div class="card-header"><h5 class="card-title mb-0"><i class="fas fa-calendar-check me-2"></i>预约管理</h5></div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-hover">
              <thead><tr><th>预约号</th><th>患者姓名</th><th>医生姓名</th><th>科室</th><th>预约时间</th><th>症状描述</th><th>状态</th><th>操作</th></tr></thead>
              <tbody>
                <tr v-for="a in appointments" :key="a.id">
                  <td><strong>{{ a.appointmentNo || 'GH' + a.id }}</strong></td>
                  <td>{{ a.patientName }}</td><td>{{ a.doctorName }}</td><td>{{ a.departmentName }}</td>
                  <td>{{ formatTime(a.registerTime) }}</td><td>{{ a.symptoms || '-' }}</td>
                  <td><span class="badge" :class="getAppointmentStatusClass(a.status)">{{ getAppointmentStatusText(a.status) }}</span></td>
                  <td>
                    <div class="btn-group btn-group-sm">
                      <button v-if="a.status === 'PENDING'" class="btn btn-outline-primary" @click="updateStatus(a.id, 'CONFIRMED')" title="确认"><i class="fas fa-check"></i></button>
                      <button v-if="a.status === 'PENDING' || a.status === 'CONFIRMED'" class="btn btn-outline-warning" @click="updateStatus(a.id, 'CANCELLED')" title="取消"><i class="fas fa-times"></i></button>
                      <button v-if="a.status === 'CONFIRMED'" class="btn btn-outline-success" @click="updateStatus(a.id, 'COMPLETED')" title="完成"><i class="fas fa-flag-checkered"></i></button>
                    </div>
                  </td>
                </tr>
                <tr v-if="appointments.length === 0"><td colspan="8" class="text-center py-4">暂无预约数据</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 用户管理 -->
      <div v-if="activeTab === 'user'" class="card mt-3">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h5 class="card-title mb-0"><i class="fas fa-users me-2"></i>用户管理</h5>
          <button class="btn btn-primary" @click="showUserModal()"><i class="fas fa-plus me-2"></i>新增用户</button>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-hover">
              <thead><tr><th>用户名</th><th>真实姓名</th><th>邮箱</th><th>手机号</th><th>角色</th><th>状态</th><th>创建时间</th><th>操作</th></tr></thead>
              <tbody>
                <tr v-for="u in users" :key="u.id">
                  <td>{{ u.username }}</td><td>{{ u.realName || '-' }}</td><td>{{ u.email || '-' }}</td><td>{{ u.phone || '-' }}</td>
                  <td><span class="badge" :class="getRoleBadgeClass(u.role)">{{ getRoleText(u.role) }}</span></td>
                  <td><span class="badge" :class="u.enabled ? 'bg-success' : 'bg-secondary'">{{ u.enabled ? '正常' : '禁用' }}</span></td>
                  <td>{{ formatTime(u.createTime) }}</td>
                  <td><div class="btn-group btn-group-sm"><button class="btn btn-outline-primary" @click="showUserModal(u)" title="编辑"><i class="fas fa-edit"></i></button><button class="btn btn-outline-danger" @click="deleteUser(u.id)" title="删除"><i class="fas fa-trash"></i></button></div></td>
                </tr>
                <tr v-if="users.length === 0"><td colspan="8" class="text-center py-4">暂无用户数据</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 科室模态框 -->
    <div v-if="showDeptModalFlag" class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.5)">
      <div class="modal-dialog"><div class="modal-content">
        <div class="modal-header"><h5 class="modal-title">{{ deptForm.id ? '编辑' : '新增' }}科室</h5><button type="button" class="btn-close" @click="showDeptModalFlag = false"></button></div>
        <div class="modal-body">
          <div class="mb-3"><label class="form-label">科室名称 *</label><input type="text" class="form-control" v-model="deptForm.name" required></div>
          <div class="mb-3"><label class="form-label">科室描述</label><textarea class="form-control" v-model="deptForm.description" rows="3"></textarea></div>
          <div class="mb-3"><label class="form-label">负责人</label><input type="text" class="form-control" v-model="deptForm.director"></div>
          <div class="mb-3"><label class="form-label">联系电话</label><input type="tel" class="form-control" v-model="deptForm.phone"></div>
          <div class="mb-3"><label class="form-label">科室位置</label><input type="text" class="form-control" v-model="deptForm.location"></div>
          <div class="mb-3"><label class="form-label">状态</label>
            <div><div class="form-check form-check-inline"><input class="form-check-input" type="radio" v-model="deptForm.active" :value="1" id="deptActive"><label class="form-check-label" for="deptActive">启用</label></div>
            <div class="form-check form-check-inline"><input class="form-check-input" type="radio" v-model="deptForm.active" :value="0" id="deptInactive"><label class="form-check-label" for="deptInactive">禁用</label></div></div>
          </div>
        </div>
        <div class="modal-footer"><button class="btn btn-secondary" @click="showDeptModalFlag = false">取消</button><button class="btn btn-primary" @click="saveDept">保存</button></div>
      </div></div>
    </div>

    <!-- 医生模态框 -->
    <div v-if="showDoctorModalFlag" class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.5)">
      <div class="modal-dialog"><div class="modal-content">
        <div class="modal-header"><h5 class="modal-title">{{ doctorForm.id ? '编辑' : '新增' }}医生</h5><button type="button" class="btn-close" @click="showDoctorModalFlag = false"></button></div>
        <div class="modal-body">
          <div class="mb-3"><label class="form-label">姓名 *</label><input type="text" class="form-control" v-model="doctorForm.name" required></div>
          <div class="mb-3"><label class="form-label">职称</label>
            <select class="form-select" v-model="doctorForm.title"><option value="">请选择</option><option>主任医师</option><option>副主任医师</option><option>主治医师</option><option>住院医师</option></select>
          </div>
          <div class="mb-3"><label class="form-label">所属科室 *</label>
            <select class="form-select" v-model="doctorForm.departmentId" required><option value="">请选择科室</option><option v-for="d in departments" :key="d.id" :value="d.id">{{ d.name }}</option></select>
          </div>
          <div class="mb-3"><label class="form-label">专长</label><textarea class="form-control" v-model="doctorForm.specialty" rows="3"></textarea></div>
          <div class="mb-3"><label class="form-label">最大接诊数</label><input type="number" class="form-control" v-model.number="doctorForm.maxPatients" min="1" value="20"></div>
          <div class="mb-3"><label class="form-label">状态</label>
            <div><div class="form-check form-check-inline"><input class="form-check-input" type="radio" v-model="doctorForm.available" :value="true" id="docActive"><label class="form-check-label" for="docActive">可预约</label></div>
            <div class="form-check form-check-inline"><input class="form-check-input" type="radio" v-model="doctorForm.available" :value="false" id="docInactive"><label class="form-check-label" for="docInactive">不可预约</label></div></div>
          </div>
        </div>
        <div class="modal-footer"><button class="btn btn-secondary" @click="showDoctorModalFlag = false">取消</button><button class="btn btn-primary" @click="saveDoctor">保存</button></div>
      </div></div>
    </div>

    <!-- 用户模态框 -->
    <div v-if="showUserModalFlag" class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.5)">
      <div class="modal-dialog"><div class="modal-content">
        <div class="modal-header"><h5 class="modal-title">{{ userForm.id ? '编辑' : '新增' }}用户</h5><button type="button" class="btn-close" @click="showUserModalFlag = false"></button></div>
        <div class="modal-body">
          <div class="mb-3"><label class="form-label">用户名 *</label><input type="text" class="form-control" v-model="userForm.username" :disabled="!!userForm.id" required></div>
          <div class="mb-3" v-if="!userForm.id"><label class="form-label">密码 *</label><input type="password" class="form-control" v-model="userForm.password" required></div>
          <div class="mb-3"><label class="form-label">真实姓名</label><input type="text" class="form-control" v-model="userForm.realName"></div>
          <div class="mb-3"><label class="form-label">手机号</label><input type="text" class="form-control" v-model="userForm.phone"></div>
          <div class="mb-3"><label class="form-label">邮箱</label><input type="email" class="form-control" v-model="userForm.email"></div>
          <div class="mb-3"><label class="form-label">角色</label>
            <select class="form-select" v-model="userForm.role"><option value="USER">用户</option><option value="ADMIN">管理员</option></select>
          </div>
          <div class="mb-3"><label class="form-label">状态</label>
            <div><div class="form-check form-check-inline"><input class="form-check-input" type="radio" v-model="userForm.enabled" :value="true" id="userActive"><label class="form-check-label" for="userActive">正常</label></div>
            <div class="form-check form-check-inline"><input class="form-check-input" type="radio" v-model="userForm.enabled" :value="false" id="userInactive"><label class="form-check-label" for="userInactive">禁用</label></div></div>
          </div>
        </div>
        <div class="modal-footer"><button class="btn btn-secondary" @click="showUserModalFlag = false">取消</button><button class="btn btn-primary" @click="saveUser">保存</button></div>
      </div></div>
    </div>

    <footer class="bg-dark text-white py-4 mt-5">
      <div class="container text-center"><p class="mb-0"><i class="fas fa-copyright me-1"></i>2026 <strong>智慧医院管理系统</strong> 版权所有</p></div>
    </footer>
  </div>
</template>

<script>
import { getDepartments, createDepartment, updateDepartment, deleteDepartment as delDept, getDoctors, createDoctor, updateDoctor, deleteDoctor as delDoc, getAppointments, updateAppointmentStatus, getUsers, createUser, updateUser, deleteUser as delUser } from '@/api/index.js'
import { getAuthState } from '@/api/auth.js'

export default {
  name: 'AdminView',
  data() {
    return {
      activeTab: 'department', departments: [], doctors: [], appointments: [], users: [],
      alertMsg: '', alertType: 'success',
      showDeptModalFlag: false, showDoctorModalFlag: false, showUserModalFlag: false,
      deptForm: { id: null, name: '', description: '', director: '', phone: '', location: '', active: 1 },
      doctorForm: { id: null, name: '', title: '', departmentId: '', specialty: '', available: true, maxPatients: 20 },
      userForm: { id: null, username: '', password: '', realName: '', phone: '', email: '', role: 'USER', enabled: true }
    }
  },
  methods: {
    formatTime(t) {
      if (!t) return '-'
      try { return new Date(t).toLocaleString('zh-CN') } catch { return t }
    },
    getRoleBadgeClass(role) {
      return { ADMIN: 'bg-danger', DOCTOR: 'bg-primary', PATIENT: 'bg-success', USER: 'bg-secondary' }[role] || 'bg-secondary'
    },
    getRoleText(role) {
      return { ADMIN: '管理员', DOCTOR: '医生', PATIENT: '患者', USER: '用户' }[role] || role
    },
    showAlert(msg, type = 'success') { this.alertMsg = msg; this.alertType = type; setTimeout(() => { this.alertMsg = '' }, 5000) },
    switchTab(tab) { this.activeTab = tab; if (tab === 'department') this.loadDepartments(); else if (tab === 'doctor') this.loadDoctors(); else if (tab === 'appointment') this.loadAppointments(); else if (tab === 'user') this.loadUsers() },
    // ===== 科室 =====
    async loadDepartments() { try { const res = await getDepartments(); this.departments = res.data || res || [] } catch (e) { console.error(e); this.showAlert('加载科室数据失败', 'danger') } },
    showDeptModal(dept = null) { this.deptForm = dept ? { ...dept } : { id: null, name: '', description: '', director: '', phone: '', location: '', active: 1 }; this.showDeptModalFlag = true },
    async saveDept() {
      if (!this.deptForm.name || !this.deptForm.name.trim()) { this.showAlert('请输入科室名称', 'danger'); return }
      try { if (this.deptForm.id) await updateDepartment(this.deptForm.id, this.deptForm); else await createDepartment(this.deptForm); this.showDeptModalFlag = false; this.showAlert('科室保存成功'); this.loadDepartments() } catch (e) { this.showAlert(e.message || '保存失败', 'danger') }
    },
    async deleteDept(id) { if (!confirm('确定要删除这个科室吗？此操作不可恢复！')) return; try { await delDept(id); this.showAlert('科室删除成功'); this.loadDepartments() } catch (e) { this.showAlert(e.message || '删除失败', 'danger') } },
    // ===== 医生 =====
    async loadDoctors() { try { const res = await getDoctors(); this.doctors = res.data || res || [] } catch (e) { console.error(e); this.showAlert('加载医生数据失败', 'danger') } },
    showDoctorModal(doc = null) { this.doctorForm = doc ? { ...doc } : { id: null, name: '', title: '', departmentId: '', specialty: '', available: true, maxPatients: 20 }; this.showDoctorModalFlag = true },
    async saveDoctor() {
      if (!this.doctorForm.name) { this.showAlert('请输入医生姓名', 'danger'); return }
      try { if (this.doctorForm.id) await updateDoctor(this.doctorForm.id, this.doctorForm); else await createDoctor(this.doctorForm); this.showDoctorModalFlag = false; this.showAlert('医生保存成功'); this.loadDoctors() } catch (e) { this.showAlert(e.message || '保存失败', 'danger') }
    },
    async deleteDoc(id) { if (!confirm('确定要删除这个医生吗？')) return; try { await delDoc(id); this.showAlert('医生删除成功'); this.loadDoctors() } catch (e) { this.showAlert(e.message || '删除失败', 'danger') } },
    // ===== 预约 =====
    async loadAppointments() { try { const res = await getAppointments(); this.appointments = res.data || res || [] } catch (e) { console.error(e); this.showAlert('加载预约数据失败', 'danger') } },
    getAppointmentStatusClass(s) { return { PENDING: 'bg-warning', CONFIRMED: 'bg-primary', CANCELLED: 'bg-danger', COMPLETED: 'bg-success' }[s] || 'bg-secondary' },
    getAppointmentStatusText(s) { return { PENDING: '待处理', CONFIRMED: '已确认', CANCELLED: '已取消', COMPLETED: '已完成' }[s] || s },
    async updateStatus(id, status) {
      const text = { CONFIRMED: '确认', CANCELLED: '取消', COMPLETED: '完成' }[status]
      if (!confirm(`确定要将预约状态修改为"${text}"吗？`)) return
      try { await updateAppointmentStatus(id, status); this.showAlert('预约状态更新成功'); this.loadAppointments() } catch (e) { this.showAlert(e.message || '更新失败', 'danger') }
    },
    // ===== 用户 =====
    async loadUsers() { try { const res = await getUsers(); this.users = res.data || res || [] } catch (e) { console.error(e); this.showAlert('加载用户数据失败', 'danger') } },
    showUserModal(user = null) { this.userForm = user ? { ...user, password: '' } : { id: null, username: '', password: '', realName: '', phone: '', email: '', role: 'USER', enabled: true }; this.showUserModalFlag = true },
    async saveUser() {
      if (!this.userForm.username) { this.showAlert('请输入用户名', 'danger'); return }
      try { if (this.userForm.id) await updateUser(this.userForm.id, this.userForm); else await createUser(this.userForm); this.showUserModalFlag = false; this.showAlert('用户保存成功'); this.loadUsers() } catch (e) { this.showAlert(e.message || '保存失败', 'danger') }
    },
    async deleteUser(id) { if (!confirm('确定要删除这个用户吗？')) return; try { await delUser(id); this.showAlert('用户删除成功'); this.loadUsers() } catch (e) { this.showAlert(e.message || '删除失败', 'danger') } }
  },
  mounted() {
    // 管理员权限检查 - 匹配 JSP checkAdminAuth()
    const auth = getAuthState()
    if (!auth.loggedIn) { alert('请先登录系统'); this.$router.push('/login'); return }
    if (auth.role !== 'ADMIN') { alert('权限不足，只有管理员可以访问此页面'); this.$router.push('/'); return }
    this.loadDepartments()
  }
}
</script>


