package com.hospital.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * 修复Repository无法解析的问题
 */
@Configuration
@MapperScan("com.hospital.repository")
public class MyBatisConfig {
    // 通过@MapperScan注解确保Repository接口被正确扫描
}