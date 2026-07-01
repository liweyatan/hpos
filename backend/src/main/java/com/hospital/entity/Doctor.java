package com.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 医生信息实体类
 * 对应数据库doctor表，移除数据库中不存在的字段
 */
@Data
public class Doctor {
    private Long id;                    // 医生ID，对应数据库id列
    private String name;               // 医生姓名，对应数据库name列
    private Long departmentId;         // 所属科室ID，对应数据库department_id列
    private String title;              // 职称，对应数据库title列
    private String specialty;          // 专长，对应数据库specialty列
    private Integer maxPatients;       // 单日最大接诊数，对应数据库max_patients列
    private Integer currentPatients;   // 当前已挂号数，对应数据库current_patients列
    private Boolean available;         // 是否可预约，对应数据库available列
    private LocalDateTime createTime;  // 创建时间，对应数据库create_time列
    private LocalDateTime updateTime;  // 更新时间，对应数据库update_time列

    // 关联字段（非数据库字段，用于JOIN查询）
    private String departmentName;     // 科室名称
}