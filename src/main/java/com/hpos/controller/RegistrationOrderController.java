package com.hpos.controller;

import com.hpos.common.PageResult;
import com.hpos.dto.ApiResponse;
import com.hpos.dto.RegistrationRequest;
import com.hpos.dto.RegistrationVO;
import com.hpos.service.RegistrationOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 挂号订单 API —— 前端的「提交挂号」「我的挂号」「取消挂号」都走这里
 * 
 * <h3>接口列表：</h3>
 * <pre>
 * POST   /api/orders              → 提交挂号（核心接口）
 * GET    /api/orders?patientId=1  → 查询挂号记录
 * PUT    /api/orders/{id}/cancel  → 取消挂号
 * </pre>
 * 
 * <h3>前端调用示例（axios）：</h3>
 * <pre>
 * // 挂号
 * axios.post('/api/orders', {
 *   patientName: '张三', deptId: 1, doctorId: 1, sourceId: 1, ...
 * })
 * 
 * // 查记录
 * axios.get('/api/orders', { params: { patientId: 1 } })
 * 
 * // 取消
 * axios.put('/api/orders/1/cancel', null, { params: { patientId: 1 } })
 * </pre>
 */
@RestController
@RequestMapping("/api/orders")
public class RegistrationOrderController {

    @Autowired
    private RegistrationOrderService orderService;

    /**
     * 提交挂号
     * 
     * <h4>请求体示例：</h4>
     * <pre>
     * {
     *   "patientName": "张三",
     *   "idCard": "110101199001011234",
     *   "phone": "13800138001",
     *   "gender": 1,
     *   "deptId": 1,
     *   "doctorId": 1,
     *   "sourceId": 1,
     *   "workDate": "2026-06-02",
     *   "period": 1
     * }
     * </pre>
     * 
     * <h4>返回示例：</h4>
     * <pre>
     * { "code": 200, "message": "挂号成功", "data": "REG20260601000001" }
     * </pre>
     * 
     * <h4>可能错误：</h4>
     * <ul>
     *   <li>400 - 参数校验失败（如手机号为空）</li>
     *   <li>500 - 号源已满 / 号源不存在</li>
     * </ul>
     * 
     * @param request 挂号参数（@Valid 会自动校验 NotBlank 等约束）
     * @return 订单号（前端可展示给用户作为凭证）
     */
    @PostMapping
    public ApiResponse<String> createOrder(@Valid @RequestBody RegistrationRequest request) {
        String orderNo = orderService.createOrder(request);
        return ApiResponse.success("挂号成功", orderNo);
    }

    /**
     * 查询挂号记录
     * 
     * <h4>请求示例：</h4>
     * GET /api/orders?patientId=1
     * 
     * <h4>返回说明：</h4>
     * status 字段含义：0=待支付 1=已支付 2=已取消 3=已就诊
     * 同时 statusText 字段会附带中文描述，前端直接展示即可
     * 
     * @param patientId 患者ID（目前写死为1，后续接入登录后从 token 获取）
     */
    @GetMapping
    public ApiResponse<PageResult<RegistrationVO>> getOrders(
            @RequestParam Integer patientId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        PageResult<RegistrationVO> result = orderService.getPatientOrders(patientId, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 取消挂号
     * 
     * <h4>请求示例：</h4>
     * PUT /api/orders/1/cancel?patientId=1
     * 
     * <h4>说明：</h4>
     * 取消后该时段的号源会恢复（available_count + 1），
     * 其他用户就可以预约这个被释放的号了。
     * 
     * <h4>限制：</h4>
     * <ul>
     *   <li>只能取消自己的订单（通过 patientId 校验）</li>
     *   <li>已就诊(status=3)的订单不能取消</li>
     *   <li>已取消(status=2)的订单不能重复取消</li>
     * </ul>
     * 
     * @param id        订单ID（URL路径参数）
     * @param patientId 患者ID（查询参数，用于权限验证）
     */
    @PutMapping("/{id}/cancel")
    public ApiResponse<String> cancelOrder(@PathVariable Integer id, @RequestParam Integer patientId) {
        orderService.cancelOrder(id, patientId);
        return ApiResponse.success("取消成功");
    }
}
