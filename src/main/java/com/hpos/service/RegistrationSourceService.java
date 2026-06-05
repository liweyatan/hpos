package com.hpos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpos.entity.RegistrationSource;
import com.hpos.dto.DoctorScheduleVO;
import java.time.LocalDate;
import java.util.List;

/**
 * 号源 Service 接口
 */
public interface RegistrationSourceService extends IService<RegistrationSource> {

    /**
     * 查询某医生在指定日期范围内的号源
     *
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 排班列表
     */
    List<DoctorScheduleVO> getDoctorSchedule(Integer doctorId, LocalDate startDate, LocalDate endDate);

    /**
     * 扣减号源（挂号时调用）
     *
     * @param sourceId 号源ID
     * @return true=扣减成功，false=号源不足
     */
    boolean deductSource(Integer sourceId);

    /**
     * 恢复号源（取消挂号时调用）
     *
     * @param sourceId 号源ID
     */
    void restoreSource(Integer sourceId);
}
