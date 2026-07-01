package com.hospital.controller.api;

import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者信息API控制器
 * 提供患者相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/patients")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    /**
     * 根据手机号查找患者
     *
     * @param phone 手机号
     * @return 返回患者信息，如果不存在返回空对象
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Patient> getPatientByPhone(@PathVariable("phone") String phone) {
        try {
            Patient patient = patientService.getPatientByPhone(phone);
            if (patient != null) {
                return new ResponseEntity<>(patient, HttpStatus.OK);
            } else {
                // 返回空患者对象而不是404，避免前端错误
                return new ResponseEntity<>(new Patient(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new Patient(), HttpStatus.OK);
        }
    }

    /**
     * 根据姓名搜索患者
     *
     * @param name 患者姓名（模糊匹配）
     * @return 返回匹配的患者列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatientsByName(@RequestParam("name") String name) {
        try {
            List<Patient> patients = patientService.searchPatientsByName(name);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            // 返回空列表而不是错误
            return new ResponseEntity<>(java.util.Collections.emptyList(), HttpStatus.OK);
        }
    }

    /**
     * 创建新的患者
     *
     * @param patient 患者信息
     * @return 返回创建结果
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPatient(@RequestBody Patient patient) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = patientService.createPatient(patient);
            if (success) {
                response.put("success", true);
                response.put("message", "患者信息创建成功");
                response.put("patient", patient);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("success", false);
                response.put("message", "患者信息创建失败");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建失败：" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID获取患者信息
     *
     * @param id 患者ID
     * @return 返回患者信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient != null) {
                return new ResponseEntity<>(patient, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
