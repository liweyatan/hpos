package com.hospital.controller.api;

import com.hospital.entity.Department;
import com.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 科室API控制器类
 * 提供科室相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentApiController {

    /**
     * 科室服务类
     * 用于处理科室相关的业务逻辑
     */
    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取所有启用的科室
     *
     * @return ResponseEntity 包含科室列表和HTTP状态码
     *         - 成功时返回200 OK状态码和科室列表
     *         - 异常时返回500 INTERNAL_SERVER_ERROR状态码
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        try {
            // 调用服务层获取所有启用的科室
            List<Department> departments = departmentService.getAllActiveDepartments();
            // 返回成功响应，包含科室列表和200状态码
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (Exception e) {
            // 发生异常时返回500状态码
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
