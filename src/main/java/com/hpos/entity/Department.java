package com.hpos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 科室表（department）实体类
 * <p>
 * 对应数据库表：department
 * 功能：存储医院所有科室信息，如内科、外科、儿科等
 * </p>
 */
@Data                              // Lombok：自动生成 getter/setter/toString/equals/hashCode
@TableName("department")           // 指定数据库表名（默认驼峰转下划线，但这里表名就是 department）
public class Department {

    /** 科室ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 科室名称 */
    private String deptName;

    /** 科室简介 */
    private String introduction;

    /** 排序（数字越小越靠前） */
    private Integer sortOrder;

    /** 状态：1=正常，0=停用 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
