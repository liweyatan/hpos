package com.hospital.service;

import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 患者信息服务类
 * 处理患者相关的业务逻辑
 */
@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * 根据手机号获取患者信息
     */
    public Patient getPatientByPhone(String phone) {
        return patientRepository.findByPhone(phone);
    }

    /**
     * 根据姓名搜索患者（模糊匹配）
     */
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContaining(name);
    }

    /**
     * 创建新的患者
     */
    public boolean createPatient(Patient patient) {
        // 检查手机号是否已存在
        Patient existingPatient = patientRepository.findByPhone(patient.getPhone());
        if (existingPatient != null) {
            throw new RuntimeException("该手机号已存在");
        }

        // 执行插入
        return patientRepository.insert(patient) > 0;
    }

    /**
     * 根据ID获取患者信息
     */
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * 更新患者信息
     */
    public boolean updatePatient(Patient patient) {
        return patientRepository.update(patient) > 0;
    }

    /**
     * 删除患者信息
     */
    public boolean deletePatient(Long id) {
        return patientRepository.deleteById(id) > 0;
    }

    /**
     * 获取所有患者信息
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}