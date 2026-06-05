// src/data/mockData.js
export const departments = [
    { id: 1, name: '内科', description: '治疗感冒、发烧、消化系统疾病' },
    { id: 2, name: '外科', description: '手术治疗、创伤处理' },
    { id: 3, name: '儿科', description: '儿童疾病诊治' },
    { id: 4, name: '妇产科', description: '妇科和产科疾病' },
    { id: 5, name: '眼科', description: '眼部疾病治疗' }
];

export const doctors = [
    { id: 1, name: '张医生', departmentId: 1, title: '主任医师', schedule: '周一至周五 上午' },
    { id: 2, name: '李医生', departmentId: 1, title: '副主任医师', schedule: '周一至周六 全天' },
    { id: 3, name: '王医生', departmentId: 2, title: '主任医师', schedule: '周二、周四、周六' },
    { id: 4, name: '赵医生', departmentId: 3, title: '主治医师', schedule: '周一至周五 全天' },
    { id: 5, name: '刘医生', departmentId: 4, title: '副主任医师', schedule: '周三、周五、周日' }
];

export const timeSlots = [
    '08:00-09:00', '09:00-10:00', '10:00-11:00',
    '14:00-15:00', '15:00-16:00', '16:00-17:00'
];

export const registrationTypes = [
    { id: 1, name: '普通号', price: 10 },
    { id: 2, name: '专家号', price: 30 },
    { id: 3, name: '急诊号', price: 50 }
];