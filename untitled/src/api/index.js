/**
 * API 接口封装文件
 * 
 * 集中管理所有后端 API 调用，方便维护和修改
 * 使用 axios 发送 HTTP 请求
 */

import axios from 'axios';

// ========== 创建 axios 实例 ==========
const request = axios.create({
  baseURL: '/api',          // 基础路径（开发环境通过 Vite proxy 转发到后端）
  timeout: 10000,           // 请求超时时间（10秒）
  headers: {
    'Content-Type': 'application/json'
  }
});

// ========== 响应拦截器 ==========
// 统一处理响应数据，只提取 data 部分
request.interceptors.response.use(
  (response) => {
    // 后端返回格式：{ code, message, data }
    const res = response.data;
    if (res.code !== 200) {
      console.error('API 错误：', res.message);
      return Promise.reject(new Error(res.message || '请求失败'));
    }
    return res.data; // 直接返回 data 部分
  },
  (error) => {
    console.error('网络错误：', error.message);
    return Promise.reject(error);
  }
);

// ==================== 科室相关 ====================

/** 获取所有正常科室 */
export function getDepartments() {
  return request.get('/departments');
}

// ==================== 医生相关 ====================

/** 根据科室ID获取医生列表 */
export function getDoctorsByDeptId(deptId) {
  return request.get('/doctors', { params: { deptId } });
}

// ==================== 号源相关 ====================

/** 获取医生排班 */
export function getDoctorSchedule(doctorId, startDate, endDate) {
  return request.get('/sources/schedule', {
    params: { doctorId, startDate, endDate }
  });
}

// ==================== 挂号订单相关 ====================

/** 提交挂号 */
export function createOrder(orderData) {
  return request.post('/orders', orderData);
}

/** 查询挂号记录 */
export function getOrders(patientId) {
  return request.get('/orders', { params: { patientId } });
}

/** 取消挂号 */
export function cancelOrder(id, patientId) {
  return request.put(`/orders/${id}/cancel`, null, {
    params: { patientId }
  });
}

// ==================== 认证相关 ====================

/** 用户登录 */
export function login(username, password) {
  return request.post('/auth/login', { username, password });
}

/** 获取用户信息 */
export function getUserInfo(username) {
  return request.get(`/auth/user/${username}`);
}

export default request;
