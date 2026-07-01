package com.hospital.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 科室信息实体类
 * 对应数据库department表，字段名与列名保持一致
 */
@Data
public class Department {
    private Long id;                    // 科室ID，对应数据库id列
    private String name;               // 科室名称，对应数据库name列（原deptName不匹配）
    private String description;        // 科室描述，对应数据库description列
    private String director;           // 科室负责人，对应数据库director列（原缺失）
    private String phone;              // 联系电话，对应数据库phone列（原缺失）
    private String location;           // 科室位置，对应数据库location列（原缺失）
    private Integer active;            // 是否启用（1启用，0禁用），对应数据库active列
    private LocalDateTime createTime;  // 创建时间，对应数据库create_time列
    private LocalDateTime updateTime;  // 更新时间，对应数据库update_time列

    // 构造方法
    public Department() {}

    public Department(String name, String description, String director, String phone, String location) {
        this.name = name;
        this.description = description;
        this.director = director;
        this.phone = phone;
        this.location = location;
        this.active = 1; // 默认启用
    }
}