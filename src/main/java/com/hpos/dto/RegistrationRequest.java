package com.hpos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 挂号请求 DTO
 * <p>
 * 接收前端提交挂号时传入的参数
 * </p>
 */
@Data
public class RegistrationRequest {

    // ========== 患者信息 ==========

    /** 患者姓名 */
    @NotBlank(message = "患者姓名不能为空")
    private String patientName;

    /** 身份证号 */
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 性别：1=男，2=女 */
    @NotNull(message = "性别不能为空")
    private Integer gender;

    // ========== 挂号信息 ==========

    /** 科室ID */
    @NotNull(message = "请选择科室")
    private Integer deptId;

    /** 医生ID */
    @NotNull(message = "请选择医生")
    private Integer doctorId;

    /** 号源ID */
    @NotNull(message = "号源无效")
    private Integer sourceId;

    /** 就诊日期 (yyyy-MM-dd) */
    @NotBlank(message = "请选择就诊日期")
    private String workDate;

    /** 时段：1=上午，2=下午 */
    @NotNull(message = "请选择时段")
    private Integer period;
}
