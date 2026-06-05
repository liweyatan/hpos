package com.hpos.controller;

import com.hpos.dto.ApiResponse;
import com.hpos.entity.Patient;
import com.hpos.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 患者 Controller
 * <p>
 * 基础路径：/api/patients
 * </p>
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * 根据手机号查询患者信息
     * <p>
     * GET /api/patients/phone/{phone}
     * </p>
     */
    @GetMapping("/phone/{phone}")
    public ApiResponse<Patient> findByPhone(@PathVariable String phone) {
        Patient patient = patientService.findByPhone(phone);
        return ApiResponse.success(patient);
    }
}
