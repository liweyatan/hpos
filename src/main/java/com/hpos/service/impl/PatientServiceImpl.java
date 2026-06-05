package com.hpos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.entity.Patient;
import com.hpos.mapper.PatientMapper;
import com.hpos.service.PatientService;
import org.springframework.stereotype.Service;

/**
 * 患者 Service 实现类
 */
@Service
public class PatientServiceImpl
        extends ServiceImpl<PatientMapper, Patient>
        implements PatientService {

    /**
     * 根据手机号查找患者
     * 手机号是唯一的（数据库有唯一索引）
     */
    @Override
    public Patient findByPhone(String phone) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getPhone, phone);
        return this.getOne(wrapper);
    }
}
