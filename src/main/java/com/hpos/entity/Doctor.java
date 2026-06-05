package com.hpos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 医生表（doctor）实体类
 * <p>
 * 对应数据库表：doctor
 * 功能：存储医生详细信息，关联科室表
 * </p>
 */
@Data
@TableName("doctor")
public class Doctor {

    /** 医生ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 医生姓名 */
    private String realName;

    /** 职称（如：主任医师、副主任医师、主治医师） */
    private String title;

    /** 擅长领域 */
    private String specialty;

    /** 所属科室ID（关联 department.id） */
    private Integer deptId;

    /** 头像路径 */
    private String avatar;

    /** 状态：1=正常出诊，0=停诊 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // ========== 非数据库字段（用于前端展示） ==========

    /** 科室名称（关联查询结果，非数据库字段） */
    @TableField(exist = false)
    private String deptName;
}
