package com.hospital.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 挂号订单数据传输对象
 * 用于包含关联查询结果的订单信息展示
 */
@Data
public class RegistrationOrderDTO {
    private Long id;                    // 订单ID
    private Long patientId;            // 患者ID
    private Long doctorId;             // 医生ID
    private LocalDateTime registerTime;   // 预约就诊时间
    private String status;            // 订单状态
    private String symptoms;           // 症状描述
    private String notes;              // 备注信息
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间

    // 关联查询字段
    private String patientName;        // 患者姓名
    private String doctorName;         // 医生姓名
    private String departmentName;     // 科室名称

    public RegistrationOrderDTO() {
    }

    public RegistrationOrderDTO(Long id, Long patientId, Long doctorId,
                                LocalDateTime registerTime, String status,
                                String symptoms, String notes,
                                LocalDateTime createTime, LocalDateTime updateTime,
                                String patientName, String doctorName, String departmentName) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.registerTime = registerTime;
        this.status = status;
        this.symptoms = symptoms;
        this.notes = notes;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
    }
}