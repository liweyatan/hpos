package com.hpos;

import com.hpos.service.impl.DepartmentServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智慧医院挂号系统 - 启动类
 * 
 * ===== 启动说明 =====
 * 1. 确保本地 MySQL 已运行，且已执行 HPOS.SQL 建库
 * 2. 检查 application.yml 中的数据库用户名和密码
 * 3. 运行本类 main 方法即可启动（默认端口 8080）
 * 4. 前端启动：cd untitled && npm run dev（默认端口 5173）
 * 
 * ===== 访问地址 =====
 * 后端 API: http://localhost:8080/api/...
 * 前端页面: http://localhost:5173
 * 
 * ===== 测试账号 =====
 * 用户名: admin / zhangsan / lisi
 * 密码:   123456（所有账号通用）
 */
@SpringBootApplication
public class HposApplication {

    public static void main(String[] args) {
        SpringApplication.run(HposApplication.class, args);

    }
}
