package com.hospital;  // 修改为顶层包

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 智慧医院管理系统 - Spring Boot主启动类
 * 移动到顶层包确保能扫描到所有组件
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hospital", "com.hospital.util"})
public class HospitalendApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HospitalendApplication.class, args);
        System.out.println("智慧医院管理系统启动成功！");
        System.out.println("请访问 http://localhost:8080/ 查看系统");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HospitalendApplication.class);
    }
}