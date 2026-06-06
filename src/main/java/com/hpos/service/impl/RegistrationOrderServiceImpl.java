package com.hpos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.common.PageResult;
import com.hpos.entity.*;
import com.hpos.mapper.RegistrationOrderMapper;
import com.hpos.service.*;
import com.hpos.dto.RegistrationRequest;
import com.hpos.dto.RegistrationVO;
import com.hpos.common.OrderNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 挂号订单 Service 实现 —— 整个系统的核心业务所在
 * 
 * <h3>三个核心操作：</h3>
 * <ul>
 *   <li>createOrder - 挂号（扣号源 + 创建订单，事务保证一致性）</li>
 *   <li>cancelOrder - 取消挂号（恢复号源 + 更新订单状态）</li>
 *   <li>getPatientOrders - 查询挂号记录（关联医生、科室、状态转换）</li>
 * </ul>
 * 
 * <h3>并发注意：</h3>
 * 扣减号源时使用"先查后更"的乐观锁方式。高并发场景下，
 * 两个线程可能同时查到 available_count=1，然后都扣减为 0，
 * 导致超卖。生产环境应改为 SQL 原子更新：
 * {@code UPDATE registration_source SET available_count = available_count - 1
 *  WHERE id = ? AND available_count > 0}，并检查受影响行数。
 */
