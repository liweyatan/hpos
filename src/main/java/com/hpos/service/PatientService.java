package com.hpos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpos.entity.Patient;

/**
 * 患者 Service 接口
 */
public interface PatientService extends IService<Patient> {

    /**
     * 根据手机号查找患者
     *
     * @param phone 手机号
     * @return 患者信息（未找到返回 null）
     */
    Patient findByPhone(String phone);
}
