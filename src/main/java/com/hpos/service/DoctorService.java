package com.hpos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpos.entity.Doctor;
import java.util.List;

/**
 * 医生 Service 接口
 */
public interface DoctorService extends IService<Doctor> {

    /**
     * 根据科室ID查询医生列表
     *
     * @param deptId 科室ID
     * @return 医生列表
     */
    List<Doctor> getDoctorsByDeptId(Integer deptId);
}
