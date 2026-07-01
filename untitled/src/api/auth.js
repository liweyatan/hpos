import { reactive } from 'vue'

const authState = reactive({
  loggedIn: localStorage.getItem('loggedIn') === 'true',
  username: localStorage.getItem('username') || '',
  realName: localStorage.getItem('realName') || '',
  role: localStorage.getItem('role') || '',
})

export function setLogin(user) {
  localStorage.setItem('loggedIn', 'true')
  localStorage.setItem('userId', String(user.id))
  localStorage.setItem('username', user.username)
  localStorage.setItem('realName', user.realName || user.username)
  localStorage.setItem('phone', user.phone || '')
  localStorage.setItem('role', user.role || 'PATIENT')

  authState.loggedIn = true
  authState.username = user.username
  authState.realName = user.realName || user.username
  authState.role = user.role || 'PATIENT'
}

export function setLogout() {
  localStorage.removeItem('loggedIn')
  localStorage.removeItem('userId')
  localStorage.removeItem('username')
  localStorage.removeItem('realName')
  localStorage.removeItem('phone')
  localStorage.removeItem('role')
  localStorage.removeItem('patientId')
  localStorage.removeItem('patientName')

  authState.loggedIn = false
  authState.username = ''
  authState.realName = ''
  authState.role = ''
}

export function getAuthState() {
  return authState
}
