package com.hpos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.entity.Doctor;
import com.hpos.mapper.DoctorMapper;
import com.hpos.service.DoctorService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 医生 Service 实现类
 */
@Service
public class DoctorServiceImpl
        extends ServiceImpl<DoctorMapper, Doctor>
        implements DoctorService {

    /**
     * 根据科室ID查询该科室下状态正常的医生
     * 按姓名排序
     */
    @Override
    public List<Doctor> getDoctorsByDeptId(Integer deptId) {
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getDeptId, deptId)       // dept_id = ?
              .eq(Doctor::getStatus, 1)             // status = 1（正常出诊）
              .orderByAsc(Doctor::getRealName);     // 按姓名排序
        return this.list(wrapper);
    }
}
