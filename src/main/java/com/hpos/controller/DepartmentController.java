package com.hpos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hpos.dto.ApiResponse;
import com.hpos.entity.Department;
import com.hpos.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 科室 API —— 首页展示所有科室列表
 * 
 * <h3>接口：</h3>
 * <pre>
 * GET /api/departments      → 获取所有正常运营的科室（按排序字段升序）
 * GET /api/departments/{id} → 获取单个科室详情
 * </pre>
 * 
 * <h3>前端使用：</h3>
 * 挂号页面的第一个下拉框 "选择科室" 就是调这个接口。
 * 用户选完科室后，前端再调 /api/doctors?deptId=xxx 加载医生列表。
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取所有正常运营的科室
     * 
     * <h4>返回：</h4>
     * <pre>
     * [
     *   { "id": 1, "deptName": "内科", "introduction": "治疗内科常见疾病...", "sortOrder": 1 },
     *   { "id": 2, "deptName": "外科", "introduction": "开展各类外科手术...", "sortOrder": 2 },
     *   { "id": 3, "deptName": "儿科", "introduction": "儿童疾病诊治...", "sortOrder": 3 }
     * ]
     * </pre>
     * 
     * 只返回 status=1（正常）的科室，按 sort_order 升序排列。
     */
    @GetMapping
    public ApiResponse<List<Department>> list() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getStatus, 1)
               .orderByAsc(Department::getSortOrder);
        List<Department> list = departmentService.list(wrapper);
        return ApiResponse.success(list);
    }

    /**
     * 根据ID获取科室详情
     * 
     * @param id 科室ID
     */
    @GetMapping("/{id}")
    public ApiResponse<Department> getById(@PathVariable Integer id) {
        Department dept = departmentService.getById(id);
        if (dept == null) {
            return ApiResponse.error(404, "科室不存在");
        }
        return ApiResponse.success(dept);
    }
}
