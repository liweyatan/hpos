package com.hpos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者表（patient）实体类
 * <p>
 * 对应数据库表：patient
 * 功能：存储患者基本信息
 * </p>
 */
@Data
@TableName("patient")
public class Patient {

    /** 患者ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 患者姓名 */
    private String realName;

    /** 性别：1=男，2=女 */
    private Integer gender;

    /** 手机号（唯一） */
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
