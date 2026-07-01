package com.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 挂号订单实体类
 * 严格按照数据库列名设计，字段名与数据库列名保持一致
 * 包含关联查询所需的扩展字段
 */
@Data
public class RegistrationOrder {
    private Long id;                    // 订单ID
    private Long patientId;            // 患者ID
    private Long doctorId;             // 医生ID
    private LocalDateTime registerTime;   // 预约就诊时间 (对应数据库register_time)
    private String status;            // 订单状态 (对应数据库status)
    private String symptoms;           // 症状描述
    private String notes;              // 备注信息
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
    private String appointmentNo;      // 预约号

    // 关联查询扩展字段
    private String patientName;        // 患者姓名
    private String doctorName;         // 医生姓名
    private String departmentName;     // 科室名称
    
    // 病人信息字段（用于创建新病人）
    private String patientPhone;       // 病人手机号
    private String patientIdCard;      // 病人身份证号
    private String patientGender;      // 病人性别
}