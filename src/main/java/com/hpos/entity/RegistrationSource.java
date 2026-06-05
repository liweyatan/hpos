package com.hpos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 号源表（registration_source）实体类
 * <p>
 * 对应数据库表：registration_source
 * 功能：存储医生每天上/下午的可预约号源信息
 * </p>
 */
@Data
@TableName("registration_source")
public class RegistrationSource {

    /** 号源ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 医生ID（关联 doctor.id） */
    private Integer doctorId;

    /** 出诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    /** 时段：1=上午，2=下午 */
    private Integer period;

    /** 总号数（默认20） */
    private Integer totalCount;

    /** 剩余号数 */
    private Integer availableCount;

    /** 挂号费用 */
    private BigDecimal fee;

    /** 状态：1=可预约，0=停止预约 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // ========== 非数据库字段（前端展示用） ==========

    /** 时段中文描述（1=上午/2=下午） */
    @TableField(exist = false)
    private String periodText;

    /** 医生姓名 */
    @TableField(exist = false)
    private String doctorName;

    /** 医生职称 */
    @TableField(exist = false)
    private String doctorTitle;

    /** 科室名称 */
    @TableField(exist = false)
    private String deptName;
}
