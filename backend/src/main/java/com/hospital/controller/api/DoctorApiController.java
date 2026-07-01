package com.hospital.controller.api;

import com.hospital.entity.Doctor;
import com.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生API控制器
 * 提供医生相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorApiController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取所有可用医生
     *
     * @return ResponseEntity 包含医生列表和HTTP状态码
     *         成功时返回200 OK状态码和医生列表
     *         异常时返回500 INTERNAL_SERVER_ERROR状态码
     */
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        try {
            // 调用服务层获取所有活跃医生
            List<Doctor> doctors = doctorService.getAllActiveDoctors();
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch (Exception e) {
            // 发生异常时返回服务器错误状态码
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据科室ID获取医生列表
     *
     * @param departmentId 科室ID
     * @return ResponseEntity 包含医生列表和HTTP状态码
     *         成功时返回200 OK状态码和医生列表
     *         异常时返回500 INTERNAL_SERVER_ERROR状态码
     */
    @GetMapping("/department/{id}")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartmentId(@PathVariable("id") Long departmentId) {
        try {
            // 调用服务层根据科室ID获取医生列表
            List<Doctor> doctors = doctorService.getDoctorsByDepartmentId(departmentId);
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch (Exception e) {
            // 发生异常时返回服务器错误状态码
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID获取医生信息
     *
     * @param id 医生ID
     * @return ResponseEntity 包含医生信息和HTTP状态码
     *         找到医生时返回200 OK状态码和医生信息
     *         未找到医生时返回404 NOT_FOUND状态码
     *         异常时返回500 INTERNAL_SERVER_ERROR状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") Long id) {
        try {
            // 调用服务层根据ID获取医生信息
            Doctor doctor = doctorService.getDoctorById(id);
            if (doctor != null) {
                // 找到医生时返回医生信息和200状态码
                return new ResponseEntity<>(doctor, HttpStatus.OK);
            } else {
                // 未找到医生时返回404状态码
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // 发生异常时返回服务器错误状态码
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
