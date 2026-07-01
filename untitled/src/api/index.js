import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true,
})

request.interceptors.response.use(
  (response) => {
    const data = response.data
    if (data && typeof data === 'object' && 'success' in data) {
      if (!data.success) {
        return Promise.reject(new Error(data.message || '请求失败'))
      }
    }
    return data
  },
  (error) => {
    const msg = error.response?.data?.message || error.message || '网络错误'
    return Promise.reject(new Error(msg))
  },
)

// ==================== 认证 ====================

export function login(username, password) {
  return request.post('/auth/login', { username, password })
}

export function register(data) {
  return request.post('/auth/register', data)
}

// ==================== 科室 ====================

export function getDepartments() {
  return request.get('/departments')
}

export function createDepartment(data) {
  return request.post('/admin/departments', data)
}

export function updateDepartment(id, data) {
  return request.put(`/admin/departments/${id}`, data)
}

export function deleteDepartment(id) {
  return request.delete(`/admin/departments/${id}`)
}

// ==================== 医生 ====================

export function getDoctors() {
  return request.get('/doctors')
}

export function getDoctorsByDepartment(deptId) {
  return request.get(`/doctors/department/${deptId}`)
}

export function getDoctorById(id) {
  return request.get(`/doctors/${id}`)
}

export function createDoctor(data) {
  return request.post('/admin/doctors', data)
}

export function updateDoctor(id, data) {
  return request.put(`/admin/doctors/${id}`, data)
}

export function deleteDoctor(id) {
  return request.delete(`/admin/doctors/${id}`)
}

// ==================== 号源/排班 ====================

export function getDoctorSchedule(doctorId, startDate, endDate) {
  return request.get('/sources/schedule', { params: { doctorId, startDate, endDate } })
}

// ==================== 患者 ====================

export function findPatientByPhone(phone) {
  return request.get(`/patients/phone/${phone}`)
}

// ==================== 挂号订单 ====================

export function createOrder(orderData) {
  return request.post('/registration-orders/with-patient', orderData)
}

export function getOrders(patientId) {
  if (patientId) {
    return request.get(`/registration-orders/patient/${patientId}`)
  }
  return Promise.resolve({ data: [], count: 0 })
}

export function cancelOrder(id) {
  return request.put(`/registration-orders/${id}/status`, null, { params: { status: 'CANCELLED' } })
}

// ==================== 预约管理（管理员） ====================

export function getAppointments() {
  return request.get('/admin/appointments')
}

export function updateAppointmentStatus(id, status) {
  return request.put(`/admin/appointments/${id}/status`, { status })
}

// ==================== 用户管理 ====================

export function getUsers() {
  return request.get('/admin/users')
}

export function createUser(data) {
  return request.post('/admin/users', data)
}

export function updateUser(id, data) {
  return request.put(`/admin/users/${id}`, data)
}

export function deleteUser(id) {
  return request.delete(`/admin/users/${id}`)
}

export default request
