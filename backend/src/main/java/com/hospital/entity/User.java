package com.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户信息实体类
 * 对应数据库user表
 */
@Data
public class User {
    private Long id;                    // 用户ID
    private String username;           // 用户名
    private String password;           // 密码
    private String role;               // 用户角色
    private String email;              // 邮箱
    private String phone;              // 手机号
    private String realName;           // 真实姓名
    private Boolean enabled;           // 是否启用
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
}