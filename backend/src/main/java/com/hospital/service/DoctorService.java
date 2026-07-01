package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 医生业务服务类
 * 处理医生相关的业务逻辑
 */
@Service
@Transactional
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * 获取所有医生（包括不可用的）
     */
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * 获取所有可用医生
     */
    public List<Doctor> getAllActiveDoctors() {
        return doctorRepository.findAllActive();
    }

    /**
     * 根据ID获取医生信息
     */
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    /**
     * 根据科室ID获取医生列表
     */
    public List<Doctor> getDoctorsByDepartmentId(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }

    /**
     * 更新医生当前挂号数
     */
    public boolean updateDoctorCurrentPatients(Long id, Integer currentPatients) {
        return doctorRepository.updateCurrentPatients(id, currentPatients) > 0;
    }

    /**
     * 检查医生姓名是否已存在（同一科室内）
     */
    public boolean isDoctorNameExists(String name, Long departmentId, Long excludeId) {
        return doctorRepository.countByNameAndDepartment(name, departmentId, excludeId) > 0;
    }

    /**
     * 创建医生
     */
    public boolean createDoctor(Doctor doctor) {
        // 设置默认值
        if (doctor.getMaxPatients() == null) {
            doctor.setMaxPatients(20);
        }
        if (doctor.getCurrentPatients() == null) {
            doctor.setCurrentPatients(0);
        }
        if (doctor.getAvailable() == null) {
            doctor.setAvailable(true);
        }

        // 检查医生姓名是否已存在
        if (isDoctorNameExists(doctor.getName(), doctor.getDepartmentId(), 0L)) {
            throw new RuntimeException("该科室下已存在同名医生");
        }

        return doctorRepository.insert(doctor) > 0;
    }

    /**
     * 更新医生信息
     */
    public boolean updateDoctor(Doctor doctor) {
        // 检查医生姓名是否已存在（排除当前医生）
        if (isDoctorNameExists(doctor.getName(), doctor.getDepartmentId(), doctor.getId())) {
            throw new RuntimeException("该科室下已存在同名医生");
        }

        return doctorRepository.update(doctor) > 0;
    }

    /**
     * 删除医生
     */
    public boolean deleteDoctor(Long id) {
        return doctorRepository.deleteById(id) > 0;
    }
}