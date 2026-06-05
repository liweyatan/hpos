package com.hpos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 医生排班视图对象（VO）
 * <p>
 * 用于前端展示医生的排班和号源信息
 * 对应 SQL 中的 v_doctor_schedule 视图
 * </p>
 */
@Data
public class DoctorScheduleVO {

    /** 号源ID */
    private Integer sourceId;

    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    /** 时段：1=上午，2=下午 */
    private Integer period;

    /** 时段文字（上午/下午） */
    private String periodText;

    /** 总号数 */
    private Integer totalCount;

    /** 剩余号数 */
    private Integer availableCount;

    /** 挂号费用 */
    private BigDecimal fee;

    /** 号源状态 */
    private Integer status;

    /** 医生ID */
    private Integer doctorId;

    /** 医生姓名 */
    private String doctorName;

    /** 医生职称 */
    private String doctorTitle;

    /** 医生擅长 */
    private String specialty;

    /** 医生头像 */
    private String avatar;

    /** 科室ID */
    private Integer deptId;

    /** 科室名称 */
    private String deptName;
}
