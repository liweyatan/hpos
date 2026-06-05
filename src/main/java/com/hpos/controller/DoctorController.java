package com.hpos.controller;

import com.hpos.dto.ApiResponse;
import com.hpos.entity.Doctor;
import com.hpos.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 医生 API —— 根据科室查医生
 * 
 * <h3>接口：</h3>
 * <pre>
 * GET /api/doctors?deptId=1 → 获取内科的医生列表
 * GET /api/doctors/{id}     → 获取某个医生的详细信息
 * </pre>
 * 
 * <h3>前端使用：</h3>
 * 用户选完科室后，前端调这个接口加载该科室下的医生列表，
 * 展示在 "选择医生" 下拉框中。
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 根据科室ID查询该科室下正常出诊的医生
     * 
     * <h4>返回示例：</h4>
     * <pre>
     * [
     *   { "id": 1, "realName": "张明", "title": "主任医师", "specialty": "心血管疾病、高血压", "deptId": 1 },
     *   { "id": 2, "realName": "李华", "title": "副主任医师", "specialty": "消化内科、胃肠疾病", "deptId": 1 }
     * ]
     * </pre>
     * 
     * @param deptId 科室ID（必传），如 deptId=1 表示内科
     */
    @GetMapping
    public ApiResponse<List<Doctor>> listByDept(@RequestParam Integer deptId) {
        List<Doctor> doctors = doctorService.getDoctorsByDeptId(deptId);
        return ApiResponse.success(doctors);
    }

    /**
     * 获取医生详情
     * 
     * @param id 医生ID
     */
    @GetMapping("/{id}")
    public ApiResponse<Doctor> getById(@PathVariable Integer id) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return ApiResponse.error(404, "医生不存在");
        }
        return ApiResponse.success(doctor);
    }
}
