package com.hpos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 挂号订单表（registration_order）实体类
 * <p>
 * 对应数据库表：registration_order
 * 功能：存储患者的每一次挂号记录
 * </p>
 */
@Data
@TableName("registration_order")
public class RegistrationOrder {

    /** 订单ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 订单号（格式：REG + 日期 + 6位序号） */
    private String orderNo;

    /** 患者ID（关联 patient.id） */
    private Integer patientId;

    /** 号源ID（关联 registration_source.id） */
    private Integer sourceId;

    /** 医生ID（关联 doctor.id） */
    private Integer doctorId;

    /** 科室ID（关联 department.id） */
    private Integer deptId;

    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    /** 时段：1=上午，2=下午 */
    private Integer period;

    /** 挂号费用 */
    private BigDecimal fee;

    /**
     * 订单状态
     * 0=待支付
     * 1=已支付
     * 2=已取消
     * 3=已就诊
     */
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

    /** 患者姓名 */
    @TableField(exist = false)
    private String patientName;

    /** 医生姓名 */
    @TableField(exist = false)
    private String doctorName;

    /** 医生职称 */
    @TableField(exist = false)
    private String doctorTitle;

    /** 科室名称 */
    @TableField(exist = false)
    private String deptName;

    /** 时段中文描述 */
    @TableField(exist = false)
    private String periodText;

    /** 状态中文描述 */
    @TableField(exist = false)
    private String statusText;
}
