package com.hpos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统用户表（sys_user）实体类
 * <p>
 * 对应数据库表：sys_user
 * 功能：存储用户的登录账号信息（登录用）
 * </p>
 */
@Data
@TableName("sys_user")
public class SysUser {

    /** 用户ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 用户名（唯一，登录用） */
    private String username;

    /** 密码（MD5 加密存储） */
    private String password;

    /** 手机号（唯一） */
    private String phone;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
