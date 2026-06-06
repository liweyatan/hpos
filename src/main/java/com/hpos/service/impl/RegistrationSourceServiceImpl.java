package com.hpos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.entity.Doctor;
import com.hpos.entity.Department;
import com.hpos.entity.RegistrationSource;
import com.hpos.mapper.RegistrationSourceMapper;
import com.hpos.service.DepartmentService;
import com.hpos.service.DoctorService;
import com.hpos.service.RegistrationSourceService;
import com.hpos.dto.DoctorScheduleVO;
import com.hpos.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 号源 Service 实现 —— 管理医生每天的出诊号和剩余数量
 * 
 * <h3>核心逻辑：</h3>
 * <ul>
 *   <li>getDoctorSchedule - 前端选医生时，展示未来7天的可预约时段</li>
 *   <li>deductSource - 用户点"挂号"时扣一个号（高并发注意超卖）</li>
 *   <li>restoreSource - 取消挂号时把号还回去</li>
 * </ul>
 * 
 * <h3>号源表设计说明：</h3>
 * 一个医生一天有上午(period=1)和下午(period=2)两个时段，
 * 每个时段有 total_count（总数）和 available_count（剩余数）。
 * 每次挂号 available_count - 1，取消 + 1。
 */
@Service
public class RegistrationSourceServiceImpl
        extends ServiceImpl<RegistrationSourceMapper, RegistrationSource>
        implements RegistrationSourceService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationSourceServiceImpl.class);
    private static final String LOCK_KEY_PREFIX = "hpos:lock:source:";
    private static final long LOCK_TTL = 10;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RegistrationSourceMapper registrationSourceMapper;

    /**
     * 查询医生的排班和号源（供前端选择时段使用）
     * 
     * 这个方法会把一个医生未来 N 天的号源全部查出来，
     * 并拼上医生姓名、职称、科室名称等前端展示需要的数据。
     * 
     * 调用场景：用户选择了科室 → 选择了医生 → 看到该医生未来7天的排班
     * 
     * @param doctorId  医生ID（不能为空）
     * @param startDate 开始日期（默认当天）
     * @param endDate   结束日期（默认7天后）
     * @return 排班列表，包含时段、剩余号数、费用等；没有排班的日子不会出现在列表中
     */
    @Override
    public List<DoctorScheduleVO> getDoctorSchedule(Integer doctorId, LocalDate startDate, LocalDate endDate) {
        // 先查医生，查不到直接报错（防止传了不存在的 doctorId）
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor == null) {
            throw new RuntimeException("医生不存在");
        }

        // 查出科室名称（医生表里只存了 deptId，需要关联 department 表获取中文名）
        Department dept = departmentService.getById(doctor.getDeptId());
        String deptName = dept != null ? dept.getDeptName() : "";

        // 查询该医生在日期范围内状态正常的号源，按日期和时段排序
        LambdaQueryWrapper<RegistrationSource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegistrationSource::getDoctorId, doctorId)
               .eq(RegistrationSource::getStatus, 1)          // 1=可预约
               .ge(RegistrationSource::getWorkDate, startDate)
               .le(RegistrationSource::getWorkDate, endDate)
               .orderByAsc(RegistrationSource::getWorkDate, RegistrationSource::getPeriod);

        List<RegistrationSource> sources = this.list(wrapper);

        // 把实体转成 VO（只返回前端需要的数据，隐藏不需要的内部字段）
        List<DoctorScheduleVO> voList = new ArrayList<>();
        for (RegistrationSource rs : sources) {
            DoctorScheduleVO vo = new DoctorScheduleVO();
            vo.setSourceId(rs.getId());
            vo.setWorkDate(rs.getWorkDate());
            vo.setPeriod(rs.getPeriod());
            // 数字时段转中文：1→"上午"，2→"下午"
            vo.setPeriodText(rs.getPeriod() == 1 ? "上午" : "下午");
            vo.setTotalCount(rs.getTotalCount());
            vo.setAvailableCount(rs.getAvailableCount());
            vo.setFee(rs.getFee());
            vo.setStatus(rs.getStatus());
            vo.setDoctorId(doctor.getId());
            vo.setDoctorName(doctor.getRealName());
            vo.setDoctorTitle(doctor.getTitle());
            vo.setSpecialty(doctor.getSpecialty());
            vo.setAvatar(doctor.getAvatar());
            vo.setDeptId(doctor.getDeptId());
            vo.setDeptName(deptName);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 扣减号源 —— 用户挂号时调用
     * 
     * 使用 Redis 分布式锁 + 原子 SQL 防止超卖：
     * 1. 先尝试获取 Redis 锁（防止并发扣减）
     * 2. 用原子 SQL 扣减（数据库层面保证）
     * 3. 释放 Redis 锁
     * 
     * @param sourceId 号源ID
     * @return true=扣减成功，false=号源已满
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deductSource(Integer sourceId) {
        return tryRedisLock(sourceId);
    }

    private boolean tryRedisLock(Integer sourceId) {
        String lockKey = LOCK_KEY_PREFIX + sourceId;
        boolean locked = redisCacheService.tryLock(lockKey, LOCK_TTL);
        if (!locked) {
            log.warn("获取Redis锁失败，有其他线程正在操作此号源: sourceId={}", sourceId);
            return false;
        }
        try {
            return doDeduct(sourceId);
        } finally {
            redisCacheService.unlock(lockKey);
        }
    }

    private boolean doDeduct(Integer sourceId) {
        int rows = registrationSourceMapper.deductSourceAtomic(sourceId);
        if (rows > 0) {
            return true;
        }
        log.warn("号源已满: sourceId={}", sourceId);
        return false;
    }

    /**
     * 恢复号源 —— 取消挂号时调用
     * 
     * 用原子 SQL 恢复，确保不超过总号数
     * 
     * @param sourceId 号源ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreSource(Integer sourceId) {
        int rows = registrationSourceMapper.restoreSourceAtomic(sourceId);
        if (rows == 0) {
            RegistrationSource source = this.getById(sourceId);
            if (source == null) {
                throw new RuntimeException("号源不存在");
            }
            if (source.getAvailableCount() >= source.getTotalCount()) {
                throw new RuntimeException("号源已满，无法恢复");
            }
        }
    }
}