@Service
public class RegistrationOrderServiceImpl
        extends ServiceImpl<RegistrationOrderMapper, RegistrationOrder>
        implements RegistrationOrderService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationOrderServiceImpl.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RegistrationSourceService sourceService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private OrderNotificationService notificationService;

    /**
     * 创建挂号订单 —— 核心业务，包含完整的事务控制
     * 
     * <h4>业务流程：</h4>
     * <ol>
     *   <li>根据手机号查找患者，不存在则自动创建（便民设计）</li>
     *   <li>扣减号源：如果号源已满则抛出异常，整个事务回滚</li>
     *   <li>生成订单号（格式：REG + 日期 + 6位序号）</li>
     *   <li>保存订单，默认状态为"待支付"</li>
     * </ol>
     * 
     * <h4>事务说明：</h4>
     * 整个方法在一个事务中执行，任意一步失败都会回滚全部操作。
     * 如果扣号源成功但保存订单失败，号源会自动恢复。
     * 
     * <h4>常见报错：</h4>
     * <ul>
     *   <li>"号源已满" - 该时段已被约满，请选其他时段</li>
     *   <li>"号源不存在" - sourceId 非法，前端的排班数据可能过期</li>
     * </ul>
     * 
     * @param request 挂号请求（已通过 @Valid 校验过字段）
     * @return 生成的订单号（前端可展示给用户）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    // rollbackFor = Exception.class 表示任何异常都回滚，包括 RuntimeException
    public String createOrder(RegistrationRequest request) {
        // ===== 第1步：查找或创建患者 =====
        // 用手机号做唯一标识：同一个手机号多次挂号，共用一条患者记录
        Patient patient = patientService.findByPhone(request.getPhone());
        if (patient == null) {
            patient = new Patient();
            patient.setRealName(request.getPatientName());
            patient.setPhone(request.getPhone());
            patient.setIdCard(request.getIdCard());
            patient.setGender(request.getGender());
            patientService.save(patient);
            // save 后 patient.getId() 会自动回填（因为主键是自增的）
        }

        // ===== 第2步：扣减号源 =====
        // 这一步必须在创建订单之前完成，避免号源被超卖
        boolean deducted = sourceService.deductSource(request.getSourceId());
        if (!deducted) {
            // 注意：这里抛出异常后，第1步新建的患者也会被回滚（事务）
            throw new RuntimeException("号源已满，请选择其他时段");
        }

        // ===== 第3步：查询号源信息（主要是获取 fee 费用字段） =====
        RegistrationSource source = sourceService.getById(request.getSourceId());
        if (source == null) {
            throw new RuntimeException("号源不存在");
        }

        // ===== 第4步：创建订单 =====
        RegistrationOrder order = new RegistrationOrder();
        order.setOrderNo(OrderNoGenerator.generate());
        order.setPatientId(patient.getId());
        order.setSourceId(request.getSourceId());
        order.setDoctorId(request.getDoctorId());
        order.setDeptId(request.getDeptId());
        order.setWorkDate(LocalDate.parse(request.getWorkDate()));
        order.setPeriod(request.getPeriod());
        order.setFee(source.getFee());
        order.setStatus(0); // 0=待支付
        order.setPatientName(patient.getRealName());
        this.save(order);

        try {
            Doctor doctor = doctorService.getById(request.getDoctorId());
            notificationService.sendOrderSuccessNotification(
                    order.getOrderNo(),
                    patient.getRealName(),
                    doctor != null ? doctor.getRealName() : "",
                    request.getWorkDate(),
                    request.getPeriod() == 1 ? "上午" : "下午"
            );
        } catch (Exception e) {
            log.warn("发送通知失败（不影响主流程）: {}", e.getMessage());
        }

        return order.getOrderNo();
    }

    /**
     * 查询患者挂号记录（带关联数据的视图）
     * 
     * 这个方法会关联查询医生和科室信息，把数据库里的数字状态
     * 转成前端能直接展示的中文描述。
     * 
     * <h4>状态映射：</h4>
     * <pre>
     * 0 → "待支付"（刚挂号，未付款）
     * 1 → "已支付"（已付款，等待就诊）
     * 2 → "已取消"（用户主动取消）
     * 3 → "已就诊"（医生已完成接诊）
     * </pre>
     * 
     * @param patientId 患者ID
     * @return 挂号记录列表（按就诊日期倒序，最新的在最上面）
     */
    @Override
    public PageResult<RegistrationVO> getPatientOrders(Integer patientId, int page, int size) {
        LambdaQueryWrapper<RegistrationOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegistrationOrder::getPatientId, patientId)
               .orderByDesc(RegistrationOrder::getWorkDate)
               .orderByDesc(RegistrationOrder::getCreateTime);

        IPage<RegistrationOrder> pageResult = this.page(new Page<>(page, size), wrapper);

        List<RegistrationVO> voList = new ArrayList<>();
        for (RegistrationOrder order : pageResult.getRecords()) {
            voList.add(buildVO(order));
        }

        return new PageResult<>(voList, pageResult.getTotal(), (int) pageResult.getCurrent(),
                (int) pageResult.getSize(), pageResult.getPages());
    }

    private RegistrationVO buildVO(RegistrationOrder order) {
        RegistrationVO vo = new RegistrationVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setPatientName(order.getPatientName());
        vo.setWorkDate(order.getWorkDate());
        vo.setPeriodText(order.getPeriod() == 1 ? "上午" : "下午");
        vo.setFee(order.getFee());
        vo.setStatus(order.getStatus());
        vo.setCreateTime(order.getCreateTime());

        String statusText = switch (order.getStatus()) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已取消";
            case 3 -> "已就诊";
            default -> "未知";
        };
        vo.setStatusText(statusText);

        Doctor doctor = doctorService.getById(order.getDoctorId());
        if (doctor != null) {
            vo.setDoctorName(doctor.getRealName());
            vo.setDoctorTitle(doctor.getTitle());
            Department dept = departmentService.getById(doctor.getDeptId());
            vo.setDeptName(dept != null ? dept.getDeptName() : "");
        }
        return vo;
    }

    /**
     * 取消挂号订单
     * 
     * <h4>取消流程：</h4>
     * <ol>
     *   <li>校验订单是否属于当前用户（防止恶意取消他人订单）</li>
     *   <li>校验状态：已取消(2)不能重复取消，已就诊(3)不能取消</li>
     *   <li>恢复号源（available_count + 1）</li>
     *   <li>更新订单状态为"已取消"</li>
     * </ol>
     * 
     * <h4>不能取消的情况：</h4>
     * <ul>
     *   <li>订单已取消（status=2）—— 防重复操作</li>
     *   <li>已就诊（status=3）—— 已经看完了，不能退</li>
     *   <li>订单不属于当前用户 —— 盗用他人订单号也无权取消</li>
     * </ul>
     * 
     * @param orderId   订单ID
     * @param patientId 当前登录的患者ID（用于权限校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Integer orderId, Integer patientId) {
        RegistrationOrder order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 权限校验：只能取消自己的订单
        if (!order.getPatientId().equals(patientId)) {
            throw new RuntimeException("无权取消此订单");
        }
        // 状态校验：已取消的不能重复取消
        if (order.getStatus() == 2) {
            throw new RuntimeException("订单已取消，请勿重复操作");
        }
        // 状态校验：已就诊的不能取消
        if (order.getStatus() == 3) {
            throw new RuntimeException("已就诊的订单无法取消");
        }

        // 恢复号源：把之前扣掉的 available_count 加回来
        sourceService.restoreSource(order.getSourceId());

        // 更新订单状态
        order.setStatus(2);
        this.updateById(order);
    }
}
