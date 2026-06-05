package com.hpos.controller;

import com.hpos.dto.ApiResponse;
import com.hpos.dto.DoctorScheduleVO;
import com.hpos.service.RegistrationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 号源/排班 API —— 前端选完医生后，查该医生哪天有号
 * 
 * <h3>接口：</h3>
 * <pre>
 * GET /api/sources/schedule?doctorId=1&startDate=2026-06-01&endDate=2026-06-07
 * </pre>
 * 
 * <h3>前端使用流程：</h3>
 * <ol>
 *   <li>用户选科室 → 调 /api/doctors?deptId=xxx</li>
 *   <li>用户选医生 → 调 /api/sources/schedule?doctorId=xxx</li>
 *   <li>展示可预约的日期和时段，用户选择 → 提交挂号</li>
 * </ol>
 * 
 * <h3>返回数据中的关键字段：</h3>
 * <ul>
 *   <li>sourceId - 号源ID（提交挂号时要回传）</li>
 *   <li>workDate - 日期（如 2026-06-02）</li>
 *   <li>periodText - "上午" 或 "下午"</li>
 *   <li>availableCount - 剩余号数（0 表示已约满）</li>
 *   <li>fee - 挂号费（前端展示用）</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/sources")
public class RegistrationSourceController {

    @Autowired
    private RegistrationSourceService sourceService;

    /**
     * 查询医生排班
     * 
     * <h4>请求示例：</h4>
     * GET /api/sources/schedule?doctorId=1
     * （不传日期则默认查当天起未来7天）
     * 
     * GET /api/sources/schedule?doctorId=1&startDate=2026-06-01&endDate=2026-06-07
     * 
     * <h4>返回示例：</h4>
     * <pre>
     * [
     *   {
     *     "sourceId": 1,
     *     "workDate": "2026-06-02",
     *     "period": 1,
     *     "periodText": "上午",
     *     "totalCount": 20,
     *     "availableCount": 15,
     *     "fee": 15.00,
     *     "doctorName": "张明",
     *     "doctorTitle": "主任医师",
     *     "deptName": "内科"
     *   }
     * ]
     * </pre>
     * 
     * @param doctorId  医生ID（必传）
     * @param startDate 开始日期（可选，默认今天）
     * @param endDate   结束日期（可选，默认7天后）
     */
    @GetMapping("/schedule")
    public ApiResponse<List<DoctorScheduleVO>> getSchedule(
            @RequestParam Integer doctorId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // 默认查询范围：当天 ~ 7天后
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(7);
        }

        List<DoctorScheduleVO> schedule = sourceService.getDoctorSchedule(doctorId, startDate, endDate);
        return ApiResponse.success(schedule);
    }
}
