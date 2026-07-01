package com.hospital.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者信息实体类
 * 对应数据库patient表
 */
@Data
public class Patient {
    private Long id;                    // 患者ID
    private String name;               // 患者姓名
    private String idCard;             // 身份证号
    private String phone;              // 手机号
    private String gender;             // 性别
    private LocalDate birthDate;       // 出生日期
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
}