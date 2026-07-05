package com.hospital.service;

import com.hospital.entity.RegistrationOrder;
import com.hospital.entity.Patient;
import com.hospital.repository.RegistrationOrderRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 挂号订单业务服务类
 * 处理挂号订单相关的业务逻辑
 */
@Service
@Transactional
public class RegistrationOrderService {

    @Autowired
    private RegistrationOrderRepository registrationOrderRepository;
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 获取所有挂号订单（包含患者、医生、科室信息）
     */
    public List<RegistrationOrder> getAllRegistrationOrders() {
        return registrationOrderRepository.findAllWithDetails();
    }

    /**
     * 获取所有预约（为管理员后台提供）
     */
    public List<RegistrationOrder> getAllAppointments() {
        return registrationOrderRepository.findAllWithDetails();
    }

    /**
     * 更新预约状态（为管理员后台提供）
     */
    public boolean updateAppointmentStatus(Long id, String status) {
        return updateOrderStatus(id, status);
    }

    /**
     * 根据ID获取挂号订单详情
     */
    public RegistrationOrder getRegistrationOrderById(Long id) {
        return registrationOrderRepository.findByIdWithDetails(id);
    }

    /**
     * 根据患者ID获取挂号订单
     */
    public List<RegistrationOrder> getRegistrationOrdersByPatientId(Long patientId) {
        return registrationOrderRepository.findByPatientId(patientId);
    }

    /**
     * 根据患者ID和状态获取预约数据
     * 如果patientId为null，则获取所有预约（管理员权限）
     */
    public List<RegistrationOrder> getAppointmentsByPatientId(Long patientId, String status) {
        List<RegistrationOrder> orders;
        
        if (patientId == null) {
            // 管理员权限：获取所有预约
            orders = registrationOrderRepository.findAllWithDetails();
        } else {
            // 普通用户权限：获取特定患者的预约
            orders = registrationOrderRepository.findByPatientId(patientId);
        }

        // 如果指定了状态，则进行过滤（忽略大小写）
        if (status != null && !status.trim().isEmpty()) {
            orders = orders.stream()
                    .filter(order -> status.equalsIgnoreCase(order.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }

        return orders;
    }

    /**
     * 取消预约
     */
    public boolean cancelAppointment(Long id) {
        RegistrationOrder order = registrationOrderRepository.findByIdWithDetails(id);
        if (order == null) {
            return false;
        }

        // 检查是否允许取消（只有PENDING和CONFIRMED状态的预约可以取消）
        if (!"PENDING".equalsIgnoreCase(order.getStatus()) && !"CONFIRMED".equalsIgnoreCase(order.getStatus())) {
            return false;
        }

        // 更新状态为CANCELLED
        return registrationOrderRepository.updateStatus(id, "CANCELLED") > 0;
    }

    /**
     * 根据医生ID获取挂号订单
     */
    public List<RegistrationOrder> getRegistrationOrdersByDoctorId(Long doctorId) {
        return registrationOrderRepository.findByDoctorId(doctorId);
    }

    /**
     * 创建新的挂号订单（支持病人信息自动创建）
     */
    public boolean createRegistrationOrder(RegistrationOrder order) {
        // 验证病人ID是否存在
        if (order.getPatientId() == null) {
            throw new RuntimeException("病人ID不能为空");
        }
        
        Patient patient = patientRepository.findById(order.getPatientId());
        if (patient == null) {
            // 如果病人不存在，尝试根据手机号查找或创建新病人
            patient = patientRepository.findByPhone(order.getPatientPhone());
            if (patient == null) {
                // 创建新的病人信息
                Patient newPatient = new Patient();
                newPatient.setName(order.getPatientName());
                newPatient.setPhone(order.getPatientPhone());
                newPatient.setIdCard(order.getPatientIdCard());
                newPatient.setGender(order.getPatientGender());
                
                if (patientRepository.insert(newPatient) > 0) {
                    // 设置新创建的病人ID
                    order.setPatientId(newPatient.getId());
                } else {
                    throw new RuntimeException("创建病人信息失败");
                }
            } else {
                // 使用已存在的病人ID
                order.setPatientId(patient.getId());
            }
        }

        // 检查同一医生同一小时是否已约满
        int dayOfWeek = order.getRegisterTime().getDayOfWeek().getValue();
        int maxPerHour = doctorScheduleService.getMaxPerHour(order.getDoctorId(), dayOfWeek);
        int count = registrationOrderRepository.countByDoctorAndHour(order.getDoctorId(), order.getRegisterTime());
        if (count >= maxPerHour) {
            throw new RuntimeException("该医生该时间段已约满");
        }

        // 设置默认状态（使用大写，与数据库一致）
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        
        // 执行插入
        return registrationOrderRepository.insert(order) > 0;
    }
    
    /**
     * 创建挂号订单（包含病人信息）
     */
    public boolean createRegistrationOrderWithPatient(RegistrationOrder order, Patient patientInfo) {
        // 验证病人信息
        if (patientInfo.getName() == null || patientInfo.getPhone() == null) {
            throw new RuntimeException("病人姓名和手机号不能为空");
        }
        
        // 检查手机号是否已存在
        Patient existingPatient = patientRepository.findByPhone(patientInfo.getPhone());
        if (existingPatient != null) {
            // 使用已存在的病人ID
            order.setPatientId(existingPatient.getId());
        } else {
            // 创建新病人（身份证号改为可选）
            // 如果身份证号为空，保持为null而不是空字符串，避免唯一约束冲突
            
            if (patientRepository.insert(patientInfo) > 0) {
                order.setPatientId(patientInfo.getId());
            } else {
                throw new RuntimeException("创建病人信息失败");
            }
        }
        
        // 检查同一医生同一小时是否已约满
        int dayOfWeek = order.getRegisterTime().getDayOfWeek().getValue();
        int maxPerHour = doctorScheduleService.getMaxPerHour(order.getDoctorId(), dayOfWeek);
        int count = registrationOrderRepository.countByDoctorAndHour(order.getDoctorId(), order.getRegisterTime());
        if (count >= maxPerHour) {
            throw new RuntimeException("该医生该时间段已约满");
        }

        // 设置默认状态
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        
        // 生成预约号
        String appointmentNo = "GH" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setAppointmentNo(appointmentNo);
        
        // 执行插入
        return registrationOrderRepository.insert(order) > 0;
    }

    /**
     * 更新挂号订单状态
     */
    public boolean updateOrderStatus(Long id, String status) {
        return registrationOrderRepository.updateStatus(id, status) > 0;
    }

    /**
     * 更新挂号订单信息
     */
    public boolean updateRegistrationOrder(RegistrationOrder order) {
        return registrationOrderRepository.update(order) > 0;
    }

    /**
     * 删除挂号订单
     */
    public boolean deleteRegistrationOrder(Long id) {
        return registrationOrderRepository.deleteById(id) > 0;
    }

    /**
     * 统计医生当天的预约数量
     */
    public int countDoctorAppointmentsByDate(Long doctorId, LocalDateTime date) {
        return registrationOrderRepository.countByDoctorAndDate(doctorId, date);
    }
}