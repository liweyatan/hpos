package com.hpos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 挂号记录视图对象（VO）
 * <p>
 * 用于前端展示患者的挂号历史记录
 * </p>
 */
@Data
public class RegistrationVO {

    /** 订单ID */
    private Integer id;

    /** 订单号 */
    private String orderNo;

    /** 患者姓名 */
    private String patientName;

    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    /** 时段文字（上午/下午） */
    private String periodText;

    /** 挂号费用 */
    private BigDecimal fee;

    /**
     * 订单状态
     * 0=待支付 1=已支付 2=已取消 3=已就诊
     */
    private Integer status;

    /** 状态中文描述 */
    private String statusText;

    /** 医生姓名 */
    private String doctorName;

    /** 医生职称 */
    private String doctorTitle;

    /** 科室名称 */
    private String deptName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
