package com.hospital.controller.api;

import com.hospital.entity.RegistrationOrder;
import com.hospital.entity.Patient;
import com.hospital.service.RegistrationOrderService;
import com.hospital.service.DoctorScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预约订单API控制器
 * 提供预约订单相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/registration-orders")
public class RegistrationOrderApiController {

    @Autowired
    private RegistrationOrderService registrationOrderService;

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 获取患者的所有预约记录
     * @param patientId 患者ID
     * @return 返回患者的预约记录列表，如果出错返回错误状态和详细信息
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getOrdersByPatientId(@PathVariable("patientId") Long patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (patientId == null || patientId <= 0) {
                response.put("success", false);
                response.put("message", "患者ID无效");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            List<RegistrationOrder> orders = registrationOrderService.getRegistrationOrdersByPatientId(patientId);
            response.put("success", true);
            response.put("data", orders);
            response.put("count", orders.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取预约记录失败：" + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("count", 0);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 创建新的预约订单
     * @param order 预约订单信息
     * @return 返回创建结果，包含预约号和状态信息
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody RegistrationOrder order) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 生成唯一的预约号，格式为GH+8位随机字符串
            String appointmentNo = "GH" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // 设置预约号到订单对象
            order.setAppointmentNo(appointmentNo);
            
            boolean success = registrationOrderService.createRegistrationOrder(order);
            if (success) {
                response.put("success", true);
                response.put("message", "预约成功");
                response.put("appointmentNo", appointmentNo);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("success", false);
                response.put("message", "预约失败");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "预约失败，请稍后重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 创建新的挂号订单（支持病人信息自动创建）
     * @param order 挂号订单信息
     * @return 返回创建结果和预约号
     */
    @PostMapping("/with-patient")
    public ResponseEntity<Map<String, Object>> createOrderWithPatient(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 解析预约信息
            RegistrationOrder order = new RegistrationOrder();
            order.setDoctorId(Long.valueOf(requestData.get("doctorId").toString()));
            order.setRegisterTime(java.time.LocalDateTime.parse(requestData.get("registerTime").toString()));
            order.setSymptoms(requestData.get("symptoms") != null ? requestData.get("symptoms").toString() : "");
            order.setNotes(requestData.get("notes") != null ? requestData.get("notes").toString() : "");
            
            // 解析病人信息
            Patient patientInfo = new Patient();
            patientInfo.setName(requestData.get("patientName").toString());
            patientInfo.setPhone(requestData.get("patientPhone").toString());
            
            // 处理身份证号字段：如果为空字符串则设置为null，避免唯一约束冲突
            String idCard = requestData.get("patientIdCard").toString();
            patientInfo.setIdCard(idCard != null && !idCard.trim().isEmpty() ? idCard : null);
            
            patientInfo.setGender(requestData.get("patientGender") != null ? requestData.get("patientGender").toString() : null);
            
            boolean success = registrationOrderService.createRegistrationOrderWithPatient(order, patientInfo);
            if (success) {
                response.put("success", true);
                response.put("message", "预约成功");
                response.put("appointmentNo", order.getAppointmentNo());
                response.put("patientId", order.getPatientId());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("success", false);
                response.put("message", "预约失败");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "预约失败，请稍后重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新预约订单状态
     * @param id 预约订单ID
     * @param status 新的状态
     * @return 返回更新结果和状态信息
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = registrationOrderService.updateOrderStatus(id, status);
            if (success) {
                response.put("success", true);
                response.put("message", "状态更新成功");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "状态更新失败");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "操作失败，请稍后重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询医生某天已预约的时间段
     */
    @GetMapping("/doctor/{doctorId}/booked-slots")
    public ResponseEntity<Map<String, Object>> getBookedSlots(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam("date") String date) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<RegistrationOrder> orders = registrationOrderService.getRegistrationOrdersByDoctorId(doctorId);
            String targetDate = date;
            List<String> bookedSlots = orders.stream()
                .filter(o -> o.getRegisterTime() != null && !o.getStatus().equals("CANCELLED"))
                .filter(o -> o.getRegisterTime().toString().startsWith(targetDate))
                .map(o -> {
                    int hour = o.getRegisterTime().getHour();
                    int minute = o.getRegisterTime().getMinute();
                    return String.format("%02d:%02d", hour, minute);
                })
                .collect(Collectors.toList());
            response.put("success", true);
            response.put("data", bookedSlots);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "查询失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取医生某天可预约的时间段（排班减去已预约）
     */
    @GetMapping("/doctor/{doctorId}/available-slots")
    public ResponseEntity<Map<String, Object>> getAvailableSlots(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam("date") String date) {
        Map<String, Object> response = new HashMap<>();
        try {
            java.time.LocalDate targetDate = java.time.LocalDate.parse(date);
            int dayOfWeek = targetDate.getDayOfWeek().getValue();
            List<String> allSlots = doctorScheduleService.generateTimeSlots(doctorId, dayOfWeek);

            List<RegistrationOrder> booked = registrationOrderService.getRegistrationOrdersByDoctorId(doctorId);
            List<String> bookedTimes = booked.stream()
                .filter(o -> o.getRegisterTime() != null && !o.getStatus().equals("CANCELLED"))
                .filter(o -> o.getRegisterTime().toLocalDate().equals(targetDate))
                .map(o -> String.format("%02d:%02d", o.getRegisterTime().getHour(), o.getRegisterTime().getMinute()))
                .collect(Collectors.toList());

            List<Map<String, Object>> slots = new ArrayList<>();
            for (String slot : allSlots) {
                Map<String, Object> slotMap = new HashMap<>();
                slotMap.put("time", slot);
                slotMap.put("available", !bookedTimes.contains(slot));
                slots.add(slotMap);
            }
            response.put("success", true);
            response.put("data", slots);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "查询失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除预约订单
     * @param id 预约订单ID
     * @return 返回删除结果和状态信息
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = registrationOrderService.deleteRegistrationOrder(id);
            if (success) {
                response.put("success", true);
                response.put("message", "预约已取消");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "操作失败");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "操作失败，请稍后重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
