import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      window.location.href = '/#/login'
      return Promise.reject(new Error('未登录或Token已过期'))
    }
    if (res.code !== 200) {
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/#/login'
    }
    return Promise.reject(error)
  },
)

// ==================== 认证 ====================

export function login(username, password) {
  return request.post('/auth/login', { username, password })
}

export function getUserInfo(username) {
  return request.get(`/auth/user/${username}`)
}

// ==================== 科室 ====================

export function getDepartments() {
  return request.get('/departments')
}

// ==================== 医生 ====================

export function getDoctorsByDeptId(deptId) {
  return request.get('/doctors', { params: { deptId } })
}

// ==================== 号源 ====================

export function getDoctorSchedule(doctorId, startDate, endDate) {
  return request.get('/sources/schedule', { params: { doctorId, startDate, endDate } })
}

// ==================== 患者 ====================

export function findPatientByPhone(phone) {
  return request.get(`/patients/phone/${phone}`)
}

// ==================== 挂号订单 ====================

export function createOrder(orderData) {
  return request.post('/orders', orderData)
}

export function getOrders(patientId, page = 1, size = 10) {
  return request.get('/orders', { params: { patientId, page, size } }).then(res => res.list)
}

export function cancelOrder(id, patientId) {
  return request.put(`/orders/${id}/cancel`, null, { params: { patientId } })
}

export default request
