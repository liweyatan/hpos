package com.hospital.controller;

import com.hospital.entity.Department;
import com.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室管理控制器
 * 处理科室相关的HTTP请求
 *
 * 该控制器负责科室的CRUD操作，包括：
 * - 显示科室列表
 * - 添加新科室
 * - 编辑现有科室
 * - 删除科室
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 科室列表页面
     * 获取所有活跃科室并添加到模型中，返回科室列表视图
     *
     * @param model 用于传递数据到视图
     * @return 科室列表页面视图名称
     */
    @GetMapping("/list")
    public String listDepartments(Model model) {
        List<Department> departments = departmentService.getAllActiveDepartments();
        model.addAttribute("departments", departments);
        return "department-list";
    }
}